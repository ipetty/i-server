package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.service.FeedService;
import net.ipetty.feed.service.ImageService;
import net.ipetty.feed.service.LocationService;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class FeedServiceTest extends BaseTest {

	@Resource
	private FeedService feedService;

	@Resource
	private ImageService imageService;

	@Resource
	private LocationService locationService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testSave() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);

		Image image = new Image(user.getId(), "smallURL", "cutURL", "originalURL");
		imageService.save(image);
		Assert.assertNotNull(image.getId());

		Location location = new Location(31171999l, 121396314l, "", "虹梅路2007号");
		locationService.save(location);
		Assert.assertNotNull(location.getId());

		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setImageId(image.getId());
		feed.setText("testFeed");
		feed.setLocationId(location.getId());
		feedService.save(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testGetById() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);

		Image image = new Image(user.getId(), "smallURL", "cutURL", "originalURL");
		imageService.save(image);
		Assert.assertNotNull(image.getId());

		Location location = new Location(31171999l, 121396314l, "", "虹梅路2007号");
		locationService.save(location);
		Assert.assertNotNull(location.getId());

		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setImageId(image.getId());
		feed.setText("testFeed");
		feed.setLocationId(location.getId());
		feedService.save(feed);
		Assert.assertNotNull(feed.getId());

		feed = feedService.getById(feed.getId());
		Assert.assertNotNull(feed);
	}

}
