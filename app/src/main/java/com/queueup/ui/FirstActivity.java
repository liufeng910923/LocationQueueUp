package com.queueup.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.queueup.baidumap.LocationOverlayDemo;

@SuppressWarnings("deprecation")
public class FirstActivity extends TabActivity implements OnCheckedChangeListener {
	private TabHost tabHost;
	private RadioGroup radioderGroup;
	public static LinearLayout mLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view1);
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1").setContent(new Intent(this, LocationOverlayDemo.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2").setContent(new Intent(this, RestaurantActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		mLayout = (LinearLayout) this.findViewById(R.id.first_hander_line_layout);
		radioderGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioderGroup.setOnCheckedChangeListener(this);
		radioderGroup.check(R.id.mainTabs_radio_msg);// 默认初始布局
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {

		case R.id.mainTabs_radio_msg:
			tabHost.setCurrentTabByTag("1");
			mLayout.setVisibility(View.INVISIBLE);
			break;
		case R.id.mainTabs_radio_selfInfo:
			mLayout.setVisibility(View.INVISIBLE);
			tabHost.setCurrentTabByTag("2");
			break;

		}

	}

}
