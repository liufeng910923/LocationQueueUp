package com.queueup.untility;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.entity.MyQueueUpInfo;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.ui.MainActivity;
import com.queueup.ui.MyQueueUp;
import com.queueup.ui.PersonAttentionActivity;

public class MyAttentionDetailUtility extends
		AsyncTask<String, String, Integer> {
	MyQueueUpInfo myQueueUpInfo = new MyQueueUpInfo();

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;

		String memberId = params[0];
		String companyId = params[1];
		String companyType = params[2];
		String myQueueUpNum = params[3];

		try {

			RClient client = new RClient(PersonAttentionActivity.instance,
					Domain.getQueueUpInfo);
			client.AddParam("MemberId", memberId);
			client.AddParam("CompanyId", companyId);
			client.AddParam("CompanyType", companyType);
			client.AddParam("MyQueueUpNum", myQueueUpNum);
			client.Execute(RMethod.GET);

			if (client.getResult() == RConstants.OK) {
				JSONObject jsonObject = new JSONObject(client.getResponse());
				String content = jsonObject.getString("contents").toString();
				JSONObject contentJson = new JSONObject(content);

				Object compyId = contentJson.get("CompanyId").toString();
				Object companyName = contentJson.get("CompanyName").toString();
				Object compyType = contentJson.get("CompanyType").toString();
				Object companyAddress = contentJson.get("CompanyAddress")
						.toString();
				Object companyContract = contentJson.get("CompanyContract")
						.toString();
				Object queueUpCount = contentJson.get("FrontQueueUpCount")
						.toString();
				Object currentNumber = contentJson.get("CurrentNumber")
						.toString();
				Object alreadyCount = contentJson.get("AlreadyCount")
						.toString();
				Object pictureAddress = contentJson.get("PictureAddress");
				
				Object myqueueUpNum = contentJson.get("MyQueueUpNum");
				
				myQueueUpInfo = new MyQueueUpInfo();
				myQueueUpInfo.setCompanyId(compyId.toString());
				myQueueUpInfo.setCompanyName(companyName.toString());
				myQueueUpInfo.setAlreadyCount(alreadyCount.toString());
				myQueueUpInfo.setCompanyContract(companyContract.toString());
				myQueueUpInfo.setCompanyType(compyType.toString());
				myQueueUpInfo.setCurrentNumber(currentNumber.toString());
				myQueueUpInfo.setPictureAddress(pictureAddress.toString());
				myQueueUpInfo.setQueueUpCount(queueUpCount.toString());
				myQueueUpInfo.setCompanyAddress(companyAddress.toString());
				myQueueUpInfo.setMyQueueUpNum(myqueueUpNum.toString());
			} else {
				ret = -1;
				publishProgress("查询失败！");
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if (("网络不可用！".equals(ex.getMessage()))
					|| ("网络不可用!".equals(ex.getMessage()))) {
				publishProgress(ex.getMessage());
				ret = -2;
			}
			ret = -3;
		}
		return ret;
	}

	@SuppressLint("ShowToast")
	protected void onPostExecute(Integer result) {
		if (result == 1) {
			
			MainActivity.myQueueUpInfo = myQueueUpInfo;
			
			Intent intent = new Intent();
			intent.setClass(PersonAttentionActivity.instance, MyQueueUp.class);
			intent.putExtra("myQueueUpInfo", myQueueUpInfo);
			PersonAttentionActivity.instance.startActivity(intent);

		} else {
			Toast.makeText(PersonAttentionActivity.instance, "网络异常",
					3000).show();
		}
	}

	@SuppressLint("ShowToast")
	protected void onProgressUpdate(String... values) {
		Toast.makeText(PersonAttentionActivity.instance, values[0].toString(),
				3000).show();
	}
}