package net.ipetty.core.context;

/**
 * 上下文中的用户主体
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
public class UserPrincipal {

	private Integer id;
	private int uid; // uid
	private String account; // 爱宠号
	private String nickname; // 昵称

	public UserPrincipal() {
	}

	public UserPrincipal(Integer id) {
		this.id = id;
	}

	public UserPrincipal(Integer id, int uid, String account, String nickname) {
		this.id = id;
		this.uid = uid;
		this.account = account;
		this.nickname = nickname;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
