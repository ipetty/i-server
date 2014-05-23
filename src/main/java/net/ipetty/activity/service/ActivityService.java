package net.ipetty.activity.service;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.repository.ActivityDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ActivityService
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Service
@Transactional
public class ActivityService {

	@Resource
	private ActivityDao activityDao;

	/**
	 * 保存事件
	 */
	public void save(Activity activity) {
		activityDao.save(activity);
	}

}
