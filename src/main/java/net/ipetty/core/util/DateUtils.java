package net.ipetty.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * DateUtils
 * 
 * @author luocanfeng
 * @date 2014年5月13日
 */
public class DateUtils {

	private static final String DATE_FORMAT_STRING = "yyyy-MM-dd";
	private static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

	/** 由于DateFormat不是线程安全的，存在性能问题，故设计为ThreadLocal */
	private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat(DATE_FORMAT_STRING);
		}
	};

	/** 由于DateFormat不是线程安全的，存在性能问题，故设计为ThreadLocal */
	private static ThreadLocal<DateFormat> datetimeFormat = new ThreadLocal<DateFormat>() {
		protected synchronized DateFormat initialValue() {
			return new SimpleDateFormat(DATETIME_FORMAT_STRING);
		}
	};

	private static DateFormat getDateFormat() {
		return dateFormat.get();
	}

	private static DateFormat getDatetimeFormat() {
		return datetimeFormat.get();
	}

	/**
	 * 转换日期对象为yyyy-MM-dd格式的字符串
	 */
	public static String toDateString(Date date) {
		return date == null ? StringUtils.EMPTY : getDateFormat().format(date);
	}

	/**
	 * 转换日期对象为yyyy-MM-dd hh:mm:ss格式的字符串
	 */
	public static String toDatetimeString(Date datetime) {
		return datetime == null ? StringUtils.EMPTY : getDatetimeFormat().format(datetime);
	}

	/**
	 * 将yyyy-MM-dd格式的字符串转换为日期对象
	 */
	public static Date fromDateString(String dateString) {
		try {
			return StringUtils.isBlank(dateString) ? null : getDateFormat().parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将yyyy-MM-dd hh:mm:ss格式的字符串转换为日期时间对象
	 */
	public static Date fromDatetimeString(String datetimeString) {
		try {
			return StringUtils.isBlank(datetimeString) ? null : getDatetimeFormat().parse(datetimeString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/** 获取当前时间戳并转换成yyyy-MM-dd hh:mm:ss格式的日期时间字符串 */
	public static String getCurrentDatetime() {
		return DateUtils.toDatetimeString(new Date());
	}

	/** 获取当前时间戳并转换成yyyy-MM-dd格式的日期字符串 */
	public static String getCurrentDate() {
		return DateUtils.toDateString(new Date());
	}

	public static void main(String[] args) throws Exception {
		Date date = new Date();
		System.out.println("toDateString: " + DateUtils.toDateString(date));
		System.out.println("toDatetimeString: " + DateUtils.toDatetimeString(date));
		System.out.println("getCurrentDatetime: " + DateUtils.getCurrentDatetime());
		System.out.println("getCurrentDate: " + DateUtils.getCurrentDate());
	}

}
