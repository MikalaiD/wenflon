package com.yosik.wenflon;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class WenflonDynamicProxy<T> {
  private final Map<Object, Predicate<String>> implementations;
  @Getter private final Class<T> representedInterface;
  private PivotProvider<?> pivotProvider;

  @Getter private final T proxy;

  WenflonDynamicProxy(final Class<T> representedInterface) {
    this.representedInterface = representedInterface;
    implementations = new HashMap<>();
    proxy = createProxy();
  }

  WenflonDynamicProxy<T> addImplementation(final Object bean, final Predicate<String> condition) {
    implementations.put(bean, condition);
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
    if (implementations.size() == 1) {
      return implementations.keySet().stream().findFirst().get();
    }
    return implementations.entrySet().stream()
        .filter(
            implementation ->
                implementation
                    .getValue()
                    .test((String) pivotProvider.getPivot())) // todo ugly temp cast
        .map(Map.Entry::getKey)
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
    implementations.entrySet().stream()
        .filter(
            entry ->
                Optional.ofNullable(properties.getConditions())
                    .map(
                        conditions ->
                            conditions.keySet().stream()
                                .anyMatch(name -> name.equals(extractClassName(entry))))
                    .orElse(false)) // todo ensure these are bean names, maybe to preserve
        // bean names? here temp uncapitalizing
        .forEach(
            entry ->
                implementations.put(
                    entry.getKey(),
                    s ->
                        properties
                            .getConditions()
                            .get(extractClassName(entry)) // todo same here
                            .contains(s)));
  }

  private static String extractClassName(Map.Entry<Object, Predicate<String>> entry) {
    return StringUtils.uncapitalize(entry.getKey().getClass().getSimpleName());
  }

  void addPivotProvider(final List<PivotProvider<?>> pivotProviders) {
    // todo only one - first best - is supported now
    this.pivotProvider = pivotProviders.stream().findFirst().orElseThrow();
  }
}
