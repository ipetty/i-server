package net.ipetty.bonuspoint.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.bonuspoint.domain.BonusPointBill;
import net.ipetty.core.repository.BaseJdbcDaoSupport;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * BonusPointBillDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
@Repository("bonusPointBillDao")
public class BonusPointBillDaoImpl extends BaseJdbcDaoSupport implements BonusPointBillDao {

	static final RowMapper<BonusPointBill> ROW_MAPPER = new RowMapper<BonusPointBill>() {
		@Override
		public BonusPointBill mapRow(ResultSet rs, int rowNum) throws SQLException {
			// bonus_point_id, bonus_point_consumption_id, bonus
			BonusPointBill bonusPointBill = new BonusPointBill();
			bonusPointBill.setBonusPointId(rs.getLong("bonus_point_id"));
			bonusPointBill.setBonusPointConsumptionId(rs.getLong("bonus_point_consumption_id"));
			bonusPointBill.setBonus(rs.getInt("bonus"));
			return bonusPointBill;
		}
	};

	private static final String SAVE_SQL = "insert into bonus_point_bill(bonus_point_id, bonus_point_consumption_id, bonus) values(?, ?, ?)";

	/**
	 * 保存积分消费记录
	 */
	public void save(BonusPointBill bonusPointBill) {
		super.getJdbcTemplate().update(SAVE_SQL, bonusPointBill.getBonusPointId(),
				bonusPointBill.getBonusPointConsumptionId(), bonusPointBill.getBonus());
	}

	private static final String DELETE_SQL = "delete from bonus_point_bill where bonus_point_id=? and bonus_point_consumption_id=?";

	/**
	 * 删除积分消费记录
	 */
	public void delete(Long bonusPointId, Long bonusPointConsumptionId) {
		super.getJdbcTemplate().update(DELETE_SQL, bonusPointId, bonusPointConsumptionId);
	}

}
