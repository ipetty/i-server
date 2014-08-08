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
	public static Integer getInteger(ResultSet rs, int columnIndex) throws SQLException {
		int columnValue = rs.getInt(columnIndex);
		return columnValue == 0 ? null : columnValue;
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

	/**
	 * 从ResultSet中获取Long对象的列值
	 */
	public static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
		long columnValue = rs.getLong(columnLabel);
		return columnValue == 0 ? null : columnValue;
	}

	/**
	 * 从ResultSet中获取Long对象的列值
	 */
	public static Long getLong(ResultSet rs, int columnIndex) throws SQLException {
		long columnValue = rs.getLong(columnIndex);
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
	 * 从ResultSet中获取Double对象的列值
	 */
	public static Double getDouble(ResultSet rs, String columnLabel) throws SQLException {
		double columnValue = rs.getDouble(columnLabel);
		return columnValue == 0 ? null : columnValue;
	}

	/**
	 * 从ResultSet中获取Double对象的列值
	 */
	public static Double getDouble(ResultSet rs, int columnIndex) throws SQLException {
		double columnValue = rs.getDouble(columnIndex);
		return columnValue == 0 ? null : columnValue;
	}

	/**
	 * 设置Double型参数
	 */
	public static void setDouble(PreparedStatement statement, int parameterIndex, Double value) throws SQLException {
		if (value != null) {
			statement.setDouble(parameterIndex, value);
		} else {
			statement.setNull(parameterIndex, Types.DOUBLE);
		}
	}

	/**
	 * 从ResultSet中获取Float对象的列值
	 */
	public static Float getFloat(ResultSet rs, String columnLabel) throws SQLException {
		float columnValue = rs.getFloat(columnLabel);
		return columnValue == 0 ? null : columnValue;
	}

	/**
	 * 从ResultSet中获取Float对象的列值
	 */
	public static Float getFloat(ResultSet rs, int columnIndex) throws SQLException {
		float columnValue = rs.getFloat(columnIndex);
		return columnValue == 0 ? null : columnValue;
	}

	/**
	 * 设置Float型参数
	 */
	public static void setFloat(PreparedStatement statement, int parameterIndex, Float value) throws SQLException {
		if (value != null) {
			statement.setFloat(parameterIndex, value);
		} else {
			statement.setNull(parameterIndex, Types.FLOAT);
		}
	}

}
