package net.ipetty.sdk;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.ActivityVO;

import org.springframework.util.LinkedMultiValueMap;

/**
 * ActivityApiImpl
 * 
 * @author luocanfeng
 * @date 2014年6月10日
 */
public class ActivityApiImpl extends BaseApi implements ActivityApi {

	public ActivityApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_LIST_ACTIVITIES = "/activity/mine";

	/**
	 * 获取我的事件流
	 */
	public List<ActivityVO> listActivities(int pageNumber, int pageSize) {
		super.requireAuthorization();

		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_ACTIVITIES, request);
		return Arrays.asList(context.getRestTemplate().getForObject(uri, ActivityVO[].class));
	}

	private static final String URI_LIST_RELATED_ACTIVITIES = "/activity/related";

	/**
	 * 获取某人相关的事件流
	 */
	public List<ActivityVO> listRelatedActivities(int pageNumber, int pageSize) {
		super.requireAuthorization();

		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_RELATED_ACTIVITIES, request);
		return Arrays.asList(context.getRestTemplate().getForObject(uri, ActivityVO[].class));
	}

}
