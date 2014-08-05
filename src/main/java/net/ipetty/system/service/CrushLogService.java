package net.ipetty.system.service;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.system.repository.CrushLogDao;

import org.springframework.stereotype.Service;

/**
 * CrushLogService
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
@Service
public class CrushLogService extends BaseService {

	@Resource
	private CrushLogDao crushLogDao;

	/**
	 * 保存崩溃日志
	 */
	public void save(String crushLog) {
		crushLogDao.save(crushLog);
	}

}
