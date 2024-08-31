package io.github.mikalaid.wenflon.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class WenflonRegistry {

  private final Map<Class<?>, DynamicProxyManager<?>> registry = new ConcurrentHashMap<>();

  void putBehindWenflon(
      final Class<?> anInterface, final Object bean, final String beanName) {
    registry.computeIfPresent(
        anInterface, (k, v) -> v.addImplementation(bean, beanName));
  }

  <T> DynamicProxyManager<T> createAndRegisterProxyManager(final Class<T> aClass) {
    final var wenflon = new DynamicProxyManager<>(aClass);
    registry.put(aClass, wenflon);
    return wenflon;
  }

  boolean isWenflonPreparedFor(Class<?> aClass) {
    return registry.containsKey(aClass);
  }
}
