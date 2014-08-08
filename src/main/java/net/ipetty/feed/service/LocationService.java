package net.ipetty.feed.service;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.repository.LocationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * LocationService
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Service
@Transactional
public class LocationService extends BaseService {

	@Resource
	private LocationDao locationDao;

	/**
	 * 保存位置信息
	 */
	public void save(Location location) {
		Assert.notNull(location, "位置不能为空");
		Assert.notNull(location.getLongitude(), "位置经度不能为空");
		Assert.notNull(location.getLatitude(), "位置纬度不能为空");
		locationDao.save(location);
	}

	/**
	 * 根据ID获取位置信息
	 */
	public Location getById(Long id) {
		Assert.notNull(id, "ID不能为空");
		return locationDao.getById(id);
	}

}
