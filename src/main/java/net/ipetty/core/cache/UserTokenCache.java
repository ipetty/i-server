package net.ipetty.core.cache;

import java.util.Map;

/**
 * UserTokenCache
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
public class UserTokenCache extends BaseHazelcastCache {

	private static final Map<String, Integer> mapUserToken2UserId = getMap(MAP_NAME_USER_TOKEN_TO_USER_ID);

	/**
	 * 判断token是否有效
	 */
	public static boolean isTokenAvaliable(String userToken) {
		return mapUserToken2UserId.containsKey(userToken);
	}

	/**
	 * 根据token获取用户基本信息
	 */
	public static Integer getUserIdByToken(String userToken) {
		return mapUserToken2UserId.get(userToken);
	}

	/**
	 * 保存token及用户基本信息
	 */
	public static void saveUserIdByToken(String userToken, Integer userId) {
		mapUserToken2UserId.put(userToken, userId);
	}

}
