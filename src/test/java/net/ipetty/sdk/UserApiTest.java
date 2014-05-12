package net.ipetty.sdk;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.HumanGender;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月6日
 */
public class UserApiTest extends BaseTest {

	UserApi userApi = new UserApiImpl(ApiContext.getInstance("1", "1"));

	private static final String TEST_ACCOUNT_EMAIL = "luocanfeng@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final int TEST_ACCOUNT_UID = 3;
	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testRegister() {
		RegisterVO register = new RegisterVO("registerWithApiTest@ipetty.net", "888888", "通过API注册用户", HumanGender.MALE);
		logger.debug("register {}", register);
		UserVO user = userApi.register(register);
		Assert.assertNotNull(user);
		logger.debug("register success {}", user);
	}

	@Test
	public void testLogin() {
		UserVO user = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		Assert.assertNotNull(user);
		logger.debug("login success {}", user);
	}

	@Test
	public void testCheckEmailAvailable() {
		String email = TEST_ACCOUNT_EMAIL;
		boolean result = userApi.checkEmailAvailable(email);
		logger.debug("check email available for {}, result is {}", email, result);
		Assert.assertFalse(result);

		email = "checkEmailAvailableTest@ipetty.net";
		result = userApi.checkEmailAvailable(email);
		logger.debug("check email available for {}, result is {}", email, result);
		Assert.assertTrue(result);
	}

	@Test
	public void testGetById() {
		UserVO user = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		user = userApi.getById(user.getId());
		Assert.assertNotNull(user);
		logger.debug("get by id '{}', result is {}", user.getId(), user);
	}

	@Test
	public void testGetByUid() {
		UserVO user = userApi.getByUid(TEST_ACCOUNT_UID);
		Assert.assertNotNull(user);
		logger.debug("get by uid '{}', result is {}", TEST_ACCOUNT_UID, user);
	}

	@Test
	public void testGetByUniqueName() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Assert.assertNotNull(user);
		logger.debug("get by unique name '{}', result is {}", TEST_ACCOUNT_UNIQUE_NAME, user);
	}

	@Test
	public void testUpdateUniqueName() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		try {
			userApi.updateUniqueName(user.getId(), TEST_ACCOUNT_UNIQUE_NAME);
		} catch (Exception e) {
			// e.printStackTrace();
			Assert.assertTrue(true);
		}
		try {
			userApi.updateUniqueName(user.getId(), "_testUpdateUniqueNameWithApi");
		} catch (Exception e) {
			// e.printStackTrace();
			Assert.assertTrue(true);
		}
	}

}
