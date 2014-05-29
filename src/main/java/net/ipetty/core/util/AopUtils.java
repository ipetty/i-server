package net.ipetty.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * 执行包括多个ognl表达式的key并返回结果
	 */
	public static <R> String executeOgnlKey(String ognlKey, ProceedingJoinPoint call) throws OgnlException {
		return executeOgnlKey(ognlKey, call, null);
	}

	/**
	 * 执行包括多个ognl表达式的key并返回结果
	 */
	public static <R> String executeOgnlKey(String ognlKey, ProceedingJoinPoint call, R result) throws OgnlException {
		if (!ognlKey.contains("$")) {
			return ognlKey;
		}

		String regexp = "\\$\\{[^\\}]+\\}";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(ognlKey);
		List<String> ognls = new ArrayList<String>();
		while (matcher.find()) {
			ognls.add(matcher.group());
		}
		return executeOgnls(ognlKey, ognls, call, result);
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
		logger.debug("Method name is {}", method.getName());

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
