package net.ipetty.notify.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.notify.domain.Notification;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * NotificationDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年7月30日
 */
@Repository("notificationDao")
public class NotificationDaoImpl extends BaseJdbcDaoSupport implements NotificationDao {

	static final RowMapper<Notification> ROW_MAPPER = new RowMapper<Notification>() {
		@Override
		public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, new_fans_num, new_fans_last_check, new_replies_num,
			// new_replies_last_check, new_favors_num, new_favors_last_check
			Notification notification = new Notification();
			notification.setUserId(rs.getInt("user_id"));
			notification.setCreatedBy(notification.getUserId());
			notification.setNewFansNum(rs.getShort("new_fans_num"));
			notification.setNewFansLastCheckDatetime(rs.getTimestamp("new_fans_last_check"));
			notification.setNewRepliesNum(rs.getShort("new_replies_num"));
			notification.setNewRepliesLastCheckDatetime(rs.getTimestamp("new_replies_last_check"));
			notification.setNewFavorsNum(rs.getShort("new_favors_num"));
			notification.setNewFavorsLastCheckDatetime(rs.getTimestamp("new_favors_last_check"));
			return notification;
		}
	};

	private static final String SAVE_SQL = "insert into notification(user_id, new_fans_num, new_fans_last_check, new_replies_num, new_replies_last_check, new_favors_num, new_favors_last_check) values(?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户通知对象
	 */
	@Override
	public void save(Notification notification) {
		super.getJdbcTemplate().update(SAVE_SQL, notification.getUserId(), notification.getNewFansNum(),
				notification.getNewFansLastCheckDatetime(), notification.getNewRepliesNum(),
				notification.getNewRepliesLastCheckDatetime(), notification.getNewFavorsNum(),
				notification.getNewFavorsLastCheckDatetime());
	}

	private static final String GET_BY_USER_ID_SQL = "select * from notification where user_id=?";

	/**
	 * 获取某用户的通知对象
	 */
	@Override
	public Notification getNotification(Integer userId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

	private static final String UPDATE_SQL = "update notification set new_fans_num=?, new_fans_last_check=?, new_replies_num=?, new_replies_last_check=?, new_favors_num=?, new_favors_last_check=? where user_id=?";

	/**
	 * 更新用户通知对象
	 */
	@Override
	public void update(Notification notification) {
		super.getJdbcTemplate().update(UPDATE_SQL, notification.getNewFansNum(),
				notification.getNewFansLastCheckDatetime(), notification.getNewRepliesNum(),
				notification.getNewRepliesLastCheckDatetime(), notification.getNewFavorsNum(),
				notification.getNewFavorsLastCheckDatetime(), notification.getUserId());
	}

}
