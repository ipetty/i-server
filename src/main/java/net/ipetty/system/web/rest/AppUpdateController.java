package net.ipetty.system.web.rest;

import java.io.IOException;

import javax.annotation.Resource;

import net.ipetty.core.web.rest.BaseController;
import net.ipetty.system.domain.AppUpdate;
import net.ipetty.system.service.AppUpdateService;
import net.ipetty.vo.AppUpdateVO;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AppUpdateController
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
@Controller
@RequestMapping(value = "/app")
public class AppUpdateController extends BaseController {

	@Resource
	private AppUpdateService appUpdateService;

	/**
	 * 更新软件版本
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void update(@RequestBody AppUpdateVO appUpdate) throws IOException {
		Assert.notNull(appUpdate, "软件版本不能为空");
		Assert.hasText(appUpdate.getAppKey(), "AppKey不能为空");
		Assert.notNull(appUpdate.getVersionCode(), "版本号不能为空");
		Assert.hasText(appUpdate.getVersionName(), "版本号不能为空");
		Assert.hasText(appUpdate.getDownloadUrl(), "下载地址不能为空");
		appUpdateService.save(AppUpdate.fromVO(appUpdate));
	}

	@RequestMapping(value = "checkUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AppUpdateVO checkUpdate(String appKey) throws IOException {
		Assert.hasText(appKey, "AppKey不能为空");
		AppUpdate appUpdate = appUpdateService.getLatest(appKey);
		return appUpdate == null ? null : appUpdate.toVO();
	}

}
