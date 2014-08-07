package net.ipetty.system.service;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.system.domain.CrashLog;
import net.ipetty.system.repository.CrashLogDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CrashLogService
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
@Service
@Transactional
public class CrashLogService extends BaseService {

	@Resource
	private CrashLogDao crashLogDao;

	/**
	 * 保存崩溃日志
	 */
	public void save(CrashLog crashLog) {
		crashLogDao.save(crashLog);
	}

}
