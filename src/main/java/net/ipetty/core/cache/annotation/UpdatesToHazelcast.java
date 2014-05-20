package net.ipetty.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UpdatesToHazelcast
 * 
 * @author luocanfeng
 * @date 2014年5月20日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdatesToHazelcast {

	UpdateToHazelcast[] value();

}
