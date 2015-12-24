package com.queueup.ui;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ScreenDialog extends Activity {
	public static ScreenDialog instance;
	/*TextView tvhospital, tvfood;*/

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_dialog);
		InitView();
		DemoApplication.getInstance().add(ScreenDialog.this);
	}
	


	private void InitView() {
		/*tvfood = (TextView) findViewById(R.id.food);
		tvhospital = (TextView) findViewById(R.id.hospital);*/
		instance = this;
	}

	public void selectOnClick(View view) {
		switch (view.getId()) {
		case R.id.food:
			/*startActivity(new Intent(instance, RestaurantActivity.class));*/
			finish();
			break;

		case R.id.hospital:
			/*startActivity(new Intent(instance, RestaurantActivity.class));*/
			finish();
			break;
		}
	}
}
