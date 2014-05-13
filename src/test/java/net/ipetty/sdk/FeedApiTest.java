package net.ipetty.sdk;

import java.util.Date;
import java.util.List;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.LocationFormVO;
import net.ipetty.vo.UserVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * FeedApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
public class FeedApiTest extends BaseTest {

	FeedApi feedApi = new FeedApiImpl(ApiContext.getInstance("1", "1"));
	UserApi userApi = new UserApiImpl(ApiContext.getInstance("1", "1"));

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testPublish() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test text");
		feedForm.setImagePath(super.getTestPhotoPath());
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishTextOnly() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishTextAndLocation() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test text");
		feedForm.setLocation(new LocationFormVO(123l, 456l, "test location"));
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());
	}

	@Test
	public void testPublishLocationOnly() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

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
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		FeedFormVO feedForm = new FeedFormVO();
		feedForm.setText("test text");
		FeedVO feed = feedApi.publish(feedForm);
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getId());

		feed = feedApi.getById(feed.getId());
		logger.debug("get by id from api is {}", feed);
	}

	@Test
	public void testPublishWithZhcn() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

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
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		List<FeedVO> feeds = feedApi.listByTimelineForSquare(new Date(), 0, 5);
		for (FeedVO feed : feeds) {
			logger.debug("{}", feed);
		}
	}

	@Test
	public void testListByTimelineForHomePage() {
		UserVO user = userApi.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		ApiContext context = ApiContext.getInstance("1", "1");
		context.setAuthorized(true);
		context.setCurrUserId(user.getId());

		List<FeedVO> feeds = feedApi.listByTimelineForHomePage(new Date(), 0, 5);
		for (FeedVO feed : feeds) {
			logger.debug("{}", feed);
		}
	}

}
