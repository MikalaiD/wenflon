package com.yosik.wenflon;

import lombok.Getter;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WenflonDynamicProxy<T> {
    private final Map<Object, Predicate<String>> implementations;
    @Getter
    private final Class<T> representedInterface;
    private final Supplier<String> pivotProvider;

    @Getter
    private final T proxy;

    WenflonDynamicProxy(final Class<T> representedInterface, final Supplier<String> pivotProvider) {
        this.representedInterface = representedInterface;
        implementations = new HashMap<>();
        proxy = createProxy();
        this.pivotProvider = pivotProvider;
    }

    WenflonDynamicProxy<T> addImplementation(final Object bean, final Predicate<String> condition) {
        implementations.put(bean, condition);
        return this;
    }

    private T createProxy() {
        return representedInterface.cast(Proxy.newProxyInstance(
                WenflonDynamicProxy.class.getClassLoader(),
                new Class<?>[]{representedInterface},
                (proxy, method, args) -> method.invoke(defineImplementation(), args))
        );
    }

    private Object defineImplementation() {
        return implementations.entrySet().stream()
                .filter(predicateObjectEntry -> predicateObjectEntry.getValue().test(pivotProvider.get()))
                .findFirst().map(Map.Entry::getKey).orElse(new Object()); //todo test this scenario if it is possible
    }

    public String getName() {
        return String.format("wenflon-%s",this.representedInterface.getSimpleName());
    }
}