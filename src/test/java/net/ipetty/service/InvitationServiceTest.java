package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.invite.domain.Invitation;
import net.ipetty.invite.domain.InvitationType;
import net.ipetty.invite.service.InvitationService;
import net.ipetty.user.domain.User;
import net.ipetty.user.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * InvitationServiceTest
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public class InvitationServiceTest extends BaseTest {

	@Resource
	private InvitationService invitationService;

	@Resource
	private UserService userService;

	private static final String TEST_ACCOUNT_UNIQUE_NAME = "luocanfeng";

	@Test
	public void testInvite() {
		Invitation invite = new Invitation(InvitationType.INVITE_USER, "test", userService.getByUniqueName(
				TEST_ACCOUNT_UNIQUE_NAME).getId());
		invitationService.invite(invite);
		Assert.assertNotNull(invite.getId());
	}

	@Test
	public void testAcceptInvite() {
		Invitation invitation = new Invitation(InvitationType.INVITE_USER, "test", userService.getByUniqueName(
				TEST_ACCOUNT_UNIQUE_NAME).getId());
		invitationService.invite(invitation);
		Assert.assertNotNull(invitation.getId());
		Assert.assertTrue(StringUtils.isNotBlank(invitation.getInviteCode()));

		invitation = invitationService.getByInviteCode(invitation.getInviteCode());

		User user = new User();
		String email = "testAcceptInvite@ipetty.net";
		user.setEmail(email);
		user.setPassword("888888");
		userService.register(user);

		invitation.setInviterId(user.getId());
		Assert.assertTrue(invitationService.acceptInvite(invitation));
	}

}
