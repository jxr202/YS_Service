package com.oudmon.wave.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
        ElementType.CONSTRUCTOR,ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface ReflectInvoke {
    String[] invokeBy();
}
