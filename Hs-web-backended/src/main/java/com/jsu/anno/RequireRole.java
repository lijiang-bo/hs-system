package com.jsu.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于权限校验
 * 标注在需要限制角色才能访问的Controller方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {

    /**
     * 排除的角色列表（无权限的角色），默认排除普通员工(role = 3)
     */
    int[] exclude() default {3};
}
