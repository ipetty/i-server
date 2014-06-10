package net.ipetty.bonuspoint.domain;

import java.util.Date;

import net.ipetty.core.domain.LongIdEntity;
import net.ipetty.vo.BonusPointVO;

import org.springframework.beans.BeanUtils;

/**
 * 积分
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class BonusPoint extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -5786507916978903896L;

	private Long activityId;
	private int bonus;
	private boolean expired = false;
	private boolean spent = false;

	public BonusPoint() {
		super();
	}

	public BonusPoint(Long activityId, int bonus) {
		super();
		this.activityId = activityId;
		this.bonus = bonus;
	}

	public BonusPoint(Long activityId, int bonus, Integer createdBy) {
		super();
		this.activityId = activityId;
		this.bonus = bonus;
		super.setCreatedBy(createdBy);
		super.setCreatedOn(new Date());
	}

	public BonusPoint(Long activityId, int bonus, Integer createdBy, Date createdOn) {
		super();
		this.activityId = activityId;
		this.bonus = bonus;
		super.setCreatedBy(createdBy);
		super.setCreatedOn(createdOn);
	}

	public BonusPointVO toVO() {
		BonusPointVO vo = new BonusPointVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public boolean isSpent() {
		return spent;
	}

	public void setSpent(boolean spent) {
		this.spent = spent;
	}

}
