package com.queueup.ui;

import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.queueup.conf.DemoApplication;
import com.queueup.entity.MyQueueUpInfo;
import com.queueup.network.AndroidUtils;
import com.queueup.push.ExampleUtil;
import com.queueup.untility.CompanyDetailUtility;
import com.queueup.untility.LogUtils;
import com.zijunlin.Zxing.Demo.CaptureActivity;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class MyQueueUp extends InstrumentedActivity {
	@SuppressWarnings("unused")
	private final String TAG = "ABC";
	public static MyQueueUp instance;
	public MyQueueUpInfo myQueueUpInfo;
	Button check_detail;
	TextView time, queueup_name, queueup_address, now_no, queue_count, my_no,
			end_no, queueup_name_title;
	public ImageView queueup_image;
	String cid, count;
	RelativeLayout rl_queue_count;
	TextView top, queueup_phone;

	public static boolean isForeground = false;
	private static final int MSG_SET_TAGS = 1001;
	public static Handler mHandler = new Handler() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_TAGS:
				Set set = (Set<String>) msg.obj;
				System.out.println("set.size()==>" + set.size());

				// 调用JPush API设置Tag
				JPushInterface.setAliasAndTags(WelcomeView.instance, null,
						(Set<String>) msg.obj, mTagsCallback);
				LogUtils.d("tag绑定成功");
				break;

			default:

			}
		}
	};

	// 下载图片
	public void DownLoad() {
		if (!TextUtils.isEmpty(myQueueUpInfo.getPictureAddress())) {
			ImageLoader.getInstance().displayImage(
					myQueueUpInfo.getPictureAddress(), queueup_image,
					DemoApplication.options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@SuppressWarnings("unused")
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
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
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							// mImageView.setScaleType(ScaleType.FIT_XY);
							// mImageView.setImageBitmap(loadedImage);

						}
					});
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_queueup);
		instance = this;
		DemoApplication.getInstance().add(MyQueueUp.this);
		initview();
		// 页面显示的字段赋值
		myQueueUpInfo = (MyQueueUpInfo) getIntent().getSerializableExtra(
				"myQueueUpInfo");

		DownLoad();
		ImageLoader.getInstance().displayImage(
				myQueueUpInfo.getPictureAddress(), queueup_image,
				DemoApplication.options);
		queueup_name.setText(myQueueUpInfo.getCompanyName());
		queueup_address.setText(myQueueUpInfo.getCompanyAddress());
		queueup_phone.setText(myQueueUpInfo.getCompanyContract());

		if (myQueueUpInfo.getCurrentNumber() == null
				|| TextUtils.isEmpty(myQueueUpInfo.getCurrentNumber())) {
			now_no.setText("无");
		} else {
			now_no.setText(myQueueUpInfo.getCurrentNumber());
		}

		String overdue = myQueueUpInfo.getOverdue();

		if ("dateOverdue".equals(overdue)) {
			rl_queue_count.setVisibility(View.GONE);
			top = (TextView) findViewById(R.id.condition);
			top.setVisibility(View.VISIBLE);
			top.setText("您的排队号码的日期已过!");
		
		} else if ("numberOverdue".equals(overdue)) {

			rl_queue_count.setVisibility(View.GONE);
			top = (TextView) findViewById(R.id.condition);
			top.setVisibility(View.VISIBLE);
			top.setText("您今天的排队号码已经过期,请重新取号!");

		} else if ("false".equals(overdue)) {

			queue_count.setText(myQueueUpInfo.getQueueUpCount());

		} else {
			if ("".equals(myQueueUpInfo.getMyQueueUpNum())) {
				rl_queue_count.setVisibility(View.GONE);
				top = (TextView) findViewById(R.id.condition);
				top.setVisibility(View.VISIBLE);
				top.setText("系统没有您在本店排队的信息,请扫描您的二维码!");
			} else {
				queue_count.setText(myQueueUpInfo.getQueueUpCount());
			}

		}

		if (myQueueUpInfo.getMyQueueUpNum() == null
				|| TextUtils.isEmpty(myQueueUpInfo.getMyQueueUpNum())) {
			my_no.setText("无");
		} else {
			my_no.setText(myQueueUpInfo.getMyQueueUpNum());
		}

		/*end_no.setText(myQueueUpInfo.getAlreadyCount());*/
		queueup_name_title.setText(myQueueUpInfo.getCompanyName());
		cid = myQueueUpInfo.getCompanyId().toString();
		count = myQueueUpInfo.getAlreadyCount().toString();

		String tag = AndroidUtils.getSharedPreferences(this, "LOGIN",
				"LOGIN_ID");
		tag = tag + "0";
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
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

		registerMessageReceiver(); // used for receive msg

	}

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

	private void initview() {
		check_detail = (Button) findViewById(R.id.check_detail); // 查看历史排队记录的详情
		queueup_phone = (TextView) findViewById(R.id.queueup_phone); // 电话号码
		queueup_image = (ImageView) findViewById(R.id.queueup_image); // 图片
		queueup_name = (TextView) findViewById(R.id.queueup_name); // 企业名称
		queueup_address = (TextView) findViewById(R.id.queueup_address); // 企业地址
		now_no = (TextView) findViewById(R.id.now_no); // 当前排号
		queue_count = (TextView) findViewById(R.id.queue_count); // 我前面的排队人数
		my_no = (TextView) findViewById(R.id.my_no); // 我的排队号码
		end_no = (TextView) findViewById(R.id.end_no); // 已排队人数
		queueup_name_title = (TextView) findViewById(R.id.queueup_name_title); // 标题栏显示

		rl_queue_count = (RelativeLayout) findViewById(R.id.rl_queue_count);

		// 点击企业名称，查看企业详情
		queueup_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String companyId = myQueueUpInfo.getCompanyId();
				String companyType = myQueueUpInfo.getCompanyType().toString();
				new CompanyDetailUtility().execute(companyId.toString(),
						companyType);

			}
		});
		// 点击企业图片，查看企业详情
		queueup_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String companyId = myQueueUpInfo.getCompanyId();
				String companyType = myQueueUpInfo.getCompanyType().toString();
				new CompanyDetailUtility().execute(companyId.toString(),
						companyType);

			}
		});

		// 打电话
		queueup_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.CALL");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setData(Uri.parse("tel:"
								+ myQueueUpInfo.getCompanyContract()));
						startActivity(intent);
					}

				}).start();

			}
		});

		// 查看排队的详情
		check_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyQueueUp.this, QueueUpDetail.class);
				intent.putExtra("cid", cid);
				intent.putExtra("count", count);
				startActivity(intent);
			}
		});
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
					String aString = contentJson.get("FrontQueueUpCount")
							.toString();
					String bString = contentJson.get("CurrentNumber")
							.toString();
					String cString = contentJson.get("AlreadyCount").toString();
					String cidString = contentJson.get("CompanyId").toString();
					if (cidString.equals(myQueueUpInfo.getCompanyId())) {  //推送过来的企业id和此界面的企业id

						StringBuilder showMsg = new StringBuilder();
						showMsg.append(aString);

						StringBuilder showMsg1 = new StringBuilder();
						showMsg1.append(bString);

						StringBuilder showMsg2 = new StringBuilder();
						showMsg2.append(cString);

						if (!ExampleUtil.isEmpty(extras)) {
							showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
						}
						if (!"".equals(myQueueUpInfo.getMyQueueUpNum())) {//我的排队号码不为空的时候，设置页面
							if (Integer.parseInt(bString)
									- Integer.parseInt(myQueueUpInfo
											.getMyQueueUpNum()) > 0) {
								rl_queue_count.setVisibility(View.GONE);
								top = (TextView) findViewById(R.id.condition);
								top.setVisibility(View.VISIBLE);
								top.setText("您的排队号码已经过期!");
							} else {
								setCostomMsg(showMsg.toString());
							}
						}
						setCostomMsg1(showMsg1.toString());
						setCostomMsg2(showMsg2.toString());

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void setCostomMsg(String msg) {
		if (null != queue_count) {
			queue_count.setText(msg);
			queue_count.setVisibility(android.view.View.VISIBLE);
		}
	}

	private void setCostomMsg1(String msg) {
		if (null != now_no) {
			now_no.setText(msg);
			now_no.setVisibility(android.view.View.VISIBLE);
		}
	}

	private void setCostomMsg2(String msg) {
		if (null != end_no) {
			/*end_no.setText(msg);*/
			end_no.setVisibility(android.view.View.VISIBLE);
		}
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

				if (ExampleUtil.isConnected(MyQueueUp.instance)) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {

				}
				break;

			default:
				logs = "Failed with errorCode = " + code;

			}

			ExampleUtil.showToast(logs, MyQueueUp.instance);
		}

	};

	// 返回按钮监听
	public void backBtn(View v) {
		this.finish();
		if (CaptureActivity.instance != null) {
			CaptureActivity.instance.finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (CaptureActivity.instance != null) {
				CaptureActivity.instance.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
