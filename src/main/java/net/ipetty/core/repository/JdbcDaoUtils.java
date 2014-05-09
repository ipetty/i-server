package net.ipetty.core.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JdbcDaoUtils
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class JdbcDaoUtils {

	/**
	 * 从ResultSet中获取Integer对象的列值
	 */
	public static Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
		int columnValue = rs.getInt(columnLabel);
		return columnValue == 0 ? null : columnValue;
	}

	/**
	 * 从ResultSet中获取Integer对象的列值
	 */
	public static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
		long columnValue = rs.getInt(columnLabel);
		return columnValue == 0 ? null : columnValue;
	}

}
