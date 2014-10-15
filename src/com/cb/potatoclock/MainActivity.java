package com.cb.potatoclock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity implements FragmentCallBack{

	private LaunchFragment launchFragment;
	private FragmentManager fragmentManager;
	public static int WORK_TIME = 25;
	public static int LONG_REST_TIME = 25;
	public static int SHORT_REST_TIME = 5;
	private final int WORKING = 0;
	private final int SHORT_REST = 1;
	private final int LONG_REST = 2;
	public int WORKING_TYPE = WORKING;
	public int POTATO_NUMBER = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fragmentManager = getFragmentManager();
		
		launchFragment = new LaunchFragment();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(R.id.launchFrameLayout, launchFragment);
		ft.commit();
		
	}
	//回调函数，实现LaunchFragment页面launch按钮的监听
	@Override
	public void launchOnClickListener(View view) {
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				ft.replace(R.id.launchFrameLayout,new WorkingFragment());
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	//回调函数，监听WokingFragment倒计时完成按钮
	@Override
	public void workTimesUpButtonListener(View view) {
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				if(WORKING_TYPE == WORKING){
					if(POTATO_NUMBER%4 == 0){
						WORKING_TYPE = LONG_REST;
					}else{
						WORKING_TYPE = SHORT_REST;
					}
					POTATO_NUMBER++;
				}else{
					WORKING_TYPE = WORKING;
				}
				
				ft.replace(R.id.launchFrameLayout,new WorkingFragment());
				
				Log.d("PotatoNumber", POTATO_NUMBER+"");
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	//回调函数，监听WokingFragment取消番茄时钟按钮
	@Override
	public void stopPotatoClockButtonListener(View view) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getWorkTimeValue() {
		return WORK_TIME;
	}

	@Override
	public int getShortRestTimeValue() {
		return SHORT_REST_TIME;
	}

	@Override
	public int getLongRestTimeValue() {
		return LONG_REST_TIME;
	}
	//回调函数，初始化WorkingFragment界面
	@Override
	public int initWorkingFragment(View view, Button button) {
		
		switch(WORKING_TYPE){
		case WORKING:
			view.setBackgroundColor(Color.parseColor("#9966CC"));
			if(POTATO_NUMBER%4 == 0){
				button.setText("长休息");
			}else{
				button.setText("短休息");
			}
			return WORK_TIME;
		case SHORT_REST:
			view.setBackgroundColor(Color.parseColor("#3399CC"));
			button.setText("工作");
			return SHORT_REST_TIME;
		case LONG_REST:
			view.setBackgroundColor(Color.parseColor("#336699"));
			button.setText("工作");
			return LONG_REST_TIME;
		default:
			return 100;
		}

	}
}
