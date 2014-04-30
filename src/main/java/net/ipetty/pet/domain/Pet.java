package net.ipetty.pet.domain;

import net.ipetty.core.domain.IntegerIdEntity;

/**
 * 宠物
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Pet extends IntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 2220351366844908107L;

	private Integer userId; // 主人ID

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
