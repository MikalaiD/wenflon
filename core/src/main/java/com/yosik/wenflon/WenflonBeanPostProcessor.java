package com.yosik.wenflon;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Slf4j
public class WenflonBeanPostProcessor
    implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

  private final WenflonRegistry wenflonRegistry = new WenflonRegistry();
  private final Map<Class<?>, Set<String>> wenflonInterfacesToBeanNames = new HashMap<>();
  private final Set<String> pivotProviderBeanNames = new HashSet<>();

  @Override
  public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry)
      throws BeansException {
    identifyWenflonAnnotatedInterfaces(registry); //todo split into 2 classes?
    identifyPivotProviders(registry);
    registerWenflonDynamicProxyForEachWenflonAnnotatedInterface(registry);
    registerPivotProviders(registry);
  }


  private void identifyPivotProviders(final BeanDefinitionRegistry registry) {
    Arrays.stream(registry.getBeanDefinitionNames())
            .filter(isPivotProvider(registry))
            .forEach(pivotProviderBeanNames::add);
  }

  private static Predicate<String> isPivotProvider(final BeanDefinitionRegistry registry) {
    return name -> {
      final var classByBeanName = getClassByBeanName(registry, name);
      return classByBeanName.isPresent() && classByBeanName.get().equals(PivotProvider.class);
    };
  }

  private void identifyWenflonAnnotatedInterfaces(final BeanDefinitionRegistry registry) {
    wenflonInterfacesToBeanNames.putAll(
        Arrays.stream(registry.getBeanDefinitionNames())//todo maybe beandefinitionmap is better and can simplify a LOT of code
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
        .filter(WenflonBeanPostProcessor::filterAndLogInappropriateObjects)
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
  public Object postProcessBeforeInitialization(
      @NonNull final Object bean, @NonNull final String beanName) throws BeansException {
    //todo here we can just strip off @Primary from the wenflon eligible beans
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
  public Object postProcessAfterInitialization(
      @NonNull final Object bean, @NonNull final String beanName) throws BeansException {
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

  private void registerPivotProviders(final BeanDefinitionRegistry registry) {
    pivotProviderBeanNames.forEach(
            name-> registerPivotProviderCaseBean(name, registry)
    );

  }

  private void registerPivotProviderCaseBean(final String name, final BeanDefinitionRegistry registry) {
    GenericBeanDefinition pivotProviderBeanDefinition = new GenericBeanDefinition();
    pivotProviderBeanDefinition.setBeanClass(PivotProviderWrapper.class);
    pivotProviderBeanDefinition.setDependsOn(name);
    ConstructorArgumentValues args = new ConstructorArgumentValues();
    args.addGenericArgumentValue(new RuntimeBeanReference(name));
    args.addGenericArgumentValue(name);
    pivotProviderBeanDefinition.setConstructorArgumentValues(args);
    registry.registerBeanDefinition(String.format("p-%s", name), pivotProviderBeanDefinition);
  }


  private static List<Map.Entry<String, Class<?>>> toEntries(
      final BeanDefinitionRegistry registry, final String name) {
    final var beanClassOptional = getClassByBeanName(registry, name);
    return beanClassOptional
        .map(
            aClass ->
                extractWenflonInterfaces(aClass).stream()
                    .<Map.Entry<String, Class<?>>>map(
                        wenflonAnnotatedInterface -> Map.entry(name, wenflonAnnotatedInterface))
                    .toList())
        .orElseGet(List::of);
  }

  @SneakyThrows
  private static Optional<Class<?>> getClassByBeanName(
      final BeanDefinitionRegistry registry, final String name) {
    final var beanDefinition = registry.getBeanDefinition(name);
    if (beanDefinition.getBeanClassName() != null) {
      return Optional.of(Class.forName(beanDefinition.getBeanClassName()));
    }
    if (beanDefinition.getSource() instanceof StandardMethodMetadata standardMethodMetadata) {
      return Optional.of(
          Class.forName(
                  standardMethodMetadata
                  .getIntrospectedMethod()
                  .getReturnType()
                  .getName()));
    }
    if (beanDefinition.getSource() instanceof Class<?> source) {
      return Optional.of(Class.forName(source.getName()));
    }
    return Optional.empty();
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
        clazz.getInterfaces()) { // todo not sure if this part is needed, maybe sometime bean class
      // won't be resolved as interface but as concrete class impl??
      if (interfaceClass.isAnnotationPresent(annotation)) {
        bucket.add(interfaceClass);
      }
    }
    collectInterfacesInHierarchyAnnotatedWith(clazz.getSuperclass(), bucket, annotation);
  }
}
