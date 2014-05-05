package net.ipetty.core.web.rest.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author xiaojinghai SpringMVC 中的Interceptor 拦截请求是通过HandlerInterceptor
 *         来实现的。在SpringMVC 中定义一个Interceptor
 *         非常简单，主要有两种方式，第一种方式是要定义的Interceptor类要实现了Spring 的HandlerInterceptor
 *         接口，或者是这个类继承实现了HandlerInterceptor 接口的类，比如Spring
 *         已经提供的实现了HandlerInterceptor 接口的抽象类HandlerInterceptorAdapter
 *         ；第二种方式是实现Spring的WebRequestInterceptor接口
 *         ，或者是继承实现了WebRequestInterceptor的类。 （一）实现HandlerInterceptor接口
 *         HandlerInterceptor 接口中定义了三个方法，我们就是通过这三个方法来对用户的请求进行拦截处理的。 （1
 *         ）preHandle (HttpServletRequest request, HttpServletResponse response,
 *         Object handle) 方法，顾名思义，该方法将在请求处理之前进行调用。SpringMVC 中的Interceptor
 *         是链式的调用的，在一个应用中或者说是在一个请求中可以同时存在多个Interceptor 。每个Interceptor
 *         的调用会依据它的声明顺序依次执行，而且最先执行的都是Interceptor 中的preHandle
 *         方法，所以可以在这个方法中进行一些前置初始化操作或者是对当前请求的一个预处理
 *         ，也可以在这个方法中进行一些判断来决定请求是否要继续进行下去。该方法的返回值是布尔值Boolean 类型的，当它返回为false
 *         时，表示请求结束，后续的Interceptor 和Controller 都不会再执行；当返回值为true
 *         时就会继续调用下一个Interceptor 的preHandle 方法，如果已经是最后一个Interceptor
 *         的时候就会是调用当前请求的Controller 方法。 （2 ）postHandle (HttpServletRequest
 *         request, HttpServletResponse response, Object handle, ModelAndView
 *         modelAndView) 方法，由preHandle 方法的解释我们知道这个方法包括后面要说到的afterCompletion
 *         方法都只能是在当前所属的Interceptor 的preHandle 方法的返回值为true 时才能被调用。postHandle
 *         方法，顾名思义就是在当前请求进行处理之后，也就是Controller 方法调用之后执行，但是它会在DispatcherServlet
 *         进行视图返回渲染之前被调用，所以我们可以在这个方法中对Controller 处理之后的ModelAndView
 *         对象进行操作。postHandle 方法被调用的方向跟preHandle 是相反的，也就是说先声明的Interceptor
 *         的postHandle 方法反而会后执行，这和Struts2 里面的Interceptor 的执行过程有点类型。Struts2
 *         里面的Interceptor 的执行过程也是链式的，只是在Struts2 里面需要手动调用ActionInvocation 的invoke
 *         方法来触发对下一个Interceptor 或者是Action 的调用，然后每一个Interceptor 中在invoke
 *         方法调用之前的内容都是按照声明顺序执行的，而invoke 方法之后的内容就是反向的。 （3
 *         ）afterCompletion(HttpServletRequest request, HttpServletResponse
 *         response, Object handle, Exception ex) 方法，该方法也是需要当前对应的Interceptor
 *         的preHandle 方法的返回值为true 时才会执行。顾名思义，该方法将在整个请求结束之后，也就是在DispatcherServlet
 *         渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的。
 */
public class SpringMVCInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SpringMVCInterceptor.class);

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
	 * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行
	 * ，而且所有的Interceptor中的preHandle方法都会在
	 * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的
	 * ，这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("RequestHeaders:" + request.getRequestURI());
		Enumeration hs = request.getHeaderNames();
		while (hs.hasMoreElements()) {
			String name = (String) hs.nextElement();
			String value = request.getHeader(name);
			logger.debug(name + ":" + value);
		}
		return true;
	}

	/**
	 * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，
	 * 它的执行时间是在处理器进行处理之
	 * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行
	 * ，也就是说在这个方法中你可以对ModelAndView进行操
	 * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用
	 * ，这跟Struts2里面的拦截器的执行过程有点像，
	 * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法
	 * ，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
	 * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前
	 * ，要在Interceptor之后调用的内容都写在调用invoke方法之后。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，
	 * 也就是DispatcherServlet渲染了视图执行，
	 * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
