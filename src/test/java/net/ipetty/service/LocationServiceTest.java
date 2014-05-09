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
		Location location = new Location(31171999l, 121396314l, "", "虹梅路2007号");
		locationService.save(location);
		Assert.assertNotNull(location.getId());
	}

	@Test
	public void testGetById() {
		Location location = new Location(31171999l, 121396314l, "", "虹梅路2007号");
		locationService.save(location);
		Assert.assertNotNull(location.getId());
		location = locationService.getById(location.getId());
		Assert.assertNotNull(location);
	}

}
