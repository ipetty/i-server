package net.ipetty.user.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTestWithDBUnit;
import net.ipetty.core.util.SaltEncoder;
import net.ipetty.user.domain.User;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
public class UserServiceTest extends BaseTestWithDBUnit {

	@Resource
	private UserService userService;

	@Test
	public void testRegister() {
		User user = new User();
		String email = "test@ipetty.net";
		user.setEmail(email);
		user.setPassword("password");
		userService.register(user);

		User result = userService.getByLoginName(email);
		Assert.assertNotNull(result);
		Assert.assertEquals(user.getEncodedPassword(), result.getEncodedPassword());
	}

	@Test
	public void testLogin() {
		String username = "luocanfeng@ipetty.net";
		String password = "888888";
		User user = userService.login(username, password);

		Assert.assertNotNull(user);
		Assert.assertEquals(SaltEncoder.encode(password, user.getSalt()), user.getEncodedPassword());
		logger.debug("userVo is {}", user.toUserVO());
	}

}
