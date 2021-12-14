package com.github.emraxxor.ivip.common.feign.clients;

import com.github.emraxxor.ivip.common.feign.realms.IvipPublicRealmFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@FeignClient
public @interface IvipPublicFeignClient {
    @AliasFor(annotation = FeignClient.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = FeignClient.class, attribute = "configuration")
    Class<?>[] configuration() default IvipPublicRealmFeignConfig.class;

    @AliasFor(annotation = FeignClient.class, attribute = "url")
    String url() default "";
}
