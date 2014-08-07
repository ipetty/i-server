package net.ipetty.system.repository;

import net.ipetty.system.domain.CrashLog;

/**
 * CrashLogDao
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public interface CrashLogDao {

	/**
	 * 保存崩溃日志
	 */
	public void save(CrashLog crashLog);

}
