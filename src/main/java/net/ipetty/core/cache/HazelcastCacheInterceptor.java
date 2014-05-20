package net.ipetty.core.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ipetty.core.cache.annotation.LoadFromHazelcast;
import net.ipetty.core.cache.annotation.UpdateToHazelcast;
import net.ipetty.core.cache.annotation.UpdatesToHazelcast;
import net.ipetty.core.util.MethodParamNamesScaner;
import ognl.Ognl;
import ognl.OgnlException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * HazelcastCacheInterceptor
 * 
 * 参考 http://www.cnblogs.com/warden/p/
 * simple_cache_solutions_base_on_springaop_and_annotation.html
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
		LoadFromHazelcast annotation = getAnnotation(call, LoadFromHazelcast.class);
		String mapName = annotation.mapName();
		String keyName = annotation.keyName();

		// get actual key
		K key = executeOgnl(keyName, call);
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
		UpdateToHazelcast annotation = getAnnotation(call, UpdateToHazelcast.class);
		String mapName = annotation.mapName();
		String keyName = annotation.keyName();

		V value = (V) call.proceed();

		K key = executeOgnl(keyName, call);
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
		UpdatesToHazelcast annotations = getAnnotation(call, UpdatesToHazelcast.class);
		V value = (V) call.proceed();

		for (UpdateToHazelcast annotation : annotations.value()) {
			String mapName = annotation.mapName();
			String keyName = annotation.keyName();

			K key = executeOgnl(keyName, call);
			BaseHazelcastCache.delete(mapName, key);
			logger.debug("Hazelcast.delete('{}', {});", mapName, key);
		}
		return value;
	}

	/**
	 * 获得Annotation对象
	 */
	private <T extends Annotation> T getAnnotation(ProceedingJoinPoint jp, Class<T> clazz) {
		MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
		Method method = joinPointObject.getMethod();
		return method.getAnnotation(clazz);
	}

	/**
	 * 执行ognl表达式并返回结果
	 */
	@SuppressWarnings("unchecked")
	private <T> T executeOgnl(String ognl, ProceedingJoinPoint jp) throws OgnlException {
		Method method = ((MethodSignature) jp.getSignature()).getMethod();

		// 形参列表
		List<String> paramNames = MethodParamNamesScaner.getParamNames(method);
		logger.debug("MethodParamNamesScaner.getParamNames is {}", paramNames);

		// 参数值
		Object[] paramValues = jp.getArgs();

		// 形参与参数值对照Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int i = 0; i < paramNames.size(); i++) {
			paramMap.put(paramNames.get(i), paramValues[i]);
		}

		// 去除${与}
		String paramName = ognl.substring(2, ognl.length() - 1);
		logger.debug("param name is {}", paramName);

		// 返回ognl执行结果
		return (T) Ognl.getValue(paramName, paramMap);
	}

}
