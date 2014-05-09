package net.ipetty.feed.repository;

import net.ipetty.feed.domain.Feed;

/**
 * FeedDao
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public interface FeedDao {

	/**
	 * 保存消息
	 */
	public void save(Feed feed);

	/**
	 * 根据ID获取消息
	 */
	public Feed getById(Long id);

}
