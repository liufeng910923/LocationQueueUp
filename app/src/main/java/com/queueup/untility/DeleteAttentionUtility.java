package com.queueup.untility;

import android.os.AsyncTask;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMessage;
import com.queueup.network.RMethod;
import com.queueup.ui.AttentionInfoActivity;
import com.queueup.ui.MyQueueUp;

public class DeleteAttentionUtility extends AsyncTask<String, String, Integer> {
	private String memberId;
	private String companyId;

	public DeleteAttentionUtility(String memberId,String companyId ) {
		this.memberId = memberId;
		this.companyId = companyId;
	}

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		try {

			RClient client = new RClient(AttentionInfoActivity.instance,
					Domain.cancelAttention);
			client.AddParam("MemberId", "" + memberId);
			client.AddParam("CompanyId", "" + companyId);
			client.Execute(RMethod.GET);

			// User Info
			if (client.getResult() == RConstants.OK) {

			} else {
				publishProgress(RMessage.getMsg(client.getResult(),
						AttentionInfoActivity.instance));
				ret = -1;
			}

		} catch (Exception ex) {
			publishProgress(ex.getMessage());
			ret = -1;
		}
		return ret;
	}

	protected void onPostExecute(Integer result) {
		if(result==1){
			Toast.makeText(AttentionInfoActivity.instance, "取消关注成功", 3000).show();
			AttentionInfoActivity.instance.finish();
			MyQueueUp.instance.finish();
		}else{
			Toast.makeText(AttentionInfoActivity.instance, "取消关注失败", 3000).show();
		}
		
	}

	protected void onProgressUpdate(String... values) {
		Toast.makeText(AttentionInfoActivity.instance, "异常 =" + values[0],
				Toast.LENGTH_LONG).show();
	}
	
}