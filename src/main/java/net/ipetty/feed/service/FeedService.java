package net.ipetty.feed.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.repository.FeedDao;
import net.ipetty.vo.FeedVO;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FeedService
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Service
@Transactional
public class FeedService {

	@Resource
	private FeedDao feedDao;

	/**
	 * 保存消息
	 */
	public void save(Feed feed) {
		feedDao.save(feed);
	}

	/**
	 * 根据ID获取消息
	 */
	public FeedVO getById(Long id) {
		return feedDao.getById(id).toVO();
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

		for (Feed feed : feeds) {
			// TODO fullfill comments
			// TODO fullfill favors
			// TODO fullfill statistics
			// TODO fullfill favored
			vos.add(feed.toVO());
		}
		return vos;
	}

}
