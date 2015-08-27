package com.hdv.lunar.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.hdv.lunar.adapter.FragmentDayAdapter;
import com.hdv.lunar.control.DayMonthYear;
import com.hdv.lunar.control.Lunar;
import com.hdv.lunar.fragment.CustomMonth;
import com.hdv.lunar.fragment.FragmentDay.IGetItem;

public class LunarActivity extends FragmentActivity implements IGetItem {

	Typeface face1, face2;
	Handler handler;
	ViewPager viewPager;
	ImageButton ibtMenu, ibtPreviewMonth, ibtNextMonth;
	TextView tvMonthYear, tvThu, tvLunar, tvLunarCanChi, tvMonthYearCenter,
			tvTime, tvLocation;
	TableLayout table;
	FragmentDayAdapter adapter;
	DayMonthYear dmyCurrent = new DayMonthYear(26, 8, 2015);
	DayMonthYear dmyChanger, dmyTable, dmyCustomMOnth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lunar);

		init();

		setTime();
		setDay(dmyCurrent);
		setTable(dmyCurrent);

		printInfo(dmyChanger);
		listenButtonChange();
	}

	public void init() {
		handler = new Handler();

		face1 = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoCondensed-Regular.ttf");
		face2 = Typeface.createFromAsset(getAssets(),
				"fonts/RobotoCondensed-Bold.ttf");

		ibtMenu = (ImageButton) findViewById(R.id.ibtMenu);
		tvMonthYear = (TextView) findViewById(R.id.tvMonthYear);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvLocation = (TextView) findViewById(R.id.tvLocation);

		viewPager = (ViewPager) findViewById(R.id.pager);

		tvThu = (TextView) findViewById(R.id.tvThu);
		tvLunar = (TextView) findViewById(R.id.tvLunar);

		ibtPreviewMonth = (ImageButton) findViewById(R.id.ibtPreviewMonth);
		ibtNextMonth = (ImageButton) findViewById(R.id.ibtNextMonth);
		tvMonthYearCenter = (TextView) findViewById(R.id.tvMonthYearCenter);

		table = (TableLayout) findViewById(R.id.month);

		tvTime.setTypeface(face1);
		tvLocation.setTypeface(face1);
		tvMonthYear.setTypeface(face2);
		tvThu.setTypeface(face2);
		tvLunar.setTypeface(face2);
		tvMonthYearCenter.setTypeface(face2);
	}

	public void setTime() {
		Runnable r = new Runnable() {
			public void run() {
				Date time = new Date(System.currentTimeMillis());
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
				String getTime = timeFormat.format(time.getTime());

				tvTime.setText(getTime);
				handler.postDelayed(this, 1000);
			}
		};
		handler.postDelayed(r, 1000);
	}

	@SuppressWarnings("deprecation")
	public void setDay(DayMonthYear dmy) {

		adapter = new FragmentDayAdapter(getSupportFragmentManager(), dmy);
		viewPager.setAdapter(adapter);

		dmyChanger = new DayMonthYear(dmy.getDay(), dmy.getMonth(),
				dmy.getYear());

		viewPager.setCurrentItem(dmyChanger.getDay());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				dmyChanger.setDay(arg0);

				if (arg0 - 1 == Lunar.maxDayOfMonth(dmyChanger.getMonth(),
						dmyChanger.getYear())) {

					dmyChanger = Lunar.addDay(dmyChanger, 1);

					adapter = new FragmentDayAdapter(
							getSupportFragmentManager(), dmyChanger);

					adapter.notifyDataSetChanged();
					viewPager.setAdapter(adapter);
					viewPager.setCurrentItem(1);

					// tao tablemonth moi khi sang thang
					setTable(dmyChanger);
				}

				if (arg0 == 0) {

					dmyChanger = Lunar.addDay(dmyChanger, -1);

					adapter = new FragmentDayAdapter(
							getSupportFragmentManager(), dmyChanger);

					adapter.notifyDataSetChanged();
					viewPager.setAdapter(adapter);
					viewPager.setCurrentItem(Lunar.maxDayOfMonth(
							dmyChanger.getMonth(), dmyChanger.getYear()));

					// tao tablemonth moi khi quay ve thang truoc
					setTable(dmyChanger);
				}

				printInfo(dmyChanger);

				// dmyTable.setMonth(dmyChanger.getMonth());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void printInfo(DayMonthYear dmy) {

		tvMonthYear
				.setText("Tháng " + dmy.getMonth() + " năm " + dmy.getYear());
		tvThu.setText(Lunar.THU[Lunar.thu(dmy)]);

		DayMonthYear l = Lunar.solar2Lunar(dmy);
		int[] can = Lunar.can(dmy);
		int[] chi = Lunar.chi(dmy);
		String str = "\n(ngày " + Lunar.CAN[can[0]] + Lunar.CHI[chi[0]]
				+ " tháng " + Lunar.CAN[can[1]] + Lunar.CHI[chi[1]] + " năm "
				+ Lunar.CAN[can[2]] + Lunar.CHI[chi[2]] + ")";
		tvLunar.setText("Ngày " + l.getDay() + " tháng " + l.getMonth()
				+ " năm " + l.getYear() + str);

	}

	public void setTable(DayMonthYear dmy) {
		tvMonthYearCenter.setText("Tháng " + dmy.getMonth() + " năm "
				+ dmy.getYear());

		dmy = new DayMonthYear(1, dmy.getMonth(), dmy.getYear());

		DayMonthYear dmyException;
		int m = dmy.getMonth();
		boolean flag;

		table.removeAllViews();
		TableRow[] row = new TableRow[6];

		for (int i = 0; i < 6; i++) {
			row[i] = new TableRow(this);

			CustomMonth[] cm = new CustomMonth[7];

			for (int j = 0; j < 7; j++) {
				cm[j] = new CustomMonth(this);

				if (dmy.getMonth() == m)
					flag = true;
				else
					flag = false;

				if (j == Lunar.thu(dmy)) {
					cm[j].setText(dmy, dmyCurrent, flag);
					dmy = Lunar.addDay(dmy, 1);
				} else {
					dmyException = Lunar.addDay(dmy, j - Lunar.thu(dmy));
					cm[j].setText(dmyException, dmyCurrent, false);
				}

				row[i].addView(cm[j]);
				cm[j].setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						1f));
			}

			table.addView(row[i]);
		}

		listenClickTableMonth();
	}

	public void listenButtonChange() {
		dmyTable = new DayMonthYear(dmyChanger.getDay(), dmyChanger.getMonth(),
				dmyChanger.getYear());

		ibtPreviewMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (dmyTable.getMonth() == 1) {
					dmyTable.setMonth(12);
					dmyTable.setYear(dmyTable.getYear() - 1);
					setTable(new DayMonthYear(1, dmyTable.getMonth(), dmyTable
							.getYear()));
				} else {
					dmyTable.setMonth(dmyTable.getMonth() - 1);
					setTable(new DayMonthYear(1, dmyTable.getMonth(), dmyTable
							.getYear()));
				}
			}
		});

		ibtNextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (dmyTable.getMonth() == 12) {
					dmyTable.setMonth(1);
					dmyTable.setYear(dmyTable.getYear() + 1);
					setTable(new DayMonthYear(1, dmyTable.getMonth(), dmyTable
							.getYear()));
				} else {
					dmyTable.setMonth(dmyTable.getMonth() + 1);
					setTable(new DayMonthYear(1, dmyTable.getMonth(), dmyTable
							.getYear()));
				}
			}
		});
	}

	public void listenClickTableMonth() {
		for (int i = 0; i < table.getChildCount(); i++) {
			final TableRow row = (TableRow) table.getChildAt(i);
			for (int j = 0; j < row.getChildCount(); j++) {
				final CustomMonth cm = (CustomMonth) row.getChildAt(j);
				if (cm.getDayD() != dmyCurrent.getDay() && cm.getDayD() != 0)
					cm.setBackgroundResource(R.drawable.imgbutton);
				
				cm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (cm.getDayD() != 0)
							setDay(new DayMonthYear(cm.getDayD(), dmyTable
									.getMonth(), dmyTable.getYear()));
					}
				});
			}
		}
	}

	@Override
	public int getMaxDay() {
		// TODO Auto-generated method stub
		return Lunar.maxDayOfMonth(dmyChanger.getMonth(), dmyChanger.getYear());
	}

	@Override
	public int getMaxDayPre() {
		// TODO Auto-generated method stub
		if (dmyChanger.getMonth() > 1)
			return Lunar.maxDayOfMonth(dmyChanger.getMonth() - 1,
					dmyChanger.getYear());
		else
			return 31;
	}

}
