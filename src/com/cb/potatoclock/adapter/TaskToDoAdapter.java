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

public class TaskToDoAdapter extends BaseAdapter{
	private List<TaskToDoListItem> data;
	private LayoutInflater inflater;
	private SwipeListView swipeListView;
	private Context context;
	
	public TaskToDoAdapter(List<TaskToDoListItem> data, SwipeListView swipeListView, 
			Context context) {
		super();
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.swipeListView = swipeListView;
		this.context = context;
	}
	
	public class ViewHolder{
		Button delete;
		TextView taskToDoName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
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
			convertView = inflater.inflate(R.layout.task_todo_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.delete = (Button)convertView.findViewById(R.id.task_todo_item_delete);
			viewHolder.taskToDoName = (TextView)convertView.findViewById(R.id.task_todo_item_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.taskToDoName.setText(data.get(position).getTaskName());
		
		viewHolder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SQLiteDao dao = new SQLiteDao(context);
				//删除数据库中对应的数据
				dao.deleteToDo(data.get(position).getId());
				//删除data中对应数据
				data.remove(position);
				swipeListView.closeOpenedItems();
				notifyDataSetChanged();
				
			}
		});
		
		return convertView;
	}

}
