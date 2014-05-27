package net.ipetty.bonuspoint.repository;

import net.ipetty.bonuspoint.domain.BonusPoint;

/**
 * BonusPointDao
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public interface BonusPointDao {

	/**
	 * 保存积分
	 */
	public void save(BonusPoint bonusPoint);

}
