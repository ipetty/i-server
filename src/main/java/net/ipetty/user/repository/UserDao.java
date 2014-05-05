package net.ipetty.user.repository;

import net.ipetty.user.domain.User;

/**
 * UserDao
 * 
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
	 * 根据uid获取用户帐号
	 */
	public User getByUid(int uid);

	/**
	 * 根据爱宠帐号获取用户帐号
	 */
	public User getByAccount(String account);

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户帐号
	 */
	public User getByLoginName(String loginName);

}
