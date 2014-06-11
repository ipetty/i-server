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

	/**
	 * 更新指定用户的关注数、粉丝数
	 */
	public void recountRelationshipNum(Integer userId);

	/**
	 * 更新指定用户的发布消息数目
	 */
	public void recountFeedNum(Integer userId);

	/**
	 * 更新指定用户的发布评论数目
	 */
	public void recountCommentNum(Integer userId);

	/**
	 * 更新指定用户发出赞的数目
	 */
	public void recountFeedFavorNum(Integer userId);

	/**
	 * 更新指定用户登录次数
	 */
	public void recountLoginNum(Integer userId);

}
