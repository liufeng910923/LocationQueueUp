package com.queueup.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.SendCodeUtility;

public class FindPasswordActivity extends Activity {
	private EditText register_phone, input_security_code;
	private Button send_code, sure_btn;
	private String mycode = "";
	private Integer recLen = 60;
	private Handler handler;
	private SendCodeUtility sendCodeUtility;
	public static FindPasswordActivity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findpassword);
		initview();
		DemoApplication.getInstance().add(FindPasswordActivity.this);

		if (LoginActivity.isDaoJiShi) {
			recLen = 60;
			// 计时器：
			handler.postDelayed(runnable, 1000);
		}

		// 获取验证码按钮
		send_code.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String rgname = register_phone.getText().toString();

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
						send_code.setEnabled(false);
						recLen = 60;
						// 生成随机数
						int code = (int) ((Math.random() * 9 + 1) * 100000);
						mycode = String.valueOf(code);
						System.out.println(mycode);
						sendCodeUtility = new SendCodeUtility(mycode, rgname, "findpass", handler, runnable, send_code);
						sendCodeUtility.execute();

					}

				}

			}
		});

		// 确定按钮
		sure_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String etcode = input_security_code.getText().toString();
				if ("".equals(mycode)) {
					ShowToast("验证码未发送");
				} else {
					if (etcode.equals(mycode)) {
						Intent intent = new Intent();
						intent.setClass(FindPasswordActivity.instance, ResetPwdActivity.class);
						intent.putExtra("phoneaddress", register_phone.getText().toString());
						startActivity(intent);
					} else {
						ShowToast("验证码不正确‘");
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
				send_code.setText("剩余时间(" + recLen.toString() + ")");
				handler.postDelayed(this, 1000);
				send_code.setEnabled(false);
				send_code.setTextSize(12);
			} else {
				LoginActivity.isDaoJiShi = false;
				send_code.setText("发送验证码");
				send_code.setEnabled(true);
			}

		}
	};

	private void initview() {

		register_phone = (EditText) findViewById(R.id.register_phone);
		input_security_code = (EditText) findViewById(R.id.input_security_code);
		send_code = (Button) findViewById(R.id.send_code);
		sure_btn = (Button) findViewById(R.id.sure_btn);
		instance = this;
		handler = new Handler();
	}

	public void ShowToast(String value) {
		Toast.makeText(instance, value, 3000).show();
	}

	public void backBtn(View v) {
		finish();
	}
}
