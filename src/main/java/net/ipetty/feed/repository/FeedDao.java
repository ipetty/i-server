package net.ipetty.feed.repository;

import java.util.Date;
import java.util.List;

import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.FeedStatistics;

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
	 * 根据消息ID获取消息统计信息
	 */
	public FeedStatistics getStatisticsByFeedId(Long feedId);

	/**
	 * 根据消息ID获取消息统计信息
	 */
	public List<FeedStatistics> listStatisticsByFeedIds(Long... feedIds);

}
