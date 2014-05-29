package net.ipetty.core.web.rest.interceptor;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ipetty.core.cache.BaseHazelcastCache;
import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.util.Encodes;
import net.ipetty.core.util.UUIDUtils;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.user.domain.User;
import net.ipetty.user.domain.UserRefreshToken;
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
	public static final String HEADER_NAME_REFRESH_TOKEN = "refresh_token";
	public static final String HEADER_NAME_DEVICE_ID = "device_id";
	public static final String HEADER_NAME_DEVICE_MAC = "device_mac";
	public static final String HEADER_NAME_DEVICE_UUID = "device_uuid";
	public static final Charset UTF8 = Charset.forName("UTF-8");

	private Logger logger = LoggerFactory.getLogger(getClass());

	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("request uri: {}", request.getRequestURI());
		String encodedUserToken = request.getHeader(HEADER_NAME_USER_TOKEN);
		String encodedRefreshToken = request.getHeader(HEADER_NAME_REFRESH_TOKEN);
		// String encodedDeviceId = request.getHeader(HEADER_NAME_DEVICE_ID);
		// String encodedDeviceMac = request.getHeader(HEADER_NAME_DEVICE_MAC);
		String encodedDeviceUuid = request.getHeader(HEADER_NAME_DEVICE_UUID);

		if (this.setUserContextByUserToken(encodedUserToken)) {
			return true;
		}

		if (this.setUserContextByRefreshToken(encodedRefreshToken, encodedDeviceUuid)) {
			return true;
		}

		if (StringUtils.isNotBlank(encodedUserToken) || StringUtils.isNotBlank(encodedRefreshToken)) {
			throw new RestException("Token无效或已过期，请重新登录");
		}

		return true;
	}

	private boolean setUserContextByUserToken(String encodedUserToken) {
		if (StringUtils.isBlank(encodedUserToken)) {
			return false;
		}

		String userToken = new String(Encodes.decodeBase64(encodedUserToken), UTF8);
		logger.debug("request header: {}={}", HEADER_NAME_USER_TOKEN, userToken);

		// 通过user_token获取对应用户信息
		Integer userId = BaseHazelcastCache.get(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, userToken);
		if (userId == null) {
			return false;
		}

		// user_token有效，将对应用户信息写入到线程安全的UserContext中
		User user = userService.getById(userId);
		UserPrincipal principal = UserPrincipal.fromUser(user, userToken);
		UserContext.setContext(principal);
		logger.debug("set user context {} by user token {}", UserContext.getContext(), userToken);
		return true;
	}

	private boolean setUserContextByRefreshToken(String encodedRefreshToken, String encodedDeviceUuid) {
		if (StringUtils.isBlank(encodedRefreshToken) || StringUtils.isBlank(encodedDeviceUuid)) {
			return false;
		}

		String refreshToken = new String(Encodes.decodeBase64(encodedRefreshToken), UTF8);
		String deviceUuid = new String(Encodes.decodeBase64(encodedDeviceUuid), UTF8);
		logger.debug("request header: {}={}", HEADER_NAME_REFRESH_TOKEN, refreshToken);
		logger.debug("request header: {}={}", HEADER_NAME_DEVICE_UUID, deviceUuid);

		UserRefreshToken userRefreshToken = userService.getRefreshToken(refreshToken);
		if (userRefreshToken != null && StringUtils.equals(deviceUuid, userRefreshToken.getDeviceUuid())) {
			// refresh_token有效，则认为用户已正常登录，换取新的user_token，并设置用户上下文
			User user = userService.getById(userRefreshToken.getUserId());
			UserPrincipal principal = UserPrincipal.fromUser(user, UUIDUtils.generateShortUUID());
			UserContext.setContext(principal);
			logger.debug("set user context {} by refresh token {}", UserContext.getContext(), refreshToken);
			return true;
		}

		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("request uri: {}", request.getRequestURI());
		// String encodedUserToken = request.getHeader(HEADER_NAME_USER_TOKEN);
		String encodedRefreshToken = request.getHeader(HEADER_NAME_REFRESH_TOKEN);
		String encodedDeviceId = request.getHeader(HEADER_NAME_DEVICE_ID);
		String encodedDeviceMac = request.getHeader(HEADER_NAME_DEVICE_MAC);
		String encodedDeviceUuid = request.getHeader(HEADER_NAME_DEVICE_UUID);

		UserPrincipal principal = UserContext.getContext();
		logger.debug("principal: {}", principal);
		if (principal != null && StringUtils.isNotBlank(principal.getToken())) {
			// response中设置user_token
			response.setHeader(HEADER_NAME_USER_TOKEN, Encodes.encodeBase64(principal.getToken().getBytes()));
			logger.debug("set response header: {}={}", HEADER_NAME_USER_TOKEN, principal.getToken());

			// 在登录新设备时生成refresh_token
			if (StringUtils.isBlank(encodedRefreshToken) && StringUtils.isNotBlank(encodedDeviceUuid)) {
				Integer userId = principal.getId();
				String deviceUuid = new String(Encodes.decodeBase64(encodedDeviceUuid), UTF8);
				String deviceId = StringUtils.isNotBlank(encodedDeviceId) ? new String(
						Encodes.decodeBase64(encodedDeviceId), UTF8) : null;
				String deviceMac = StringUtils.isNotBlank(encodedDeviceMac) ? new String(
						Encodes.decodeBase64(encodedDeviceMac), UTF8) : null;

				UserRefreshToken userRefreshToken = userService.get(userId, deviceUuid);
				if (userRefreshToken == null) { // 用户在此设备上未登录过
					userRefreshToken = new UserRefreshToken(userId, null, deviceId, deviceMac, deviceUuid,
							UUIDUtils.generateShortUUID());
					userService.save(userRefreshToken);

					// response中设置refresh_token
					response.setHeader(HEADER_NAME_REFRESH_TOKEN,
							Encodes.encodeBase64(userRefreshToken.getRefreshToken().getBytes()));
					logger.debug("set response header: {}={}", HEADER_NAME_REFRESH_TOKEN,
							userRefreshToken.getRefreshToken());
				}
			}
		}

		// 在登出时删除refresh_token
		if (StringUtils.isNotBlank(encodedRefreshToken)
				&& (principal == null || StringUtils.isBlank(principal.getToken()))) {
			userService.delete(new String(Encodes.decodeBase64(encodedRefreshToken), UTF8));
		}
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
