package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromHazelcast;
import net.ipetty.core.cache.annotation.UpdateToHazelcast;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.user.domain.UserStatistics;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserStatisticsDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月28日
 */
@Repository("userStatisticsDao")
public class UserStatisticsDaoImpl extends BaseJdbcDaoSupport implements UserStatisticsDao {

	static final RowMapper<UserStatistics> ROW_MAPPER = new RowMapper<UserStatistics>() {
		@Override
		public UserStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, bonus_of_history, bonus_current, friends_num,
			// follower_num, feed_num, comment_num, favor_num
			UserStatistics statistics = new UserStatistics();
			statistics.setUserId(rs.getInt("user_id"));
			statistics.setBonusOfHistory(rs.getInt("bonus_of_history"));
			statistics.setBonusCurrent(rs.getInt("bonus_current"));
			statistics.setFriendsNum(rs.getInt("friends_num"));
			statistics.setFollowerNum(rs.getInt("follower_num"));
			statistics.setFeedNum(rs.getInt("feed_num"));
			statistics.setCommentNum(rs.getInt("comment_num"));
			statistics.setFavorNum(rs.getInt("favor_num"));
			return statistics;
		}
	};

	private static final String SAVE_SQL = "insert into user_statistics(user_id, bonus_of_history, bonus_current, friends_num, follower_num, feed_num, comment_num, favor_num) values(?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户统计信息
	 */
	public void save(UserStatistics statistics) {
		super.getJdbcTemplate().update(SAVE_SQL, statistics.getUserId(), statistics.getBonusOfHistory(),
				statistics.getBonusCurrent(), statistics.getFriendsNum(), statistics.getFollowerNum(),
				statistics.getFeedNum(), statistics.getCommentNum(), statistics.getFavorNum());
	}

	private static final String GET_SQL = "select * from user_statistics where user_id=?";

	/**
	 * 获取用户统计信息
	 */
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public UserStatistics get(Integer userId) {
		return super.queryUniqueEntity(GET_SQL, ROW_MAPPER, userId);
	}

	private static final String UPDATE_SQL = "update user_statistics set bonus_of_history=?, bonus_current=?, friends_num=?, follower_num=?, feed_num=?, comment_num=?, favor_num=? where user_id=?";

	/**
	 * 更新用户统计信息
	 */
	@UpdateToHazelcast(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${statistics.userId}")
	public void update(UserStatistics statistics) {
		super.getJdbcTemplate().update(UPDATE_SQL, statistics.getBonusOfHistory(), statistics.getBonusCurrent(),
				statistics.getFriendsNum(), statistics.getFollowerNum(), statistics.getFeedNum(),
				statistics.getCommentNum(), statistics.getFavorNum(), statistics.getUserId());
	}

}
