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
		
		initData();
		
		swipeListView = (SwipeListView)findViewById(R.id.task_todo_swiplistview);
		adapter = new TaskToDoAdapter(data, swipeListView, this);
		swipeListView.setAdapter(adapter);
		
		swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener(){

			@Override
			public void onClickFrontView(int position) {
				super.onClickFrontView(position);
				String taskName = data.get(position).getTaskName();
				dao.deleteToDo(data.get(position).getId());
				
				Intent intent = new Intent();
				intent.putExtra("task_name",taskName);
				setResult(1000,intent);
				finish();
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				swipeListView.closeOpenedItems();
				super.onStartOpen(position, action, right);
			}
			
		});
		
		taskNameEditView = (EditText)findViewById(R.id.task_todo_name_edittext);
		submitTaskName = (Button)findViewById(R.id.task_todo_name_submit);
		submitTaskName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = taskNameEditView.getText().toString();
				dao.addToDo(name);
				dataAddNewItem();
				taskNameEditView.setText(null);
				taskNameEditView.clearFocus();
				adapter.notifyDataSetChanged();
				swipeListView.closeOpenedItems();
				((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(TaskToDoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
		});
	}

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
	
	public void dataAddNewItem(){
		Cursor cursor = dao.queryToDoList();
		cursor.moveToLast();
		String taskName = cursor.getString(cursor.getColumnIndex("task_name"));
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		TaskToDoListItem item = new TaskToDoListItem(id, taskName);
		data.add(item);
		dao.closeDB();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}

}
