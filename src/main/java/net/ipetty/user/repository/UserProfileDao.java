package net.ipetty.user.repository;

import net.ipetty.user.domain.UserProfile;

/**
 * UserProfileDao
 * 
 * @author luocanfeng
 * @date 2014年5月7日
 */
public interface UserProfileDao {

	/**
	 * 保存用户个人信息
	 */
	public void save(UserProfile profile);

	/**
	 * 根据用户ID获取用户个人信息
	 */
	public UserProfile getByUserId(Integer userId);

	/**
	 * 更新用户个人信息
	 */
	public void update(UserProfile profile);

}
