package com.yosik.wenflon;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class WenflonBeansPostprocessor implements BeanPostProcessor {


        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean.getClass().isAnnotationPresent(Interchangable.class)) {
                throw new RuntimeException("Tmp - for wenflon!");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
}
