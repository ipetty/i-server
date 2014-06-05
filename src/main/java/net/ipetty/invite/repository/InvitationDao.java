package net.ipetty.invite.repository;

import net.ipetty.invite.domain.Invitation;

/**
 * InvitationDao
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public interface InvitationDao {

	/**
	 * 保存邀请
	 */
	public void save(Invitation invitation);

	/**
	 * 根据邀请码获取邀请信息
	 */
	public Invitation get(String inviteCode);

	/**
	 * 更新邀请（受邀、过期）
	 */
	public void update(Invitation invitation);

	/**
	 * 更新已过期的邀请
	 */
	public void expireInvites();

}
