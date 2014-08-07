package net.ipetty.system.domain;

import net.ipetty.core.domain.AbstractEntity;
import net.ipetty.vo.CrashLogVO;

import org.springframework.beans.BeanUtils;

/**
 * 崩溃日志
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrashLog extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -4123629740281400212L;

	private Integer userId; // 用户ID
	private String userName; // 用户昵称
	private String androidVersion; // Android系统版本
	private Integer appVersionCode; // 应用版本
	private String appVersionName; // 应用版本
	private String crashType; // 日志类型，crash/error
	private String log; // 日志内容

	public CrashLog() {
		super();
	}

	public CrashLog(String log) {
		super();
		this.log = log;
	}

	public CrashLog(Integer userId, String userName, String androidVersion, int appVersionCode, String appVersionName,
			String crashType, String log) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.androidVersion = androidVersion;
		this.appVersionCode = appVersionCode;
		this.appVersionName = appVersionName;
		this.crashType = crashType;
		this.log = log;
	}

	public static CrashLog fromVO(CrashLogVO vo) {
		CrashLog crashLog = new CrashLog();
		BeanUtils.copyProperties(vo, crashLog);
		return crashLog;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public Integer getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(Integer appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getCrashType() {
		return crashType;
	}

	public void setCrashType(String crashType) {
		this.crashType = crashType;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
