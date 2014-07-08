package net.ipetty.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.ipetty.core.cache.CacheConstants;
import net.ipetty.core.cache.annotation.LoadFromCache;
import net.ipetty.core.cache.annotation.UpdateToCache;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.core.util.JdbcDaoUtils;
import net.ipetty.feed.domain.Image;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * ImageDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Repository("imageDao")
public class ImageDaoImpl extends BaseJdbcDaoSupport implements ImageDao {

	static final RowMapper<Image> ROW_MAPPER = new RowMapper<Image>() {
		@Override
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, created_by, created_on, small_url, cut_url, original_url,
			// deleted
			Image image = new Image();
			image.setId(rs.getLong("id"));
			image.setCreatedBy(rs.getInt("created_by"));
			image.setCreatedOn(rs.getTimestamp("created_on"));
			image.setSmallURL(rs.getString("small_url"));
			image.setCutURL(rs.getString("cut_url"));
			image.setOriginalURL(rs.getString("original_url"));
			image.setDeleted(rs.getBoolean("deleted"));
			return image;
		}
	};

	private static final String SAVE_SQL = "insert into image(created_by, small_url, cut_url, original_url, created_on) values(?, ?, ?, ?, ?)";

	/**
	 * 保存图片信息
	 */
	@Override
	public void save(Image image) {
		image.setCreatedOn(new Date());
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
			// 图片必须要有创建人
			JdbcDaoUtils.setInteger(statement, 1, image.getCreatedBy());
			statement.setString(2, image.getSmallURL());
			statement.setString(3, image.getCutURL());
			statement.setString(4, image.getOriginalURL());
			statement.setTimestamp(5, new Timestamp(image.getCreatedOn().getTime()));
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				image.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", image);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from image where id=? and deleted=false";

	/**
	 * 根据ID获取图片信息
	 */
	@Override
	@LoadFromCache(mapName = CacheConstants.CACHE_IMAGE_ID_TO_IMAGE, key = "${id}")
	public Image getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

	private static final String DELETE_SQL = "update image set deleted=true where id=?";

	/**
	 * 删除图片
	 */
	@Override
	@UpdateToCache(mapName = CacheConstants.CACHE_IMAGE_ID_TO_IMAGE, key = "${id}")
	public void delete(Long id) {
		super.getJdbcTemplate().update(DELETE_SQL, id);
	}

	private static final String LIST_BY_FEED_IDS_SQL = "select i.* from image i inner join (select image_id from feed where id in (?)) iid on i.id=iid.image_id";

	/**
	 * 获取指定主题消息列表的所有图片信息
	 */
	@Override
	public List<Image> listByFeedIds(Long... feedIds) {
		String inStatement = feedIds.length > 0 ? StringUtils.arrayToCommaDelimitedString(feedIds) : "null";
		return super.getJdbcTemplate().query(LIST_BY_FEED_IDS_SQL.replace("?", inStatement), ROW_MAPPER);
	}

}
