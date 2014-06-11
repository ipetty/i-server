package net.ipetty.feed.repository;

import java.util.List;

import net.ipetty.feed.domain.FeedFavor;

/**
 * FeedFavorDao
 * 
 * @author luocanfeng
 * @date 2014年5月13日
 */
public interface FeedFavorDao {

	/**
	 * 保存赞
	 */
	public void save(FeedFavor feedFavor);

	/**
	 * 获取用户对于指定消息的赞
	 */
	public FeedFavor getByUserIdAndFeedId(Integer userId, Long feedId);

	/**
	 * 删除赞
	 */
	public void delete(Long id);

	/**
	 * 根据ID获取赞
	 */
	public FeedFavor getById(Long id);

	/**
	 * 获取指定主题消息的所有赞列表
	 */
	public List<FeedFavor> listByFeedId(Long feedId);

	/**
	 * 获取指定主题消息列表的所有赞列表
	 */
	public List<FeedFavor> listByFeedIds(Long... feedIds);

	/**
	 * 获取指定用户在指定主题消息列表上给出的所有赞列表
	 */
	public List<FeedFavor> listByUserIdAndFeedIds(Integer userId, Long... feedIds);

}
