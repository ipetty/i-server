package net.ipetty.user.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.user.domain.User;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
@Repository("userDao")
public class UserDaoImpl extends BaseJdbcDaoSupport implements UserDao {

	static final RowMapper<User> ROW_MAPPER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_on, uid, unique_name, phone_number, email, qq,
			// qzone_uid, weibo_account, weibo_uid, password, salt
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setCreatedOn(rs.getDate("created_on"));
			user.setUid(rs.getInt("uid"));
			user.setUniqueName(rs.getString("unique_name"));
			user.setPhoneNumber(rs.getString("phone_number"));
			user.setEmail(rs.getString("email"));
			user.setQq(rs.getString("qq"));
			user.setQzoneUid(rs.getString("qzone_uid"));
			user.setWeiboAccount(rs.getString("weibo_account"));
			user.setWeiboUid(rs.getString("weibo_uid"));
			user.setPassword(rs.getString("password"));
			user.setEncodedPassword(user.getPassword());
			user.setSalt(rs.getString("salt"));
			return user;
		}
	};

	private static final String CREATE_USER_SQL = "insert into users(uid, unique_name, phone_number, email, qq, qzone_uid, weibo_account, weibo_uid, password, salt)"
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户帐号
	 */
	@Override
	public void save(User user) {
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user.getUid());
			statement.setString(2, user.getUniqueName());
			statement.setString(3, user.getPhoneNumber());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getQq());
			statement.setString(6, user.getQzoneUid());
			statement.setString(7, user.getWeiboAccount());
			statement.setString(8, user.getWeiboUid());
			statement.setString(9, user.getEncodedPassword());
			statement.setString(10, user.getSalt());

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				user.setId(rs.getInt(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", user);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from users where id=?";

	/**
	 * 根据ID获取用户帐号
	 */
	@Override
	public User getById(Integer id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String GET_BY_UID_SQL = "select * from users where uid=?";

	/**
	 * 根据uid获取用户帐号
	 */
	@Override
	public User getByUid(int uid) {
		return super.queryUniqueEntity(GET_BY_UID_SQL, ROW_MAPPER, uid);
	}

	private static final String GET_BY_UNIQUE_NAME_SQL = "select * from users where unique_name=?";

	/**
	 * 根据爱宠号获取用户帐号
	 */
	@Override
	public User getByUniqueName(String uniqueName) {
		return super.queryUniqueEntity(GET_BY_UNIQUE_NAME_SQL, ROW_MAPPER, uniqueName);
	}

	private static final String GET_BY_LOGIN_NAME_SQL = "select * from users where unique_name=? or phone_number=? or email=? or qzone_uid=? or weibo_uid=?";

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户帐号
	 */
	@Override
	public User getByLoginName(String loginName) {
		return super.queryUniqueEntity(GET_BY_LOGIN_NAME_SQL, ROW_MAPPER, loginName, loginName, loginName, loginName,
				loginName);
	}

	private static final String UPDATE_USER_SQL = "update users set phone_number=?, email=?, qq=?, qzone_uid=?, weibo_account=?, weibo_uid=? where id=?";

	/**
	 * 更新用户帐号信息
	 */
	@Override
	public void update(User user) {
		super.getJdbcTemplate().update(UPDATE_USER_SQL, user.getPhoneNumber(), user.getEmail(), user.getQq(),
				user.getQzoneUid(), user.getWeiboAccount(), user.getWeiboUid(), user.getId());
		logger.debug("updated {}", user);
	}

	private static final String UPDATE_UNIQUE_NAME_SQL = "update users set unique_name=? where id=?";

	/**
	 * 更新爱宠号
	 */
	@Override
	public void updateUniqueName(Integer id, String uniqueName) {
		super.getJdbcTemplate().update(UPDATE_UNIQUE_NAME_SQL, uniqueName, id);
		logger.debug("updated unique name for user({}), unique name is {}", id, uniqueName);
	}

	private static final String CHANGE_PASSWORD_SQL = "update users set password=? where id=?";

	/**
	 * 修改密码
	 */
	@Override
	public void changePassword(Integer id, String newEncodedPassword) {
		super.getJdbcTemplate().update(CHANGE_PASSWORD_SQL, newEncodedPassword, id);
		logger.debug("changed password for user({})", id);
	}

}
