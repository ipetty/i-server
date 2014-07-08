package net.ipetty.core.cache.interceptor;

import net.ipetty.core.cache.Caches;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.cache.annotation.UpdateToCache;
import net.ipetty.core.cache.annotation.UpdatesToCache;
import net.ipetty.core.util.AopUtils;
import ognl.OgnlException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * CachesInterceptor
 * 
 * 参考
 * "http://www.cnblogs.com/warden/p/simple_cache_solutions_base_on_springaop_and_annotation.html"
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
@Component
@Aspect
public class CachesInterceptor {

	private final String LOAD_FROM_CACHE = "@annotation(net.ipetty.core.cache.annotation.LoadFromCache)";
	private final String UPDATE_TO_CACHE = "@annotation(net.ipetty.core.cache.annotation.UpdateToCache)";
	private final String UPDATES_TO_CACHE = "@annotation(net.ipetty.core.cache.annotation.UpdatesToCache)";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 首先从缓存中加载数据，缓存命中则返回数据，未命中则从数据库查找，并加入缓存
	 */
	@SuppressWarnings("unchecked")
	@Around(LOAD_FROM_CACHE)
	public <K, V> V get(ProceedingJoinPoint call) throws Throwable {
		LoadFromCache annotation = AopUtils.getAnnotation(call, LoadFromCache.class);
		String mapName = annotation.mapName();
		String keyName = annotation.key();

		try {
			// get actual key
			String key = AopUtils.executeOgnlKey(keyName, call);
			V value = Caches.get(mapName, key);
			logger.debug("Hazelcast.get('{}', {}) = {}", mapName, key, value);

			if (value == null) {
				value = (V) call.proceed();
				if (value != null) {
					Caches.set(mapName, key, value);
					logger.debug("Hazelcast.set('{}', {}, {});", mapName, key, value);
				} else {
					Caches.delete(mapName, key);
					logger.debug("Hazelcast.delete('{}', {});", mapName, key);
				}
			}

			return value;
		} catch (OgnlException e) {
			logger.error("Ognl Execute Execption: ", e);
			return (V) call.proceed();
		}
	}

	/**
	 * 执行方法的同时更新缓存中的数据
	 */
	@SuppressWarnings("unchecked")
	@Around(UPDATE_TO_CACHE)
	public <K, V> V update(ProceedingJoinPoint call) throws Throwable {
		UpdateToCache annotation = AopUtils.getAnnotation(call, UpdateToCache.class);
		String mapName = annotation.mapName();
		String keyName = annotation.key();

		V value = (V) call.proceed();

		try {
			String key = AopUtils.executeOgnlKey(keyName, call, value);
			Caches.delete(mapName, key);
			logger.debug("Hazelcast.delete('{}', {});", mapName, key);
		} catch (OgnlException e) {
			logger.error("Ognl Execute Execption: ", e);
		}

		return value;
	}

	/**
	 * 执行方法的同时更新缓存中的数据
	 */
	@SuppressWarnings("unchecked")
	@Around(UPDATES_TO_CACHE)
	public <K, V> V updates(ProceedingJoinPoint call) throws Throwable {
		UpdatesToCache annotations = AopUtils.getAnnotation(call, UpdatesToCache.class);
		V value = (V) call.proceed();

		for (UpdateToCache annotation : annotations.value()) {
			String mapName = annotation.mapName();
			String keyName = annotation.key();

			try {
				String key = AopUtils.executeOgnlKey(keyName, call, value);
				Caches.delete(mapName, key);
				logger.debug("Hazelcast.delete('{}', {});", mapName, key);
			} catch (OgnlException e) {
				logger.error("Ognl Execute Execption: ", e);
			}
		}
		return value;
	}

}
