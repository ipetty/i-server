package net.ipetty.sdk;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.AppUpdateVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * AppUpdateApiTest
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
public class AppUpdateApiTest extends BaseTest {

	AppUpdateApi appUpdateApi = new AppUpdateApiImpl(ApiContext.getInstance());

	@Test
	public void testLatestVersion() {
		AppUpdateVO appUpdate = appUpdateApi.latestVersion("test_latest_version_from_api");
		Assert.assertNotNull(appUpdate);
		Assert.assertEquals(2, appUpdate.getVersionCode().intValue());
	}

}
