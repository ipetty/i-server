package net.ipetty.pet.domain;

import net.ipetty.core.domain.IntegerIdEntity;

/**
 * 宠物
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Pet extends IntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 2220351366844908107L;

	private int uid; // uid
	private String uniqueName; // 宠物在爱宠的唯一标识，一经设定不能更改
	private String name; // 名称
	private String gender; // 性别
	private int sortOrder; // 用户宠物的排序，从0开始

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
