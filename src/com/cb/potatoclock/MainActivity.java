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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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
				WorkingFragment workingFragment = new WorkingFragment();
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
				
				ft.replace(R.id.launchFrameLayout,workingFragment);
				
				Log.d("PotatoNumber", POTATO_NUMBER+"");
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	//回调函数，监听WokingFragment取消番茄时钟按钮
	@Override
	public void stopPotatoClockButtonListener(View view) {
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
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
	public int initWorkingFragment(View view, TextView workStatus, Button button, Button recordTask, ImageButton stopTimer, LinearLayout addImage) {
		//
		addStatusImage(addImage, WORKING_TYPE);
		//
		switch(WORKING_TYPE){
		case WORKING:
			view.setBackgroundColor(Color.parseColor("#CC0033"));
			workStatus.setText("工作中...");
			
			if(POTATO_NUMBER%4 == 0){
				button.setText("长休息");
			}else{
				button.setText("短休息");
			}
			return WORK_TIME;
		case SHORT_REST:
			view.setBackgroundColor(Color.parseColor("#3399CC"));
			workStatus.setText("短休息中...");
			button.setText("工作");
			recordTask.setVisibility(View.VISIBLE);
			stopTimer.setVisibility(View.GONE);
			return SHORT_REST_TIME;
		case LONG_REST:
			view.setBackgroundColor(Color.parseColor("#336699"));
			workStatus.setText("长休息中...");
			button.setText("工作");
			recordTask.setVisibility(View.VISIBLE);
			stopTimer.setVisibility(View.GONE);
			return LONG_REST_TIME;
		default:
			return 90;
		}

	}
	//添加表示工作状态的图标
	public void addStatusImage(LinearLayout ll, int status){
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		ImageView statusImage = new ImageView(MainActivity.this);
		if(status == 0){
			statusImage.setImageResource(R.drawable.tomato);
		}else if(status == 1){
			statusImage.setImageResource(R.drawable.shortrest);
		}else{
			statusImage.setImageResource(R.drawable.longrest);
		}
		lp.setMargins(5, 0, 0, 0);
		ll.addView(statusImage, lp);
		
	}
	
	//番茄工作时钟完成后，单击完成按钮，能记录任务的详细信息
	@Override
	public void recordTaskButtonListener(View view) {
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
