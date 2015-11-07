package com.agartner.workouttimer;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements TimerCallbacks{
	TimerService mService;
	boolean mServiceBound;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,
									   IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			TimerService.LocalBinder binder = (TimerService.LocalBinder) service;
			mService = binder.getService();
			mService.setCallbacks(MainActivity.this);
			mServiceBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mServiceBound = false;
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	@Override
	protected void onStart()
	{
		super.onStart();

		Intent intent = new Intent(this,TimerService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mServiceBound)
		{
			unbindService(mConnection);
			mServiceBound = false;
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	public void timePickerResult(int seconds)
	{
		mService.setTime(seconds);
	}

	/*** TimerCallback Impl ***/
	public void updateTime()
	{
		TextView timer = (TextView) findViewById(R.id.textMainTimer);

		int minutes = mService.getMinutes();
		int pSeconds = mService.getSeconds();
		String timerText = String.format("%d:%02d", minutes, pSeconds);
		timer.setText(timerText);
	}
	public void timerFinished()
	{
		Button btn = (Button) findViewById(R.id.btn_start);
		btn.setText(R.string.stop);
	}

	/*** Click Handlers ***/
	public void timerSelected(View view)
	{
		Bundle params= new Bundle();
		params.putInt("seconds", mService.getTotalSeconds());

		DialogFragment newFragment = new TimePickerFragment();
		newFragment.setArguments(params);
		newFragment.show(getFragmentManager(), "timePicker");
	}
	public void onStartButtonClicked(View view)
	{
		Button btn = (Button) findViewById(R.id.btn_start);
		if (mService.isRunning())
		{
			mService.stopTimer();
			btn.setText(R.string.start);
		}
		else
		{
			mService.startTimer();
			btn.setText(R.string.stop);
		}
	}

}
