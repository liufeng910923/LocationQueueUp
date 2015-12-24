package com.queueup.untility;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.push.ExampleUtil;
import com.queueup.ui.LoadingActivity;
import com.queueup.ui.LoginActivity;
import com.queueup.ui.MainActivity;
import com.queueup.ui.MyQueueUp;

@SuppressLint("ShowToast")
public class LoginUtility extends AsyncTask<String, String, Integer> {
	private static final int MSG_SET_TAGS = 1001;
	public static ArrayList<String> login_name_list=new ArrayList<String>();
	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;

		String username = params[0];
		String password = params[1];

		Log.i("info", "user ---> " + username);
		Log.i("info", "pwd ---> " + password);
		try {

			RClient client = new RClient(LoginActivity.instance,
					Domain.insert_login);
			client.AddParam("LoginName", username);
			client.AddParam("LoginPwd", password);
			client.Execute(RMethod.GET);

			if (client.getResult() == RConstants.OK) {
				JSONObject jo = new JSONObject(client.getResponse());
				String content = jo.getString("contents").toString();

				JSONObject contentJson = new JSONObject(content);
				String existstr = contentJson.get("Exist").toString();
				String matchingstr = contentJson.get("Matching").toString();
				System.out.println(existstr);
				System.out.println(matchingstr);

				if ("true".equals(existstr)) {
					if ("true".equals(matchingstr)) {
						publishProgress("登陆成功!");
						ret = 1;
						AndroidUtils.setSharedPreferences(
								LoginActivity.instance, "LOGIN", "FLG", "TRUE");
						AndroidUtils.setSharedPreferences(
								LoginActivity.instance, "LOGIN", "LOGIN_NAME",
								username);

						Object id = contentJson.get("Id").toString();
						Object name = contentJson.get("LoginName").toString();
						//将登陆名加到loginname_list里面去
						login_name_list.add(username);
						System.out.println(login_name_list);
						
						AndroidUtils.setSharedPreferences(
								LoginActivity.instance, "LOGIN", "LOGIN_ID",
								(String) id);
						AndroidUtils.setSharedPreferences(
								LoginActivity.instance, "LOGIN", "LOGIN_NAME",
								(String) name);
						

					} else {
						ret = -1;
						publishProgress("登入失败密码错误");
					}

				} else {

					ret = -2;
					publishProgress("对不起，该用户不存在");

				}
			}

		} catch (Exception ex) {
			if (("网络不可用！".equals(ex.getMessage()))
					|| ("网络不可用!".equals(ex.getMessage()))) {
				/*publishProgress(ex.getMessage());*/
				ret = -3;
			}
			ret = -4;
		}
		return ret;
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onPostExecute(Integer result) {
		if (result == 1) {

			Intent intent = new Intent();
			intent.setClass(LoginActivity.instance, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			LoginActivity.instance.startActivity(intent);
			LoginActivity.instance.finish();
			LoadingActivity.instance.finish();
		} else if (result == -1) {
			Toast.makeText(LoginActivity.instance, "登入失败,密码错误!", 3000).show();
			LoadingActivity.instance.finish();
		} else if (result == -2) {
			Toast.makeText(LoginActivity.instance, "对不起，该用户不存在!", 3000).show();
			LoadingActivity.instance.finish();
		}
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onProgressUpdate(String... values) {
		Toast.makeText(LoginActivity.instance, values[0].toString(), 3000)
				.show();
	}
}