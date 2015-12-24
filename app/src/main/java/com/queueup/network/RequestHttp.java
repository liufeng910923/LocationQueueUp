package com.queueup.network;

import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.queueup.untility.LogUtils;

public class RequestHttp {
	// private final String = "HTTP";

	/**
	 * 开始请求
	 */
	public static final int REQUESTDATA_START = 001;
	/**
	 * 请求连接成功
	 */
	public static final int REQUESTDATA_SUCCESS_OK = 002;
	/**
	 * 请求连接失败
	 */
	public static final int REQUESTDATA_SUCCESS_NO = 003;
	/**
	 * 请求失败
	 */
	public static final int REQUESTDATA_FAILURE = 004;
	/**
	 * 请求完成
	 */
	public static final int REQUESTDATA_FINISH = 005;

	/**
	 * Get请求
	 */
	public static final int GET = 006;
	/**
	 * Post请求
	 */
	public static final int POST = 007;

	private Handler mHandler;
	private RequestParams mParams;// 请求参数
	private String request_url;// 请求url地址

	public RequestHttp(Handler handler, String url) {
		this.mHandler = handler;
		this.request_url = url;
		requestGetData();
	}

	public RequestHttp(Handler handler, String url, RequestParams params, int request_type) {
		this.mHandler = handler;
		this.request_url = url;
		this.mParams = params;
		switch (request_type) {
		case GET:
			requestGetParamsData();
			break;
		case POST:
			requestPostParamsData();
			break;

		default:
			break;
		}
	}

	/**
	 * 普通Get请求，不加参数
	 */
	private void requestGetData() {

		HttpUtil.get(request_url, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				LogUtils.e("onStart()开始请求");
				LogUtils.w(request_url);
				mHandler.sendEmptyMessage(REQUESTDATA_START);

			}

			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);

				Message message = Message.obtain();
				if (arg0 == 200) {
					LogUtils.e("onSuccess()请求连接成功" + "　请求码：" + arg0);
					message.what = REQUESTDATA_SUCCESS_OK;
					message.obj = arg1;

					mHandler.sendMessage(message);

				} else {
					LogUtils.e("onSuccess()请求连接失败" + "　请求码：" + arg0);
					mHandler.sendEmptyMessage(REQUESTDATA_SUCCESS_NO);
				}

			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				LogUtils.e("onFailure()请求失败");
				mHandler.sendEmptyMessage(REQUESTDATA_FAILURE);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				LogUtils.e("onFinish()请求完成");
				mHandler.sendEmptyMessage(REQUESTDATA_FINISH);
			}
		});
	}

	/**
	 * Get请求，加参数
	 */
	private void requestGetParamsData() {

		HttpUtil.get(request_url, mParams, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				LogUtils.e("onStart()开始请求");
				LogUtils.w(request_url + mParams.toString().trim());
				mHandler.sendEmptyMessage(REQUESTDATA_START);

			}

			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);
				LogUtils.e("onSuccess()请求成功" + "　请求码：" + arg0);
				Message message = Message.obtain();
				if (arg0 == 200) {
					message.what = REQUESTDATA_SUCCESS_OK;
					message.obj = arg1;
					mHandler.sendMessage(message);

				} else {
					mHandler.sendEmptyMessage(REQUESTDATA_SUCCESS_NO);
				}

			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				LogUtils.e("onFailure()请求失败");
				mHandler.sendEmptyMessage(REQUESTDATA_FAILURE);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				LogUtils.e("onFinish()请求完成");
				mHandler.sendEmptyMessage(REQUESTDATA_FINISH);
			}
		});
	}

	/*********************************************************************************************/

	/**
	 * Post请求，提交表单
	 */
	private void requestPostParamsData() {

		HttpUtil.get(request_url, mParams, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				LogUtils.e("onStart()开始请求");
				LogUtils.w(request_url + mParams.toString().trim());
				mHandler.sendEmptyMessage(REQUESTDATA_START);

			}

			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);
				LogUtils.e("onSuccess()请求成功" + "　请求码：" + arg0);
				Message message = Message.obtain();
				if (arg0 == 200) {
					message.what = REQUESTDATA_SUCCESS_OK;
					message.obj = arg1;
					mHandler.sendMessage(message);
				} else {
					mHandler.sendEmptyMessage(REQUESTDATA_SUCCESS_NO);
				}

			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				LogUtils.e("onFailure()请求失败");
				mHandler.sendEmptyMessage(REQUESTDATA_FAILURE);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				LogUtils.e("onFinish()请求完成");
				mHandler.sendEmptyMessage(REQUESTDATA_FINISH);
			}
		});
	}
}
