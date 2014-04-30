package net.ipetty.user.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 用户订阅设置
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class UserSubscription extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -1574047743788183628L;

	private Integer userId; // 用户ID

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
