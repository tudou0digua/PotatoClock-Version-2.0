package com.cb.potatoclock;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cb.potatoclock.adapter.TaskToDoAdapter;
import com.cb.potatoclock.adapter.TaskToDoListItem;
import com.cb.sqlite.SQLiteDao;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class TaskToDoActivity extends FragmentActivity {
	private List<TaskToDoListItem> data;
	private TaskToDoAdapter adapter;
	private SwipeListView swipeListView;
	private EditText taskNameEditView;
	private Button submitTaskName;
	private SQLiteDao dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_todo);
		
		dao = new SQLiteDao(this);
		//初始化swipeListView的item要显示的数据
		initData();
		//绑定swipelistview,并给其绑定Adapter
		swipeListView = (SwipeListView)findViewById(R.id.task_todo_swiplistview);
		adapter = new TaskToDoAdapter(data, swipeListView, this);
		swipeListView.setAdapter(adapter);
		//监听swipelistView的操作
		swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener(){
			//单击未完成任务item，启动番茄工作（返回taskName的值，在SlidngMenuFragment中启动）
			@Override
			public void onClickFrontView(int position) {
				super.onClickFrontView(position);
				String taskName = data.get(position).getTaskName();
				//在未完成任务中删除对应任务名
				dao.deleteToDo(data.get(position).getId());
				//返回taskName
				Intent intent = new Intent();
				intent.putExtra("task_name",taskName);
				setResult(RESULT_OK,intent);
				//返回值后，结束TaskToDoActivity
				finish();
			}
			//item开始滑动时，关闭之前打开的item
			@Override
			public void onStartOpen(int position, int action, boolean right) {
				swipeListView.closeOpenedItems();
				super.onStartOpen(position, action, right);
			}
			
		});
		//绑定输入任务名的edittext和提交button
		taskNameEditView = (EditText)findViewById(R.id.task_todo_name_edittext);
		submitTaskName = (Button)findViewById(R.id.task_todo_name_submit);
		//监听提交button
		submitTaskName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = taskNameEditView.getText().toString();
				dao.addToDo(name);
				//给存储item显示数据的data增加提交的数据
				dataAddNewItem();
				//清空editText
				taskNameEditView.setText(null);
				//使editText失去焦点
				taskNameEditView.clearFocus();
				//通知adapter数据的改变，刷新数据
				adapter.notifyDataSetChanged();
				swipeListView.closeOpenedItems();
				//关闭输入软键盘
				((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(TaskToDoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}
	//初始化data
	public void initData(){
		data = new ArrayList<TaskToDoListItem>();
		Cursor cursor = dao.queryToDoList();
		while(cursor.moveToNext()){
			String taskName = cursor.getString(cursor.getColumnIndex("task_name"));
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			TaskToDoListItem item = new TaskToDoListItem(id, taskName);
			data.add(item);
		}
		dao.closeDB();
	}
	//给存储item显示数据的data增加提交的数据
	public void dataAddNewItem(){
		Cursor cursor = dao.queryToDoList();
		cursor.moveToLast();
		String taskName = cursor.getString(cursor.getColumnIndex("task_name"));
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		TaskToDoListItem item = new TaskToDoListItem(id, taskName);
		data.add(item);
		dao.closeDB();
	}
	//重写onSaveInstanceState，以免fragment不能被destory
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}

}
