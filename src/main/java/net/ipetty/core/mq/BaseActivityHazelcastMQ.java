package net.ipetty.core.mq;

import net.ipetty.core.hazelcast.Hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

/**
 * BaseActivityHazelcastMQ
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
public class BaseActivityHazelcastMQ {

	protected static final HazelcastInstance hazelcast = Hazelcast.getInstance();

	protected static final String MQ_TOPIC = "mqTopic";

	public static <T> void publish(ITopic<T> topic, T message) {
		topic.publish(message);
	}

	public static <T> ITopic<T> getTopic(String topicName) {
		return hazelcast.getTopic(topicName);
	}

	public static <T> void publish(String topicName, T message) {
		getTopic(topicName).publish(message);
	}

}
