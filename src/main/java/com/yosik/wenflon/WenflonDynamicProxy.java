package com.yosik.wenflon;

import lombok.Getter;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

class WenflonDynamicProxy {
    private final List<Object> implementations;
    private final Class<?> interfaceUnderWenflon;

    @Getter
    private final Object proxy;

    WenflonDynamicProxy(final Class<?> interfaceUnderWenflon) {
        this.interfaceUnderWenflon = interfaceUnderWenflon;
        implementations = new ArrayList<>();
        proxy = createProxy();
    }

    WenflonDynamicProxy addImplementation(final Object bean) {
        implementations.add(bean);
        return this;
    }

    private Object createProxy() {
        return Proxy.newProxyInstance(
                WenflonDynamicProxy.class.getClassLoader(),
                new Class<?>[]{interfaceUnderWenflon},
                (proxy, method, args) -> {
                    System.out.println("Placeholder!!!");
                    return "test";
                }//todo normal implementation
        );
    }
}