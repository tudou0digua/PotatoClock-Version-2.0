package com.cb.potatoclock;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

public class TaskDoneCalendarFragment extends Fragment {
	private CalendarView calendarView;
	private TaskDoneCallBack taskDoneCallBack;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			taskDoneCallBack = (TaskDoneActivity)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implements TaskDoneCallback Interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.task_done_calendar,container,false);
		
		calendarView = (CalendarView)view.findViewById(R.id.task_done_calendar);
		taskDoneCallBack.CalendarViewOnClickListener(calendarView);
		
		return view;
	}

}
