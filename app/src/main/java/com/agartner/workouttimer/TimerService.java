package com.agartner.workouttimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import java.security.acl.NotOwnerException;

/**
 * Created by alex on 11/6/2015.
 */
public class TimerService extends Service{

	private final IBinder mBinder = new LocalBinder();
	private TimerCallbacks serviceCallbacks;

	private Notification.Builder notification;
	private NotificationManager notificationManager;

	private static final int NOTIFICATION_ID= 1;

	private int mSeconds;

	private boolean mIsRunning = false;

	final Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			if (!mIsRunning) {
				serviceCallbacks.timerFinished();
				return;
			}
			--mSeconds;
			updateNotification();
			serviceCallbacks.updateTime();
			if (mSeconds == 0){
				mIsRunning = false;
				serviceCallbacks.timerFinished();
				return;
			}
			timerHandler.postDelayed(this, 1000);
		}
	};

	public  class LocalBinder extends Binder
	{
		TimerService getService()
		{
			return TimerService.this;
		}
	}


	public void setCallbacks(TimerCallbacks callbacks)
	{
		serviceCallbacks = callbacks;
	}

	@Override
	public IBinder onBind(Intent i)
	{
		return mBinder;
	}

	public void setTime(int seconds)
	{
		mSeconds = seconds;

		serviceCallbacks.updateTime();
	}

	public int getMinutes()
	{
		return mSeconds / 60;
	}

	public int getSeconds()
	{
		return mSeconds % 60;
	}

	public int getTotalSeconds()
	{
		return mSeconds;
	}
	public String getTimeString()
	{
		return String.format("%d:%02d", getMinutes(), getSeconds());
	}

	public void startTimer()
	{
		mIsRunning = true;

		timerHandler.postDelayed(timerRunnable, 1000);
		if (notification == null)
			setupNotifications();
		updateNotification();
	}
	public void stopTimer()
	{
		mIsRunning = false;
	}

	public boolean isRunning()
	{
		return mIsRunning;
	}

	public void clearNotifications()
	{
		if (notification != null)
		{
			notificationManager.cancel(NOTIFICATION_ID);
		}

	}
	private void setupNotifications()
	{
		notification = new Notification.Builder(this)
				.setContentTitle("Workout Timer")
				.setContentText("0:00")
				.setSmallIcon(android.R.drawable.presence_online)
				.setOngoing(true)
				.setAutoCancel(false)
				.setShowWhen(false);

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private void updateNotification()
	{
		notification.setContentText(getTimeString());
		notificationManager.notify(NOTIFICATION_ID, notification.build());
	}

}
