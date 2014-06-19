package net.ipetty.core.cache;

import java.util.Map;

import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapConfig.EvictionPolicy;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * BaseHazelcastCache
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
public class BaseHazelcastCache {

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

		hazelcast = Hazelcast.getOrCreateHazelcastInstance(hazelcastConfig);
	}

	public static <K extends Object, V extends Object> Map<K, V> getMap(String mapName) {
		return hazelcast.getMap(mapName);
	}

	@SuppressWarnings("unchecked")
	public static <K extends Object, V extends Object> V get(String mapName, K key) {
		return (V) getMap(mapName).get(key);
	}

	public static <K extends Object, V extends Object> void set(String mapName, K key, V value) {
		getMap(mapName).put(key, value);
	}

	public static <K extends Object> void delete(String mapName, K key) {
		getMap(mapName).remove(key);
	}

	public static <K extends Object, V extends Object> boolean containsKey(String mapName, K key) {
		return getMap(mapName).containsKey(key);
	}

}
