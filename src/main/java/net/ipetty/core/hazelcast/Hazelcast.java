package net.ipetty.core.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapConfig.EvictionPolicy;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.core.HazelcastInstance;

/**
 * Hazelcast
 * 
 * @author luocanfeng
 * @date 2014年7月4日
 */
public class Hazelcast {

	protected static final String hazelcastInstanceName = "ipetty_hazelcast";
	protected static final Config hazelcastConfig = new Config(hazelcastInstanceName);
	protected static final HazelcastInstance hazelcast;

	static {
		hazelcastConfig.setProperty("hazelcast.logging.type", "log4j");

		hazelcastConfig
				.addMapConfig(
						new MapConfig()
								.setName("mapUserId2User")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(8).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU))
				.addMapConfig(
						new MapConfig()
								.setName("mapUserId2UserProfile")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(8).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU))
				.addMapConfig(
						new MapConfig()
								.setName("mapPetId2Pet")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(8).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU))
				.addMapConfig(
						new MapConfig()
								.setName("mapFeedId2Feed")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(32).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU))
				.addMapConfig(
						new MapConfig()
								.setName("mapCommentId2Comment")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(32).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU))
				.addMapConfig(
						new MapConfig()
								.setName("mapImageId2Image")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(32).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU))
				.addMapConfig(
						new MapConfig()
								.setName("map*")
								.setInMemoryFormat(InMemoryFormat.OBJECT)
								.setMaxSizeConfig(
										new MaxSizeConfig().setSize(2).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
								.setEvictionPolicy(EvictionPolicy.LFU));
		hazelcastConfig.getMapConfig("default").setInMemoryFormat(InMemoryFormat.OBJECT)
				.setMaxSizeConfig(new MaxSizeConfig().setSize(8).setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE))
				.setEvictionPolicy(EvictionPolicy.LFU);

		hazelcastConfig.addTopicConfig(new TopicConfig().setName("mqTopic"));

		hazelcast = com.hazelcast.core.Hazelcast.getOrCreateHazelcastInstance(hazelcastConfig);
	}

	public static HazelcastInstance getInstance() {
		return hazelcast;
	}

}
