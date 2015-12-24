package com.queueup.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;
import com.queueup.network.AndroidUtils;

public class ExitDialogView extends Activity {
	private Button btn_sure;
	private Button btn_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_menu);
		DemoApplication.getInstance().add(ExitDialogView.this);
		btn_sure = (Button) findViewById(R.id.exitsure);
		btn_cancle = (Button) findViewById(R.id.exitcancel);
	}

	@SuppressLint("NewApi")
	public void exitclick(View v) {
		switch (v.getId()) {
		case R.id.exitsure:
			AndroidUtils.setSharedPreferences(this, "LOGIN", "FLG", "FALSE");
			btn_sure.setBackgroundResource(R.drawable.tcsure2);
			Intent intent = new Intent(ExitDialogView.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			ExitDialogView.this.finish();
			break;
		case R.id.exitcancel:
			btn_cancle.setBackgroundResource(R.drawable.tcqx2);
			ExitDialogView.this.finish();
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
}