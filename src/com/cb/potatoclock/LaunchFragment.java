package com.cb.potatoclock;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class LaunchFragment extends Fragment {

	private Dialog dialogSetDuration;
	private LinearLayout linearLayoutWork, linearLayoutShortTime,linearLayoutLongTime;
	private Button dialogCancel, dialogConfirm;
	private ImageButton launch;
	private FragmentCallBack fragmentCallBack;
	private SeekBar seekBarWorkTime,seekBarShortRestTime,seekBarLongRestTime;
	private TextView tvWorkTime,tvShortRestTime,tvLongRestTime;
	private TextView tvLaunchWorkTime,tvLaunchShortRestTime,tvLaunchLongRestTime;
	private int workTime,shortRestTime,longRestTime;
	private Bundle cacheTime= new Bundle();
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			fragmentCallBack = (MainActivity)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implements FragmentCallback Interface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.launch,container,false);
		//通过回调，设置workTime,shortRestTime,longRestTime
		workTime = fragmentCallBack.getWorkTimeValue();
		shortRestTime = fragmentCallBack.getShortRestTimeValue();
		longRestTime = fragmentCallBack.getLongRestTimeValue();
		//设置cacheTime初值
		cacheTime.putInt("work", workTime);
		cacheTime.putInt("shortrest", shortRestTime);
		cacheTime.putInt("longrest", longRestTime);
		//绑定launch页面的3个显示时间的TextView
		tvLaunchWorkTime = (TextView)view.findViewById(R.id.textview_worktime);
		tvLaunchShortRestTime = (TextView)view.findViewById(R.id.textview_shortresttime);
		tvLaunchLongRestTime = (TextView)view.findViewById(R.id.textview_longresttime);
		//设置launch页面的3个显示时间的TextView的初始值
		setLuanchTimeTextView(workTime, shortRestTime, longRestTime);
		// 生成设置时间的Dialog
		myDialog();
		//绑定开始页面的3个LinearLayout
		linearLayoutWork = (LinearLayout) view.findViewById(R.id.linearlayout_work);
		linearLayoutShortTime = (LinearLayout) view.findViewById(R.id.linearlayout_shortrest);
		linearLayoutLongTime = (LinearLayout) view.findViewById(R.id.linearlayout_longrest);
		//监听这3个LinearLayout
		linearLayoutWork.setOnClickListener(new LinearLayoutListener());
		linearLayoutShortTime.setOnClickListener(new LinearLayoutListener());
		linearLayoutLongTime.setOnClickListener(new LinearLayoutListener());
		//开始番茄工作法，监听launch按钮
		launch = ((ImageButton)view.findViewById(R.id.ready_go));
		fragmentCallBack.launchOnClickListener(launch);
		
		
		return view;
	}
	
	class LinearLayoutListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// 得到Dialog所在的当前窗口
			Window window = dialogSetDuration.getWindow();
			// 得到屏幕的尺寸
			WindowManager.LayoutParams layoutParam = window.getAttributes();
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			// 将Dialog的宽设为，屏幕宽的90%
			layoutParam.width = (int) (dm.widthPixels * 0.9);
			// 设置自定义好的dialog窗口属性
			window.setAttributes(layoutParam);
			// 显示Dialog
			dialogSetDuration.show();
			// 在当前Dialog window 下绑定Button
			// 设置Dialog 的取消按钮
			dialogCancel = (Button) window.findViewById(R.id.dialog_button_cancel);
			dialogCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogSetDuration.dismiss();
				}
			});
			// 设置Dialog的确定按钮
			dialogConfirm = (Button) window.findViewById(R.id.dialog_button_confirm);
			dialogConfirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//取出cacheTime中的数据
					workTime = cacheTime.getInt("work");
					MainActivity.WORK_TIME = workTime;
					shortRestTime = cacheTime.getInt("shortrest");
					MainActivity.SHORT_REST_TIME = shortRestTime;
					longRestTime = cacheTime.getInt("longrest");
					MainActivity.LONG_REST_TIME = longRestTime;
					//设置luanch页面中的有关时间的TextView
					setLuanchTimeTextView(workTime,shortRestTime,longRestTime);
					dialogSetDuration.dismiss();
				}
			});
			//绑定dialog中显示时间的TextView
			tvWorkTime = (TextView)window.findViewById(R.id.tvWorkTime);
			tvShortRestTime = (TextView)window.findViewById(R.id.tvShortRestTime);
			tvLongRestTime = (TextView)window.findViewById(R.id.tvLongRestTime);
			//设定dialog中显示时间的TextView的初始值
			setDialogTimeTextView(workTime, shortRestTime, longRestTime);
			//绑定Dialog中3个SeekBar，并监听
			seekBarWorkTime = (SeekBar)window.findViewById(R.id.seekBarWorkTime);
			seekBarShortRestTime = (SeekBar)window.findViewById(R.id.seekBarShortRestTime);
			seekBarLongRestTime = (SeekBar)window.findViewById(R.id.seekBarLongRestTime);
			//设置3个seekBar的初始值
			seekBarWorkTime.setProgress(workTime-1);
			seekBarShortRestTime.setProgress(shortRestTime-1);
			seekBarLongRestTime.setProgress(longRestTime-1);
			//生成OnSeekBarChangeListener
			SeekBarListener seekBarListener = new SeekBarListener();
			seekBarWorkTime.setOnSeekBarChangeListener(seekBarListener);
			seekBarShortRestTime.setOnSeekBarChangeListener(seekBarListener);
			seekBarLongRestTime.setOnSeekBarChangeListener(seekBarListener);
			
		}
	}
	//实现OnSeekBarChangeListener接口，监听Dialog中3个SeekBar
	public class SeekBarListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int time = progress+1;
			switch(seekBar.getId()){
			case R.id.seekBarWorkTime:
				tvWorkTime.setText(time+"");
				cacheTime.putInt("work", time);
				break;
			case R.id.seekBarShortRestTime:
				tvShortRestTime.setText(time+"");
				cacheTime.putInt("shortrest", time);
				break;
			case R.id.seekBarLongRestTime:
				tvLongRestTime.setText(time+"");
				cacheTime.putInt("longrest", time);
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
		
	}
	// 设置工作时间等的Dialog
	private void myDialog() {
		dialogSetDuration = new Dialog(getActivity(),R.style.dialogSetDuration);
		dialogSetDuration.setContentView(R.layout.dialog_setduration);
	}
	//设置luanch页面有关时间的TextView
	private void setLuanchTimeTextView(int work,int shortrest,int longrest){
		tvLaunchWorkTime.setText(work+"分钟");
		tvLaunchShortRestTime.setText(shortrest+"分钟");
		tvLaunchLongRestTime.setText(longrest+"分钟");
	}
	//设置dialog页面有关时间的TextView
	private void setDialogTimeTextView(int work,int shortrest,int longrest){
		tvWorkTime.setText(String.valueOf(work));
		tvShortRestTime.setText(String.valueOf(shortrest));
		tvLongRestTime.setText(String.valueOf(longrest));	
	}

}
