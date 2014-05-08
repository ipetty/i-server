package net.ipetty.user.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
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
public class UserServiceTest extends BaseTest {

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_EMAIL = "luocanfeng@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final int TEST_ACCOUNT_UID = 3;
	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testRegister() {
		User user = new User();
		String email = "registerWithServiceTest@ipetty.net";
		user.setEmail(email);
		user.setPassword("888888");
		userService.register(user);

		User result = userService.getByLoginName(email);
		Assert.assertNotNull(result);
		Assert.assertEquals(user.getEncodedPassword(), result.getEncodedPassword());
	}

	@Test
	public void testLogin() {
		User user = userService.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		Assert.assertNotNull(user);
		Assert.assertEquals(SaltEncoder.encode(TEST_ACCOUNT_PASSWORD, user.getSalt()), user.getEncodedPassword());
		logger.debug("userVo is {}", user.toUserVO());
	}

	@Test
	public void testGetByUid() {
		User user = userService.getByUid(TEST_ACCOUNT_UID);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST_ACCOUNT_EMAIL, user.getEmail());
	}

	@Test
	public void testGetByUniqueName() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Assert.assertNotNull(user);
		Assert.assertEquals(TEST_ACCOUNT_EMAIL, user.getEmail());
	}

	@Test
	public void testUpdate() {
		String phoneNumber = "13800138000";
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		user.setPhoneNumber(phoneNumber);
		userService.update(user);
		user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Assert.assertEquals("13800138000", user.getPhoneNumber());
	}

	@Test
	public void testUpdateUniqueName() {
		try {
			User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
			userService.updateUniqueName(user.getId(), "testuniquename");
			Assert.assertTrue(false);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}

		User user = new User();
		String email = "updateUniqueNameTest@ipetty.net";
		user.setEmail(email);
		user.setPassword("888888");
		userService.register(user);
		userService.updateUniqueName(user.getId(), "updateUniqueNameTest");
		user = userService.getById(user.getId());
		Assert.assertEquals("updateUniqueNameTest", user.getUniqueName());
	}

	@Test
	public void testChangePassword() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		userService.changePassword(user.getId(), "888888", "123456");
		user = userService.getById(user.getId());
		Assert.assertEquals(SaltEncoder.encode("123456", user.getSalt()), user.getEncodedPassword());
	}

}
