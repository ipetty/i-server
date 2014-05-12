package net.ipetty.user.web.rest;

import javax.annotation.Resource;

import net.ipetty.core.context.SpringContextHelper;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.service.FeedService;
import net.ipetty.feed.service.ImageService;
import net.ipetty.feed.service.LocationService;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.FeedVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * FeedController
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
@Controller
@RequestMapping(value = "/feed")
public class FeedController extends BaseController {

	@Resource
	private FeedService feedService;

	@Resource
	private ImageService imageService;

	@Resource
	private LocationService locationService;

	@Resource
	private UserService userService;

	/**
	 * 发布消息
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO publish(String text, MultipartFile imageFile, String longitude, String latitude, String address) {
		logger.debug("publish {}", text);
		if (StringUtils.isEmpty(text) && imageFile == null) {
			throw new RestException("图片与内容不能为空");
		}

		// 当前用户
		// FIXME
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null) {
			currentUser = new UserPrincipal();
		}
		currentUser.setId(13);
		currentUser.setUid(3);
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能发布消息");
		}

		// Feed
		Feed feed = new Feed();
		feed.setCreatedBy(currentUser.getId());
		feed.setText(text);

		// 图片
		Image image = imageService.save(imageFile, SpringContextHelper.getWebContextRealPath(), currentUser.getId(),
				currentUser.getUid());
		feed.setImageId(image.getId());

		// 位置
		if (longitude != null && latitude != null && address != null) {
			Location location = new Location(Long.valueOf(longitude), Long.valueOf(latitude), null, address);
			locationService.save(location);
			feed.setLocationId(location.getId());
		}

		// 消息
		feedService.save(feed);
		return feed.toVO();
	}

}
