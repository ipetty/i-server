package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.activity.domain.ActivityType;
import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.cache.annotation.UpdateToCache;
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
			// follower_num, feed_num, comment_num, favor_num, login_num
			UserStatistics statistics = new UserStatistics();
			statistics.setUserId(rs.getInt("user_id"));
			statistics.setBonusOfHistory(rs.getInt("bonus_of_history"));
			statistics.setBonusCurrent(rs.getInt("bonus_current"));
			statistics.setFriendsNum(rs.getInt("friends_num"));
			statistics.setFollowerNum(rs.getInt("follower_num"));
			statistics.setFeedNum(rs.getInt("feed_num"));
			statistics.setCommentNum(rs.getInt("comment_num"));
			statistics.setFavorNum(rs.getInt("favor_num"));
			statistics.setLoginNum(rs.getInt("login_num"));
			return statistics;
		}
	};

	private static final String SAVE_SQL = "insert into user_statistics(user_id, bonus_of_history, bonus_current, friends_num, follower_num, feed_num, comment_num, favor_num, login_num) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户统计信息
	 */
	public void save(UserStatistics statistics) {
		super.getJdbcTemplate()
				.update(SAVE_SQL, statistics.getUserId(), statistics.getBonusOfHistory(), statistics.getBonusCurrent(),
						statistics.getFriendsNum(), statistics.getFollowerNum(), statistics.getFeedNum(),
						statistics.getCommentNum(), statistics.getFavorNum(), statistics.getLoginNum());
	}

	private static final String GET_SQL = "select * from user_statistics where user_id=?";

	/**
	 * 获取用户统计信息
	 */
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public UserStatistics get(Integer userId) {
		return super.queryUniqueEntity(GET_SQL, ROW_MAPPER, userId);
	}

	private static final String UPDATE_SQL = "update user_statistics set bonus_of_history=?, bonus_current=?, friends_num=?, follower_num=?, feed_num=?, comment_num=?, favor_num=?, login_num=? where user_id=?";

	/**
	 * 更新用户统计信息
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${statistics.userId}")
	public void update(UserStatistics statistics) {
		super.getJdbcTemplate().update(UPDATE_SQL, statistics.getBonusOfHistory(), statistics.getBonusCurrent(),
				statistics.getFriendsNum(), statistics.getFollowerNum(), statistics.getFeedNum(),
				statistics.getCommentNum(), statistics.getFavorNum(), statistics.getLoginNum(), statistics.getUserId());
	}

	private static final String RECOUNT_RELATIONSHIP_NUM_SQL = "update user_statistics set friends_num=(select count(*) from user_relationship where follower_id=?),"
			+ " follower_num=(select count(*) from user_relationship where friend_id=?) where user_id=?";

	/**
	 * 更新指定用户的关注数、粉丝数
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public void recountRelationshipNum(Integer userId) {
		super.getJdbcTemplate().update(RECOUNT_RELATIONSHIP_NUM_SQL, userId, userId, userId);
	}

	private static final String RECOUNT_FEED_NUM_SQL = "update user_statistics set feed_num=(select count(*) from feed where created_by=? and deleted=false) where user_id=?";

	/**
	 * 更新指定用户的发布消息数目
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public void recountFeedNum(Integer userId) {
		super.getJdbcTemplate().update(RECOUNT_FEED_NUM_SQL, userId, userId);
	}

	private static final String RECOUNT_COMMENT_NUM_SQL = "update user_statistics set comment_num=(select count(*) from feed_comment where created_by=? and deleted=false) where user_id=?";

	/**
	 * 更新指定用户的发布评论数目
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public void recountCommentNum(Integer userId) {
		super.getJdbcTemplate().update(RECOUNT_COMMENT_NUM_SQL, userId, userId);
	}

	private static final String RECOUNT_FEED_FAVORS_NUM_SQL = "update user_statistics set favor_num=(select count(*) from feed_favor where created_by=?) where user_id=?";

	/**
	 * 更新指定用户发出赞的数目
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public void recountFeedFavorNum(Integer userId) {
		super.getJdbcTemplate().update(RECOUNT_FEED_FAVORS_NUM_SQL, userId, userId);
	}

	private static final String RECOUNT_LOGIN_NUM_SQL = "update user_statistics set login_num=(select count(*) from activity where created_by=? and type=?) where user_id=?";

	/**
	 * 更新指定用户登录次数
	 */
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_STATISTICS, key = "${userId}")
	public void recountLoginNum(Integer userId) {
		super.getJdbcTemplate().update(RECOUNT_LOGIN_NUM_SQL, userId, ActivityType.LOGIN, userId);
	}

}
