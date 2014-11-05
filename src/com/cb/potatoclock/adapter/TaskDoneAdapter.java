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
import com.cb.sqlite.SQLiteDao;
import com.fortysevendeg.swipelistview.SwipeListView;

public class TaskDoneAdapter extends BaseAdapter {
	private List<TaskDoneListItem> mDatas;
	private LayoutInflater mInflater;
	private SwipeListView mSwipeListView;
	private Context context;
	
	public TaskDoneAdapter(List<TaskDoneListItem> mDatas, Context context,
			SwipeListView mSwipeListView) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.mInflater = LayoutInflater.from(context);
		this.mSwipeListView = mSwipeListView;
	}

	private class ViewHolder{
		Button taskNumber;
		Button remove;
		TextView taskName;
		TextView taskWorkingTime;
		TextView taskStartTime;
		TextView taskDoneTime;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
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
			viewHolder.remove = (Button)convertView.findViewById(R.id.id_remove);
			viewHolder.taskNumber = (Button)convertView.findViewById(R.id.task_number);
			viewHolder.taskName = (TextView)convertView.findViewById(R.id.task_name);
			viewHolder.taskWorkingTime = (TextView)convertView.findViewById(R.id.task_working_time);
			viewHolder.taskStartTime = (TextView)convertView.findViewById(R.id.task_start_time);
			viewHolder.taskDoneTime = (TextView)convertView.findViewById(R.id.task_done_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//TODO
		viewHolder.taskNumber.setText(String.valueOf(position + 1));
		viewHolder.taskName.setText(mDatas.get(position).getTaskName());
		viewHolder.taskWorkingTime.setText(mDatas.get(position).getWorkingTime() + "分钟");
		viewHolder.taskStartTime.setText(mDatas.get(position).getStartTime());
		viewHolder.taskDoneTime.setText(mDatas.get(position).getDoneTime());
		//监听删除按钮操作
		viewHolder.remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//删除数据库中对应数据
				SQLiteDao dao = new SQLiteDao(context);
				dao.deleteDone(mDatas.get(position).getId());
				//删除mDatas中对应的数据
				mDatas.remove(position);
				//关闭打开的itme，不然刷新界面可能会出错
				mSwipeListView.closeOpenedItems();
				//T通知adapter刷新界面
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

}
