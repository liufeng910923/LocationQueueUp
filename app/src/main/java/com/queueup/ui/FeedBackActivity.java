package com.queueup.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.queueup.conf.DemoApplication;
import com.queueup.network.AndroidUtils;
import com.queueup.untility.FeedbackUntility;

public class FeedBackActivity extends Activity {
	public static FeedBackActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		final EditText feedback = (EditText) findViewById(R.id.edt_suggestion);

		final Button submit = (Button) findViewById(R.id.btn_submit);

		instance = this;

		DemoApplication.getInstance().add(FeedBackActivity.this);

		// 给提交意见Button添加监听事件：
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				// 定义String 意见框的内容
				String str_feedback = feedback.getText().toString();
				// 编辑框的内容为空：
				if (!TextUtils.isEmpty(str_feedback)) {
					// 定义String member_id
					String str_member_id = AndroidUtils.getSharedPreferences(FeedBackActivity.instance, "LOGIN", "LOGIN_ID");
					new FeedbackUntility().execute(str_feedback, str_member_id);
				} else {
					ShowToast("提交的内容为空，请重新编辑...");

				}
			}

		});

	}

	private void ShowToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public void backBtn(View view) {
		FeedBackActivity.this.finish();
	}
}
