package net.ipetty.core.util;

/**
 * 盐值加密工具类
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
public class SaltEncoder {

	private static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	/**
	 * 生成盐值
	 */
	public static final String generateSalt() {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		return Encodes.encodeHex(salt);
	}

	/**
	 * 加密密码
	 */
	public static final String encode(String orignal, String salt) {
		byte[] hashPassword = Digests.sha1(orignal.getBytes(), Encodes.decodeHex(salt), HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}

}
