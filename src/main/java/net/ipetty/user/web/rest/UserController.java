package net.ipetty.user.web.rest;

import javax.annotation.Resource;

import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.UserVO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * UserController
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	@Resource
	private UserService userService;

	/**
	 * 用户验证(临时,非安全接口)
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO login(String username, String password) {
		logger.debug("login with username={}", username);
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			// IllegalArgumentException
			throw new RestException("用户名、密码不能为空");
		}
		User user = userService.login(username, password);
		return user.toUserVO();
	}

}
