package com.yosik.wenflon;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;

@RequiredArgsConstructor
public class WenflonBeansPostprocessor implements BeanPostProcessor {

        private final WenflonRegistry wenflonRegistry;
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (Arrays.stream(bean.getClass().getInterfaces()).anyMatch(aClass -> aClass.isInstance(WenflonSimpleMarking.class))) {
                //todo add a counter here, so in post postProcessAfterInitialization we know how many and what kind of bean is there
                //can be async
                wenflonRegistry.preRegister(bean);
            }
            return bean; //todo what to do with it? deregister? or make proxy primary forcefully
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
}
