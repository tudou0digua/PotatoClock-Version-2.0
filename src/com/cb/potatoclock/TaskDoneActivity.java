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
	private int year,month,day;  //保存年月日数据，使DataPickerDialog再次查询时，能定位到上次查询的日期
	
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
		//初始化显示所查询任务的日期
		initView();
		//初始化存储item数据的mDatas
		mDatas = new ArrayList<TaskDoneListItem>();
		initDatas();
		//绑定mSwipeListView，并监听其操作
		mSwipeListView = (SwipeListView)findViewById(R.id.task_done_swiplistview);
		mAdapter = new TaskDoneAdapter(mDatas, this, mSwipeListView);
		mSwipeListView.setAdapter(mAdapter);
		//监听mSwipeListView的操作
		mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener(){
			//item开始滑动时，关闭之前打开的item
			@Override
			public void onStartOpen(int position, int action, boolean right) {
				mSwipeListView.closeOpenedItems();
				super.onStartOpen(position, action, right);
			}
			
		});
		
		Log.d("TaskDoneAcitivity", "TaskDone onCreate()");
	}
	//初始化存储item数据的mDatas
	private void initDatas()  
    {  
        SQLiteDao dao = new SQLiteDao(this);
        //查询指定日期（selectedDay）的数据
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
	////初始化显示所查询任务的日期
	public void initView(){
		calendar = Calendar.getInstance(Locale.getDefault());
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		//显示当天日期
		selectedTime = (TextView)findViewById(R.id.task_done_selected_time);
		selectedDay = String.valueOf(calendar.get(Calendar.YEAR)) + 
				parseDay(calendar.get(Calendar.MONTH)+1) + parseDay(calendar.get(Calendar.DAY_OF_MONTH));
		selectedTime.setText(selectedDay);
		//监听显示日期的LinearLayout的单击，若单击，弹出DatePickerDailog
		ll = (LinearLayout)findViewById(R.id.task_done_linearlayout);
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				datePickerDialog = new DatePickerDialog(TaskDoneActivity.this, new MyDateSetListener(), 
						year, month, day);
				datePickerDialog.show();
			}
		});
		
	}
	//监听DataPickerDialog日期的改变
	public class MyDateSetListener implements DatePickerDialog.OnDateSetListener{
		//监听DataPickerDialog设置按钮的操作
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			selectedDay = String.valueOf(year) + parseDay(monthOfYear+1) + parseDay(dayOfMonth);
			selectedTime.setText(selectedDay);
			//保存年月日数据，使DataPickerDialog再次查询时，能定位到上次查询的日期
			setYear(year);
			setMonth(monthOfYear);
			setDay(dayOfMonth);
			//清除mDatas中保存的数据
			mDatas.clear();
			//重新查询指定日期的数据
			initDatas();
			//刷新数据显示
			mAdapter.notifyDataSetChanged();
		}
		
	}
	//若月、日 日期数小于10，在签名添0，并在月、日 日期前面添“-”
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
	//设置暂存的年月日
	private void setYear(int year) {
		this.year = year;
	}

	private void setMonth(int month) {
		this.month = month;
	}

	private void setDay(int day) {
		this.day = day;
	}
	
}
