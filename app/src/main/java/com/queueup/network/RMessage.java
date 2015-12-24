package com.queueup.network;

import android.content.Context;

import com.queueup.ui.R;

public class RMessage {
	public static String getMsg(RConstants code, Context context) {
		String msg = "";

		if (code == RConstants.ERROR_SERVER) {
			msg = context.getResources().getString(R.string.msg_error_server);
		} else if (code == RConstants.ERROR_CLIENT) {
			msg = context.getResources().getString(R.string.msg_error_client);
		} else if (code == RConstants.ERROR_UNKOWN) {
			msg = context.getResources().getString(R.string.msg_error_unknown);
		} else if (code == RConstants.NO_DATA) {
			msg = context.getResources().getString(R.string.msg_no_data);
		} else if (code == RConstants.Unauthorized) {
			msg = context.getResources().getString(R.string.msg_no_authority);
		}
		return msg;
	}
}
