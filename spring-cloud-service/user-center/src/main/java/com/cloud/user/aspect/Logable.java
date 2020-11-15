package com.cloud.user.aspect;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logable {
    String description() default "";
}
