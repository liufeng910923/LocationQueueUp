package com.queueup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.jpush.android.api.JPushInterface;

import com.queueup.adapters.ViewPagerAdapter;
import com.queueup.network.AndroidUtils;

/**
 * @Title:Guidance.java
 * @Description:TODO 引导界面
 * @Date 2014-8-20
 */
public class Guidance extends Activity implements OnPageChangeListener {

	public static final String SHAREDPREFERENCES_NAME = "first_pref";
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;

	/** 记录当前选中位置 */
	private int currentIndex;

	private GestureDetector gestureDetector;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guidance);

		gestureDetector = new GestureDetector(new GestureLisener());

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// 初始化页面
		initViews();

		// 初始化底部小点
		initDots();
	}

	

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views);

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {

		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		setCurrentDot(arg0);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (gestureDetector.onTouchEvent(ev)) {
			ev.setAction(MotionEvent.ACTION_CANCEL);
		}

		return super.dispatchTouchEvent(ev);
	}

	class GestureLisener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			if (currentIndex == (views.size() - 1)) {
				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
						- e2.getY())
						&& (e1.getX() - e2.getX() > 0)) {
					goHome();
					// 设置已经引导
					setGuided();
				}
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}

	private void goHome() {

		Intent intent;

		String flg = AndroidUtils.getSharedPreferences(WelcomeView.instance,
				"LOGIN", "FLG");// 获取是否登录

		// 判断是否登录成功

		if (flg != null && "TRUE".equals(flg)) {

			intent = new Intent(WelcomeView.instance, MainActivity.class);

		} else {
			intent = new Intent(WelcomeView.instance, LoginActivity.class);
		}

		startActivity(intent);
		
		Guidance.this.finish();
		

	}

	/**
	 * 
	 * 设置已经引导过了，下次启动不用再次引导
	 */
	private void setGuided() {
		SharedPreferences preferences = getApplicationContext()
				.getSharedPreferences(SHAREDPREFERENCES_NAME,
						Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", true);
		// 提交修改
		editor.commit();
	}

}
