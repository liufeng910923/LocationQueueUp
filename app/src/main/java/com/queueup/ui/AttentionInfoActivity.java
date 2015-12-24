package com.queueup.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.queueup.conf.DemoApplication;
import com.queueup.entity.AttentionRestaurantInfo;
import com.queueup.network.AndroidUtils;
import com.queueup.untility.DeleteAttentionUtility;

public class AttentionInfoActivity extends Activity {
	public static AttentionInfoActivity instance;
	public AttentionRestaurantInfo attentionRestaurantInfo;
	private DeleteAttentionUtility deleteAttentionUtility;
	private LinearLayout ll_guanzhu;
	private Button focus_btn;
	private TextView detail_name, nearby_name, ny_idCard, nearby_queueup, companyintroduce, companyFeature, adr, mobile;
	public ImageView nearby_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_detail_info);
		initview();

		attentionRestaurantInfo = (AttentionRestaurantInfo) getIntent().getSerializableExtra("companydetail");
		detail_name.setText(attentionRestaurantInfo.getName());
		DownLoad();
		ImageLoader.getInstance().displayImage(attentionRestaurantInfo.getPic(), nearby_image, DemoApplication.options);
		nearby_name.setText(attentionRestaurantInfo.getName());
		/* ny_idCard.setText("距离："+attentionRestaurantInfo.getDistance()); */
		nearby_queueup.setText(attentionRestaurantInfo.getQueueup()+" 人排队");
		companyintroduce.setText(attentionRestaurantInfo.getCompanySimpleIntro());
		companyFeature.setText(attentionRestaurantInfo.getCompanyFeature());
		adr.setText(attentionRestaurantInfo.getAddress());
		mobile.setText(attentionRestaurantInfo.getCompanyContract());
		focus_btn.setText("取消关注");
		// 取消关注按钮
		ll_guanzhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String companyId = attentionRestaurantInfo.getCompanyId().toString();
				String memberId = AndroidUtils.getSharedPreferences(AttentionInfoActivity.instance, "LOGIN", "LOGIN_ID");
				deleteAttentionUtility = new DeleteAttentionUtility(memberId, companyId);
				deleteAttentionUtility.execute();
			}
		});

		DemoApplication.getInstance().add(AttentionInfoActivity.this);

	}

	private void initview() {

		detail_name = (TextView) findViewById(R.id.detail_name);
		nearby_image = (ImageView) findViewById(R.id.nearby_image);
		nearby_name = (TextView) findViewById(R.id.nearby_name);
		ny_idCard = (TextView) findViewById(R.id.ny_idCard);
		nearby_queueup = (TextView) findViewById(R.id.nearby_queueup);
		companyintroduce = (TextView) findViewById(R.id.companyintroduce);
		companyFeature = (TextView) findViewById(R.id.companyFeature);
		adr = (TextView) findViewById(R.id.adr);
		mobile = (TextView) findViewById(R.id.mobile);
		ll_guanzhu=(LinearLayout)this.findViewById(R.id.ll_guanzhu);
		focus_btn = (Button) findViewById(R.id.focus_btn);
		instance = this;

		findViewById(R.id.mobile).setOnClickListener(new OnClickListener() {
			// 打电话
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.CALL");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setData(Uri.parse("tel:" + attentionRestaurantInfo.getCompanyContract()));
						startActivity(intent);
					}
				}).start();
			}
		});
	}

	// 返回按钮
	public void backBtn(View v) {
		finish();
	}

	public void DownLoad() {
		if (!TextUtils.isEmpty(attentionRestaurantInfo.getPic())) {
			final ImageView mImageView = AttentionInfoActivity.instance.nearby_image;
			ImageLoader.getInstance().displayImage(attentionRestaurantInfo.getPic(), mImageView, DemoApplication.options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@SuppressWarnings("unused")
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
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
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

					mImageView.setScaleType(ScaleType.FIT_XY);
					mImageView.setImageBitmap(loadedImage);
				}
			});
		}
	}

}
