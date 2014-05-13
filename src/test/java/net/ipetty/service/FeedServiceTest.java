package net.ipetty.service;

import java.util.Date;
import java.util.List;

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
import net.ipetty.vo.FeedVO;

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

		FeedVO feedVO = feedService.getById(feed.getId());
		Assert.assertNotNull(feedVO);
		Assert.assertNotNull(feedVO.getId());
	}

	@Test
	public void testListByTimelineForSquare() throws InterruptedException {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		this.createFeeds(10, user.getId());
		Date timeline = new Date();
		List<FeedVO> feeds = feedService.listByTimelineForSquare(user.getId(), timeline, 0, 5);
		Assert.assertEquals(5, feeds.size());
		for (FeedVO feed : feeds) {
			logger.debug("{}", feed);
		}
		feeds = feedService.listByTimelineForSquare(user.getId(), timeline, 1, 5);
		Assert.assertEquals(5, feeds.size());
		for (FeedVO feed : feeds) {
			logger.debug("{}", feed);
		}
	}

	@Test
	public void testListByTimelineForHomePage() throws InterruptedException {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		this.createFeeds(10, user.getId());
		Date timeline = new Date();
		List<FeedVO> feeds = feedService.listByTimelineForHomePage(user.getId(), timeline, 0, 5);
		Assert.assertEquals(5, feeds.size());
		for (FeedVO feed : feeds) {
			logger.debug("{}", feed);
		}
		feeds = feedService.listByTimelineForHomePage(user.getId(), timeline, 1, 5);
		Assert.assertEquals(5, feeds.size());
		for (FeedVO feed : feeds) {
			logger.debug("{}", feed);
		}
	}

	private void createFeeds(int num, Integer creator) throws InterruptedException {
		for (int i = 0; i < num; i++) {
			this.createFeed("test text " + i, creator);
			Thread.sleep(1000);
		}
	}

	private void createFeed(String text, Integer creator) {
		Feed feed = new Feed();
		feed.setCreatedBy(creator);
		feed.setText(text);
		feedService.save(feed);
	}

}
