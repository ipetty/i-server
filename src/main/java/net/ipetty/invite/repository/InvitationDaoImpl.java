package net.ipetty.invite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.invite.domain.Invitation;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * InvitationDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
@Repository("invitationDao")
public class InvitationDaoImpl extends BaseJdbcDaoSupport implements InvitationDao {

	static final RowMapper<Invitation> ROW_MAPPER = new RowMapper<Invitation>() {
		@Override
		public Invitation mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, invite_code, invite_type, inviter, inviter_id, invited,
			// invited_on, expired_datetime, expired, created_by, created_on
			Invitation invitation = new Invitation();
			invitation.setId(rs.getInt("id"));
			invitation.setInviteCode(rs.getString("invite_code"));
			invitation.setInviteType(rs.getString("invite_type"));
			invitation.setInviter(rs.getString("inviter"));
			invitation.setInviterId(JdbcDaoUtils.getInteger(rs, "inviter_id"));
			invitation.setInvited(rs.getBoolean("invited"));
			invitation.setInvitedOn(rs.getTimestamp("invited_on"));
			invitation.setExpiredDatetime(rs.getTimestamp("expired_datetime"));
			invitation.setExpired(rs.getBoolean("expired"));
			invitation.setCreatedBy(JdbcDaoUtils.getInteger(rs, "created_by"));
			invitation.setCreatedOn(rs.getTimestamp("created_on"));
			return invitation;
		}
	};

	private static final String SAVE_SQL = "insert into invitation(invite_code, invite_type, inviter, expired_datetime, created_by, created_on) values(?, ?, ?, ?, ?, ?)";

	/**
	 * 保存邀请
	 */
	@Override
	public void save(Invitation invitation) {
		invitation.setCreatedOn(new Date());
		invitation.setExpiredDatetime(DateUtils.addDays(invitation.getCreatedOn(), 3));
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, invitation.getInviteCode());
			statement.setString(2, invitation.getInviteType());
			statement.setString(3, invitation.getInviter());
			statement.setTimestamp(4, new Timestamp(invitation.getExpiredDatetime().getTime()));
			JdbcDaoUtils.setInteger(statement, 5, invitation.getCreatedBy());
			statement.setTimestamp(6, new Timestamp(invitation.getCreatedOn().getTime()));

			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				invitation.setId(rs.getInt(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", invitation);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_INVITE_CODE_SQL = "select * from invitation where invite_code=?";

	/**
	 * 根据邀请码获取邀请信息
	 */
	@Override
	public Invitation get(String inviteCode) {
		return super.queryUniqueEntity(GET_BY_INVITE_CODE_SQL, ROW_MAPPER, inviteCode);
	}

	private static final String UPDATE_SQL = "update invitation set inviter_id=?, invited=?, invited_on=?, expired=? where id=?";

	/**
	 * 更新邀请（受邀、过期）
	 */
	@Override
	public void update(Invitation invitation) {
		super.getJdbcTemplate().update(UPDATE_SQL, invitation.getInviterId(), invitation.isInvited(),
				invitation.getInvitedOn(), invitation.isExpired(), invitation.getId());
	}

	private static final String EXPIRE_INVITES_SQL = "update invitation set expired=true where expired=false and invited=false and expired_datetime<now()";

	/**
	 * 更新已过期的邀请
	 */
	@Override
	public void expireInvites() {
		super.getJdbcTemplate().update(EXPIRE_INVITES_SQL);
	}

}
