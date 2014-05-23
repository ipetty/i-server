package net.ipetty.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AopUtils
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
	 * 执行ognl表达式并返回结果
	 */
	public static <T> T executeOgnl(String ognl, ProceedingJoinPoint call) throws OgnlException {
		return executeOgnl(ognl, call, null);
	}

	/**
	 * 执行ognl表达式并返回结果
	 * 
	 * @param result
	 *            被拦截方法的返回值
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> T executeOgnl(String ognl, ProceedingJoinPoint call, R result) throws OgnlException {
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

}
