package net.ipetty.core.mq;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

/**
 * BaseActivityHazelcastMQ
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
public class BaseActivityHazelcastMQ {

	protected static final String hazelcastInstanceName = "ipetty_hazelcast";
	protected static final Config hazelcastConfig = new Config(hazelcastInstanceName);
	protected static final HazelcastInstance hazelcast = Hazelcast.getOrCreateHazelcastInstance(hazelcastConfig);

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
