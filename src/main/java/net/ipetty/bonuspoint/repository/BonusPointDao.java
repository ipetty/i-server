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

	/**
	 * 查找指定用户今天在指定事件类型上已经获得的积分数
	 */
	public int getBonusGainedTodayByActivityType(Integer userId, String activityType);

}
