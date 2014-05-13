package net.ipetty.user.service;

import javax.annotation.Resource;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.service.BaseService;
import net.ipetty.core.util.SaltEncoder;
import net.ipetty.user.domain.User;
import net.ipetty.user.domain.UserProfile;
import net.ipetty.user.repository.UserDao;
import net.ipetty.user.repository.UserProfileDao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private UidService uidService;

	/**
	 * 登录验证
	 */
	public User login(String username, String password) throws BusinessException {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new BusinessException("用户名、密码不能为空");
		}

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
			if (StringUtils.isBlank(user.getPassword())) {
				throw new BusinessException("密码不能为空");
			}

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

			// TODO persist user zone
			// TODO persist user preferences
			// TODO persist user statistics

			// TODO persist user roles
		}
	}

	private void checkUnique(String fieldValue, String fieldLabel) throws BusinessException {
		if (StringUtils.isNotBlank(fieldValue)) {
			User orignal = userDao.getByLoginName(fieldValue);
			if (orignal != null) {
				throw new BusinessException(fieldLabel + "已存在");
			}
		}
	}

	/**
	 * 根据ID获取用户帐号
	 */
	public User getById(Integer id) {
		return userDao.getById(id);
	}

	/**
	 * 根据uid获取用户帐号
	 */
	public User getByUid(int uid) {
		return userDao.getByUid(uid);
	}

	/**
	 * 根据爱宠号获取用户帐号
	 */
	public User getByUniqueName(String uniqueName) {
		return userDao.getByUniqueName(uniqueName);
	}

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户帐号
	 */
	public User getByLoginName(String loginName) {
		return userDao.getByLoginName(loginName);
	}

	/**
	 * 更新用户帐号信息
	 */
	public void update(User user) {
		if (user == null || user.getId() == null) {
			throw new BusinessException("用户不存在");
		}
		userDao.update(user);
	}

	/**
	 * 设置爱宠号，只能设置一次，一经设置不能变更
	 */
	public void updateUniqueName(Integer id, String uniqueName) {
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
	public void changePassword(Integer id, String oldPassword, String newPassword) {
		if (id == null || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
			throw new BusinessException("用户ID与密码不能为空");
		}

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

}
