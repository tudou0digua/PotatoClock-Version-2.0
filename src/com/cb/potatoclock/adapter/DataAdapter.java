package com.cb.potatoclock.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cb.potatoclock.R;
import com.fortysevendeg.swipelistview.SwipeListView;

public class DataAdapter extends BaseAdapter {
	private List<String> mDatas;
	private LayoutInflater mInflater;
	private SwipeListView mSwipeListView;
	
	public DataAdapter(List<String> mDatas, Context context,
			SwipeListView mSwipeListView) {
		super();
		this.mDatas = mDatas;
		this.mInflater = LayoutInflater.from(context);
		this.mSwipeListView = mSwipeListView;
	}

	private class ViewHolder{
		TextView textView;
		Button button;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.task_done_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView)convertView.findViewById(R.id.id_text);
			viewHolder.button = (Button)convertView.findViewById(R.id.id_remove);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.textView.setText(mDatas.get(position));
		viewHolder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDatas.remove(position);
				notifyDataSetChanged();
				mSwipeListView.closeOpenedItems();
			}
		});
		return convertView;
	}

}
