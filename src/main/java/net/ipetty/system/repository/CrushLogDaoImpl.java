package net.ipetty.system.repository;

import net.ipetty.core.repository.BaseJdbcDaoSupport;

import org.springframework.stereotype.Repository;

/**
 * CrushLogDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
@Repository("crushLogDao")
public class CrushLogDaoImpl extends BaseJdbcDaoSupport implements CrushLogDao {

	private static final String SAVE_SQL = "insert into crush_log(log) values(?)";

	/**
	 * 保存崩溃日志
	 */
	public void save(String crushLog) {
		super.getJdbcTemplate().update(SAVE_SQL, crushLog);
	}

}
