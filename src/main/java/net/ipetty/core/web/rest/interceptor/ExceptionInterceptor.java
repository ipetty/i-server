package net.ipetty.core.web.rest.interceptor;

import net.ipetty.core.web.rest.exception.RestException;

import org.aspectj.lang.annotation.AfterThrowing;

/**
 * 
 * @author xiaojinghai
 */
// @Aspect
public class ExceptionInterceptor {
	// Todo:全系统异常转换:exception-conversion-aspect
	// http://www.cnblogs.com/wushiqi54719880/archive/2011/08/09/2133048.html
	// http://forum.spring.io/forum/spring-projects/aop/42208-exception-conversion-aspect

	@AfterThrowing(pointcut = "execution(* com.example.*.*(..))", throwing = "t")
	public void toRuntimeException(Throwable t) {

		// if (t instanceof KnownException) {
		// throw (KnownException) t;
		// } else {
		RestException se = new RestException(t.getMessage());
		se.setStackTrace(t.getStackTrace());
		throw se;
		// }
	}

}
