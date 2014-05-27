package net.ipetty.bonuspoint.repository;

import net.ipetty.bonuspoint.domain.BonusPointConsumption;

/**
 * BonusPointConsumptionDao
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public interface BonusPointConsumptionDao {

	/**
	 * 保存积分消费记录
	 */
	public void save(BonusPointConsumption bonusPointConsumption);

	/**
	 * 删除积分消费记录
	 */
	public void delete(Long bonusPointConsumptionId);

}
