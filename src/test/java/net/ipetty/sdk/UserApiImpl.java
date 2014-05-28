package net.ipetty.sdk;

import java.net.URI;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.RegisterVO;
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

	private static final String URI_LOGOUT = "/logout";

	/**
	 * 登出
	 */
	@Override
	public void logout() {
		context.getRestTemplate().getForObject(buildUri(URI_LOGOUT), Boolean.class);
		context.setAuthorized(false);
		context.setCurrUserId(null);
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

}
