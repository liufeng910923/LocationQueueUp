package com.queueup.untility;

import java.util.HashMap;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.ui.LoadingActivity;
import com.queueup.ui.MainActivity;
import com.queueup.ui.RegisterActivity;

public class RegisteUntility extends AsyncTask<String, String, Integer> {
	private HashMap<String, String> hashMap;

	public RegisteUntility(HashMap<String, String> hashMap) {
		this.hashMap = hashMap;
	}

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;

		Log.i("info", "ret ---> " + ret);
		try {
			RClient client = new RClient(RegisterActivity.instance,
					Domain.insert_register);
			Log.i("info", "hashMap.get(username)" + hashMap.get("username"));
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			client.AddParam("Id", id);
			client.AddParam("PhoneNumber", hashMap.get("username"));
			client.AddParam("PassWord", hashMap.get("password"));
			client.Execute(RMethod.GET);

			Log.i("info", " ---> hashmap");
			// User Info
			if (client.getResult() == RConstants.OK) {
				publishProgress("RConstants.OK--> 注册成功");
				AndroidUtils.setSharedPreferences(RegisterActivity.instance,
						"LOGIN", "FLG", "TRUE");
				AndroidUtils.setSharedPreferences(RegisterActivity.instance,
						"LOGIN", "LOGIN_ID", id);
				AndroidUtils.setSharedPreferences(RegisterActivity.instance,
						"LOGIN", "LOGIN_NAME", hashMap.get("username"));
			} else {
				ret = -1;
			}

		} catch (Exception e) {
			publishProgress("e--->" + e.getMessage());
			ret = -1;
		}
		return ret;
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onPostExecute(Integer result) {

		if (result == 1) {
			Intent intent = new Intent(RegisterActivity.instance,
					MainActivity.class);
			RegisterActivity.instance.startActivity(intent);
			LoadingActivity.instance.finish();
		} else if (result == -1) {
			Toast.makeText(RegisterActivity.instance, "注册失败", 3000).show();
			LoadingActivity.instance.finish();
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
	}
}
