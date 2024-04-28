package com.yosik.wenflon;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class WenflonRegistry {

  private final Map<Class<?>, WenflonDynamicProxy<?>> registry = new ConcurrentHashMap<>();

  public WenflonDynamicProxy<?> putBehindWenflon(
      final Class<?> anInterface, // todo maybe return void?
      final Object bean) {
    registry.computeIfPresent(
        anInterface,
        (k, v) ->
            v.addImplementation(
                bean, c -> true)); // todo temp default condition... just seems better than null
    return registry.get(anInterface);
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
