package com.queueup.baidumap;

import java.util.ArrayList;
import java.util.List;

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
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.loopj.android.http.RequestParams;
import com.queueup.baidumap.MyOrientationListener.OnOrientationListener;
import com.queueup.conf.DemoApplication;
import com.queueup.conf.Domain;
import com.queueup.entity.RestaurantInfo;
import com.queueup.network.RequestHttp;
import com.queueup.ui.DetailInfoActivity;
import com.queueup.ui.R;
import com.queueup.untility.LogUtils;

/**
 * @Title:MainActivity.java
 * @Description:TODO
 * @Author:SimonYang
 * @Date 2014-9-18 上午11:10:08
 */
public class LocationOverlayDemo extends Activity implements
		OnGetGeoCoderResultListener {
	private Context mContext;
	private SDKReceiver mReceiver;
	public static LocationOverlayDemo instance;

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			LogUtils.e("action: " + s);
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(getApplicationContext(),
						"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
						Toast.LENGTH_LONG).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getApplicationContext(), "网络出错",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/****************************************************/
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	/**
	 * 当前地点击点
	 */
	private LatLng currentPt;

	/**
	 * 当前点击类型
	 */
	private String touchType;

	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myLocationListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private LocationClientOption option;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	private BitmapDescriptor mCurrentMarker;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	private BitmapDescriptor mIconMaker;

	/**
	 * 最新一次的经纬度
	 */
	/**
	 * 实时纬度
	 */
	public static double mCurrentLantitude;
	/**
	 * 实时经度
	 */
	public static double mCurrentLongitude;

	/**
	 * 点击纬度
	 */
	public static double mDianJiLantitude;
	/**
	 * 点击经度
	 */
	public static double mDianJiLongitude;

	/**
	 * 当前的精度
	 */
	private float mCurrentAccracy;
	/**
	 * 方向传感器的监听器
	 */
	private MyOrientationListener myOrientationListener;
	/**
	 * 方向传感器X方向的值
	 */
	private int mXDirection;

	// 搜索相关
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private String cityName = "北京";// 设置当前搜索的城市

	private boolean isFirstLoc = true;// 是否首次定位
	private boolean isShiShiJiaoTong = false;
	private int ErrorCode;

	// UI相关
	private LinearLayout mLocationLinear;// 普通、更新、罗盘布局
	private LinearLayout mRoadconditionLinear;// 开关实时路况布局

	private ImageView mLocationImage;// 普通、更新、罗盘图片
	private ImageView mRoadconditionImage;// 开关实时路况图片

	// 搜索布局相关
	public static EditText mMapEditText;
	public Button mMapTalkTextView;
	private TextView mMapSearchText;
	private Button baidu_map_button;

	private View mMarkerView = null;

	private List<RestaurantInfo> mMarkerInfos;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RequestHttp.REQUESTDATA_START:

				break;
			case RequestHttp.REQUESTDATA_SUCCESS_OK:
				String content = (String) msg.obj;
				// LogUtils.d(content);
				if (initJson(content)) {
					addInfosOverlay();
				}
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

	/**
	 * Title: onCreate
	 * 
	 * @Description:
	 * @param savedInstanceState
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.d("生命周期");
		setContentView(R.layout.activity_location_baidu_map);
		mContext = this;
		instance = this;

		ZhuCeBaiDuSDKGuangBoJianTingZhe();// 注册 SDK 广播监听者

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 说话image：
		baidu_map_button = (Button) this.findViewById(R.id.baidu_map_button);
//		final ConstantState iView = baidu_map_imageview.getDrawable().getConstantState();
//		final ConstantState iState = getResources().getDrawable(R.drawable.baidu_map_mic1).getConstantState();
		baidu_map_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (baidu_map_button.getBackground().getConstantState()
						.equals(getResources().getDrawable(R.drawable.baidu_map_mic_0).getConstantState())) {
					
					System.out.println("进来了");
					mMapTalkTextView.setVisibility(View.VISIBLE);
					mMapEditText.setVisibility(View.GONE);
					baidu_map_button
							.setBackgroundResource(R.drawable.baidu_map_editor0);
				} else if (baidu_map_button.getBackground().getConstantState()
						.equals(getResources().getDrawable(R.drawable.baidu_map_editor0).getConstantState())) {
					mMapTalkTextView.setVisibility(View.GONE);
					mMapEditText.setVisibility(View.VISIBLE);
					baidu_map_button
							.setBackgroundResource(R.drawable.baidu_map_mic_0);
				}

			}
		});
		// 编辑框：
		mMapEditText = (EditText) this.findViewById(R.id.baidu_map_geocode_key);
		// 初始化语音模块
		mMapTalkTextView = (Button) this
				.findViewById(R.id.baidu_map_geocode_talk_key);

		

		// 给语音textview一个touch事件：
		mMapTalkTextView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 按下去的状态
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					System.out.println("按下去  语音开始：");
					mMapEditText.setText("");
					Intent intent = new Intent(LocationOverlayDemo.this,
							TalkToolActivity.class);
					startActivity(intent);

				}

				// 松手的状态：
				else if (event.getAction() == MotionEvent.ACTION_UP) {

					System.out
							.println("--------MainActivity:onTouch():  松手后事件   ------------");
					System.out
							.println("-----------MainActivity:onTouch():talktext="
									+ TextConstant.TALK_TEXT + "-------------");

					TalkToolActivity.activity.finish();
					// 将输入框显示出来，语音textview隐藏
					mMapTalkTextView.setVisibility(View.GONE);
					mMapEditText.setVisibility(View.VISIBLE);
					baidu_map_button
						.setBackgroundResource(R.drawable.baidu_map_mic_0);
				}
				return false;

			}
		});

		mMapSearchText = (TextView) this.findViewById(R.id.baidu_map_geocode);

		// 搜索的点击事件：
		mMapSearchText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String key = mMapEditText.getText().toString().trim();
				if (TextUtils.isEmpty(key)) {
					return;
				}

				Toast.makeText(mContext, "正在搜索：" + key, Toast.LENGTH_SHORT)
						.show();

				// Geo搜索
				mSearch.geocode(new GeoCodeOption().city(cityName).address(key));

			}
		});

		mMapView = (MapView) this.findViewById(R.id.baidu_map_bmapView);
		mLocationLinear = (LinearLayout) this
				.findViewById(R.id.baidu_map_location_line);
		mLocationImage = (ImageView) this
				.findViewById(R.id.baidu_map_location_img);
		mLocationLinear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 普通、更新、罗盘 点击事件
				switch (mCurrentMode) {
				case NORMAL:// 普通
					LogUtils.d("普通的点击事件");
					// mLocClient.start();
					mLocationImage
							.setBackgroundResource(R.drawable.main_icon_follow);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));

					break;
				case COMPASS:// 罗盘
					// LogUtils.d("罗盘的点击事件");
					// mLocationImage.setBackgroundResource(R.drawable.main_icon_follow);
					// mCurrentMode = LocationMode.FOLLOWING;
					// mBaiduMap.setMyLocationConfigeration(new
					// MyLocationConfiguration(mCurrentMode, true,
					// mCurrentMarker));
					break;
				case FOLLOWING:// 跟随
					LogUtils.d("跟随的点击事件");
					mLocationImage
							.setBackgroundResource(R.drawable.main_icon_location);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));

					break;
				}
			}
		});
		mRoadconditionLinear = (LinearLayout) this
				.findViewById(R.id.baidu_map_roadcondition_line);
		mRoadconditionImage = (ImageView) this
				.findViewById(R.id.baidu_map_roadcondition_img);
		mRoadconditionLinear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 开关实时路况 点击事件
				if (!isShiShiJiaoTong) {
					mRoadconditionImage
							.setBackgroundResource(R.drawable.main_icon_roadcondition_on);
					isShiShiJiaoTong = true;
					mBaiduMap.setTrafficEnabled(true);
					Toast.makeText(mContext, "已开启实时交通", Toast.LENGTH_SHORT)
							.show();
				} else {
					mRoadconditionImage
							.setBackgroundResource(R.drawable.main_icon_roadcondition_off);
					isShiShiJiaoTong = false;
					mBaiduMap.setTrafficEnabled(false);
					Toast.makeText(mContext, "已关闭实时交通", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		mBaiduMap = mMapView.getMap();
		initBaiduMapListener();// 初始化地图点击事件监听器

		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(14).build()));// 设置缩放级别
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myLocationListener);
		// 开启图层定位
		mBaiduMap.setMyLocationEnabled(true);
		option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");// 设置是否需要地址信息
		option.setProdName("定位、搜索周边");// 设置产品线名称
		option.setPriority(LocationClientOption.GpsFirst);// 定位优先级：设置GPS定位和网络定位
		// option.setPriority(LocationClientOption.NetWorkFirst);// 定位优先级：设置网络定位
		option.disableCache(true);// 设置是否启用缓存定位
		mLocClient.setLocOption(option);

		initOritationListener();
		initMarkerClickEvent();
		// 初始化marker布局
		LayoutInflater factory = LayoutInflater.from(getApplicationContext());
		mMarkerView = factory.inflate(R.layout.map_marker_view, null);

	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay() {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;

		for (int i = 0; i < mMarkerInfos.size(); i++) {
			RestaurantInfo info = mMarkerInfos.get(i);
			// 位置
			latLng = new LatLng(Double.parseDouble(info.getLatitude()),
					Double.parseDouble(info.getLongitude()));
			mIconMaker = BitmapDescriptorFactory.fromView(initMarkerView(
					info.getCompanyName(), info.getQueueUpCount()));
			// 图标
			overlayOptions = new MarkerOptions().position(latLng)
					.icon(mIconMaker).zIndex(5);
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		// 将地图移到到最后一个经纬度位置
		// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
		// mBaiduMap.setMapStatus(u);
	}

	private View initMarkerView(String title, String num) {
		// TODO Auto-generated method stub
		TextView mTextTitle = (TextView) mMarkerView
				.findViewById(R.id.marker_view_title);
		TextView mTextView = (TextView) mMarkerView
				.findViewById(R.id.marker_view_num);

		mTextTitle.setText(title.trim());
		mTextView.setText(num.trim());

		return mMarkerView;
	}

	private void initMarkerClickEvent() {
		// 对Marker的点击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据
				RestaurantInfo info = (RestaurantInfo) marker.getExtraInfo()
						.get("info");
				// InfoWindow mInfoWindow;
				// // 生成一个TextView用户在地图中显示InfoWindow
				// TextView location = new TextView(getApplicationContext());
				// location.setBackgroundResource(R.drawable.location_tips);
				// location.setPadding(30, 20, 30, 50);
				// location.setText(info.getCompanyName());
				// // 将marker所在的经纬度的信息转化成屏幕上的坐标
				// final LatLng ll = marker.getPosition();
				// Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				// p.y -= 47;
				// LatLng llInfo =
				// mBaiduMap.getProjection().fromScreenLocation(p);
				// // 为弹出的InfoWindow添加点击事件
				// mInfoWindow = new InfoWindow(location, llInfo, new
				// OnInfoWindowClickListener() {
				//
				// @Override
				// public void onInfoWindowClick() {
				// // 隐藏InfoWindow
				// mBaiduMap.hideInfoWindow();
				// }
				// });
				// // 显示InfoWindow
				// mBaiduMap.showInfoWindow(mInfoWindow);
				// // 设置详细信息布局为可见
				// mMarkerInfoLy.setVisibility(View.VISIBLE);
				// // 根据商家信息为详细信息布局设置信息
				// popupInfo(mMarkerInfoLy, info);

				Intent intent = new Intent(mContext, DetailInfoActivity.class);
				intent.putExtra("restaurantInfo", info);
				startActivity(intent);

				LogUtils.d("跳转界面传递Ben:RestaurantInfo" + info.getCompanyName());

				return true;
			}
		});
	}

	/**
	 * 
	 * @Description:TODO 数据请求
	 */
	private void requestData(double lan, double lon) {

		String urlString = Domain.get_NearCompany + "?";

		RequestParams mParams = new RequestParams();
		mParams.put("MemberAddressLatitude", Double.toString(lan));
		mParams.put("MemberAddressLongitude", Double.toString(lon));
		mParams.put("FirstResult", String.valueOf(0));
		mParams.put("MaxResults", String.valueOf(10));
		mParams.put("MemberId", "111111");
		mParams.put("CompanyType", "restaurant");
		mParams.put("NearDistance", "5");// 默认获取周边5公里以内的餐馆

		new RequestHttp(mHandler, urlString, mParams, RequestHttp.GET);

	}

	/**
	 * 
	 * @Description:TODO 解析Json
	 */
	private Boolean initJson(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return false;
		}

		mMarkerInfos = new ArrayList<RestaurantInfo>();

		try {
			JSONObject jsonObject = new JSONObject(jsonString);
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
				info.setCompanySimpleIntro(jsonObject2
						.getString("companySimpleIntro"));
				info.setCompanyFeature(jsonObject2.getString("companyFeature"));
				info.setCompanyContract(jsonObject2
						.getString("companyContract"));
				info.setAttention(jsonObject2.getString("attention"));
				info.setLatitude(jsonObject2.getString("latitude"));
				info.setLongitude(jsonObject2.getString("longitude"));
				mMarkerInfos.add(info);
			}
			LogUtils.e("mMarkerInfos的总数量：" + mMarkerInfos.size());

			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtils.e("catch:解析Json失败");
			return false;
		} finally {
			if (mMarkerInfos.size() > 0) {
				// LogUtils.d(mMarkerInfos.toString());
			} else {
				LogUtils.e("finally:解析Json失败");
				return false;
			}
		}
	}

	/***************** 地图点击事件监听器↓ *****************************************************/
	/**
	 * @Description:TODO 地图点击事件监听器
	 */
	private void initBaiduMapListener() {
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				touchType = "单击";
				LogUtils.w(touchType);
				currentPt = point;
				updateMapState();
				mDianJiLantitude = currentPt.latitude;
				mDianJiLongitude = currentPt.longitude;

				LogUtils.w("经度：" + mDianJiLantitude);
				LogUtils.w("纬度：" + mDianJiLongitude);

				mBaiduMap.setMapStatus(MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder().zoom(14).build()));// 设置缩放级别
				LatLng ll = new LatLng(currentPt.latitude, currentPt.longitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				requestData(mDianJiLantitude, mDianJiLongitude);// 请求数据

			}

			public boolean onMapPoiClick(MapPoi poi) {
				touchType = "点击POI点:" + poi.getName();
				mBaiduMap.clear();
				LogUtils.w(touchType);
				currentPt = poi.getPosition();
				updateMapState();
				mDianJiLantitude = currentPt.latitude;
				mDianJiLongitude = currentPt.longitude;

				LogUtils.w("经度：" + mDianJiLantitude);
				LogUtils.w("纬度：" + mDianJiLongitude);

				mBaiduMap.setMapStatus(MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder().zoom(14).build()));// 设置缩放级别
				LatLng ll = new LatLng(currentPt.latitude, currentPt.longitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				requestData(mDianJiLantitude, mDianJiLongitude);// 请求数据
				return true;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				touchType = "长按";
				LogUtils.w(touchType);
				currentPt = point;
				// updateMapState();
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				touchType = "双击";
				LogUtils.w(touchType);
				currentPt = point;
				// updateMapState();
			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			public void onMapStatusChangeStart(MapStatus status) {
				// LogUtils.w("开始拖动百度地图");

			}

			public void onMapStatusChangeFinish(MapStatus status) {
				// LogUtils.w("结束拖动百度地图");
				// updateMapState();
			}

			public void onMapStatusChange(MapStatus status) {
				// LogUtils.w("拖动百度地图中ing。。。");
				// updateMapState();

			}
		});
	}

	/**
	 * 更新地图状态显示面板
	 */
	private void updateMapState() {
		String state = "";
		if (currentPt == null) {
			state = "点击、长按、双击地图以获取经纬度和地图状态";
		} else {
			state = String.format(touchType + ",当前经度： %f 当前纬度：%f",
					currentPt.longitude, currentPt.latitude);
		}
		state += "\n";
		MapStatus ms = mBaiduMap.getMapStatus();
		state += String.format("zoom=%.1f rotate=%d overlook=%d", ms.zoom,
				(int) ms.rotate, (int) ms.overlook);
		// Toast.makeText(mContext, state, Toast.LENGTH_SHORT).show();
		LogUtils.d(state);

	}

	/***************** 地图点击事件监听器↑ *****************************************************/

	/******* 定位SDK监听事件↓ *********************************************/

	/**
	 * 初始化方向传感器
	 */
	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mXDirection = (int) x;
						// 构造定位数据
						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mCurrentAccracy)
								// 此处设置开发者获取到的方向信息，顺时针0-360
								.direction(mXDirection)
								.latitude(mCurrentLantitude)
								.longitude(mCurrentLongitude).build();
						// 设置定位数据
						mBaiduMap.setMyLocationData(locData);

					}
				});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		// 返回值：
		// 61 ： GPS定位结果
		// 62 ： 扫描整合定位依据失败。此时定位结果无效。
		// 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
		// 65 ： 定位缓存的结果。
		// 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
		// 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
		// 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
		// 161： 表示网络定位结果
		// 162~167： 服务端定位失败。

		// 如果不能定位，请记住这个返回值，并到我们的hi群或者贴吧中交流。若返回值是162~167，请发送邮件至mapapi@baidu.com反馈。

		@Override
		public void onReceiveLocation(BDLocation location) {
			// LogUtils.d("定位成功");
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mXDirection).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			mCurrentLantitude = location.getLatitude();// 获取维度
			mCurrentLongitude = location.getLongitude();// 获取经度
			ErrorCode = location.getLocType();
			// LogUtils.e("获取error code：" + ErrorCode);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				requestData(location.getLatitude(), location.getLongitude());// 请求数据
			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/******* 定位SDK监听事件↑ *********************************************/

	/*********** 管理百度地图SDK声明周期↓ ***************************************************/
	/**
	 * @Description:TODO 注册 SDK 广播监听者
	 */
	private void ZhuCeBaiDuSDKGuangBoJianTingZhe() {
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtils.d("生命周期");
		// 取消监听 SDK 广播
		unregisterReceiver(mReceiver);
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		mSearch.destroy();
		mMapView = null;
		System.out
				.println("---------LocationOverlayDemo:onDestroy------------");
	}

	@Override
	protected void onResume() {

		super.onResume();
		LogUtils.d("生命周期");
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		System.out.println("---------LocationOverlayDemo:onResume------------");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.d("生命周期");
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		LogUtils.w("经度：" + mCurrentLongitude);
		LogUtils.w("纬度：" + mCurrentLantitude);
		System.out
				.println("---------LocationOverlayDemo:onPause()------------");
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
		System.out
				.println("---------LocationOverlayDemo:onRestart()------------");
	}

	/**
	 * Title: onStart
	 * 
	 * @Description:
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {

		super.onStart();
		LogUtils.d("生命周期");
		// 开启图层定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocClient.isStarted()) {
			LogUtils.d("开始定位");
			mLocClient.start();
		}
		// 开启方向传感器
		myOrientationListener.start();
		System.out
				.println("---------LocationOverlayDemo:onStart()------------");
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
		// 关闭方向传感器
		myOrientationListener.stop();
		mLocClient.stop();// 停止定位
		System.out
				.println("---------LocationOverlayDemo:onStop() ------------");
	}

	/*********** 管理百度地图SDK声明周期↑ ***************************************************/

	/**
	 * Title: onGetGeoCodeResult
	 * 
	 * @Description:
	 * @param arg0
	 * @see com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener#onGetGeoCodeResult(com.baidu.mapapi.search.geocode.GeoCodeResult)
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		LogUtils.d("地点转坐标");
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);

		Toast.makeText(getApplicationContext(), strInfo, Toast.LENGTH_SHORT)
				.show();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(15).build()));// 设置缩放级别
		LatLng ll = new LatLng(result.getLocation().latitude,
				result.getLocation().longitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);

		requestData(result.getLocation().latitude,
				result.getLocation().longitude);// 请求数据
	}

	/**
	 * Title: onGetReverseGeoCodeResult
	 * 
	 * @Description:
	 * @param arg0
	 * @see com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener#onGetReverseGeoCodeResult(com.baidu.mapapi.search.geocode.ReverseGeoCodeResult)
	 */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		LogUtils.d("坐标转地点");
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		Toast.makeText(getApplicationContext(), result.getAddress(),
				Toast.LENGTH_SHORT).show();

	}

	/**
	 * 监听返回键，退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("location - onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) { // && event.getRepeatCount() ==
			// 0
			AlertDialog.Builder builder = new Builder(
					LocationOverlayDemo.this.getParent());
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
		}
		return true;
	}

}
