package com.cb.potatoclock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cb.potatoclock.adapter.TaskDoneAdapter;
import com.cb.potatoclock.adapter.TaskDoneListItem;
import com.cb.sqlite.SQLiteDao;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class TaskDoneActivity extends FragmentActivity {
	private List<TaskDoneListItem> mDatas;
	private SwipeListView mSwipeListView;
	private TaskDoneAdapter mAdapter;
	private LinearLayout ll;
	private TextView selectedTime;
	private DatePickerDialog datePickerDialog;
	private String selectedDay;
	private Calendar calendar;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("TaskDoneAcitivity", "TaskDone onDestroy()");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("TaskDoneAcitivity", "TaskDone onStop()");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_done);
		
		initView();
		
		mDatas = new ArrayList<TaskDoneListItem>();
		initDatas();
		mSwipeListView = (SwipeListView)findViewById(R.id.task_done_swiplistview);
		mAdapter = new TaskDoneAdapter(mDatas, this, mSwipeListView);
		mSwipeListView.setAdapter(mAdapter);
		
		mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener(){

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				mSwipeListView.closeOpenedItems();
				super.onStartOpen(position, action, right);
			}
			
		});
		
		Log.d("TaskDoneAcitivity", "TaskDone onCreate()");
	}
	
	private void initDatas()  
    {  
        SQLiteDao dao = new SQLiteDao(this);
        Cursor cursor = dao.queryDoneDate(selectedDay);
        while(cursor.moveToNext()){
        	String taskName = cursor.getString(cursor.getColumnIndex("task_name"));
        	int id = cursor.getInt(cursor.getColumnIndex("_id"));
        	int workingTime = cursor.getInt(cursor.getColumnIndex("time_working"));
        	long timeStart = cursor.getLong(cursor.getColumnIndex("time_start"));
        	long timeDone = cursor.getLong(cursor.getColumnIndex("time_done"));
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        	String startTime = simpleDateFormat.format(timeStart);
        	String doneTime = simpleDateFormat.format(timeDone);
        	TaskDoneListItem item = new TaskDoneListItem(taskName, workingTime, startTime, doneTime, id);
        	mDatas.add(item);
        }
        dao.closeDB();
    } 

	public void initView(){
		calendar = Calendar.getInstance(Locale.getDefault());
		
		selectedTime = (TextView)findViewById(R.id.task_done_selected_time);
		selectedDay = String.valueOf(calendar.get(Calendar.YEAR)) + 
				parseDay(calendar.get(Calendar.MONTH)+1) + parseDay(calendar.get(Calendar.DAY_OF_MONTH));
		selectedTime.setText(selectedDay);
		
		ll = (LinearLayout)findViewById(R.id.task_done_linearlayout);
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				datePickerDialog = new DatePickerDialog(TaskDoneActivity.this, new MyDateSetListener(), 
						calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
		
	}
	
	public class MyDateSetListener implements DatePickerDialog.OnDateSetListener{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			selectedDay = String.valueOf(year) + parseDay(monthOfYear+1) + parseDay(dayOfMonth);
			selectedTime.setText(selectedDay);
			mDatas.clear();
			initDatas();
			mAdapter.notifyDataSetChanged();
		}
		
	}
	
	public String parseDay(int day){
		String date;
		if(day < 10){
			date = "-0" + day;
		}else{
			date = "-" + day;
		}
		return date;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}

}
