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
		UserVO user = userApi.login("luocanfeng@ipetty.net", "888888");
		Assert.assertNotNull(user);
		logger.debug("login success {}", user);
	}

	@Test
	public void testCheckEmailAvailable() {
		String email = "luocanfeng@ipetty.net";
		boolean result = userApi.checkEmailAvailable(email);
		logger.debug("check email available for {}, result is {}", email, result);
		Assert.assertFalse(result);

		email = "checkEmailAvailableTest@ipetty.net";
		result = userApi.checkEmailAvailable(email);
		logger.debug("check email available for {}, result is {}", email, result);
		Assert.assertTrue(result);
	}

}
