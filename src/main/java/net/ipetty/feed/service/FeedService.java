package net.ipetty.feed.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.service.BaseService;
import net.ipetty.feed.domain.Comment;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.FeedFavor;
import net.ipetty.feed.domain.FeedStatistics;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.repository.CommentDao;
import net.ipetty.feed.repository.FeedDao;
import net.ipetty.feed.repository.FeedFavorDao;
import net.ipetty.feed.repository.FeedStatisticsDao;
import net.ipetty.feed.repository.ImageDao;
import net.ipetty.user.repository.UserStatisticsDao;
import net.ipetty.vo.ActivityType;
import net.ipetty.vo.CachedUserVersion;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedList;
import net.ipetty.vo.FeedListItem;
import net.ipetty.vo.FeedVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * FeedService
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Service
@Transactional
public class FeedService extends BaseService {

	@Resource
	private FeedDao feedDao;

	@Resource
	private FeedStatisticsDao feedStatisticsDao;

	@Resource
	private UserStatisticsDao userStatisticsDao;

	@Resource
	private CommentDao commentDao;

	@Resource
	private FeedFavorDao feedFavorDao;

	@Resource
	private ImageDao imageDao;

	/**
	 * 保存消息
	 */
	@ProduceActivity(type = ActivityType.PUBLISH_FEED, createdBy = "${feed.createdBy}", targetId = "${feed.id}")
	public FeedVO save(Feed feed) {
		Assert.notNull(feed, "消息不能为空");
		if (StringUtils.isBlank(feed.getText()) && feed.getImageId() == null) {
			throw new BusinessException("图片与内容不能为空");
		}
		feedDao.save(feed);
		feedStatisticsDao.save(new FeedStatistics(feed.getId(), 0, 0));
		// 重新统计发布消息数
		userStatisticsDao.recountFeedNum(feed.getCreatedBy());
		return this.getById(feed.getId());
	}

	/**
	 * 判断用户是否已赞给定消息
	 */
	private boolean isFavored(Long feedId, Integer userId) {
		return feedFavorDao.getByUserIdAndFeedId(userId, feedId) != null;
	}

	/**
	 * 根据ID获取消息
	 */
	public Feed getFeedById(Long id) {
		Assert.notNull(id, "ID不能为空");
		return feedDao.getById(id);
	}

	/**
	 * 根据ID获取消息
	 */
	public FeedVO getById(Long id) {
		Assert.notNull(id, "ID不能为空");
		Feed feed = feedDao.getById(id);
		if (feed == null) {
			return null;
		}
		feed.setComments(commentDao.listByFeedId(id));
		feed.setFavors(feedFavorDao.listByFeedId(id));
		feed.setStatistics(feedStatisticsDao.getStatisticsByFeedId(id));

		FeedVO vo = feed.toVO();

		UserPrincipal principal = UserContext.getContext();
		Integer userId = principal == null ? null : principal.getId();
		if (userId != null) {
			vo.setFavored(this.isFavored(id, userId));
		}

		if (feed.getImageId() != null) {
			Image image = imageDao.getById(feed.getImageId());
			if (image != null) { // 此处不应该为空，仅为容错处理
				vo.setImageOriginalURL(image.getOriginalURL());
				vo.setImageSmallURL(image.getSmallURL());
			}
		}

		return vo;
	}

	/**
	 * 删除消息
	 */
	@ProduceActivity(type = ActivityType.DELETE_FEED, createdBy = "${feed.createdBy}", targetId = "${feed.id}")
	public void delete(Feed feed) {
		Assert.notNull(feed, "消息不存在");
		UserPrincipal principal = UserContext.getContext();
		if (principal == null || feed.getCreatedBy() == null || !feed.getCreatedBy().equals(principal.getId())) {
			throw new BusinessException("只能删除自己的消息");
		}
		feedDao.delete(feed.getId());
		// 重新统计发布消息数
		userStatisticsDao.recountFeedNum(feed.getCreatedBy());
	}

	/**
	 * 根据时间线分页获取消息（广场）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<FeedVO> listByTimelineForSquare(Integer userId, Date timeline, int pageNumber, int pageSize) {
		List<Feed> feeds = feedDao.listByTimelineWithPage(timeline, pageNumber, pageSize);
		return this.feeds2FeedVOs(feeds, userId);
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<FeedVO> listByTimelineForHomePage(Integer userId, Date timeline, int pageNumber, int pageSize) {
		List<Feed> feeds = feedDao.listByUserIdAndTimelineWithPage(userId, timeline, pageNumber, pageSize);
		return this.feeds2FeedVOs(feeds, userId);
	}

	/**
	 * 根据时间线分页获取指定用户空间的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public List<FeedVO> listByTimelineForSpace(Integer userId, Integer currentUserId, Date timeline, int pageNumber,
			int pageSize) {
		List<Feed> feeds = feedDao.listByAuthorIdAndTimelineWithPage(userId, timeline, pageNumber, pageSize);
		return this.feeds2FeedVOs(feeds, currentUserId);
	}

	private List<FeedVO> feeds2FeedVOs(List<Feed> feeds, Integer userId) {
		List<FeedVO> vos = new ArrayList<FeedVO>();
		if (CollectionUtils.isEmpty(feeds)) {
			return vos;
		}

		Map<Long, Feed> feedMap = new LinkedHashMap<Long, Feed>();
		for (Feed feed : feeds) {
			feedMap.put(feed.getId(), feed);
		}

		Long[] feedIds = new Long[feeds.size()];
		feedIds = feedMap.keySet().toArray(feedIds);

		// fullfill comments
		List<Comment> comments = commentDao.listByFeedIds(feedIds);
		for (Comment comment : comments) {
			feedMap.get(comment.getFeedId()).getComments().add(comment);
		}

		// fullfill favors
		List<FeedFavor> favors = feedFavorDao.listByFeedIds(feedIds);
		for (FeedFavor favor : favors) {
			feedMap.get(favor.getFeedId()).getFavors().add(favor);
		}

		// fullfill statistics
		List<FeedStatistics> statisticses = feedStatisticsDao.listStatisticsByFeedIds(feedIds);
		for (FeedStatistics statistics : statisticses) {
			feedMap.get(statistics.getFeedId()).setStatistics(statistics);
		}

		// favored
		Set<Long> favoredFeedIds = new HashSet<Long>();
		List<FeedFavor> myFavors = feedFavorDao.listByUserIdAndFeedIds(userId, feedIds);
		for (FeedFavor favor : myFavors) {
			favoredFeedIds.add(favor.getFeedId());
		}
		// images
		List<Image> images = imageDao.listByFeedIds(feedIds);
		Map<Long, Image> imageMap = new HashMap<Long, Image>();
		for (Image image : images) {
			imageMap.put(image.getId(), image);
		}

		// fullfill favored and image
		for (Feed feed : feeds) {
			Long imageId = feed.getImageId();
			FeedVO vo = feed.toVO();
			if (favoredFeedIds.contains(vo.getId())) {
				vo.setFavored(true);
			}
			if (imageId != null) {
				Image image = imageMap.get(imageId);
				// if (image == null) {
				// logger.error("Image {} not loaded from database.", imageId);
				// } else {
				vo.setImageSmallURL(image.getSmallURL());
				vo.setImageOriginalURL(image.getOriginalURL());
				// }
			}
			vos.add(vo);
		}

		return vos;
	}

	/**
	 * 根据时间线分页获取消息（广场）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public FeedList listByTimelineForSquare(Integer userId, Date timeline, List<CachedUserVersion> cachedUserVersions,
			int pageNumber, int pageSize) {
		List<Feed> feeds = feedDao.listByTimelineWithPage(timeline, pageNumber, pageSize);
		return this.feeds2FeedList(feeds, cachedUserVersions, userId);
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public FeedList listByTimelineForHomePage(Integer userId, Date timeline,
			List<CachedUserVersion> cachedUserVersions, int pageNumber, int pageSize) {
		List<Feed> feeds = feedDao.listByUserIdAndTimelineWithPage(userId, timeline, pageNumber, pageSize);
		return this.feeds2FeedList(feeds, cachedUserVersions, userId);
	}

	/**
	 * 根据时间线分页获取指定用户空间的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public FeedList listByTimelineForSpace(Integer userId, Integer currentUserId, Date timeline,
			List<CachedUserVersion> cachedUserVersions, int pageNumber, int pageSize) {
		List<Feed> feeds = feedDao.listByAuthorIdAndTimelineWithPage(userId, timeline, pageNumber, pageSize);
		return this.feeds2FeedList(feeds, cachedUserVersions, currentUserId);
	}

	private FeedList feeds2FeedList(List<Feed> feeds, List<CachedUserVersion> cachedUserVersions, Integer currentUserId) {
		FeedList feedList = new FeedList();
		if (CollectionUtils.isEmpty(feeds)) {
			return feedList;
		}

		Map<Long, Feed> feedMap = new LinkedHashMap<Long, Feed>();
		for (Feed feed : feeds) {
			feedMap.put(feed.getId(), feed);
		}

		Long[] feedIds = new Long[feeds.size()];
		feedIds = feedMap.keySet().toArray(feedIds);

		// fullfill comments
		List<Comment> comments = commentDao.listByFeedIds(feedIds);
		for (Comment comment : comments) {
			feedMap.get(comment.getFeedId()).getComments().add(comment);
		}

		// fullfill favors
		List<FeedFavor> favors = feedFavorDao.listByFeedIds(feedIds);
		for (FeedFavor favor : favors) {
			feedMap.get(favor.getFeedId()).getFavors().add(favor);
		}

		// fullfill statistics
		List<FeedStatistics> statisticses = feedStatisticsDao.listStatisticsByFeedIds(feedIds);
		for (FeedStatistics statistics : statisticses) {
			feedMap.get(statistics.getFeedId()).setStatistics(statistics);
		}

		// favored
		Set<Long> favoredFeedIds = new HashSet<Long>();
		if (currentUserId != null) {
			List<FeedFavor> myFavors = feedFavorDao.listByUserIdAndFeedIds(currentUserId, feedIds);
			for (FeedFavor favor : myFavors) {
				favoredFeedIds.add(favor.getFeedId());
			}
		}
		// images
		List<Image> images = imageDao.listByFeedIds(feedIds);
		Map<Long, Image> imageMap = new HashMap<Long, Image>();
		for (Image image : images) {
			imageMap.put(image.getId(), image);
		}

		// fullfill favored and image
		for (Feed feed : feeds) {
			Long imageId = feed.getImageId();
			FeedListItem feedListItem = feed.toFeedListItem();
			if (favoredFeedIds.contains(feedListItem.getId())) {
				feedListItem.setFavored(true);
			}
			if (imageId != null) {
				Image image = imageMap.get(imageId);
				// if (image == null) {
				// logger.error("Image {} not loaded from database.", imageId);
				// } else {
				feedListItem.setImageSmallURL(image.getSmallURL());
				feedListItem.setImageOriginalURL(image.getOriginalURL());
				// }
			}
			feedList.getFeeds().add(feedListItem);
		}

		return feedList;
	}

	/**
	 * 发表评论
	 */
	@ProduceActivity(type = ActivityType.COMMENT, createdBy = "${comment.createdBy}", targetId = "${comment.id}", content = "${comment.text}")
	public FeedVO comment(Comment comment) {
		Assert.notNull(comment, "评论不能为空");
		Assert.notNull(comment.getFeedId(), "评论的消息不能为空");
		Assert.hasText(comment.getText(), "评论内容不能为空");
		Assert.notNull(comment.getCreatedBy(), "评论人不能为空");

		if (comment.getReplyToCommentId() != null && comment.getReplyToUserId() == null) {
			Comment replyTo = commentDao.getById(comment.getReplyToCommentId());
			comment.setReplyToUserId(replyTo.getCreatedBy());
		}

		commentDao.save(comment);
		// 重新统计评论数
		feedStatisticsDao.recountCommentCount(comment.getFeedId());
		userStatisticsDao.recountCommentNum(comment.getCreatedBy());
		return this.getById(comment.getFeedId());
	}

	/**
	 * 根据评论ID获取评论
	 */
	public CommentVO getCommentById(Long id) {
		Assert.notNull(id, "评论ID不能为空");
		Comment comment = commentDao.getById(id);
		if (comment == null) {
			return null;
		}
		return comment.toVO();
	}

	/**
	 * 删除评论
	 */
	@ProduceActivity(type = ActivityType.DELETE_COMMENT, createdBy = "${comment.createdBy}", targetId = "${comment.id}")
	public void delete(Comment comment) {
		Assert.notNull(comment, "评论不存在");
		UserPrincipal principal = UserContext.getContext();
		if (principal == null || comment.getCreatedBy() == null || !comment.getCreatedBy().equals(principal.getId())) {
			throw new BusinessException("只能删除自己的评论");
		}
		commentDao.delete(comment.getId());
		// 重新统计评论数
		feedStatisticsDao.recountCommentCount(comment.getFeedId());
		userStatisticsDao.recountCommentNum(comment.getCreatedBy());
	}

	/**
	 * 获取指定主题消息的所有评论列表
	 */
	public List<CommentVO> listCommentsByFeedId(Long feedId) {
		Assert.notNull(feedId, "评论的消息不能为空");
		List<CommentVO> vos = new ArrayList<CommentVO>();
		List<Comment> comments = commentDao.listByFeedId(feedId);
		if (CollectionUtils.isEmpty(comments)) {
			return vos;
		}
		for (Comment comment : comments) {
			vos.add(comment.toVO());
		}
		return vos;
	}

	/**
	 * 赞
	 */
	@ProduceActivity(type = ActivityType.FEED_FAVOR, createdBy = "${favor.createdBy}", targetId = "${favor.feedId}")
	public FeedVO favor(FeedFavor favor) {
		Assert.notNull(favor, "赞不能为空");
		Assert.notNull(favor.getFeedId(), "赞的消息不能为空");
		Assert.notNull(favor.getCreatedBy(), "赞的发起人不能为空");
		FeedFavor f = feedFavorDao.getByUserIdAndFeedId(favor.getCreatedBy(), favor.getFeedId());
		Assert.isNull(f, "已经赞过该消息");
		feedFavorDao.save(favor);
		// 重新统计赞的数目
		feedStatisticsDao.recountFavorCount(favor.getFeedId());
		userStatisticsDao.recountFeedFavorNum(favor.getCreatedBy());
		return this.getById(favor.getFeedId());
	}

	/**
	 * 取消赞
	 */
	@ProduceActivity(type = ActivityType.FEED_UNFAVOR, createdBy = "${favor.createdBy}", targetId = "${favor.feedId}")
	public FeedVO unfavor(FeedFavor favor) {
		Assert.notNull(favor, "赞不能为空");
		Assert.notNull(favor.getFeedId(), "赞的消息不能为空");
		Assert.notNull(favor.getCreatedBy(), "赞的发起人不能为空");
		FeedFavor f = feedFavorDao.getByUserIdAndFeedId(favor.getCreatedBy(), favor.getFeedId());
		Assert.notNull(f, "您还未赞过该消息");
		feedFavorDao.delete(f.getId());
		// 重新统计赞的数目
		feedStatisticsDao.recountFavorCount(favor.getFeedId());
		userStatisticsDao.recountFeedFavorNum(favor.getCreatedBy());
		return this.getById(favor.getFeedId());
	}

	/**
	 * 获取指定主题消息列表的所有赞列表
	 */
	public List<FeedFavorVO> listFavorsByFeedId(Long feedId) {
		Assert.notNull(feedId, "赞的消息不能为空");
		List<FeedFavorVO> vos = new ArrayList<FeedFavorVO>();
		List<FeedFavor> favors = feedFavorDao.listByFeedId(feedId);
		if (CollectionUtils.isEmpty(favors)) {
			return vos;
		}
		for (FeedFavor favor : favors) {
			vos.add(favor.toVO());
		}
		return vos;
	}

}
