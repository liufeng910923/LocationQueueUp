package com.queueup.ui;

import java.io.File;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.queueup.conf.DemoApplication;
import com.queueup.untility.AssetsWriteToSD;
import com.queueup.untility.LogUtils;

public class ShareDialogView extends Activity {
	private ImageView img_wechat;
	private ImageView img_friend;
	private ImageView img_sina;
	private ImageView img_msg;
	private String imgPath = "";
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		DemoApplication.getInstance().add(ShareDialogView.this);
		setContentView(R.layout.popuwindow_layout);
		init();
		handler.post(runnable);

	}

	public void init() {
		img_wechat = (ImageView) findViewById(R.id.img_wechat);
		img_friend = (ImageView) findViewById(R.id.img_friend);
		img_sina = (ImageView) findViewById(R.id.img_sina);
		img_msg = (ImageView) findViewById(R.id.img_msg);

	}

	@SuppressLint("NewApi")
	public void myclick(View v) {
		switch (v.getId()) {
		case R.id.ll_wechat:			
			wechapalShare();
			break;
		case R.id.ll_friend:			
			wechatfriendShare();
			break;
		case R.id.ll_sina:			
			sinaShare(); 
			break;
		case R.id.ll_shortmeg:
			msgShare();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	/**
	 * 
	 * 新浪分享：
	 * */

	public void sinaShare() {

		ShareSDK.initSDK(this);
		ShareParams sp = new ShareParams();
		sp.setText("客户端　　"
				+ "http://appkinx.aliapp.com/QueueClientDownload.html");

		sp.setImagePath(imgPath);

		Platform weibo = ShareSDK.getPlatform(
				ShareDialogView.this.getApplicationContext(), SinaWeibo.NAME);
		PlatformActionListener paListener = null;
		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
		weibo.share(sp);

	}

	/**
	 * 微信朋友圈分享：
	 * 
	 * */
	public void wechatfriendShare() {

		ShareSDK.initSDK(this);
		ShareParams spParams1 = new ShareParams();
		spParams1.setText("客户端	  	"
				+ "http://appkinx.aliapp.com/QueueClientDownload.html");
		spParams1
				.setImageData(getAssetsBitmap(ShareDialogView.this, "map.jpg"));
		Platform wechat1 = ShareSDK.getPlatform(getApplicationContext(),
				WechatMoments.NAME);
		wechat1.share(spParams1);

	}

	/**
	 * 微信好友分享
	 * */

	public void wechapalShare() {

		ShareSDK.initSDK(this);
		ShareParams spParams1 = new ShareParams();
		spParams1.setText("客户端	  	"
				+ "http://appkinx.aliapp.com/QueueClientDownload.html");
		spParams1.setImagePath(imgPath);
		Platform wechatfriends1 = ShareSDK.getPlatform(getApplicationContext(),
				Wechat.NAME);
		wechatfriends1.share(spParams1);
	}

	/**
	 * 短信分享：
	 * */
	public void msgShare() {
		Intent intent = new Intent(ShareDialogView.this, MsgShareActivity.class);
		startActivity(intent);
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			File file = new File("/storage/emulated/0/.paidui/map.jpg");
			if (!file.exists()) {
				LogUtils.d("本地不存在，去创建本地图片");
				imgPath = AssetsWriteToSD.write(ShareDialogView.this);
				LogUtils.e(imgPath);
			} else {
				LogUtils.d("本地存在，去读取本地图片");
				imgPath = "/storage/emulated/0/.paidui/map.jpg";
			}

		}
	};

	/**
	 * 从Assets文件夹中读取图片
	 * 
	 * @param mContext
	 *            上下文
	 * @param fileName
	 *            文件名
	 * @return Bitmap图片
	 */
	private Bitmap getAssetsBitmap(Context mContext, String fileName) {
		Bitmap img = null;

		try {
			AssetManager am = mContext.getAssets();
			InputStream is = am.open(fileName);
			img = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return img;

	}

}
