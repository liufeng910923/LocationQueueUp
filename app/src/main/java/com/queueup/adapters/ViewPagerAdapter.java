/**
 * @(#)ViewPagerAdapter.java
 * Copyright (c) 2013-2014  Simon  All rights reserved.
 */
package com.queueup.adapters;

import java.util.List;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @Title:ViewPagerAdapter.java
 * @Description:TODO
 * @Author:Simon
 * @Date 2014-5-12
 */
public class ViewPagerAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private TextView mStartWeiboImageButton;

	public ViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 获得当前界面数
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(View arg0, int arg1) {

		((ViewPager) arg0).addView(views.get(arg1), 0);

		Log.d("wyy", "--instantiateItem--out--" + arg1);

		if (arg1 == views.size() - 1) {

			Log.d("wyy", "--instantiateItem-inner -" + arg1);

		}

		return views.get(arg1);
	}

	public TextView getStartButton() {

		return null != mStartWeiboImageButton ? mStartWeiboImageButton : null;

	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
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
