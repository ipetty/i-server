package net.ipetty.sdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.CachedUserVersion;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedList;
import net.ipetty.vo.FeedTimelineQueryParams;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.LocationVO;
import net.ipetty.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
public class FeedApiTest extends BaseApiTest {

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
		feedForm.setLocation(new LocationVO(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true));
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
		feedForm.setLocation(new LocationVO(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true));
		FeedVO feed = feedApi.publishWithLocation(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishLocationOnly() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setLocation(new LocationVO(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true));
		try {
			feedApi.publishWithLocation(feedForm);
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
	public void testDelete() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test feed text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());

		feed = feedApi.getById(feed.getId());
		logger.debug("get by id from api is {}", feed);

		Assert.assertTrue(feedApi.delete(feed.getId()));
	}

	@Test
	public void testPublishWithZhcn() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("中文内容");
		feedForm.setImagePath(super.getTestPhotoPath());
		feedForm.setLocation(new LocationVO(31.1790070000, 121.4023470000, "bd09ll", 9f, "上海", "上海", "徐汇", "", true));
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
	public void testListByTimelineForSpace() {
		UserVO user = userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		List<FeedVO> feeds = feedApi.listByTimelineForSpace(user.getId(), new Date(), 0, 5);
		logger.debug("--testListByTimelineForSpace {}", feeds);
	}

	@Test
	public void testListByTimelineForSquareWithCachedUsers() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedList feeds = feedApi.listByTimelineForSquare(new FeedTimelineQueryParams(new Date(),
				new ArrayList<CachedUserVersion>(), 0, 5));
		logger.debug("--testListByTimelineForSquare {}", feeds);
	}

	@Test
	public void testListByTimelineForHomePageWithCachedUsers() {
		userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedList feeds = feedApi.listByTimelineForHomePage(new FeedTimelineQueryParams(new Date(),
				new ArrayList<CachedUserVersion>(), 0, 5));
		logger.debug("--testListByTimelineForHomePage {}", feeds);
	}

	@Test
	public void testListByTimelineForSpaceWithCachedUsers() {
		UserVO user = userApi.login(TEST_ACCOUNT_UNIQUE_NAME, TEST_ACCOUNT_PASSWORD);

		FeedList feeds = feedApi.listByTimelineForSpace(new FeedTimelineQueryParams(user.getId(), new Date(),
				new ArrayList<CachedUserVersion>(), 0, 5));
		logger.debug("--testListByTimelineForSpace {}", feeds);
	}

	@Test
	public void testListCommentsByFeedId() {
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
		feed = feedApi.comment(comment);
		feed = feedApi.comment(comment);
		Assert.assertEquals(3, feed.getComments().size());

		List<CommentVO> comments = feedApi.listCommentsByFeedId(feed.getId());
		Assert.assertEquals(3, comments.size());
	}

	@Test
	public void testListFavorsByFeedId() {
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

		List<FeedFavorVO> favors = feedApi.listFavorsByFeedId(feed.getId());
		Assert.assertEquals(1, favors.size());
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
	public void testDeleteComment() {
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

		Assert.assertTrue(feedApi.deleteComment(feed.getComments().get(0).getId()));
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
		feed = feedApi.getById(feed.getId());
		Assert.assertEquals(1, feed.getFavors().size());
		logger.debug("get favored feed by id is {}", feed);
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
