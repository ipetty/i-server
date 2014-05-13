package net.ipetty.feed.service;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.repository.LocationDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		locationDao.save(location);
	}

	/**
	 * 根据ID获取位置信息
	 */
	public Location getById(Long id) {
		return locationDao.getById(id);
	}

}
