package net.ipetty.feed.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.cache.annotation.UpdateToCache;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.feed.domain.FeedStatistics;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * FeedStatisticsDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
@Repository("feedStatisticsDao")
public class FeedStatisticsDaoImpl extends BaseJdbcDaoSupport implements FeedStatisticsDao {

	static final RowMapper<FeedStatistics> ROW_MAPPER = new RowMapper<FeedStatistics>() {
		@Override
		public FeedStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
			// feed_id, comment_count, favor_count
			FeedStatistics statistics = new FeedStatistics();
			statistics.setFeedId(JdbcDaoUtils.getLong(rs, "feed_id"));
			statistics.setCommentCount(rs.getInt("comment_count"));
			statistics.setFavorCount(rs.getInt("favor_count"));
			return statistics;
		}
	};

	private static final String SAVE_SQL = "insert into feed_statistics(feed_id, comment_count, favor_count) values(?, ?, ?)";

	/**
	 * 保存消息统计信息
	 */
	@Override
	public void save(FeedStatistics feedStatistics) {
		super.getJdbcTemplate().update(SAVE_SQL, feedStatistics.getFeedId(), feedStatistics.getCommentCount(),
				feedStatistics.getFavorCount());
	}

	private static final String UPDATE_SQL = "update feed_statistics set comment_count=?, favor_count=? where feed_id=?";

	/**
	 * 更新消息统计信息
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_FEED_STATISTICS, key = "${feedStatistics.feedId}")
	public void update(FeedStatistics feedStatistics) {
		super.getJdbcTemplate().update(UPDATE_SQL, feedStatistics.getCommentCount(), feedStatistics.getFavorCount(),
				feedStatistics.getFeedId());
	}

	private static final String GET_STATISTICS_BY_FEED_ID_SQL = "select * from feed_statistics where feed_id=?";

	/**
	 * 根据消息ID获取消息统计信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_FEED_STATISTICS, key = "${feedId}")
	public FeedStatistics getStatisticsByFeedId(Long feedId) {
		return super.queryUniqueEntity(GET_STATISTICS_BY_FEED_ID_SQL, ROW_MAPPER, feedId);
	}

	private static final String LIST_STATISTICS_BY_FEED_IDS_SQL = "select * from feed_statistics where feed_id in (?)";

	/**
	 * 根据消息ID获取消息统计信息
	 */
	@Override
	public List<FeedStatistics> listStatisticsByFeedIds(Long... feedIds) {
		String inStatement = feedIds.length > 0 ? StringUtils.arrayToCommaDelimitedString(feedIds) : "null";
		return super.getJdbcTemplate().query(LIST_STATISTICS_BY_FEED_IDS_SQL.replace("?", inStatement), ROW_MAPPER);
	}

	private static final String RECOUNT_COMMENT_COUNT_SQL = "update feed_statistics set comment_count=(select count(*) from feed_comment where feed_id=? and deleted=false) where feed_id=?";

	/**
	 * 重新统计评论数
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_FEED_STATISTICS, key = "${feedId}")
	public void recountCommentCount(Long feedId) {
		super.getJdbcTemplate().update(RECOUNT_COMMENT_COUNT_SQL, feedId, feedId);
	}

	private static final String RECOUNT_FAVOR_COUNT_SQL = "update feed_statistics set favor_count=(select count(*) from feed_favor where feed_id=?) where feed_id=?";

	/**
	 * 重新统计赞的数目
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_FEED_STATISTICS, key = "${feedId}")
	public void recountFavorCount(Long feedId) {
		super.getJdbcTemplate().update(RECOUNT_FAVOR_COUNT_SQL, feedId, feedId);
	}

}
