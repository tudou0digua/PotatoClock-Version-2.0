package com.cb.potatoclock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;

public class TaskDoneActivity extends FragmentActivity implements TaskDoneCallBack{
	private FragmentManager fragmentManger;
	private TaskDoneCalendarFragment calendarFragment;
	private TaskDoneSwipListViewFragment swipListViewFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_done);
		
		fragmentManger = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManger.beginTransaction();
		calendarFragment = new TaskDoneCalendarFragment();
		swipListViewFragment = new TaskDoneSwipListViewFragment();
		
		ft.add(R.id.framlayout_task_done,calendarFragment).commit();
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}

	@Override
	public void CalendarViewOnClickListener(CalendarView calendarView) {
		
		calendarView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = fragmentManger.beginTransaction();
				ft.replace(R.id.framlayout_task_done, swipListViewFragment);
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		
/*		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				
				FragmentTransaction ft = fragmentManger.beginTransaction();
				ft.replace(R.id.framlayout_task_done, swipListViewFragment);
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				ft.addToBackStack(null);
				ft.commit();
			}
		});*/
	}
}
