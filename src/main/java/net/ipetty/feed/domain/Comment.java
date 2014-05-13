package net.ipetty.feed.domain;

import net.ipetty.core.domain.LongIdEntity;
import net.ipetty.vo.CommentVO;

import org.springframework.beans.BeanUtils;

/**
 * 评论
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public class Comment extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1792004941600597440L;

	private Long feedId; // 评论的对象
	private String text; // 评论内容

	public Comment() {
		super();
	}

	public Comment(Long feedId, String text, Integer createdBy) {
		super();
		this.feedId = feedId;
		this.text = text;
		super.setCreatedBy(createdBy);
	}

	public CommentVO toVO() {
		CommentVO vo = new CommentVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Comment fromVO(CommentVO vo) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(vo, comment);
		return comment;
	}

	public Long getFeedId() {
		return feedId;
	}

	public void setFeedId(Long feedId) {
		this.feedId = feedId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
