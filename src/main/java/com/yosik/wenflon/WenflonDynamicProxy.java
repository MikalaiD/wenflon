package com.yosik.wenflon;

import lombok.Getter;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WenflonDynamicProxy<T> {
  private final Map<Object, Predicate<String>> implementations;
  @Getter private final Class<T> representedInterface;
  private final Supplier<Map<String, Supplier<?>>> pivotProvider;

  @Getter private final T proxy;

  WenflonDynamicProxy(
      final Class<T> representedInterface,
      final Supplier<Map<String, Supplier<?>>> pivotProvidersMap) {
    this.representedInterface = representedInterface;
    implementations = new HashMap<>();
    proxy = createProxy();
    this.pivotProvider = pivotProvidersMap;
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
    final var pivot =
        pivotProvider
            .get()
            .getOrDefault(
                "defaultPivotProvider",
                () -> {
                  throw new RuntimeException("No pivot provider of a given name");
                })
            .get();
    return pivot.equals("panda")
        ? implementations.keySet().stream()
            .filter(aClass -> aClass.getClass().getSimpleName().contains("ServiceA")) // todo temp
            .findFirst()
            .get()
        : implementations.keySet().stream()
            .filter(aClass -> aClass.getClass().getSimpleName().contains("ServiceB"))
            .findFirst()
            .get();
  }

  public String getName() {
    return String.format("wenflon-%s", this.representedInterface.getSimpleName());
  }
}
