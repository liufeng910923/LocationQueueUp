package com.queueup.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.queueup.conf.DemoApplication;
import com.queueup.conf.Domain;
import com.queueup.entity.DiscountInfo;
import com.queueup.network.AndroidUtils;
import com.queueup.network.RequestHttp;
import com.queueup.push.ExampleUtil;
import com.queueup.ui.lib.XListView;
import com.queueup.ui.lib.XListView.IXListViewListener;
import com.queueup.untility.LogUtils;
import com.queueup.untility.ViewHolder;

/**
 * @Title:DiscountActivity.java
 * @Description:TODO
 * @Author:SimonYang
 * @Date 2014-9-16 下午5:25:22
 */
public class DiscountActivity extends Activity implements IXListViewListener {

	private ViewPager viewPager;

	private Button mWeiShiYongBtn, mYiShiYongBtn, mYiGuoQiBtn;

	private List<View> pagerViews;
	private View view1, view2, view3;
	private XListView listview1, listview2, listview3;
	private MyAdapter1 myAdapter1;
	private MyAdapter1 myAdapter2;
	private MyAdapter1 myAdapter3;
	public static DiscountActivity instance;
	private Handler mHandler;

	private String memberId;
	private List<DiscountInfo> mUnusedDiscount = new ArrayList<DiscountInfo>();
	private List<DiscountInfo> mUsedDiscount = new ArrayList<DiscountInfo>();
	private List<DiscountInfo> mOverdueDiscount = new ArrayList<DiscountInfo>();
	private boolean isUnused;
	private boolean isUsed;
	private boolean isOverdue;
	private int xuLieHaoUnused = 0;
	private int xuLieHaoUsed = 0;
	private int xuLieHaoOverdue = 0;

	private int firstResult = 0; // 初始记录
	private int maxResults = 10; // 记录数量
	DiscountInfo dInfo;

	public static boolean isForeground = false;
	private static final int MSG_SET_TAGS = 1001;
	public static Handler mjushHandler = new Handler() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_TAGS:
				Set set = (Set<String>) msg.obj;
				System.out.println("set.size()==>" + set.size());

				// 调用JPush API设置Tag
				JPushInterface.setAliasAndTags(DiscountActivity.instance, null,
						(Set<String>) msg.obj, mTagsCallback);
				LogUtils.d("tag绑定成功");
				break;

			default:

			}
		}
	};

	private Handler mUnusedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				System.out.println("content" + content);
				initJson1(content);
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_NO:

				break;
			case RequestHttp.REQUESTDATA_FAILURE:

				break;
			case RequestHttp.REQUESTDATA_FINISH:

				LogUtils.d("开始请求已使用优惠卷");
				requestData2("used");

				break;

			default:
				break;
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler mUsedHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				System.out.println("content" + content);
				initJson2(content);
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_NO:

				break;
			case RequestHttp.REQUESTDATA_FAILURE:

				break;
			case RequestHttp.REQUESTDATA_FINISH:

				LogUtils.d("开始请求已过期优惠卷");
				requestData3("overdue");

				break;

			default:
				break;
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler mOverdueHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				System.out.println("content" + content);
				initJson3(content);
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_NO:

				break;
			case RequestHttp.REQUESTDATA_FAILURE:

				break;
			case RequestHttp.REQUESTDATA_FINISH:

				myAdapter1 = new MyAdapter1(instance, mUnusedDiscount);
				myAdapter2 = new MyAdapter1(instance, mUsedDiscount);
				myAdapter3 = new MyAdapter1(instance, mOverdueDiscount);

				listview1.setAdapter(myAdapter1);
				listview2.setAdapter(myAdapter2);
				listview3.setAdapter(myAdapter3);

				break;

			default:
				break;
			}
		};
	};

	// 使用优惠券 2014.9.23
	@SuppressLint("HandlerLeak")
	private Handler useCouponHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				System.out.println("content" + content);
				initJson4(content);
				break;
			case RequestHttp.REQUESTDATA_SUCCESS_NO:

				break;
			case RequestHttp.REQUESTDATA_FAILURE:

				break;
			case RequestHttp.REQUESTDATA_FINISH:

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_layout);
		instance = this;
		DemoApplication.getInstance().add(DiscountActivity.this);
		memberId = AndroidUtils.getSharedPreferences(
				PersonAttentionActivity.instance, "LOGIN", "LOGIN_ID");
		mHandler = new Handler();
		initControl();
		initViewPager();
		InitTextView();
		listview1.setPullLoadEnable(false);
		listview1.setPullRefreshEnable(true);
		listview1.setXListViewListener(this);
		listview2.setPullLoadEnable(false);
		listview2.setPullRefreshEnable(true);
		listview2.setXListViewListener(this);
		listview3.setPullLoadEnable(false);
		listview3.setPullRefreshEnable(true);
		listview3.setXListViewListener(this);

		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				/*
				 * if (!isUnused) { xuLieHaoUnused = getRandomNum(999999999,
				 * 111111111, position); isUnused = true; }
				 */

				/*
				 * Toast.makeText(instance, "优惠卷使用序列号:" + xuLieHaoUnused,
				 * Toast.LENGTH_SHORT).show();
				 */
				String couponId = mUnusedDiscount.get(position - 1)
						.getCouponId().toString();
				String companyId = mUnusedDiscount.get(position - 1)
						.getCompanyId().toString();
				initAlerDialog(couponId, companyId);
			}
		});
		listview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * if (!isUsed) { xuLieHaoUsed = getRandomNum(999999999,
				 * 111111111, position); isUsed = true; }
				 */

				Toast.makeText(instance, "优惠券您已经使用！", Toast.LENGTH_SHORT)
						.show();
			}
		});
		listview3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * if (!isOverdue) { xuLieHaoOverdue = getRandomNum(999999999,
				 * 111111111, position); isOverdue = true; }
				 */

				Toast.makeText(instance, "此优惠券已经过期！", Toast.LENGTH_SHORT)
						.show();
			}
		});

		String tag = AndroidUtils.getSharedPreferences(this, "LOGIN",
				"LOGIN_ID");
		tag = tag + "1";
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(this, "tag不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(this, "格式不对", Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}
		mjushHandler.sendMessage(mjushHandler.obtainMessage(MSG_SET_TAGS,
				tagSet));

		registerMessageReceiver(); // used for receive msg

	}

	/*
	 * private int getRandomNum(int max, int min, int postion) { int s = new
	 * Random().nextInt(max) % (max - min + postion + 1) + min;
	 * 
	 * return s; }
	 */

	private void initControl() {
		viewPager = (ViewPager) findViewById(R.id.pvr_user_pager);
		viewPager.setOffscreenPageLimit(2);/* 预加载页面 */
	}

	private void initAlerDialog(final String couponId, final String companyId) {
		AlertDialog.Builder builder = new Builder(DiscountActivity.this);

		builder.setMessage("优惠券序列号:" + couponId + "\n您确定使用?");
		builder.setTitle("提示");

		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						requestData4(couponId, companyId);

					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/* 初始化ViewPager */
	private void initViewPager() {
		pagerViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		view1 = mInflater.inflate(R.layout.discount_listview_lay, null);
		view2 = mInflater.inflate(R.layout.discount_listview_lay, null);
		view3 = mInflater.inflate(R.layout.discount_listview_lay, null);
		pagerViews.add(view1);
		pagerViews.add(view2);
		pagerViews.add(view3);
		viewPager.setAdapter(new MyPagerAdapter(pagerViews));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(myOnPageChangeListener);
		initList();
	}

	/* 初始化页卡标题 */
	public void InitTextView() {
		mWeiShiYongBtn = (Button) findViewById(R.id.weishiyong);
		mWeiShiYongBtn.setTextColor(getResources().getColor(
				R.color.YouHuiJuanTitleTextColor2));
		mYiShiYongBtn = (Button) findViewById(R.id.yishiyong);
		mYiGuoQiBtn = (Button) findViewById(R.id.yiguoqi);

		mWeiShiYongBtn.setOnClickListener(new MyOnClickListener(0));
		mYiShiYongBtn.setOnClickListener(new MyOnClickListener(1));
		mYiGuoQiBtn.setOnClickListener(new MyOnClickListener(2));
	}

	/* 初始化各个页卡列表 */
	private void initList() {

		listview1 = (XListView) view1.findViewById(R.id.discount_mylistview);
		listview2 = (XListView) view2.findViewById(R.id.discount_mylistview);
		listview3 = (XListView) view3.findViewById(R.id.discount_mylistview);

		LogUtils.d("开始请求未使用优惠卷");

		requestData1("unused");

	}

	private void onLoad() {

		listview1.stopRefresh();
		listview1.stopLoadMore();
		listview1.setRefreshTime(initData());

		listview2.stopRefresh();
		listview2.stopLoadMore();
		listview2.setRefreshTime(initData());

		listview3.stopRefresh();
		listview3.stopLoadMore();
		listview3.setRefreshTime(initData());

		// viewUp(mUnusedDiscount, mUsedDiscount, mOverdueDiscount);

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

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				firstResult = 0;
				LogUtils.d("开始请求未使用优惠卷");
				requestData1("unused");

				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				onLoad();
			}
		}, 2000);
	}

	private void requestData1(String discountType) {

		String urlString = Domain.getDiscountInfo + "?";
		RequestParams mParams = new RequestParams();
		mParams.put("MemberId", memberId);
		mParams.put("CouponsStatus", discountType);
		mParams.put("FirstResult", String.valueOf(firstResult));
		mParams.put("MaxResults", String.valueOf(maxResults));

		// 需要传进去一个Handler
		new RequestHttp(mUnusedHandler, urlString, mParams, RequestHttp.GET);
	}

	private void requestData2(String discountType) {

		String urlString = Domain.getDiscountInfo + "?";
		RequestParams mParams = new RequestParams();
		mParams.put("MemberId", memberId);
		mParams.put("CouponsStatus", discountType);
		mParams.put("FirstResult", String.valueOf(firstResult));
		mParams.put("MaxResults", String.valueOf(maxResults));

		// 需要传进去一个Handler
		new RequestHttp(mUsedHandler, urlString, mParams, RequestHttp.GET);
	}

	private void requestData3(String discountType) {

		String urlString = Domain.getDiscountInfo + "?";
		RequestParams mParams = new RequestParams();
		mParams.put("MemberId", memberId);
		mParams.put("CouponsStatus", discountType);
		mParams.put("FirstResult", String.valueOf(firstResult));
		mParams.put("MaxResults", String.valueOf(maxResults));

		// 需要传进去一个Handler
		new RequestHttp(mOverdueHandler, urlString, mParams, RequestHttp.GET);
	}

	// 使用优惠券 2014.9.23
	private void requestData4(String couponId, String companyId) {

		String urlString = Domain.useCouponInfo + "?";
		RequestParams mParams = new RequestParams();
		mParams.put("MemberId", memberId);
		mParams.put("CouponId", couponId);
		mParams.put("CompanyId", companyId);

		// 需要传进去一个Handler
		new RequestHttp(useCouponHandler, urlString, mParams, RequestHttp.GET);
	}

	private void initJson1(String jsonString) {

		if (TextUtils.isEmpty(jsonString)) {
			return;
		}

		LogUtils.d("解析未使用");
		mUnusedDiscount = new ArrayList<DiscountInfo>();
		try {
			JSONObject jsonObject = new org.json.JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("contents");
			DiscountInfo dInfo;
			for (int i = 0; i < jsonArray.length(); i++) {
				dInfo = new DiscountInfo();
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				dInfo.setCouponsAddress(jsonObject2.getString("couponsAddress")); // 优惠券图片地址
				dInfo.setCompanyId(jsonObject2.getString("companyId")); // 企业ID
				dInfo.setCouponId(jsonObject2.getString("couponId")); // 优惠券ID
				mUnusedDiscount.add(dInfo);
			}

			LogUtils.e(mUnusedDiscount.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initJson2(String jsonString) {

		if (TextUtils.isEmpty(jsonString)) {
			return;
		}

		LogUtils.d("解析已使用");
		mUsedDiscount = new ArrayList<DiscountInfo>();
		try {
			JSONObject jsonObject = new org.json.JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("contents");
			DiscountInfo dInfo;
			for (int i = 0; i < jsonArray.length(); i++) {
				dInfo = new DiscountInfo();
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				dInfo.setCouponsAddress(jsonObject2.getString("couponsAddress")); // 优惠券图片地址
				dInfo.setCompanyId(jsonObject2.getString("companyId")); // 企业ID
				dInfo.setCouponId(jsonObject2.getString("couponId")); // 优惠券ID
				mUsedDiscount.add(dInfo);
			}

			LogUtils.e(mUsedDiscount.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initJson3(String jsonString) {

		if (TextUtils.isEmpty(jsonString)) {
			return;
		}

		LogUtils.d("解析已过期");
		mOverdueDiscount = new ArrayList<DiscountInfo>();
		try {
			JSONObject jsonObject = new org.json.JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("contents");
			DiscountInfo dInfo;
			for (int i = 0; i < jsonArray.length(); i++) {
				dInfo = new DiscountInfo();
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				dInfo.setCouponsAddress(jsonObject2.getString("couponsAddress")); // 优惠券图片地址
				dInfo.setCompanyId(jsonObject2.getString("companyId")); // 企业ID
				dInfo.setCouponId(jsonObject2.getString("couponId")); // 优惠券ID
				mOverdueDiscount.add(dInfo);
			}

			LogUtils.e(mOverdueDiscount.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 使用优惠券 解析数据 2014.9.23
	 */

	private void initJson4(String jsonString) {

		if (TextUtils.isEmpty(jsonString)) {
			return;
		}

		try {
			JSONObject jsonObject = new org.json.JSONObject(jsonString);
			String content = jsonObject.getString("contents");
			JSONObject contentJson = new JSONObject(content);
			String canUse = contentJson.get("CanUse").toString();
			String sendSuccess = contentJson.get("SendSuccess").toString();
			if ("true".equals(canUse)) {
				if ("true".equals(sendSuccess)) {
					Toast.makeText(this, "申请使用优惠券成功!", Toast.LENGTH_SHORT)
							.show();

				} else {
					Toast.makeText(this, "向企业端推送消息失败!", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "今天你未在此企业排队,无法使用优惠券!", Toast.LENGTH_SHORT)
						.show();
			}
			return;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		requestData1("unused");
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 页卡切换监听
	 * */

	public OnPageChangeListener myOnPageChangeListener = new OnPageChangeListener() {

		@Override
		// 页面切换后：
		public void onPageSelected(int arg0) {

			switch (arg0) {
			case 0:
				mWeiShiYongBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor2));
				mYiShiYongBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor1));
				mYiGuoQiBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor1));
				break;
			case 1:
				mWeiShiYongBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor1));
				mYiShiYongBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor2));
				mYiGuoQiBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor1));
				break;
			case 2:
				mWeiShiYongBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor1));
				mYiShiYongBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor1));
				mYiGuoQiBtn.setTextColor(getResources().getColor(
						R.color.YouHuiJuanTitleTextColor2));
				break;
			default:
				break;
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

	};

	/* 标题点击监听 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	/**
	 * 监听返回键，退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("more - onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) { // && event.getRepeatCount() ==
			// 0
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage("确定要退出吗?");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DemoApplication.getInstance().exit();
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			// startActivity(new Intent(MoreActivity.this,
			// ExitDialogView.class));
		}
		return true;
	}

	private class MyAdapter1 extends BaseAdapter {
		private List<DiscountInfo> list;
		private LayoutInflater inflater;

		public MyAdapter1(Context mContext, List<DiscountInfo> list) {
			this.list = list;
			inflater = LayoutInflater.from(mContext);
			LogUtils.e("开始配置适配器");

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (list == null) {
				LogUtils.e("list == null");
				return 0;
			} else {
				return list.size();
			}

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.discount_listview_item,
						parent, false);
			}

			final ImageView imageView = ViewHolder.get(convertView,
					R.id.discount_item_img);

			DiscountInfo info = list.get(position);
			String imgUrl = info.getCouponsAddress();
			// String imgUrl = list.get(position);

			if (!TextUtils.isEmpty(imgUrl)) {
				ImageLoader.getInstance().displayImage(imgUrl, imageView,
						DemoApplication.options,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								String message = null;
								switch (failReason.getType()) {
								case IO_ERROR:
									message = "下载错误";
									break;
								case DECODING_ERROR:
									message = "图片无法显示";
									break;
								case NETWORK_DENIED:
									message = "网络有问题，无法下载";
									break;
								case OUT_OF_MEMORY:
									message = "图片太大无法显示";
									break;
								case UNKNOWN:
									message = "未知的错误";
									break;
								}
								LogUtils.e(message);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {

								imageView.setScaleType(ScaleType.FIT_XY);
								imageView.setImageBitmap(loadedImage);

							}
						});

			}

			return convertView;
		}

	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				try {
					JSONObject contentJson = new JSONObject(messge);
					String couponId = contentJson.get("CouponId").toString();

					StringBuilder showMsg = new StringBuilder();
					showMsg.append(couponId);

					if (!ExampleUtil.isEmpty(extras)) {
						showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
					}
					setCostomMsg(showMsg.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void setCostomMsg(String couponId) {
		Toast.makeText(instance, "您的优惠券  " + couponId + " 使用成功!",
				Toast.LENGTH_SHORT).show();
		requestData1("unused");
	}

	private final static TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";

				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";

				if (ExampleUtil.isConnected(DiscountActivity.instance)) {
					mjushHandler.sendMessageDelayed(
							mjushHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {

				}
				break;

			default:
				logs = "Failed with errorCode = " + code;

			}

			ExampleUtil.showToast(logs, DiscountActivity.instance);
		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

}
