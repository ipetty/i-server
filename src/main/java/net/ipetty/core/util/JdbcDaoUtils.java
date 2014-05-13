package net.ipetty.core.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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

	/**
	 * 设置Long型参数
	 */
	public static void setLong(PreparedStatement statement, int parameterIndex, Long value) throws SQLException {
		if (value != null) {
			statement.setLong(parameterIndex, value);
		} else {
			statement.setNull(parameterIndex, Types.BIGINT);
		}
	}

	/**
	 * 设置Integer型参数
	 */
	public static void setInteger(PreparedStatement statement, int parameterIndex, Integer value) throws SQLException {
		if (value != null) {
			statement.setInt(parameterIndex, value);
		} else {
			statement.setNull(parameterIndex, Types.INTEGER);
		}
	}

}
