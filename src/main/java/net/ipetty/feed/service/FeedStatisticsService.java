package net.ipetty.feed.service;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.core.service.BaseService;
import net.ipetty.feed.repository.FeedStatisticsDao;
import net.ipetty.vo.ActivityType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * FeedStatisticsService
 * 
 * @author luocanfeng
 * @date 2014年6月6日
 */
@Service
@Transactional
public class FeedStatisticsService extends BaseService {

	@Resource
	private FeedStatisticsDao feedStatisticsDao;

	public void updateStatisticsFromActivity(Activity activity) {
		Assert.notNull(activity, "事件不能为空");
		Assert.notNull(activity.getId(), "事件ID不能为空");
		Assert.hasText(activity.getType(), "事件类型不能为空");

		if (activity.getCreatedBy() == null) {
			return;
		}

		String activityType = activity.getType();
		// 重新统计评论数
		if (ActivityType.COMMENT.equals(activityType) || ActivityType.DELETE_COMMENT.equals(activityType)) {
			feedStatisticsDao.recountCommentCount(activity.getTargetId());
		}
		// 重新统计赞的数目
		else if (ActivityType.FEED_FAVOR.equals(activityType) || ActivityType.FEED_UNFAVOR.equals(activityType)) {
			feedStatisticsDao.recountFavorCount(activity.getTargetId());
		}
	}

}
