package net.ipetty.invite.domain;

import java.util.Date;

import net.ipetty.core.domain.IntegerIdEntity;

/**
 * 邀请
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Invitation extends IntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -7474877086078373887L;

	private String inviteCode; // 随机生成的邀请码
	private String inviteType; // 邀请类型
	private String inviter; // 邀请时的邀请人信息，如邮箱地址、QQ等
	private Integer inviterId; // 实际接受邀请人的ID
	private boolean invited; // 是否已受邀
	private Date invitedOn; // 受邀日期
	private Date expiredDatetime; // 有效期
	private boolean expired; // 是否已过期

	public Invitation() {
		super();
	}

	public Invitation(String inviteType, String inviter, Integer createdBy) {
		super();
		this.inviteType = inviteType;
		this.inviter = inviter;
		super.setCreatedBy(createdBy);
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getInviteType() {
		return inviteType;
	}

	public void setInviteType(String inviteType) {
		this.inviteType = inviteType;
	}

	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public Integer getInviterId() {
		return inviterId;
	}

	public void setInviterId(Integer inviterId) {
		this.inviterId = inviterId;
	}

	public boolean isInvited() {
		return invited;
	}

	public void setInvited(boolean invited) {
		this.invited = invited;
	}

	public Date getInvitedOn() {
		return invitedOn;
	}

	public void setInvitedOn(Date invitedOn) {
		this.invitedOn = invitedOn;
	}

	public Date getExpiredDatetime() {
		return expiredDatetime;
	}

	public void setExpiredDatetime(Date expiredDatetime) {
		this.expiredDatetime = expiredDatetime;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

}
