package com.yosik.wenflon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WenflonRegistry {

  private final Map<Class<?>, WenflonDynamicProxy<?>> registry = new ConcurrentHashMap<>();

  public void putBehindWenflon(
      final Class<?> anInterface, final Object bean, final String beanName) {
    registry.computeIfPresent(
        anInterface, (k, v) -> v.addImplementation(bean, beanName, c -> false));
  }

  public <T> WenflonDynamicProxy<T> createAndRegisterWenflonProxy(final Class<T> aClass) {
    final var wenflon = new WenflonDynamicProxy<>(aClass);
    registry.put(aClass, wenflon);
    return wenflon;
  }

  public boolean isWenflonPreparedFor(Class<?> aClass) {
    return registry.containsKey(aClass);
  }
}