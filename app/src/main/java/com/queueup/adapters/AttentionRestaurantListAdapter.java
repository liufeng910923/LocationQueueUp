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
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.queueup.conf.DemoApplication;
import com.queueup.entity.AttentionRestaurantInfo;
import com.queueup.entity.RestaurantInfo;
import com.queueup.ui.R;
import com.queueup.untility.LogUtils;

/**
 * @author 查看关注列表的适配器
 * 
 */
public class AttentionRestaurantListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<AttentionRestaurantInfo> attentionRestaurantInfos;

	public AttentionRestaurantListAdapter(Context context, List<AttentionRestaurantInfo> attentionRestaurantInfos) {
		inflater = LayoutInflater.from(context);
		this.attentionRestaurantInfos = attentionRestaurantInfos;
	}

	public int getCount() {
		return attentionRestaurantInfos.size() > 0 ? attentionRestaurantInfos.size() : 0;
	}

	public AttentionRestaurantInfo getItem(int position) {
		return attentionRestaurantInfos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setDataNotifyChangerView(List<AttentionRestaurantInfo> attentionRestaurantInfos) {
		this.attentionRestaurantInfos = attentionRestaurantInfos;
		this.notifyDataSetChanged();
	}

	// 相当于线性列表的只视图
	ViewHolder holder = null;
	RestaurantInfo collect;

	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.attention_restaurant_item, null);
			holder.pic = (ImageView) convertView.findViewById(R.id.img_icon);
			holder.name = (TextView) convertView.findViewById(R.id.text_Name);
			holder.queueState = (ImageView) convertView.findViewById(R.id.text_distance_item);
			holder.address = (TextView) convertView.findViewById(R.id.text_address);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.queueup = (TextView) convertView.findViewById(R.id.text_queueup);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AttentionRestaurantInfo attentionRestaurantInfo = attentionRestaurantInfos.get(position);

		if (!TextUtils.isEmpty(attentionRestaurantInfo.getPic())) {
			final ImageView mImageView = holder.pic;
			ImageLoader.getInstance().displayImage(attentionRestaurantInfo.getPic(), mImageView, DemoApplication.options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

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
					LogUtils.e(message);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

					mImageView.setScaleType(ScaleType.FIT_XY);
					mImageView.setImageBitmap(loadedImage);
				}
			});
		}

		/* holder.pic.setImageResource(restaurantInfo.getPic()); */
		holder.name.setText(attentionRestaurantInfo.getName());

		if ("true".equals(attentionRestaurantInfo.getQueueStatus())) {

			holder.queueState.setImageResource(attentionRestaurantInfo.getQueueState());
		} else {

			holder.queueState.setVisibility(View.GONE);

		}

		holder.address.setText("地址:" + attentionRestaurantInfo.getAddress());
		holder.imageView.setImageResource(attentionRestaurantInfo.getImageView());
		holder.queueup.setText("排队人数:" + attentionRestaurantInfo.getQueueup());

		return convertView;
	}

	class ViewHolder {
		public ImageView pic, imageView, queueState;
		public RatingBar ratingBar;
		public TextView name, address, queueup;
	}
}