package com.github.mikalaid.wenflon.core;

import java.lang.annotation.*;

/**
 * Annotation to use on an interface for which dynamic proxy will to be created. Won't have any effect if used on class.
 * All the implementations of an annotated interface will be put behind a special proxy at runtime,
 * implementing the very same interface. The proxy will be autowired in all the places where the interface implementation is expected,
 * but no @Qualifier is used. Effectively, the proxy will become a primary bean. Then, in runtime, depending on application state (see {@link PivotProvider})
 * and previously declared conditions (via application properties), the proxy will direct calls to the interface method to an appropriate implementation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Wenflon {
    /**
     * If <b>true</b> and annotated interface has only one implementation, then, even if there are conditions in the <i>application.properties</i>
     * the implementation will be used as the default one and no condition will be checked.
     * If <b>false</b> then condition will always be checked, even for the single implementation. In case condition is not met the {@link com.github.mikalaid.wenflon.exceptions.WenflonException }
     * will be thrown.
     *
     * @return
     *
     */
    boolean soleConditionalImplAsImplicitDefault() default true;

    /**
     * To be used in scenarios where there are 2+ {@link PivotProvider } implementations.
     * @return
     */
    String pivotProviderBeanName() default "none";
}
