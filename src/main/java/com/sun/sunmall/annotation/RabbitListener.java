package com.sun.sunmall.annotation;

import java.lang.annotation.*;

import static java.lang.Character.TYPE;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by sun on 2018/3/7.
 */
@Target(value={ElementType.TYPE,METHOD,ANNOTATION_TYPE})
@Retention(value=RUNTIME)
@Documented

public @interface RabbitListener{}
