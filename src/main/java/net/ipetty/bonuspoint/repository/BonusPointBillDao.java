package net.ipetty.bonuspoint.repository;

import net.ipetty.bonuspoint.domain.BonusPointBill;

/**
 * BonusPointBillDao
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public interface BonusPointBillDao {

	/**
	 * 保存积分消费流水帐
	 */
	public void save(BonusPointBill bonusPointBill);

	/**
	 * 删除积分消费流水帐
	 */
	public void delete(Long bonusPointId, Long bonusPointConsumptionId);

}
