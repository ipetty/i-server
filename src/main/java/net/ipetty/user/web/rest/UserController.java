package net.ipetty.user.web.rest;

import javax.annotation.Resource;

import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
public class UserController extends BaseController {

	@Resource
	private UserService userService;

	/*
	 * 用户登陆验证
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO login(String username, String password) {
		logger.debug("login with username={}", username);
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new RestException("用户名、密码不能为空");
		}
		User user = userService.login(username, password);
		return user.toUserVO();
	}

	/**
	 * 注册用户
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO register(String email, String password) {
		logger.debug("register with email={}", email);
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			throw new RestException("邮箱、密码不能为空");
		}

		User registerUser = new User();
		registerUser.setEmail(email);
		registerUser.setPassword(password);
		userService.register(registerUser);
		return registerUser.toUserVO();
	}

	/**
	 * 检查用户名是否可用，true表示可用，false表示不可用
	 */
	@RequestMapping(value = "/user/checkEmailAvailable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean checkEmailAvailable(String email) {
		if (StringUtils.isEmpty(email)) {
			throw new RestException("邮箱不能为空");
		}
		User user = userService.getByLoginName(email);
		logger.debug("get by email {}, result is {}", email, user);
		return user == null;
	}

}
