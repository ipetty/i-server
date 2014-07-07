package net.ipetty.system.repository;

import net.ipetty.system.domain.AppUpdate;

/**
 * AppUpdateDao
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
public interface AppUpdateDao {

	/**
	 * 新增应用版本
	 */
	public void save(AppUpdate appUpdate);

	/**
	 * 获取指定应用的最新版本
	 */
	public AppUpdate getByAppKeyAndVersionCode(String appKey, Integer versionCode);

	/**
	 * 获取指定应用的最新版本
	 */
	public AppUpdate getLatest(String appKey);

}
