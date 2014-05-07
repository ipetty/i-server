package net.ipetty.user.service;

import javax.annotation.Resource;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.util.SaltEncoder;
import net.ipetty.user.domain.User;
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
public class UserService {

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
			if (StringUtils.isBlank(user.getAccount()) && StringUtils.isBlank(user.getPhoneNumber())
					&& StringUtils.isBlank(user.getEmail()) && StringUtils.isBlank(user.getQzoneUid())
					&& StringUtils.isBlank(user.getWeiboUid())) {
				// No account field been setted
				throw new BusinessException("Excepted at least one account field been setted.");
			}

			// verify password
			if (StringUtils.isBlank(user.getPassword())) {
				throw new BusinessException("Password must not be empty.");
			}

			// check unique
			this.checkUnique(user.getAccount(), "Account");
			this.checkUnique(user.getPhoneNumber(), "Phone Number");
			this.checkUnique(user.getEmail(), "Email");
			this.checkUnique(user.getQzoneUid(), "Qzone Uid");
			this.checkUnique(user.getWeiboUid(), "Weibo Uid");

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

			// find the id back
			User result = userDao.getByUid(user.getUid());
			user.setId(result.getId());

			// persist user profile
			user.getProfile().setUserId(user.getId());
			userProfileDao.save(user.getProfile());
		}
	}

	private void checkUnique(String fieldValue, String fieldLabel) throws BusinessException {
		if (StringUtils.isNotBlank(fieldValue)) {
			User orignal = userDao.getByLoginName(fieldValue);
			if (orignal != null) {
				throw new BusinessException(fieldLabel + " already exist.");
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
	 * 根据爱宠帐号获取用户帐号
	 */
	public User getByAccount(String account) {
		return userDao.getByAccount(account);
	}

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户帐号
	 */
	public User getByLoginName(String loginName) {
		return userDao.getByLoginName(loginName);
	}

}
