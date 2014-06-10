package net.ipetty.sdk;

import java.util.List;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.ActivityVO;

import org.junit.Test;

/**
 * ActivityApiTest
 * 
 * @author luocanfeng
 * @date 2014年6月10日
 */
public class ActivityApiTest extends BaseTest {

	ActivityApi activityApi = new ActivityApiImpl(ApiContext.getInstance());
	UserApi userApi = new UserApiImpl(ApiContext.getInstance());

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testListActivities() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<ActivityVO> feeds = activityApi.listActivities(0, 10000);
		logger.debug("testListActivities, size is {}", feeds.size());
	}

	@Test
	public void testListRelatedActivities() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<ActivityVO> feeds = activityApi.listRelatedActivities(0, 10000);
		logger.debug("testListRelatedActivities, size is {}", feeds.size());
	}

}
