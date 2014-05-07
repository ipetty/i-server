package net.ipetty.user.repository;

import java.util.Collection;
import java.util.List;

/**
 * UidDao
 * 
 * @author luocanfeng
 * @date 2014年5月7日
 */
public interface UidDao {

	/**
	 * 获取所有可用的Uids。
	 */
	public List<Integer> listAllAvailable();

	/**
	 * 标记给定Uid为已使用。
	 */
	public void markAsUsed(int uid);

	/**
	 * 批量插入Uids，失败（可能已存在）忽略。
	 */
	public void batchInsert(Collection<Integer> uids);

	/**
	 * 获取系统中已存在的最大Uid
	 */
	public Integer getMaxUid();

}
