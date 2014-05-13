package net.ipetty.feed.repository;

import java.util.List;

import net.ipetty.feed.domain.Comment;

/**
 * CommentDao
 * 
 * @author luocanfeng
 * @date 2014年5月13日
 */
public interface CommentDao {

	/**
	 * 保存评论
	 */
	public void save(Comment comment);

	/**
	 * 根据ID获取评论信息
	 */
	public Comment getById(Long id);

	/**
	 * 获取指定主题消息的所有评论列表
	 */
	public List<Comment> listByFeedId(Long feedId);

	/**
	 * 获取指定主题消息列表的所有评论列表
	 */
	public List<Comment> listByFeedIds(Long... feedIds);

}
