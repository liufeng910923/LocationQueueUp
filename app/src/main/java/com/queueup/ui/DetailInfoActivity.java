package com.queueup.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.queueup.conf.DemoApplication;
import com.queueup.entity.RestaurantInfo;
import com.queueup.network.AndroidUtils;
import com.queueup.untility.DeleteMyAttentionUtility;
import com.queueup.untility.InsertMyAttentionUtility;

public class DetailInfoActivity extends Activity {
	public static DetailInfoActivity instance;
	public RestaurantInfo restaurantInfo;
	private InsertMyAttentionUtility insertMyAttentionUtility;
	private DeleteMyAttentionUtility deleteMyAttentionUtility;
	public Button focus_btn;
	TextView detail_name, nearby_name, ny_idCard, nearby_queueup, companyintroduce, companyFeature, adr, mobile;

	public ImageView nearby_image,img_focus;
	public static String attention;
	private LinearLayout ll_guanzhu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_detail_info);
		initview();
		restaurantInfo = (RestaurantInfo) getIntent().getSerializableExtra("restaurantInfo");
		detail_name.setText(restaurantInfo.getCompanyName());

		ImageLoader.getInstance().displayImage(restaurantInfo.getPictureAddress(), nearby_image, DemoApplication.options);

		nearby_name.setText(restaurantInfo.getCompanyName());
		ny_idCard.setText("距离：" + restaurantInfo.getDistance() + "km");
		nearby_queueup.setText(restaurantInfo.getQueueUpCount()+"人排队");
		companyintroduce.setText(restaurantInfo.getCompanySimpleIntro());
		companyFeature.setText(restaurantInfo.getCompanyFeature());
		adr.setText(restaurantInfo.getCompanyAddress());
		mobile.setText(restaurantInfo.getCompanyContract());
		attention = restaurantInfo.getAttention().toString();
		if (attention.equals("false")) {
			focus_btn.setText("关注");
			img_focus.setBackgroundResource(R.drawable.guanzhutb);
		} else {
			focus_btn.setText("取消关注");
		}
		// 关注按钮
		ll_guanzhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (attention.equals("false")) {
					String cid = restaurantInfo.getCompanyId().toString();
					String jid = AndroidUtils.getSharedPreferences(DetailInfoActivity.instance, "LOGIN", "LOGIN_ID");
					insertMyAttentionUtility = new InsertMyAttentionUtility(jid, cid);
					insertMyAttentionUtility.execute();
				} else {
					String cid = restaurantInfo.getCompanyId().toString();
					String jid = AndroidUtils.getSharedPreferences(DetailInfoActivity.instance, "LOGIN", "LOGIN_ID");
					deleteMyAttentionUtility = new DeleteMyAttentionUtility(jid, cid);
					deleteMyAttentionUtility.execute();
				}

			}
		});
		DemoApplication.getInstance().add(DetailInfoActivity.this);
	}
	private void initview() {
		findViewById(R.id.mobile).setOnClickListener(new OnClickListener() {
			// 打电话
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setAction("android.intent.action.CALL");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setData(Uri.parse("tel:" + restaurantInfo.getCompanyContract()));
						startActivity(intent);
					}
				}).start();
			}
		});
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
		img_focus = (ImageView)findViewById(R.id.img_focus);
		instance = this;
	}

	// 返回按钮
	public void backBtn(View v) {
		finish();
	}

	public void llOnclick(View view) {

		if (attention.equals("false")) {
			String cid = restaurantInfo.getCompanyId().toString();
			String jid = AndroidUtils.getSharedPreferences(DetailInfoActivity.instance, "LOGIN", "LOGIN_ID");
			insertMyAttentionUtility = new InsertMyAttentionUtility(jid, cid);
			insertMyAttentionUtility.execute();
		} else {
			String cid = restaurantInfo.getCompanyId().toString();
			String jid = AndroidUtils.getSharedPreferences(DetailInfoActivity.instance, "LOGIN", "LOGIN_ID");
			deleteMyAttentionUtility = new DeleteMyAttentionUtility(jid, cid);
			deleteMyAttentionUtility.execute();
		}

	}

}
