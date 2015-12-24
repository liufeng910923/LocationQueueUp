package com.queueup.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.UpdateVersionInfoUtility;

public class WelcomeView extends Activity {
	public ProgressDialog pBar;
	public static WelcomeView instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcomeview);
		instance = this;
		DemoApplication.getInstance().add(WelcomeView.this);
		new Handler().postDelayed(new Runnable() {

			public void run() {
				// 判断版本是否是最新的,如果不是弹提示 是否更新
				int verCode = -1;
				try {
					verCode = getApplicationContext().getPackageManager().getPackageInfo("com.queueup.ui", 0).versionCode;
					UpdateVersionInfoUtility updateVersionInfoUtility = new UpdateVersionInfoUtility(String.valueOf(verCode), instance);
					updateVersionInfoUtility.execute();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 3000);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(instance);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(instance);
	}
	

}