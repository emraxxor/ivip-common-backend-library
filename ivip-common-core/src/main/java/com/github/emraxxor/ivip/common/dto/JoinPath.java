package com.github.emraxxor.ivip.common.dto;

import java.lang.annotation.*;


/**
 *
 * @author Attila Barna
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface JoinPath {

    String path();
}
