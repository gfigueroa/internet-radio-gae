/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Utility class for converting and formatting time and date values.
 * 
 */

public class DateManager {

	public static final TimeZone timeZone = TimeZone.getTimeZone("Asia/Taipei");
	
	/**
	 * Converts the given simple date String to a Date.
	 * From: MM.DD.YYYY (e.g. "02.29.2012")
	 * @param dateString
	 * 			: the String representation of the Date in MM.DD.YYYY format
	 * @return a Date instance
	 */
	public static Date getSimpleDateValue(String date){
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		
		try {
			// Parse date
			String[] dateTokens = date.split("[.]");
			int month = Integer.valueOf(dateTokens[0]) - 1;
			int day = Integer.valueOf(dateTokens[1]);
			int year = Integer.valueOf(dateTokens[2]);

			int hours = 0;
			int minutes = 0;
			int seconds = 1;
	
			calendar.set(year, month, day, hours, minutes, seconds);
	
			return calendar.getTime();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Converts the given String to a Date.
	 * From: 2/29/2012 17:49:03
	 * @param dateString
	 * 			: the String representation of the Date
	 * @return a Date instance
	 */
	public static Date getDateValue(String date){
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		
		try {
			String[] tokens = date.split(" ");
			
			// Parse date
			String[] dateTokens = tokens[0].split("/");
			int month = Integer.valueOf(dateTokens[0]) - 1;
			int day = Integer.valueOf(dateTokens[1]);
			int year = Integer.valueOf(dateTokens[2]);
			
			// Parse time
			String[] timeTokens = tokens[1].split(":");
			int hours = Integer.valueOf(timeTokens[0]);
			int minutes = Integer.valueOf(timeTokens[1]);
			int seconds = 0;
	
			calendar.set(year, month, day, hours, minutes, seconds);
	
			return calendar.getTime();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Converts the given hours and minutes to a Date
	 * 
	 * @param hours
	 *            : the hours for this time
	 * @param minutes
	 * 			  : the minutes for this time
	 * @return a Date instance
	 */
	public static Date getDateValue(int hours, int minutes) {
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.set(GregorianCalendar.HOUR_OF_DAY, hours);
		calendar.set(GregorianCalendar.MINUTE, minutes);

		return calendar.getTime();
	}	

	/**
	 * Prints the given Date as a string (MM/dd/yyyy HH:mm)
	 * 
	 * @param date
	 *            : the date to print
	 * @return a String representation of the given Date
	 */
	public static String printDateAsString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		simpleDateFormat.setTimeZone(timeZone);
		
		return simpleDateFormat.format(date);
	}
	
	/**
	 * Prints the given Date as time (hh:mm)
	 * 
	 * @param date
	 *            : the date to print
	 * @return a String representation of the given Date as a time format
	 */
	public static String printDateAsTime(Date date) {
		if (date == null) {
			return "";
		}
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		timeFormat.setTimeZone(timeZone);
		
		return timeFormat.format(date);
	}
	
	/**
	 * Prints the given Date as time in 24-hour format (HH:mm)
	 * 
	 * @param date
	 *            : the date to print
	 * @return a String representation of the given Date as a 24-hour time format
	 */
	public static String printDateAsTime24(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		simpleDateFormat.setTimeZone(timeZone);
		
		return simpleDateFormat.format(date);
	}
	
	/**
	 * Returns the hours for this Date
	 * 
	 * @param date
	 *            : the date
	 * @return the hours corresponding to this date
	 */
	public static int getHours(Date date) {
		if (date == null) {
			return 0;
		}
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(date);
		
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * Returns the minutes for this Date
	 * 
	 * @param date
	 *            : the date
	 * @return the minutes corresponding to this date
	 */
	public static int getMinutes(Date date) {
		if (date == null) {
			return 0;
		}
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(date);
		
		return calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * Get the last day of the given month and year.
	 * @param month
	 * @param year
	 * @return the maximum value for the day of the given month
	 * and year
	 */
	public static int getLastDayOfMonth(int month, int year) {
		  Calendar calendar = Calendar.getInstance();
		  int date = 1;
		  calendar.set(year, month, date);

		  return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Add minutes to a date.
	 * @param date
	 * 			: the date to modify
	 * @param minutes
	 * 			: the minutes to add
	 * @return a Date instance with the added minutes
	 */
	public static Date addMinutesToDate(Date date, int minutes) {
		GregorianCalendar calendar = new GregorianCalendar(timeZone);
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);

		return calendar.getTime();
	}
}
