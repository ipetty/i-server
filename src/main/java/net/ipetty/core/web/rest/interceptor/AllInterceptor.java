package net.ipetty.core.web.rest.interceptor;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * 
 * @author xiaojinghai
 * 
 *         WebRequestInterceptor
 *         中也定义了三个方法，我们也是通过这三个方法来实现拦截的。这三个方法都传递了同一个参数WebRequest ，那么这个WebRequest
 *         是什么呢？这个WebRequest 是Spring 定义的一个接口，它里面的方法定义都基本跟HttpServletRequest
 *         一样，在WebRequestInterceptor 中对WebRequest 进行的所有操作都将同步到HttpServletRequest
 *         中，然后在当前请求中一直传递。 （1 ）preHandle(WebRequest request)
 *         方法。该方法将在请求处理之前进行调用，也就是说会在Controller 方法调用之前被调用。这个方法跟HandlerInterceptor
 *         中的preHandle 是不同的，主要区别在于该方法的返回值是void
 *         ，也就是没有返回值，所以我们一般主要用它来进行资源的准备工作，比如我们在使用Hibernate
 *         的时候可以在这个方法中准备一个Hibernate 的Session 对象，然后利用WebRequest
 *         的setAttribute(name, value, scope) 把它放到WebRequest
 *         的属性中。这里可以说说这个setAttribute 方法的第三个参数scope ，该参数是一个Integer
 *         类型的。在WebRequest 的父层接口RequestAttributes 中对它定义了三个常量： SCOPE_REQUEST
 *         ：它的值是0 ，代表只有在request 中可以访问。 SCOPE_SESSION ：它的值是1
 *         ，如果环境允许的话它代表的是一个局部的隔离的session，否则就代表普通的session，并且在该session范围内可以访问。
 *         SCOPE_GLOBAL_SESSION ：它的值是2
 *         ，如果环境允许的话，它代表的是一个全局共享的session，否则就代表普通的session，并且在该session 范围内可以访问。 （2
 *         ）postHandle(WebRequest request, ModelMap model)
 *         方法。该方法将在请求处理之后，也就是在Controller
 *         方法调用之后被调用，但是会在视图返回被渲染之前被调用，所以可以在这个方法里面通过改变数据模型ModelMap
 *         来改变数据的展示。该方法有两个参数，WebRequest 对象是用于传递整个请求数据的，比如在preHandle
 *         中准备的数据都可以通过WebRequest 来传递和访问；ModelMap 就是Controller 处理之后返回的Model
 *         对象，我们可以通过改变它的属性来改变返回的Model 模型。 （3 ）afterCompletion(WebRequest
 *         request, Exception ex)
 *         方法。该方法会在整个请求处理完成，也就是在视图返回并被渲染之后执行。所以在该方法中可以进行资源的释放操作。而WebRequest
 *         参数就可以把我们在preHandle 中准备的资源传递到这里进行释放。Exception
 *         参数表示的是当前请求的异常对象，如果在Controller 中抛出的异常已经被Spring
 *         的异常处理器给处理了的话，那么这个异常对象就是是null 。
 */
public class AllInterceptor implements WebRequestInterceptor {

	/**
	 * 在请求处理之前执行，该方法主要是用于准备资源数据的，然后可以把它们当做请求属性放到WebRequest中
	 */
	@Override
	public void preHandle(WebRequest request) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("AllInterceptor...............................");
		// request.setAttribute("request", "request",
		// WebRequest.SCOPE_REQUEST);//这个是放到request范围内的，所以只能在当前请求中的request中获取到
		// request.setAttribute("session", "session",
		// WebRequest.SCOPE_SESSION);//这个是放到session范围内的，如果环境允许的话它只能在局部的隔离的会话中访问，否则就是在普通的当前会话中可以访问
		// request.setAttribute("globalSession", "globalSession",
		// WebRequest.SCOPE_GLOBAL_SESSION);//如果环境允许的话，它能在全局共享的会话中访问，否则就是在普通的当前会话中访问
	}

	/**
	 * 该方法将在Controller执行之后，返回视图之前执行，ModelMap表示请求Controller处理之后返回的Model对象，所以可以在
	 * 这个方法中修改ModelMap的属性，从而达到改变返回的模型的效果。
	 */
	@Override
	public void postHandle(WebRequest request, ModelMap map) throws Exception {
		// TODO Auto-generated method stub
		// for (String key : map.keySet()) {
		// System.out.println(key + "-------------------------");
		// };
		// map.put("name3", "value3");
		// map.put("name1", "name1");
	}

	/**
	 * 该方法将在整个请求完成之后，也就是说在视图渲染之后进行调用，主要用于进行一些资源的释放
	 */
	@Override
	public void afterCompletion(WebRequest request, Exception exception) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println(exception +
		// "-=-=--=--=-=-=-=-=-=-=-=-==-=--=-=-=-=");
	}

}
