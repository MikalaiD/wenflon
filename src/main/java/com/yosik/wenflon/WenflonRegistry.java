package com.yosik.wenflon;


import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WenflonRegistry {

    private final Map<Class<?>, WenflonDynamicProxy<?>> registry = new ConcurrentHashMap<>();
    public void registerBehindWenflon(final Object bean) {
        Arrays.stream(bean.getClass().getInterfaces())
                .filter(in->in.isAnnotationPresent(Wenflon.class))
                .forEach(this::upsertWenflon);
    }

    public Object getWenflon(final Class<?> aClass){
        return registry.get(aClass); //todo here must be returned proxy
    }

    private void upsertWenflon(final Class<?> aClass) {
        registry.merge(aClass, new WenflonDynamicProxy<>(), (a,b)-> {b.increment(); return b;});
    }
}
