package com.queueup.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.queueup.conf.DemoApplication;
import com.queueup.entity.RestaurantInfo;
import com.queueup.ui.R;

public class MyAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<RestaurantInfo> mRestaurantInfos;
	private Context context;

	public MyAdapter(Context context, List<RestaurantInfo> mRestaurantInfos) {
		inflater = LayoutInflater.from(context);
		this.mRestaurantInfos = mRestaurantInfos;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRestaurantInfos.size() > 0 ? mRestaurantInfos.size() : 0;
	}

	
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mRestaurantInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setNotifyDataChange(List<RestaurantInfo> mRestaurantInfos) {
		this.mRestaurantInfos=mRestaurantInfos;
	}

	class ViewHolder {
		public ImageView pic;
		public TextView name, distance, address, queueup;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.restaurant_item, null);
			holder = new ViewHolder();
			holder.pic = (ImageView) convertView.findViewById(R.id.restaurant_img_icon);
			holder.name = (TextView) convertView.findViewById(R.id.restaurant_text_Name);
			holder.distance = (TextView) convertView
					.findViewById(R.id.restaurant_text_distance);
			holder.address = (TextView) convertView
					.findViewById(R.id.restaurant_text_address);
			holder.queueup = (TextView) convertView
					.findViewById(R.id.restaurant_text_queueup);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RestaurantInfo restaurantInfo = mRestaurantInfos.get(position);

		if (!TextUtils.isEmpty(restaurantInfo.getPictureAddress())) {
			final ImageView mImageView = holder.pic;
			ImageLoader.getInstance().displayImage(restaurantInfo.getPictureAddress(),
					mImageView, DemoApplication.options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
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
							Toast.makeText(MyAdapter.this.context, message,
									Toast.LENGTH_SHORT).show();

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							mImageView.setScaleType(ScaleType.FIT_XY);
							mImageView.setImageBitmap(loadedImage);
						}
					});
		}

		holder.name.setText(restaurantInfo.getCompanyName().trim());
		holder.distance.setText("距离：" + restaurantInfo.getDistance().trim());
		holder.address.setText("地址：" + restaurantInfo.getCompanyAddress().trim());
		holder.queueup.setText("排队人数：" + restaurantInfo.getQueueUpCount().trim());

		return convertView;
	}
}
