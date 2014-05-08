package net.ipetty.sdk.common;

import java.net.URI;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * API基类, 提供统一RestTemplate对象和一些常用方法
 * 
 * @author xiaojinghai
 */
public class BaseApi {

	protected ApiContext context;

	private static final LinkedMultiValueMap<String, String> EMPTY_PARAMETERS = new LinkedMultiValueMap<String, String>();

	public BaseApi(ApiContext context) {
		this.context = context;
	}

	protected URI buildUri(String path) {
		return buildUri(path, EMPTY_PARAMETERS);
	}

	protected URI buildUri(String path, String parameterName, String parameterValue) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.set(parameterName, parameterValue);
		return buildUri(path, parameters);
	}

	protected URI buildUri(String path, MultiValueMap<String, String> parameters) {
		return URIBuilder.fromUri(ApiContext.API_SERVER_BASE + path).queryParams(parameters).build();
	}

	protected void requireAuthorization() {
		if (!context.isAuthorized()) {
			throw new MissingAuthorizationException();
		}
	}

}
