package com.cb.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.cb.potatoclock.MainActivity;
import com.cb.potatoclock.R;

public class PotatoClockService extends Service {
	Notification notification;
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		showNotification();
		
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		startForeground(0, notification);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@SuppressLint("NewApi")
	private void showNotification(){
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification = new Notification.Builder(this)
		.setContent(remoteViews)
		.setContentIntent(pendingIntent)
		.setSmallIcon(R.drawable.logo_notif_s)
		.setTicker("开始工作了")
		.setWhen(System.currentTimeMillis())
		.build();
		

		notificationManager.notify(0,notification);
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
