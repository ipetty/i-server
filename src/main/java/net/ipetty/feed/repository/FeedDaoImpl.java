package net.ipetty.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			// id, created_by, created_on, image_id, text, location_id
			Feed feed = new Feed();
			feed.setId(rs.getLong("id"));
			feed.setCreatedBy(rs.getInt("created_by"));
			feed.setCreatedOn(rs.getDate("created_on"));
			feed.setImageId(JdbcDaoUtils.getLong(rs, "image_id"));
			feed.setText(rs.getString("text"));
			feed.setLocationId(JdbcDaoUtils.getLong(rs, "location_id"));
			return feed;
		}
	};

	private static final String INSERT_SQL = "insert into feed(created_by, image_id, text, location_id) values(?, ?, ?, ?)";

	/**
	 * 保存位置信息
	 */
	@Override
	public void save(Feed feed) {
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, feed.getCreatedBy());
			if (feed.getImageId() != null) {
				statement.setLong(2, feed.getImageId());
			}
			statement.setString(3, feed.getText());
			if (feed.getLocationId() != null) {
				statement.setLong(4, feed.getLocationId());
			}
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

	private static final String GET_BY_ID_SQL = "select * from feed where id=?";

	/**
	 * 根据ID获取位置信息
	 */
	@Override
	public Feed getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

}
