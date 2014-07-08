package net.ipetty.activity.mq;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.service.ActivityService;
import net.ipetty.bonuspoint.service.BonusPointService;
import net.ipetty.feed.service.FeedStatisticsService;
import net.ipetty.user.service.UserStatisticsService;

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
	private BonusPointService bonusPointService;
	@Resource
	private UserStatisticsService userStatisticsService;
	@Resource
	private FeedStatisticsService feedStatisticsService;

	public void receive(Activity activity) {
		logger.debug("receive {}", activity);

		// save activity
		activityService.save(activity);

		// bonus point
		bonusPointService.gain(activity);

		// user statistics
		userStatisticsService.updateStatisticsFromActivity(activity);

		// feed statistics
		feedStatisticsService.updateStatisticsFromActivity(activity);
	}

}
