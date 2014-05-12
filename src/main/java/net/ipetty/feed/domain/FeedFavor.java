package net.ipetty.feed.domain;

import net.ipetty.core.domain.LongIdEntity;
import net.ipetty.vo.FeedFavorVO;

import org.springframework.beans.BeanUtils;

/**
 * 赞
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public class FeedFavor extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -4061407920776168383L;

	private Long feedId; // 赞的对象

	public FeedFavor() {
		super();
	}

	public FeedFavor(Long feedId) {
		super();
		this.feedId = feedId;
	}

	public FeedFavorVO toVO() {
		FeedFavorVO vo = new FeedFavorVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

}
