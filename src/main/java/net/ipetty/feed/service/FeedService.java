package net.ipetty.feed.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.feed.domain.Comment;
import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.domain.FeedFavor;
import net.ipetty.feed.domain.FeedStatistics;
import net.ipetty.feed.repository.CommentDao;
import net.ipetty.feed.repository.FeedDao;
import net.ipetty.feed.repository.FeedFavorDao;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedVO;

import org.apache.shiro.util.CollectionUtils;
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
	private CommentDao commentDao;

	@Resource
	private FeedFavorDao feedFavorDao;

	/**
	 * 保存消息
	 */
	public FeedVO save(Feed feed) {
		feedDao.save(feed);
		return this.getById(feed.getId());
	}

	/**
	 * 根据ID获取消息
	 */
	public FeedVO getById(Long id) {
		Feed feed = feedDao.getById(id);
		feed.setComments(commentDao.listByFeedId(id));
		feed.setFavors(feedFavorDao.listByFeedId(id));
		feed.setStatistics(feedDao.getStatisticsByFeedId(id));
		FeedVO vo = feed.toVO();
		return vo;
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
		List<FeedStatistics> statisticses = feedDao.listStatisticsByFeedIds(feedIds);
		for (FeedStatistics statistics : statisticses) {
			feedMap.get(statistics.getFeedId()).setStatistics(statistics);
		}

		// TODO fullfill favored

		for (Feed feed : feeds) {
			vos.add(feed.toVO());
		}
		return vos;
	}

	/**
	 * 发表评论
	 */
	public FeedVO comment(Comment comment) {
		Assert.notNull(comment, "评论不能为空");
		Assert.notNull(comment.getFeedId(), "评论的消息不能为空");
		Assert.hasText(comment.getText(), "评论内容不能为空");
		Assert.notNull(comment.getCreatedBy(), "评论人不能为空");
		commentDao.save(comment);
		return this.getById(comment.getFeedId());
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
	public FeedVO favor(FeedFavor favor) {
		Assert.notNull(favor, "赞不能为空");
		Assert.notNull(favor.getFeedId(), "赞的消息不能为空");
		Assert.notNull(favor.getCreatedBy(), "赞的发起人不能为空");
		FeedFavor f = feedFavorDao.getByUserIdAndFeedId(favor.getCreatedBy(), favor.getFeedId());
		Assert.isNull(f, "已经赞过该消息");
		feedFavorDao.save(favor);
		return this.getById(favor.getFeedId());
	}

	/**
	 * 取消赞
	 */
	public FeedVO unfavor(FeedFavor favor) {
		Assert.notNull(favor, "赞不能为空");
		Assert.notNull(favor.getFeedId(), "赞的消息不能为空");
		Assert.notNull(favor.getCreatedBy(), "赞的发起人不能为空");
		FeedFavor f = feedFavorDao.getByUserIdAndFeedId(favor.getCreatedBy(), favor.getFeedId());
		Assert.notNull(f, "您还未赞过该消息");
		feedFavorDao.delete(f.getId());
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
