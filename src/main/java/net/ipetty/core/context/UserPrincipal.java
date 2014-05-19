package net.ipetty.core.context;

import net.ipetty.user.domain.User;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 上下文中的用户主体
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
public class UserPrincipal {

	private Integer id;
	private int uid; // uid
	private String uniqueName; // 爱宠号
	private String nickname; // 昵称
	private String token; // 用户身份token标识，在用户登录后产生，并在服务器端缓存

	public UserPrincipal() {
	}

	public UserPrincipal(Integer id) {
		this.id = id;
	}

	public UserPrincipal(Integer id, int uid, String uniqueName, String nickname) {
		this.id = id;
		this.uid = uid;
		this.uniqueName = uniqueName;
		this.nickname = nickname;
	}

	public static UserPrincipal fromUser(User user, String userToken) {
		UserPrincipal principal = new UserPrincipal();
		principal.setId(user.getId());
		principal.setUid(user.getUid());
		principal.setUniqueName(user.getUniqueName());
		principal.setNickname(user.getProfile() == null ? null : user.getProfile().getNickname());
		principal.setToken(userToken);
		return principal;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
