package com.yosik.wenflon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class WenflonBeanPostprocessor
    implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

  private final WenflonRegistry wenflonRegistry = new WenflonRegistry();
  private final Set<String> pivotProviderBeanNames = new HashSet<>();

  @Override
  public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry)
      throws BeansException {
    extractPivotProviderBeanName(registry);
    findAnnotatedInterfaces(registry)
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
    if (bean instanceof PivotProvider<?> pivotProvider){
      wenflonRegistry.addPivotProvider(pivotProvider, beanName);
    } else {
      putBehindWendWenflonIfApplicable(bean);
    }
    return bean;
  }

  private void putBehindWendWenflonIfApplicable(Object bean) {
    Stream.of(bean)
        .filter(aBean -> !(aBean instanceof Proxy))
        .map(Object::getClass)
        .flatMap(aClass -> Arrays.stream(aClass.getInterfaces()))
        .filter(aClass -> aClass.isAnnotationPresent(Wenflon.class)) //todo maybe this step is not needed? can populate some sturcture in the postProcessBeanDefinitionRegistry step and here just filter
        .filter(wenflonRegistry::isWenflonPreparedFor)
        .forEach(
            aClass ->
                wenflonRegistry.putBehindWenflon(
                    aClass,
                        bean,
                    (value) ->
                        true
                            ? value.equals("panda")
                            : value.equals(
                                "grizzly")) // todo temp, come up with passing condition from WenflonList
            );
  }

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName)
      throws BeansException {
    return bean;
  }

  private void registerWenflonDynamicProxyAsPrimaryBean(
      final BeanDefinitionRegistry registry, final Map.Entry<Class<?>, List<String>> wenflonCase) {
    final var aClass = wenflonCase.getKey();
    final var userDefinedBeanDefinitionsNames = wenflonCase.getValue().toArray(new String[] {});
    WenflonDynamicProxy<?> wenflon = wenflonRegistry.createAndRegisterWenflonProxy(aClass);
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(aClass);
    beanDefinition.setDependsOn(userDefinedBeanDefinitionsNames);
    beanDefinition.setPrimary(true);
    beanDefinition.setInstanceSupplier(() -> aClass.cast(wenflon.getProxy()));
    registry.registerBeanDefinition(wenflon.getName(), beanDefinition);
  }

  private Map<Class<?>, List<String>> findAnnotatedInterfaces(
      final BeanDefinitionRegistry registry) {
    return Arrays.stream(registry.getBeanDefinitionNames())
        .map(name -> toEntry(registry, name))
        .filter(entry -> isAnnotatedWithWenflon(entry.getValue()))
        .collect(
            Collectors.toMap(
                Map.Entry::getValue,
                stringEntry -> List.of(stringEntry.getKey()),
                (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).toList()));
  }

  private void extractPivotProviderBeanName(final BeanDefinitionRegistry registry) {
    Arrays.stream(registry.getBeanDefinitionNames()) //todo repeated code in findAnnotatedInterfaces
            .map(name -> toEntry(registry, name))
            .filter(entry -> PivotProvider.class.isAssignableFrom(entry.getValue()))
            .map(Map.Entry::getKey)
            .forEach(pivotProviderBeanNames::add); //todo probably no need to save to the list
    if (pivotProviderBeanNames.isEmpty()){
      throw new RuntimeException("At least one bean implementing PivotProvider should be present in context");
    }
  }

  private static Map.Entry<String, ? extends Class<?>> toEntry(
      final BeanDefinitionRegistry registry, final String name) {
    return Map.entry(
        name,
        Objects.requireNonNull(registry.getBeanDefinition(name).getResolvableType().getRawClass()));
  }

  private static boolean isAnnotatedWithWenflon(final Class<?> clazz) {
    return Optional.ofNullable(clazz)
        .map(aClass -> aClass.isAnnotationPresent(Wenflon.class))
        .orElse(false);
  }
}
