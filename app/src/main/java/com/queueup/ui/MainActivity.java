package com.queueup.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.queueup.entity.MyQueueUpInfo;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	private TabHost tabHost;
	private View tabarLineOne, tabarLineTwo, tabarLineThree;
	private RelativeLayout home_view, msg_view, selfinfo_view, more_view;
	private ImageView home_imv, msg_imv, selfInfo_imv, more_imvs;
	
	public static MyQueueUpInfo myQueueUpInfo = new MyQueueUpInfo();
	

	public void initview() {
		tabarLineOne = (View) this.findViewById(R.id.mainTabs_radio_line_one);
		tabarLineTwo = (View) this.findViewById(R.id.mainTabs_radio_line_two);
		tabarLineThree = (View) this.findViewById(R.id.mainTabs_radio_line_three);

		home_view = (RelativeLayout) this.findViewById(R.id.maintabs_rlhome);
		msg_view = (RelativeLayout) this.findViewById(R.id.maintabs_rlmsg);
		selfinfo_view = (RelativeLayout) this.findViewById(R.id.maintabs_rlselfinfo);
		more_view = (RelativeLayout) this.findViewById(R.id.maintabs_rlmore);

		home_imv = (ImageView) this.findViewById(R.id.mainTabs_radio_home);
		msg_imv = (ImageView) this.findViewById(R.id.mainTabs_radio_msg);
		selfInfo_imv = (ImageView) this.findViewById(R.id.mainTabs_radio_selfInfo);
		more_imvs = (ImageView) this.findViewById(R.id.mainTabs_radio_more);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initview();

		tabHost = this.getTabHost();

		// mth = (TabHost) this.findViewById(R.id.tabhost);
		// // 注意下面这段 （可以在不集成TabActivity的情况下使用）
		// LocalActivityManager mLocalActivityManager = new
		// LocalActivityManager(
		// this, false);
		// mLocalActivityManager.dispatchCreate(savedInstanceState);
		// tabHost.setup(mLocalActivityManager);

		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1").setContent(new Intent(this, PersonAttentionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2").setContent(new Intent(this, FirstActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3").setContent(new Intent(this, DiscountActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		tabHost.addTab(tabHost.newTabSpec("4").setIndicator("4").setContent(new Intent(this, MoreActivity.class)));

		tabHost.setCurrentTab(0);
		// radioderGroup = (RadioGroup) findViewById(R.id.main_rsadio);
		// radioderGroup.setOnCheckedChangeListener(this);
		// radioderGroup.check(R.id.mainTabs_radio_home);// 默认初始布局
		home_view.setOnClickListener(new MyOnlickListener(0));
		msg_view.setOnClickListener(new MyOnlickListener(1));
		selfinfo_view.setOnClickListener(new MyOnlickListener(2));
		more_view.setOnClickListener(new MyOnlickListener(3));
	}

	private class MyOnlickListener implements OnClickListener {
		private int index = -1;

		public MyOnlickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View arg0) {
			switch (index) {
			case 0:
				home_imv.setImageResource(R.drawable.tabar_attention);
				msg_imv.setImageResource(R.drawable.tabar_nearby_default);
				selfInfo_imv.setImageResource(R.drawable.tabar_coupon_default);
				more_imvs.setImageResource(R.drawable.tabar_more_default);

				tabarLineOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.fgxt));

				tabarLineTwo.setBackgroundColor(getResources().getColor(R.color.white));

				tabarLineThree.setBackgroundColor(getResources().getColor(R.color.white));

				tabHost.setCurrentTab(0);
				break;

			case 1:
				home_imv.setImageResource(R.drawable.tabar_attention_default);
				msg_imv.setImageResource(R.drawable.tabar_nearby);
				selfInfo_imv.setImageResource(R.drawable.tabar_coupon_default);
				more_imvs.setImageResource(R.drawable.tabar_more_default);

				tabarLineOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.fgxo));

				tabarLineTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.fgxt));

				tabarLineThree.setBackgroundColor(getResources().getColor(R.color.white));

				tabHost.setCurrentTab(1);
				break;

			case 2:
				home_imv.setImageResource(R.drawable.tabar_attention_default);
				msg_imv.setImageResource(R.drawable.tabar_nearby_default);
				selfInfo_imv.setImageResource(R.drawable.tabar_coupon);
				more_imvs.setImageResource(R.drawable.tabar_more_default);

				tabarLineOne.setBackgroundColor(getResources().getColor(R.color.white));

				tabarLineTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.fgxo));

				tabarLineThree.setBackgroundDrawable(getResources().getDrawable(R.drawable.fgxt));

				tabHost.setCurrentTab(2);
				break;

			case 3:
				/* showMenu();// 这句用来显示侧滑菜单 */
				home_imv.setImageResource(R.drawable.tabar_attention_default);
				msg_imv.setImageResource(R.drawable.tabar_nearby_default);
				selfInfo_imv.setImageResource(R.drawable.tabar_coupon_default);
				more_imvs.setImageResource(R.drawable.tabar_more);

				tabarLineOne.setBackgroundColor(getResources().getColor(R.color.white));

				tabarLineTwo.setBackgroundColor(getResources().getColor(R.color.white));

				tabarLineThree.setBackgroundDrawable(getResources().getDrawable(R.drawable.fgxo));

				tabHost.setCurrentTab(3);
				break;
			}
		}

	}
}
