package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.service.ImageService;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;

import org.junit.Assert;
import org.junit.Test;

/**
 * ImageServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class ImageServiceTest extends BaseTest {

	@Resource
	private ImageService imageService;

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
	}

	@Test
	public void testGetById() {
		User user = userService.getByUniqueName(TEST_ACCOUNT_UNIQUE_NAME);
		Image image = new Image(user.getId(), "smallURL", "cutURL", "originalURL");
		imageService.save(image);
		Assert.assertNotNull(image.getId());
		image = imageService.getById(image.getId());
		Assert.assertNotNull(image);
	}

}
