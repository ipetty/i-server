package net.ipetty.user.domain;

import java.util.Date;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 用户关注关系
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class UserRelationship extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 7487379933100532714L;

	private Integer friendId; // 被关注者ID
	private Integer followerId; // 关注者ID
	private Date followedOn; // 关注时间

	public Integer getFriendId() {
		return friendId;
	}

	public void setFriendId(Integer friendId) {
		this.friendId = friendId;
	}

	public Integer getFollowerId() {
		return followerId;
	}

	public void setFollowerId(Integer followerId) {
		this.followerId = followerId;
	}

	public Date getFollowedOn() {
		return followedOn;
	}

	public void setFollowedOn(Date followedOn) {
		this.followedOn = followedOn;
	}

}
