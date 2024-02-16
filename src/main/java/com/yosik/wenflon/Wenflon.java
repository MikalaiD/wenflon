package com.yosik.wenflon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.*;

public class Wenflon <T>  {

    private final List<T> interfaces = new ArrayList<>(); //todo not sure if needed concurrent

    public void add(T implementation){
        interfaces.add(implementation);
    }
    public T createProxy() {
        Class<?>[] interfaces1 = interfaces.get(0).getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(
                Wenflon.class.getClassLoader(),
                interfaces1,
                (proxy, method, args) ->
                    interfaces.get(0).getClass().getSimpleName()
                );
    }
}