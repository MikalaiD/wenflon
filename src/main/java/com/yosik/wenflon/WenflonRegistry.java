package com.yosik.wenflon;


import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WenflonRegistry {

    private final Map<Class<?>, WenflonDynamicProxyFactory> registry = new ConcurrentHashMap<>();
    public Object registerBehindWenflon(final Class<?> anInterface, final Object bean) {
       return registry.merge(anInterface, new WenflonDynamicProxyFactory(anInterface, bean), (a, b)-> {a.add(bean); return a;});
    }
}
