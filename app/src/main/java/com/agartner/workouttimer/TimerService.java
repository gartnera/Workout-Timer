package com.agartner.workouttimer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

/**
 * Created by alex on 11/6/2015.
 */
public class TimerService extends Service{

	private final IBinder mBinder = new LocalBinder();
	private TimerCallbacks serviceCallbacks;

	private int mMinutes;
	private int mSeconds;

	final Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			if (mSeconds > 0)
				--mSeconds;
			else if (mMinutes > 0)
			{
				--mMinutes;
				mSeconds = 60;
			}
			else
			{
				return;
			}
			serviceCallbacks.setTime(mMinutes, mSeconds);
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

	public TimerService()
	{

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

	public void setTime(int minutes, int seconds)
	{
		mMinutes = minutes;
		mSeconds = seconds;

		serviceCallbacks.setTime(minutes, seconds);
	}

	public int getMinutes()
	{
		return mMinutes;
	}

	public int getSeconds()
	{
		return mSeconds;
	}

	public void startTimer()
	{
		timerHandler.postDelayed(timerRunnable, 1000);
	}
}
