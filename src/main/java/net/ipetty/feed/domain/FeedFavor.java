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

	public FeedFavor(Long feedId, Integer createdBy) {
		super();
		this.feedId = feedId;
		super.setCreatedBy(createdBy);
	}

	public FeedFavorVO toVO() {
		FeedFavorVO vo = new FeedFavorVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static FeedFavor fromVO(FeedFavorVO vo) {
		FeedFavor favor = new FeedFavor();
		BeanUtils.copyProperties(vo, favor);
		return favor;
	}

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

}
