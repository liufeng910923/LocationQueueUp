package com.queueup.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.queueup.entity.QueueUpDetailInfo;
import com.queueup.ui.R;

/**
 * @author 历史详情
 *
 */
public class QueueUpDetailListAdapter extends BaseAdapter {
	public List<QueueUpDetailInfo> queueUpDetailInfos;
	private LayoutInflater inflater;
	/*private Context context;*/

	public QueueUpDetailListAdapter(Context context , List<QueueUpDetailInfo> queueUpDetailInfos) {
		inflater = LayoutInflater.from(context);
		this.queueUpDetailInfos = queueUpDetailInfos;
		/*this.context = context;*/
	}

	public int getCount() {
		return queueUpDetailInfos.size() > 0 ? queueUpDetailInfos.size():0;
	}

	public QueueUpDetailInfo getItem(int position) {
		return queueUpDetailInfos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setDataNotifyChangerView(List<QueueUpDetailInfo> queueUpDetailInfos) {
		this.queueUpDetailInfos = queueUpDetailInfos;
		this.notifyDataSetChanged();
	}

	// 相当于线性列表的只视图
	
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.queue_up_detail_item, null);
			holder.number = (TextView)convertView.findViewById(R.id.number);
			holder.eatTime = (TextView)convertView.findViewById(R.id.eat_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		QueueUpDetailInfo queueUpDetailInfo = this.queueUpDetailInfos.get(position);
		holder.number.setText("排队号码："+queueUpDetailInfo.getQueueNo());
		holder.eatTime.setText("叫号时间："+queueUpDetailInfo.getEatTime());
		return convertView;
	}

	class ViewHolder {
		public TextView number,queueTime,eatTime;
	}
}