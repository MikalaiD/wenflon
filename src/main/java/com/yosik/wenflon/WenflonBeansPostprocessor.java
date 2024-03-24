package com.yosik.wenflon;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class WenflonBeansPostprocessor implements BeanPostProcessor {

        private final WenflonRegistry wenflonRegistry = new WenflonRegistry(); //todo not sure if temp or permanent solution
        @Override
        public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
            //here we assume class will implement only one interface under wenflon
            Optional<Class<?>> interfaceWithWenflon = Arrays.stream(bean.getClass().getInterfaces()).filter(aClass -> aClass.isAnnotationPresent(Wenflon.class)).findFirst();
            if (interfaceWithWenflon.isPresent()) {
               return wenflonRegistry.registerBehindWenflon(interfaceWithWenflon.get(), bean);
            }
            return bean;
        }
}
