package com.yosik.wenflon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.*;
import java.util.Optional;

public class Wenflon<T> {

    private final List<T> implementations = new ArrayList<>(); //todo not sure if needed concurrent

    public void add(T implementation) {
        implementations.add(implementation);
    }

    public T createProxy() {
        Class<?>[] interfaces1 =  implementations.stream()
                .map(impl -> Arrays.stream(impl.getClass().getInterfaces()).toList())
                .reduce((a, b) -> {
                    var newA = new ArrayList<>(a);
                    newA.retainAll(b);
                    return newA;
                }).orElseThrow().toArray(Class<?>[]::new); //todo come up with a dedicated exception
        return (T) Proxy.newProxyInstance(
                Wenflon.class.getClassLoader(),
                interfaces1,
                (proxy, method, args) ->
                        implementations.get(0).getClass().getSimpleName()
        );
    }
}