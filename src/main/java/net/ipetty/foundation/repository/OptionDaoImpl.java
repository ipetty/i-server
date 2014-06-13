package net.ipetty.foundation.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.ipetty.core.repository.BaseJdbcDaoSupport;
import net.ipetty.vo.Option;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * OptionDaoImpl
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
@Repository("optionDao")
public class OptionDaoImpl extends BaseJdbcDaoSupport implements OptionDao {

	static final RowMapper<Option> ROW_MAPPER = new RowMapper<Option>() {
		@Override
		public Option mapRow(ResultSet rs, int rowNum) throws SQLException {
			// option_group, value, label, icon, tips, idx
			Option option = new Option();
			option.setGroup(rs.getString("option_group"));
			option.setValue(rs.getString("value"));
			option.setLabel(rs.getString("label"));
			option.setIcon(rs.getString("icon"));
			option.setTips(rs.getString("tips"));
			return option;
		}
	};

	private static final String LIST_BY_GROUP_SQL = "select * from options where option_group=? order by idx asc";

	/**
	 * 获取指定选项组的所有选项
	 */
	public List<Option> listByGroup(String group) {
		return super.getJdbcTemplate().query(LIST_BY_GROUP_SQL, ROW_MAPPER, group);
	}

}
