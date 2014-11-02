package com.cb.potatoclock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cb.sqlite.SQLiteDao;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends Activity implements FragmentCallBack{

	private LaunchFragment launchFragment;
	private SlidingMenuFragment slidingMenuFragment;
	private SlidingMenu menu;
	private FragmentManager fragmentManager;
	public  static int WORK_TIME = 25;
	public  static int LONG_REST_TIME = 25;
	public  static int SHORT_REST_TIME = 5;
	private final int WORKING = 0;
	private final int SHORT_REST = 1;
	private final int LONG_REST = 2;
	public int WORKING_TYPE = WORKING;
	public int POTATO_NUMBER = 1;
	public String TASK_NAME = "";
	public int KILLED_TAG = 0;  //0:没有出现在倒计时页面；1：工作页面；2：短休息页面；3:长休息页面
	public SharedPreferences sharedPreferences;
	public SharedPreferences.Editor editor;
	private Vibrator vibrator;
	//onSaveInstanceState里面什么都不写，没加入到栈的fragment在被replace之后貌似就能被destory了，不然，不能被destory
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("MainActivity", "MainActivity-onSaveInstanceState()");
	}
	
	@Override
	protected void onStart() {
		Log.d("MainActivity", "MainActivity-OnStart()");
		super.onStart();
	}
	@Override
	protected void onRestart() {
		Log.d("MainActivity", "MainActivity-OnReStart()");
		super.onRestart();
	}
	@Override
	protected void onResume() {
		Log.d("MainActivity", "MainActivity-OnResume()");
		super.onResume();
		
	}
	@Override
	protected void onPause() {
		Log.d("MainActivity", "MainActivity-OnPauset()");
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		Log.d("MainActivity", "MainActivity-OnDestroy()");
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("MainActivity", "MainActivity-OnCreate()");
		super.onCreate(savedInstanceState);
		//锁屏时也能弹出Acitivity
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);
		//
		initTimeData();
		//
		slidingMenuFragment = new SlidingMenuFragment();
		//
		fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();

		if (KILLED_TAG == 0) {
			launchFragment = new LaunchFragment();
			ft.replace(R.id.launchFrameLayout, launchFragment);
		} else {
			initStatusData();
			long stopTime = sharedPreferences.getLong("stop_time", System.currentTimeMillis());
			long pastTime = System.currentTimeMillis() - stopTime;
			long millisLeft = sharedPreferences.getLong("millisLeft", 0);
			editor.putLong("counter_time_left", millisLeft - pastTime);
			// 当killed_tag_2 为1时，代表应用在计时阶段被杀死过，这样couter_time_left可以赋值给WorkingFragment的COUNTER_TIME
			editor.putInt("killed_tag_2", 1).commit();
			ft.replace(R.id.launchFrameLayout, new WorkingFragment());
		}
		//
		ft.commit();
		//
		intiSlidingMenu();
		
	}
	//
	@Override
	protected void onStop() {
		super.onStop();
		editor.putLong("stop_time", System.currentTimeMillis());
		editor.putInt("working_type",WORKING_TYPE);
		editor.putInt("potato_number", POTATO_NUMBER);
		editor.putString("task_name", sharedPreferences.getString("task_name",""));
		editor.commit();
		Log.d("MainActivity", "MainActivity-OnStop()");
	}
	//
	private void initTimeData(){
		sharedPreferences = this.getSharedPreferences("pomodoro_time", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		WORK_TIME = sharedPreferences.getInt("work_time",25);
		SHORT_REST_TIME = sharedPreferences.getInt("short_rest_time",5);
		LONG_REST_TIME = sharedPreferences.getInt("long_rest_time", 25);
		KILLED_TAG = sharedPreferences.getInt("killed_tag",0);
		editor.putInt("killed_tag_2", 0).commit();
	}
	//
	private void initStatusData(){
		WORKING_TYPE = sharedPreferences.getInt("working_type", WORKING);
		POTATO_NUMBER = sharedPreferences.getInt("potato_number", 1);
		TASK_NAME = sharedPreferences.getString("task_name", "");
	}
	//
	private void intiSlidingMenu() {
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.sliding_menu);
		fragmentManager.beginTransaction().replace(R.id.sliding_menu, slidingMenuFragment).commit();
	}

	@Override
	public void onBackPressed() {
		if(menu.isMenuShowing()){
			menu.showContent();
			
		}else{
			moveTaskToBack(false);
			//super.onBackPressed();
		}
		
	}

/*	//重写返回键，使程序后台运行
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/


	//回调函数，实现LaunchFragment页面launch按钮的监听
	@Override
	public void launchOnClickListener(View view, EditText taskName) {
		view.setOnClickListener(new LaunchButtonOnClickListener(taskName));
	}
	//重写LaunchButton的OnClickListener 接口
	public class LaunchButtonOnClickListener implements OnClickListener{
		EditText task;
		public LaunchButtonOnClickListener(EditText taskName){
			task = taskName;
		}
		
		@Override
		public void onClick(View v) {
			//得到任务名，赋值给TASK_NAME
			TASK_NAME = task.getText().toString();
			//
			launchTask();
		}
		
	}
	//
	public void launchTask() {
		String task_name;
		if ("".equals(TASK_NAME.trim()) || TASK_NAME == null) {
			task_name = "未命名任务";
		} else {
			task_name = TASK_NAME;
		}
		editor.putInt("killed_tag", 1).putString("task_name", task_name)
				.putLong("task_start_time", System.currentTimeMillis())
				.commit();
		//
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(android.R.animator.fade_in,
				android.R.animator.fade_out);
		ft.replace(R.id.launchFrameLayout, new WorkingFragment());
		ft.commit();

		// Intent intent = new Intent(MainActivity.this,PotatoClockService.class);
		// startService(intent);
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
						editor.putInt("killed_tag", 3).commit();
						WORKING_TYPE = LONG_REST;
					}else{
						editor.putInt("killed_tag", 2).commit();
						WORKING_TYPE = SHORT_REST;
					}
					
				}else{
					editor.putInt("killed_tag", 1).commit();
					WORKING_TYPE = WORKING;
					POTATO_NUMBER++;
				}
				
				ft.replace(R.id.launchFrameLayout,workingFragment);
				
				Log.d("PotatoNumber", POTATO_NUMBER+"");
				ft.addToBackStack(null);
				ft.commit();
				
				cancelVibrator();
			}
		});
	}
	//回调函数，监听WokingFragment取消番茄时钟按钮
	@Override
	public void stopPotatoClockButtonListener(View view) {
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				ft.replace(R.id.launchFrameLayout, new LaunchFragment());
				ft.commit();
				//
				clearStatus();
			}
		});
	}
	
	//回调函数，初始化WorkingFragment界面
	@Override
	public int initWorkingFragment(View view, TextView workStatus,
			Button button, Button recordTask, ImageButton stopTimer,
			LinearLayout addImage, TextView showTask) {
		//设置任务名，为空则显示“未命名任务”
		showTask.setText(sharedPreferences.getString("task_name", "未命名任务"));
		//用图像显示已经完成的番茄数和休息数
		setStatusImage(addImage);
		//
		switch (WORKING_TYPE) {
		case WORKING:
			view.setBackgroundColor(Color.parseColor("#CC0033"));
			workStatus.setText("工作中...");

			if (POTATO_NUMBER % 4 == 0) {
				button.setText("长休息");
			} else {
				button.setText("短休息");
			}
			editor.putLong("counter_time", (WORK_TIME+1)*60000).commit();
			return WORK_TIME;
		case SHORT_REST:
			view.setBackgroundColor(Color.parseColor("#3399CC"));
			workStatus.setText("短休息中...");
			button.setText("工作");
			recordTask.setVisibility(View.VISIBLE);
			stopTimer.setVisibility(View.GONE);
			editor.putLong("counter_time", (SHORT_REST_TIME+1)*60000).commit();
			return SHORT_REST_TIME;
		case LONG_REST:
			view.setBackgroundColor(Color.parseColor("#336699"));
			workStatus.setText("长休息中...");
			button.setText("工作");
			recordTask.setVisibility(View.VISIBLE);
			stopTimer.setVisibility(View.GONE);
			editor.putLong("counter_time", (LONG_REST_TIME+1)*60000).commit();
			return LONG_REST_TIME;
		default:
			return 90;
		}

	}
	//根据POTATO_NUMBER,用图像显示已经完成的番茄数和休息数
	public void setStatusImage(LinearLayout addImage){
		for(int i = 1; i <= POTATO_NUMBER; i++){
			if(i < POTATO_NUMBER){
				addStatusImage(addImage, WORKING);
				if(i%4 == 0){
					addStatusImage(addImage, LONG_REST);
				}else{
					addStatusImage(addImage, SHORT_REST);
				}
			}else{
				addStatusImage(addImage, WORKING);
				if(WORKING_TYPE == WORKING){
					break;
				}else{
					addStatusImage(addImage, WORKING_TYPE);
				}
			}
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
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				ft.replace(R.id.launchFrameLayout, new LaunchFragment());
				ft.commit();
				//
				int workingTime = POTATO_NUMBER * WORK_TIME;
				SQLiteDao dao = new SQLiteDao(MainActivity.this);
				String taskName = sharedPreferences.getString("task_name", "未命名任务");
				long startTime = sharedPreferences.getLong("task_start_time", 0);
				long doneTime = sharedPreferences.getLong("task_done_time", 0);
				dao.addDone(taskName, startTime, doneTime, workingTime);
				//
				cancelVibrator();
				clearStatus();
			}
		});
	}
	//
	public void cancelVibrator(){
		if(vibrator == null){
			vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
		}
		vibrator.cancel();
	}
	//
	public void clearStatus(){
		editor.putInt("working_type",WORKING);
		editor.putInt("potato_number", 1);
		editor.putString("task_name", "");
		editor.putInt("killed_tag", 0);
		WORKING_TYPE = WORKING;
		POTATO_NUMBER = 1;
		KILLED_TAG = 0;
	}
	//TODO
	@Override
	public void startTask(String taskName) {
/*		Fragment fragment = fragmentManager.findFragmentById(R.id.launchFrameLayout);
		if(fragment instanceof LaunchFragment){
			((LaunchFragment) fragment).getEditText().setText(taskName);
			Log.d("taskName", "aaaaaaaaaa");
		}*/
//		menu.showContent();
		TASK_NAME = taskName;
		launchTask();
	}
	
	@Override
	public void closeSlidingMenu() {
		menu.showContent();
	}
	
	// TODO
	public int getWORK_TIME() {
		return WORK_TIME;
	}

	public void setWORK_TIME(int wORK_TIME) {
		WORK_TIME = wORK_TIME;
	}

	public int getLONG_REST_TIME() {
		return LONG_REST_TIME;
	}

	public void setLONG_REST_TIME(int lONG_REST_TIME) {
		LONG_REST_TIME = lONG_REST_TIME;
	}

	public int getSHORT_REST_TIME() {
		return SHORT_REST_TIME;
	}

	public void setSHORT_REST_TIME(int sHORT_REST_TIME) {
		SHORT_REST_TIME = sHORT_REST_TIME;
	}

	// TODO
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
}
