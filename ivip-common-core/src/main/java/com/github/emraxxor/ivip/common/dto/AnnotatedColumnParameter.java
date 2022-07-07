package com.github.emraxxor.ivip.common.dto;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface AnnotatedColumnParameter {

    Class<?> parameterType();

}
