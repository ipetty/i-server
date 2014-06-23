package net.ipetty.user.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.cache.BaseHazelcastCache;
import net.ipetty.core.cache.CacheConstants;
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
import net.ipetty.vo.UserFormVO;
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
import org.springframework.web.multipart.MultipartFile;

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
		BaseHazelcastCache.set(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, principal.getToken(), principal.getId());

		return user.toVO();
	}

	/**
	 * 登出
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean logout() {
		logger.debug("logout");

		// 清理用户上下文
		UserPrincipal principal = UserContext.getContext();
		if (principal == null || StringUtils.isBlank(principal.getToken())) {
			logger.error("用户尚未登录");
			UserContext.clearContext();
			return true;
		}
		BaseHazelcastCache.delete(CacheConstants.CACHE_USER_TOKEN_TO_USER_ID, principal.getToken());
		UserContext.clearContext();

		return true;
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
			pet.setCreatedBy(user.getId());
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
		return this.fullfillIsFollowed(user.toVO());
	}

	private UserVO fullfillIsFollowed(UserVO vo) {
		if (vo == null) {
			return vo;
		}
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser != null && currentUser.getId() != null) {
			vo.setFollowed(userService.isFollow(vo.getId(), currentUser.getId()));
		}
		return vo;
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
		return this.fullfillIsFollowed(user.toVO());
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
		return this.fullfillIsFollowed(user.toVO());
	}

	/**
	 * 设置爱宠号，只能设置一次，一经设置不能变更
	 */
	@RequestMapping(value = "/user/uniqueName", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean updateUniqueName(String uniqueName) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能设置爱宠号");
		}
		if (StringUtils.isNotBlank(currentUser.getUniqueName())) {
			throw new RestException("爱宠号已设置，不能再次设置爱宠号");
		}
		Assert.hasText(uniqueName, "爱宠号不能为空");
		userService.updateUniqueName(currentUser.getId(), uniqueName);
		return true;
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean changePassword(String oldPassword, String newPassword) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能修改密码");
		}
		Assert.hasText(oldPassword, "旧密码不能为空");
		Assert.hasText(newPassword, "新密码不能为空");
		userService.changePassword(currentUser.getId(), oldPassword, newPassword);
		return true;
	}

	/**
	 * 关注
	 */
	@RequestMapping(value = "/user/follow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean follow(String friendId) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能关注/取消关注");
		}
		Assert.hasText(friendId, "被关注人ID不能为空");
		userService.follow(Integer.valueOf(friendId), currentUser.getId());
		return true;
	}

	/**
	 * 是否已关注，true为已关注，false为未关注
	 */
	@RequestMapping(value = "/user/isfollow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean isFollow(String friendId) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			return false; // 未登录一律显示为未关注
		}
		Assert.hasText(friendId, "被关注人ID不能为空");
		return userService.isFollow(Integer.valueOf(friendId), currentUser.getId());
	}

	/**
	 * 取消关注
	 */
	@RequestMapping(value = "/user/unfollow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean unfollow(String friendId) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能关注/取消关注");
		}
		Assert.hasText(friendId, "被关注人ID不能为空");
		userService.unfollow(Integer.valueOf(friendId), currentUser.getId());
		return true;
	}

	/**
	 * 分页获取关注列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/user/friends", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UserVO> listFriends(String userId, String pageNumber, String pageSize) {
		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();

		List<User> users = userService.listFriends(Integer.valueOf(userId), Integer.valueOf(pageNumber),
				Integer.valueOf(pageSize));
		return this.fullfillIsFollowed(users, currentUserId);
	}

	private List<UserVO> fullfillIsFollowed(List<User> users, Integer currentUserId) {
		List<UserVO> vos = new ArrayList<UserVO>();
		for (User user : users) {
			// fullfill isFollowed field
			vos.add(this.fullfillIsFollowed(user.toVO(), currentUserId));
		}
		return vos;
	}

	private UserVO fullfillIsFollowed(UserVO vo, Integer currentUserId) {
		if (vo == null || currentUserId == null) {
			return vo;
		}
		vo.setFollowed(userService.isFollow(vo.getId(), currentUserId));
		return vo;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/user/followers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UserVO> listFollowers(String userId, String pageNumber, String pageSize) {
		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();

		List<User> users = userService.listFollowers(Integer.valueOf(userId), Integer.valueOf(pageNumber),
				Integer.valueOf(pageSize));
		return this.fullfillIsFollowed(users, currentUserId);
	}

	/**
	 * 获取好友列表（双向关注）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/user/bifriends", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UserVO> listBiFriends(String userId, String pageNumber, String pageSize) {
		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();

		List<User> users = userService.listBiFriends(Integer.valueOf(userId), Integer.valueOf(pageNumber),
				Integer.valueOf(pageSize));
		return this.fullfillIsFollowed(users, currentUserId);
	}

	/**
	 * 更新用户头像
	 */
	@RequestMapping(value = "/user/updateAvatar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateAvatar(MultipartFile imageFile) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能更新头像");
		}
		return userService.updateAvatar(imageFile, currentUser.getId(), currentUser.getUid());
	}

	/**
	 * 更新个人空间背景图片
	 */
	@RequestMapping(value = "/user/updateBackground", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String updateBackground(MultipartFile imageFile) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能更新个人空间背景图片");
		}
		return userService.updateBackground(imageFile, currentUser.getId(), currentUser.getUid());
	}

	/**
	 * 修改用户个人信息
	 */
	@RequestMapping(value = "/user/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserVO update(@RequestBody UserFormVO userFormVo) {
		Assert.notNull(userFormVo, "用户个人信息表单不能为空");
		Assert.notNull(userFormVo.getId(), "用户ID不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能修改个人信息");
		}

		Assert.isTrue(userFormVo.getId().equals(currentUser.getId()), "只能修改自己的个人信息");

		userService.updateProfile(UserProfile.fromUserFormVO(userFormVo));
		return userService.getById(userFormVo.getId()).toVO();
	}

}
