package net.ipetty.user.domain;

import net.ipetty.core.domain.AbstractEntity;
import net.ipetty.user.category.Gender;

/**
 * 用户个人信息
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class UserProfile extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -8765934166348055267L;

	private Integer userId; // 用户ID
	private String nickname; // 昵称
	private String avatar; // 头像
	private String background; // 背景图片
	private Gender gender; // 性别
	private String stateAndRegion; // 地区
	private String signature; // 个性签名

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getStateAndRegion() {
		return stateAndRegion;
	}

	public void setStateAndRegion(String stateAndRegion) {
		this.stateAndRegion = stateAndRegion;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
