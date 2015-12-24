package com.queueup.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.queueup.adapters.MyAdapter;
import com.queueup.baidumap.LocationOverlayDemo;
import com.queueup.conf.DemoApplication;
import com.queueup.conf.Domain;
import com.queueup.entity.RestaurantInfo;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RequestHttp;
import com.queueup.ui.lib.XListView;
import com.queueup.ui.lib.XListView.IXListViewListener;
import com.queueup.untility.CheckNetwork;
import com.queueup.untility.LogUtils;

public class RestaurantActivity extends Activity implements IXListViewListener {
	public String type; // 企业类型的值
	private String memberId; // 用户id
	/**
	 * 纬度
	 */
	private String lat;
	/**
	 * 纬度
	 */
	private String lng;
	private int firstResult = 0; // 初始记录
	private int maxResults = 10; // 记录数量
	private XListView mXListView;
	private List<RestaurantInfo> mRestaurantInfos = new ArrayList<RestaurantInfo>();
	private final int LOADMORE = 001;
	private final int REFRESH = 002;
	private int model = REFRESH;

	public static Context instance;
	/* private MyAdapter adapter; */
	private LinearLayout mLayout;
	private ProgressBar mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.d("生命周期");
		setContentView(R.layout.restaurant_list);
		DemoApplication.getInstance().add(RestaurantActivity.this);
		instance = this;
		mXListView = (XListView) this.findViewById(R.id.rest_lv);
		mProgressBar = (ProgressBar) this.findViewById(R.id.rest_proBar);
		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(true);
		mXListView.setXListViewListener(this);

		mXListView.setRefreshTime(initData());

		memberId = AndroidUtils.getSharedPreferences(this, "LOGIN", "LOGIN_ID");

		lat = String.valueOf(LocationOverlayDemo.mDianJiLantitude);
		lng = String.valueOf(LocationOverlayDemo.mDianJiLongitude);

		if (lat.equals("0.0") && lng.equals("0.0")) {
			LogUtils.e("不是点击POI点，所以获取定位的经纬度");
			lat = String.valueOf(LocationOverlayDemo.mCurrentLantitude);
			lng = String.valueOf(LocationOverlayDemo.mCurrentLongitude);

		}

		mXListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(RestaurantActivity.this, DetailInfoActivity.class);
				intent.putExtra("restaurantInfo", mRestaurantInfos.get(position - 1));
				startActivity(intent);
			}
		});

		mLayout = FirstActivity.mLayout;

		// LayoutParams布局,控制spinner显示的宽度和高度。
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final Spinner spinner = new Spinner(RestaurantActivity.this.getParent());
		spinner.setLayoutParams(params);

		spinner.setBackgroundResource(R.drawable.hometopleft);

		List<String> lists = new ArrayList<String>();
		lists.add("餐厅");
		lists.add("医院");

		// SpinnerAdapter spinadapter=new SpinnerAdapter(instance,lists);
		// spinner的适配器
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(instance, R.array.books, android.R.layout.simple_spinner_item);

		// set方法是来设置spinner中每个条目的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setSelection(0);
		spinner.setAdapter(adapter);

		// 类型选择
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				TextView tv = (TextView) view;
				tv.setTextColor(getResources().getColor(R.color.white)); // 设置颜色
				tv.setTextSize(12.5f); // 设置大小
				tv.setGravity(android.view.Gravity.CENTER); // 设置居中
				tv.setPadding(0, 10, 0, 0);

				// 获取一行数据
				type = instance.getResources().getStringArray(R.array.books)[position];
				requestData(type);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// linearlyout中添加spinner
		mLayout.addView(spinner);

	}

	/**
	 * Title: onStart
	 * 
	 * @Description:
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtils.d("生命周期");
	}

	/**
	 * Title: onStop
	 * 
	 * @Description:
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtils.d("生命周期");
	}

	/**
	 * Title: onPause
	 * 
	 * @Description:
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtils.d("生命周期");
	}

	/**
	 * Title: onResume
	 * 
	 * @Description:
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.d("生命周期");
	}

	/**
	 * Title: onDestroy
	 * 
	 * @Description:
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.d("生命周期");
	}

	/**
	 * Title: onRestart
	 * 
	 * @Description:
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtils.d("生命周期");
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

		if ("餐厅".equals(companyType)) {
			type = "restaurant";
		} else if ("医院".equals(companyType)) {
			type = "hospital";
		}

		String urlString = Domain.get_NearCompany + "?";
		RequestParams mParams = new RequestParams();
		mParams.put("MemberAddressLatitude", lat);
		mParams.put("MemberAddressLongitude", lng);
		mParams.put("FirstResult", String.valueOf(firstResult));
		mParams.put("MaxResults", String.valueOf(maxResults));
		mParams.put("MemberId", memberId);
		mParams.put("CompanyType", type);
		mParams.put("NearDistance", "100");

		new RequestHttp(mHandler, urlString, mParams, RequestHttp.GET);

	}

	/**
	 * 监听返回键，退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("restaurant - onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) { // && event.getRepeatCount() ==
			// 0
			AlertDialog.Builder builder = new Builder(RestaurantActivity.this.getParent());
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

	private boolean initJson(String response, int model) {

		if (model == REFRESH) {
			// mRestaurantInfos = new ArrayList<RestaurantInfo>();
			mRestaurantInfos.clear();
		}

		if (TextUtils.isEmpty(response)) {
			return false;
		}

		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("contents");
			for (int i = 0; i < jsonArray.length(); i++) {
				RestaurantInfo info = new RestaurantInfo();
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				info.setPictureAddress(jsonObject2.getString("pictureAddress"));
				info.setCompanyId(jsonObject2.getString("companyId"));
				info.setCompanyName(jsonObject2.getString("companyName"));
				info.setDistance(jsonObject2.getString("distance"));
				info.setCompanyType(jsonObject2.getString("companyType"));
				info.setQueueUpCount(jsonObject2.getString("queueUpCount"));
				info.setCompanyAddress(jsonObject2.getString("companyAddress"));
				info.setCompanySimpleIntro(jsonObject2.getString("companySimpleIntro"));
				info.setCompanyFeature(jsonObject2.getString("companyFeature"));
				info.setCompanyContract(jsonObject2.getString("companyContract"));
				info.setAttention(jsonObject2.getString("attention"));
				info.setLatitude(jsonObject2.getString("latitude"));
				info.setLongitude(jsonObject2.getString("longitude"));
				mRestaurantInfos.add(info);
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtils.e("catch:解析Json失败");
			return false;
		} finally {
			if (mRestaurantInfos.size() > 0) {
				// LogUtils.d(mRestaurantInfos.toString());
			} else {
				LogUtils.e("finally:解析Json失败");
				return false;
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				if (initJson(content, model)) {
					initView(model);
				}
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_NO:

				break;
			case RequestHttp.REQUESTDATA_FAILURE:
				Toast.makeText(instance, "暂时没有定位到点", Toast.LENGTH_LONG).show();
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
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CheckNetwork.isOpenNetwork(RestaurantActivity.this)) {
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
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CheckNetwork.isOpenNetwork(RestaurantActivity.this)) {
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
		MyAdapter adapter = new MyAdapter(this, mRestaurantInfos);
		if (model == REFRESH) {
			mXListView.setAdapter(adapter);
			int num = mXListView.getCount();
			if (num <= 10) {
				mXListView.setPullLoadEnable(false);
				// mXListView.setPullRefreshEnable(true);
			} else {
				mXListView.setPullLoadEnable(true);
			}
		} else if (model == LOADMORE) {
			adapter.setNotifyDataChange(mRestaurantInfos);
			mXListView.setSelection(mRestaurantInfos.size() - 10);

			int num = mXListView.getCount();
			if (num <= 10) {
				mXListView.setPullLoadEnable(false);
				// mXListView.setPullRefreshEnable(true);
			} else {
				mXListView.setPullLoadEnable(true);
			}
		}

	}

}
