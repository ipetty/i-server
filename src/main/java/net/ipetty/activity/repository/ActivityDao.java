package net.ipetty.activity.repository;

import net.ipetty.activity.domain.Activity;

/**
 * ActivityDao
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
public interface ActivityDao {

	/**
	 * 保存事件
	 */
	public void save(Activity activity);

}
