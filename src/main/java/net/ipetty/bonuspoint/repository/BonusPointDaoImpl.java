package net.ipetty.bonuspoint.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import net.ipetty.bonuspoint.domain.BonusPoint;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * BonusPointDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
@Repository("bonusPointDao")
public class BonusPointDaoImpl extends BaseJdbcDaoSupport implements BonusPointDao {

	static final RowMapper<BonusPoint> ROW_MAPPER = new RowMapper<BonusPoint>() {
		@Override
		public BonusPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, activity_id, bonus, expired, spent, created_by, created_on
			BonusPoint bonusPoint = new BonusPoint();
			bonusPoint.setId(rs.getLong("id"));
			bonusPoint.setActivityId(JdbcDaoUtils.getLong(rs, "activity_id"));
			bonusPoint.setBonus(rs.getInt("bonus"));
			bonusPoint.setExpired(rs.getBoolean("expired"));
			bonusPoint.setSpent(rs.getBoolean("spent"));
			bonusPoint.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			bonusPoint.setCreatedOn(rs.getDate("created_on"));
			return bonusPoint;
		}
	};

	private static final String SAVE_SQL = "insert into bonus_point(activity_id, bonus, created_by, created_on) values(?, ?, ?, ?)";

	/**
	 * 保存积分
	 */
	public void save(BonusPoint bonusPoint) {
		bonusPoint.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			JdbcDaoUtils.setLong(statement, 1, bonusPoint.getActivityId());
			statement.setInt(2, bonusPoint.getBonus());
			JdbcDaoUtils.setInteger(statement, 3, bonusPoint.getCreatedBy());
			statement.setTimestamp(4, new Timestamp(bonusPoint.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				bonusPoint.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", bonusPoint);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BONUS_GAINED_TODAY_BY_ACTIVITY_TYPE_SQL = "select sum(bp.bonus) from bonus_point bp left join activity a on bp.activity_id=a.id where bp.created_by=? and a.type=?";

	/**
	 * 查找指定用户今天在指定事件类型上已经获得的积分数
	 */
	public int getBonusGainedTodayByActivityType(Integer userId, String activityType) {
		Integer result = super.queryUniqueEntity(GET_BONUS_GAINED_TODAY_BY_ACTIVITY_TYPE_SQL, INTEGER_ROW_MAPPER,
				userId, activityType);
		return result == null ? 0 : result;
	}

}
