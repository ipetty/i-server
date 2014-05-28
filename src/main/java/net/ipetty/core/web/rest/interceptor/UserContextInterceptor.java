package net.ipetty.core.web.rest.interceptor;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ipetty.core.cache.BaseHazelcastCache;
import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.util.Encodes;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * UserContextInterceptor
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
public class UserContextInterceptor implements HandlerInterceptor {

	public static final String HEADER_NAME_USER_TOKEN = "user_token";
	public static final Charset UTF8 = Charset.forName("UTF-8");

	private Logger logger = LoggerFactory.getLogger(getClass());

	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 从request中读取用户token，解析后写入到线程安全的UserContext中
		logger.debug("request uri: {}", request.getRequestURI());
		String encodedUserToken = request.getHeader(HEADER_NAME_USER_TOKEN);
		if (StringUtils.isNotBlank(encodedUserToken)) { // 带token
			String userToken = new String(Encodes.decodeBase64(encodedUserToken), UTF8);
			logger.debug("request header: {}={}", HEADER_NAME_USER_TOKEN, userToken);
			// 从服务器缓存中获取对应用户信息并写入到线程安全的UserContext中
			Integer userId = BaseHazelcastCache.get(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, userToken);
			if (userId != null) {
				User user = userService.getById(userId);
				UserPrincipal principal = UserPrincipal.fromUser(user, userToken);
				UserContext.setContext(principal);
				logger.debug("current user is {}", UserContext.getContext());
				// response.setHeader(HEADER_NAME_USER_TOKEN, encodedUserToken);
			} else {
				// TODO 如果token过期，则返回错误，要求用户重新登录
				throw new RestException("Token无效或已过期，请重新登录");
				// response.setStatus(401);
				// response.getWriter().print("token过期");
				// return false;
			}
		} else { // 不带token
			UserContext.clearContext();
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// response中添加用户token
		logger.debug("request uri: {}", request.getRequestURI());
		UserPrincipal principal = UserContext.getContext();
		logger.debug("principal: {}", principal);
		if (principal != null && StringUtils.isNotBlank(principal.getToken())) {
			response.setHeader(HEADER_NAME_USER_TOKEN, Encodes.encodeBase64(principal.getToken().getBytes()));
		}
		logger.debug("response {}={}", HEADER_NAME_USER_TOKEN, response.getHeader(HEADER_NAME_USER_TOKEN));
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
