package net.ipetty.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UpdatesToCache
 * 
 * @author luocanfeng
 * @date 2014年5月20日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdatesToCache {

	UpdateToCache[] value();

}
