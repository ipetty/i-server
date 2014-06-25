package net.ipetty.pet.repository;

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
import net.ipetty.core.cache.annotation.UpdatesToHazelcast;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.pet.domain.Pet;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * PetDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
@Repository("petDao")
public class PetDaoImpl extends BaseJdbcDaoSupport implements PetDao {

	static final RowMapper<Pet> ROW_MAPPER = new RowMapper<Pet>() {
		@Override
		public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_on, created_by, uid, unique_name, nickname, avatar,
			// gender, family, birthday, signature, sort_order, version
			Pet pet = new Pet();
			pet.setId(rs.getInt("id"));
			pet.setCreatedBy(rs.getInt("created_by"));
			pet.setCreatedOn(rs.getTimestamp("created_on"));
			pet.setUid(rs.getInt("uid"));
			pet.setUniqueName(rs.getString("unique_name"));
			pet.setNickname(rs.getString("nickname"));
			pet.setAvatar(rs.getString("avatar"));
			pet.setGender(rs.getString("gender"));
			pet.setFamily(rs.getString("family"));
			pet.setBirthday(rs.getDate("birthday"));
			pet.setSignature(rs.getString("signature"));
			pet.setSortOrder(rs.getInt("sort_order"));
			pet.setVersion(rs.getInt("version"));
			return pet;
		}
	};

	private static final String SAVE_SQL = "insert into pet(created_by, uid, unique_name, nickname, avatar, gender, family, birthday, signature, sort_order, created_on, version) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存宠物信息
	 */
	@Override
	public void save(Pet pet) {
		pet.setCreatedOn(new Date());
		pet.setVersion(1);
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			// 宠物必须要有主人
			statement.setInt(1, pet.getCreatedBy());
			statement.setInt(2, pet.getUid());
			statement.setString(3, pet.getUniqueName());
			statement.setString(4, pet.getNickname());
			statement.setString(5, pet.getAvatar());
			statement.setString(6, pet.getGender());
			statement.setString(7, pet.getFamily());
			statement.setDate(8, pet.getBirthday() == null ? null : new java.sql.Date(pet.getBirthday().getTime()));
			statement.setString(9, pet.getSignature());
			statement.setInt(10, pet.getSortOrder()); // 必须设置排序，不然此处会报NullPointerException（null转int时报错）
			statement.setTimestamp(11, new Timestamp(pet.getCreatedOn().getTime()));
			statement.setInt(12, pet.getVersion());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				pet.setId(rs.getInt(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", pet);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from pet where id=?";

	/**
	 * 根据ID获取宠物
	 */
	@Override
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_PET_ID_TO_PET, key = "${id}")
	public Pet getById(Integer id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String GET_BY_UID_SQL = "select id from pet where uid=?";

	/**
	 * 根据uid获取宠物ID
	 */
	@Override
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_PET_UID_TO_PET_ID, key = "${uid}")
	public Integer getPetIdByUid(int uid) {
		return super.queryUniqueEntity(GET_BY_UID_SQL, INTEGER_ROW_MAPPER, uid);
	}

	private static final String GET_BY_UNIQUE_NAME_SQL = "select id from pet where unique_name=?";

	/**
	 * 根据爱宠号获取宠物ID
	 */
	@Override
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_PET_UN_TO_PET_ID, key = "${uniqueName}")
	public Integer getPetIdByUniqueName(String uniqueName) {
		return super.queryUniqueEntity(GET_BY_UNIQUE_NAME_SQL, INTEGER_ROW_MAPPER, uniqueName);
	}

	private static final String LIST_BY_USER_ID_SQL = "select * from pet where created_by=? order by sort_order asc";

	/**
	 * 获取指定用户的所有宠物
	 */
	@Override
	public List<Pet> listByUserId(Integer userId) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

	private static final String UPDATE_PET_SQL = "update pet set nickname=?, avatar=?, gender=?, family=?, birthday=?, signature=?, sort_order=?, version=version+1 where id=?";

	/**
	 * 更新宠物信息
	 */
	@Override
	@UpdateToHazelcast(mapName = CacheConstants.CACHE_PET_ID_TO_PET, key = "${pet.id}")
	public void update(Pet pet) {
		super.getJdbcTemplate().update(UPDATE_PET_SQL, pet.getNickname(), pet.getAvatar(), pet.getGender(),
				pet.getFamily(), pet.getBirthday(), pet.getBirthday(), pet.getSortOrder(), pet.getId());
		logger.debug("updated {}", pet);
	}

	private static final String UPDATE_UNIQUE_NAME_SQL = "update pet set unique_name=?, version=version+1 where id=?";

	/**
	 * 更新爱宠唯一标识
	 */
	@Override
	@UpdatesToHazelcast({ @UpdateToHazelcast(mapName = CacheConstants.CACHE_PET_ID_TO_PET, key = "${id}"),
			@UpdateToHazelcast(mapName = CacheConstants.CACHE_PET_UN_TO_PET_ID, key = "${uniqueName}") })
	public void updateUniqueName(Integer id, String uniqueName) {
		super.getJdbcTemplate().update(UPDATE_UNIQUE_NAME_SQL, uniqueName, id);
		logger.debug("updated unique name for pet({}), unique name is {}", id, uniqueName);
	}

}
