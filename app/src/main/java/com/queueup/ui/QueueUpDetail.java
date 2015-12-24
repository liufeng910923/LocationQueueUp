package com.queueup.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.queueup.adapters.QueueUpDetailListAdapter;
import com.queueup.conf.DemoApplication;
import com.queueup.conf.Domain;
import com.queueup.entity.QueueUpDetailInfo;
import com.queueup.network.HttpUtil;
import com.queueup.ui.lib.XListView;
import com.queueup.ui.lib.XListView.IXListViewListener;
import com.queueup.untility.CheckNetwork;
import com.queueup.untility.LogUtils;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class QueueUpDetail extends Activity implements IXListViewListener {
	private int firstResult = 0; // 初始记录
	private int maxResults = 10; // 记录数量
	private XListView mXListView;// 排队详情列表；
	TextView time;
	public List<QueueUpDetailInfo> queueUpDetailInfos = new ArrayList<QueueUpDetailInfo>();// 存放显示列表信息的数组；
	public QueueUpDetailListAdapter queueUpDetailListAdapter;// 适配器
	private final int LOADMORE = 001;
	private final int REFRESH = 002;
	public static Context instance;
	DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	String date = dateFormat.format(new java.util.Date());;// 获取当前时间
	String cid, count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queue_up_detail);
		mXListView = (XListView) this.findViewById(R.id.lv_list_detail);

		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(true);
		mXListView.setXListViewListener(this);

		mXListView.setRefreshTime(initData());
		time = (TextView) findViewById(R.id.time);
		time.setText("当前日期 : " + date.toString());
		instance = this;

		cid = getIntent().getStringExtra("cid");
		count = getIntent().getStringExtra("count");
		DemoApplication.getInstance().add(QueueUpDetail.this);
		if (requestData(REFRESH)) {
			LogUtils.d("requestData-->true");
		} else {
			LogUtils.d("requestData-->flase");
		}
	}

	private String initData() {
		// TODO Auto-generated method stub
		// 24小时
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return (hour + "点" + minute + "分" + second + "秒").trim();
	}

	private boolean requestData(final int model) {

		final String urlString = Domain.get_alreadyQueue + "?";
		final RequestParams mParams = new RequestParams();
		mParams.put("CompanyId", cid);
		mParams.put("AlreadyCount", count);
		mParams.put("FirstResult", String.valueOf(firstResult));
		mParams.put("MaxResults", String.valueOf(maxResults));

		HttpUtil.get(urlString, mParams, new AsyncHttpResponseHandler() {
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				LogUtils.d(urlString + mParams.toString().trim());
			}

			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);
				LogUtils.d("json" + arg1);
				if (initJson(arg1, model)) {
					LogUtils.d(queueUpDetailInfos.toString());
					initView(model);
				}

			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

		});

		return true;
	}

	private boolean initJson(String response, int model) {

		if (model == REFRESH) {
			// mRestaurantInfos = new ArrayList<RestaurantInfo>();
			queueUpDetailInfos.clear();
		}

		if (TextUtils.isEmpty(response)) {
			return false;
		}

		try {

			JSONObject jo = new JSONObject(response);
			String content = jo.get("contents").toString();
			JSONArray jsonArray = new JSONArray(content);
			int jsonlength = jsonArray.length();
			for (int i = 0; i < jsonlength; i++) {
				JSONObject jsonObject1 = jsonArray.getJSONObject(i);

				Object queueUpNum = jsonObject1.get("queueUpNum").toString();
				Object callNumTime = jsonObject1.get("callNumTime").toString();

				QueueUpDetailInfo queueDetailInfo = new QueueUpDetailInfo();

				queueDetailInfo = new QueueUpDetailInfo();
				queueDetailInfo.setEatTime(callNumTime.toString());
				queueDetailInfo.setQueueNo(queueUpNum.toString());
				queueUpDetailInfos.add(queueDetailInfo);

			}

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	private Handler mHandler = new Handler();

	/**
	 * ListView下拉刷新
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CheckNetwork.isOpenNetwork(QueueUpDetail.this)) {
					firstResult = 0;
					if (requestData(REFRESH)) {
						LogUtils.d("requestData-->true");

					} else {
						LogUtils.d("requestData-->flase");
					}
				} else {
					Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_LONG).show();
				}

				onLoad();
			}
		}, 1000);
	}

	/**
	 * ListView上拉刷新
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CheckNetwork.isOpenNetwork(QueueUpDetail.this)) {
					firstResult += maxResults;
					if (requestData(LOADMORE)) {
						LogUtils.d("requestData-->true");

					} else {
						LogUtils.d("requestData-->flase");
					}

				} else {
					Toast.makeText(getApplicationContext(), "网络未连接", Toast.LENGTH_LONG).show();
				}

				onLoad();
			}
		}, 1000);
	}

	/**
	 * ListView下拉、上拉刷新结束状态
	 */
	private void onLoad() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime(initData());
	}

	private void initView(int model) {
		queueUpDetailListAdapter = new QueueUpDetailListAdapter(this, queueUpDetailInfos);
		if (model == REFRESH) {
			mXListView.setAdapter(queueUpDetailListAdapter);
			int num = mXListView.getCount();
			if (num <= 10) {
				mXListView.setPullLoadEnable(false);
				// mXListView.setPullRefreshEnable(true);
			} else {
				mXListView.setPullLoadEnable(true);
			}
		} else if (model == LOADMORE) {
			queueUpDetailListAdapter.setDataNotifyChangerView(queueUpDetailInfos);
			mXListView.setSelection(queueUpDetailInfos.size() - 10);

			int num = mXListView.getCount();
			if (num <= 10) {
				mXListView.setPullLoadEnable(false);
				// mXListView.setPullRefreshEnable(true);
			} else {
				mXListView.setPullLoadEnable(true);
			}
		}

	}

	// 返回按钮
	public void backBtn(View v) {
		this.finish();
	}
}
