package net.ipetty.pet.domain;

import java.util.Date;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 宠物
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Pet extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 2220351366844908107L;

	private Integer id;
	private Date createdOn; // 创建时间

	private Integer userId; // 主人ID
	private int uid; // uid
	private String uniqueName; // 宠物在爱宠的唯一标识，一经设定不能更改
	private String name; // 名称
	private String gender; // 性别
	private int sortOrder; // 用户宠物的排序，从0开始

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
