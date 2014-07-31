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

	private Long feedId; // 评论的主题ID
	private Long replyToCommentId; // 针对的评论ID，大部分情况下为空
	private Integer replyToUserId; // 针对的评论作者ID，大部分情况下为空
	private String text; // 评论内容
	private boolean deleted = false; // 删除标识

	public Comment() {
		super();
	}

	public Comment(Long feedId, String text, Integer createdBy) {
		super();
		this.feedId = feedId;
		this.text = text;
		super.setCreatedBy(createdBy);
	}

	public Comment(Long feedId, Long replyToCommentId, Integer replyToUserId, String text, Integer createdBy) {
		super();
		this.feedId = feedId;
		this.replyToCommentId = replyToCommentId;
		this.replyToUserId = replyToUserId;
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

	public Long getReplyToCommentId() {
		return replyToCommentId;
	}

	public void setReplyToCommentId(Long replyToCommentId) {
		this.replyToCommentId = replyToCommentId;
	}

	public Integer getReplyToUserId() {
		return replyToUserId;
	}

	public void setReplyToUserId(Integer replyToUserId) {
		this.replyToUserId = replyToUserId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
