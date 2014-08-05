package net.ipetty.system.repository;

/**
 * CrushLogDao
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public interface CrushLogDao {

	/**
	 * 保存崩溃日志
	 */
	public void save(String crushLog);

}
