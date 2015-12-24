package com.queueup.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		DemoApplication.getInstance().add(AboutActivity.this);
	}

	public void backBtn(View view) {
		finish();
	}
}
