package net.ipetty.pet.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.pet.domain.Pet;
import net.ipetty.pet.service.PetService;
import net.ipetty.vo.PetVO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * PetController
 * 
 * @author luocanfeng
 * @date 2014年6月24日
 */
@Controller
public class PetController extends BaseController {

	@Resource
	private PetService petService;

	/**
	 * 新增宠物
	 */
	@RequestMapping(value = "/newpet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PetVO save(@RequestBody PetVO pet) {
		Assert.notNull(pet, "宠物不能为空");
		Assert.hasText(pet.getNickname(), "宠物昵称不能为空");

		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能创建宠物");
		}

		pet.setCreatedBy(currentUser.getId());

		return petService.save(Pet.fromVO(pet)).toVO();
	}

	/**
	 * 根据ID获取宠物
	 */
	@RequestMapping(value = "/pet/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PetVO getById(@PathVariable("id") Integer id) {
		Assert.notNull(id, "宠物ID不能为空");
		Pet pet = petService.getById(id);
		if (pet == null) {
			throw new RestException("宠物不存在");
		}
		return pet.toVO();
	}

	/**
	 * 根据uid获取宠物
	 */
	@RequestMapping(value = "/pet/uid/{uid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PetVO getByUid(@PathVariable("uid") int uid) {
		Pet pet = petService.getByUid(uid);
		if (pet == null) {
			throw new RestException("宠物不存在");
		}
		return pet.toVO();
	}

	/**
	 * 根据爱宠唯一标识获取宠物
	 */
	@RequestMapping(value = "/pet/{uniqueName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PetVO getByUniqueName(@PathVariable("uniqueName") String uniqueName) {
		Assert.hasText(uniqueName, "爱宠号不能为空");
		Pet pet = petService.getByUniqueName(uniqueName);
		if (pet == null) {
			throw new RestException("宠物不存在");
		}
		return pet.toVO();
	}

	/**
	 * 获取指定用户的所有宠物
	 */
	@RequestMapping(value = "/pets/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PetVO> listByUserId(@PathVariable("userId") Integer userId) {
		Assert.notNull(userId, "用户ID不能为空");
		List<Pet> pets = petService.listByUserId(userId);
		List<PetVO> petVos = new ArrayList<PetVO>();
		for (Pet pet : pets) {
			petVos.add(pet.toVO());
		}
		return petVos;
	}

	/**
	 * 更新宠物信息
	 */
	@RequestMapping(value = "/pet/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PetVO update(@RequestBody PetVO pet) {
		Assert.notNull(pet, "宠物不能为空");
		Assert.notNull(pet.getId(), "宠物ID不能为空");
		petService.update(Pet.fromVO(pet));
		return petService.getById(pet.getId()).toVO();
	}

}
