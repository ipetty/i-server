package net.ipetty.user.service;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.core.service.BaseService;
import net.ipetty.user.domain.UserStatistics;
import net.ipetty.user.repository.UserStatisticsDao;
import net.ipetty.vo.ActivityType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * UserStatisticsService
 * 
 * @author luocanfeng
 * @date 2014年6月6日
 */
@Service
@Transactional
public class UserStatisticsService extends BaseService {

	@Resource
	private UserStatisticsDao userStatisticsDao;

	/**
	 * 通过用户ID获取用户统计信息
	 */
	public UserStatistics getByUserId(Integer userId) {
		Assert.notNull(userId, "用户ID不能为空");
		return userStatisticsDao.get(userId);
	}

	/**
	 * 根据事件更新用户统计信息
	 */
	public void updateStatisticsFromActivity(Activity activity) {
		Assert.notNull(activity, "事件不能为空");
		Assert.notNull(activity.getId(), "事件ID不能为空");
		Assert.hasText(activity.getType(), "事件类型不能为空");

		if (activity.getCreatedBy() == null) {
			return;
		}

		String activityType = activity.getType();
		// 重新统计登录次数
		if (ActivityType.LOGIN.equals(activityType)) {
			userStatisticsDao.recountLoginNum(activity.getCreatedBy());
		}
		// 重新统计关注数、粉丝数
		else if (ActivityType.FOLLOW.equals(activityType) || ActivityType.UNFOLLOW.equals(activityType)) {
			userStatisticsDao.recountRelationshipNum(activity.getCreatedBy());
			userStatisticsDao.recountRelationshipNum(activity.getTargetId().intValue());
			return;
		}
		// 重新统计发布消息数
		else if (ActivityType.PUBLISH_FEED.equals(activityType) || ActivityType.DELETE_FEED.equals(activityType)) {
			userStatisticsDao.recountFeedNum(activity.getCreatedBy());
		}
		// 重新统计发布评论数
		else if (ActivityType.COMMENT.equals(activityType) || ActivityType.DELETE_COMMENT.equals(activityType)) {
			userStatisticsDao.recountCommentNum(activity.getCreatedBy());
		}
		// 重新统计发出赞的数目
		else if (ActivityType.FEED_FAVOR.equals(activityType) || ActivityType.FEED_UNFAVOR.equals(activityType)) {
			userStatisticsDao.recountFeedFavorNum(activity.getCreatedBy());
		}
	}

}
