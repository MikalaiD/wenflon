package com.yosik.wenflon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.ResolvableType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class WenflonBeanFactoryPostprocessor implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

    private final WenflonRegistry wenflonRegistry = new WenflonRegistry();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Set<Class<?>> wenflonInterfaces = findAnnotatedInterfaces(registry);
        for (Class<?> aClass : wenflonInterfaces) {
            registerProxyAsPrimaryBean(registry, aClass);
        }
    }

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

//    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
//
//        beanFactory.getBeansWithAnnotation(Wenflon.class)
//                .keySet().stream()
//                .map(beanFactory::getBeanDefinition)
//                .filter(BeanDefinition::isPrimary)
//                .forEach(beanDefinition -> beanDefinition.setPrimary(false));
//        beanFactory.getBeansWithAnnotation(Wenflon.class)
//                .values()
//                .stream()
//                .map(Object::getClass)
//                .flatMap(aClass -> Arrays.stream(aClass.getInterfaces()))
//                .filter(aClass -> aClass.isAnnotationPresent(Wenflon.class))
//                .distinct()
//                .forEach(aClass -> registerProxyAsPrimaryBean(beanFactory, aClass));
//    }

    private void registerProxyAsPrimaryBean(BeanDefinitionRegistry registry, final Class<?> aClass) {
        WenflonDynamicProxy<?> wenflon = wenflonRegistry.createAndRegisterWenflonProxy(aClass);
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(aClass);
        beanDefinition.setDependsOn("testableA", "testableB");
        beanDefinition.setPrimary(true);
        beanDefinition.setInstanceSupplier(() -> aClass.cast(wenflon.getProxy()));
        registry.registerBeanDefinition(wenflon.getName(), beanDefinition);
    }

    private Set<Class<?>> findAnnotatedInterfaces(BeanDefinitionRegistry registry) {
        return Arrays.stream(registry.getBeanDefinitionNames())
                .map(registry::getBeanDefinition)
                .map(BeanDefinition::getResolvableType)
                .map(ResolvableType::getRawClass)
                .filter(Objects::nonNull)
                .filter(aClass -> aClass.isAnnotationPresent(Wenflon.class))
                .collect(Collectors.toSet());
    }
}
