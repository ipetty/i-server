package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

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

	static final RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_on, uid, account, phone_number, email, qq, qzone_uid,
			// weibo_account, weibo_uid, password, salt
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setCreatedOn(rs.getDate("created_on"));
			user.setUid(rs.getInt("uid"));
			user.setAccount(rs.getString("account"));
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

	private static final String CREATE_USER_SQL = "insert into users(uid, account, phone_number, email, qq, qzone_uid, weibo_account, weibo_uid, password, salt)"
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户帐号
	 */
	@Override
	public void save(User user) {
		user.prePersist();
		super.getJdbcTemplate().update(CREATE_USER_SQL, user.getUid(), user.getAccount(), user.getPhoneNumber(),
				user.getEmail(), user.getQq(), user.getQzoneUid(), user.getWeiboAccount(), user.getWeiboUid(),
				user.getEncodedPassword(), user.getSalt());
		logger.debug("saved {}", user);
	}

	private static final String GET_BY_ID_SQL = "select * from users where id=?";

	/**
	 * 根据ID获取用户帐号
	 */
	@Override
	public User getById(Integer id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, USER_ROW_MAPPER, id);
	}

	private static final String GET_BY_UID_SQL = "select * from users where uid=?";

	/**
	 * 根据uid获取用户帐号
	 */
	@Override
	public User getByUid(int uid) {
		return super.queryUniqueEntity(GET_BY_UID_SQL, USER_ROW_MAPPER, uid);
	}

	private static final String GET_BY_ACCOUNT_SQL = "select * from users where account=?";

	/**
	 * 根据爱宠帐号获取用户帐号
	 */
	@Override
	public User getByAccount(String account) {
		return super.queryUniqueEntity(GET_BY_ACCOUNT_SQL, USER_ROW_MAPPER, account);
	}

	private static final String GET_BY_LOGIN_NAME_SQL = "select * from users where account=? or phone_number=? or email=? or qzone_uid=? or weibo_uid=?";

	/**
	 * 根据帐号（爱宠帐号，手机号码，邮箱，Qzone Uid，新浪微博Uid）获取用户帐号
	 */
	@Override
	public User getByLoginName(String loginName) {
		return super.queryUniqueEntity(GET_BY_LOGIN_NAME_SQL, USER_ROW_MAPPER, loginName, loginName, loginName,
				loginName, loginName);
	}

}
