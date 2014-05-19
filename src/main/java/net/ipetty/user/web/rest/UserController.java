package net.ipetty.user.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.cache.BaseHazelcastCache;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.util.UUIDUtils;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.pet.domain.Pet;
import net.ipetty.pet.service.PetService;
import net.ipetty.user.domain.User;
import net.ipetty.user.domain.UserProfile;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	@Resource
	private PetService petService;

	/**
	 * 用户登陆验证
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO login(String username, String password) {
		logger.debug("login with username={}", username);
		Assert.hasText(username, "用户名不能为空");
		Assert.hasText(password, "密码不能为空");
		User user = userService.login(username, password);

		// 设置用户上下文
		UserPrincipal principal = UserPrincipal.fromUser(user, UUIDUtils.generateShortUUID());
		UserContext.setContext(principal);
		logger.debug("generate user token {}", principal.getToken());

		// 将用户token写入缓存
		BaseHazelcastCache.set(BaseHazelcastCache.MAP_NAME_USER_TOKEN_TO_USER_ID, principal.getToken(),
				principal.getId());
		logger.debug("all user token: {}", BaseHazelcastCache.getMap(BaseHazelcastCache.MAP_NAME_USER_TOKEN_TO_USER_ID)
				.keySet());

		return user.toVO();
	}

	/**
	 * 注册
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO register(@RequestBody RegisterVO register) {
		logger.debug("register {}", register);
		Assert.notNull(register, "注册信息不能为空");
		Assert.hasText(register.getEmail(), "邮箱不能为空");
		Assert.hasText(register.getPassword(), "密码不能为空");

		User user = new User();
		user.setEmail(register.getEmail());
		user.setPassword(register.getPassword());

		UserProfile profile = new UserProfile();
		profile.setNickname(register.getNickname());
		profile.setGender(register.getGender());
		user.setProfile(profile);

		userService.register(user);

		if (StringUtils.isNotBlank(register.getPetName())) {
			Pet pet = new Pet();
			pet.setUserId(user.getId());
			pet.setGender(register.getPetGender());
			pet.setName(register.getPetName());
			petService.save(pet);
		}

		return user.toVO();
	}

	/**
	 * 检查用户名是否可用，true表示可用，false表示不可用
	 */
	@RequestMapping(value = "/user/checkEmailAvailable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean checkEmailAvailable(String email) {
		Assert.hasText(email, "邮箱不能为空");
		User user = userService.getByLoginName(email);
		logger.debug("get by email {}, result is {}", email, user);
		return user == null;
	}

	/**
	 * 根据ID获取用户帐号
	 */
	@RequestMapping(value = "/user/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO getById(@PathVariable("id") Integer id) {
		Assert.notNull(id, "ID不能为空");
		User user = userService.getById(id);
		if (user == null) {
			throw new RestException("用户不存在");
		}
		logger.debug("get by id {}, result is {}", id, user);
		return user.toVO();
	}

	/**
	 * 根据uid获取用户帐号
	 */
	@RequestMapping(value = "/user/uid/{uid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO getByUid(@PathVariable("uid") int uid) {
		User user = userService.getByUid(uid);
		if (user == null) {
			throw new RestException("用户不存在");
		}
		logger.debug("get by uid {}, result is {}", uid, user);
		return user.toVO();
	}

	/**
	 * 根据爱宠号获取用户帐号
	 */
	@RequestMapping(value = "/user/{uniqueName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO getByUniqueName(@PathVariable("uniqueName") String uniqueName) {
		Assert.hasText(uniqueName, "爱宠号不能为空");
		User user = userService.getByUniqueName(uniqueName);
		if (user == null) {
			throw new RestException("用户不存在");
		}
		logger.debug("get by unique name {}, result is {}", uniqueName, user);
		return user.toVO();
	}

	/**
	 * 更新用户帐号信息
	 */
	// TODO

	/**
	 * 设置爱宠号，只能设置一次，一经设置不能变更
	 */
	@RequestMapping(value = "/user/uniqueName", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean updateUniqueName(String id, String uniqueName) {
		Assert.hasText(id, "ID不能为空");
		Assert.hasText(uniqueName, "爱宠号不能为空");
		userService.updateUniqueName(Integer.valueOf(id), uniqueName);
		return true;
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean changePassword(String id, String oldPassword, String newPassword) {
		Assert.hasText(id, "ID不能为空");
		Assert.hasText(oldPassword, "旧密码不能为空");
		Assert.hasText(newPassword, "新密码不能为空");
		userService.changePassword(Integer.valueOf(id), oldPassword, newPassword);
		return true;
	}

	/**
	 * 关注
	 */
	@RequestMapping(value = "/user/follow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean follow(String friendId, String followerId) {
		Assert.hasText(friendId, "被关注人ID不能为空");
		Assert.hasText(followerId, "关注人ID不能为空");
		userService.follow(Integer.valueOf(friendId), Integer.valueOf(followerId));
		return true;
	}

	/**
	 * 是否已关注，true为已关注，false为未关注
	 */
	@RequestMapping(value = "/user/isfollow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean isFollow(String friendId, String followerId) {
		Assert.hasText(friendId, "被关注人ID不能为空");
		Assert.hasText(followerId, "关注人ID不能为空");
		return userService.isFollow(Integer.valueOf(friendId), Integer.valueOf(followerId));
	}

	/**
	 * 取消关注
	 */
	@RequestMapping(value = "/user/unfollow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean unfollow(String friendId, String followerId) {
		Assert.hasText(friendId, "被关注人ID不能为空");
		Assert.hasText(followerId, "关注人ID不能为空");
		userService.unfollow(Integer.valueOf(friendId), Integer.valueOf(followerId));
		return true;
	}

	/**
	 * 分页获取关注列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<UserVO> listFriends(String userId, String pageNumber, String pageSize) {
		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");

		// FIXME move to service, and fullfill isFollowed field
		List<UserVO> vos = new ArrayList<UserVO>();
		List<User> users = userService.listFriends(Integer.valueOf(userId), Integer.valueOf(pageNumber),
				Integer.valueOf(pageSize));
		for (User user : users) {
			vos.add(user.toVO());
		}
		return vos;
	}

}
