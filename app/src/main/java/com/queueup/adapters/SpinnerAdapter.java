package com.queueup.adapters;

import java.util.List;

import com.queueup.ui.PersonAttentionActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SpinnerAdapter extends BaseAdapter {
	private PersonAttentionActivity instance;
	private List<String> lists;
	
	public SpinnerAdapter(PersonAttentionActivity instance,List<String> lists){
		this.instance=instance;
		this.lists=lists;
	}
	
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
