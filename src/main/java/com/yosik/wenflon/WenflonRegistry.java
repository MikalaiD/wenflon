package com.yosik.wenflon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WenflonRegistry {

    private final Map<Class<?>, WenflonDynamicProxyFactory> registry = new ConcurrentHashMap<>();
    public Object registerBehindWenflon(final Class<?> anInterface, final Object bean) {
        registry.putIfAbsent(anInterface, new WenflonDynamicProxyFactory(anInterface));
        registry.computeIfPresent(anInterface, (k, v)->v.addImplementation(bean));
        return registry.get(anInterface).getProxy();
    }
}
