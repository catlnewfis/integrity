package com.catlbattery.alm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {

	private static Log log = LogFactory.getLog(DateUtil.class);

	/***
	 * MMM d, YYYY
	 * 
	 * @param date
	 * @return
	 */
	public static String formatMediumEn(Date date) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
		return df.format(date);
	}

	/***
	 * 美式时间 Oct 5, 2016 10:31:21 AM
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTimeEn(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US);
		return df.format(date);
	}

	/**
	 * MMM d, YYYY
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseDateEn(String str) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			log.error("parseDateEn:" + e.getMessage());
		}
		return date;
	}

	/***
	 * YYYYMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatShort(Date date) {
		return format(date, "YYYYMMdd");
	}

	/***
	 * YYYY-M-d
	 * 
	 * @param date
	 * @return
	 */
	public static String formatCN(Date date) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINESE);
		return df.format(date);
	}

	/***
	 * YYYY年M月d日
	 * 
	 * @param date
	 * @return
	 */
	public static String formatLongCN(Date date) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINESE);
		return df.format(date);
	}

	/***
	 * customized pattern
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * date + days = new Date
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * to - from = days
	 * 
	 * @param from
	 * @param to
	 * @return days
	 */
	public static long daysBetween(Date from, Date to) {
		Calendar fc = Calendar.getInstance();
		fc.setTime(from);
		fc.set(Calendar.HOUR_OF_DAY, 0);
		fc.set(Calendar.MINUTE, 0);
		fc.set(Calendar.SECOND, 0);
		fc.set(Calendar.MILLISECOND, 0);

		Calendar tc = Calendar.getInstance();
		tc.setTime(to);
		tc.set(Calendar.HOUR_OF_DAY, 0);
		tc.set(Calendar.MINUTE, 0);
		tc.set(Calendar.SECOND, 0);
		tc.set(Calendar.MILLISECOND, 0);

		long days = (tc.getTimeInMillis() - fc.getTimeInMillis()) / (1000 * 60 * 60 * 24);
		return days;
	}

	/**
	 * months
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static int monthsBetween(Date from, Date to) {
		Calendar fc = Calendar.getInstance();
		fc.setTime(from);
		Calendar tc = Calendar.getInstance();
		tc.setTime(to);

		int m = (tc.get(Calendar.YEAR) - fc.get(Calendar.YEAR)) * 12 + tc.get(Calendar.MONTH) - fc.get(Calendar.MONTH);
		return m;
	}

	public static int weekday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static Date nextWeekend(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date preWeekend(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -7);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static List<Date> weekendsBetween(Date from, Date to) {
		Calendar fc = Calendar.getInstance();
		fc.setTime(from);
		Calendar tc = Calendar.getInstance();
		tc.setTime(to);

		List<Date> dates = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.setTime(nextWeekend(from));
		while (tc.compareTo(c) > -1) {
			dates.add(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, 7);
		}

		return dates;
	}

	public static Date getSundayOfThisWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week);
		return c.getTime();
	}

	/**
	 * 获取本周每天的日期
	 * 
	 * @param date
	 * @return
	 */
	public static List<Date> getDateBetweenAWeek(Date date) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		Calendar fc = Calendar.getInstance();
		fc.setTime(date);
		fc.add(Calendar.DAY_OF_WEEK, 6);
		Date enDate = fc.getTime();
		List<Date> dates = new ArrayList<>();
		dates.add(date);
		Date betweenDate = null;
		do {
			start.add(Calendar.DAY_OF_YEAR, 1);
			betweenDate = start.getTime();
			dates.add(betweenDate);
		} while (betweenDate.before(enDate));
		return dates;
	}

}
