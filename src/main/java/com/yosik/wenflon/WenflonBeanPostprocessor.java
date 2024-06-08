package com.yosik.wenflon;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

@RequiredArgsConstructor
@Slf4j
public class WenflonBeanPostprocessor
    implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

  private final WenflonRegistry wenflonRegistry = new WenflonRegistry();
  private final Map<Class<?>, Set<String>> wenflonInterfacesToBeanNames = new HashMap<>();

  @Override
  public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry)
      throws BeansException {
    identifyWenflonAnnotatedInterfaces(registry);
    registerWenflonDynamicProxyForEachWenflonAnnotatedInterface(registry);
  }

  private void identifyWenflonAnnotatedInterfaces(final BeanDefinitionRegistry registry) {
    wenflonInterfacesToBeanNames.putAll(
        Arrays.stream(registry.getBeanDefinitionNames())
            .flatMap(name -> toEntries(registry, name).stream())
            .collect(
                Collectors.toMap(
                    Map.Entry::getValue,
                    stringEntry -> Set.of(stringEntry.getKey()),
                    (l1, l2) ->
                        Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toSet()))));
  }

  private void registerWenflonDynamicProxyForEachWenflonAnnotatedInterface(
      BeanDefinitionRegistry registry) {
    this.wenflonInterfacesToBeanNames.entrySet().stream()
        .filter(WenflonBeanPostprocessor::filterAndLogInappropriateObjects)
        .forEach(
            interfaceAnnotatedWithWenflon ->
                registerWenflonDynamicProxyAsPrimaryBean(registry, interfaceAnnotatedWithWenflon));
  }

  private static boolean filterAndLogInappropriateObjects(
      final Map.Entry<Class<?>, Set<String>> classSetEntry) {
    if (!classSetEntry.getKey().isInterface()) {
      log.warn(
          "Dynamic proxy can be created only for interface: {} is not an interface.",
          classSetEntry.getKey().getCanonicalName());
      return false;
    }
    return true;
  }

  @Override
  public Object postProcessBeforeInitialization(@NonNull final Object bean, @NonNull final String beanName)
      throws BeansException {
    // here we can just strip off @Primary from the wenflon eligible beans
    // here we assume class will implement only one interface under wenflon
    if (!(bean instanceof Proxy) && implementsInterfaceAnnotatedWithWenflon(beanName)) {
      putBehindAppropriateWenflons(bean, beanName);
    }
    return bean;
  }

  private boolean implementsInterfaceAnnotatedWithWenflon(final String beanName) {
    return wenflonInterfacesToBeanNames.values().stream()
        .flatMap(Collection::stream)
        .anyMatch(name -> name.equals(beanName));
  }

  private void putBehindAppropriateWenflons(final Object bean, final String beanName) {
    Stream.of(bean)
        .map(Object::getClass)
        .flatMap(aClass -> Arrays.stream(aClass.getInterfaces()))
        .filter(aClass -> aClass.isAnnotationPresent(Wenflon.class))
        .filter(wenflonRegistry::isWenflonPreparedFor)
        .forEach(aClass -> wenflonRegistry.putBehindWenflon(aClass, bean, beanName));
  }

  @Override
  public Object postProcessAfterInitialization(@NonNull final Object bean, @NonNull final String beanName)
      throws BeansException {
    return bean;
  }

  private void registerWenflonDynamicProxyAsPrimaryBean(
      final BeanDefinitionRegistry registry, final Map.Entry<Class<?>, Set<String>> wenflonCase) {
    final var aClass = wenflonCase.getKey();
    final var userDefinedBeanDefinitionsNames = wenflonCase.getValue().toArray(new String[] {});
    WenflonDynamicProxy<?> wenflon = wenflonRegistry.createAndRegisterWenflonProxy(aClass);
    GenericBeanDefinition wProxyBeanDefinition = new GenericBeanDefinition();
    wProxyBeanDefinition.setBeanClass(aClass);
    wProxyBeanDefinition.setDependsOn(userDefinedBeanDefinitionsNames);
    wProxyBeanDefinition.setPrimary(true);
    wProxyBeanDefinition.setInstanceSupplier(() -> aClass.cast(wenflon.getWenflonProxy()));
    registry.registerBeanDefinition(wenflon.getProxyName(), wProxyBeanDefinition);
    // should also register a wenflon as a bean so it is then injected into final assembler
    GenericBeanDefinition wenflonBeanDefinition = new GenericBeanDefinition();
    wenflonBeanDefinition.setBeanClass(WenflonDynamicProxy.class);
    wenflonBeanDefinition.setInstanceSupplier(() -> wenflon);
    registry.registerBeanDefinition(wenflon.getName(), wenflonBeanDefinition);
  }

  private static List<Map.Entry<String, Class<?>>> toEntries(
      final BeanDefinitionRegistry registry, final String name) {
    final Class<?> beanClass = getClassByBeanName(registry, name);
    return extractWenflonInterfaces(beanClass).stream()
        .<Map.Entry<String, Class<?>>>map(
            wenflonAnnotatedInterface -> Map.entry(name, wenflonAnnotatedInterface))
        .toList();
  }

  @SneakyThrows
  private static Class<?>
      getClassByBeanName( // FINISHED here - this is a new method but proxy stopped producing itself
      final BeanDefinitionRegistry registry, final String name) {
    final var beanDefinition = registry.getBeanDefinition(name);
    if (Objects.isNull(beanDefinition)) {
      throw new RuntimeException("Not known case"); // todo come up with better exception
    }
    if (beanDefinition.getBeanClassName() != null) {
      return Class.forName(beanDefinition.getBeanClassName());
    } else if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
      return Class.forName(
          ((StandardMethodMetadata) beanDefinition.getSource())
              .getIntrospectedMethod()
              .getReturnType()
              .getName());
    } else if (beanDefinition.getSource() instanceof Class<?>) {
      return Class.forName(((Class<?>) beanDefinition.getSource()).getName());
    }
    throw new RuntimeException("Not known case"); // todo come up with better exception
  }

  private static Collection<Class<?>> extractWenflonInterfaces(final Class<?> beanClass) {
    final Set<Class<?>> output = new HashSet<>();
    if (beanClass == null) {
      return output;
    }
    if (beanClass.isInterface()) {
      collectInterfaceIfAnnotatedWith(beanClass, output, Wenflon.class);
    } else {
      collectInterfacesInHierarchyAnnotatedWith(beanClass, output, Wenflon.class);
    }
    return output;
  }

  private static void collectInterfaceIfAnnotatedWith(
      final Class<?> beanClass,
      final Set<Class<?>> output,
      final Class<? extends Annotation> annotation) {
    if (beanClass.isAnnotationPresent(annotation)) {
      output.add(beanClass);
    }
  }

  private static void collectInterfacesInHierarchyAnnotatedWith(
      final Class<?> clazz,
      final Collection<Class<?>> bucket,
      final Class<? extends Annotation> annotation) {
    if (clazz == null) {
      return;
    }
    for (Class<?> interfaceClass :
        clazz
            .getInterfaces()) { // todo not sure if this part is needed, maybe sometime bean class
                                // won't be resolved as interface but as concrete class impl??
      if (interfaceClass.isAnnotationPresent(annotation)) {
        bucket.add(interfaceClass);
      }
    }
    collectInterfacesInHierarchyAnnotatedWith(clazz.getSuperclass(), bucket, annotation);
  }
}
