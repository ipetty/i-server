package net.ipetty.sdk;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.PetVO;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;

/**
 * PetApiImpl
 * 
 * @author luocanfeng
 * @date 2014年6月24日
 */
public class PetApiImpl extends BaseApi implements PetApi {

	public PetApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_NEW_PET = "/newpet";

	/**
	 * 新增宠物
	 */
	public PetVO save(PetVO pet) {
		super.requireAuthorization();
		return context.getRestTemplate().postForObject(buildUri(URI_NEW_PET), pet, PetVO.class);
	}

	private static final String URI_GET_BY_ID = "/pet/id/{id}";

	/**
	 * 根据ID获取宠物
	 */
	public PetVO getById(Integer id) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_BY_ID, PetVO.class, id);
	}

	private static final String URI_GET_BY_UID = "/pet/uid/{uid}";

	/**
	 * 根据uid获取宠物
	 */
	public PetVO getByUid(int uid) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_BY_UID, PetVO.class, uid);
	}

	private static final String URI_GET_BY_UNIQUE_NAME = "/pet/{uniqueName}";

	/**
	 * 根据爱宠唯一标识获取宠物
	 */
	public PetVO getByUniqueName(String uniqueName) {
		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_BY_UNIQUE_NAME, PetVO.class,
				uniqueName);
	}

	private static final String URI_LIST_BY_USER_ID = "/pets/{userId}";

	/**
	 * 获取指定用户的所有宠物
	 */
	public List<PetVO> listByUserId(Integer userId) {
		return Arrays.asList(context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_LIST_BY_USER_ID,
				PetVO[].class, userId));
	}

	private static final String URI_UPDATE = "/pet/update";

	/**
	 * 更新宠物信息
	 */
	public PetVO update(PetVO pet) {
		super.requireAuthorization();
		return context.getRestTemplate().postForObject(buildUri(URI_UPDATE), pet, PetVO.class);
	}

	private static final String URI_UPDATE_AVATAR = "/pet/updateAvatar";

	/**
	 * 更新宠物头像
	 */
	@Override
	public String updateAvatar(String petId, String imagePath) {
		super.requireAuthorization();
		Assert.notNull(petId, "宠物ID不能为空");

		URI updateAvatarUri = buildUri(URI_UPDATE_AVATAR);
		LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
		request.add("petId", petId);
		request.add("imageFile", new FileSystemResource(imagePath));
		return context.getRestTemplate().postForObject(updateAvatarUri, request, String.class);
	}

}
