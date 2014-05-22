package net.ipetty.user.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 用户个人空间
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class UserZone extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -5373059166404356563L;

	private Integer userId; // 用户ID
	private UserZoneStatistics statistics; // 个人空间统计信息

	public UserZone() {
		super();
	}

	public UserZone(Integer userId) {
		super();
		this.userId = userId;
	}

	public UserZone(Integer userId, UserZoneStatistics statistics) {
		super();
		this.userId = userId;
		this.statistics = statistics;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserZoneStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(UserZoneStatistics statistics) {
		this.statistics = statistics;
	}

}
