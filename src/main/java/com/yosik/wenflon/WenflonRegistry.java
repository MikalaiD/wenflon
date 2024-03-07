package com.yosik.wenflon;


import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WenflonRegistry {

    private final Map<Class<?>, WenflonDynamicProxyFactory> registry = new ConcurrentHashMap<>();
    public void registerBehindWenflon(final Object bean) {
        Arrays.stream(bean.getClass().getInterfaces())
                .filter(in->in.isAnnotationPresent(Wenflon.class))
                .forEach(underWenflon->upsertWenflon(underWenflon, bean));
    }

    public Object getWenflon(final Class<?> aClass){
        return registry.get(aClass); //todo here must be returned proxy
    }

    private void upsertWenflon(final Class<?> anInterface, final Object bean) {
        registry.merge(anInterface, new WenflonDynamicProxyFactory(anInterface, bean), (a, b)-> {b.add(bean); return b;});
    }

    private static <T> T getCast(Class<T> anInterface, Object bean) {
        return anInterface.cast(bean);
    }
}
