package net.ipetty.sdk;

import java.util.List;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.ActivityVO;

import org.junit.Test;

/**
 * ActivityApiTest
 * 
 * @author luocanfeng
 * @date 2014年6月10日
 */
public class ActivityApiTest extends BaseApiTest {

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

	@Test
	public void testListNewFansActivities() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<ActivityVO> feeds = activityApi.listNewFansActivities();
		logger.debug("testListNewFansActivities, size is {}", feeds.size());
	}

	@Test
	public void testListNewRepliesActivities() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<ActivityVO> feeds = activityApi.listNewRepliesActivities();
		logger.debug("testListNewRepliesActivities, size is {}", feeds.size());
	}

	@Test
	public void testListNewFavorsActivities() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<ActivityVO> feeds = activityApi.listNewFavorsActivities();
		logger.debug("testListNewFavorsActivities, size is {}", feeds.size());
	}

	@Test
	public void testListNewActivities() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<ActivityVO> feeds = activityApi.listNewActivities();
		logger.debug("testListNewActivities, size is {}", feeds.size());
	}

}
