package net.ipetty.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.feed.domain.Image;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
			// id, created_by, created_on, small_url, cut_url, original_url
			Image image = new Image();
			image.setId(rs.getLong("id"));
			image.setCreatedBy(rs.getInt("created_by"));
			image.setCreatedOn(rs.getDate("created_on"));
			image.setSmallURL(rs.getString("small_url"));
			image.setCutURL(rs.getString("cut_url"));
			image.setOriginalURL(rs.getString("original_url"));
			return image;
		}
	};

	private static final String INSERT_SQL = "insert into image(created_by, small_url, cut_url, original_url) values(?, ?, ?, ?)";

	/**
	 * 保存图片信息
	 */
	@Override
	public void save(Image image) {
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
			if (image.getCreatedBy() != null) { // 图片必须要有创建人
				statement.setInt(1, image.getCreatedBy());
			}
			statement.setString(2, image.getSmallURL());
			statement.setString(3, image.getCutURL());
			statement.setString(4, image.getOriginalURL());
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

	private static final String GET_BY_ID_SQL = "select * from image where id=?";

	/**
	 * 根据ID获取图片信息
	 */
	@Override
	public Image getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

}
