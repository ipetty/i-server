package net.ipetty.notify.service;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.notify.domain.Notification;
import net.ipetty.notify.repository.NotificationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * NotificationService
 * 
 * @author luocanfeng
 * @date 2014年7月30日
 */
@Service
@Transactional
public class NotificationService extends BaseService {

	@Resource
	private NotificationDao notificationDao;

	/**
	 * 保存用户通知对象
	 */
	public void save(Notification notification) {
		Assert.notNull(notification, "通知对象不能为空");
		Assert.notNull(notification.getUserId(), "用户ID不能为空");
		notificationDao.save(notification);
	}

	/**
	 * 获取某用户的通知对象
	 */
	public Notification getNotification(Integer userId) {
		Assert.notNull(userId, "用户ID不能为空");
		return notificationDao.getNotification(userId);
	}

	/**
	 * 更新用户通知对象
	 */
	public void update(Notification notification) {
		Assert.notNull(notification, "通知对象不能为空");
		Assert.notNull(notification.getUserId(), "用户ID不能为空");
		notificationDao.update(notification);
	}

}
