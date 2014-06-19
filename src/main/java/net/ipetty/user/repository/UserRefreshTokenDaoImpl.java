package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromHazelcast;
import net.ipetty.core.cache.annotation.UpdateToHazelcast;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.user.domain.UserRefreshToken;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserRefreshTokenDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月29日
 */
@Repository("userRefreshTokenDao")
public class UserRefreshTokenDaoImpl extends BaseJdbcDaoSupport implements UserRefreshTokenDao {

	static final RowMapper<UserRefreshToken> ROW_MAPPER = new RowMapper<UserRefreshToken>() {
		@Override
		public UserRefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, device_screen_name, device_id, device_mac, device_uuid,
			// refresh_token, created_on
			UserRefreshToken refreshToken = new UserRefreshToken();
			refreshToken.setUserId(rs.getInt("user_id"));
			refreshToken.setDeviceScreenName(rs.getString("device_screen_name"));
			refreshToken.setDeviceId(rs.getString("device_id"));
			refreshToken.setDeviceMac(rs.getString("device_mac"));
			refreshToken.setDeviceUuid(rs.getString("device_uuid"));
			refreshToken.setRefreshToken(rs.getString("refresh_token"));
			refreshToken.setCreatedOn(rs.getTimestamp("created_on"));
			return refreshToken;
		}
	};

	private static final String SAVE_SQL = "insert into user_refresh_token(user_id, device_screen_name, device_id, device_mac, device_uuid, refresh_token, created_on) values(?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存
	 */
	public void save(UserRefreshToken refreshToken) {
		refreshToken.setCreatedOn(new Date());
		super.getJdbcTemplate().update(SAVE_SQL, refreshToken.getUserId(), refreshToken.getDeviceScreenName(),
				refreshToken.getDeviceId(), refreshToken.getDeviceMac(), refreshToken.getDeviceUuid(),
				refreshToken.getRefreshToken(), refreshToken.getCreatedOn());
	}

	private static final String GET_BY_REFRESH_TOKEN_SQL = "select * from user_refresh_token where refresh_token=?";

	/**
	 * 获取RefreshToken
	 */
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_USER_REFRESH_TOKEN, key = "${refreshToken}")
	public UserRefreshToken get(String refreshToken) {
		return super.queryUniqueEntity(GET_BY_REFRESH_TOKEN_SQL, ROW_MAPPER, refreshToken);
	}

	private static final String GET_BY_USER_ID_AND_DEVICE_UUID_SQL = "select * from user_refresh_token where user_id=? and device_uuid=?";

	/**
	 * 获取指定用户在指定设备上的RefreshToken
	 */
	public UserRefreshToken get(Integer userId, String deviceUuid) {
		return super.queryUniqueEntity(GET_BY_USER_ID_AND_DEVICE_UUID_SQL, ROW_MAPPER, userId, deviceUuid);
	}

	private static final String DELETE_SQL = "delete from user_refresh_token where refresh_token=?";

	/**
	 * 删除RefreshToken
	 */
	@UpdateToHazelcast(mapName = CacheConstants.CACHE_USER_REFRESH_TOKEN, key = "${refreshToken}")
	public void delete(String refreshToken) {
		super.getJdbcTemplate().update(DELETE_SQL, refreshToken);
	}

}
