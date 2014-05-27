package net.ipetty.bonuspoint.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 积分消费流水帐 - 积分消费记录与积分对应记录
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public class BonusPointBill extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -8639275544557092416L;

	private Long bonusPointId;
	private Long bonusPointConsumptionId;
	private int bonus;

	public BonusPointBill() {
		super();
	}

	public BonusPointBill(Long bonusPointId, Long bonusPointConsumptionId, int bonus) {
		super();
		this.bonusPointId = bonusPointId;
		this.bonusPointConsumptionId = bonusPointConsumptionId;
		this.bonus = bonus;
	}

	public Long getBonusPointId() {
		return bonusPointId;
	}

	public void setBonusPointId(Long bonusPointId) {
		this.bonusPointId = bonusPointId;
	}

	public Long getBonusPointConsumptionId() {
		return bonusPointConsumptionId;
	}

	public void setBonusPointConsumptionId(Long bonusPointConsumptionId) {
		this.bonusPointConsumptionId = bonusPointConsumptionId;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

}
