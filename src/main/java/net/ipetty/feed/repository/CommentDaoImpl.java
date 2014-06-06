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
import net.ipetty.core.cache.annotation.LoadFromHazelcast;
import net.ipetty.core.cache.annotation.UpdateToHazelcast;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.feed.domain.Comment;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * CommentDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月13日
 */
@Repository("commentDao")
public class CommentDaoImpl extends BaseJdbcDaoSupport implements CommentDao {

	static final RowMapper<Comment> ROW_MAPPER = new RowMapper<Comment>() {
		@Override
		public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_by, created_on, feed_id, text, deleted
			Comment comment = new Comment();
			comment.setId(rs.getLong("id"));
			comment.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			comment.setCreatedOn(rs.getDate("created_on"));
			comment.setFeedId(JdbcDaoUtils.getLong(rs, "feed_id"));
			comment.setText(rs.getString("text"));
			comment.setDeleted(rs.getBoolean("deleted"));
			return comment;
		}
	};

	private static final String SAVE_SQL = "insert into feed_comment(created_by, feed_id, text, created_on) values(?, ?, ?, ?)";

	/**
	 * 保存评论
	 */
	@Override
	public void save(Comment comment) {
		comment.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setInteger(statement, 1, comment.getCreatedBy());
			JdbcDaoUtils.setLong(statement, 2, comment.getFeedId());
			statement.setString(3, comment.getText());
			statement.setTimestamp(4, new Timestamp(comment.getCreatedOn().getTime()));
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				comment.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", comment);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from feed_comment where id=? and deleted=false";

	/**
	 * 根据ID获取评论信息
	 */
	@Override
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_COMMENT_ID_TO_COMMENT, key = "${id}")
	public Comment getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String DELETE_SQL = "update feed_comment set deleted=true where id=?";

	/**
	 * 删除评论
	 */
	@Override
	@UpdateToHazelcast(mapName = CacheConstants.CACHE_COMMENT_ID_TO_COMMENT, key = "${id}")
	public void delete(Long id) {
		super.getJdbcTemplate().update(DELETE_SQL, id);
	}

	private static final String LIST_BY_FEED_ID_SQL = "select * from feed_comment where feed_id=? and deleted=false order by id asc";

	/**
	 * 获取指定主题消息的所有评论列表
	 */
	@Override
	public List<Comment> listByFeedId(Long feedId) {
		return super.getJdbcTemplate().query(LIST_BY_FEED_ID_SQL, ROW_MAPPER, feedId);
	}

	private static final String LIST_BY_FEED_IDS_SQL = "select * from feed_comment where feed_id in (?) and deleted=false order by feed_id, id asc";

	/**
	 * 获取指定主题消息列表的所有评论列表
	 */
	@Override
	public List<Comment> listByFeedIds(Long... feedIds) {
		return super.getJdbcTemplate().query(LIST_BY_FEED_IDS_SQL, ROW_MAPPER,
				StringUtils.arrayToCommaDelimitedString(feedIds));
	}

}
