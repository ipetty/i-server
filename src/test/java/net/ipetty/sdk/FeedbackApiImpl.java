package net.ipetty.sdk;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.FeedbackVO;

import org.springframework.util.LinkedMultiValueMap;

/**
 * FeedbackApiImpl
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public class FeedbackApiImpl extends BaseApi implements FeedbackApi {

	public FeedbackApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_FEEDBACK = "/feedback";

	/**
	 * 反馈意见
	 */
	@Override
	public FeedbackVO feedback(String title, String content, String contact) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("title", title);
		request.add("content", content);
		request.add("contact", contact);
		return context.getRestTemplate().postForObject(buildUri(URI_FEEDBACK), request, FeedbackVO.class);
	}

}
