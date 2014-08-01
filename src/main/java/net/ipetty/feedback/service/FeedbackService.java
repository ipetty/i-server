package net.ipetty.feedback.service;

import javax.annotation.Resource;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.core.service.BaseService;
import net.ipetty.feedback.domain.Feedback;
import net.ipetty.feedback.repository.FeedbackDao;
import net.ipetty.vo.ActivityType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * FeedbackService
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
@Service
@Transactional
public class FeedbackService extends BaseService {

	@Resource
	private FeedbackDao feedbackDao;

	/**
	 * 反馈意见
	 */
	@ProduceActivity(type = ActivityType.FEEDBACK, createdBy = "${feedback.createdBy}", targetId = "${feedback.id}")
	public void feedback(Feedback feedback) {
		Assert.notNull(feedback, "意见反馈不能为空");
		Assert.hasText(feedback.getContent(), "反馈内容不能为空");
		feedbackDao.save(feedback);
	}

}
