package net.ipetty.user.repository;

import net.ipetty.user.domain.UserRefreshToken;

/**
 * UserRefreshTokenDao
 * 
 * @author luocanfeng
 * @date 2014年5月29日
 */
public interface UserRefreshTokenDao {

	/**
	 * 保存
	 */
	public void save(UserRefreshToken refreshToken);

	/**
	 * 获取RefreshToken
	 */
	public UserRefreshToken get(String refreshToken);

	/**
	 * 获取指定用户在指定设备上的RefreshToken
	 */
	public UserRefreshToken get(Integer userId, String deviceUuid);

	/**
	 * 删除RefreshToken
	 */
	public void delete(String refreshToken);

}
