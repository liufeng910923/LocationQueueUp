package com.queueup.untility;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMessage;
import com.queueup.network.RMethod;
import com.queueup.ui.LoginActivity;
import com.queueup.ui.RegisterActivity;

public class SendCodeUtility extends AsyncTask<String, String, Integer> {
	private String mycode;
	private String rgname;
	private String msg;
	private String flg;
	private Handler mHandler;
	private Runnable runnable;
	private Button mButton;

	public SendCodeUtility(String mycode, String rgname, String flg, Handler mHandler, Runnable runnable, Button mButton) {
		this.mycode = mycode;
		this.rgname = rgname;
		this.flg = flg;
		this.mHandler = mHandler;
		this.runnable = runnable;
		this.mButton = mButton;
	}

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		try {

			RClient client = new RClient(LoginActivity.instance, Domain.send_code);
			client.AddParam("SendCode", "" + mycode);
			client.AddParam("PhoneNumber", "" + rgname);
			client.AddParam("SendType", "" + flg);
			client.Execute(RMethod.GET);
			if (client.getResult() == RConstants.OK) {
				LogUtils.e(client.getResponse());
				JSONObject jo = new JSONObject(client.getResponse());
				String content = jo.get("contents").toString();
				JSONObject contentJson = new JSONObject(content);

				String sendtstr = contentJson.get("Send").toString();
				String inLimitstr = contentJson.get("InLimit").toString();

				// ture：在限制内 false：超过限制 (半小时以内同一手机号发验证码不能超过3次)
				if (inLimitstr.equals("true")) {
					// true：能发送 false：不能发送
					if (sendtstr.equals("true")) {
						msg = "验证码发送成功";
						mHandler.postDelayed(runnable, 1000);

					} else {
						if ("register".equals(flg)) {
							msg = "该手机号已经被注册";
						} else if ("findpass".equals(flg)) {
							msg = "未使用此手机号注册";
						}
					}
				} else {
					msg = "您操作过于频繁，请在30分钟后再试下。";
				}

			} else {
				msg = "网络错误，发送失败";
				publishProgress(RMessage.getMsg(client.getResult(), RegisterActivity.instance));
				ret = -1;
			}

		} catch (Exception ex) {
			msg = "发送错误，请重试";
			publishProgress(ex.getMessage());
			ret = -1;
		}
		return ret;
	}

	@SuppressLint("ShowToast")
	protected void onPostExecute(Integer result) {
		if (!msg.equals("验证码发送成功")) {
			mButton.setEnabled(true);
		}
		Toast.makeText(LoginActivity.instance, msg, 3000).show();

	}

	protected void onProgressUpdate(String... values) {
	}

}