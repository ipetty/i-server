package net.ipetty.sdk;

import java.util.Date;
import java.util.List;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.LocationFormVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
public class FeedApiTest extends BaseTest {

	FeedApi feedApi = new FeedApiImpl(ApiContext.getInstance());
	UserApi userApi = new UserApiImpl(ApiContext.getInstance());

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";
	private static final String TEST_ACCOUNT_PASSWORD = "888888";

	@Test
	public void testPublish() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		feedForm.setImagePath(super.getTestPhotoPath());
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishTextOnly() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishTextAndLocation() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishLocationOnly() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		try {
			feedApi.publish(feedForm);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testGetById() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());

		feed = feedApi.getById(feed.getId());
		logger.debug("get by id from api is {}", feed);
	}

	@Test
	public void testPublishWithZhcn() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("中文内容");
		feedForm.setImagePath(super.getTestPhotoPath());
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
		logger.debug("test publish Chinese content {}", feed);
	}

	@Test
	public void testListByTimelineForSquare() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<FeedVO> feeds = feedApi.listByTimelineForSquare(new Date(), 0, 5);
		logger.debug("--testListByTimelineForSquare {}", feeds);
	}

	@Test
	public void testListByTimelineForHomePage() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<FeedVO> feeds = feedApi.listByTimelineForHomePage(new Date(), 0, 5);
		logger.debug("--testListByTimelineForHomePage {}", feeds);
	}

	@Test
	public void testComment() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());

		CommentVO comment = new CommentVO();
		comment.setFeedId(feed.getId());
		comment.setText("test comment text");
		feed = feedApi.comment(comment);
		Assert.assertEquals(1, feed.getComments().size());
	}

	@Test
	public void testFavor() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());

		FeedFavorVO favor = new FeedFavorVO();
		favor.setFeedId(feed.getId());
		feed = feedApi.favor(favor);
		Assert.assertEquals(1, feed.getFavors().size());
	}

	@Test
	public void testUnfavor() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());

		FeedFavorVO favor = new FeedFavorVO();
		favor.setFeedId(feed.getId());
		feed = feedApi.favor(favor);
		Assert.assertEquals(1, feed.getFavors().size());

		feed = feedApi.unfavor(favor);
		Assert.assertEquals(0, feed.getFavors().size());
	}

}
