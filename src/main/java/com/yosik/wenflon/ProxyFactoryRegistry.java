package com.yosik.wenflon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyFactoryRegistry {

  private final Map<Class<?>, ProxyFactory<?>> registry = new ConcurrentHashMap<>();

  public void putBehindWenflon(final Class<?> anInterface, final Object bean) {
    registry.computeIfPresent(anInterface, (k, v) -> v.addImplementation(bean, c -> false));
    registry.get(anInterface);
  }

  public <T> ProxyFactory<T> createAndRegisterWenflonProxy(final Class<T> aClass) {
    final var wenflon = new ProxyFactory<>(aClass);
    registry.put(aClass, wenflon);
    return wenflon;
  }

  public boolean isWenflonPreparedFor(Class<?> aClass) {
    return registry.containsKey(aClass);
  }
}
