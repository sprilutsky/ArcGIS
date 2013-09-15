package com.example.study.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

/***
 * 
 * @author Sergey Prilutsky
 * 
 */

public class Utilities {

	public static boolean hasConnection(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}
		return false;
	}

	public static final Point getLongitude(Context ctx, MapView mapView,
			Point point) {
		SpatialReference sp = SpatialReference
				.create(SpatialReference.WKID_WGS84);
		point = (Point) GeometryEngine.project(point,
				mapView.getSpatialReference(), sp);
		return point;
	}

	private static double modulus(double x, double y) {
		return x - y * Math.floor(x / y);
	}

	private static int minuteOfDegree(double longitude) {
		int minute = (int) (modulus(Math.abs(longitude), 1.0) * 60.0);
		return minute;
	}

	private static int secondOfDegree(double longitude) {
		int seconds = (int) (modulus(longitude, 1.0 / 60.0) * 3600.0);
		return seconds;
	}

	public static String getNormalizeLongitude(double longitudeValue) {
		int longitude = (int) longitudeValue;
		String valueLongitude = null;
		if (longitude < 0) {
			valueLongitude = "E";
			longitude = -longitude;
		} else {
			valueLongitude = "W";
		}

		int minuteLongitude = minuteOfDegree(Math.abs(longitudeValue) + 0.005);
		int secondLongitude = secondOfDegree(longitudeValue);

		String longitudeRet;
		if (secondLongitude != 0 && secondLongitude != 60) {
			longitudeRet = new StringBuilder(String.format("%02d", longitude))
					.append("\u00B0").append(valueLongitude)
					.append(String.format("%02d", minuteLongitude))
					.append("\'")
					.append(String.format("%02d", secondLongitude))
					.append("\'\'").toString();
		} else {
			longitudeRet = new StringBuilder(String.format("%02d", longitude))
					.append("\u00B0").append(valueLongitude)
					.append(String.format("%02d", minuteLongitude))
					.append("\'").toString();
		}
		return longitudeRet;
	}

	public static String getNormalizeLatitude(double latitudeValue) {
		int latitude = (int) latitudeValue;
		String valueLatitude = null;
		if (latitude < 0) {
			valueLatitude = "S";
			latitude = -latitude;
		} else {
			valueLatitude = "N";
		}

		int minuteLatitude = minuteOfDegree(Math.abs(latitudeValue) + 0.005);
		int secondLatitude = secondOfDegree(latitudeValue);

		String latitudeRet;

		if (secondLatitude != 0 && secondLatitude != 60) {
			latitudeRet = new StringBuilder(String.format("%02d", latitude))
					.append("\u00B0").append(valueLatitude)
					.append(String.format("%02d", minuteLatitude)).append("\'")
					.append(String.format("%02d", secondLatitude))
					.append("\'\'").toString();
		} else {
			latitudeRet = new StringBuilder(String.format("%02d", latitude))
					.append("\u00B0").append(valueLatitude)
					.append(String.format("%02d", minuteLatitude)).append("\'")
					.toString();
		}
		return latitudeRet;
	}
}