package net.ipetty.feedback.web.rest;

import javax.annotation.Resource;

import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.feedback.domain.Feedback;
import net.ipetty.feedback.service.FeedbackService;
import net.ipetty.vo.FeedbackVO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FeedbackController
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
@Controller
public class FeedbackController extends BaseController {

	@Resource
	private FeedbackService feedbackService;

	/**
	 * 反馈意见
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedbackVO feedback(String title, String content, String contact) {
		Assert.hasText(content, "反馈内容不能为空");

		Feedback feedback = new Feedback(title, content, contact, null);
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser != null) {
			feedback.setCreatedBy(currentUser.getId());
		}

		feedbackService.feedback(feedback);
		return feedback.toVO();
	}

}
