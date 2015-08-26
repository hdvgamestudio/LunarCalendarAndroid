package com.hdv.lunar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hdv.lunar.control.DayMonthYear;
import com.hdv.lunar.control.Lunar;
import com.hdv.lunar.fragment.FragmentDay;

public class FragmentDayAdapter extends FragmentPagerAdapter {

	private DayMonthYear dmy;
		
	public FragmentDayAdapter(FragmentManager fm, DayMonthYear dmy) {
		super(fm);
		this.dmy= dmy;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub

		return new FragmentDay (arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (Lunar.maxDayOfMonth(dmy.getMonth(), dmy.getYear())+2);
	}

}
