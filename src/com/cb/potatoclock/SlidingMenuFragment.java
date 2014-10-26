package com.cb.potatoclock;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SlidingMenuFragment extends Fragment {
	private LinearLayout taskDoneLinearLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sliding_menu_content, container,false);
		
		taskDoneLinearLayout = (LinearLayout)view.findViewById(R.id.task_done_linearlayout);
		taskDoneLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),TaskDoneActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

}
