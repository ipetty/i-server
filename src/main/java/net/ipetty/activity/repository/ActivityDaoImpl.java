package net.ipetty.activity.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.ipetty.activity.domain.Activity;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.vo.ActivityType;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * ActivityDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Repository("activityDao")
public class ActivityDaoImpl extends BaseJdbcDaoSupport implements ActivityDao {

	static final RowMapper<Activity> ROW_MAPPER = new RowMapper<Activity>() {
		@Override
		public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, type, created_by, target_id, this_id, content, created_on
			Activity activity = new Activity();
			activity.setId(rs.getLong("id"));
			activity.setType(rs.getString("type"));
			activity.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			activity.setTargetId(JdbcDaoUtils.getLong(rs, "target_id"));
			activity.setThisId(JdbcDaoUtils.getLong(rs, "this_id"));
			activity.setContent(rs.getString("content"));
			activity.setCreatedOn(rs.getTimestamp("created_on"));
			return activity;
		}
	};

	private static final String SAVE_SQL = "insert into activity(type, created_by, target_id, this_id, content, created_on) values(?, ?, ?, ?, ?, ?)";

	/**
	 * 保存事件
	 */
	@Override
	public void save(Activity activity) {
		activity.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, activity.getType());
			JdbcDaoUtils.setInteger(statement, 2, activity.getCreatedBy());
			JdbcDaoUtils.setLong(statement, 3, activity.getTargetId());
			JdbcDaoUtils.setLong(statement, 4, activity.getThisId());
			statement.setString(5, activity.getContent());
			statement.setTimestamp(6, new Timestamp(activity.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				activity.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", activity);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String SAVE_INBOX_SQL = "insert into activity_inbox(activity_id, receiver_id) values(?, ?)";

	/**
	 * 保存事件的inbox通知
	 */
	@Override
	public void saveActivityInbox(Long activityId, Integer receiverId) {
		super.getJdbcTemplate().update(SAVE_INBOX_SQL, activityId, receiverId);
	}

	private static final String LIST_ACTIVITIES_SQL = "select * from activity where created_by=? order by created_on desc limit ?,?";

	/**
	 * 获取某人的事件流
	 */
	@Override
	public List<Activity> listActivities(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_ACTIVITIES_SQL, ROW_MAPPER, userId, pageNumber * pageSize, pageSize);
	}

	private static final String LIST_RELATED_ACTIVITIES_SQL = "select a.* from activity a left join (select friend_id,follower_id from user_relationship where follower_id=?) ur on a.created_by=ur.friend_id union select * from activity where created_by=? order by created_on desc limit ?,?";

	/**
	 * 获取某人相关的事件流
	 */
	@Override
	public List<Activity> listRelatedActivities(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_RELATED_ACTIVITIES_SQL, ROW_MAPPER, userId, userId,
				pageNumber * pageSize, pageSize);
	}

	private static final String LIST_INBOX_ACTIVITIES_BY_TYPE_SQL = "select a.* from activity a left join activity_inbox ai on a.id=ai.activity_id where ai.receiver_id=? and a.type=? and created_on>? and created_on<?";
	private static final String LIST_INBOX_ACTIVITIES_BY_TYPE_WITH_NONE_LAST_CHECKOUT_TIME_SQL = "select a.* from activity a left join activity_inbox ai on a.id=ai.activity_id where ai.receiver_id=? and a.type=? and created_on<?";

	/**
	 * 获取用户新事件
	 */
	@Override
	public List<Activity> listInboxActivities(Integer userId, String activityType, Date lastCheckoutTime,
			Date currentTime) {
		return lastCheckoutTime != null ? super.getJdbcTemplate().query(LIST_INBOX_ACTIVITIES_BY_TYPE_SQL, ROW_MAPPER,
				userId, activityType, lastCheckoutTime, currentTime) : super.getJdbcTemplate().query(
				LIST_INBOX_ACTIVITIES_BY_TYPE_WITH_NONE_LAST_CHECKOUT_TIME_SQL, ROW_MAPPER, userId, activityType,
				currentTime);
	}

	private static final String LIST_INBOX_ACTIVITIES_SQL = "select a.* from activity a left join activity_inbox ai on a.id=ai.activity_id where ai.receiver_id=? and (a.type=? or a.type=? or a.type=?) and created_on>? and created_on<?";
	private static final String LIST_INBOX_ACTIVITIES_WITH_NONE_LAST_CHECKOUT_TIME_SQL = "select a.* from activity a left join activity_inbox ai on a.id=ai.activity_id where ai.receiver_id=? and (a.type=? or a.type=? or a.type=?) and created_on<?";

	/**
	 * 获取用户新事件
	 */
	@Override
	public List<Activity> listInboxActivities(Integer userId, Date lastCheckoutTime, Date currentTime) {
		return lastCheckoutTime != null ? super.getJdbcTemplate().query(LIST_INBOX_ACTIVITIES_SQL, ROW_MAPPER, userId,
				ActivityType.FOLLOW, ActivityType.COMMENT, ActivityType.FEED_FAVOR, lastCheckoutTime, currentTime)
				: super.getJdbcTemplate().query(LIST_INBOX_ACTIVITIES_WITH_NONE_LAST_CHECKOUT_TIME_SQL, ROW_MAPPER,
						userId, ActivityType.FOLLOW, ActivityType.COMMENT, ActivityType.FEED_FAVOR, currentTime);
	}

	private static final String LIST_INBOX_ACTIVITIES_PAGING_SQL = "select a.* from activity a left join activity_inbox ai on a.id=ai.activity_id where ai.receiver_id=? and (a.type=? or a.type=? or a.type=?) and a.created_on<? order by a.created_on desc limit ?,?";

	/**
	 * 分页（包括历史时间列表）获取用户的新粉丝、新回复、新赞事件列表
	 */
	public List<Activity> listNewActivities(Integer userId, Date currentTime, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_INBOX_ACTIVITIES_PAGING_SQL, ROW_MAPPER, userId, ActivityType.FOLLOW,
				ActivityType.COMMENT, ActivityType.FEED_FAVOR, currentTime, pageNumber * pageSize, pageSize);
	}

}
