package net.ipetty.pet.repository;

import java.util.List;

import net.ipetty.pet.domain.Pet;

/**
 * PetDao
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public interface PetDao {

	/**
	 * 保存宠物信息
	 */
	public void save(Pet pet);

	/**
	 * 根据ID获取宠物
	 */
	public Pet getById(Integer id);

	/**
	 * 根据uid获取宠物ID
	 */
	public Integer getPetIdByUid(int uid);

	/**
	 * 根据爱宠号获取宠物ID
	 */
	public Integer getPetIdByUniqueName(String uniqueName);

	/**
	 * 获取指定用户的所有宠物
	 */
	public List<Pet> listByUserId(Integer userId);

	/**
	 * 更新宠物信息
	 */
	public void update(Pet pet);

	/**
	 * 更新爱宠唯一标识
	 */
	public void updateUniqueName(Integer id, String uniqueName);

}
