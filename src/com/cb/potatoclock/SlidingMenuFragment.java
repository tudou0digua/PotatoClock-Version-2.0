package com.cb.potatoclock;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class SlidingMenuFragment extends Fragment {
	private LinearLayout taskDoneLinearLayout,taskToDoLinearLayout;
	private FragmentCallBack fragmentCallBack;
	private CheckBox ring,vibrator;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
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
		View view = inflater.inflate(R.layout.sliding_menu_content, container,false);
		
		taskDoneLinearLayout = (LinearLayout)view.findViewById(R.id.task_done_linearlayout);
		taskDoneLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				fragmentCallBack.closeSlidingMenu();
				Intent intent = new Intent(getActivity(),TaskDoneActivity.class);
				startActivity(intent);
				fragmentCallBack.closeSlidingMenu();
			}
		});
		
		taskToDoLinearLayout = (LinearLayout)view.findViewById(R.id.task_todo_linearlayout);
		taskToDoLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(),TaskToDoActivity.class);
				startActivityForResult(intent, 100);
				fragmentCallBack.closeSlidingMenu();
			}
		});
		
		sp = getActivity().getSharedPreferences("pomodoro_time", Context.MODE_PRIVATE);
		editor = sp.edit();
		
		ring = (CheckBox)view.findViewById(R.id.checkbox_ring);
		vibrator = (CheckBox)view.findViewById(R.id.checkbox_vibrator);
		CheckBoxOnCheckChangeListener listener = new CheckBoxOnCheckChangeListener();
		ring.setOnCheckedChangeListener(listener);
		vibrator.setOnCheckedChangeListener(listener);
		
		initCheckBox();
		
		return view;
	}
	//
	private void initCheckBox() {
		vibrator.setChecked(sp.getBoolean("vibrator", false));
		ring.setChecked(sp.getBoolean("ring", false));
	}
	//
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("sliding", "sliding");
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			Bundle bundle = data.getExtras();
			String taskName = bundle.getString("task_name");
			fragmentCallBack.startTask(taskName);
		}
	}
	//
	public class CheckBoxOnCheckChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.checkbox_vibrator:
				if(isChecked){
					editor.putBoolean("vibrator", true).commit();
				}else{
					editor.putBoolean("vibrator", false).commit();
				}
				break;
			case R.id.checkbox_ring:
				if(isChecked){
					editor.putBoolean("ring", true).commit();
				}else{
					editor.putBoolean("ring", false).commit();
				}
				break;
			}
		}
		
	}
}
