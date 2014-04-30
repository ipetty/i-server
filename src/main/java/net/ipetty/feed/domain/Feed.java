package net.ipetty.feed.domain;

import net.ipetty.core.domain.LongIdEntity;

/**
 * 消息
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Feed extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -4750939714532404430L;

	private String text; // 文本内容
	private String image; // 图片

}
