package net.ipetty.user.repository;

import net.ipetty.user.domain.User;

/**
 * UserDao
 * @author luocanfeng
 * @date 2014年5月4日
 */
public interface UserDao {

	/**
	 * 保存用户帐号
	 */
	public void save(User user);

	/**
	 * 根据ID获取用户帐号
	 */
	public User getById(Integer id);

	/**
	 * 根据微信 userId获取用户ID
	 */
	public Integer getUserIdByWechatUserId(String wechatUserId);

	/**
	 * 根据QZone userId获取用户ID
	 */
	public Integer getUserIdByQZoneUserId(String qzoneUserId);

	/**
	 * 根据新浪微博userId获取用户ID
	 */
	public Integer getUserIdBySinaWeiboUserId(String sinaWeiboUserId);

	/**
	 * 根据uid获取用户ID
	 */
	public Integer getUserIdByUid(int uid);

	/**
	 * 根据爱宠号获取用户ID
	 */
	public Integer getUserIdByUniqueName(String uniqueName);

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户ID
	 */
	public Integer getUserIdByLoginName(String loginName);

	/**
	 * 更新用户帐号信息
	 */
	public void update(User user);

	/**
	 * 更新邮箱
	 */
	public void updateEmail(Integer id, String email);

	/**
	 * 更新爱宠号
	 */
	public void updateUniqueName(Integer id, String uniqueName);

	/**
	 * 修改密码
	 */
	public void changePassword(Integer id, String newEncodedPassword);

	/**
	 * 修改密码与盐值
	 */
	public void changePasswordWithSalt(Integer id, String newEncodedPassword, String salt);

}
