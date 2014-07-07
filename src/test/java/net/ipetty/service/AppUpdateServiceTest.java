package net.ipetty.service;

import javax.annotation.Resource;

import junit.framework.Assert;
import net.ipetty.core.test.BaseTest;
import net.ipetty.system.domain.AppUpdate;
import net.ipetty.system.service.AppUpdateService;

import org.junit.Test;

/**
 * AppUpdateServiceTest
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
public class AppUpdateServiceTest extends BaseTest {

	@Resource
	private AppUpdateService appUpdateService;

	@Test
	public void testSave() {
		String appName = "test_save_app";
		AppUpdate appUpdate = new AppUpdate();
		appUpdate.setAppName(appName);
		appUpdate.setAppKey(appName);
		appUpdate.setAppSecret(appName);
		appUpdate.setVersionCode(1);
		appUpdate.setVersionName("1");
		appUpdate.setVersionDescription(appName);
		appUpdate.setDownloadUrl(appName);
		appUpdateService.save(appUpdate);
	}

	@Test
	public void testGetLatest() {
		String appName = "test_get_latest_app";
		AppUpdate appUpdate = new AppUpdate();
		appUpdate.setAppName(appName);
		appUpdate.setAppKey(appName);
		appUpdate.setAppSecret(appName);
		appUpdate.setVersionCode(1);
		appUpdate.setVersionName("1");
		appUpdate.setVersionDescription(appName);
		appUpdate.setDownloadUrl(appName);
		appUpdateService.save(appUpdate);

		appUpdate = new AppUpdate();
		appUpdate.setAppName(appName);
		appUpdate.setAppKey(appName);
		appUpdate.setAppSecret(appName);
		appUpdate.setVersionCode(2);
		appUpdate.setVersionName("2");
		appUpdate.setVersionDescription(appName);
		appUpdate.setDownloadUrl(appName);
		appUpdateService.save(appUpdate);

		appUpdate = null;

		appUpdate = appUpdateService.getLatest(appName);
		Assert.assertEquals(2, appUpdate.getVersionCode().intValue());
	}

}
