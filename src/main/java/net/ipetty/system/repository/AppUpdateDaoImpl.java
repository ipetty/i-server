package net.ipetty.system.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.system.domain.AppUpdate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * AppUpdateDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
@Repository("appUpdateDao")
public class AppUpdateDaoImpl extends BaseJdbcDaoSupport implements AppUpdateDao {

	static final RowMapper<AppUpdate> ROW_MAPPER = new RowMapper<AppUpdate>() {
		@Override
		public AppUpdate mapRow(ResultSet rs, int rowNum) throws SQLException {
			// app_name, app_key, app_secret, version_code, version_name,
			// version_description, download_url
			AppUpdate appUpdate = new AppUpdate();
			appUpdate.setAppName(rs.getString("app_name"));
			appUpdate.setAppKey(rs.getString("app_key"));
			appUpdate.setAppSecret(rs.getString("app_secret"));
			appUpdate.setVersionCode(rs.getInt("version_code"));
			appUpdate.setVersionName(rs.getString("version_name"));
			appUpdate.setVersionDescription(rs.getString("version_description"));
			appUpdate.setDownloadUrl(rs.getString("download_url"));
			return appUpdate;
		}
	};

	private static final String SAVE_SQL = "insert into app_update(app_name, app_key, app_secret, version_code, version_name, version_description, download_url) values(?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 新增应用版本
	 */
	@Override
	public void save(AppUpdate appUpdate) {
		super.getJdbcTemplate().update(SAVE_SQL, appUpdate.getAppName(), appUpdate.getAppKey(),
				appUpdate.getAppSecret(), appUpdate.getVersionCode(), appUpdate.getVersionName(),
				appUpdate.getVersionDescription(), appUpdate.getDownloadUrl());
	}

	private static final String GET_BY_APP_KEY_AND_VERSION_CODE = "select * from app_update where app_key=? and version_code=? order by version_code desc limit 1";

	/**
	 * 获取指定应用的最新版本
	 */
	@Override
	public AppUpdate getByAppKeyAndVersionCode(String appKey, Integer versionCode) {
		return super.queryUniqueEntity(GET_BY_APP_KEY_AND_VERSION_CODE, ROW_MAPPER, appKey, versionCode);
	}

	private static final String GET_LATEST_SQL = "select * from app_update where app_key=? order by version_code desc limit 1";

	/**
	 * 获取指定应用的最新版本
	 */
	@Override
	public AppUpdate getLatest(String appKey) {
		return super.queryUniqueEntity(GET_LATEST_SQL, ROW_MAPPER, appKey);
	}

}
