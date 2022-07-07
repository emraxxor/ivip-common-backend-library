package com.github.emraxxor.ivip.common.dto;

import java.lang.annotation.*;

/**
 * Indicates the full path to the document.
 *
 * @author Attila Barna
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface DocumentPath {

    String path();
}
