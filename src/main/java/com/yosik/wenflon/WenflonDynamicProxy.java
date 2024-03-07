package com.yosik.wenflon;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.*;

public class WenflonDynamicProxy<T> { //todo rename to vet?

    @Getter
    private int latch = 1; //todo tmp, before switch to some dedicated async structure

    public WenflonDynamicProxy() {//todo tmp, before switch to some dedicated async structure
        this.latch = 1;
    }

    void increment() { //todo tmp, before switch to some dedicated async structure
        latch++;
    }

    private final List<T> implementations = new ArrayList<>(); //todo not sure if needed concurrent

    public void add(T implementation) {
        implementations.add(implementation);
    }

    public T createProxy() {
        final Class<?>[] commonInterfaces = getCommonInterfaces(); //todo come up with a dedicated exception
        return (T) Proxy.newProxyInstance(
                WenflonDynamicProxy.class.getClassLoader(),
                commonInterfaces,
                (proxy, method, args) -> implementations.get(0).getClass().getSimpleName()
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