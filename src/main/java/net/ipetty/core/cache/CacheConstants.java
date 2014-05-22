package net.ipetty.core.cache;

/**
 * CacheConstants
 * 
 * @author luocanfeng
 * @date 2014年5月20日
 */
public interface CacheConstants {

	/* user token */
	public static final String CACHE_USER_TOKEN_TO_USER_ID = "mapUserToken2UserId";

	/* user */
	public static final String CACHE_USER_ID_TO_USER = "mapUserId2User";

	/* user */
	public static final String CACHE_USER_UID_TO_USER_ID = "mapUserUid2UserId";

	/* user */
	public static final String CACHE_USER_UNIQUE_NAME_TO_USER_ID = "mapUserUN2UserId";

	/* user */
	public static final String CACHE_USER_LOGIN_NAME_TO_USER_ID = "mapUserLN2UserId";

	/* user profile */
	public static final String CACHE_USER_PROFILE_ID_TO_PROFILE = "mapUserId2UserProfile";

	/* user profile */
	public static final String CACHE_USER_RELATIONSHIP_ID_TO_RELATIONSHIP = "mapUserRelationshipId2Relationship";

	/* user zone */
	public static final String CACHE_USER_ZONE_ID_TO_ZONE = "mapUseZoneId2UserZone";

	/* pet */
	public static final String CACHE_PET_ID_TO_PET = "mapPetId2Pet";

	/* pet */
	public static final String CACHE_PET_UID_TO_PET_ID = "mapPetUid2PetId";

	/* pet */
	public static final String CACHE_PET_UN_TO_PET_ID = "mapPetUN2PetId";

	/* image */
	public static final String CACHE_IMAGE_ID_TO_IMAGE = "mapImageId2Image";

	/* location */
	public static final String CACHE_LOCATION_ID_TO_LOCATION = "mapLocationId2Location";

}
