package com.quincy.sdk.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface VCodeRequired {
	public String clientTokenName() default "";
	public boolean ignoreCase() default true;
	public String timeoutRedirectTo() default "/auth/vcode/failure";
}