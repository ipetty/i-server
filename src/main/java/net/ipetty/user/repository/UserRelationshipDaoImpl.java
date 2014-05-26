package net.ipetty.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromHazelcast;
import net.ipetty.core.cache.annotation.UpdateToHazelcast;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.user.domain.UserRelationship;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * UserRelationshipDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月14日
 */
@Repository("userRelationshipDao")
public class UserRelationshipDaoImpl extends BaseJdbcDaoSupport implements UserRelationshipDao {

	static final RowMapper<UserRelationship> ROW_MAPPER = new RowMapper<UserRelationship>() {
		@Override
		public UserRelationship mapRow(ResultSet rs, int rowNum) throws SQLException {
			// friend_id, follower_id, followed_on
			UserRelationship relationship = new UserRelationship();
			relationship.setFriendId(JdbcDaoUtils.getInteger(rs, "friend_id"));
			relationship.setFollowerId(JdbcDaoUtils.getInteger(rs, "follower_id"));
			relationship.setFollowedOn(rs.getDate("followed_on"));
			return relationship;
		}
	};

	private static final String INSERT_SQL = "insert into user_relationship(friend_id, follower_id) values(?, ?)";

	/**
	 * 关注
	 */
	@Override
	public void follow(Integer friendId, Integer followerId) {
		super.getJdbcTemplate().update(INSERT_SQL, friendId, followerId);
	}

	private static final String RETRIEVE_SQL = "select * from user_relationship where friend_id=? and follower_id=?";

	/**
	 * 获取关注信息
	 */
	@Override
	@LoadFromHazelcast(mapName = CacheConstants.CACHE_USER_RELATIONSHIP_ID_TO_RELATIONSHIP, key = "${friendId}_${followerId}")
	public UserRelationship get(Integer friendId, Integer followerId) {
		return super.queryUniqueEntity(RETRIEVE_SQL, ROW_MAPPER, friendId, followerId);
	}

	private static final String DELETE_SQL = "delete from user_relationship where friend_id=? and follower_id=?";

	/**
	 * 取消关注
	 */
	@Override
	@UpdateToHazelcast(mapName = CacheConstants.CACHE_USER_RELATIONSHIP_ID_TO_RELATIONSHIP, key = "${friendId}_${followerId}")
	public void unfollow(Integer friendId, Integer followerId) {
		super.getJdbcTemplate().update(DELETE_SQL, friendId, followerId);
	}

	private static final String LIST_FRIENDS_SQL = "select friend_id from user_relationship where follower_id=? order by followed_on desc limit ?,?";

	/**
	 * 分页获取关注列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<Integer> listFriends(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_FRIENDS_SQL, INTEGER_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

	private static final String LIST_FOLLOWERS_SQL = "select follower_id from user_relationship where friend_id=? order by followed_on desc limit ?,?";

	/**
	 * 获取粉丝列表
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<Integer> listFollowers(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_FOLLOWERS_SQL, INTEGER_ROW_MAPPER, userId, pageNumber * pageSize,
				pageSize);
	}

	private static final String LIST_BI_FRIENDS_SQL = "select id from (select friend_id as id, followed_on from user_relationship where follower_id=?"
			+ " union all select follower_id as id, followed_on from user_relationship where friend_id=?) as r"
			+ " group by id having count(id)=2 order by followed_on desc limit ?,?";

	/**
	 * 获取好友列表（双向关注）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<Integer> listBiFriends(Integer userId, int pageNumber, int pageSize) {
		return super.getJdbcTemplate().query(LIST_BI_FRIENDS_SQL, INTEGER_ROW_MAPPER, userId, userId,
				pageNumber * pageSize, pageSize);
	}

}
