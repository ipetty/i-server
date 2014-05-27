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
	private int commentCount; // 评论数
	private int favorCount; // 赞的数量

	public FeedStatistics() {
		super();
	}

	public FeedStatistics(Long feedId, int commentCount, int favorCount) {
		super();
		this.feedId = feedId;
		this.commentCount = commentCount;
		this.favorCount = favorCount;
	}

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getFavorCount() {
		return favorCount;
	}

	public void setFavorCount(int favorCount) {
		this.favorCount = favorCount;
	}

}
