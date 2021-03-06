package com.queueup.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.queueup.network.AndroidUtils;
import com.queueup.ui.R.string;
import com.queueup.untility.InsertMesgShareUtility;

@SuppressWarnings("deprecation")
public class MsgShareActivity extends Activity {
	public static MsgShareActivity instance;
	private Button peopleBtn, sendBtn, exitBtn;
	private EditText writePeople;
	private EditText context;
	private String wNumberStr;
	private static final int CONTACT_REQUEST_CODE = 2;
	final String SEND_SUCCESS_ACITON = "SEND_SUCCESS_ACITON";
	final String ACCEPT_SUCCESS_ACITON = "ACCEPT_SUCCESS_ACITON";
	private IntentFilter mSMSResultFilter = new IntentFilter();
	BroadcastReceiver sendMessage;
	TextView show;
	String memberId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_mesg);
		writePeople = (EditText) findViewById(R.id.write_people);
		peopleBtn = (Button) findViewById(R.id.people);
		sendBtn = (Button) findViewById(R.id.send);
		exitBtn = (Button) findViewById(R.id.exit);
		context = (EditText) findViewById(R.id.context);
		instance = this;

		memberId = AndroidUtils.getSharedPreferences(MsgShareActivity.this, "LOGIN", "LOGIN_ID");
		System.out.println("memberId==>" + memberId);

		// 联系人按钮
		peopleBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {
				// 页面传值并获取回传值
				Intent intent = new Intent();
				intent.setClass(MsgShareActivity.this, ContactListView.class);
				Bundle bundle = new Bundle();
				wNumberStr = writePeople.getText().toString();

				bundle.putString("wNumberStr", wNumberStr);
				intent.putExtras(bundle);
				startActivityForResult(intent, CONTACT_REQUEST_CODE);
			}

		});

		//
		sendMessage = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) { // 判断短信是否发送成功
				String phoneNum = intent.getStringExtra("KEY_PHONENUM");
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "短信已发送到:" + phoneNum, Toast.LENGTH_SHORT).show();

					String receiverName = "";
					String receiverPhone = phoneNum;
					String shareFlag = "member";
					InsertMesgShareUtility insertMesgShareUtility = new InsertMesgShareUtility(
							memberId, receiverName, receiverPhone, shareFlag);
					insertMesgShareUtility.execute();

					sendBtn.setEnabled(true);
					
					break;
				default:
					Toast.makeText(instance, "发送失败:" + phoneNum, Toast.LENGTH_LONG).show();
					break;
				}
			}
		};

		sendBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String message = context.getText().toString().trim();
				wNumberStr = writePeople.getText().toString().trim();
				if (isMobileNO(wNumberStr)) {
					wNumberStr = wNumberStr.replaceAll("，", ",");
					String number[] = wNumberStr.split(",");
					SmsManager sm = SmsManager.getDefault();

					mSMSResultFilter.addAction("SENT_SMS_ACTION");
					registerReceiver(sendMessage, mSMSResultFilter);
					for (int i = 0; i < number.length; i++) {

						Intent itSend = new Intent("SENT_SMS_ACTION");
						itSend.putExtra("KEY_PHONENUM", number[i]);
						PendingIntent sentIntent = PendingIntent.getBroadcast(instance, i, itSend,
								PendingIntent.FLAG_ONE_SHOT);
						sendBtn.setEnabled(false);
						sm.sendTextMessage(number[i], null, message, sentIntent, null);
						
					}
				} else {
					Toast.makeText(instance, "号码格式不正确", Toast.LENGTH_SHORT).show();
				}

			}

			@SuppressLint("ShowToast")
			public void showToast() {
				Toast.makeText(MsgShareActivity.instance, "发送成功", 3000).show();
			}
		});
		exitBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}

	// 重写获取页面回传值
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONTACT_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				String numberStr = null;
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					numberStr = bundle.getString("numberStr");
				}
				writePeople.setText(numberStr);
			}
			break;
		}
	}

	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		System.out.println(m.matches() + "---");
		return m.matches();
	}
}
