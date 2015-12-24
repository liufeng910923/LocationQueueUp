package com.queueup.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.queueup.adapters.AttentionRestaurantListAdapter;
import com.queueup.conf.DemoApplication;
import com.queueup.conf.Domain;
import com.queueup.entity.AttentionRestaurantInfo;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RequestHttp;
import com.queueup.ui.lib.XListView;
import com.queueup.ui.lib.XListView.IXListViewListener;
import com.queueup.untility.CheckNetwork;
import com.queueup.untility.LogUtils;
import com.queueup.untility.MyAttentionDetailUtility;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class PersonAttentionActivity extends Activity implements IXListViewListener {

	private XListView mXListView;// 我的关注列表；
	private int firstResult = 0; // 初始记录
	private int maxResults = 10; // 记录数量
	public List<AttentionRestaurantInfo> attentionRestaurantInfos = new ArrayList<AttentionRestaurantInfo>();// 存放显示列表信息的数组；
	public static PersonAttentionActivity instance;
	String memberId, type;
	private final int LOADMORE = 001;
	private final int REFRESH = 002;
	private int model = REFRESH;
	View scaner;
	AttentionRestaurantInfo attentionRestaurantInfo;
	private ProgressBar mProgressBar;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attention_restaurant_list);
		System.out.println("====>oncreate");
		instance = this;

		scaner = (View) findViewById(R.id.scanner);

		// 扫描的监听
		scaner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(PersonAttentionActivity.this, CaptureActivity.class));
			}
		});

		DemoApplication.getInstance().add(PersonAttentionActivity.this);
		mXListView = (XListView) this.findViewById(R.id.attention_lv);
		mProgressBar = (ProgressBar) this.findViewById(R.id.attention_proBar);
		// mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(true);
		mXListView.setXListViewListener(this);

		mXListView.setRefreshTime(initData());

		memberId = AndroidUtils.getSharedPreferences(PersonAttentionActivity.instance, "LOGIN", "LOGIN_ID");

		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// // LayoutParams布局,控制spinner显示的宽度和高度。
		// @SuppressWarnings("deprecation")
		// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.FILL_PARENT);
		// final Spinner spinner = new
		// Spinner(PersonAttentionActivity.instance);
		// spinner.setLayoutParams(params);
		// spinner.setBackgroundResource(R.drawable.hometopleft);
		//
		// List<String> lists = new ArrayList<String>();
		// lists.add("餐厅");
		// lists.add("医院");
		//
		// // SpinnerAdapter spinadapter=new SpinnerAdapter(instance,lists);
		//
		// // spinner的适配器
		// ArrayAdapter<CharSequence> adapter =
		// ArrayAdapter.createFromResource(instance, R.array.books,
		// android.R.layout.simple_spinner_item);
		// // set方法是来设置spinner中每个条目的样式
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinner.setSelection(0);
		// spinner.setAdapter(adapter);
		//
		// // 类型选择
		// spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view, int
		// position, long id) {
		// TextView tv = (TextView) view;
		// tv.setTextColor(getResources().getColor(R.color.white)); // 设置颜色
		// tv.setTextSize(14.0f); // 设置大小
		// tv.setGravity(android.view.Gravity.CENTER); // 设置居中
		// tv.setPadding(0, 10, 0, 0);
		//
		// // 获取一行数据
		// type =
		// instance.getResources().getStringArray(R.array.books)[position];
		//
		// requestData(type);
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		//
		// }
		// });
		//
		// LinearLayout linearLt = (LinearLayout)
		// findViewById(R.id.attent_layout);
		//
		// // linearlyout中添加spinner
		// linearLt.addView(spinner);

		requestData("餐厅");

		mXListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				/*
				 * Intent intent = new Intent(PersonAttentionActivity.this,
				 * AttentionInfoActivity.class);
				 * intent.putExtra("attentionRestaurantInfo",
				 * attentionRestaurantInfos.get(position - 1));
				 * startActivity(intent);
				 */

				memberId = AndroidUtils.getSharedPreferences(PersonAttentionActivity.instance, "LOGIN", "LOGIN_ID");
				attentionRestaurantInfo = attentionRestaurantInfos.get(position - 1);

				new MyAttentionDetailUtility().execute(memberId.toString(), attentionRestaurantInfo.getCompanyId().toString(), attentionRestaurantInfo.getCompanyType().toString(),
						attentionRestaurantInfo.getMyQueueUpNum().toString());
			}
		});

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtils.d("onRestart");
		requestData("餐厅");
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

	private void requestData(String companyType) {
		// TODO Auto-generated method stub
		LogUtils.d("我的关注，请求数据");
		if ("餐厅".equals(companyType)) {
			type = "restaurant";
		} else if ("医院".equals(companyType)) {
			type = "hospital";
		}

		String urlString = Domain.getMyAttention + "?";
		RequestParams mParams = new RequestParams();
		mParams.put("MemberId", memberId);
		mParams.put("CompanyType", type);
		mParams.put("FirstResult", String.valueOf(firstResult));
		mParams.put("MaxResults", String.valueOf(maxResults));

		new RequestHttp(mHandler, urlString, mParams, RequestHttp.GET);
	}

	private boolean initJson(String response, int model) {

		if (model == REFRESH) {
			// mRestaurantInfos = new ArrayList<RestaurantInfo>();
			attentionRestaurantInfos.clear();
		}

		if (TextUtils.isEmpty(response)) {
			return false;
		}

		try {

			JSONObject jo = new JSONObject(response);
			String content = jo.get("contents").toString();
			if ((content != null) && (!("".equals(content))) && content != "null") {

				JSONArray jsonArray = new JSONArray(content);
				int jsonlength = jsonArray.length();
				for (int i = 0; i < jsonlength; i++) {
					JSONObject jsonObject1 = jsonArray.getJSONObject(i);

					Object companyId = jsonObject1.get("companyId"); // 企业id
					Object companyName = jsonObject1.get("companyName"); // 企业名称
					Object pictureAddress = jsonObject1.get("pictureAddress"); // 图片下载地址
					Object type = jsonObject1.get("companyType"); // 企业类型
					Object companyAddress = jsonObject1.get("companyAddress"); // 企业地址

					Object queueStatus = jsonObject1.get("queueStatus");// 是否已排队
					Object queueUpCount = jsonObject1.get("queueUpCount"); // 企业排队人数

					Object myQueueUpNum = jsonObject1.get("myQueueUpNum");// 我的排队号码

					attentionRestaurantInfo = new AttentionRestaurantInfo();
					attentionRestaurantInfo.setCompanyId((String) companyId);
					attentionRestaurantInfo.setName((String) companyName);
					attentionRestaurantInfo.setPic((String) pictureAddress);
					attentionRestaurantInfo.setCompanyType((String) type);
					attentionRestaurantInfo.setAddress((String) companyAddress);
					attentionRestaurantInfo.setQueueStatus(queueStatus.toString());
					attentionRestaurantInfo.setQueueup((String) queueUpCount);
					attentionRestaurantInfo.setMyQueueUpNum(myQueueUpNum.toString());

					attentionRestaurantInfos.add(attentionRestaurantInfo);

				}
			}

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				if (initJson(content, model)) {
					/*
					 * if (attentionRestaurantInfos.size() > 10) {
					 * mXListView.setPullLoadEnable(true); }else {
					 * mXListView.setPullLoadEnable(false); }
					 */
					initView(model);
				}
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_NO:

				break;
			case RequestHttp.REQUESTDATA_FAILURE:
				Toast.makeText(instance, "请检查网络连接", Toast.LENGTH_LONG).show();

				break;
			case RequestHttp.REQUESTDATA_FINISH:
				mProgressBar.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * ListView下拉刷新
	 */
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CheckNetwork.isOpenNetwork(PersonAttentionActivity.this)) {
					firstResult = 0;
					model = REFRESH;
					requestData(type);
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
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CheckNetwork.isOpenNetwork(PersonAttentionActivity.this)) {
					firstResult += maxResults;
					model = LOADMORE;
					requestData(type);

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
		AttentionRestaurantListAdapter attentionRestaurantListAdapter
			= new AttentionRestaurantListAdapter(this, attentionRestaurantInfos);
		if (model == REFRESH) {
			mXListView.setAdapter(attentionRestaurantListAdapter);
			int num = mXListView.getCount();
			if (num <= 10) {
				mXListView.setPullLoadEnable(false);
				// mXListView.setPullRefreshEnable(true);
			} else {
				mXListView.setPullLoadEnable(true);
			}
		} else if (model == LOADMORE) {
			attentionRestaurantListAdapter.setDataNotifyChangerView(attentionRestaurantInfos);
			mXListView.setSelection(attentionRestaurantInfos.size() - 10);
			int num = mXListView.getCount();
			if (num <= 10) {
				mXListView.setPullLoadEnable(false);
				// mXListView.setPullRefreshEnable(true);
			} else {
				mXListView.setPullLoadEnable(true);
			}
		}

	}

	/**
	 * 监听返回键，退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("PersonAttentionActivity  onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) { // && event.getRepeatCount() ==
			// 0
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage("确定要退出吗?");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					DemoApplication.getInstance().exit();
				}
			});
			builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		return true;
	}

}
