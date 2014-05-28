package net.ipetty.bonuspoint.service;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.domain.ActivityType;
import net.ipetty.bonuspoint.domain.BonusPoint;
import net.ipetty.bonuspoint.repository.BonusPointConsumptionDao;
import net.ipetty.bonuspoint.repository.BonusPointDao;
import net.ipetty.core.service.BaseService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * BonusPointService
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
@Service
@Transactional
public class BonusPointService extends BaseService {

	@Resource
	private BonusPointDao bonusPointDao;

	@Resource
	private BonusPointConsumptionDao bonusPointConsumptionDao;

	/**
	 * 从事件中获得积分
	 */
	public void gain(Activity activity) {
		Assert.notNull(activity, "事件不能为空");
		Assert.notNull(activity.getId(), "事件ID不能为空");
		BonusPoint bonusPoint = activity2BonusPoint(activity);
		if (bonusPoint != null) {
			bonusPointDao.save(bonusPoint);
		}
	}

	private BonusPoint activity2BonusPoint(Activity activity) {
		Assert.notNull(activity, "事件不能为空");
		Assert.notNull(activity.getId(), "事件ID不能为空");
		Assert.notNull(activity.getCreatedBy(), "事件发起人不能为空");
		Assert.hasText(activity.getType(), "事件类型不能为空");

		BonusPoint bonusPoint = new BonusPoint(activity.getId(), this.getBonus(activity.getCreatedBy(),
				activity.getType()), activity.getCreatedBy(), activity.getCreatedOn());
		return bonusPoint.getBonus() == 0 ? null : bonusPoint;
	}

	/**
	 * 查找指定用户今天在指定事件类型上已经获得的积分数
	 */
	private int getBonusGainedTodayByActivityType(Integer userId, String activityType) {
		return bonusPointDao.getBonusGainedTodayByActivityType(userId, activityType);
	}

	/**
	 * 指定用户在指定事件类型上可以获得的积分数
	 */
	private int getBonus(Integer userId, String activityType) {
		int bonusGainedToday = getBonusGainedTodayByActivityType(userId, activityType);

		// 登录（+1 每日上限 1）
		if (ActivityType.LOGIN.equals(activityType)) {
			return getBonus(bonusGainedToday, 1, 1);
		}
		// 签到（+1 每日上限 1）
		else if (ActivityType.SIGN_IN.equals(activityType)) {
			return getBonus(bonusGainedToday, 1, 1);
		}
		// TODO 在线时长（3分钟+3 每日上限 3）
		// 发帖（+3 每日上限 9）
		else if (ActivityType.PUBLISH_FEED.equals(activityType)) {
			return getBonus(bonusGainedToday, 3, 9);
		}
		// 评论（+3 每日上限 9）
		else if (ActivityType.COMMENT.equals(activityType)) {
			return getBonus(bonusGainedToday, 3, 9);
		}
		// 赞（+3 每日上限 9）
		else if (ActivityType.FEED_FAVOR.equals(activityType)) {
			return getBonus(bonusGainedToday, 3, 9);
		}
		// TODO 邀请（+10 每日上限 20）
		// TODO 查看附近的人（+3 每日上限 3）
		// TODO 与附近的人建立联系（+5 每日上限 5）
		// TODO 与他人互扫二维码（+5 每日上限 5）
		// TODO 商城购物（+50 每日上限 100）
		// TODO 商城购物后进行评论和打分（+30 每日上限 60）
		// TODO 意见反馈（+10 每日上限 10）
		return 0;
	}

	/**
	 * 根据当天已获取积分、单次获取积分、每日积分限额计算出该次应当获得的积分
	 */
	private int getBonus(int bonusGainedToday, int bonusOnce, int bonusPerDay) {
		return bonusGainedToday + bonusOnce > bonusPerDay ? bonusPerDay - bonusGainedToday : bonusOnce;
	}

}
