package com.queueup.ui;

import cn.jpush.android.api.JPushInterface;

import com.queueup.conf.DemoApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VersionUpdateActivity extends Activity{
	public boolean isNewLy=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.version_update_no);
		
		DemoApplication.getInstance().add(VersionUpdateActivity.this);
		
		TextView tv_sure=(TextView)findViewById(R.id.sure);
		//确定按钮
		tv_sure.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if(isNewLy){
					VersionUpdateActivity.this.finish();//节省 内存。
				}else{
					
				}
				
				
			}
		});
	}
	
	


	
}

