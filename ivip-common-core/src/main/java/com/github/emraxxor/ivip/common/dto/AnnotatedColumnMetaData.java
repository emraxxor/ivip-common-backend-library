package com.github.emraxxor.ivip.common.dto;

import java.lang.annotation.*;


/**
 * Helps to manage columns
 *
 * @collection ivip.cloud
 * @author Attila Barna
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface AnnotatedColumnMetaData {

    String columnName();

    AnnotatedColumnParameter[] parameters();
}
