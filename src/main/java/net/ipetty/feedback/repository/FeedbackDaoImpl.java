package net.ipetty.feedback.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.feedback.domain.Feedback;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * FeedbackDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
@Repository("feedbackDao")
public class FeedbackDaoImpl extends BaseJdbcDaoSupport implements FeedbackDao {

	static final RowMapper<Feedback> ROW_MAPPER = new RowMapper<Feedback>() {
		@Override
		public Feedback mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, title, content, contact, created_by, created_on
			Feedback feedback = new Feedback();
			feedback.setId(rs.getInt("id"));
			feedback.setTitle(rs.getString("title"));
			feedback.setContent(rs.getString("content"));
			feedback.setContact(rs.getString("contact"));
			feedback.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			feedback.setCreatedOn(rs.getDate("created_on"));
			return feedback;
		}
	};

	private static final String SAVE_SQL = "insert into feedback(title, content, contact, created_by, created_on) values(?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	@Override
	public void save(Feedback feedback) {
		feedback.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, feedback.getTitle());
			statement.setString(2, feedback.getContent());
			statement.setString(3, feedback.getContact());
			JdbcDaoUtils.setInteger(statement, 4, feedback.getCreatedBy());
			statement.setTimestamp(5, new Timestamp(feedback.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				feedback.setId(rs.getInt(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", feedback);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

}
