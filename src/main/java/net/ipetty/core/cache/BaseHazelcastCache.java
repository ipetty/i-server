package net.ipetty.core.cache;

import java.util.Map;

import com.hazelcast.config.Config;
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
	protected static final HazelcastInstance hazelcast = Hazelcast.getOrCreateHazelcastInstance(hazelcastConfig);

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
