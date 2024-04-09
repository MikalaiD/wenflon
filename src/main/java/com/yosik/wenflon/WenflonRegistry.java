package com.yosik.wenflon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WenflonRegistry {

    private final Map<Class<?>, WenflonDynamicProxy<?>> registry = new ConcurrentHashMap<>();

    public WenflonDynamicProxy<?> putBehindWenflon(final Class<?> anInterface, //todo maybe return void?
                                                   final Object bean,
                                                   final Supplier<String> pivotProvider, //todo generalise from String
                                                   final Predicate<String> condition) { //todo generalise from String
        if (!registry.containsKey(anInterface)) {
            registry.put(anInterface, new WenflonDynamicProxy<>(anInterface, pivotProvider));
        }
        registry.computeIfPresent(anInterface, (k, v) -> v.addImplementation(bean, condition)); //todo come up with predicate based on wenflon type
        return registry.get(anInterface);
    }

    public <T> WenflonDynamicProxy<T> createAndRegisterWenflonProxy(final Class<T> aClass) {
        final var wenflon = new WenflonDynamicProxy<>(aClass, () -> "panda");
        registry.put(aClass, wenflon); //todo  temp pivot provider
        return wenflon;
    }

    public boolean isWenflonPreparedFor(Class<?> aClass) {
        return registry.containsKey(aClass);
    }
}
