package com.queueup.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;

import com.queueup.untility.LoginUtility;
import com.queueup.untility.RegisteUntility;

public class LoadingActivity extends Activity {
	String username, password, flg;
	public static LoadingActivity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		instance = this;
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		username = bundle.getString("username");
		password = bundle.getString("password");
		flg = bundle.getString("flg");
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (flg.equals("1")) {
						
					new LoginUtility().execute(username, password);
					
					}else if (flg.equals("2")) {
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("username", username);
						hashMap.put("password", password);
						new RegisteUntility(hashMap).execute();
					}
				}
			}, 200);
		} 

	

}