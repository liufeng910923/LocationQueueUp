/*package com.queueup.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.jpush.android.api.JPushInterface;

import com.queueup.baidumap.ui.DemoApplication;
import com.queueup.untility.LogUtile;

public class QueueUpActivity extends Activity {
	Button sure_btnButton, scanner_btn;
	public static EditText queue_number;
	public static QueueUpActivity instance;
	public String memberId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queueup);
		instance = this;
		initview();
		DemoApplication.getInstance().add(QueueUpActivity.this);
		// 确定按钮
		sure_btnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				 * Intent intent = new Intent();
				 * intent.setClass(QueueUpActivity.this, MyQueueUp.class);
				 * startActivity(intent);
				 
				memberId = AndroidUtils.getSharedPreferences(
						CaptureActivity.instance, "LOGIN", "LOGIN_ID");
				new QueueUpUtility().execute(memberId.toString(), queue_number
						.getText().toString());
			}
		});

		// 扫描按钮
		scanner_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QueueUpActivity.this,
						CaptureActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initview() {
		sure_btnButton = (Button) findViewById(R.id.sure);
		scanner_btn = (Button) findViewById(R.id.scanner);
		queue_number = (EditText) findViewById(R.id.queue_edit);
		instance = this;
	}


	
	*//**
	 * 监听返回键，退出程序
	 *//*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtile.d("QueueUpActivity  onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) { // && event.getRepeatCount() ==
			// 0
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage("确定要退出吗?");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DemoApplication.getInstance().exit();
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
		return true;
	}

}
*/