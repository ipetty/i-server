package net.ipetty.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.service.ActivityService;
import net.ipetty.core.test.BaseTest;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.ActivityVO;

import org.junit.Test;

/**
 * ActivityServiceTest
 * 
 * @author luocanfeng
 * @date 2014年6月10日
 */
public class ActivityServiceTest extends BaseTest {

	@Resource
	private ActivityService activityService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testListActivities() {
		List<ActivityVO> activities = activityService.listActivities(
				userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME).getId(), 0, 10000);
		logger.debug("User {} has {} activities.", TEST_ACCOUNT_UNIQUE_NAME, activities.size());
	}

	@Test
	public void testListRelatedActivities() {
		List<ActivityVO> activities = activityService.listRelatedActivities(
				userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME).getId(), 0, 10000);
		logger.debug("User {} has {} related activities.", TEST_ACCOUNT_UNIQUE_NAME, activities.size());
	}

}
