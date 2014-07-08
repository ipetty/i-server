package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.user.domain.UserZone;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserZoneDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月22日
 */
@Repository("userZoneDao")
public class UserZoneDaoImpl extends BaseJdbcDaoSupport implements UserZoneDao {

	static final RowMapper<UserZone> ROW_MAPPER = new RowMapper<UserZone>() {
		@Override
		public UserZone mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id
			UserZone userZone = new UserZone();
			userZone.setUserId(rs.getInt("user_id"));
			return userZone;
		}
	};

	private static final String SAVE_SQL = "insert into user_zone(user_id) values(?)";

	/**
	 * 保存用户空间
	 */
	@Override
	public void save(UserZone userZone) {
		super.getJdbcTemplate().update(SAVE_SQL, userZone.getUserId());
		logger.debug("saved {}", userZone);
	}

	private static final String GET_BY_USER_ID_SQL = "select * from user_zone where user_id=?";

	/**
	 * 根据用户ID获取用户空间
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_ZONE_ID_TO_ZONE, key = "${userId}")
	public UserZone getByUserId(Integer userId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

}
