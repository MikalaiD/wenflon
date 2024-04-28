package com.yosik.wenflon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

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

  private void registerWenflonDynamicProxyForEachWenflonAnnotatedInterface(
      BeanDefinitionRegistry registry) {
    this.wenflonInterfacesToBeanNames
        .entrySet()
        .forEach(
            interfaceAnnotatedWithWenflon ->
                registerWenflonDynamicProxyAsPrimaryBean(registry, interfaceAnnotatedWithWenflon));
  }

  @Override
  public Object postProcessBeforeInitialization(final Object bean, final String beanName)
      throws BeansException {
    // here we can just strip off @Primary from the wenflon eligible beans
    // here we assume class will implement only one interface under wenflon
    if (!(bean instanceof Proxy) && implementsInterfaceAnnotatedWithWenflon(beanName)) {
      putBehindAppropriateWenflons(bean);
    }
    return bean;
  }

  private boolean implementsInterfaceAnnotatedWithWenflon(final String beanName) {
    return wenflonInterfacesToBeanNames.values().stream()
        .flatMap(Collection::stream)
        .anyMatch(name -> name.equals(beanName));
  }

  private void putBehindAppropriateWenflons(Object bean) {
    Stream.of(bean)
        .map(Object::getClass)
        .flatMap(aClass -> Arrays.stream(aClass.getInterfaces()))
        .filter(aClass -> aClass.isAnnotationPresent(Wenflon.class))
        .filter(wenflonRegistry::isWenflonPreparedFor)
        .forEach(aClass -> wenflonRegistry.putBehindWenflon(aClass, bean));
  }

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName)
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
    wProxyBeanDefinition.setInstanceSupplier(() -> aClass.cast(wenflon.getProxy()));
    registry.registerBeanDefinition(wenflon.getProxyName(), wProxyBeanDefinition);
    // should also register a wenflon as a bean so it is then injected into final assembler
    GenericBeanDefinition wenflonBeanDefinition = new GenericBeanDefinition();
    wenflonBeanDefinition.setBeanClass(WenflonDynamicProxy.class);
    wenflonBeanDefinition.setInstanceSupplier(() -> wenflon);
    registry.registerBeanDefinition(wenflon.getName(), wenflonBeanDefinition);
  }

  private void identifyWenflonAnnotatedInterfaces(final BeanDefinitionRegistry registry) {
    final var wenflonInterfacesToBeanNames =
        Arrays.stream(registry.getBeanDefinitionNames())
            .map(name -> toEntry(registry, name))
            .filter(entry -> isAnnotatedWithWenflon(entry.getValue()))
            .collect(
                Collectors.toMap(
                    Map.Entry::getValue,
                    stringEntry -> Set.of(stringEntry.getKey()),
                    (l1, l2) ->
                        Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toSet())));
    this.wenflonInterfacesToBeanNames.putAll(wenflonInterfacesToBeanNames);
  }

  private static Map.Entry<String, ? extends Class<?>> toEntry(
      final BeanDefinitionRegistry registry, final String name) {
    return Map.entry(name, getClassByBeanName(registry, name));
  }

  private static Class<?> getClassByBeanName(BeanDefinitionRegistry registry, String name) {
    return Objects.requireNonNull(
        registry.getBeanDefinition(name).getResolvableType().getRawClass());
  }

  private static boolean isAnnotatedWithWenflon(final Class<?> clazz) {
    return Optional.ofNullable(clazz)
        .map(aClass -> aClass.isAnnotationPresent(Wenflon.class))
        .orElse(false);
  }
}
