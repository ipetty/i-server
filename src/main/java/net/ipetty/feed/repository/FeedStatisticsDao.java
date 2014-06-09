package net.ipetty.feed.repository;

import java.util.List;

import net.ipetty.feed.domain.FeedStatistics;

/**
 * FeedStatisticsDao
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public interface FeedStatisticsDao {

	/**
	 * 保存消息统计信息
	 */
	public void save(FeedStatistics feedStatistics);

	/**
	 * 更新消息统计信息
	 */
	public void update(FeedStatistics feedStatistics);

	/**
	 * 根据消息ID获取消息统计信息
	 */
	public FeedStatistics getStatisticsByFeedId(Long feedId);

	/**
	 * 根据消息ID获取消息统计信息
	 */
	public List<FeedStatistics> listStatisticsByFeedIds(Long... feedIds);

	/**
	 * 重新统计评论数
	 */
	public void recountCommentCount(Long feedId);

	/**
	 * 重新统计赞的数目
	 */
	public void recountFavorCount(Long feedId);

}
