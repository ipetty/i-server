package net.ipetty.activity.mq;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.domain.ActivityType;
import net.ipetty.activity.service.ActivityService;
import net.ipetty.bonuspoint.service.BonusPointService;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.service.FeedService;
import net.ipetty.feed.service.FeedStatisticsService;
import net.ipetty.notify.domain.Notification;
import net.ipetty.notify.service.NotificationService;
import net.ipetty.user.service.UserStatisticsService;
import net.ipetty.vo.CommentVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ActivityMQMessageSender
 * 
 * @author luocanfeng
 * @date 2014年7月8日
 */
@Component
@Transactional
public class ActivityMQMessageReceiver {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private JmsTemplate jmsTemplate;
	@Resource
	private ActivityService activityService;
	@Resource
	private NotificationService notificationService;
	@Resource
	private FeedService feedService;
	@Resource
	private BonusPointService bonusPointService;
	@Resource
	private UserStatisticsService userStatisticsService;
	@Resource
	private FeedStatisticsService feedStatisticsService;

	public void receive(Activity activity) {
		logger.debug("receive {}", activity);

		// save activity
		activityService.save(activity);

		// save activity inbox and update user notification
		if (ActivityType.FOLLOW.equals(activity.getType())) {
			Integer receiverId = activity.getTargetId().intValue();
			activityService.saveActivityInbox(activity.getId(), receiverId);
			Notification notification = notificationService.getNotification(receiverId);
			notification.setNewFansNum(notification.getNewFansNum() + 1);
			notificationService.update(notification);
		} else if (ActivityType.COMMENT.equals(activity.getType())) {
			Long commentId = activity.getTargetId();
			CommentVO comment = feedService.getCommentById(commentId);
			if (comment.getReplyToUserId() != null) {
				Integer receiverId = comment.getReplyToUserId();
				activityService.saveActivityInbox(activity.getId(), receiverId);
				Notification notification = notificationService.getNotification(receiverId);
				notification.setNewRepliesNum(notification.getNewRepliesNum() + 1);
				notificationService.update(notification);
			}

			Long feedId = comment.getFeedId();
			Feed feed = feedService.getFeedById(feedId);
			if (!feed.getCreatedBy().equals(comment.getReplyToUserId())) { // 如果与被回复人是同一人则不发通知
				Integer receiverId = feed.getCreatedBy();
				activityService.saveActivityInbox(activity.getId(), receiverId);
				Notification notification = notificationService.getNotification(receiverId);
				notification.setNewRepliesNum(notification.getNewRepliesNum() + 1);
				notificationService.update(notification);
			}
		} else if (ActivityType.FEED_FAVOR.equals(activity.getType())) {
			Long feedId = activity.getTargetId();
			Feed feed = feedService.getFeedById(feedId);
			Integer receiverId = feed.getCreatedBy();
			activityService.saveActivityInbox(activity.getId(), receiverId);
			Notification notification = notificationService.getNotification(receiverId);
			notification.setNewFavorsNum(notification.getNewFavorsNum() + 1);
			notificationService.update(notification);
		}

		// bonus point
		bonusPointService.gain(activity);

		// user statistics
		// userStatisticsService.updateStatisticsFromActivity(activity);

		// feed statistics
		// feedStatisticsService.updateStatisticsFromActivity(activity);
	}

}
