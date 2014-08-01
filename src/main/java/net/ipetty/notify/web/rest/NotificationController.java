package net.ipetty.notify.web.rest;

import javax.annotation.Resource;

import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.notify.domain.Notification;
import net.ipetty.notify.service.NotificationService;
import net.ipetty.vo.NotificationVO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * NotificationController
 * 
 * @author luocanfeng
 * @date 2014年8月1日
 */
@Controller
public class NotificationController extends BaseController {

	@Resource
	private NotificationService notificationService;

	/**
	 * 获取我的通知
	 */
	@RequestMapping(value = "/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public NotificationVO getMyNotification() {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能查看通知");
		}
		Notification notification = notificationService.getNotification(currentUser.getId());
		if (notification == null) {
			throw new RestException("查找用户通知对象时出错");
		}
		return notification.toVO();
	}

}
