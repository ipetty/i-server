package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.feedback.domain.Feedback;
import net.ipetty.feedback.service.FeedbackService;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedbackServiceTest
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public class FeedbackServiceTest extends BaseTest {

	@Resource
	private FeedbackService feedbackService;

	@Test
	public void testFeedback() {
		Feedback feedback = new Feedback("title", "content", "contact", null);
		feedbackService.feedback(feedback);
		Assert.assertNotNull(feedback.getId());
	}

}
