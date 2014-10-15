package com.cb.potatoclock;

import android.view.View;
import android.widget.Button;

public interface FragmentCallBack {

	public void launchOnClickListener(View view);
	public void workTimesUpButtonListener(View view);
	public void stopPotatoClockButtonListener(View view);
	public int getWorkTimeValue();
	public int getShortRestTimeValue();
	public int getLongRestTimeValue();
	public int initWorkingFragment(View view,Button button);
}
