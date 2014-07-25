package net.ipetty.sdk;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.AppUpdateVO;

import org.springframework.util.LinkedMultiValueMap;

/**
 * AppUpdateApiImpl
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
public class AppUpdateApiImpl extends BaseApi implements AppUpdateApi {

	public AppUpdateApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_GET_LATEST_VERSION = "/app/checkUpdate";

	/**
	 * 检查应用版本信息
	 */
	@Override
	public AppUpdateVO latestVersion(String appKey) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("appKey", appKey);
		return context.getRestTemplate().getForObject(buildUri(URI_GET_LATEST_VERSION, request), AppUpdateVO.class);
	}

}
