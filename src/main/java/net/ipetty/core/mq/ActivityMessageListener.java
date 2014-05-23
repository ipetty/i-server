package net.ipetty.core.mq;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.service.ActivityService;
import net.ipetty.core.context.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

/**
 * ActivityMessageListener
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
public class ActivityMessageListener implements MessageListener<Activity> {

	private static ActivityService activityService;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private static ActivityService getActivityService() {
		if (activityService == null) {
			activityService = SpringContextHelper.getBean(ActivityService.class);
		}
		return activityService;
	}

	@Override
	public void onMessage(Message<Activity> message) {
		Activity activity = message.getMessageObject();
		logger.debug("receive {}", activity);

		// save activity
		getActivityService().save(activity);

		// bonus point

		// statistics
	}

}
