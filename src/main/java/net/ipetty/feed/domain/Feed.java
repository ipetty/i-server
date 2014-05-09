package net.ipetty.feed.domain;

import java.util.ArrayList;
import java.util.List;

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
	private Image image; // 图片

	private List<Comment> comments = new ArrayList<Comment>(); // 评论列表
	private List<FeedFavor> favors = new ArrayList<FeedFavor>(); // 赞列表
	private FeedStatistics statistics; // 统计信息
	private Location location; // 发表地点

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
