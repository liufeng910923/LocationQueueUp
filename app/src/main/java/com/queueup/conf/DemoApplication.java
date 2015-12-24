package com.queueup.conf;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.queueup.ui.R;
import com.queueup.untility.LogUtils;

public class DemoApplication extends Application {

	private static DemoApplication mInstance = null;
	public static final boolean DEVELOPER_MODE = false;
	public static ImageLoader imageLoader = null;
	public boolean m_bKeyRight = true;
	public static DisplayImageOptions options = null;
	public List<Activity> activities = new ArrayList<Activity>();


	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		mInstance = this;

		LogUtils.customTagPrefix = "凯恩克斯";// 设定TAG 可选
		LogUtils.debug = true;// Log日志打开
		// LogUtils.debug = false; //Log日志关闭
		 L.disableLogging();//关闭ImageLoaderLog输出
		//L.enableLogging();// 打开ImageLoaderLog输出

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.empty_photo).showImageForEmptyUri(R.drawable.empty_photo).showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();

		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		LogUtils.d("初始化百度地图SDK");

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		initImageLoader(this);

	}

	public void add(Activity activity) {
		activities.add(activity);
		LogUtils.d("Activity总数：" + activities.size());
	}

	public void exit() {
		LogUtils.d("开始循环finishActivity，退出程序");
		for (Activity activity : activities) {
			activity.finish();
		}
		System.exit(0);
	}

	public static DemoApplication getInstance() {
		return mInstance;
	}

	public static void initImageLoader(Context context) {

		// ImageLoader配置信息

		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);

		// 设置线程并发数量
		config.threadPoolSize(5);
		// 设定线程等级比普通低一点
		config.threadPriority(Thread.NORM_PRIORITY - 1);
		// 设定内存缓存为弱缓存
		config.memoryCache(new WeakMemoryCache());
		// 设定内存图片缓存大小限制，不设置默认为屏幕的宽高
		// config.memoryCacheExtraOptions(480, 800);
		// 缓存到内存的最大数据(8MB)
		config.memoryCacheSize(8 * 1024 * 1024);
		// 缓存到文件的最大数据(50MB)
		config.discCacheSize(50 * 1024 * 1024);
		// 保存在SD卡上的最大文件数量
		config.discCacheFileCount(10000);
		// 设定只保存同一尺寸的图片在内存
		config.denyCacheImageMultipleSizesInMemory();
		// 设定图片缓存到SDCard目录的图片文件命名
		config.discCacheFileNameGenerator(new Md5FileNameGenerator());
		// 设定网络连接超时 timeout: 10s 读取网络连接超时read timeout: 30s
		config.imageDownloader(new BaseImageDownloader(context, 10000, 30000));
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs();
		// config.discCache(new FileCountLimitedDiscCache(new File(FileUtile
		// .getImgsDir()), 200));

		ImageLoader.getInstance().init(config.build());

	}

}