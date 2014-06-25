package net.ipetty.pet.domain;

import java.util.Date;

import net.ipetty.core.domain.IntegerIdEntity;
import net.ipetty.vo.PetVO;

import org.springframework.beans.BeanUtils;

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
	private String nickname; // 名称
	private String avatar; // 头像
	private String gender; // 性别
	private String family; // 家族
	private Date birthday; // 生日
	private String signature; // 个性签名
	private int sortOrder; // 用户宠物的排序，从0开始

	public Pet() {
		super();
	}

	public PetVO toVO() {
		PetVO vo = new PetVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public static Pet fromVO(PetVO vo) {
		Pet pet = new Pet();
		BeanUtils.copyProperties(vo, pet);
		return pet;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
