package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.cache.annotation.UpdateToCache;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.user.domain.UserProfile;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserProfileDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月7日
 */
@Repository("userProfileDao")
public class UserProfileDaoImpl extends BaseJdbcDaoSupport implements UserProfileDao {

	static final RowMapper<UserProfile> ROW_MAPPER = new RowMapper<UserProfile>() {
		@Override
		public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
			// user_id, nickname, avatar, background, gender, state_and_region,
			// signature, birthday
			UserProfile profile = new UserProfile();
			profile.setUserId(rs.getInt("user_id"));
			profile.setNickname(rs.getString("nickname"));
			profile.setAvatar(rs.getString("avatar"));
			profile.setBackground(rs.getString("background"));
			profile.setGender(rs.getString("gender"));
			profile.setStateAndRegion(rs.getString("state_and_region"));
			profile.setSignature(rs.getString("signature"));
			profile.setBirthday(rs.getDate("birthday"));
			return profile;
		}
	};

	private static final String SAVE_SQL = "insert into user_profile(user_id, nickname, avatar, background, gender, state_and_region, signature, birthday)"
			+ " values(?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存用户个人信息
	 */
	@Override
	public void save(UserProfile profile) {
		super.getJdbcTemplate().update(SAVE_SQL, profile.getUserId(), profile.getNickname(), profile.getAvatar(),
				profile.getBackground(), profile.getGender(), profile.getStateAndRegion(), profile.getSignature(),
				profile.getBirthday());
		logger.debug("saved {}", profile);
	}

	private static final String GET_BY_USER_ID_SQL = "select * from user_profile where user_id=?";

	/**
	 * 根据用户ID获取用户个人信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_USER_PROFILE_ID_TO_PROFILE, key = "${userId}")
	public UserProfile getByUserId(Integer userId) {
		return super.queryUniqueEntity(GET_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

	private static final String UPDATE_USER_PROFILE_SQL = "update user_profile set nickname=?, avatar=?, background=?, gender=?, state_and_region=?, signature=?, birthday=? where user_id=?";

	/**
	 * 更新用户个人信息
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_USER_PROFILE_ID_TO_PROFILE, key = "${profile.userId}")
	public void update(UserProfile profile) {
		super.getJdbcTemplate().update(UPDATE_USER_PROFILE_SQL, profile.getNickname(), profile.getAvatar(),
				profile.getBackground(), profile.getGender(), profile.getStateAndRegion(), profile.getSignature(),
				profile.getBirthday(), profile.getUserId());
		logger.debug("updated {}", profile);
	}

}
