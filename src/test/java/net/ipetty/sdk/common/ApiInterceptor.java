package net.ipetty.sdk.common;

import java.io.IOException;
import java.nio.charset.Charset;

import net.ipetty.core.util.Encodes;
import net.ipetty.core.util.UUIDUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Api请求拦截
 * 
 * @author xiaojinghai
 */
public class ApiInterceptor implements ClientHttpRequestInterceptor {

	public static final String HEADER_NAME_USER_TOKEN = "user_token";
	public static final String HEADER_NAME_REFRESH_TOKEN = "refresh_token";
	public static final String HEADER_NAME_DEVICE_ID = "device_id";
	public static final String HEADER_NAME_DEVICE_MAC = "device_mac";
	public static final String HEADER_NAME_DEVICE_UUID = "device_uuid";
	public static final Charset UTF8 = Charset.forName("UTF-8");

	private static String DEVICE_UUID = UUIDUtils.generateShortUUID();
	private static String DEVICE_ID = UUIDUtils.generateShortUUID();
	private static String DEVICE_MAC = UUIDUtils.generateShortUUID();

	private Logger logger = LoggerFactory.getLogger(getClass());

	public ApiInterceptor() {
	}

	/**
	 * 在每个请求中加入Basic验证头信息
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		logger.debug("request uri: {}", request.getURI());

		// request.getHeaders().setAcceptEncoding(ContentCodingType.GZIP);
		// 设置user_token
		if (StringUtils.isNotBlank(ApiContext.USER_CONTEXT)) {
			request.getHeaders().set(HEADER_NAME_USER_TOKEN,
					Encodes.encodeBase64(ApiContext.USER_CONTEXT.getBytes(UTF8)));
			logger.debug("set user token: {}", ApiContext.USER_CONTEXT);
		}
		if (StringUtils.isNotBlank(ApiContext.REFRESH_TOKEN)) {
			request.getHeaders().set(HEADER_NAME_REFRESH_TOKEN,
					Encodes.encodeBase64(ApiContext.REFRESH_TOKEN.getBytes(UTF8)));
			logger.debug("set refresh token: {}", ApiContext.REFRESH_TOKEN);
		}
		request.getHeaders().set(HEADER_NAME_DEVICE_UUID, Encodes.encodeBase64(DEVICE_UUID.getBytes(UTF8)));
		request.getHeaders().set(HEADER_NAME_DEVICE_ID, Encodes.encodeBase64(DEVICE_ID.getBytes(UTF8)));
		request.getHeaders().set(HEADER_NAME_DEVICE_MAC, Encodes.encodeBase64(DEVICE_MAC.getBytes(UTF8)));

		ClientHttpResponse response = execution.execute(request, body);

		// 如果返回的user_token发生变化，则更新本地user_token
		String encodedUserToken = response.getHeaders().getFirst(HEADER_NAME_USER_TOKEN);
		if (StringUtils.isNotBlank(encodedUserToken)) {
			String responseUserToken = new String(Encodes.decodeBase64(encodedUserToken), UTF8);
			logger.debug("response user token is: {}", responseUserToken);
			if (!StringUtils.equals(responseUserToken, ApiContext.USER_CONTEXT)) {
				ApiContext.USER_CONTEXT = responseUserToken;
				logger.debug("set USER_CONTEXT: {}", ApiContext.USER_CONTEXT);
			}
		} else {
			ApiContext.USER_CONTEXT = null;
			logger.debug("set USER_CONTEXT: {}", ApiContext.USER_CONTEXT);
		}

		// 如果之前没有refresh_token，返回了refresh_token，则保存refresh_token到本地
		String encodedRefreshToken = response.getHeaders().getFirst(HEADER_NAME_REFRESH_TOKEN);
		if (StringUtils.isBlank(ApiContext.REFRESH_TOKEN) && StringUtils.isNotBlank(encodedRefreshToken)) {
			ApiContext.REFRESH_TOKEN = new String(Encodes.decodeBase64(encodedRefreshToken), UTF8);
			logger.debug("set REFRESH_TOKEN: {}", ApiContext.REFRESH_TOKEN);
		}

		return response;
	}

}
