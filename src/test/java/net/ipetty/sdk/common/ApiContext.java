package net.ipetty.sdk.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * 应用容器，单例，线程安全，存放API范围内公共变量
 * 
 * @author xiaojinghai
 */
public class ApiContext {

	private final RestTemplate restTemplate;

	private Boolean authorized;

	private Integer currUserId;

	// 文件服务器地址
	public static final String FILE_SERVER_BASE = "http://localhost:8080";

	// API服务器地址
	public static final String API_SERVER_BASE = "http://localhost:8080/api";

	private static ApiContext instance;

	private static final Charset UTF8 = Charset.forName("UTF-8");

	private ApiContext() {
		authorized = false;

		restTemplate = new RestTemplate();
		/**
		 * 只能在Android端使用 //避免HttpURLConnection的http.keepAlive Bug
		 * HttpComponentsClientHttpRequestFactory factory = new
		 * HttpComponentsClientHttpRequestFactory();
		 * factory.setConnectTimeout(10 * 1000); factory.setReadTimeout(30 *
		 * 1000); restTemplate.setRequestFactory(factory);
		 */

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter(UTF8));
		// messageConverters.add(new MappingJackson2HttpMessageConverter());

		MappingJacksonHttpMessageConverter mjm = new MappingJacksonHttpMessageConverter();
		mjm.getObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		messageConverters.add(mjm);

		restTemplate.setMessageConverters(messageConverters);

		restTemplate.setErrorHandler(new ApiExceptionHandler());

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new ApiInterceptor());
		restTemplate.setInterceptors(interceptors);
		if (restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
			((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(10 * 1000);
			((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(10 * 1000);
		} else if (restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
			((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(10 * 1000);
			((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(10 * 1000);
		}
	}

	public static synchronized ApiContext getInstance() {
		if (instance == null) {
			instance = new ApiContext();
		}
		return instance;
	}

	public synchronized RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public synchronized Boolean isAuthorized() {
		return authorized;
	}

	public synchronized void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}

	public synchronized Integer getCurrUserId() {
		return currUserId;
	}

	public synchronized void setCurrUserId(Integer currUserId) {
		this.currUserId = currUserId;
	}

}
