package net.ipetty.activity.web.rest;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.service.ActivityService;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.vo.ActivityVO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ActivityController
 * 
 * @author luocanfeng
 * @date 2014年6月10日
 */
@Controller
public class ActivityController extends BaseController {

	@Resource
	private ActivityService activityService;

	/**
	 * 获取我的事件流
	 */
	@RequestMapping(value = "/activity/mine", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ActivityVO> listActivities(String pageNumber, String pageSize) {
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能查看事件流");
		}
		return activityService.listActivities(currentUser.getId(), Integer.valueOf(pageNumber),
				Integer.valueOf(pageSize));
	}

	/**
	 * 获取与我相关的事件流
	 */
	@RequestMapping(value = "/activity/related", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ActivityVO> listRelatedActivities(String pageNumber, String pageSize) {
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能查看事件流");
		}
		return activityService.listRelatedActivities(currentUser.getId(), Integer.valueOf(pageNumber),
				Integer.valueOf(pageSize));
	}

}
