package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.user.domain.User;
import net.ipetty.user.domain.UserStatistics;
import net.ipetty.user.service.UserService;
import net.ipetty.user.service.UserStatisticsService;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserStatisticsServiceTest
 * 
 * @author luocanfeng
 * @date 2014年6月24日
 */
public class UserStatisticsServiceTest extends BaseTest {

	@Resource
	private UserService userService;

	@Resource
	private UserStatisticsService userStatisticsService;

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testGetByUserId() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		UserStatistics userStatistics = userStatisticsService.getByUserId(user.getId());
		Assert.assertNotNull(userStatistics);
	}

}
