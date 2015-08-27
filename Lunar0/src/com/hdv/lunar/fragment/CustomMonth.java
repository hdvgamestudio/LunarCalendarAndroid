package com.hdv.lunar.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdv.lunar.activity.R;
import com.hdv.lunar.control.DayMonthYear;
import com.hdv.lunar.control.Lunar;

public class CustomMonth extends LinearLayout {

	TextView tvDayD = null;
	TextView tvDayA = null;
	boolean flag;
	Context context;

	public void setText(DayMonthYear dmy, DayMonthYear dmyCurrent, boolean flag) {

		this.flag = flag;

		tvDayD.setText(dmy.getDay() + "");
		tvDayA.setText(Lunar.solar2Lunar(dmy).getDay() + "");

		tvDayD.setTextColor(Color.parseColor("#777777"));
		tvDayA.setTextColor(Color.parseColor("#777777"));

		if (Lunar.thu(dmy) == 6) {
			tvDayD.setTextColor(Color.parseColor("#DA2128"));
			tvDayA.setTextColor(Color.parseColor("#DA2128"));
		}

		if (Lunar.solar2Lunar(dmy).getDay() == 1) {
			tvDayA.setTextColor(Color.parseColor("#DA2128"));
			tvDayA.setText(Lunar.solar2Lunar(dmy).getDay() + "/"
					+ Lunar.solar2Lunar(dmy).getMonth());
		}

		if (Lunar.daysBetween2Dates(dmy, dmyCurrent) == 0) {
			this.setBackgroundColor(Color.parseColor("#DA2128"));
		}

		if (flag == false) {
			tvDayD.setTextColor(Color.parseColor("#D3D3D3"));
			tvDayA.setTextColor(Color.parseColor("#D3D3D3"));
		}
	}

	public int getDayD() {
		if (flag)
			return Integer.parseInt(tvDayD.getText().toString());
		else
			return 0;
	}

	public CustomMonth(Context context) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (inflater != null) {
			inflater.inflate(R.layout.layout_month, this);

			this.tvDayA = (TextView) findViewById(R.id.tvDayA);
			this.tvDayD = (TextView) findViewById(R.id.tvDayD);

		}
	}
}
