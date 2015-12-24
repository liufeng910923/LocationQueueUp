package com.queueup.untility;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.queueup.conf.Domain;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMessage;
import com.queueup.network.RMethod;
import com.queueup.ui.LoginActivity;
import com.queueup.ui.ResetPwdActivity;

public class ResetPwdUtility extends AsyncTask<String, String, Integer> {
	private String pass;
	private String phoneNo;
	private String msg;

	public ResetPwdUtility(String pass,String phoneNo) {
		this.pass = pass;
		this.phoneNo = phoneNo;
	}

	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		try {

			RClient client = new RClient(ResetPwdActivity.instance,
					Domain.reset_password);
			client.AddParam("LoginPwd", "" + pass);
			client.AddParam("LoginName", "" + phoneNo);
			client.Execute(RMethod.GET);
			if (client.getResult() == RConstants.OK) {
				msg = "密码修改成功";
			} else {
				msg = "密码修改失败";
				publishProgress(RMessage.getMsg(client.getResult(),
						ResetPwdActivity.instance));
				ret = -1;
			}

		} catch (Exception ex) {
			publishProgress(ex.getMessage());
			ret = -1;
		}
		return ret;
	}

	protected void onPostExecute(Integer result) {
		Toast.makeText(ResetPwdActivity.instance, msg, 3000).show();
		if(result==1){
			Intent intent = new Intent();
			intent.setClass(ResetPwdActivity.instance, LoginActivity.class);
			ResetPwdActivity.instance.startActivity(intent);
		}
	}

	protected void onProgressUpdate(String... values) {
		/*Toast.makeText(CollectInfoActivity.instance, "异常 = " + values[0],
				Toast.LENGTH_LONG).show();*/
	}
	
	
}