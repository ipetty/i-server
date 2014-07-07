package net.ipetty.system.service;

import javax.annotation.Resource;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.service.BaseService;
import net.ipetty.system.domain.AppUpdate;
import net.ipetty.system.repository.AppUpdateDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AppUpdateService
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
@Service
@Transactional
public class AppUpdateService extends BaseService {

	@Resource
	private AppUpdateDao appUpdateDao;

	/**
	 * 新增应用版本
	 */
	public void save(AppUpdate appUpdate) {
		if (appUpdateDao.getByAppKeyAndVersionCode(appUpdate.getAppKey(), appUpdate.getVersionCode()) != null) {
			throw new BusinessException("此版本已经存在！");
		}

		appUpdateDao.save(appUpdate);
	}

	/**
	 * 获取指定应用的最新版本
	 */
	public AppUpdate getLatest(String appKey) {
		return appUpdateDao.getLatest(appKey);
	}

}
