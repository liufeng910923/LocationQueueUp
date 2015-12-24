package com.queueup.untility;

import org.json.JSONObject;

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
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class QueueUpUtility extends AsyncTask<String, String, Integer> {
	MyQueueUpInfo myQueueUpInfo = new MyQueueUpInfo();

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;

		String memberId = params[0];
		String myQueueUpNum = params[1];

		try {

			RClient client = new RClient(CaptureActivity.instance, Domain.get_myQueueUp);
			client.AddParam("MemberId", memberId);
			client.AddParam("MyQueueUpNum", myQueueUpNum);
			client.Execute(RMethod.GET);

			if (client.getResult() == RConstants.OK) {
				JSONObject jsonObject = new JSONObject(client.getResponse());
				String content = jsonObject.getString("contents").toString();
				JSONObject contentJson = new JSONObject(content);
				String effective = contentJson.get("Effective").toString();
				/* String overdue = contentJson.get("Overdue").toString(); */

				if ("true".equals(effective)) {
					/*
					 * if ("false".equals(overdue)) {
					 */
					Object companyId = contentJson.get("CompanyId").toString();
					Object companyName = contentJson.get("CompanyName").toString();
					Object companyType = contentJson.get("CompanyType").toString();
					Object companyAddress = contentJson.get("CompanyAddress").toString();
					Object companyContract = contentJson.get("CompanyContract").toString();
					Object queueUpCount = contentJson.get("FrontQueueUpCount").toString();
					Object currentNumber = contentJson.get("CurrentNumber").toString();
					Object alreadyCount = contentJson.get("AlreadyCount").toString();
					Object pictureAddress = contentJson.get("PictureAddress");

					Object overdue = contentJson.get("Overdue");

					myQueueUpInfo = new MyQueueUpInfo();
					myQueueUpInfo.setCompanyId(companyId.toString());
					myQueueUpInfo.setCompanyName(companyName.toString());
					myQueueUpInfo.setAlreadyCount(alreadyCount.toString());
					myQueueUpInfo.setCompanyContract(companyContract.toString());
					myQueueUpInfo.setCompanyType(companyType.toString());
					myQueueUpInfo.setCurrentNumber(currentNumber.toString());
					myQueueUpInfo.setPictureAddress(pictureAddress.toString());
					myQueueUpInfo.setQueueUpCount(queueUpCount.toString());
					myQueueUpInfo.setCompanyAddress(companyAddress.toString());
					String number[] = myQueueUpNum.toString().split("-");
					String mynumber = number[1];
					myQueueUpInfo.setMyQueueUpNum(mynumber);
					myQueueUpInfo.setOverdue(overdue.toString());
				} else {
					ret = -1;
					publishProgress("您输入的号码不存在，请确认！");
				}

			} else {
				ret = -1;
				publishProgress("查询失败！");
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if (("网络不可用！".equals(ex.getMessage())) || ("网络不可用!".equals(ex.getMessage()))) {
				publishProgress(ex.getMessage());
				ret = -2;
			}
			ret = -3;
		}
		return ret;
	}

	protected void onPostExecute(Integer result) {
		if (result == 1) {
			MainActivity.myQueueUpInfo = myQueueUpInfo;
			Intent intent = new Intent();
			intent.setClass(CaptureActivity.instance, MyQueueUp.class);
			intent.putExtra("myQueueUpInfo", myQueueUpInfo);
			CaptureActivity.instance.startActivity(intent);

		} else if (result == -1) {
			CaptureActivity.instance.finish();
		}
	}

	protected void onProgressUpdate(String... values) {
		Toast.makeText(CaptureActivity.instance, values[0].toString(), 3000).show();
	}
}