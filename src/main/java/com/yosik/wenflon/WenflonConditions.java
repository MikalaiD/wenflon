package com.yosik.wenflon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WenflonConditions {
   String[] conditions() default "#{@environment.getProperty('non-wenflon-properties.not-cute')}";
   ; //todo introduce second parameter - clazz to be of paramerter
   //todo maybe to introduce condition type - by default will have only contains
}