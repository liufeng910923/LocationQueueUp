package com.queueup.untility;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;
import com.queueup.conf.Domain;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RClient;
import com.queueup.network.RConstants;
import com.queueup.network.RMethod;
import com.queueup.ui.Guidance;
import com.queueup.ui.LoginActivity;
import com.queueup.ui.MainActivity;
import com.queueup.ui.WelcomeView;

public class UpdateVersionInfoUtility extends
		AsyncTask<String, String, Integer> {
	private String verCode;
	private String newCode = "";
	public ProgressDialog pBar;
	private Context mContext;

	public UpdateVersionInfoUtility(String verCode, Context mContext) {
		this.verCode = verCode;
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		try {

			RClient client = new RClient(mContext, Domain.versionUpdate);
			client.AddParam("CurrentVersion", String.valueOf(verCode));
			client.Execute(RMethod.GET);
			if (client.getResult() == RConstants.OK) {

				JSONObject jo = new JSONObject(client.getResponse());
				String content = jo.get("contents").toString();
				JSONObject contentJson = new JSONObject(content);
				String needUpdate = contentJson.get("NeedUpdate").toString();
				if ("true".equals(needUpdate)) {
					newCode = contentJson.get("LatestVersion").toString();
					ret = 2;
				}
			}

		} catch (Exception ex) {
			publishProgress(ex.getMessage());
			ret = -1;
		}
		return ret;
	}

	public Dialog dialog;

	protected void onPostExecute(Integer result) {
		if (result == 2) {
			dialog = new AlertDialog.Builder(WelcomeView.instance)
					.setTitle("软件更新")
					.setMessage("发现新版本:" + newCode)
					// 设置内容
					.setPositiveButton("更新",// 设置确定按钮
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									pBar = new ProgressDialog(
											WelcomeView.instance);
									pBar.setTitle("正在下载");
									pBar.setMessage("请稍候...");
									pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
									pBar.show();
									// TODO START
									UpdateToNewVersionUtility updateToNewVersionUtility = new UpdateToNewVersionUtility(
											"");
									updateToNewVersionUtility.execute();

								}

							})
					.setNegativeButton("暂不更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 点击"取消"按钮之后退出程序

									Intent intent;

									SharedPreferences preferences = WelcomeView.instance
											.getSharedPreferences(
													Guidance.SHAREDPREFERENCES_NAME,
													Context.MODE_PRIVATE);
									boolean is = preferences.getBoolean(
											"isFirstIn", false);// 获取是否第一次启动

									// 判断是否第一次登录

									if (!is) {
										LogUtils.d("是第一次登录");
										intent = new Intent(
												WelcomeView.instance,
												Guidance.class);
									} else {
										LogUtils.d("不是第一次登录");
										String flg = AndroidUtils
												.getSharedPreferences(
														WelcomeView.instance,
														"LOGIN", "FLG");// 获取是否登录

										// 判断是否登录成功

										if (flg != null && "TRUE".equals(flg)) {

											intent = new Intent(
													WelcomeView.instance,
													MainActivity.class);

										} else {
											intent = new Intent(
													WelcomeView.instance,
													LoginActivity.class);
										}

									}

									WelcomeView.instance.startActivity(intent);

									WelcomeView.instance.finish();
								}
							}).create();// 创建
			// 显示对话框
			dialog.show();
		} else {
			Intent intent;

			SharedPreferences preferences = WelcomeView.instance
					.getSharedPreferences(
							Guidance.SHAREDPREFERENCES_NAME,
							Context.MODE_PRIVATE);
			boolean is = preferences.getBoolean(
					"isFirstIn", false);// 获取是否第一次启动

			// 判断是否第一次登录

			if (!is) {
				LogUtils.d("是第一次登录");
				intent = new Intent(
						WelcomeView.instance,
						Guidance.class);
			} else {
				LogUtils.d("不是第一次登录");
				String flg = AndroidUtils
						.getSharedPreferences(
								WelcomeView.instance,
								"LOGIN", "FLG");// 获取是否登录

				// 判断是否登录成功

				if (flg != null && "TRUE".equals(flg)) {

					intent = new Intent(
							WelcomeView.instance,
							MainActivity.class);

				} else {
					intent = new Intent(
							WelcomeView.instance,
							LoginActivity.class);
				}

			}

			WelcomeView.instance.startActivity(intent);
			WelcomeView.instance.finish();
		}
	}

	protected void onProgressUpdate(String... values) {
		Toast.makeText(WelcomeView.instance, values[0], Toast.LENGTH_LONG)
				.show();

	}
}