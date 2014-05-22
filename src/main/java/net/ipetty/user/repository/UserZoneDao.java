package net.ipetty.user.repository;

import net.ipetty.user.domain.UserZone;

/**
 * UserZoneDao
 * 
 * @author luocanfeng
 * @date 2014年5月22日
 */
public interface UserZoneDao {

	/**
	 * 保存用户空间
	 */
	public void save(UserZone userZone);

	/**
	 * 根据用户ID获取用户空间
	 */
	public UserZone getByUserId(Integer userId);

}
