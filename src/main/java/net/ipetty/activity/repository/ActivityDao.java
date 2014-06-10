package net.ipetty.activity.repository;

import java.util.List;

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

	/**
	 * 获取某人的事件流
	 */
	public List<Activity> listActivities(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取某人相关的事件流
	 */
	public List<Activity> listRelatedActivities(Integer userId, int pageNumber, int pageSize);

}
