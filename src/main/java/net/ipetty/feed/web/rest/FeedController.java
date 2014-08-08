package net.ipetty.feed.web.rest;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.context.SpringContextHelper;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.util.DateUtils;
import net.ipetty.core.web.rest.BaseController;
import net.ipetty.core.web.rest.exception.RestException;
import net.ipetty.feed.domain.Comment;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.FeedFavor;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.domain.Location;
import net.ipetty.feed.service.FeedService;
import net.ipetty.feed.service.ImageService;
import net.ipetty.feed.service.LocationService;
import net.ipetty.user.service.UserService;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedList;
import net.ipetty.vo.FeedTimelineQueryParams;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.FeedWithLocationVO;
import net.ipetty.vo.ImageVO;
import net.ipetty.vo.LocationVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	 * 单独发布图片
	 */
	@RequestMapping(value = "/publishImage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ImageVO publishImage(MultipartFile imageFile) {
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能发布消息");
		}
		Image image = imageService.save(imageFile, SpringContextHelper.getWebContextRealPath(), currentUser.getId(),
				currentUser.getUid());
		return image.toVO();
	}

	/**
	 * 发布消息
	 */
	@RequestMapping(value = "/publishText", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO publishText(String text, String imageId, String longitude, String latitude, String address) {
		logger.debug("publish text={}, image.id={}", text, imageId);
		if (StringUtils.isBlank(text) && StringUtils.isBlank(imageId)) {
			throw new RestException("图片与内容不能为空");
		}

		// 当前用户
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能发布消息");
		}

		// Feed
		Feed feed = new Feed();
		feed.setCreatedBy(currentUser.getId());
		feed.setText(StringUtils.isBlank(text) ? null : text);
		feed.setImageId(StringUtils.isBlank(imageId) ? null : Long.valueOf(imageId));

		// 位置
		if (longitude != null && latitude != null) {
			// TODO generate geohash
			// Location location = new Location(Long.valueOf(longitude),
			// Long.valueOf(latitude), null, address);
			// locationService.save(location);
			// feed.setLocationId(location.getId());
		}

		// 消息
		feedService.save(feed);
		return feedService.getById(feed.getId());
	}

	/**
	 * 发布消息（带位置信息）
	 */
	@RequestMapping(value = "/publishText2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO publishTextWithLocation(@RequestBody FeedWithLocationVO feedWithLocationVO) {
		if (feedWithLocationVO == null) {
			throw new RestException("发布消息不能为空");
		}
		String text = feedWithLocationVO.getText();
		Long imageId = feedWithLocationVO.getImageId();
		LocationVO locationVO = feedWithLocationVO.getLocation();
		if (imageId == null && StringUtils.isBlank(text)) {
			throw new RestException("图片与内容不能为空");
		}
		logger.debug("publish text={}, image.id={}", text, imageId);

		// 当前用户
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能发布消息");
		}

		// Feed
		Feed feed = new Feed();
		feed.setCreatedBy(currentUser.getId());
		feed.setText(StringUtils.isBlank(text) ? null : text);
		feed.setImageId(imageId);

		// 位置
		if (locationVO != null) {
			// TODO generate geohash
			Location location = Location.fromVO(locationVO);
			locationService.save(location);
			feed.setLocationId(location.getId());
		}

		// 消息
		feedService.save(feed);
		return feedService.getById(feed.getId());
	}

	/**
	 * 根据ID获取消息
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO getById(@PathVariable("id") Long id) {
		logger.debug("get by id {}", id);
		Assert.notNull(id, "ID不能为空");
		return feedService.getById(id);
	}

	/**
	 * 删除消息
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean delete(Long id) {
		logger.debug("delete {}", id);
		Assert.notNull(id, "消息ID不能为空");
		FeedVO vo = feedService.getById(id);
		if (vo == null) {
			return false;
		}
		feedService.delete(Feed.fromVO(vo));
		return true;
	}

	/**
	 * 根据时间线分页获取消息（广场）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/square", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Deprecated
	public List<FeedVO> listByTimelineForSquare(String timeline, String pageNumber, String pageSize) {
		Assert.hasText(timeline, "时间线不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();
		logger.debug("list by timeline for square userId={}, timeline={}, pageNumber={}, pageSize={}", currentUserId,
				timeline, pageNumber, pageSize);
		return feedService.listByTimelineForSquare(currentUserId, DateUtils.fromDatetimeString(timeline),
				Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Deprecated
	public List<FeedVO> listByTimelineForHomePage(String timeline, String pageNumber, String pageSize) {
		Assert.hasText(timeline, "时间线不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			// 用户未登录，首页不显示任何消息
			throw new RestException("未登录的用户首页无任何消息");
		}
		logger.debug("list by timeline for homepage userId={}, timeline={}, pageNumber={}, pageSize={}",
				currentUser.getId(), timeline, pageNumber, pageSize);
		return feedService.listByTimelineForHomePage(currentUser.getId(), DateUtils.fromDatetimeString(timeline),
				Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
	}

	/**
	 * 根据时间线分页获取指定用户空间的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/space", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Deprecated
	public List<FeedVO> listByTimelineForSpace(String userId, String timeline, String pageNumber, String pageSize) {
		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(timeline, "时间线不能为空");
		Assert.hasText(pageNumber, "页码不能为空");
		Assert.hasText(pageSize, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();
		logger.debug("list by timeline for space userId={}, timeline={}, pageNumber={}, pageSize={}", userId, timeline,
				pageNumber, pageSize);
		return feedService.listByTimelineForSpace(Integer.valueOf(userId), currentUserId,
				DateUtils.fromDatetimeString(timeline), Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
	}

	/**
	 * 根据时间线分页获取消息（广场）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/square", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedList listByTimelineForSquare(@RequestBody FeedTimelineQueryParams queryParams) {
		Assert.notNull(queryParams, "查询参数不能为空");
		Assert.notNull(queryParams.getTimeline(), "时间线不能为空");
		Assert.isTrue(queryParams.getPageSize() > 0, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();
		logger.debug("list by timeline for square userId={}, timeline={}, pageNumber={}, pageSize={}", currentUserId,
				queryParams.getTimeline(), queryParams.getPageNumber(), queryParams.getPageSize());
		return feedService.listByTimelineForSquare(currentUserId, queryParams.getTimeline(),
				queryParams.getCachedUserVersions(), queryParams.getPageNumber(), queryParams.getPageSize());
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/home", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedList listByTimelineForHomePage(@RequestBody FeedTimelineQueryParams queryParams) {
		Assert.notNull(queryParams, "查询参数不能为空");
		Assert.notNull(queryParams.getTimeline(), "时间线不能为空");
		Assert.isTrue(queryParams.getPageSize() > 0, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			// 用户未登录，首页不显示任何消息
			throw new RestException("未登录的用户首页无任何消息");
		}
		logger.debug("list by timeline for homepage userId={}, timeline={}, pageNumber={}, pageSize={}",
				currentUser.getId(), queryParams.getTimeline(), queryParams.getPageNumber(), queryParams.getPageSize());
		return feedService.listByTimelineForHomePage(currentUser.getId(), queryParams.getTimeline(),
				queryParams.getCachedUserVersions(), queryParams.getPageNumber(), queryParams.getPageSize());
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@RequestMapping(value = "/space", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedList listByTimelineForSpace(@RequestBody FeedTimelineQueryParams queryParams) {
		Assert.notNull(queryParams.getUserId(), "用户ID不能为空");
		Assert.notNull(queryParams, "查询参数不能为空");
		Assert.notNull(queryParams.getTimeline(), "时间线不能为空");
		Assert.isTrue(queryParams.getPageSize() > 0, "每页条数不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		Integer currentUserId = currentUser == null ? null : currentUser.getId();
		logger.debug("list by timeline for homepage userId={}, timeline={}, pageNumber={}, pageSize={}",
				queryParams.getUserId(), queryParams.getTimeline(), queryParams.getPageNumber(),
				queryParams.getPageSize());
		return feedService.listByTimelineForSpace(queryParams.getUserId(), currentUserId, queryParams.getTimeline(),
				queryParams.getCachedUserVersions(), queryParams.getPageNumber(), queryParams.getPageSize());
	}

	/**
	 * 获取指定消息的评论列表
	 */
	@RequestMapping(value = "/comments/{feedId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<CommentVO> listCommentsByFeedId(@PathVariable("feedId") Long feedId) {
		Assert.notNull(feedId, "评论的消息不能为空");
		return feedService.listCommentsByFeedId(feedId);
	}

	/**
	 * 获取指定消息的赞列表
	 */
	@RequestMapping(value = "/favors/{feedId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<FeedFavorVO> listFavorsByFeedId(@PathVariable("feedId") Long feedId) {
		Assert.notNull(feedId, "赞的消息不能为空");
		return feedService.listFavorsByFeedId(feedId);
	}

	/**
	 * 评论
	 */
	@RequestMapping(value = "/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO comment(@RequestBody CommentVO comment) {
		Assert.notNull(comment, "评论不能为空");
		Assert.hasText(comment.getText(), "评论内容不能为空");
		Assert.notNull(comment.getFeedId(), "评论的消息不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能评论消息");
		}
		comment.setCreatedBy(currentUser.getId());
		Comment entity = Comment.fromVO(comment);
		return feedService.comment(entity);
	}

	/**
	 * 删除评论
	 */
	@RequestMapping(value = "/comment/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean deleteComment(Long id) {
		logger.debug("delete {}", id);
		Assert.notNull(id, "评论ID不能为空");
		CommentVO vo = feedService.getCommentById(id);
		if (vo == null) {
			return false;
		}
		feedService.delete(Comment.fromVO(vo));
		return true;
	}

	/**
	 * 赞
	 */
	@RequestMapping(value = "/favor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO favor(@RequestBody FeedFavorVO favor) {
		Assert.notNull(favor, "赞不能为空");
		Assert.notNull(favor.getFeedId(), "赞的消息不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能赞消息");
		}
		favor.setCreatedBy(currentUser.getId());
		FeedFavor entity = FeedFavor.fromVO(favor);
		return feedService.favor(entity);
	}

	/**
	 * 取消赞
	 */
	@RequestMapping(value = "/unfavor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeedVO unfavor(@RequestBody FeedFavorVO favor) {
		Assert.notNull(favor, "赞不能为空");
		Assert.notNull(favor.getFeedId(), "赞的消息不能为空");
		UserPrincipal currentUser = UserContext.getContext();
		if (currentUser == null || currentUser.getId() == null) {
			throw new RestException("注册用户才能赞/取消赞");
		}
		favor.setCreatedBy(currentUser.getId());
		FeedFavor entity = FeedFavor.fromVO(favor);
		return feedService.unfavor(entity);
	}

}
