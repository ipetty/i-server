package net.ipetty.core.context;


/**
 * 用户上下文
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
public class UserContext {

	private static ThreadLocal<UserPrincipal> contextHolder = new ThreadLocal<UserPrincipal>();

	public static void clearContext() {
		contextHolder.set(null);
	}

	public static UserPrincipal getContext() {
		return contextHolder.get();
	}

	public static void setContext(UserPrincipal user) {
		contextHolder.set(user);
	}

}
