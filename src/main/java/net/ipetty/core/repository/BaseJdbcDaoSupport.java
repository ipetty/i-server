package net.ipetty.core.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.ipetty.core.util.JdbcDaoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * JdbcDaoSupport基类，注入JdbcTemplate
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public abstract class BaseJdbcDaoSupport extends JdbcDaoSupport {

	protected static final RowMapper<Integer> INTEGER_ROW_MAPPER = new RowMapper<Integer>() {
		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return JdbcDaoUtils.getInteger(rs, 1);
		}
	};

	protected static final RowMapper<Long> LONG_ROW_MAPPER = new RowMapper<Long>() {
		@Override
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			return JdbcDaoUtils.getLong(rs, 1);
		}
	};

	protected static final RowMapper<String> STRING_ROW_MAPPER = new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	};

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void initJdcbTemplate() {
		super.setJdbcTemplate(jdbcTemplate);
	}

	/**
	 * 查找唯一对象实例，如果没有记录则返回null
	 */
	protected <T> T queryUniqueEntity(String sql, RowMapper<T> rowMapper, Object... args) {
		try {
			return super.getJdbcTemplate().queryForObject(sql, rowMapper, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 查找唯一对象实例，如果没有记录则返回null
	 */
	protected <T> T queryUniqueEntity(String sql, RowMapper<T> rowMapper) {
		try {
			return super.getJdbcTemplate().queryForObject(sql, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
