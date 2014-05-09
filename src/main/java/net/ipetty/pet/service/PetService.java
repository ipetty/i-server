package net.ipetty.pet.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.pet.domain.Pet;
import net.ipetty.pet.repository.PetDao;
import net.ipetty.user.service.UidService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PetService
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
@Service
@Transactional
public class PetService {

	@Resource
	private PetDao petDao;

	@Resource
	private UidService uidService;

	/**
	 * 新增宠物
	 */
	public void save(Pet pet) {
		synchronized (uidService) {
			if (pet.getUserId() == null) {
				throw new BusinessException("宠物主人不能为空");
			}

			// verify name
			if (StringUtils.isBlank(pet.getName())) {
				throw new BusinessException("宠物名称不能为空");
			}

			// check unique
			if (StringUtils.isNotBlank(pet.getUniqueName())) {
				Pet orignal = petDao.getByUniqueName(pet.getUniqueName());
				if (orignal != null) {
					throw new BusinessException("宠物唯一标识已存在");
				}
			}

			// retreive an available uid
			int uid = uidService.getUid();
			pet.setUid(uid);

			// set sort order
			List<Pet> pets = petDao.listByUserId(pet.getUserId());
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
		return petDao.getById(id);
	}

	/**
	 * 根据uid获取宠物
	 */
	public Pet getByUid(int uid) {
		return petDao.getByUid(uid);
	}

	/**
	 * 根据爱宠号获取宠物
	 */
	public Pet getByUniqueName(String uniqueName) {
		return petDao.getByUniqueName(uniqueName);
	}

	/**
	 * 获取指定用户的所有宠物
	 */
	public List<Pet> listByUserId(Integer userId) {
		return petDao.listByUserId(userId);
	}

	/**
	 * 更新宠物信息
	 */
	public void update(Pet pet) {
		if (pet == null || pet.getId() == null) {
			throw new BusinessException("宠物不存在");
		}
		petDao.update(pet);
	}

	/**
	 * 设置爱宠唯一标识，只能设置一次，一经设置不能变更
	 */
	public void updateUniqueName(Integer id, String uniqueName) {
		if (id == null || StringUtils.isBlank(uniqueName)) {
			throw new BusinessException("宠物ID与唯一标识不能为空");
		}

		Pet pet = petDao.getByUniqueName(uniqueName);
		if (pet != null) {// 校验唯一性
			throw new BusinessException("宠物唯一标识已存在");
		}

		pet = petDao.getById(id);
		if (pet == null) {
			throw new BusinessException("宠物不存在");
		}

		if (StringUtils.isNotBlank(pet.getUniqueName())) { // 是否已设置；不允许重复设置
			throw new BusinessException("宠物唯一标识已设置，一经设置不能变更");
		}

		petDao.updateUniqueName(id, uniqueName);
	}

}
