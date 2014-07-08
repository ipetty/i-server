package net.ipetty.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.cache.annotation.UpdateToCache;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.feed.domain.Feed;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * FeedDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
@Repository("feedDao")
public class FeedDaoImpl extends BaseJdbcDaoSupport implements FeedDao {

	static final RowMapper<Feed> ROW_MAPPER = new RowMapper<Feed>() {
		@Override
		public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_by, created_on, image_id, text, location_id, deleted
			Feed feed = new Feed();
			feed.setId(rs.getLong("id"));
			feed.setCreatedBy(rs.getInt("created_by"));
			feed.setCreatedOn(rs.getTimestamp("created_on"));
			feed.setImageId(JdbcDaoUtils.getLong(rs, "image_id"));
			feed.setText(rs.getString("text"));
			feed.setLocationId(JdbcDaoUtils.getLong(rs, "location_id"));
			feed.setDeleted(rs.getBoolean("deleted"));
			return feed;
		}
	};

	private static final String SAVE_SQL = "insert into feed(created_by, image_id, text, location_id, created_on) values(?, ?, ?, ?, ?)";

	/**
	 * 保存消息
	 */
	@Override
	public void save(Feed feed) {
		feed.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, feed.getCreatedBy());
			JdbcDaoUtils.setLong(statement, 2, feed.getImageId());
			statement.setString(3, feed.getText());
			JdbcDaoUtils.setLong(statement, 4, feed.getLocationId());
			statement.setTimestamp(5, new Timestamp(feed.getCreatedOn().getTime()));
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				feed.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", feed);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from feed where id=? and deleted=false";

	/**
	 * 根据ID获取消息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_FEED_ID_TO_FEED, key = "${id}")
	public Feed getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String DELETE_SQL = "update feed set deleted=true where id=?";

	/**
	 * 删除消息
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_FEED_ID_TO_FEED, key = "${id}")
	public void delete(Long id) {
		super.getJdbcTemplate().update(DELETE_SQL, id);
	}

	private static final String LIST_BY_TIMELINE_WITH_PAGE_SQL = "select * from feed where created_on<=? and deleted=false order by created_on desc limit ?,?";

	/**
	 * 根据时间线分页获取消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Feed> listByTimelineWithPage(Date timeline, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_TIMELINE_WITH_PAGE_SQL, ROW_MAPPER, timeline,
				pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_USER_ID_AND_TIMELINE_WITH_PAGE_SQL = "select * from feed f where f.created_on<=? and deleted=false and (f.created_by=? or exists(select 1 from user_relationship ur where ur.follower_id=? and ur.friend_id=f.created_by)) order by f.created_on desc limit ?,?";

	/**
	 * 根据时间线分页获取与我相关的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Feed> listByUserIdAndTimelineWithPage(Integer userId, Date timeline, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_AND_TIMELINE_WITH_PAGE_SQL, ROW_MAPPER, timeline, userId,
				userId, pageNumber * pageSize, pageSize);
	}

	private static final String LIST_BY_AUTHOR_ID_AND_TIMELINE_WITH_PAGE_SQL = "select * from feed f where f.created_on<=? and deleted=false and f.created_by=? order by f.created_on desc limit ?,?";

	/**
	 * 根据时间线分页获取指定用户的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<Feed> listByAuthorIdAndTimelineWithPage(Integer userId, Date timeline, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BY_AUTHOR_ID_AND_TIMELINE_WITH_PAGE_SQL, ROW_MAPPER, timeline,
				userId, pageNumber * pageSize, pageSize);
	}

}
