package com.hdv.lunar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdv.lunar.activity.R;

public class FragmentDay extends Fragment {

	private int day;
	TextView tvDay;
	IGetItem item;

	public interface IGetItem {
		public int getMaxDay();
		public int getMaxDayPre();
	}

	public FragmentDay(int day) {
		this.day = day;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		item = (IGetItem) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_day, container,
				false);

		init(rootView);

		return rootView;
	}

	public void init(View view) {

		tvDay = (TextView) view.findViewById(R.id.tvDay);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (day > item.getMaxDay()) {
			tvDay.setText("1");
		} else if (day <= 0) {
			tvDay.setText(item.getMaxDayPre() + "");
		} else
			tvDay.setText(day + "");

	}
}
