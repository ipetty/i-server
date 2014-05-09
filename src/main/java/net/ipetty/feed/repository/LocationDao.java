package net.ipetty.feed.repository;

import net.ipetty.feed.domain.Location;

/**
 * LocationDao
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public interface LocationDao {

	/**
	 * 保存位置信息
	 */
	public void save(Location location);

	/**
	 * 根据ID获取位置信息
	 */
	public Location getById(Long id);

}
