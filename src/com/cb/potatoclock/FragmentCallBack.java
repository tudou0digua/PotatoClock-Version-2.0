package com.cb.potatoclock;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
//回调接口，MainActivity实习该接口，以便，fragment和MainActivity之间通信
public interface FragmentCallBack {
	
	public void openSLidingMenu();
	public void closeSlidingMenu();
	public void startTask(String taskName);
	public void launchOnClickListener(View view, EditText taskName);
	public void workTimesUpButtonListener(View view);
	public void stopPotatoClockButtonListener(View view);
	public void recordTaskButtonListener(View view);
	public int getWorkTimeValue();
	public int getShortRestTimeValue();
	public int getLongRestTimeValue();
	public int initWorkingFragment(View view,TextView workStatus, Button button, Button recordTask, ImageButton stopTimer, LinearLayout addImage, TextView showTask);
}
