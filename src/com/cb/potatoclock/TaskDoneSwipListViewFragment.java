package com.cb.potatoclock;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb.potatoclock.adapter.DataAdapter;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class TaskDoneSwipListViewFragment extends Fragment {
	private List<String> mDatas;
	private SwipeListView mSwipeListView;
	private DataAdapter mAdapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.task_done_swiplistview, container,false);
		
		initDatas();
		mSwipeListView = (SwipeListView)view.findViewById(R.id.task_done_swiplistview);
		mAdapter = new DataAdapter(mDatas, getActivity(), mSwipeListView);
		mSwipeListView.setAdapter(mAdapter);
		
		mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener());
		
		return view;
	}

	private void initDatas()  
    {  
        mDatas = new ArrayList<String>();  
        for (int i = 'A'; i <= 'Z'; i++)  
            mDatas.add((char) i + "");  
    } 
	
}
