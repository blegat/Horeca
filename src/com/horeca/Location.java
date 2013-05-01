package com.horeca;

import android.util.Log;


public class Location {
	private double longitude;
	private double latitude;
	public Location (double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public double getDistance (GPSTracker gps) {
		android.location.Location cur = gps.getLocation();
		Log.i("cur/long", String.valueOf(cur.getLongitude()));
		Log.i("cur/lat", String.valueOf(cur.getLatitude()));
		android.location.Location dest = new android.location.Location(cur);
		dest.setLongitude(longitude);
		dest.setLatitude(latitude);
		Log.i("des/long", String.valueOf(dest.getLongitude()));
		Log.i("des/lat", String.valueOf(dest.getLatitude()));
		return cur.distanceTo(dest);
	}
}