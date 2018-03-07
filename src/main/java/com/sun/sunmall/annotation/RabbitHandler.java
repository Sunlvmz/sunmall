package com.sun.sunmall.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by sun on 2018/3/7.
 */
@Target(value={METHOD,ANNOTATION_TYPE})
@Retention(value=RUNTIME)

@Documented
public @interface RabbitHandler{};
