package net.ipetty.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ipetty.core.exception.BusinessException;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AopUtils
 * 
 * 参考
 * "http://www.cnblogs.com/warden/p/simple_cache_solutions_base_on_springaop_and_annotation.html"
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
public class AopUtils {

	public static final String RESULT_KEY = "return";
	private static Logger logger = LoggerFactory.getLogger(AopUtils.class);

	/**
	 * 获得Annotation对象
	 */
	public static <T extends Annotation> T getAnnotation(ProceedingJoinPoint call, Class<T> clazz) {
		MethodSignature joinPointObject = (MethodSignature) call.getSignature();
		Method method = joinPointObject.getMethod();
		return method.getAnnotation(clazz);
	}

	/**
	 * 执行包括单个ognl表达式的key并返回结果
	 */
	public static <T> T executeSingleKey(String singleKey, ProceedingJoinPoint call) throws OgnlException {
		return executeOgnl(singleKey, call, null);
	}

	/**
	 * 执行包括单个ognl表达式的key并返回结果
	 */
	public static <T, R> T executeSingleKey(String singleKey, ProceedingJoinPoint call, R result) throws OgnlException {
		return executeOgnl(singleKey, call, result);
	}

	/**
	 * 执行ognl表达式并返回结果
	 * 
	 * @param result
	 *            被拦截方法的返回值
	 */
	@SuppressWarnings("unchecked")
	private static <T, R> T executeOgnl(String ognl, ProceedingJoinPoint call, R result) throws OgnlException {
		Method method = ((MethodSignature) call.getSignature()).getMethod();

		// 形参列表
		List<String> paramNames = MethodParamNamesScaner.getParamNames(method);
		logger.debug("MethodParamNamesScaner.getParamNames is {}", paramNames);

		// 参数值
		Object[] paramValues = call.getArgs();

		// 形参与参数值对照Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int i = 0; i < paramNames.size(); i++) {
			paramMap.put(paramNames.get(i), paramValues[i]);
		}
		// 将结果也放入到形参Map中
		paramMap.put(RESULT_KEY, result);

		// 去除${与}
		String paramName = ognl.substring(2, ognl.length() - 1);
		logger.debug("param name is {}", paramName);

		// 返回ognl执行结果
		return (T) Ognl.getValue(paramName, paramMap);
	}

	/**
	 * 执行包括多个ognl表达式的key并返回结果
	 */
	public static <R> String executeJoinedKey(String joinedKey, ProceedingJoinPoint call) throws OgnlException {
		return executeJoinedKey(joinedKey, call, null);
	}

	/**
	 * 执行包括多个ognl表达式的key并返回结果
	 */
	public static <R> String executeJoinedKey(String joinedKey, ProceedingJoinPoint call, R result)
			throws OgnlException {
		if (!joinedKey.contains("$")) {
			return joinedKey;
		}

		String regexp = "\\$\\{[^\\}]+\\}";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(joinedKey);
		List<String> ognls = new ArrayList<String>();
		try {
			while (matcher.find()) {
				ognls.add(matcher.group());
			}
			return executeOgnls(joinedKey, ognls, call, result);
		} catch (Exception e) {
			logger.error("Regex Parse Error!", e);
			throw new BusinessException(e);
		}
	}

	/**
	 * 执行包括多个ognl表达式的key并返回结果
	 * 
	 * @param result
	 *            被拦截方法的返回值
	 */
	private static <R> String executeOgnls(String key, List<String> ognls, ProceedingJoinPoint call, R result)
			throws OgnlException {
		String value = key;
		if (CollectionUtils.isEmpty(ognls)) {
			return value;
		}

		Method method = ((MethodSignature) call.getSignature()).getMethod();

		// 形参列表
		List<String> paramNames = MethodParamNamesScaner.getParamNames(method);
		logger.debug("MethodParamNamesScaner.getParamNames is {}", paramNames);

		// 参数值
		Object[] paramValues = call.getArgs();

		// 形参与参数值对照Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int i = 0; i < paramNames.size(); i++) {
			paramMap.put(paramNames.get(i), paramValues[i]);
		}
		// 将结果也放入到形参Map中
		paramMap.put(RESULT_KEY, result);

		// 执行ognl
		for (String ognl : ognls) {
			String temp = ognl.substring(2);
			temp = temp.substring(0, temp.length() - 1);
			Object ognlValue = Ognl.getValue(temp, paramMap);
			value = replace(value, ognl, ognlValue == null ? "null" : ognlValue.toString());
		}

		// 返回ognl执行结果
		return value;
	}

	/**
	 * 不依赖Regex的替换，避免$符号、{}等在String.replaceAll方法中当做Regex处理时候的问题。
	 */
	private static String replace(String src, String from, String to) {
		int index = src.indexOf(from);
		if (index == -1) {
			return src;
		}
		return src.substring(0, index) + to + src.substring(index + from.length());
	}

}
