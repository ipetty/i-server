package net.ipetty.sdk;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.LocationFormVO;
import net.ipetty.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
public class FeedApiTest extends BaseTest {

	FeedApi feedApi = new FeedApiImpl(ApiContext.getInstance("1", "1"));
	UserApi userApi = new UserApiImpl(ApiContext.getInstance("1", "1"));

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testPublish() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test text");
		feedForm.setImagePath("C:\\Users\\Administrator\\Desktop\\logos\\test\\test.jpg");
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

}
