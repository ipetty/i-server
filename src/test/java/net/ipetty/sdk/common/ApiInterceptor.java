package net.ipetty.sdk.common;

import java.io.IOException;
import java.nio.charset.Charset;

import net.ipetty.util.Encodes;

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
	public static final Charset UTF8 = Charset.forName("UTF-8");

	// TODO 这里测试时使用简单的字符串作为用户上下文，存储当前用户的user_token，实际Android客户端中应该将用户上下文存储到本地系统中
	private static String USER_CONTEXT;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public ApiInterceptor() {
	}

	/**
	 * 在每个请求中加入Basic验证头信息
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		// retrieve user token from user context
		String userToken = USER_CONTEXT;
		if (StringUtils.isNotBlank(userToken)) {
			request.getHeaders().set(HEADER_NAME_USER_TOKEN, Encodes.encodeBase64(userToken.getBytes(UTF8)));
		}
		// request.getHeaders().setAcceptEncoding(ContentCodingType.GZIP);

		ClientHttpResponse response = execution.execute(request, body);

		// retrieve user token from response and compare to user context,
		// and then update it.
		String encodedUserToken = response.getHeaders().getFirst(HEADER_NAME_USER_TOKEN);
		logger.debug("response encoded user token is: {}", encodedUserToken);
		if (StringUtils.isNotBlank(encodedUserToken)) {
			String responseUserToken = new String(Encodes.decodeBase64(encodedUserToken), UTF8);
			if (!StringUtils.equals(responseUserToken, userToken)) {
				USER_CONTEXT = responseUserToken;
				logger.debug("set USER_CONTEXT: {}", USER_CONTEXT);
			}
		}

		return response;
	}

}
