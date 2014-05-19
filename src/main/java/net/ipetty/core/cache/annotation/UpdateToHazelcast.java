package net.ipetty.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UpdateToHazelcast
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateToHazelcast {

	String[] value(); // 可能有多个key需要更新

	String condition() default ""; // 执行缓存查询的条件

}
