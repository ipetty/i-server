package net.ipetty.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.activity.domain.ActivityType;
import net.ipetty.core.context.SpringContextHelper;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.service.BaseService;
import net.ipetty.core.util.ImageUtils;
import net.ipetty.core.util.SaltEncoder;
import net.ipetty.user.domain.User;
import net.ipetty.user.domain.UserProfile;
import net.ipetty.user.domain.UserRefreshToken;
import net.ipetty.user.domain.UserStatistics;
import net.ipetty.user.domain.UserZone;
import net.ipetty.user.repository.UserDao;
import net.ipetty.user.repository.UserProfileDao;
import net.ipetty.user.repository.UserRefreshTokenDao;
import net.ipetty.user.repository.UserRelationshipDao;
import net.ipetty.user.repository.UserStatisticsDao;
import net.ipetty.user.repository.UserZoneDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * UserService
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
@Service
@Transactional
public class UserService extends BaseService {

	private static final String SECRETARY_ACCOUNT_EMAIL = "service@ipetty.net";
	private static final String PLATFORM_SINA_WEIBO = "SinaWeibo";
	private static final String PLATFORM_QZONE = "QZone";
	private static final String PLATFORM_WECHAT = "Wechat";

	@Resource
	private UserDao userDao;

	@Resource
	private UserRefreshTokenDao refreshTokenDao;

	@Resource
	private UserProfileDao userProfileDao;

	@Resource
	private UserZoneDao userZoneDao;

	@Resource
	private UserRelationshipDao relationshipDao;

	@Resource
	private UserStatisticsDao userStatisticsDao;

	@Resource
	private UidService uidService;

	/**
	 * 登录验证
	 */
	@ProduceActivity(type = ActivityType.LOGIN, createdBy = "${return.id}")
	public User login(String username, String password) throws BusinessException {
		Assert.hasText(username, "用户名不能为空");
		Assert.hasText(password, "密码不能为空");

		User user = this.getByLoginName(username);
		if (user == null) {
			throw new BusinessException("用户名不存在");
		}

		String encodedPassword = SaltEncoder.encode(password, user.getSalt());
		if (!StringUtils.equals(user.getEncodedPassword(), encodedPassword)) {
			throw new BusinessException("密码错误");
		}

		return user;
	}

	/**
	 * 使用第三方帐号登陆
	 */
	public User login3rd(String platform, String platformUserId) {
		// 如果帐号已存在则登录成功
		Integer userId = null;
		User user = null;
		if (PLATFORM_QZONE.equals(platform)) {
			userId = userDao.getUserIdByQZoneUserId(platformUserId);
		} else if (PLATFORM_SINA_WEIBO.equals(platform)) {
			userId = userDao.getUserIdBySinaWeiboUserId(platformUserId);
		}
		if (userId != null) {
			user = userDao.getById(userId);
			return user;
		}

		// 如果没有帐号则创建帐号
		return this.create3rdAccount(platform, platformUserId);
	}

	public User create3rdAccount(String platform, String platformUserId) {
		User user = new User();

		// retreive an available uid
		int uid = uidService.getUid();
		user.setUid(uid);

		if (PLATFORM_QZONE.equals(platform)) {
			user.setQzoneUid(platformUserId);
		} else if (PLATFORM_SINA_WEIBO.equals(platform)) {
			user.setWeiboUid(platformUserId);
		}

		// persist user
		userDao.save(user);

		// mark the uid as used
		uidService.markAsUsed(uid);

		// persist other info of user
		this.persistOtherInfoOfUser(user);

		return user;
	}

	/**
	 * 注册帐号
	 */
	@ProduceActivity(type = ActivityType.SIGN_UP, createdBy = "${user.id}")
	public void register(User user) throws BusinessException {
		synchronized (uidService) {
			// verify accounts feilds
			if (StringUtils.isBlank(user.getUniqueName()) && StringUtils.isBlank(user.getPhoneNumber())
					&& StringUtils.isBlank(user.getEmail()) && StringUtils.isBlank(user.getQzoneUid())
					&& StringUtils.isBlank(user.getWeiboUid())) {
				// No account field been setted
				throw new BusinessException("必须至少设置一个帐号字段");
			}

			// verify password
			Assert.hasText(user.getPassword(), "密码不能为空");

			// check unique
			this.checkUniqueForRegister(user.getUniqueName(), "爱宠号");
			this.checkUniqueForRegister(user.getPhoneNumber(), "手机号");
			this.checkUniqueForRegister(user.getEmail(), "邮箱");
			this.checkUniqueForRegister(user.getQzoneUid(), "QQ空间Uid");
			this.checkUniqueForRegister(user.getWeiboUid(), "新浪微博Uid");

			// retreive an available uid
			int uid = uidService.getUid();
			user.setUid(uid);

			// generate salt
			String salt = SaltEncoder.generateSalt();
			user.setSalt(salt);

			// encode password
			user.setEncodedPassword(SaltEncoder.encode(user.getPassword(), user.getSalt()));
			user.setPassword(user.getEncodedPassword());

			// persist user
			userDao.save(user);

			// mark the uid as used
			uidService.markAsUsed(uid);

			// persist other info of user
			this.persistOtherInfoOfUser(user);
		}
	}

	private void persistOtherInfoOfUser(User user) {
		// persist user profile
		if (user.getProfile() == null) {
			user.setProfile(new UserProfile());
		}
		user.getProfile().setUserId(user.getId());
		userProfileDao.save(user.getProfile());

		// persist user zone
		user.setZone(new UserZone(user.getId()));
		userZoneDao.save(user.getZone());

		// TODO persist user zone statistics

		// TODO persist user preferences

		// persist user statistics
		user.setStatistics(new UserStatistics(user.getId()));
		userStatisticsDao.save(user.getStatistics());

		// TODO persist user roles

		// 关注小秘书
		Integer secretaryId = userDao.getUserIdByLoginName(SECRETARY_ACCOUNT_EMAIL);
		if (secretaryId == null) {
			logger.error("小秘书帐号{}不存在！", SECRETARY_ACCOUNT_EMAIL);
		} else {
			relationshipDao.follow(secretaryId, user.getId());
			userStatisticsDao.recountRelationshipNum(secretaryId);
			userStatisticsDao.recountRelationshipNum(user.getId());
			logger.debug("{} follow {}", user.getId(), secretaryId);
		}
	}

	private void checkUniqueForRegister(String fieldValue, String fieldLabel) throws BusinessException {
		if (StringUtils.isNotBlank(fieldValue)) {
			User orignal = this.getByLoginName(fieldValue);
			if (orignal != null) {
				throw new BusinessException(fieldLabel + "已存在");
			}
		}
	}

	/**
	 * 根据ID获取用户帐号
	 */
	public User getById(Integer id) {
		Assert.notNull(id, "ID不能为空");
		User user = userDao.getById(id);
		if (user == null) {
			throw new BusinessException("指定ID（" + id + "）的用户不存在");
		}
		user.setProfile(userProfileDao.getByUserId(id));
		user.setZone(userZoneDao.getByUserId(id));
		user.setStatistics(userStatisticsDao.get(id));
		return user;
	}

	/**
	 * 根据uid获取用户帐号
	 */
	public User getByUid(int uid) {
		Assert.notNull(uid, "UID不能为空");
		Integer id = userDao.getUserIdByUid(uid);
		if (id == null) {
			throw new BusinessException("指定UID（" + uid + "）的用户不存在");
		}
		return this.getById(id);
	}

	/**
	 * 根据爱宠号获取用户帐号
	 */
	public User getByUniqueName(String uniqueName) {
		Assert.hasText(uniqueName, "爱宠号不能为空");
		Integer id = userDao.getUserIdByUniqueName(uniqueName);
		if (id == null) {
			throw new BusinessException("指定爱宠号（" + uniqueName + "）的用户不存在");
		}
		return this.getById(id);
	}

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户帐号
	 */
	public User getByLoginName(String loginName) {
		Assert.hasText(loginName, "帐号不能为空");
		Integer id = userDao.getUserIdByLoginName(loginName);
		return id == null ? null : this.getById(id);
	}

	/**
	 * 设置爱宠号，只能设置一次，一经设置不能变更
	 */
	@ProduceActivity(type = ActivityType.UPDATE_UNIQUE_NAME, createdBy = "${id}")
	public void updateUniqueName(Integer id, String uniqueName) {
		Assert.notNull(id, "用户ID不能为空");
		Assert.hasText(uniqueName, "爱宠号不能为空");
		if (StringUtils.isNotBlank(uniqueName)) { // 校验唯一性
			User orignalUniqueName = this.getByLoginName(uniqueName);
			if (orignalUniqueName != null) {
				throw new BusinessException("爱宠号已存在");
			}
		}

		User user = userDao.getById(id);
		if (user == null) {
			throw new BusinessException("指定ID（" + id + "）的用户不存在");
		}

		if (StringUtils.isNotBlank(user.getUniqueName())) { // 是否已设置；不允许重复设置
			throw new BusinessException("爱宠号已设置，一经设置不能变更");
		}

		userDao.updateUniqueName(id, uniqueName);
	}

	/**
	 * 修改密码
	 */
	@ProduceActivity(type = ActivityType.CHANGE_PASSWORD, createdBy = "${id}")
	public void changePassword(Integer id, String oldPassword, String newPassword) {
		Assert.notNull(id, "用户ID不能为空");
		Assert.hasText(oldPassword, "旧密码不能为空");
		Assert.hasText(newPassword, "新密码不能为空");

		User user = userDao.getById(id);
		if (user == null) {
			throw new BusinessException("指定ID（" + id + "）的用户不存在");
		}

		String oldEncodedPassword = SaltEncoder.encode(oldPassword, user.getSalt());
		if (!StringUtils.equals(oldEncodedPassword, user.getEncodedPassword())) {
			throw new BusinessException("原密码不匹配");
		}

		String newEncodedPassword = SaltEncoder.encode(newPassword, user.getSalt());
		userDao.changePassword(id, newEncodedPassword);
	}

	/**
	 * 关注
	 */
	@ProduceActivity(type = ActivityType.FOLLOW, createdBy = "${followerId}", targetId = "${friendId}")
	public void follow(Integer friendId, Integer followerId) {
		Assert.notNull(friendId, "被关注人ID不能为空");
		Assert.notNull(followerId, "关注人ID不能为空");
		Assert.isTrue(!friendId.equals(followerId), "不能关注自己");

		if (relationshipDao.get(friendId, followerId) != null) {
			throw new BusinessException("您已关注该用户");
		}
		relationshipDao.follow(friendId, followerId);
	}

	/**
	 * 是否已关注，true为已关注，false为未关注
	 */
	public boolean isFollow(Integer friendId, Integer followerId) {
		Assert.notNull(friendId, "被关注人ID不能为空");
		Assert.notNull(followerId, "关注人ID不能为空");

		return relationshipDao.get(friendId, followerId) != null;
	}

	/**
	 * 取消关注
	 */
	@ProduceActivity(type = ActivityType.UNFOLLOW, createdBy = "${followerId}", targetId = "${friendId}")
	public void unfollow(Integer friendId, Integer followerId) {
		Assert.notNull(friendId, "被关注人ID不能为空");
		Assert.notNull(followerId, "关注人ID不能为空");

		if (relationshipDao.get(friendId, followerId) == null) {
			throw new BusinessException("您尚未关注该用户");
		}
		relationshipDao.unfollow(friendId, followerId);
	}

	/**
	 * 分页获取关注列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<User> listFriends(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<User> users = new ArrayList<User>();
		List<Integer> userIds = relationshipDao.listFriends(userId, pageNumber, pageSize);
		if (CollectionUtils.isNotEmpty(userIds)) {
			for (Integer id : userIds) {
				users.add(this.getById(id));
			}
		}
		return users;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<User> listFollowers(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<User> users = new ArrayList<User>();
		List<Integer> userIds = relationshipDao.listFollowers(userId, pageNumber, pageSize);
		if (CollectionUtils.isNotEmpty(userIds)) {
			for (Integer id : userIds) {
				users.add(this.getById(id));
			}
		}
		return users;
	}

	/**
	 * 获取好友列表（双向关注）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<User> listBiFriends(Integer userId, int pageNumber, int pageSize) {
		Assert.notNull(userId, "用户ID不能为空");

		List<User> users = new ArrayList<User>();
		List<Integer> userIds = relationshipDao.listBiFriends(userId, pageNumber, pageSize);
		if (CollectionUtils.isNotEmpty(userIds)) {
			for (Integer id : userIds) {
				users.add(this.getById(id));
			}
		}
		return users;
	}

	/**
	 * 更新用户头像
	 */
	@ProduceActivity(type = ActivityType.UPDATE_AVATAR, createdBy = "${userId}")
	public String updateAvatar(MultipartFile imageFile, Integer userId, int userUid) {
		try {
			String avatarUrl = ImageUtils
					.saveImageFile(imageFile, SpringContextHelper.getWebContextRealPath(), userUid);
			UserProfile profile = userProfileDao.getByUserId(userId);
			profile.setAvatar(avatarUrl);
			userProfileDao.update(profile);
			return profile.getAvatar();
		} catch (IOException e) {
			throw new BusinessException("保存图片时出错", e);
		}
	}

	/**
	 * 更新个人空间背景图片
	 */
	@ProduceActivity(type = ActivityType.UPDATE_BACKGROUND, createdBy = "${userId}")
	public String updateBackground(MultipartFile imageFile, Integer userId, int userUid) {
		try {
			String imageUrl = ImageUtils.saveImageFile(imageFile, SpringContextHelper.getWebContextRealPath(), userUid);
			UserProfile profile = userProfileDao.getByUserId(userId);
			profile.setBackground(imageUrl);
			userProfileDao.update(profile);
			return profile.getBackground();
		} catch (IOException e) {
			throw new BusinessException("保存图片时出错", e);
		}
	}

	/**
	 * 更新个人信息（不含头像、背景图片的更新）
	 */
	public UserProfile updateProfile(UserProfile profile) {
		Assert.notNull(profile, "用户个人信息不能为空");
		Assert.notNull(profile.getUserId(), "用户ID不能为空");
		UserProfile original = userProfileDao.getByUserId(profile.getUserId());
		profile.setAvatar(original.getAvatar());
		profile.setBackground(original.getBackground());
		userProfileDao.update(profile);
		return profile;
	}

	/**
	 * 保存RefreshToken
	 */
	public void saveRefreshToken(UserRefreshToken refreshToken) {
		refreshTokenDao.save(refreshToken);
	}

	/**
	 * 获取RefreshToken
	 */
	public UserRefreshToken getRefreshToken(String refreshToken) {
		return refreshTokenDao.get(refreshToken);
	}

	/**
	 * 获取指定用户在指定设备上的RefreshToken
	 */
	public UserRefreshToken getRefreshToken(Integer userId, String deviceUuid) {
		return refreshTokenDao.get(userId, deviceUuid);
	}

	/**
	 * 删除RefreshToken
	 */
	public void deleteRefreshToken(String refreshToken) {
		refreshTokenDao.delete(refreshToken);
	}

}
