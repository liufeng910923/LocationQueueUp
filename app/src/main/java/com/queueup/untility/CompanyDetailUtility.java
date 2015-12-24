package com.queueup.untility;

import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.Toast;
import com.queueup.conf.Domain;
import com.queueup.entity.AttentionRestaurantInfo;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.ui.AttentionInfoActivity;
import com.queueup.ui.MyQueueUp;

public class CompanyDetailUtility extends AsyncTask<String, String, Integer> {
	public static AttentionRestaurantInfo attentionRestaurantInfo;

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		String companyId = params[0];
		String companyType = params[1];
		try {

			RClient client = new RClient(MyQueueUp.instance,
					Domain.companyIntroduce);
			client.AddParam("CompanyId", companyId);
			client.AddParam("CompanyType", companyType);
			client.Execute(RMethod.GET);

			if (client.getResult() == RConstants.OK) {

				JSONObject jo = new JSONObject(client.getResponse());
				String content = jo.get("contents").toString();
				if ((content != null) && (!("".equals(content)))
						&& content != "null") {
					JSONObject contentJson = new JSONObject(content);
					Object compyId = contentJson.get("CompanyId");
					Object companyName = contentJson.get("CompanyName");
					Object pictureAddress = contentJson.get("PictureAddress");
					/* Object type = contentJson.get("CompanyType"); */
					/*
					 * Object distance = contentJson .get("distance");
					 */
					Object queueUpCount = contentJson.get("QueueUpCount");
					Object companyAddress = contentJson.get("CompanyAddress");
					Object companySimpleIntro = contentJson
							.get("CompanySimpleIntro");
					Object companyFeature = contentJson.get("CompanyFeature");
					Object companyContract = contentJson.get("CompanyContract");

					attentionRestaurantInfo = new AttentionRestaurantInfo();
					attentionRestaurantInfo.setCompanyId((String) compyId);
					attentionRestaurantInfo.setName((String) companyName);
					attentionRestaurantInfo.setPic((String) pictureAddress);
					/*
					 * attentionRestaurantInfo .setCompanyType((String)
					 * companyType);
					 */
					/* attentionRestaurantInfo.setDistance((String) distance); */
					attentionRestaurantInfo.setQueueup((String) queueUpCount);
					attentionRestaurantInfo.setAddress((String) companyAddress);
					attentionRestaurantInfo.setCompanyFeature(companyFeature
							.toString());
					attentionRestaurantInfo.setCompanyContract(companyContract
							.toString());
					attentionRestaurantInfo
							.setCompanySimpleIntro(companySimpleIntro
									.toString());

				} else {
					ret = -1;
				}
			} else {
				ret = -1;
			}

		} catch (Exception ex) {
			publishProgress(ex.getMessage());
			ret = -1;
		}
		return ret;
	}

	protected void onPostExecute(Integer result) {
		Intent intent = new Intent();
		intent.setClass(MyQueueUp.instance, AttentionInfoActivity.class);
		intent.putExtra("companydetail", attentionRestaurantInfo);
		MyQueueUp.instance.startActivity(intent);
	}

	protected void onProgressUpdate(String... values) {
		Toast.makeText(MyQueueUp.instance, "异常 = " + values[0],
				Toast.LENGTH_LONG).show();
	}

	public void SelectMenuItem(MenuItem item) {
	}
}