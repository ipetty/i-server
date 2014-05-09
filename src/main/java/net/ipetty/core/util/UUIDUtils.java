package net.ipetty.core.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * UUIDUtils
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class UUIDUtils {

	/**
	 * 生成12或13位短UUID
	 */
	public static String generateShortUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString().replaceAll("-", "");
		long l = ByteBuffer.wrap(uuidString.getBytes()).getLong();
		return Long.toString(l, Character.MAX_RADIX);
	}

	public static void main(String[] args) {
		int n12 = 0, n13 = 0;
		for (int i = 0; i < 100000; i++) {
			String shortUuid = generateShortUUID();
			if (shortUuid.length() == 12) {
				n12++;
			} else if (shortUuid.length() == 13) {
				n13++;
			} else {
				System.out.println(shortUuid.length());
			}
		}

		System.out.println(n12);
		System.out.println(n13);
	}

}
