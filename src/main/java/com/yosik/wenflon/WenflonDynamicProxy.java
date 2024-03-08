package com.yosik.wenflon;

import lombok.Getter;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class WenflonDynamicProxy { //todo rename to vet?
    private final List<Object> implementations; //todo not sure if needed concurrent
    private final Class<?> interfaceUnderWenflon;

    @Getter
    private final Object proxy;

    public WenflonDynamicProxy(final Class<?> interfaceUnderWenflon) {
        this.interfaceUnderWenflon = interfaceUnderWenflon;
        implementations=new ArrayList<>();
        proxy = createProxy();
    }

    public WenflonDynamicProxy addImplementation(final Object bean) {
        implementations.add(bean);
        return this;
    }

    private Object createProxy() {
        return Proxy.newProxyInstance(
                WenflonDynamicProxy.class.getClassLoader(),
                new Class<?> [] {interfaceUnderWenflon},
                (proxy, method, args) -> {
                    System.out.println("Placeholder!!!");
                    return "test";
                }//todo normal implementation
        );
    }
}