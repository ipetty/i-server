package net.ipetty.sdk;

import java.util.List;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.PetVO;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserForm43rdVO;
import net.ipetty.vo.UserFormVO;
import net.ipetty.vo.UserStatisticsVO;
import net.ipetty.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * UserApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月6日
 */
public class UserApiTest extends BaseApiTest {

	UserApi userApi = new UserApiImpl(ApiContext.getInstance());
	PetApi petApi = new PetApiImpl(ApiContext.getInstance());

	private static final String TEST_ACCOUNT_EMAIL = "luocanfeng@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";
	private static final int TEST_ACCOUNT_UID = 3;
	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testRegister() {
		RegisterVO register = new RegisterVO("testRegisterWithApi@ipetty.net", "888888", "通过API注册用户", "male");
		logger.debug("register {}", register);
		UserVO user = userApi.register(register);
		Assert.assertNotNull(user);
		logger.debug("register success {}", user);
	}

	@Test
	public void testLogin() {
		UserVO user = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		logger.debug("login success {}", user);
	}

	@Test
	public void testLoginOrRegister3rd() {
		UserVO user = userApi.loginOrRegister3rd("SinaWeibo", "54321", "54321@gmail.com", "test");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		logger.debug("loginOrRegister3rd success {}", user);
		Integer userId = user.getId();

		user = userApi.loginOrRegister3rd("SinaWeibo", "54321", "54321@gmail.com", "test");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		Assert.assertEquals(userId, user.getId());
		logger.debug("loginOrRegister3rd success {}", user);

		user = userApi.login3rd("SinaWeibo", "54321");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		Assert.assertEquals(userId, user.getId());
		logger.debug("login3rd success {}", user);
	}

	@Test
	public void testLoginOrRegister3rdWithoutEmail() {
		UserVO user = userApi.loginOrRegister3rd("SinaWeibo", "noemail54321", null, "test");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		logger.debug("loginOrRegister3rd success {}", user);
	}

	@Test
	public void testImproveUserInfo43rd() {
		UserVO user = userApi.loginOrRegister3rd("SinaWeibo", "123456789", null, "testImproveUserInfo43rd");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		logger.debug("loginOrRegister3rd success {}", user);
		Integer userId = user.getId();

		List<PetVO> pets = petApi.listByUserId(userId);
		PetVO pet = pets.get(0);
		UserForm43rdVO userForm = new UserForm43rdVO(userId, "testImproveUserInfo43rd@gmail.com", "nickname",
				pet.getId(), "petName", "male", "dog");
		userApi.improveUserInfo43rd(userForm);
	}

	@Test
	public void testImproveUserInfo43rdWithoutEmail() {
		UserVO user = userApi.loginOrRegister3rd("SinaWeibo", "12345678911", null,
				"testImproveUserInfo43rdWithoutEmail");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		logger.debug("loginOrRegister3rd success {}", user);
		Integer userId = user.getId();

		List<PetVO> pets = petApi.listByUserId(userId);
		PetVO pet = pets.get(0);
		UserForm43rdVO userForm = new UserForm43rdVO(userId, null, "nickname", pet.getId(), "petName", "male", "dog");
		userApi.improveUserInfo43rd(userForm);
	}

	// @Test
	public void testLoginWithWrongPassword() {
		try {
			userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD + "123456789");
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testLogout() throws InterruptedException {
		UserVO user = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		Assert.assertNotNull(user);
		logger.debug("login success {}", user);
		userApi.logout();
		logger.debug("logout success");
		Thread.sleep(500);
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
	public void testGetUserStatisticsByUserId() {
		UserVO user = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		user = userApi.getById(user.getId());
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		UserStatisticsVO userStatistics = userApi.getUserStatisticsByUserId(user.getId());
		Assert.assertNotNull(userStatistics);
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
		RegisterVO register = new RegisterVO("testUpdateUniqueNameWithApi@ipetty.net", "888888", "通过API注册用户", "male");
		UserVO user = userApi.register(register);
		Assert.assertNotNull(user);

		userApi.login("testUpdateUniqueNameWithApi@ipetty.net", "888888");

		try {
			userApi.updateUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}

		userApi.login("testUpdateUniqueNameWithApi@ipetty.net", "888888");

		Assert.assertTrue(userApi.updateUniqueName("_testUpdateUniqueName"));
	}

	@Test
	public void testChangePassword() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);
		Assert.assertTrue(userApi.changePassword(TEST_ACCOUNT_PASSWORD, TEST_ACCOUNT_PASSWORD));
	}

	@Test
	public void testFollowAndIsFollowAndUnfollow() {
		RegisterVO register1 = new RegisterVO("testFollowWithApi1@ipetty.net", "888888", "通过API注册用户", "male");
		UserVO user1 = userApi.register(register1);
		Assert.assertNotNull(user1);
		RegisterVO register2 = new RegisterVO("testFollowWithApi2@ipetty.net", "888888", "通过API注册用户", "male");
		UserVO user2 = userApi.register(register2);
		Assert.assertNotNull(user2);

		userApi.login("testFollowWithApi1@ipetty.net", "888888");
		userApi.follow(user2.getId());
		Assert.assertTrue(userApi.isFollow(user2.getId()));

		userApi.login("testFollowWithApi2@ipetty.net", "888888");
		userApi.follow(user1.getId());
		Assert.assertTrue(userApi.isFollow(user1.getId()));

		userApi.login("testFollowWithApi1@ipetty.net", "888888");
		userApi.unfollow(user2.getId());
		Assert.assertFalse(userApi.isFollow(user2.getId()));
		userApi.login("testFollowWithApi2@ipetty.net", "888888");
		Assert.assertTrue(userApi.isFollow(user1.getId()));
	}

	@Test
	public void testListRelationships() {
		UserVO user = userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<UserVO> users = userApi.listFriends(user.getId(), 0, 20);
		logger.debug("userApi.listFriends, size={}", users.size());

		users = userApi.listFollowers(user.getId(), 0, 20);
		logger.debug("userApi.listFollowers, size={}", users.size());

		users = userApi.listBiFriends(user.getId(), 0, 20);
		logger.debug("userApi.listBiFriends, size={}", users.size());

		userApi.logout();

		users = userApi.listFriends(user.getId(), 0, 20);
		logger.debug("userApi.listFriends, size={}", users.size());

		users = userApi.listFollowers(user.getId(), 0, 20);
		logger.debug("userApi.listFollowers, size={}", users.size());

		users = userApi.listBiFriends(user.getId(), 0, 20);
		logger.debug("userApi.listBiFriends, size={}", users.size());
	}

	@Test
	public void testUpdateAvatar() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);
		String avatarUrl = userApi.updateAvatar(getTestPhotoPath());
		Assert.assertNotNull(avatarUrl);
	}

	@Test
	public void testUpdateBackground() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);
		String backgroundUrl = userApi.updateBackground(getTestPhotoPath());
		Assert.assertNotNull(backgroundUrl);
	}

	@Test
	public void testUpdateProfile() {
		UserVO userVo = userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);
		UserFormVO userFormVo = new UserFormVO();
		userFormVo.setId(userVo.getId());
		userFormVo.setNickname("testUpdateUNFromApi");
		userApi.update(userFormVo);
	}

	@Test
	public void testDoSomethingWithRefreshToken() {
		// do not login
		String backgroundUrl = userApi.updateBackground(getTestPhotoPath());
		Assert.assertNotNull(backgroundUrl);
	}

}
