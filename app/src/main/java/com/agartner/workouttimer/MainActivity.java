package com.agartner.workouttimer;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void timerSelected(View view)
    {
		Bundle params= new Bundle();
		params.putInt("minutes", getMinutes());
		params.putInt("seconds", getSeconds());

        DialogFragment newFragment = new TimePickerFragment();
		newFragment.setArguments(params);
        newFragment.show(getFragmentManager(), "timePicker");
    }

	public int getMinutes()
	{
		TextView timer = (TextView) findViewById(R.id.textMainTimer);
		String timerText = (String) timer.getText();
		String[] parts = timerText.split(":");

		return Integer.parseInt(parts[0]);
	}

	public int getSeconds()
	{
		TextView timer = (TextView) findViewById(R.id.textMainTimer);
		String timerText = (String) timer.getText();
		String[] parts = timerText.split(":");

		return Integer.parseInt(parts[1]);
	}
	public void setTime(int minutes, int seconds)
	{
		TextView timer = (TextView) findViewById(R.id.textMainTimer);

		String timerText = String.format("%d:%02d", minutes, seconds);
		timer.setText(timerText);
	}
}
