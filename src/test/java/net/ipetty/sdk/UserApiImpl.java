package net.ipetty.sdk;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserFormVO;
import net.ipetty.vo.UserStatisticsVO;
import net.ipetty.vo.UserVO;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * UserApiImpl
 * 
 * @author luocanfeng
 * @date 2014年5月6日
 */
public class UserApiImpl extends BaseApi implements UserApi {

	public UserApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_LOGIN = "/login";

	/**
	 * 用户登陆验证
	 */
	@Override
	public UserVO login(String username, String password) {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("username", username);
		request.set("password", password);
		UserVO user = context.getRestTemplate().postForObject(buildUri(URI_LOGIN), request, UserVO.class);
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());
		return user;
	}

	private static final String URI_LOGIN_3RD = "/login3rd";

	/**
	 * 使用第三方帐号登陆
	 */
	public UserVO login3rd(String platform, String userId) {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("platform", platform);
		request.set("userId", userId);
		UserVO user = context.getRestTemplate().postForObject(buildUri(URI_LOGIN_3RD), request, UserVO.class);
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());
		return user;
	}

	private static final String URI_LOGIN_OR_REGISTER_3RD = "/loginOrRegister3rd";

	/**
	 * 使用第三方帐号登陆或注册后登登陆返回
	 */
	public UserVO loginOrRegister3rd(String platform, String userId, String email, String userName) {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("platform", platform);
		request.set("userId", userId);
		request.set("email", email);
		request.set("userName", userName);
		UserVO user = context.getRestTemplate().postForObject(buildUri(URI_LOGIN_OR_REGISTER_3RD), request,
				UserVO.class);
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());
		return user;
	}

	private static final String URI_LOGOUT = "/logout";

	/**
	 * 登出
	 */
	@Override
	public void logout() {
		context.getRestTemplate().getForObject(buildUri(URI_LOGOUT), Boolean.class);
		context.setAuthorized(false);
		context.setCurrUserId(null);
		// FIXME 这个在客户端应该删除本地存储
		ApiContext.REFRESH_TOKEN = null;
		ApiContext.USER_CONTEXT = null;
		logger.debug("set REFRESH_TOKEN: {}", ApiContext.REFRESH_TOKEN);
		logger.debug("set USER_CONTEXT: {}", ApiContext.USER_CONTEXT);
	}

	private static final String URI_REGISTER = "/register";

	/**
	 * 注册
	 */
	@Override
	public UserVO register(RegisterVO register) {
		return context.getRestTemplate().postForObject(buildUri(URI_REGISTER), register, UserVO.class);
	}

	private static final String URI_CHECK_EMAIL_AVAILABLE = "/user/checkEmailAvailable";

	/**
	 * 检查用户名是否可用，true表示可用，false表示不可用
	 */
	@Override
	public boolean checkEmailAvailable(String email) {
		return context.getRestTemplate().getForObject(buildUri(URI_CHECK_EMAIL_AVAILABLE, "email", email),
				Boolean.class);
	}

	private static final String URI_GET_BY_ID = "/user/id/{id}";

	/**
	 * 根据ID获取用户帐号
	 */
	@Override
	public UserVO getById(Integer id) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_BY_ID, UserVO.class, id);
	}

	private static final String URI_GET_USER_STATISTICS_BY_USER_ID = "/user/statistics/{userId}";

	/**
	 * 根据用户ID获取用户统计信息
	 */
	public UserStatisticsVO getUserStatisticsByUserId(Integer userId) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_USER_STATISTICS_BY_USER_ID,
				UserStatisticsVO.class, userId);
	}

	private static final String URI_GET_BY_UID = "/user/uid/{uid}";

	/**
	 * 根据uid获取用户帐号
	 */
	@Override
	public UserVO getByUid(int uid) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_BY_UID, UserVO.class, uid);
	}

	private static final String URI_GET_BY_UNIQUE_NAME = "/user/{uniqueName}";

	/**
	 * 根据爱宠号获取用户帐号
	 */
	@Override
	public UserVO getByUniqueName(String uniqueName) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_BY_UNIQUE_NAME,
				UserVO.class, uniqueName);
	}

	private static final String URI_UPDATE_UNIQUE_NAME = "/user/uniqueName";

	/**
	 * 设置爱宠号，只能设置一次，一经设置不能变更
	 */
	@Override
	public boolean updateUniqueName(String uniqueName) {
		super.requireAuthorization();

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("uniqueName", uniqueName);
		return context.getRestTemplate().postForObject(buildUri(URI_UPDATE_UNIQUE_NAME), request, Boolean.class);
	}

	private static final String URI_CHANGE_PASSWORD = "/changePassword";

	/**
	 * 修改密码
	 */
	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		super.requireAuthorization();

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("oldPassword", oldPassword);
		request.set("newPassword", newPassword);
		return context.getRestTemplate().postForObject(buildUri(URI_CHANGE_PASSWORD), request, Boolean.class);
	}

	private static final String URI_FOLLOW = "/user/follow";

	/**
	 * 关注
	 */
	@Override
	public boolean follow(Integer friendId) {
		super.requireAuthorization();

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("friendId", String.valueOf(friendId));
		return context.getRestTemplate().postForObject(buildUri(URI_FOLLOW), request, Boolean.class);
	}

	private static final String URI_IS_FOLLOW = "/user/isfollow";

	/**
	 * 是否已关注，true为已关注，false为未关注
	 */
	@Override
	public boolean isFollow(Integer friendId) {
		super.requireAuthorization();

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("friendId", String.valueOf(friendId));
		return context.getRestTemplate().postForObject(buildUri(URI_IS_FOLLOW), request, Boolean.class);
	}

	private static final String URI_IS_UNFOLLOW = "/user/unfollow";

	/**
	 * 取消关注
	 */
	@Override
	public boolean unfollow(Integer friendId) {
		super.requireAuthorization();

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("friendId", String.valueOf(friendId));
		return context.getRestTemplate().postForObject(buildUri(URI_IS_UNFOLLOW), request, Boolean.class);
	}

	private static final String URI_LIST_FRIENDS = "/user/friends";

	/**
	 * 分页获取关注列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<UserVO> listFriends(Integer userId, int pageNumber, int pageSize) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("userId", String.valueOf(userId));
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_FRIENDS, request);
		return Arrays.asList(context.getRestTemplate().getForObject(uri, UserVO[].class));
	}

	private static final String URI_LIST_FOLLOWERS = "/user/followers";

	/**
	 * 获取粉丝列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<UserVO> listFollowers(Integer userId, int pageNumber, int pageSize) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("userId", String.valueOf(userId));
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_FOLLOWERS, request);
		return Arrays.asList(context.getRestTemplate().getForObject(uri, UserVO[].class));
	}

	private static final String URI_LIST_BIFRIENDS = "/user/bifriends";

	/**
	 * 获取好友列表（双向关注）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<UserVO> listBiFriends(Integer userId, int pageNumber, int pageSize) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("userId", String.valueOf(userId));
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_BIFRIENDS, request);
		return Arrays.asList(context.getRestTemplate().getForObject(uri, UserVO[].class));
	}

	private static final String URI_UPDATE_AVATAR = "/user/updateAvatar";

	/**
	 * 更新用户头像
	 */
	@Override
	public String updateAvatar(String imagePath) {
		super.requireAuthorization();

		URI updateAvatarUri = buildUri(URI_UPDATE_AVATAR);
		LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
		request.add("imageFile", new FileSystemResource(imagePath));
		return context.getRestTemplate().postForObject(updateAvatarUri, request, String.class);
	}

	private static final String URI_UPDATE_BACKGROUD = "/user/updateBackground";

	/**
	 * 更新个人空间背景图片
	 */
	@Override
	public String updateBackground(String imagePath) {
		super.requireAuthorization();

		URI updateBackgroundUri = buildUri(URI_UPDATE_BACKGROUD);
		LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
		request.add("imageFile", new FileSystemResource(imagePath));
		return context.getRestTemplate().postForObject(updateBackgroundUri, request, String.class);
	}

	private static final String URI_UPDATE = "/user/update";

	/**
	 * 修改用户个人信息
	 */
	@Override
	public UserVO update(UserFormVO userFormVo) {
		super.requireAuthorization();
		return context.getRestTemplate().postForObject(buildUri(URI_UPDATE), userFormVo, UserVO.class);
	}

}
