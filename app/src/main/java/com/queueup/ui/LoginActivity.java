package com.queueup.ui;
import android.R.anim;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.LogUtils;
import com.queueup.untility.LoginUtility;

public class LoginActivity extends Activity {
	public static LoginActivity instance;
	private EditText  password;
	private AutoCompleteTextView username;
	private TextView register, findpassword;
	private Intent intent;
	public ArrayAdapter<String> adapter;
	public static boolean isDaoJiShi = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initview();
		DemoApplication.getInstance().add(LoginActivity.this);
		

		// 注册
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
			}
		});

		// 找回密码
		findpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						FindPasswordActivity.class));
			}
		});
	}


	// 登入按钮
	public void loginViewClick(View view) {
		intent = new Intent();
		intent.setClass(LoginActivity.this, LoadingActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("username", username.getText().toString());
		bundle.putString("password", password.getText().toString());
		bundle.putString("flg", "1");
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void initview() {
		register = (TextView) findViewById(R.id.TextView2);
		findpassword = (TextView) findViewById(R.id.textView1);
		//AutoCompleteTextView的初始化；
		username = (AutoCompleteTextView) findViewById(R.id.login_user_edit);
		//初始化适配器：
		adapter = new ArrayAdapter<String>(this,  
                android.R.layout.simple_dropdown_item_1line,LoginUtility.login_name_list );
		//绑定适配器：
		username.setAdapter(adapter);
		
		password = (EditText) findViewById(R.id.login_pwd_edit);
		instance = this;
	}
	
	
	/**
	 * 监听返回键，退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("PersonAttentionActivity  onKeyDown");
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
