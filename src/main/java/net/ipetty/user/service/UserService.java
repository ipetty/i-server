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
import net.ipetty.user.domain.UserStatistics;
import net.ipetty.user.domain.UserZone;
import net.ipetty.user.repository.UserDao;
import net.ipetty.user.repository.UserProfileDao;
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

	@Resource
	private UserDao userDao;

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
		return StringUtils.equals(user.getEncodedPassword(), encodedPassword) ? user : null;
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
			this.checkUnique(user.getUniqueName(), "爱宠号");
			this.checkUnique(user.getPhoneNumber(), "手机号");
			this.checkUnique(user.getEmail(), "邮箱");
			this.checkUnique(user.getQzoneUid(), "QQ空间Uid");
			this.checkUnique(user.getWeiboUid(), "新浪微博Uid");

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
		}
	}

	// FIXME update帐号字段时校验唯一性的逻辑还需改善
	private void checkUnique(String fieldValue, String fieldLabel) throws BusinessException {
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
		this.checkUnique(uniqueName, "Unique Name"); // 校验唯一性

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

}
