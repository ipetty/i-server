package net.ipetty.sdk;

import java.util.List;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.PetVO;
import net.ipetty.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * PetApiTest
 * 
 * @author luocanfeng
 * @date 2014年6月25日
 */
public class PetApiTest extends BaseApiTest {

	UserApi userApi = new UserApiImpl(ApiContext.getInstance());
	PetApi petApi = new PetApiImpl(ApiContext.getInstance());

	private static final String TEST_ACCOUNT_EMAIL = "luocanfeng@ipetty.net";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testSave() {
		userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);

		PetVO pet = new PetVO();
		pet.setNickname("testSavePet");
		pet = petApi.save(pet);
		Assert.assertNotNull(pet.getId());
		Assert.assertNotNull(pet.getCreatedBy());
	}

	@Test
	public void testGetByIdOrUidOrUniqueName() {
		userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);

		PetVO pet = new PetVO();
		pet.setNickname("testPetNickname");
		pet.setUniqueName("testPetUniqueName");
		pet = petApi.save(pet);
		Assert.assertNotNull(pet.getId());

		pet = petApi.getById(pet.getId());
		Assert.assertNotNull(pet);
		Assert.assertNotNull(pet.getNickname());

		pet = petApi.getByUid(pet.getUid());
		Assert.assertNotNull(pet);
		Assert.assertNotNull(pet.getNickname());

		pet = petApi.getByUniqueName(pet.getUniqueName());
		Assert.assertNotNull(pet);
		Assert.assertNotNull(pet.getNickname());
	}

	@Test
	public void testListByUserId() {
		UserVO user = userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);

		PetVO pet = new PetVO();
		pet.setNickname("testListByUserId");
		pet = petApi.save(pet);
		Assert.assertNotNull(pet.getId());

		List<PetVO> pets = petApi.listByUserId(user.getId());
		Assert.assertNotNull(pets);
		Assert.assertTrue(pets.size() > 0);
	}

	@Test
	public void testUpdate() {
		userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);

		PetVO pet = new PetVO();
		pet.setNickname("testUpdatePet");
		pet = petApi.save(pet);
		Assert.assertNotNull(pet.getId());

		pet = petApi.getById(pet.getId());
		pet.setNickname("testUpdatePet2");

		pet = petApi.update(pet);
		Assert.assertNotNull(pet);
		Assert.assertEquals("testUpdatePet2", pet.getNickname());
	}

	@Test
	public void testUpdateAvatar() {
		userApi.login(TEST_ACCOUNT_EMAIL, TEST_ACCOUNT_PASSWORD);

		PetVO pet = new PetVO();
		pet.setNickname("testUpdatePet");
		pet = petApi.save(pet);
		Assert.assertNotNull(pet.getId());

		String avatarUrl = petApi.updateAvatar(String.valueOf(pet.getId()), getTestPhotoPath());
		Assert.assertNotNull(avatarUrl);
	}

}
