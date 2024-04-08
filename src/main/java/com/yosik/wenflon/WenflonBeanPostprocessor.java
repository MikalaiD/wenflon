package com.yosik.wenflon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@RequiredArgsConstructor
@Slf4j
public class WenflonBeanPostprocessor implements BeanPostProcessor {

    private final WenflonRegistry wenflonRegistry = new WenflonRegistry(); //todo not sure if temp or permanent solution

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        //here we can just strip off @Primary from the wenflon eligible beans
        //here we assume class will implement only one interface under wenflon

//        Optional.of(bean.getClass())
//                .filter(wenflonRegistry::isWenflonRegisteredFor)
//                .ifPresent(aClass -> wenflonRegistry.registerBehindWenflon(aClass,
//                        bean,
//                        () -> "panda", //todo temp, come up with passing pivot provider class from Wenflon
//                        (value) -> true ? value.equals("panda") : value.equals("grizzly"))//todo temp, come up with passing condition from WenflonList
//                );
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

}
