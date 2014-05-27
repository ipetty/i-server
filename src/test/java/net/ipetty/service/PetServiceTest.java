package net.ipetty.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.pet.domain.Pet;
import net.ipetty.pet.service.PetService;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.AnimalGender;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * PetServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public class PetServiceTest extends BaseTest {

	@Resource
	private PetService petService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testSave() {
		Pet pet = savePet();
		Assert.assertNotNull(pet.getId());
		Assert.assertNotNull(pet.getCreatedOn());
		pet = petService.getById(pet.getId());
		Assert.assertNotNull(pet);
	}

	private Pet savePet() {
		Pet pet = new Pet();
		pet.setName("_testSavePet");
		pet.setCreatedBy(userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME).getId());
		petService.save(pet);
		return pet;
	}

	@Test
	public void testGetByUid() {
		Pet pet = savePet();
		Assert.assertNotNull(pet.getUid());
		pet = petService.getByUid(pet.getUid());
		Assert.assertNotNull(pet);
	}

	@Test
	public void testGetByUniqueName() {
		String uniqueName = "_testGetByUniqueName";
		Pet pet = savePet();
		petService.updateUniqueName(pet.getId(), uniqueName);
		pet = petService.getByUniqueName(uniqueName);
		Assert.assertNotNull(pet);
	}

	@Test
	public void testListByUserId() {
		savePet();
		List<Pet> pets = petService.listByUserId(userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME).getId());
		Assert.assertTrue(CollectionUtils.isNotEmpty(pets));
		logger.debug("pet list by user({}): {}", TEST_ACCOUNT_UNIQUE_NAME, pets);
	}

	@Test
	public void testUpdate() {
		Pet pet = savePet();
		pet.setGender(AnimalGender.OTHERS);
		petService.update(pet);
		pet = petService.getById(pet.getId());
		Assert.assertEquals(AnimalGender.OTHERS, pet.getGender());
	}

	@Test
	public void testUpdateUniqueName() {
		String uniqueName = "_testUpdateUniqueName";
		Pet pet = savePet();
		petService.updateUniqueName(pet.getId(), uniqueName);
		pet = petService.getByUniqueName(uniqueName);
		Assert.assertNotNull(pet);
	}

}
