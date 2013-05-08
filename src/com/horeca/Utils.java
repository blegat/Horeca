package com.horeca;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;

public class Utils {
	public static String dateToString(Date date) {
		//return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT).format(date);
		return DateFormat.getInstance().format(date);
	}
	public static String priceToString(double price, Context context) {
		return price + context.getResources().getString(R.string.plat_price_currency);
	}
	public static String distanceToString(double distance) {
		return ((long) distance) + " m";
	}
}