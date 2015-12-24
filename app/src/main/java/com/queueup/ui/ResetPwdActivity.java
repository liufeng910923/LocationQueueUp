package com.queueup.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.ResetPwdUtility;

public class ResetPwdActivity extends Activity {
/*	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_pwd);
	}*/
	private EditText et_fp_mail1, et_fp_password1, et_fp_confirmpwd;
	private Button fp_reback_btn, fp_right_btn;
	private String phoneNo;
	private ResetPwdUtility resetPwdUtility;
	public static Context instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_pwd);
		phoneNo = getIntent().getStringExtra("phoneaddress");
		DemoApplication.getInstance().add(ResetPwdActivity.this);
		initview();
		fp_right_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String pass = et_fp_password1.getText().toString();
				String cpass = et_fp_confirmpwd.getText().toString();
				if (pass == null || "".equals(pass)) {
					ShowToast("密码不能为空");
					return;
				}
				if (cpass == null || "".equals(cpass)) {
					ShowToast("确认密码不能为空");
					return;
				}
				if (!pass.equals(cpass)) {
					ShowToast("两次密码输入不同");
					return;
				}
				resetPwdUtility = new ResetPwdUtility(pass, phoneNo);
				resetPwdUtility.execute();
			}
		});
		fp_reback_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}


	private void initview() {

		et_fp_password1 = (EditText) findViewById(R.id.et_fp_password1);
		et_fp_mail1 = (EditText) findViewById(R.id.et_fp_phone1);
		et_fp_confirmpwd = (EditText) findViewById(R.id.et_fp_confirmpwd);
		fp_reback_btn = (Button) findViewById(R.id.fp_reback_btn);
		fp_right_btn = (Button) findViewById(R.id.fp_right_btn);

		et_fp_mail1.setText(phoneNo);

		instance = this;
	}

	public void ShowToast(String value) {
		Toast.makeText(instance, value, 3000).show();
	}

}
