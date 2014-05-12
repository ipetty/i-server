package net.ipetty.sdk;

import java.net.URI;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.LocationFormVO;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;

/**
 * FeedApiImpl
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
public class FeedApiImpl extends BaseApi implements FeedApi {

	public FeedApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_PUBLISH = "/feed/publish";

	/**
	 * 发布消息
	 */
	@Override
	public FeedVO publish(FeedFormVO feed) {
		super.requireAuthorization();
		URI uri = buildUri(URI_PUBLISH);
		LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
		request.add("text", feed.getText());
		request.add("imageFile", new FileSystemResource(feed.getImagePath()));
		LocationFormVO location = feed.getLocation();
		request.add("longitude", String.valueOf(location.getLongitude()));
		request.add("latitude", String.valueOf(location.getLatitude()));
		request.add("address", location.getAddress());
		return context.getRestTemplate().postForObject(uri, request, FeedVO.class);
	}

}
