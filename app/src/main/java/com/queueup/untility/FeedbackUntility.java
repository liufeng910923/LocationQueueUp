package com.queueup.untility;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.ui.FeedBackActivity;

public class FeedbackUntility extends AsyncTask<String, String, Integer> {

	// public FeedbackUntility(HashMap<String, String> hashMap) {
	// this.hashMap = hashMap;
	// }

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;

		String str_feedback = params[0];
		String str_member_id = params[1];

		System.out.println("str_feedback=" + str_feedback);
		System.out.println("str_member_id=" + str_member_id);

		Log.i("info", "ret ---> " + ret);
		try {
			RClient client = new RClient(FeedBackActivity.instance, Domain.str_feedback);

			Log.i("info", "hashMap.get(str_feedback)" + str_feedback);

			client.AddParam("str_feedback", str_feedback);
			client.AddParam("member_id", str_member_id);
			client.Execute(RMethod.GET);

			Log.i("info", " ---> hashmap");
			// User Info
			if (client.getResult() == RConstants.OK) {
				publishProgress("RConstants.OK-->提交成功");
				AndroidUtils.setSharedPreferences(FeedBackActivity.instance, "FEEDBACK", "FLG", "TRUE");
				// AndroidUtils.setSharedPreferences(FeedBackActivity.instance,
				// "LOGIN", "LOGIN_ID", id);
				AndroidUtils.setSharedPreferences(FeedBackActivity.instance, "FEEDBACK", "FEEDBACGCONTENT", str_feedback);
			} else {
				ret = -1;
			}

		} catch (Exception e) {
			publishProgress("e--->" + e.getMessage());
			ret = -1;
		}
		return ret;
	}

	@Override
	protected void onPostExecute(Integer result) {

		if (result == 1) {
			Toast.makeText(FeedBackActivity.instance, "内容提交成功，感谢您宝贵的意见,内容提交成功!", 3000).show();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FeedBackActivity.instance.finish();
		} else if (result == -1) {
			// Toast.makeText(FeedBackActivity.instance, "提交失败", 3000).show();
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
	}
}
