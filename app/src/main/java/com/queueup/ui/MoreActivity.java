package com.queueup.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.LogUtils;

public class MoreActivity extends Activity {
	public static MoreActivity instance;
	public RelativeLayout about_btn;
	public RelativeLayout idea_feed_back;
	public RelativeLayout share;
	public RelativeLayout more_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moreview);
		instance = this;
		DemoApplication.getInstance().add(MoreActivity.this);
		init();
	}

	public void init() {
		about_btn = (RelativeLayout) findViewById(R.id.about_btn);

		idea_feed_back = (RelativeLayout) findViewById(R.id.idea_feed_back);

		share = (RelativeLayout) findViewById(R.id.share);

		more_back = (RelativeLayout) findViewById(R.id.more_back);
	}

	@SuppressLint("ResourceAsColor")
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.about_btn: // 关于

			startActivity(new Intent(MoreActivity.this, AboutActivity.class));

			break;
		// case R.id.version_update: // 版本更新
		// /*
		// * startActivity(new
		// * Intent(MoreActivity.this,VersionUpdateActivity.class));
		// */
		// int verCode = -1;
		// try {
		// verCode = getApplicationContext().getPackageManager()
		// .getPackageInfo("com.queueup.ui", 0).versionCode;
		// UpdateVersionInfoUtility updateVersionInfoUtility = new
		// UpdateVersionInfoUtility(
		// String.valueOf(verCode),MoreActivity.this);
		// updateVersionInfoUtility.execute();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// break;
		case R.id.idea_feed_back:// 意见反馈

			startActivity(new Intent(MoreActivity.this, FeedBackActivity.class));
			break;

		case R.id.share: // 分享

			startActivity(new Intent(MoreActivity.this, ShareDialogView.class));
			break;

		// case R.id.btn_discount://优惠券：
		// startActivity(new Intent(MoreActivity.this,DiscountActivity.class));
		// break;
		case R.id.more_back: // 退出

			startActivity(new Intent(MoreActivity.this, ExitDialogView.class));
			break;
		}
	}

	/**
	 * 监听返回键，退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("more - onKeyDown");
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
			// startActivity(new Intent(MoreActivity.this,
			// ExitDialogView.class));
		}
		return true;
	}

}
