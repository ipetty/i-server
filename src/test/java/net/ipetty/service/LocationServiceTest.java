package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.service.LocationService;

import org.junit.Assert;
import org.junit.Test;

/**
 * LocationServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class LocationServiceTest extends BaseTest {

	@Resource
	private LocationService locationService;

	@Test
	public void testSave() {
		Location location = new Location(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true);
		locationService.save(location);
		Assert.assertNotNull(location.getId());
	}

	@Test
	public void testGetById() {
		Location location = new Location(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true);
		locationService.save(location);
		Assert.assertNotNull(location.getId());
		location = locationService.getById(location.getId());
		Assert.assertNotNull(location);
	}

}
