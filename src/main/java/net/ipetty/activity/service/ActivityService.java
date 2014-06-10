package net.ipetty.activity.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.repository.ActivityDao;
import net.ipetty.core.service.BaseService;
import net.ipetty.vo.ActivityVO;

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
public class ActivityService extends BaseService {

	@Resource
	private ActivityDao activityDao;

	/**
	 * 保存事件
	 */
	public void save(Activity activity) {
		activityDao.save(activity);
	}

	/**
	 * 获取某人的事件流
	 */
	public List<ActivityVO> listActivities(Integer userId, int pageNumber, int pageSize) {
		List<Activity> activities = activityDao.listActivities(userId, pageNumber, pageSize);
		List<ActivityVO> vos = new ArrayList<ActivityVO>();
		for (Activity activity : activities) {
			vos.add(activity.toVO());
		}
		return vos;
	}

	/**
	 * 获取某人相关的事件流
	 */
	public List<ActivityVO> listRelatedActivities(Integer userId, int pageNumber, int pageSize) {
		List<Activity> activities = activityDao.listRelatedActivities(userId, pageNumber, pageSize);
		List<ActivityVO> vos = new ArrayList<ActivityVO>();
		for (Activity activity : activities) {
			vos.add(activity.toVO());
		}
		return vos;
	}

}
