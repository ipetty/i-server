package net.ipetty.user.domain;

/**
 * 匿名用户
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class AnonymousUser extends User {

	/** serialVersionUID */
	private static final long serialVersionUID = 7207635663223042069L;

	private boolean anonymous = true;

	private AnonymousUser() {
		super();
		super.getProfile().setNickname("匿名用户");
		// TODO
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

}
