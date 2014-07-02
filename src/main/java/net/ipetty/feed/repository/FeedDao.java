package net.ipetty.feed.repository;

import java.util.Date;
import java.util.List;

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

	/**
	 * 删除消息
	 */
	public void delete(Long id);

	/**
	 * 根据时间线分页获取消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Feed> listByTimelineWithPage(Date timeline, int pageNumber, int pageSize);

	/**
	 * 根据时间线分页获取与我相关的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Feed> listByUserIdAndTimelineWithPage(Integer userId, Date timeline, int pageNumber, int pageSize);

	/**
	 * 根据时间线分页获取指定用户的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Feed> listByAuthorIdAndTimelineWithPage(Integer userId, Date timeline, int pageNumber, int pageSize);

}
