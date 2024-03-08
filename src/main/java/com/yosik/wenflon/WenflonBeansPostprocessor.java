package com.yosik.wenflon;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class WenflonBeansPostprocessor implements BeanPostProcessor {

        private final WenflonRegistry wenflonRegistry;
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            //here we assume class will implement only one interface under wenflon
            Optional<Class<?>> interfaceWithWenflon = Arrays.stream(bean.getClass().getInterfaces()).filter(aClass -> aClass.isAnnotationPresent(Wenflon.class)).findFirst();
            if (interfaceWithWenflon.isPresent()) {
                //todo add a counter here, so in post postProcessAfterInitialization we know how many and what kind of bean is there
                //can be async
               return wenflonRegistry.registerBehindWenflon(interfaceWithWenflon.get(), bean);
            } //FINISHED here - make it return actual proxy
            return bean;
        }
}
