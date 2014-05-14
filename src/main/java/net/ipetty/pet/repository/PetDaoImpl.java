package net.ipetty.pet.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
			// id, created_on, user_id, uid, unique_name, name, gender,
			// sort_order
			Pet pet = new Pet();
			pet.setId(rs.getInt("id"));
			pet.setCreatedOn(rs.getDate("created_on"));
			pet.setUserId(rs.getInt("user_id"));
			pet.setUid(rs.getInt("uid"));
			pet.setUniqueName(rs.getString("unique_name"));
			pet.setName(rs.getString("name"));
			pet.setGender(rs.getString("gender"));
			pet.setSortOrder(rs.getInt("sort_order"));
			return pet;
		}
	};

	private static final String CREATE_PET_SQL = "insert into pet(user_id, uid, unique_name, name, gender, sort_order, created_on) values(?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 保存宠物信息
	 */
	@Override
	public void save(Pet pet) {
		pet.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(CREATE_PET_SQL, Statement.RETURN_GENERATED_KEYS);
			// 宠物必须要有主人
			statement.setInt(1, pet.getUserId());
			statement.setInt(2, pet.getUid());
			statement.setString(3, pet.getUniqueName());
			statement.setString(4, pet.getName());
			statement.setString(5, pet.getGender());
			statement.setInt(6, pet.getSortOrder()); // 必须设置排序，不然此处会报NullPointerException（null转int时报错）
			statement.setTimestamp(7, new Timestamp(pet.getCreatedOn().getTime()));
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
	public Pet getById(Integer id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String GET_BY_UID_SQL = "select * from pet where uid=?";

	/**
	 * 根据uid获取宠物
	 */
	@Override
	public Pet getByUid(int uid) {
		return super.queryUniqueEntity(GET_BY_UID_SQL, ROW_MAPPER, uid);
	}

	private static final String GET_BY_UNIQUE_NAME_SQL = "select * from pet where unique_name=?";

	/**
	 * 根据爱宠号获取宠物
	 */
	@Override
	public Pet getByUniqueName(String uniqueName) {
		return super.queryUniqueEntity(GET_BY_UNIQUE_NAME_SQL, ROW_MAPPER, uniqueName);
	}

	private static final String LIST_BY_USER_ID_SQL = "select * from pet where user_id=? order by sort_order asc";

	/**
	 * 获取指定用户的所有宠物
	 */
	@Override
	public List<Pet> listByUserId(Integer userId) {
		return super.getJdbcTemplate().query(LIST_BY_USER_ID_SQL, ROW_MAPPER, userId);
	}

	private static final String UPDATE_PET_SQL = "update pet set name=?, gender=?, sort_order=? where id=?";

	/**
	 * 更新宠物信息
	 */
	@Override
	public void update(Pet pet) {
		super.getJdbcTemplate().update(UPDATE_PET_SQL, pet.getName(), pet.getGender(), pet.getSortOrder(), pet.getId());
		logger.debug("updated {}", pet);
	}

	private static final String UPDATE_UNIQUE_NAME_SQL = "update pet set unique_name=? where id=?";

	/**
	 * 更新爱宠唯一标识
	 */
	@Override
	public void updateUniqueName(Integer id, String uniqueName) {
		super.getJdbcTemplate().update(UPDATE_UNIQUE_NAME_SQL, uniqueName, id);
		logger.debug("updated unique name for pet({}), unique name is {}", id, uniqueName);
	}

}
