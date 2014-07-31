package net.ipetty.notify.repository;

import net.ipetty.notify.domain.Notification;

/**
 * NotificationDao
 * 
 * @author luocanfeng
 * @date 2014年7月30日
 */
public interface NotificationDao {

	/**
	 * 保存用户通知对象
	 */
	public void save(Notification notification);

	/**
	 * 获取某用户的通知对象
	 */
	public Notification getNotification(Integer userId);

	/**
	 * 更新用户通知对象
	 */
	public void update(Notification notification);

}
