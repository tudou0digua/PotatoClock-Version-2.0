package com.cb.potatoclock.recevier;

import com.cb.potatoclock.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent show = new Intent(context, MainActivity.class);
		show.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(show);
		Log.d("Recevier", "my_recevier");
	}

}
