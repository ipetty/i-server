package net.ipetty.bonuspoint.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import net.ipetty.bonuspoint.domain.BonusPointConsumption;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * BonusPointConsumptionDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
@Repository("bonusPointConsumptionDao")
public class BonusPointConsumptionDaoImpl extends BaseJdbcDaoSupport implements BonusPointConsumptionDao {

	static final RowMapper<BonusPointConsumption> ROW_MAPPER = new RowMapper<BonusPointConsumption>() {
		@Override
		public BonusPointConsumption mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, activity_id, bonus, created_by, created_on
			BonusPointConsumption bonusPointConsumption = new BonusPointConsumption();
			bonusPointConsumption.setId(rs.getLong("id"));
			bonusPointConsumption.setActivityId(JdbcDaoUtils.getLong(rs, "activity_id"));
			bonusPointConsumption.setBonus(rs.getInt("bonus"));
			bonusPointConsumption.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			bonusPointConsumption.setCreatedOn(rs.getDate("created_on"));
			return bonusPointConsumption;
		}
	};

	private static final String SAVE_SQL = "insert into bonus_point_consumption(activity_id, bonus, created_by, created_on) values(?, ?, ?, ?)";

	/**
	 * 保存积分消费记录
	 */
	public void save(BonusPointConsumption bonusPointConsumption) {
		bonusPointConsumption.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL,
					Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setLong(statement, 1, bonusPointConsumption.getActivityId());
			statement.setInt(2, bonusPointConsumption.getBonus());
			JdbcDaoUtils.setInteger(statement, 3, bonusPointConsumption.getCreatedBy());
			statement.setTimestamp(4, new Timestamp(bonusPointConsumption.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				bonusPointConsumption.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", bonusPointConsumption);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String DELETE_SQL = "delete from bonus_point_consumption where id=?";

	/**
	 * 删除积分消费记录
	 */
	public void delete(Long bonusPointConsumptionId) {
		super.getJdbcTemplate().update(DELETE_SQL, bonusPointConsumptionId);
	}

}
