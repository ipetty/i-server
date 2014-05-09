package net.ipetty.feed.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.feed.domain.Feed;
import net.ipetty.feed.repository.FeedDao;

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
	public Feed getById(Long id) {
		return feedDao.getById(id);
	}

	/**
	 * 根据时间线分页获取消息（广场）
	 */
	public List<Feed> listByTimelineForSquare(Integer userId, Date timeline, int pageNumber, int pageSize) {
		// TODO
		return null;
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 */
	public List<Feed> listByTimelineForHomePage(Integer userId, Date timeline, int pageNumber, int pageSize) {
		// TODO
		return null;
	}

	private List<Feed/* VO */> feeds2FeedVOs(List<Feed> feeds) {
		// TODO
		return null;
	}

}
