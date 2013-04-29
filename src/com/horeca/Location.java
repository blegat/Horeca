package com.horeca;

import android.content.Context;
import android.location.LocationManager;

public class Location {
	private static android.location.Location getCurrentLocation(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}
	public static double getCurrentLongitude (Context context) {
		return 0;
		//return getCurrentLocation(context).getLongitude();
	}
	public static double getCurrentLatitude (Context context) {
		return 0;
		//return getCurrentLocation(context).getLatitude();
	}
	
	private double longitude;
	private double latitude;
	public double getDistance (Context context) {
		android.location.Location cur = getCurrentLocation(context);
		android.location.Location dest = new android.location.Location(cur);
		dest.setLongitude(longitude);
		dest.setLatitude(latitude);
		return cur.distanceTo(cur);
	}
}