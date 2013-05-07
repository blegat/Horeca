package com.horeca;

import java.text.DateFormat;
import java.util.Date;

public class Utils {
	public static String dateToString(Date date) {
		//return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT).format(date);
		return DateFormat.getInstance().format(date);
	}
	public static String distanceToString(double distance) {
		return ((long) distance) + " m";
	}
}