package com.queueup.untility;

import android.os.AsyncTask;

import com.queueup.conf.Domain;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMessage;
import com.queueup.network.RMethod;
import com.queueup.ui.MsgShareActivity;

public class InsertMesgShareUtility extends AsyncTask<String, String, Integer> {
	private String memberId;
	private String receiverName;
	private String receiverPhone;
	private String shareFlag;
	
	public InsertMesgShareUtility(String memberId,String receiverName, String receiverPhone, String shareFlag) {
	this.memberId = memberId;
	this.receiverName = receiverName;
	this.receiverPhone = receiverPhone;
	this.shareFlag = shareFlag;
	}

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		try {

			RClient client = new RClient(MsgShareActivity.instance, Domain.messageShare);
			client.AddParam("MemberId", memberId);
			client.AddParam("ReceiverName", receiverName);
			client.AddParam("ReceiverPhone", receiverPhone);
			client.AddParam("ShareFlag", "shareFlag");
			client.Execute(RMethod.GET);

			// User Info
			if (client.getResult() == RConstants.OK) {

			} else {
				publishProgress(RMessage.getMsg(client.getResult(),
						MsgShareActivity.instance));
				ret = -1;
			}

		} catch (Exception ex) {
			publishProgress(ex.getMessage());
			ret = -1;
		}
		return ret;
	}
}