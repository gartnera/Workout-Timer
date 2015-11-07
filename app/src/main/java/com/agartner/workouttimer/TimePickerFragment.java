package com.agartner.workouttimer;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;


public class TimePickerFragment extends DialogFragment {

	private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_picker, container);

		int seconds = getArguments().getInt("seconds");

		setupClickListeners();
		setupNumberSelectors(seconds);

        return view;
    }

	private void setupClickListeners()
	{
		Button btn = (Button) view.findViewById(R.id.cancel);
		btn.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				dismiss();
			}
		});

		btn = (Button) view.findViewById(R.id.ok);
		btn.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				NumberPicker np = (NumberPicker) getDialog().findViewById(R.id.numberPicker1);
				int minutes = np.getValue();
				np = (NumberPicker) getDialog().findViewById(R.id.numberPicker2);
				int seconds = np.getValue();

				MainActivity main = (MainActivity) getActivity();
				main.timePickerResult(minutes * 60 + seconds);

				dismiss();
			}
		});
	}

	private void setupNumberSelectors(int seconds){
		int currentMinutes = seconds / 60;
		int currentSeconds = seconds % 60;

		//set range of minute selector
		String[] minutes = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker1);
		np.setMinValue(0);
		np.setMaxValue(minutes.length  - 1);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(minutes);
		np.setValue(currentMinutes);

		//set range of second selector
		String[] secondChoices = new String[60];
		for (int i  = 0; i < 60; ++i)
		{
			secondChoices[i] = String.format("%02d", i);
		}
		np = (NumberPicker) view.findViewById(R.id.numberPicker2);
		np.setMinValue(0);
		np.setMaxValue(secondChoices.length - 1);
		np.setWrapSelectorWheel(false);
		np.setDisplayedValues(secondChoices);
		np.setValue(currentSeconds);
	}

}
