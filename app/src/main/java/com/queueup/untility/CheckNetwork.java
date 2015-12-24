/**
 * @(#)CheckNetwork.java
 * Copyright (c) 2013-2014  Simon  All rights reserved.
 */
package com.queueup.untility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * @Title:CheckNetwork.java
 * @Description:TODO
 * @Author:Simon
 * @Date 2014-5-20
 */
public class CheckNetwork {
	private static ConnectivityManager connManager;

	public static boolean isAirplaneModeOn(Context context) { // 返回值是1时表示处于飞行模式
		int modeIdx = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		boolean isEnabled = (modeIdx == 1);
		return isEnabled;
	}

	/**
	 * @Title: isNetworkConnected
	 * @Description: 网络是否连接
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkConnected(Context context) {
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		if (networkinfo != null) {
			return networkinfo.isConnected();
		}
		return false;
	}

	/***
	 * 是否是wifi连接
	 * 
	 * @Title: isWifiConnected
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifiConnected(Context context) {
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi != null) {
			return mWifi.isConnected();
		}
		return false;
	}

	/***
	 * 是否网络连接状态
	 * 
	 * @Title: isMobileConnected
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobileConnected(Context context) {
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mMobile != null) {
			return mMobile.isConnected();
		}
		return false;
	}

	/***
	 * 检测网络等
	 * 
	 * @Title: isOpenNetwork
	 * @param context
	 * @return boolean
	 */
	public static boolean isOpenNetwork(Context context) {
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// setContext(acvt.getApplicationContext());
		if (isAirplaneModeOn(context)) {
			return false;
		}

		if (isWifiConnected(context)) {
			return true;
		}

		if (isMobileConnected(context)) {
			return true;
		}

		if (isNetworkConnected(context)) {
			return true;
		}

		return false;
	}

}
