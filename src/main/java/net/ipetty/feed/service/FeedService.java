package net.ipetty.feed.service;

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

}
