package net.ipetty.bonuspoint.domain;

import java.util.Date;

import net.ipetty.core.domain.LongIdEntity;

/**
 * 积分消费记录
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public class BonusPointConsumption extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -2956379084064793595L;

	private Long activityId;
	private int bonus;

	public BonusPointConsumption() {
		super();
	}

	public BonusPointConsumption(Long activityId, int bonus) {
		super();
		this.activityId = activityId;
		this.bonus = bonus;
	}

	public BonusPointConsumption(Long activityId, int bonus, Integer createdBy) {
		super();
		this.activityId = activityId;
		this.bonus = bonus;
		super.setCreatedBy(createdBy);
		super.setCreatedOn(new Date());
	}

	public BonusPointConsumption(Long activityId, int bonus, Integer createdBy, Date createdOn) {
		super();
		this.activityId = activityId;
		this.bonus = bonus;
		super.setCreatedBy(createdBy);
		super.setCreatedOn(createdOn);
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

}
