package net.ipetty.core.cache;

import java.util.Map;

import net.ipetty.user.domain.User;

/**
 * UserCache
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
public class UserCache extends BaseHazelcastCache {

	private static final Map<Integer, User> mapUserId2User = getMap(MAP_NAME_USER_ID_TO_USER);

	public static User getUserById(Integer id) {
		return mapUserId2User.get(id);
	}

	public static void saveUserById(User user) {
		mapUserId2User.put(user.getId(), user);
	}

}
