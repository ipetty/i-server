package net.ipetty.invite.service;

import java.util.Date;

import javax.annotation.Resource;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.core.service.BaseService;
import net.ipetty.core.util.UUIDUtils;
import net.ipetty.invite.domain.Invitation;
import net.ipetty.invite.repository.InvitationDao;
import net.ipetty.vo.ActivityType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * InvitationService
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
@Service
@Transactional
public class InvitationService extends BaseService {

	@Resource
	private InvitationDao invitationDao;

	/**
	 * 邀请
	 */
	@ProduceActivity(type = ActivityType.INVITE, createdBy = "${invitation.createdBy}", targetId = "${invitation.id}")
	public void invite(Invitation invitation) {
		Assert.notNull(invitation, "邀请不能为空");
		Assert.notNull(invitation.getCreatedBy(), "邀请人不能为空");
		Assert.hasText(invitation.getInviteType(), "邀请类型不能为空");

		invitation.setInviteCode(UUIDUtils.generateShortUUID());
		invitationDao.save(invitation);
	}

	/**
	 * 根据邀请码获取邀请信息
	 */
	public Invitation getByInviteCode(String inviteCode) {
		Assert.hasText(inviteCode, "邀请码不能为空");
		return invitationDao.get(inviteCode);
	}

	/**
	 * 接受邀请
	 */
	@ProduceActivity(type = ActivityType.ACCEPT_INVITE, createdBy = "${invitation.inviterId}", targetId = "${invitation.id}")
	public boolean acceptInvite(Invitation invitation) {
		Assert.notNull(invitation, "邀请不能为空");
		Assert.hasText(invitation.getInviteCode(), "邀请码不能为空");
		Assert.notNull(invitation.getInviterId(), "接受邀请人不能为空");
		Assert.notNull(invitation.getExpiredDatetime(), "邀请码有效期不能为空");

		invitation.setInvited(true);
		invitation.setInvitedOn(new Date());

		if (invitation.isExpired() || invitation.getInvitedOn().after(invitation.getExpiredDatetime())) { // 邀请码已过期
			invitationDao.expireInvites();
			return false;
		}

		invitationDao.update(invitation);
		invitationDao.expireInvites();
		return true;
	}

	/**
	 * 更新已过期的邀请
	 */
	public void expireInvites() {
		invitationDao.expireInvites();
	}

}
