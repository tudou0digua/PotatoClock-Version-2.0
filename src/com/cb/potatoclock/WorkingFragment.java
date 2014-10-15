package com.cb.potatoclock;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkingFragment extends Fragment {
	private TextView timer_minute,timer_second;
	private ImageButton stop_timer;
	private Button timer_done;
	private LinearLayout timer_linearlayout;
	private int TOTAL_TIME = 1;
	private CountDownTimer timer;
	private FragmentCallBack fragmentCallBack;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//将该WorkingFragment绑定的activity实例赋值给fragmentCallBack,并判断acitivity是否实现了回调用的接口
		try{
			fragmentCallBack = (MainActivity)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implements FragmentCallback Interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_work,container,false);
		
		//绑定显示倒计时分钟和秒的TextView，以及LinearLayout
		timer_minute = (TextView)view.findViewById(R.id.timer_minute);
		timer_second = (TextView)view.findViewById(R.id.timer_second);
		timer_linearlayout = (LinearLayout)view.findViewById(R.id.timer_linearlayout);
		
		//绑定取消番茄时钟的Button stop_timer和计时完成的Button timer_done
		stop_timer = (ImageButton)view.findViewById(R.id.stop_timer);
		timer_done = (Button)view.findViewById(R.id.timer_done);
		//回调MainActivity的initWorkingFragment()函数
		//返回倒计时的时间传给Total_Time，并初始化成对应的模式
		TOTAL_TIME = fragmentCallBack.initWorkingFragment(view, timer_done);
		//监听取消番茄时钟Button
		fragmentCallBack.stopPotatoClockButtonListener(stop_timer);
		//监听计时完成的Button timer_done
		fragmentCallBack.workTimesUpButtonListener(timer_done);
		//初始化倒计时
		setTimer();
		//开始倒计时
		timer.start();
		
		return view;
	}
	//初始化倒计时
	public void setTimer() {
		timer = new CountDownTimer((TOTAL_TIME+1) * 60 * 1000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				int minute = (int) millisUntilFinished / 60000;
				int second = (int) (int) (millisUntilFinished / 1000) % 60;
				if (minute < 10) {
					timer_minute.setText("0" + (minute - 1) + " : ");
				} else {
					timer_minute.setText(minute + " : ");
				}
				if(minute == 0){
					timer_linearlayout.setVisibility(View.GONE);
					timer_done.setVisibility(View.VISIBLE);
					this.cancel();
				}
				if (second < 10) {
					timer_second.setText("0" + second);
				} else {
					timer_second.setText(second + "");
				}
			};

			@Override
			public void onFinish() {
				
			}
			
		};
	}
	
}
