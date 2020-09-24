package com.roberto.field.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roberto.field.controller.support.FieldException;

/**
 * Utility class to handle dates.
 * @author roberto
 *
 */
public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	// Example: "2020-07-25T10:03:56.782Z"
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");

	public static String convertDateToString(Date date) {
		logger.debug("convertDateToString: " + date);
		
		if (date == null) {
			return "";
		}
		return sdf.format(date);
	}

	public static Date convertStringToDate(String dateSource) {
		logger.debug("convertStringToDate: " + dateSource);

		if (dateSource == null || dateSource.trim().isEmpty()) {
			return null;
		}
		try {
			return sdf.parse(dateSource);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new FieldException("Invalid date format: " + dateSource);
		}
	}
	
	public static String getNow() {
		LocalDateTime now = LocalDateTime.now();
				
		return Long.toString(now.toEpochSecond(ZoneOffset.UTC));
	}
	
	public static String getSevenDaysBehind() {
		LocalDateTime sevenDaysBehind = LocalDateTime.now().minusDays(6);
		
		return Long.toString(sevenDaysBehind.toEpochSecond(ZoneOffset.UTC));
	}

}
