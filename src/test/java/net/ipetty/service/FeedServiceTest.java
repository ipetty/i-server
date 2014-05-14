package net.ipetty.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.feed.domain.Comment;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.FeedFavor;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.service.FeedService;
import net.ipetty.feed.service.ImageService;
import net.ipetty.feed.service.LocationService;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
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
		Assert.assertNotNull(image.getCreatedOn());

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
		Assert.assertNotNull(feed.getCreatedOn());
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
		logger.debug("--testListByTimelineForSquare {}", feeds);
		feeds = feedService.listByTimelineForSquare(user.getId(), timeline, 1, 5);
		Assert.assertEquals(5, feeds.size());
		logger.debug("--testListByTimelineForSquare {}", feeds);
	}

	@Test
	public void testListByTimelineForHomePage() throws InterruptedException {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		this.createFeeds(10, user.getId());
		Date timeline = new Date();
		List<FeedVO> feeds = feedService.listByTimelineForHomePage(user.getId(), timeline, 0, 5);
		Assert.assertEquals(5, feeds.size());
		logger.debug("--testListByTimelineForHomePage {}", feeds);
		feeds = feedService.listByTimelineForHomePage(user.getId(), timeline, 1, 5);
		Assert.assertEquals(5, feeds.size());
		logger.debug("--testListByTimelineForHomePage {}", feeds);
	}

	private void createFeeds(int num, Integer creator) throws InterruptedException {
		for (int i = 0; i < num; i++) {
			this.createFeed("test feed text " + i, creator);
			Thread.sleep(50);
		}
	}

	private void createFeed(String text, Integer creator) {
		Feed feed = new Feed();
		feed.setCreatedBy(creator);
		feed.setText(text);
		feedService.save(feed);
	}

	@Test
	public void testComment() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		Comment comment = new Comment(feed.getId(), "test comment text", user.getId());
		feedService.comment(comment);
		Assert.assertNotNull(comment.getId());
		Assert.assertNotNull(comment.getCreatedOn());
	}

	@Test
	public void testListCommentsByFeedId() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		for (int i = 0; i < 5; i++) {
			Comment comment = new Comment(feed.getId(), "test comment text " + i, user.getId());
			feedService.comment(comment);
			Assert.assertNotNull(comment.getId());
		}
		List<CommentVO> comments = feedService.listCommentsByFeedId(feed.getId());
		Assert.assertEquals(5, comments.size());
		logger.debug("--testListCommentsByFeedId {}", comments);
	}

	@Test
	public void testListFeedsWithComments() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		for (int i = 0; i < 5; i++) {
			Comment comment = new Comment(feed.getId(), "test comment text " + i, user.getId());
			feedService.comment(comment);
			Assert.assertNotNull(comment.getId());
		}

		List<FeedVO> feeds = feedService.listByTimelineForSquare(user.getId(), new Date(), 0, 5);
		logger.debug("--testListFeedsWithComments {}", feeds);
	}

	@Test
	public void testFavor() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		FeedFavor favor = new FeedFavor(feed.getId(), user.getId());
		feedService.favor(favor);
		Assert.assertNotNull(favor.getId());
		Assert.assertNotNull(favor.getCreatedOn());

		try {
			feedService.favor(favor);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testUnfavor() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		FeedFavor favor = new FeedFavor(feed.getId(), user.getId());
		feedService.favor(favor);
		Assert.assertNotNull(favor.getId());

		feedService.unfavor(favor);
		feedService.favor(favor);
	}

	@Test
	public void testListFavorsByFeedId() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		FeedFavor favor = new FeedFavor(feed.getId(), user.getId());
		feedService.favor(favor);
		Assert.assertNotNull(favor.getId());

		List<FeedFavorVO> favors = feedService.listFavorsByFeedId(feed.getId());
		Assert.assertEquals(1, favors.size());
	}

	@Test
	public void testListFeedsWithFavors() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Feed feed = new Feed();
		feed.setCreatedBy(user.getId());
		feed.setText("test feed text");
		feedService.save(feed);

		FeedFavor favor = new FeedFavor(feed.getId(), user.getId());
		feedService.favor(favor);

		List<FeedVO> feeds = feedService.listByTimelineForSquare(user.getId(), new Date(), 0, 5);
		logger.debug("--testListFeedsWithFavors {}", feeds);
	}

}
