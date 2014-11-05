package com.cb.potatoclock;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cb.view.CircleProgressBarView;

public class WorkingFragment extends Fragment {
	private TextView timer_minute,timer_second,status_textview,show_task;
	private ImageButton stop_timer;
	private Button timer_done,record_task;
	private LinearLayout timer_linearlayout,status_image_linearlayout;
	private int TOTAL_TIME = 1;
	private long COUNTER_TIME = 0;
	private mCountDownTimer timer;
	private FragmentCallBack fragmentCallBack;
	private CircleProgressBarView circleProgressBar;
	private long millisLeft = 0;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Vibrator vibrator;
	private SoundPool soundPool;
	private int soundId;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("WorkingFragment","OnActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart() {
		Log.d("WorkingFragment","OnStart()");
		super.onStart();
	}
	@Override
	public void onResume() {
		Log.d("WorkingFragment","OnResume()");
		super.onResume();
	}
	
	@Override
	public void onLowMemory() {
		Log.d("WorkingFragment","OnLowMemory()");
		super.onLowMemory();
	}
	@Override
	public void onDestroyView() {
		Log.d("WorkingFragment","OnDestoryView()");
		super.onDestroyView();
	}
	@Override
	public void onDestroy() {
		Log.d("WorkingFragment","OnDestory()");
		super.onDestroy();
	}
	@Override
	public void onDetach() {
		Log.d("WorkingFragment","OnDetach()");
		super.onDetach();
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//将该WorkingFragment绑定的activity实例赋值给fragmentCallBack,并判断acitivity是否实现了回调用的接口
		try{
			fragmentCallBack = (MainActivity)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implements FragmentCallback Interface");
		}
		
		Log.d("WorkingFragment","OnAttach()");
	}
	//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences("pomodoro_time", Context.MODE_PRIVATE);
		editor = sp.edit();
		Log.d("WorkingFragment","OnCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_work,container,false);
		//绑定倒计时页面（WorkingFragment）显示任务名的TextView
		show_task = (TextView)view.findViewById(R.id.show_task);
		//绑定显示倒计时分钟和秒的TextView，以及LinearLayout
		timer_minute = (TextView)view.findViewById(R.id.timer_minute);
		timer_second = (TextView)view.findViewById(R.id.timer_second);
		timer_linearlayout = (LinearLayout)view.findViewById(R.id.timer_linearlayout);
		//绑定放置多少个番茄钟图片的LinearLayout
		status_image_linearlayout = (LinearLayout)view.findViewById(R.id.status_image_linearlayout);
		//绑定取消番茄时钟的Button stop_timer和计时完成的Button timer_done
		stop_timer = (ImageButton)view.findViewById(R.id.stop_timer);
		timer_done = (Button)view.findViewById(R.id.timer_done);
		//绑定显示工作状态的TextView
		status_textview = (TextView)view.findViewById(R.id.status_textview);
		//绑定提交任务记录任务详细信息按钮
		record_task = (Button)view.findViewById(R.id.record_task);
		fragmentCallBack.recordTaskButtonListener(record_task);
		//回调MainActivity的initWorkingFragment()函数
		//返回倒计时的时间传给Total_Time，并初始化成对应的模式
		TOTAL_TIME = fragmentCallBack.initWorkingFragment(view, status_textview, timer_done, record_task, stop_timer, status_image_linearlayout, show_task);
		//COUNTER_TIME记录倒计时的总时间（一开始用的，TOTAL_TIME,为了更好的存储计时时间，采用了counter_timer,
		//由于TOTAL_TIME在circleProgressVBar中有使用，所以没弃用TOTAL_TIME，以后可以改进，统一起来）
		COUNTER_TIME = sp.getLong("counter_time", 5400000);
		//判断killed_tag_2标记是否为1，为1则表示程序被杀死过，给COUNTER_TIME赋予counter_time_left。killed_tag_2重新赋值为0
		int tag = sp.getInt("killed_tag_2", 0);
		if( tag == 1){
			COUNTER_TIME = sp.getLong("counter_time_left", 5400000);
			editor.putInt("killed_tag_2", 0);
			editor.putLong("task_done_time", System.currentTimeMillis());
			editor.commit();
		}
		//监听取消番茄时钟Button
		fragmentCallBack.stopPotatoClockButtonListener(stop_timer);
		//监听计时完成的Button timer_done
		fragmentCallBack.workTimesUpButtonListener(timer_done);
		//添加倒计时的 ProgressBar
		circleProgressBar = (CircleProgressBarView)view.findViewById(R.id.circleProgressBar);
		circleProgressBar.setMax(TOTAL_TIME*60);
		circleProgressBar.setRadius(dip2px(getActivity(), 110));
		circleProgressBar.setCircleWidth(dip2px(getActivity(), 7));
		//初始化倒计时
		setTimer();
		//开始倒计时
		timer.start();
		Log.d("WorkingFragment","OnCreateView()");
		
		return view;
	}
	//根据屏幕density dp转px
	public static int dip2px(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue*scale + 0.5f);
	}
	//初始化倒计时
	public void setTimer() {
		timer = new MyTimer(COUNTER_TIME,1000);
	}
	//自定义倒计时类mCountDownTimer,因为官方的CountDownTimer在onTick()中调用cancel()方法失效
	//而不在onCraeteView中调用cancel()方法是有用的，所以使用了大神写的mCountDownTimer类
	public class MyTimer extends mCountDownTimer{

		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			millisLeft = millisUntilFinished;
			//得到用于显示的分钟和秒
			int minute = (int) millisUntilFinished / 60000;
			int second = (int) (int) (millisUntilFinished / 1000) % 60;
			//刷新circleProgressBar的进度条显示
			circleProgressBar.setProgress((int)(millisUntilFinished - 60000)/1000);
			//分钟小于10时，前面添0
			if (minute < 10) {
				timer_minute.setText("0" + (minute - 1) + " : ");
			} else {
				timer_minute.setText((minute - 1) + " : ");
			}
			//因为倒计时完成，盗用onFinish()会暂停时间较长，所以在到计时还剩一分钟时，结束倒计时(前面倒计时总时间有加1分钟)
			//其实，倒计时总时间加1.2秒就行，之前没有考虑好，有待改进
			if(minute == 0){
				timer_linearlayout.setVisibility(View.GONE);
				circleProgressBar.setVisibility(View.GONE);
				stop_timer.setVisibility(View.GONE);
				timer_done.setVisibility(View.VISIBLE);
				record_task.setVisibility(View.VISIBLE);
				editor.putLong("task_done_time", System.currentTimeMillis()).commit();
				
				Log.d("timer", "timer_times_up");
				//开始震动
				startVibrator();
				//播放响铃
				playMusic();
				//当前Activity后台运行时，发送广播，在广播中启动该Activity，使之前台显示
				sendBroadCast();
				//取消倒计时
				cancel();
			}
			//分钟小于10时，前面添0
			if (second < 10) {
				timer_second.setText("0" + second);
			} else {
				timer_second.setText(second + "");
			}
		}

		@Override
		public void onFinish() {
			timer_linearlayout.setVisibility(View.GONE);
			circleProgressBar.setVisibility(View.GONE);
			stop_timer.setVisibility(View.GONE);
			timer_done.setVisibility(View.VISIBLE);
			record_task.setVisibility(View.VISIBLE);
		}
		
	}
	//当震动打开时，倒计时完成开始震动
	public void startVibrator(){
		if(sp.getBoolean("vibrator", false)){
			if(vibrator == null){
				vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			}
			vibrator.vibrate(new long[]{1000,1000,1000,1000,1000,1000}, -1);
		}
	}
	//倒计时完成时，当前Activity后台运行时，发送广播，在广播中启动该Activity，使之前台显示
	public void sendBroadCast(){
		//当前Activity后台运行时，才发送广播
		if(!isRunningForeground()){
			Intent intent = new Intent("android.intent.action.MY_BROADCAST");
			getActivity().sendBroadcast(intent);
		}
	}
	//当铃声打开时，倒计时完成开始播放铃声
	public void playMusic(){
		if(sp.getBoolean("ring",false)){
			Log.d("SoundPool", "playMusic-soundPool");
			//soundPool同时播放一个音频
			soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			soundId = soundPool.load(getActivity(), R.raw.spring, 1);
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					AudioManager audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
					int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
					//以系统音量播放铃声R.raw.spring 3次，正常速度播放
					soundPool.play(soundId, streamVolume, streamVolume, 1, 3, 1.0f);
				}
			});
		}
	}
	//判断当前运行的程序是否为PotatoClock，是返回true，否则返回false
	private boolean isRunningForeground(){
		Context context = getActivity();
		String packageName = context.getPackageName();
		String topActivityName = getTopActivityName(context);
		if(packageName != null && topActivityName != null && topActivityName.startsWith(packageName)){
			return true;
		}else{
			return false;
		}
	}
	
	//得到当前运行Activity的名称
	private String getTopActivityName(Context context){
		String name = null;
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
		if(runningTaskInfo != null){
			ComponentName componentName = runningTaskInfo.get(0).topActivity;
			name = componentName.getClassName();
		}
		return name;
	}
	//暂停时释放soundPool
	@Override
	public void onPause() {
		Log.d("WorkingFragment","OnPause");
		super.onPause();
		if(soundPool != null){
			soundPool.release();
		}
	}
	//在onStop中存储倒计时剩余时间，用于恢复倒计时
	@Override
	public void onStop() {
		super.onStop();
		editor.putLong("millisLeft", millisLeft).commit();
		
		Log.d("WorkingFragment","OnStop()");
	}
	
	
}
