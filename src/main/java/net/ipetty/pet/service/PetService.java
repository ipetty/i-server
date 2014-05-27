package net.ipetty.pet.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.activity.domain.ActivityType;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.service.BaseService;
import net.ipetty.pet.domain.Pet;
import net.ipetty.pet.repository.PetDao;
import net.ipetty.user.service.UidService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * PetService
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
@Service
@Transactional
public class PetService extends BaseService {

	@Resource
	private PetDao petDao;

	@Resource
	private UidService uidService;

	/**
	 * 新增宠物
	 */
	@ProduceActivity(type = ActivityType.NEW_PET, createdBy = "${pet.createdBy}", targetId = "${pet.id}")
	public void save(Pet pet) {
		synchronized (uidService) {
			Assert.notNull(pet, "宠物不能为空");
			Assert.notNull(pet.getCreatedBy(), "宠物主人不能为空");
			Assert.hasText(pet.getName(), "宠物名称不能为空");

			// check unique
			if (StringUtils.isNotBlank(pet.getUniqueName())) {
				Pet orignal = this.getByUniqueName(pet.getUniqueName());
				if (orignal != null) {
					throw new BusinessException("爱宠唯一标识已存在");
				}
			}

			// retreive an available uid
			int uid = uidService.getUid();
			pet.setUid(uid);

			// set sort order
			List<Pet> pets = petDao.listByUserId(pet.getCreatedBy());
			pet.setSortOrder(pets.size());

			// persist
			petDao.save(pet);

			// mark the uid as used
			uidService.markAsUsed(uid);
		}
	}

	/**
	 * 根据ID获取宠物
	 */
	public Pet getById(Integer id) {
		Assert.notNull(id, "ID不能为空");
		return petDao.getById(id);
	}

	/**
	 * 根据uid获取宠物
	 */
	public Pet getByUid(int uid) {
		Assert.notNull(uid, "UID不能为空");
		Integer id = petDao.getPetIdByUid(uid);
		if (id == null) {
			throw new BusinessException("指定UID（" + uid + "）的宠物不存在");
		}
		return petDao.getById(id);
	}

	/**
	 * 根据爱宠唯一标识获取宠物
	 */
	public Pet getByUniqueName(String uniqueName) {
		Assert.hasText(uniqueName, "爱宠唯一标识不能为空");
		Integer id = petDao.getPetIdByUniqueName(uniqueName);
		if (id == null) {
			throw new BusinessException("指定爱宠唯一标识（" + uniqueName + "）的宠物不存在");
		}
		return petDao.getById(id);
	}

	/**
	 * 获取指定用户的所有宠物
	 */
	public List<Pet> listByUserId(Integer userId) {
		Assert.notNull(userId, "用户ID不能为空");
		return petDao.listByUserId(userId);
	}

	/**
	 * 更新宠物信息
	 */
	@ProduceActivity(type = ActivityType.UPDATE_PET, createdBy = "${pet.createdBy}", targetId = "${pet.id}")
	public void update(Pet pet) {
		Assert.notNull(pet, "宠物不能为空");
		Assert.notNull(pet.getId(), "宠物ID不能为空");
		Assert.notNull(pet.getCreatedBy(), "宠物主人不能为空");
		Assert.hasText(pet.getName(), "宠物名称不能为空");
		petDao.update(pet);
	}

	/**
	 * 设置爱宠唯一标识，只能设置一次，一经设置不能变更
	 */
	@ProduceActivity(type = ActivityType.UPDATE_PET_UNIQUE_NAME, createdBy = "${return.createdBy}", targetId = "${id}")
	public Pet updateUniqueName(Integer id, String uniqueName) {
		Assert.notNull(id, "宠物ID不能为空");
		Assert.hasText(uniqueName, "爱宠唯一标识不能为空");

		if (petDao.getPetIdByUniqueName(uniqueName) != null) { // 校验唯一性
			throw new BusinessException("爱宠唯一标识已存在");
		}

		Pet pet = petDao.getById(id);
		if (pet == null) {
			throw new BusinessException("宠物不存在");
		}

		if (StringUtils.isNotBlank(pet.getUniqueName())) { // 是否已设置；不允许重复设置
			throw new BusinessException("爱宠唯一标识已设置，一经设置不能变更");
		}

		petDao.updateUniqueName(id, uniqueName);

		return petDao.getById(id);
	}

}
