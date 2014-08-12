package net.ipetty.feed.repository;

import java.util.List;

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

	/**
	 * 获取指定主题消息列表的位置信息列表
	 */
	public List<Location> listByFeedIds(Long... feedIds);

}
