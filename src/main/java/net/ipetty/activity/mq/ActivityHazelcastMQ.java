package net.ipetty.activity.mq;

import net.ipetty.activity.domain.Activity;
import net.ipetty.core.context.SpringContextHelper;
import net.ipetty.core.mq.BaseActivityHazelcastMQ;

import com.hazelcast.core.ITopic;

/**
 * BaseHazelcastMQ
 * 
 * @author luocanfeng
 * @date 2014年5月22日
 */
public class ActivityHazelcastMQ extends BaseActivityHazelcastMQ {

	protected static final String ACTIVITY_MQ_TOPIC = "mqTopicActivity";
	protected static final ITopic<Activity> topic = getTopic(ACTIVITY_MQ_TOPIC);

	static {
		topic.addMessageListener(SpringContextHelper.getBean(ActivityMessageListener.class));
	}

	public static void publish(Activity message) {
		topic.publish(message);
	}

}
