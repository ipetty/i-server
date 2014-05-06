package net.ipetty.core.repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
