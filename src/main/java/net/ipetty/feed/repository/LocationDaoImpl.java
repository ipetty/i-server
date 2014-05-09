package net.ipetty.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.feed.domain.Location;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * LocationDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
@Repository("locationDao")
public class LocationDaoImpl extends BaseJdbcDaoSupport implements LocationDao {

	static final RowMapper<Location> ROW_MAPPER = new RowMapper<Location>() {
		@Override
		public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, longitude, latitude, geoHash, address
			Location location = new Location();
			location.setId(rs.getLong("id"));
			location.setLongitude(rs.getLong("longitude"));
			location.setLatitude(rs.getLong("latitude"));
			location.setGeoHash(rs.getString("geoHash"));
			location.setAddress(rs.getString("address"));
			return location;
		}
	};

	private static final String INSERT_SQL = "insert into location(longitude, latitude, geoHash, address) values(?, ?, ?, ?)";

	/**
	 * 保存位置信息
	 */
	@Override
	public void save(Location location) {
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, location.getLongitude());
			statement.setLong(2, location.getLatitude());
			statement.setString(3, location.getGeoHash());
			statement.setString(4, location.getAddress());
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				location.setId(rs.getLong(1));
			}
			rs.close();
			statement.close();
			logger.debug("saved {}", location);
		} catch (SQLException e) {
			throw new BusinessException("Database exception", e);
		}
	}

	private static final String GET_BY_ID_SQL = "select * from location where id=?";

	/**
	 * 根据ID获取位置信息
	 */
	@Override
	public Location getById(Long id) {
		return super.queryUniqueEntity(GET_BY_ID_SQL, ROW_MAPPER, id);
	}

}
