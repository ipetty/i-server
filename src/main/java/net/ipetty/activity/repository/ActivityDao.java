package net.ipetty.activity.repository;

import java.util.Date;
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
	 * 保存事件的inbox通知
	 */
	public void saveActivityInbox(Long activityId, Integer receiverId);

	/**
	 * 获取某人的事件流
	 */
	public List<Activity> listActivities(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取某人相关的事件流
	 */
	public List<Activity> listRelatedActivities(Integer userId, int pageNumber, int pageSize);

	/**
	 * 获取用户新事件
	 */
	public List<Activity> listInboxActivities(Integer userId, String activityType, Date lastCheckoutTime,
			Date currentTime);

	/**
	 * 获取用户新事件
	 */
	public List<Activity> listInboxActivities(Integer userId, Date lastCheckoutTime, Date currentTime);

	/**
	 * 分页（包括历史时间列表）获取用户的新粉丝、新回复、新赞事件列表
	 */
	public List<Activity> listNewActivities(Integer userId, Date currentTime, int pageNumber, int pageSize);

}
