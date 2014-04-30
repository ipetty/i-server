package net.ipetty.core.repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * JdbcDaoSupport基类，注入JdbcTemplate
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public abstract class BaseJdbcDaoSupport extends JdbcDaoSupport {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void initJdcbTemplate() {
		super.setJdbcTemplate(jdbcTemplate);
	}

}
