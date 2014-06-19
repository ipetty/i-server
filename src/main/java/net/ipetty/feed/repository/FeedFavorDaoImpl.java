package net.ipetty.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.feed.domain.FeedFavor;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * FeedFavorDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月13日
 */
@Repository("feedFavorDao")
public class FeedFavorDaoImpl extends BaseJdbcDaoSupport implements FeedFavorDao {

	static final RowMapper<FeedFavor> ROW_MAPPER = new RowMapper<FeedFavor>() {
		@Override
		public FeedFavor mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_by, created_on, feed_id
			FeedFavor feedFavor = new FeedFavor();
			feedFavor.setId(rs.getLong("id"));
			feedFavor.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			feedFavor.setCreatedOn(rs.getTimestamp("created_on"));
			feedFavor.setFeedId(JdbcDaoUtils.getLong(rs, "feed_id"));
			return feedFavor;
		}
	};

	private static final String SAVE_SQL = "insert into feed_favor(created_by, feed_id, created_on) values(?, ?, ?)";

	/**
	 * 保存赞
	 */
	@Override
	public void save(FeedFavor feedFavor) {
		feedFavor.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setInteger(statement, 1, feedFavor.getCreatedBy());
			JdbcDaoUtils.setLong(statement, 2, feedFavor.getFeedId());
			statement.setTimestamp(3, new Timestamp(feedFavor.getCreatedOn().getTime()));
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				feedFavor.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", feedFavor);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_USER_ID_AND_FEED_ID_SQL = "select * from feed_favor where created_by=? and feed_id=?";

	/**
	 * 获取用户对于指定消息的赞
	 */
	@Override
	public FeedFavor getByUserIdAndFeedId(Integer userId, Long feedId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_AND_FEED_ID_SQL, ROW_MAPPER, userId, feedId);
	}

	private static final String DELETE_SQL = "delete from feed_favor where id=?";

	/**
	 * 删除赞
	 */
	@Override
	public void delete(Long id) {
		super.getJdbcTemplate().update(DELETE_SQL, id);
	}

	private static final String GET_BY_ID_SQL = "select * from feed_favor where id=?";

	/**
	 * 根据ID获取赞信息
	 */
	@Override
	public FeedFavor getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String LIST_BY_FEED_ID_SQL = "select * from feed_favor where feed_id=? order by id asc";

	/**
	 * 获取指定主题消息的所有赞列表
	 */
	@Override
	public List<FeedFavor> listByFeedId(Long feedId) {
		return super.getJdbcTemplate().query(LIST_BY_FEED_ID_SQL, ROW_MAPPER, feedId);
	}

	private static final String LIST_BY_FEED_IDS_SQL = "select * from feed_favor where feed_id in (?) order by feed_id, id asc";

	/**
	 * 获取指定主题消息列表的所有赞列表
	 */
	@Override
	public List<FeedFavor> listByFeedIds(Long... feedIds) {
		String inStatement = feedIds.length > 0 ? StringUtils.arrayToCommaDelimitedString(feedIds) : "null";
		return super.getJdbcTemplate().query(LIST_BY_FEED_IDS_SQL.replace("?", inStatement), ROW_MAPPER);
	}

	private static final String LIST_BY_USER_ID_AND_FEED_IDS_SQL = "select * from feed_favor where created_by=? and feed_id in (?) order by feed_id, id asc";

	/**
	 * 获取指定用户在指定主题消息列表上给出的所有赞列表
	 */
	@Override
	public List<FeedFavor> listByUserIdAndFeedIds(Integer userId, Long... feedIds) {
		String inStatement = feedIds.length > 0 ? StringUtils.arrayToCommaDelimitedString(feedIds) : "null";
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_AND_FEED_IDS_SQL.replace("(?)", "(" + inStatement + ")"),
				ROW_MAPPER, userId);
	}

}
