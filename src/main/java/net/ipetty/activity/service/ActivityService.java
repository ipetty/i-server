package net.ipetty.activity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.domain.ActivityType;
import net.ipetty.activity.repository.ActivityDao;
import net.ipetty.core.service.BaseService;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.repository.FeedDao;
import net.ipetty.feed.service.ImageService;
import net.ipetty.notify.domain.Notification;
import net.ipetty.notify.service.NotificationService;
import net.ipetty.vo.ActivityVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private FeedDao feedDao;

	@Resource
	private ImageService imageService;

	/**
	 * 保存事件
	 */
	public void save(Activity activity) {
		activityDao.save(activity);
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
		return listInboxActivities(userId, ActivityType.FOLLOW, false);
	}

	/**
	 * 获取新回复事件列表
	 */
	public List<ActivityVO> listNewRepliesActivities(Integer userId) {
		return listInboxActivities(userId, ActivityType.COMMENT, true);
	}

	/**
	 * 获取新赞事件列表
	 */
	public List<ActivityVO> listNewFavorsActivities(Integer userId) {
		return listInboxActivities(userId, ActivityType.FEED_FAVOR, true);
	}

	/**
	 * 获取用户新事件
	 */
	private List<ActivityVO> listInboxActivities(Integer userId, String activityType, boolean fullfillImage) {
		Notification notification = notificationService.getNotification(userId);
		Date lastCheckoutTime = null;

		if (ActivityType.FOLLOW.equals(activityType)) {
			lastCheckoutTime = notification.getNewFansLastCheckDatetime();
		} else if (ActivityType.COMMENT.equals(activityType)) {
			lastCheckoutTime = notification.getNewRepliesLastCheckDatetime();
		} else if (ActivityType.FEED_FAVOR.equals(activityType)) {
			lastCheckoutTime = notification.getNewFavorsLastCheckDatetime();
		}

		Date now = new Date();
		List<Activity> activities = activityDao.listInboxActivities(userId, activityType, lastCheckoutTime, now);
		List<ActivityVO> vos = activities2vo(activities, fullfillImage);

		if (ActivityType.FOLLOW.equals(activityType)) {
			notification.setNewFansNum(0);
			notification.setNewFansLastCheckDatetime(now);
		} else if (ActivityType.COMMENT.equals(activityType)) {
			notification.setNewRepliesNum(0);
			notification.setNewRepliesLastCheckDatetime(now);
		} else if (ActivityType.FEED_FAVOR.equals(activityType)) {
			notification.setNewFavorsNum(0);
			notification.setNewFavorsLastCheckDatetime(now);
		}
		notificationService.update(notification);

		return vos;
	}

	private List<ActivityVO> activities2vo(List<Activity> activities, boolean fullfillImage) {
		List<ActivityVO> vos = new ArrayList<ActivityVO>(activities.size());
		for (Activity activity : activities) {
			ActivityVO vo = activity.toVO();
			if (fullfillImage) {
				Feed feed = feedDao.getById(activity.getTargetId());
				if (feed != null && feed.getImageId() != null) {
					Image image = imageService.getById(feed.getImageId());
					vo.setFeedImageUrl(image.getSmallURL());
				}
			}
			vos.add(vo);
		}
		return vos;
	}

}
