package net.ipetty.sdk;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.NotificationVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * NotificationApiTest
 * 
 * @author luocanfeng
 * @date 2014年8月1日
 */
public class NotificationApiTest extends BaseApiTest {

	NotificationApi notificationApi = new NotificationApiImpl(ApiContext.getInstance());
	UserApi userApi = new UserApiImpl(ApiContext.getInstance());

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testGetMyNotification() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		NotificationVO notification = notificationApi.getMyNotification();
		Assert.assertNotNull(notification);
		logger.debug("notification={}", notification);
	}

}
