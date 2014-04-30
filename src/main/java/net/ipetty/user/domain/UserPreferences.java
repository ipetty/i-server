package net.ipetty.user.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 用户个性化设置
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class UserPreferences extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 892773215076008166L;

	private Integer userId; // 用户ID
	private UserSubscription subscription; // 用户订阅设置

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserSubscription getSubscription() {
		return subscription;
	}

	public void setSubscription(UserSubscription subscription) {
		this.subscription = subscription;
	}

}
