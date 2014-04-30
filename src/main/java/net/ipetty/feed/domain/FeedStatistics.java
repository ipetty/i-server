package net.ipetty.feed.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 消息统计信息
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public class FeedStatistics extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 7626666895268752144L;

	private Long feedId; // 消息ID

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

}
