package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.notify.domain.Notification;
import net.ipetty.notify.service.NotificationService;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;

import org.junit.Assert;
import org.junit.Test;

/**
 * NotificationServiceTest
 * 
 * @author luocanfeng
 * @date 2014年7月31日
 */
public class NotificationServiceTest extends BaseTest {

	@Resource
	private NotificationService notificationService;

	@Resource
	private UserService userService;

	@Test
	public void testAll() {
		User user = new User();
		String email = "testSave@ipetty.net";
		user.setEmail(email);
		user.setPassword("888888");
		userService.register(user);
		Assert.assertNotNull(user.getId());
		Assert.assertNotNull(user.getCreatedOn());

		Notification notification = notificationService.getNotification(user.getId());
		Assert.assertNotNull(notification);
		Assert.assertNotNull(notification.getUserId());

		notification.setNewFansNum(1);
		notificationService.update(notification);
		notification = notificationService.getNotification(user.getId());
		Assert.assertEquals(1, notification.getNewFansNum());
	}

}
