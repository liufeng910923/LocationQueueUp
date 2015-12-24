/*package com.queueup.untility;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.entity.AttentionRestaurantInfo;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.ui.AttentionInfoActivity;
import com.queueup.ui.PersonAttentionActivity;
import com.queueup.ui.RestaurantActivity;
import com.queueup.ui.lib.CustomListView;

public class GetMyAttentionUtility extends AsyncTask<String, String, Integer> {
	private ArrayList<AttentionRestaurantInfo> attentionRestaurantInfos=new ArrayList<AttentionRestaurantInfo>();
	public CustomListView mListView;
	private Bitmap bm1;
	private String type;
	public GetMyAttentionUtility(CustomListView mListView) {
		this.mListView = mListView;
	}


	@SuppressWarnings("static-access")
	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		String memberId = params[0];
		String companyType = params[1];
		String lat = params[2];
		String lng = params[3];
		String firstResult = params[4];
		String maxResults = params[5];
		if ("餐厅".equals(companyType)) {
			type = "restaurant";
		} else if ("医院".equals(companyType)) {
			type = "hospital";
		}
		try {

			RClient client = new RClient(PersonAttentionActivity.instance, Domain.getMyAttention);
			client.AddParam("MemberId", memberId);
			client.AddParam("MemberAddressLongitude", lat);
			client.AddParam("MemberAddressLatitude", lng);
			client.AddParam("CompanyType", type);
			client.AddParam("FirstResult", firstResult);
			client.AddParam("MaxResults", maxResults);
			client.Execute(RMethod.GET);

			if (client.getResult() == RConstants.OK) {

				JSONObject jo = new JSONObject(client.getResponse());
				String content = jo.get("contents").toString();
				if ((content!=null)&&(!("".equals(content)))&&content!="null") {
					
				
				JSONArray jsonArray = new JSONArray(content);
				int jsonlength = jsonArray.length();
				for (int i = 0; i < jsonlength; i++) {
					JSONObject jsonObject1 = jsonArray.getJSONObject(i);

					Object companyId = jsonObject1.get("companyId");
					Object companyName = jsonObject1.get("companyName");
					Object pictureAddress = jsonObject1.get("pictureAddress");
					Object type = jsonObject1.get("companyType");
					Object distance = jsonObject1
							.get("distance");
					Object queueUpCount = jsonObject1
							.get("queueUpCount");
					Object companyAddress = jsonObject1
							.get("companyAddress");
					Object companySimpleIntro = jsonObject1
							.get("companySimpleIntro");
					Object companyFeature = jsonObject1
							.get("companyFeature");
					Object companyContract = jsonObject1
							.get("companyContract");
					if ((pictureAddress!=null)&&(!("".equals(pictureAddress)))&&pictureAddress!="null") {
						URL uil1 = new URL(jsonObject1.get("pictureAddress")
								.toString());
						InputStream is1 = uil1.openStream();
						this.bm1 = BitmapFactory.decodeStream(is1);
					}

					AttentionRestaurantInfo attentionRestaurantInfo = new AttentionRestaurantInfo();
					attentionRestaurantInfo.setCompanyId((String) companyId);
					attentionRestaurantInfo.setName((String) companyName);
					attentionRestaurantInfo.setPic(bm1);
					attentionRestaurantInfo.setCompanyType((String) companyType);
					attentionRestaurantInfo.setDistance((String) distance);
					attentionRestaurantInfo.setQueueup((String) queueUpCount);
					attentionRestaurantInfo.setAddress((String) companyAddress);
					attentionRestaurantInfo.setCompanyFeature(companyFeature.toString());
					attentionRestaurantInfo.setCompanyContract(companyContract.toString());
					attentionRestaurantInfo.setCompanySimpleIntro(companySimpleIntro.toString());

					restaurantInfos.add(restaurantInfo);
					PersonAttentionActivity.instance.attentionRestaurantInfos.add(attentionRestaurantInfo);
				}
				}else {
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
		RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(restaurantInfos,
				RestaurantActivity.instance);
		PersonAttentionActivity.instance.attentionRestaurantListAdapter.setDataNotifyChangerView(PersonAttentionActivity.instance.attentionRestaurantInfos);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(PersonAttentionActivity.instance,
						AttentionInfoActivity.class);
				intent.putExtra("attentionRestaurantInfo", PersonAttentionActivity.instance.attentionRestaurantInfos.get(position-1));
				PersonAttentionActivity.instance.startActivity(intent);
			}
		});
	}

	protected void onProgressUpdate(String... values) {
		Toast.makeText(PersonAttentionActivity.instance, "异常 = " + values[0],
				Toast.LENGTH_LONG).show();
	}
	
	public void SelectMenuItem(MenuItem item){
	}
}*/