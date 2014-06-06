package net.ipetty.activity.mq;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.service.ActivityService;
import net.ipetty.bonuspoint.service.BonusPointService;
import net.ipetty.user.service.UserStatisticsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

/**
 * ActivityMessageListener
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Component
@Transactional
public class ActivityMessageListener implements MessageListener<Activity> {

	@Resource
	private ActivityService activityService;
	@Resource
	private BonusPointService bonusPointService;
	@Resource
	private UserStatisticsService userStatisticsService;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message<Activity> message) {
		Activity activity = message.getMessageObject();
		logger.debug("receive {}", activity);

		// save activity
		activityService.save(activity);

		// bonus point
		bonusPointService.gain(activity);

		// user statistics
		userStatisticsService.updateStatisticsFromActivity(activity);

		// feed statistics
	}

}
