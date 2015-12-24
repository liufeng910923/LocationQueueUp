package com.queueup.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.SendCodeUtility;

public class RegisterActivity extends Activity {
	public static Context instance;
	EditText et_username;
	EditText et_password;
	EditText et_repass;
	EditText et_email, yanzhengma;
	Button login_reback_btn, sendcode_btn, regass_login_btn;
	ImageView usernameerror, passworderror, repasserror, yanzhengmaerror;
	private String mycode = "";
	private SendCodeUtility sendCodeUtility;
	private Handler handler;
	Integer recLen = 60;
	private String rgname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		initview();

		handler = new Handler();
		regass_login_btn.setClickable(false);
		DemoApplication.getInstance().add(RegisterActivity.this);

		if (LoginActivity.isDaoJiShi) {
			recLen = 60;
			// 计时器：
			handler.postDelayed(runnable, 1000);
		}

		// 注册按钮
		regass_login_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String username = et_username.getText().toString();
				String password = et_password.getText().toString();
				String repass = et_repass.getText().toString();

				boolean isNull = false;

				if ("".equals(username)) {
					usernameerror.setVisibility(View.VISIBLE);
					isNull = true;
					return;
				} else {
					String check = "^[1][3,4,5,8][0-9]{9}$";
					Pattern regex = Pattern.compile(check);
					Matcher matcher = regex.matcher(username);
					if (!matcher.matches()) {
						usernameerror.setVisibility(View.VISIBLE);
						ShowToast("手机号码格式不正确!");
						return;
					}
					usernameerror.setVisibility(View.GONE);

				}
				String etcode = yanzhengma.getText().toString();
				if ("".equals(mycode)) {
					ShowToast("验证码未发送");
					yanzhengmaerror.setVisibility(View.VISIBLE);
					isNull = true;
					return;
				} else {
					if (!etcode.equals(mycode)) {
						ShowToast("验证码不正确");
						yanzhengmaerror.setVisibility(View.VISIBLE);
						isNull = true;
						return;
					}
				}

				if ("".equals(password)) {
					passworderror.setVisibility(View.VISIBLE);
					isNull = true;
					return;
				} else {
					passworderror.setVisibility(View.GONE);
					isNull = false;
				}
				if ("".equals(repass)) {
					repasserror.setVisibility(View.VISIBLE);
					isNull = true;
					return;
				} else {
					repasserror.setVisibility(View.GONE);
					isNull = false;
				}

				if (isNull) {
					return;
				}

				if (!(repass.equals(password))) {
					ShowToast("两次密码输入不一致!");
					return;
				}

				Toast.makeText(RegisterActivity.this, "插入用户信息", 3000).show();

				/*
				 * HashMap<String, String> hashMap = new HashMap<String,
				 * String>(); hashMap.put("username", username);
				 * hashMap.put("password", password);
				 */
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("username", username);
				bundle.putString("password", password);
				bundle.putString("flg", "2");
				intent.putExtras(bundle);
				intent.setClass(instance, LoadingActivity.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
				/* new RegisteUntility(hashMap).execute(); */
			}

		});

		// 点击获取验证码按钮
		sendcode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				rgname = et_username.getText().toString();

				if ("".equals(rgname)) {
					ShowToast("手机号不能为空!");
					return;
				} else {
					String check = "^[1][3,4,5,8][0-9]{9}$";
					Pattern regex = Pattern.compile(check);
					Matcher matcher = regex.matcher(rgname);
					if (!matcher.matches()) {
						ShowToast("手机号码格式不正确!");
						return;
					} else {
						Log.i("验证码", "点击了一次");
						sendcode_btn.setEnabled(false);
						recLen = 60;
						// 生成随机数
						int code = (int) ((Math.random() * 9 + 1) * 100000);
						mycode = String.valueOf(code);
						System.out.println(mycode);
						sendCodeUtility = new SendCodeUtility(mycode, rgname, "register", handler, runnable, sendcode_btn);
						sendCodeUtility.execute();

					}

				}
			}

		});

	}

	/**
	 * 定义一个倒数的计时器：
	 * */
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (recLen > 0) {
				LoginActivity.isDaoJiShi = true;
				recLen--;
				sendcode_btn.setText("剩余时间(" + recLen.toString() + ")");
				handler.postDelayed(this, 1000);
				sendcode_btn.setEnabled(false);
				sendcode_btn.setTextSize(12);
			} else {
				LoginActivity.isDaoJiShi = false;
				sendcode_btn.setText("发送验证码");
				sendcode_btn.setEnabled(true);
			}

		}
	};

	private void initview() {
		instance = this;
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);
		et_repass = (EditText) findViewById(R.id.repass);
		yanzhengma = (EditText) findViewById(R.id.yanzhengma);
		usernameerror = (ImageView) findViewById(R.id.usernameerror);
		passworderror = (ImageView) findViewById(R.id.passworderror);
		repasserror = (ImageView) findViewById(R.id.repasserror);
		yanzhengmaerror = (ImageView) findViewById(R.id.yanzhengmaerror);
		login_reback_btn = (Button) findViewById(R.id.login_reback_btn);
		sendcode_btn = (Button) findViewById(R.id.sendcode_btn);
		regass_login_btn = (Button) findViewById(R.id.regass_login_btn);

	}

	private void ShowToast(String string) {
	}

	public void backBtn(View v) {
		finish();
	}

}
