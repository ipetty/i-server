package net.ipetty.core.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
public class Caches {

	private static Map<String, Map<?, ?>> MAPS = new HashMap<String, Map<?, ?>>();

	@SuppressWarnings("unchecked")
	public static <K extends Object, V extends Object> Map<K, V> getMap(String mapName) {
		Map<K, V> map = (Map<K, V>) MAPS.get(mapName);
		if (map == null) {
			map = new HashMap<K, V>();
			MAPS.put(mapName, map);
		}
		return map;
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
