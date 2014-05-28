package net.ipetty.user.repository;

import net.ipetty.user.domain.UserStatistics;

/**
 * UserStatisticsDao
 * 
 * @author luocanfeng
 * @date 2014年5月28日
 */
public interface UserStatisticsDao {

	/**
	 * 保存用户统计信息
	 */
	public void save(UserStatistics statistics);

	/**
	 * 获取用户统计信息
	 */
	public UserStatistics get(Integer userId);

	/**
	 * 更新用户统计信息
	 */
	public void update(UserStatistics statistics);

}
