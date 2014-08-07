package net.ipetty.system.repository;

import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.system.domain.CrashLog;

import org.springframework.stereotype.Repository;

/**
 * CrashLogDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
@Repository("crashLogDao")
public class CrashLogDaoImpl extends BaseJdbcDaoSupport implements CrashLogDao {

	// user_id, user_name, android_version, app_version_code, app_version_name,
	// crash_type, log
	private static final String SAVE_SQL = "insert into crash_log(user_id,user_name,android_version,app_version_code,app_version_name,crash_type,log) values(?,?,?,?,?,?,?)";

	/**
	 * 保存崩溃日志
	 */
	public void save(CrashLog crashLog) {
		super.getJdbcTemplate().update(SAVE_SQL, crashLog.getUserId(), crashLog.getUserName(),
				crashLog.getAndroidVersion(), crashLog.getAppVersionCode(), crashLog.getAppVersionName(),
				crashLog.getCrashType(), crashLog.getLog());
	}

}
