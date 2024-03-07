package com.yosik.wenflon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.*;

public class WenflonDynamicProxyFactory { //todo rename to vet?
    private final List<Object> implementations; //todo not sure if needed concurrent
    private final Class<?> interfaceUnderWenflon;

    public WenflonDynamicProxyFactory(final Class<?> interfaceUnderWenflon, final Object bean) {
        this.interfaceUnderWenflon = interfaceUnderWenflon;
        implementations=new ArrayList<>(List.of(bean));
    }

    public void add(Object bean) {
        implementations.add(bean);
    }

    public Object createProxy() {
        return Proxy.newProxyInstance(
                WenflonDynamicProxyFactory.class.getClassLoader(),
                new Class<?> [] {interfaceUnderWenflon},
                (proxy, method, args) -> implementations.get(0).getClass().getSimpleName() //todo normal implementation
        );
    }

    private Class<?>[] getCommonInterfaces() {
        return implementations.stream()
                .map(impl -> Arrays.stream(impl.getClass().getInterfaces()).toList())
                .reduce((a, b) -> {
                    var newA = new ArrayList<>(a);
                    newA.retainAll(b);
                    return newA;
                }).orElseThrow().toArray(Class<?>[]::new);
    }
}