package net.ipetty.feed.domain;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.core.domain.LongIdEntity;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedVO;

import org.springframework.beans.BeanUtils;

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
	private Long imageId; // 图片ID

	private List<Comment> comments = new ArrayList<Comment>(); // 评论列表
	private List<FeedFavor> favors = new ArrayList<FeedFavor>(); // 赞列表
	private FeedStatistics statistics; // 统计信息
	private Long locationId; // 发表位置ID
	private boolean deleted = false; // 删除标识

	public Feed() {
		super();
	}

	public FeedVO toVO() {
		FeedVO vo = new FeedVO();
		BeanUtils.copyProperties(this, vo, "comments", "favors");

		for (Comment comment : this.getComments()) {
			vo.getComments().add(comment.toVO());
		}

		for (FeedFavor favor : this.getFavors()) {
			vo.getFavors().add(favor.toVO());
		}

		if (this.getStatistics() != null) {
			BeanUtils.copyProperties(this.getStatistics(), vo);
		}

		return vo;
	}

	public static Feed fromVO(FeedVO vo) {
		Feed feed = new Feed();
		BeanUtils.copyProperties(vo, feed, "comments", "favors");

		for (CommentVO commentVO : vo.getComments()) {
			feed.getComments().add(Comment.fromVO(commentVO));
		}

		for (FeedFavorVO favorVO : vo.getFavors()) {
			feed.getFavors().add(FeedFavor.fromVO(favorVO));
		}

		feed.setStatistics(new FeedStatistics());
		BeanUtils.copyProperties(vo, feed.getStatistics());

		return feed;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<FeedFavor> getFavors() {
		return favors;
	}

	public void setFavors(List<FeedFavor> favors) {
		this.favors = favors;
	}

	public FeedStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(FeedStatistics statistics) {
		this.statistics = statistics;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
