package net.ipetty.user.domain;

import java.util.Date;

import net.ipetty.core.domain.AbstractEntity;

/**
 * UserRefreshToken
 * 
 * @author luocanfeng
 * @date 2014年5月29日
 */
public class UserRefreshToken extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -6270689818679234841L;

	private Integer userId;
	private String deviceScreenName;
	private String deviceId; // IMEI
	private String deviceMac;
	private String deviceUuid; // 客户端根据设备的多个唯一ID生成的UUID
	private String refreshToken;
	private Date createdOn;

	public UserRefreshToken() {
		super();
	}

	public UserRefreshToken(Integer userId, String deviceScreenName, String deviceId, String deviceMac,
			String deviceUuid, String refreshToken) {
		super();
		this.userId = userId;
		this.deviceScreenName = deviceScreenName;
		this.deviceId = deviceId;
		this.deviceMac = deviceMac;
		this.deviceUuid = deviceUuid;
		this.refreshToken = refreshToken;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDeviceScreenName() {
		return deviceScreenName;
	}

	public void setDeviceScreenName(String deviceScreenName) {
		this.deviceScreenName = deviceScreenName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
