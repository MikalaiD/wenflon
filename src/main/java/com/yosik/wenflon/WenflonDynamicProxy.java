package com.yosik.wenflon;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class WenflonDynamicProxy<T> {
  private final Map<Implementation, Predicate<String>> implementationCases;
  @Getter private final Class<T> representedInterface;
  private PivotProvider<?> pivotProvider;

  @Getter private final T wenflonProxy;

  WenflonDynamicProxy(final Class<T> representedInterface) {
    this.representedInterface = representedInterface;
    implementationCases = new HashMap<>();
    wenflonProxy = createProxy();
  }

  WenflonDynamicProxy<T> addImplementation(
      final Object bean, final String beanName, final Predicate<String> condition) {
    final var implementation = new Implementation(bean, beanName);
    implementationCases.put(implementation, condition);
    return this;
  }

  private T createProxy() {
    return representedInterface.cast(
        Proxy.newProxyInstance(
            WenflonDynamicProxy.class.getClassLoader(),
            new Class<?>[] {representedInterface, com.yosik.wenflon.Proxy.class},
            (proxy, method, args) -> method.invoke(defineImplementation(), args)));
  }

  private Object defineImplementation() {
    if (implementationCases.size() == 1) {
      return implementationCases.keySet().stream().map(Implementation::getBean).findFirst().get();
    }
    return implementationCases.entrySet().stream()
        .filter(
            implementation ->
                implementation
                    .getValue()
                    .test((String) pivotProvider.getPivot())) // todo ugly temp cast
        .map(Map.Entry::getKey)
        .map(Implementation::getBean)
        .findFirst()
        .orElseThrow(); // todo come up with a better exception
  }

  public String getName() {
    return String.format("wenflon-%s", this.representedInterface.getSimpleName());
  }

  public String getProxyName() {
    return String.format("wproxy-%s", this.representedInterface.getSimpleName());
  }

  void addConditions(final WenflonProperties properties) {
    implementationCases.entrySet().stream()
        .filter(
            entry ->
                Optional.ofNullable(properties.getConditions())
                    .map(
                        conditions ->
                            conditions.keySet().stream()
                                .anyMatch(name -> name.equals(entry.getKey().getBeanName())))
                    .orElse(false))
        .forEach(
            entry ->
                implementationCases.put(
                    entry.getKey(),
                    s ->
                        properties
                            .getConditions()
                            .get(entry.getKey().getBeanName())
                            .contains(s)));
  }

  void addPivotProvider(final List<PivotProvider<?>> pivotProviders) {
    // todo only one - first best - is supported now
    this.pivotProvider = pivotProviders.stream().findFirst().orElseThrow();
  }

  @AllArgsConstructor
  @EqualsAndHashCode(of = "bean")
  @Getter
  private static class Implementation {
    private final Object bean;
    private final String beanName;
  }
}
