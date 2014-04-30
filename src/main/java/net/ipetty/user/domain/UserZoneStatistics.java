package net.ipetty.user.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 用户个人空间统计信息
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class UserZoneStatistics extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 3799504243304331382L;

	private Integer userId; // 用户ID

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
