package net.ipetty.core.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UpdateToCache
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateToCache {

	String mapName(); // 缓存的mapName

	String key(); // 缓存的key，可以是多个字段拼合的key

}
