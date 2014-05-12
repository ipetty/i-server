package net.ipetty.feed.domain;

import net.ipetty.core.domain.AbstractEntity;
import net.ipetty.vo.LocationFormVO;
import net.ipetty.vo.LocationVO;

import org.springframework.beans.BeanUtils;

/**
 * 位置
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public class Location extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 502817242515711348L;

	private Long id;

	private Long longitude; // 经度
	private Long latitude; // 纬度
	private String geoHash; // GEO哈希值
	private String address; // 地点文本信息

	public Location() {
		super();
	}

	public Location(Long longitude, Long latitude, String geoHash, String address) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.geoHash = geoHash;
		this.address = address;
	}

	public Location(Long id, Long longitude, Long latitude, String geoHash, String address) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.geoHash = geoHash;
		this.address = address;
	}

	public LocationVO toVO() {
		LocationVO vo = new LocationVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Location fromVO(LocationFormVO vo) {
		Location location = new Location();
		BeanUtils.copyProperties(vo, location);
		return location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public String getGeoHash() {
		return geoHash;
	}

	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
