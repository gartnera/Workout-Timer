package com.agartner.workouttimer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by alex on 11/6/2015.
 */
public class TimerService extends Service{

	private final IBinder mBinder = new LocalBinder();
	private TimerCallbacks serviceCallbacks;

	private int mMinutes;
	private int mSeconds;

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
}
