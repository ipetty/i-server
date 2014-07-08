package net.ipetty.sdk;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.FeedbackVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedbackApiTest
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public class FeedbackApiTest extends BaseApiTest {

	FeedbackApi feedbackApi = new FeedbackApiImpl(ApiContext.getInstance());
	UserApi userApi = new UserApiImpl(ApiContext.getInstance());

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testFeedbackAnonymously() {
		userApi.logout();

		FeedbackVO feedback = feedbackApi.feedback("testFeedbackAnonymouslyFromApi", "content", "contact");
		Assert.assertNotNull(feedback);
		Assert.assertNotNull(feedback.getId());
	}

	@Test
	public void testFeedback() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedbackVO feedback = feedbackApi.feedback("testFeedbackFromApi", "content", "contact");
		Assert.assertNotNull(feedback);
		Assert.assertNotNull(feedback.getId());
	}

}
