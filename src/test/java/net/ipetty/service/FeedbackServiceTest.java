package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.feedback.domain.Feedback;
import net.ipetty.feedback.service.FeedbackService;
import net.ipetty.user.service.UserService;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedbackServiceTest
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public class FeedbackServiceTest extends BaseTest {

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Resource
	private FeedbackService feedbackService;

	@Resource
	private UserService userService;

	@Test
	public void testFeedbackAnonymously() {
		Feedback feedback = new Feedback("title", "content", "contact", null);
		feedbackService.feedback(feedback);
		Assert.assertNotNull(feedback.getId());
	}

	@Test
	public void testFeedback() {
		Feedback feedback = new Feedback("title", "content", "contact", userService.getByUniqueName(
				TEST_ACCOUNT_UNIQUE_NAME).getId());
		feedbackService.feedback(feedback);
		Assert.assertNotNull(feedback.getId());
	}

}
