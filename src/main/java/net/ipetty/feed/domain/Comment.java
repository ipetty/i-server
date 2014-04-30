package net.ipetty.feed.domain;

import net.ipetty.core.domain.LongIdEntity;

/**
 * 评论
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public class Comment extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1792004941600597440L;

	private Long feedId;

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

}
