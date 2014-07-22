package net.ipetty.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.core.util.SaltEncoder;
import net.ipetty.user.domain.User;
import net.ipetty.user.domain.UserProfile;
import net.ipetty.user.service.UserService;

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
		Assert.assertNotNull(user.getCreatedOn());

		User result = userService.getByLoginName(email);
		Assert.assertNotNull(result);
		Assert.assertEquals(user.getEncodedPassword(), result.getEncodedPassword());
	}

	@Test
	public void testLogin() {
		User user = userService.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);
		Assert.assertNotNull(user);
		Assert.assertEquals(SaltEncoder.encode(TEST_ACCOUNT_PASSWORD, user.getSalt()), user.getEncodedPassword());
		logger.debug("userVo is {}", user.toVO());
	}

	@Test
	public void testLogin3rd() {
		User user = userService.login3rd("SinaWeibo", "12345");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		Integer userId = user.getId();

		user = userService.login3rd("SinaWeibo", "12345");
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		Assert.assertEquals(userId, user.getId());
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
		userService.changePassword(user.getId(), "888888", "888888");
		user = userService.getById(user.getId());
		Assert.assertEquals(SaltEncoder.encode("888888", user.getSalt()), user.getEncodedPassword());
	}

	@Test
	public void testFollowAndIsFollowAndUnfollow() {
		User user1 = new User();
		user1.setEmail("testFollowAndIsFollowAndUnfollow1@ipetty.net");
		user1.setPassword("888888");
		userService.register(user1);
		User user2 = new User();
		user2.setEmail("testFollowAndIsFollowAndUnfollow2@ipetty.net");
		user2.setPassword("888888");
		userService.register(user2);

		userService.follow(user2.getId(), user1.getId());
		Assert.assertTrue(userService.isFollow(user2.getId(), user1.getId()));

		userService.follow(user1.getId(), user2.getId());
		Assert.assertTrue(userService.isFollow(user1.getId(), user2.getId()));

		userService.unfollow(user2.getId(), user1.getId());
		Assert.assertFalse(userService.isFollow(user2.getId(), user1.getId()));
		Assert.assertTrue(userService.isFollow(user1.getId(), user2.getId()));
	}

	@Test
	public void testFollowSelf() {
		try {
			User user1 = new User();
			user1.setEmail("testFollowSelf1@ipetty.net");
			user1.setPassword("888888");
			userService.register(user1);
			userService.follow(user1.getId(), user1.getId());
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testListFriendsAndFollowersAndBiFriends() {
		User user1 = new User();
		user1.setEmail("testListFriendsAndFollowersAndBi..1@ipetty.net");
		user1.setPassword("888888");
		userService.register(user1);
		User user2 = new User();
		user2.setEmail("testListFriendsAndFollowersAndBi..2@ipetty.net");
		user2.setPassword("888888");
		userService.register(user2);

		userService.follow(user2.getId(), user1.getId());
		Assert.assertTrue(userService.isFollow(user2.getId(), user1.getId()));
		userService.follow(user1.getId(), user2.getId());
		Assert.assertTrue(userService.isFollow(user1.getId(), user2.getId()));

		List<User> users = userService.listFriends(user1.getId(), 0, 20);
		Assert.assertEquals(2, users.size());
		users = userService.listFriends(user2.getId(), 0, 20);
		Assert.assertEquals(2, users.size());
		users = userService.listFollowers(user1.getId(), 0, 20);
		Assert.assertEquals(1, users.size());
		users = userService.listFollowers(user2.getId(), 0, 20);
		Assert.assertEquals(1, users.size());
		users = userService.listBiFriends(user1.getId(), 0, 20);
		Assert.assertEquals(1, users.size());
		users = userService.listBiFriends(user2.getId(), 0, 20);
		Assert.assertEquals(1, users.size());

		userService.unfollow(user2.getId(), user1.getId());
		Assert.assertFalse(userService.isFollow(user2.getId(), user1.getId()));
		Assert.assertTrue(userService.isFollow(user1.getId(), user2.getId()));

		users = userService.listFriends(user1.getId(), 0, 20);
		Assert.assertEquals(1, users.size());
		users = userService.listFriends(user2.getId(), 0, 20);
		Assert.assertEquals(2, users.size());
		users = userService.listFollowers(user1.getId(), 0, 20);
		Assert.assertEquals(1, users.size());
		users = userService.listFollowers(user2.getId(), 0, 20);
		Assert.assertEquals(0, users.size());
		users = userService.listBiFriends(user1.getId(), 0, 20);
		Assert.assertEquals(0, users.size());
		users = userService.listBiFriends(user2.getId(), 0, 20);
		Assert.assertEquals(0, users.size());
	}

	@Test
	public void testUpdateProfile() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		UserProfile profile = user.getProfile();
		profile.setNickname("luocanfeng");
		userService.updateProfile(profile);
		user = userService.getById(user.getId());
		Assert.assertEquals("luocanfeng", user.getProfile().getNickname());
	}

}
