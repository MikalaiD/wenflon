package com.yosik.wenflon;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Wenflon {
    boolean soleConditionalImplAsImplicitDefault() default true;
    String pivotProviderBeanName() default "";
}
