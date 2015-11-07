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

	private int mSeconds;

	private boolean mIsRunning = false;

	final Handler timerHandler = new Handler();
	Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			if (!mIsRunning)
				return;
			if (mSeconds > 0) {
				--mSeconds;
			}
			else
			{
				mIsRunning = false;
				return;
			}
			serviceCallbacks.updateTime();
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

	public void startTimer()
	{
		mIsRunning = true;
		timerHandler.postDelayed(timerRunnable, 1000);
	}
	public void stopTimer()
	{
		mIsRunning = false;
	}

	public boolean isRunning()
	{
		return mIsRunning;
	}
}
