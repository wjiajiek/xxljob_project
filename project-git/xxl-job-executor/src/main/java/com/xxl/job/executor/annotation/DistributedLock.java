
package com.xxl.job.executor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    /**
     * 锁名称
     */
    String value() default "";

    /**
     * 锁过期时间，默认300s，单位秒
     */
    long expire() default 300;
}
