package net.ipetty.user.repository;

import java.util.List;

import net.ipetty.user.domain.UserRelationship;

/**
 * UserRelationshipDao
 * 
 * @author luocanfeng
 * @date 2014年5月14日
 */
public interface UserRelationshipDao {

	/**
	 * 关注
	 */
	public void follow(Integer friendId, Integer followerId);

	/**
	 * 获取关注信息
	 */
	public UserRelationship get(Integer friendId, Integer followerId);

	/**
	 * 取消关注
	 */
	public void unfollow(Integer friendId, Integer followerId);

	/**
	 * 分页获取关注列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Integer> listFriends(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取粉丝列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Integer> listFollowers(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取好友列表（双向关注）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Integer> listBiFriends(Integer userId, int pageNumber, int pageSize);

}
