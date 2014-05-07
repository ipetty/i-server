package net.ipetty.user.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.ipetty.core.repository.BaseJdbcDaoSupport;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

/**
 * UidDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月7日
 */
@Repository("uidDao")
public class UidDaoImpl extends BaseJdbcDaoSupport implements UidDao {

	private static final String LIST_ALL_AVAILABLE_SQL = "select uid from sys_uid_pool where enable=true order by uid asc";

	/**
	 * 获取所有可用的Uids。
	 */
	@Override
	public List<Integer> listAllAvailable() {
		return super.getJdbcTemplate().queryForList(LIST_ALL_AVAILABLE_SQL, Integer.class);
	}

	private static final String MARK_AS_USED_SQL = "update sys_uid_pool set enable=false where uid=?";

	/**
	 * 标记给定Uid为已使用。
	 */
	@Override
	public void markAsUsed(int uid) {
		super.getJdbcTemplate().update(MARK_AS_USED_SQL, uid);
	}

	private static final String INSERT_SQL = "insert into sys_uid_pool(uid, enable) values(?, true)";

	/**
	 * 批量插入Uids，失败（可能已存在）忽略。
	 */
	@Override
	public void batchInsert(Collection<Integer> uids) {
		List<Object[]> batchArgs = new ArrayList<Object[]>(uids.size());
		for (Integer uid : uids) {
			batchArgs.add(new Object[] { uid });
		}
		super.getJdbcTemplate().batchUpdate(INSERT_SQL, batchArgs);
	}

	private static final String GET_MAX_UID_SQL = "select max(uid) from sys_uid_pool";

	/**
	 * 获取系统中已存在的最大Uid
	 */
	@Override
	public Integer getMaxUid() {
		try {
			return super.getJdbcTemplate().queryForObject(GET_MAX_UID_SQL, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
