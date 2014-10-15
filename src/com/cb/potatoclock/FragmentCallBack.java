package com.cb.potatoclock;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public interface FragmentCallBack {

	public void launchOnClickListener(View view);
	public void workTimesUpButtonListener(View view);
	public void stopPotatoClockButtonListener(View view);
	public void recordTaskButtonListener(View view);
	public int getWorkTimeValue();
	public int getShortRestTimeValue();
	public int getLongRestTimeValue();
	public int initWorkingFragment(View view,TextView workStatus, Button button, Button recordTask, ImageButton stopTimer, LinearLayout addImage);
}
