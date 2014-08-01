package net.ipetty.activity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.repository.ActivityDao;
import net.ipetty.core.service.BaseService;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.service.FeedService;
import net.ipetty.feed.service.ImageService;
import net.ipetty.notify.domain.Notification;
import net.ipetty.notify.service.NotificationService;
import net.ipetty.vo.ActivityType;
import net.ipetty.vo.ActivityVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * ActivityService
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Service
@Transactional
public class ActivityService extends BaseService {

	@Resource
	private ActivityDao activityDao;

	@Resource
	private NotificationService notificationService;

	@Resource
	private FeedService feedService;

	@Resource
	private ImageService imageService;

	/**
	 * 保存事件
	 */
	public void save(Activity activity) {
		Assert.notNull(activity, "事件不能为空");
		activityDao.save(activity);
	}

	/**
	 * 保存事件的inbox通知
	 */
	public void saveActivityInbox(Long activityId, Integer receiverId) {
		Assert.notNull(activityId, "事件ID不能为空");
		Assert.notNull(receiverId, "事件通知人ID不能为空");
		activityDao.saveActivityInbox(activityId, receiverId);
	}

	/**
	 * 获取某人的事件流
	 */
	public List<ActivityVO> listActivities(Integer userId, int pageNumber, int pageSize) {
		List<Activity> activities = activityDao.listActivities(userId, pageNumber, pageSize);
		List<ActivityVO> vos = new ArrayList<ActivityVO>();
		for (Activity activity : activities) {
			vos.add(activity.toVO());
		}
		return vos;
	}

	/**
	 * 获取某人相关的事件流
	 */
	public List<ActivityVO> listRelatedActivities(Integer userId, int pageNumber, int pageSize) {
		List<Activity> activities = activityDao.listRelatedActivities(userId, pageNumber, pageSize);
		List<ActivityVO> vos = new ArrayList<ActivityVO>();
		for (Activity activity : activities) {
			vos.add(activity.toVO());
		}
		return vos;
	}

	/**
	 * 获取新粉丝事件列表
	 */
	public List<ActivityVO> listNewFansActivities(Integer userId) {
		return listInboxActivities(userId, ActivityType.FOLLOW);
	}

	/**
	 * 获取新回复事件列表
	 */
	public List<ActivityVO> listNewRepliesActivities(Integer userId) {
		return listInboxActivities(userId, ActivityType.COMMENT);
	}

	/**
	 * 获取新赞事件列表
	 */
	public List<ActivityVO> listNewFavorsActivities(Integer userId) {
		return listInboxActivities(userId, ActivityType.FEED_FAVOR);
	}

	/**
	 * 获取用户新事件
	 */
	private List<ActivityVO> listInboxActivities(Integer userId, String activityType) {
		Notification notification = notificationService.getNotification(userId);
		Date lastCheckoutTime = null;
		Date now = new Date();

		if (ActivityType.FOLLOW.equals(activityType)) {
			lastCheckoutTime = notification.getNewFansLastCheckDatetime();
			notification.setNewFansNum(0);
			notification.setNewFansLastCheckDatetime(now);
		} else if (ActivityType.COMMENT.equals(activityType)) {
			lastCheckoutTime = notification.getNewRepliesLastCheckDatetime();
			notification.setNewRepliesNum(0);
			notification.setNewRepliesLastCheckDatetime(now);
		} else if (ActivityType.FEED_FAVOR.equals(activityType)) {
			lastCheckoutTime = notification.getNewFavorsLastCheckDatetime();
			notification.setNewFavorsNum(0);
			notification.setNewFavorsLastCheckDatetime(now);
		}

		List<Activity> activities = activityDao.listInboxActivities(userId, activityType, lastCheckoutTime, now);
		List<ActivityVO> vos = activities2vo(activities);

		notificationService.update(notification);

		logger.debug("notification={}", notification);
		return vos;
	}

	private List<ActivityVO> activities2vo(List<Activity> activities) {
		List<ActivityVO> vos = new ArrayList<ActivityVO>(activities.size());
		for (Activity activity : activities) {
			ActivityVO vo = activity.toVO();
			if (ActivityType.PUBLISH_FEED.equals(activity.getType()) || ActivityType.COMMENT.equals(activity.getType())
					|| ActivityType.FEED_FAVOR.equals(activity.getType())) {
				Feed feed = feedService.getFeedById(activity.getTargetId());
				if (feed != null && feed.getImageId() != null) {
					Image image = imageService.getById(feed.getImageId());
					vo.setFeedImageUrl(image.getSmallURL());
				}
			}
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 获取用户的新粉丝、新回复、新赞事件列表
	 */
	public List<ActivityVO> listNewActivities(Integer userId) {
		Notification notification = notificationService.getNotification(userId);
		Date lastCheckoutTime = notification.getNewRepliesLastCheckDatetime();

		Date now = new Date();
		List<Activity> activities = activityDao.listInboxActivities(userId, lastCheckoutTime, now);
		List<ActivityVO> vos = activities2vo(activities);

		notification.setNewFansNum(0);
		notification.setNewFansLastCheckDatetime(now);
		notification.setNewRepliesNum(0);
		notification.setNewRepliesLastCheckDatetime(now);
		notification.setNewFavorsNum(0);
		notification.setNewFavorsLastCheckDatetime(now);
		notificationService.update(notification);

		logger.debug("notification={}", notification);
		return vos;
	}

}
