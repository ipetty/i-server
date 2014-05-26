package net.ipetty.core.cache;

import net.ipetty.core.cache.annotation.LoadFromHazelcast;
import net.ipetty.core.cache.annotation.UpdateToHazelcast;
import net.ipetty.core.cache.annotation.UpdatesToHazelcast;
import net.ipetty.core.util.AopUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * HazelcastCacheInterceptor
 * 
 * 参考
 * "http://www.cnblogs.com/warden/p/simple_cache_solutions_base_on_springaop_and_annotation.html"
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
@Component
@Aspect
public class HazelcastCacheInterceptor {

	private final String GET = "@annotation(net.ipetty.core.cache.annotation.LoadFromHazelcast)";
	private final String UPDATE = "@annotation(net.ipetty.core.cache.annotation.UpdateToHazelcast)";
	private final String UPDATES = "@annotation(net.ipetty.core.cache.annotation.UpdatesToHazelcast)";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 首先从缓存中加载数据，缓存命中则返回数据，未命中则从数据库查找，并加入缓存
	 */
	@SuppressWarnings("unchecked")
	@Around(GET)
	public <K, V> V get(ProceedingJoinPoint call) throws Throwable {
		LoadFromHazelcast annotation = AopUtils.getAnnotation(call, LoadFromHazelcast.class);
		String mapName = annotation.mapName();
		String keyName = annotation.key();

		// get actual key
		String key = AopUtils.executeJoinedKey(keyName, call);
		V value = BaseHazelcastCache.get(mapName, key);
		logger.debug("Hazelcast.get('{}', {}) = {}", mapName, key, value);

		if (value == null) {
			value = (V) call.proceed();
			if (value != null) {
				BaseHazelcastCache.set(mapName, key, value);
				logger.debug("Hazelcast.set('{}', {}, {});", mapName, key, value);
			} else {
				BaseHazelcastCache.delete(mapName, key);
				logger.debug("Hazelcast.delete('{}', {});", mapName, key);
			}
		}

		return value;
	}

	/**
	 * 执行方法的同时更新缓存中的数据
	 */
	@SuppressWarnings("unchecked")
	@Around(UPDATE)
	public <K, V> V update(ProceedingJoinPoint call) throws Throwable {
		UpdateToHazelcast annotation = AopUtils.getAnnotation(call, UpdateToHazelcast.class);
		String mapName = annotation.mapName();
		String keyName = annotation.key();

		V value = (V) call.proceed();

		String key = AopUtils.executeJoinedKey(keyName, call, value);
		BaseHazelcastCache.delete(mapName, key);
		logger.debug("Hazelcast.delete('{}', {});", mapName, key);

		return value;
	}

	/**
	 * 执行方法的同时更新缓存中的数据
	 */
	@SuppressWarnings("unchecked")
	@Around(UPDATES)
	public <K, V> V updates(ProceedingJoinPoint call) throws Throwable {
		UpdatesToHazelcast annotations = AopUtils.getAnnotation(call, UpdatesToHazelcast.class);
		V value = (V) call.proceed();

		for (UpdateToHazelcast annotation : annotations.value()) {
			String mapName = annotation.mapName();
			String keyName = annotation.key();

			String key = AopUtils.executeJoinedKey(keyName, call, value);
			BaseHazelcastCache.delete(mapName, key);
			logger.debug("Hazelcast.delete('{}', {});", mapName, key);
		}
		return value;
	}

}
