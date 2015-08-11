package change;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class Lunar {

	static final double LOCAL_TIMEZONE = 7.0;
	static final double PI = Math.PI;

	static final double[] SUNLONG_MAJOR = new double[] { 19 * PI / 12,
			5 * PI / 3, 7 * PI / 4, 11 * PI / 6, 23 * PI / 12, 0, PI / 12,
			PI / 6, PI / 4, PI / 3, 5 * PI / 12, PI / 2, 7 * PI / 12,
			2 * PI / 3, 3 * PI / 4, 5 * PI / 6, 11 * PI / 12, PI, 13 * PI / 12,
			7 * PI / 6, 5 * PI / 4, 4 * PI / 3, 17 * PI / 12, 3 * PI / 2 };

	static final String[] SAO = new String[] { "14. Bich (thuy)",
			"15. Khue (moc)", "16. Lau (kim)", "17. Vi (Tho)",
			"18. Mao (nhat)", "19. Tat (nguyet)", "20. Chuy (hoa)",
			"21. Sam (thuy)", "22. Tinh (moc)", "23. Quy (kim)",
			"24. Lieu (tho)", "25. Tinh (nhat)", "26. Truong (nguyet)",
			"27. Du (hoa)", "28. Chan (thuy)", "1. Giac (moc)",
			"2. Cang (kim)", "3. De (tho)", "4. Phong (nhat)",
			"5. Tam (nguyet)", "6. Vi (hoa)", "7. Co (thuy)", "8. Dau (moc)",
			"9. Nguu (kim)", "10. Nu (tho)", "11. Hu (nhat)",
			"12. Nguy (nguyet)", "13. That (hoa)" };

	static final String[] SAOTOT = new String[] { "Thien Duc", "Nguyet Duc",
			"Thien Giai", "Thien Hy", "Thien Quy", "Tam Hop", "Sinh Khi",
			"Thien Thanh", "Thien Quan", "Loc Ma", "Phuc Sinh", "Giai Than",
			"Thien An" };

	static final String[] SAOXAU = new String[] { "Thien Cuong", "Thu Tu",
			"Dai Hao\tTu Khi\tQuan Phu", "Tieu Hao", "Sat Chu", "Thien Hoa",
			"Dia Hoa", "Hoa Tai", "Nguyet Pha", "Bang Tieu Ngoa Giai",
			"Tho Cam", "Tho Ky\tVang Vong", "Co Than", "Qua Tu", "Trung Tang",
			"Trung Phuc" };

	static final String[] TIETKHI = new String[] { "Tieu han", "Dai han",
			"Lap xuan", "Vu thuy", "Kinh trap", "Xuan phan", "Thanh minh",
			"Coc vu", "Lap ha", "Tieu man", "Mang chung", "Ha chi", "Tieu thu",
			"Dai thu", "Lap thu", "Xu thu", "Bach lo", "Thu phan", "Han lo",
			"Suong gian", "Lap dong", "Tieu tuyet", "Dai tuyet", "Dong chi" };

	static final String[] TRUC = new String[] { "Truc kien", "Truc tru",
			"Truc man", "Truc binh", "Truc dinh", "Truc chap", "Truc pha",
			"Truc nguy", "Truc thanh", "Truc thu", "Truc khai", "Truc be" };

	static final String[] CAN = new String[] { "Giap", "At", "Binh", "Dinh",
			"Mau", "Ky", "Canh", "Tan", "Nham", "Quy" };

	static final String[] CHI = new String[] { "Ty (chuot)", "Suu", "Dan",
			"Mao", "Thin", "Ty (ran)", "Ngo", "Mui", "Than", "Dau", "Tuat",
			"Hoi" };
	static final String[] THU = new String[] { "Chu nhat", "Thu hai", "Thu ba",
			"Thu tu", "Thu nam", "Thu sau", "Thu bay" };

	static final String[] GIO = new String[] { "23h-1h", "1h-3h", "3h-5h",
			"5h-7h", "7h-9h", "9h-11h", "11h-13h", "13h-15h", "15h-17h",
			"17h-19h", "19h-21h", "21h-23h" };

	public static int INT(double d) {
		return (int) Math.floor(d);
	}

	public static int MOD(int x, int y) {
		int z = x - (int) (y * Math.floor(((double) x / y)));
		if (z == 0) {
			z = y;
		}
		return z;
	}

	// doi duong lich ra julius
	public static double universalToJD(DayMonthYear dmy) {
		double JD;
		int D = dmy.getDay();
		int M = dmy.getMonth();
		int Y = dmy.getYear();

		if (Y > 1582 || (Y == 1582 && M > 10)
				|| (Y == 1582 && M == 10 && D > 14)) {
			JD = 367 * Y - INT(7 * (Y + INT((M + 9) / 12)) / 4)
					- INT(3 * (INT((Y + (M - 9) / 7) / 100) + 1) / 4)
					+ INT(275 * M / 9) + D + 1721028.5;
		} else {
			JD = 367 * Y - INT(7 * (Y + 5001 + INT((M - 9) / 7)) / 4)
					+ INT(275 * M / 9) + D + 1729776.5;
		}
		return JD;
	}

	// doi julius ra duong lich
	public static DayMonthYear universalFromJD(double JD) {
		int Z, A, alpha, B, C, D, E, dd, mm, yyyy;
		double F;
		Z = INT(JD + 0.5);
		F = (JD + 0.5) - Z;
		if (Z < 2299161) {
			A = Z;
		} else {
			alpha = INT((Z - 1867216.25) / 36524.25);
			A = Z + 1 + alpha - INT(alpha / 4);
		}
		B = A + 1524;
		C = INT((B - 122.1) / 365.25);
		D = INT(365.25 * C);
		E = INT((B - D) / 30.6001);
		dd = INT(B - D - INT(30.6001 * E) + F);
		if (E < 14) {
			mm = E - 1;
		} else {
			mm = E - 13;
		}
		if (mm < 3) {
			yyyy = C - 4715;
		} else {
			yyyy = C - 4716;
		}

		return new DayMonthYear(dd, mm, yyyy);
	}

	// julius ra duong lich (theo gio Viet Nam)
	public static DayMonthYear localFromJD(double JD) {
		return universalFromJD(JD + LOCAL_TIMEZONE / 24.0);
	}

	// duong lich ra julius (theo gio Viet Nam)
	public static double localToJD(DayMonthYear dmy) {
		return universalToJD(dmy) - LOCAL_TIMEZONE / 24.0;
	}

	// tinh thoi diem soc
	public static double newMoon(int k) {
		double T = k / 1236.85; // Time in Julian centuries from 1900 January
								// 0.5
		double T2 = T * T;
		double T3 = T2 * T;
		double dr = PI / 180;
		double Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2
				- 0.000000155 * T3;
		Jd1 = Jd1 + 0.00033
				* Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr); // Mean
																		// new
																		// moon
		double M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347
				* T3; // Sun's mean anomaly
		double Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236
				* T3; // Moon's mean anomaly
		double F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239
				* T3; // Moon's argument of latitude
		double C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021
				* Math.sin(2 * dr * M);
		C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr);
		C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr);
		C1 = C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051
				* Math.sin(dr * (M + Mpr));
		C1 = C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004
				* Math.sin(dr * (2 * F + M));
		C1 = C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006
				* Math.sin(dr * (2 * F + Mpr));
		C1 = C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005
				* Math.sin(dr * (2 * Mpr + M));
		double deltat;
		if (T < -11) {
			deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3
					- 0.000000081 * T * T3;
		} else {
			deltat = -0.000278 + 0.000265 * T + 0.000262 * T2;
		}
		;
		double JdNew = Jd1 + C1 - deltat;
		return JdNew;
	}

	// tinh vi tri mat troi luc 00:00 (dai diem tuy dau vao day julius)
	public static double sunLongitude(double jdn) {
		double T = (jdn - 2451545.0) / 36525; // Time in Julian centuries from
												// 2000-01-01 12:00:00 GMT
		double T2 = T * T;
		double dr = PI / 180; // degree to radian
		double M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048
				* T * T2; // mean anomaly, degree
		double L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean
																	// longitude,
																	// degree
		double DL = (1.914600 - 0.004817 * T - 0.000014 * T2)
				* Math.sin(dr * M);
		DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290
				* Math.sin(dr * 3 * M);
		double L = L0 + DL; // true longitude, degree
		L = L * dr;
		L = L - PI * 2 * (INT(L / (PI * 2))); // Normalize to (0, 2*PI)
		return L;
	}

	public static DayMonthYear lunarMonth11(int Y) {
		DayMonthYear dmy = new DayMonthYear(31, 12, Y);
		double off = localToJD(dmy) - 2415021.076998695;
		int k = INT(off / 29.530588853);
		double jd = newMoon(k);
		dmy = localFromJD(jd);
		double sunLong = sunLongitude(localToJD(dmy)); // sun
														// longitude
														// at
														// local
														// midnight
		if (sunLong > 3 * PI / 2) {
			jd = newMoon(k - 1);
		}
		return localFromJD(jd);
	}

	public static DayMonthYear[] lunarYear(int Y) {
		DayMonthYear[] ret = null;
		DayMonthYear month11A = lunarMonth11(Y - 1);
		double jdMonth11A = localToJD(month11A);
		int k = (int) Math
				.floor(0.5 + (jdMonth11A - 2415021.076998695) / 29.530588853);
		DayMonthYear month11B = lunarMonth11(Y);
		double off = localToJD(month11B) - jdMonth11A;
		boolean leap = off > 365.0;
		if (!leap) {
			ret = new DayMonthYear[13];
		} else {
			ret = new DayMonthYear[14];
		}
		ret[0] = new DayMonthYear(month11A.getDay(), month11A.getMonth(),
				month11A.getYear(), 0, 0);
		ret[ret.length - 1] = new DayMonthYear(month11B.getDay(),
				month11B.getMonth(), month11B.getYear(), 0, 0);
		for (int i = 1; i < ret.length - 1; i++) {
			double nm = newMoon(k + i);
			DayMonthYear a = localFromJD(nm);
			ret[i] = new DayMonthYear(a.getDay(), a.getMonth(), a.getYear(), 0,
					0);
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i].setNm(MOD(i + 11, 12));
		}
		if (leap) {
			initLeapYear(ret);
		}
		return ret;
	}

	static void initLeapYear(DayMonthYear[] ret) {
		double[] sunLongitudes = new double[ret.length];
		for (int i = 0; i < ret.length; i++) {
			DayMonthYear dmy = ret[i];
			double jdAtMonthBegin = localToJD(dmy);
			sunLongitudes[i] = sunLongitude(jdAtMonthBegin);
		}
		boolean found = false;
		for (int i = 0; i < ret.length; i++) {
			if (found) {
				ret[i].setNm(MOD(i + 10, 12));
				continue;
			}
			double sl1 = sunLongitudes[i];
			double sl2 = sunLongitudes[i + 1];
			boolean hasMajorTerm = Math.floor(sl1 / PI * 6) != Math.floor(sl2
					/ PI * 6);
			if (!hasMajorTerm) {
				found = true;
				ret[i].setLeap(1);
				ret[i].setNm(MOD(i + 10, 12));
			}
		}
	}

	/* bat dau chuyen doi ngay am duong */
	public static DayMonthYear solar2Lunar(DayMonthYear dmy) {
		int yy = dmy.getYear();
		int Y = yy;
		DayMonthYear[] ly = lunarYear(Y); // Please cache the result of this
		// computation for later use!!!
		DayMonthYear month11 = ly[ly.length - 1];
		double jdToday = localToJD(dmy);
		double jdMonth11 = localToJD(month11);
		if (jdToday >= jdMonth11) {
			ly = lunarYear(Y + 1);
			yy = Y + 1;
		}
		int i = ly.length - 1;
		while (jdToday < localToJD(ly[i])) {
			i--;
		}
		int dd = (int) (jdToday - localToJD(ly[i])) + 1;
		int mm = ly[i].getLeap();
		if (mm >= 11) {
			yy--;
		}
		return new DayMonthYear(dd, mm, yy, ly[i].getLeap());
	}

	// dmy: ngay, thang, nam, leap
	public static DayMonthYear lunar2Solar(DayMonthYear dmy) {
		int yy = dmy.getYear();
		if (dmy.getMonth() >= 11) {
			yy = dmy.getYear() + 1;
		}
		DayMonthYear[] lunarYear = lunarYear(yy);
		DayMonthYear lunarMonth = null;
		for (int i = 0; i < lunarYear.length; i++) {
			DayMonthYear lm = lunarYear[i];
			if (lm.getNm() == dmy.getMonth() && lm.getLeap() == dmy.getLeap()) {
				lunarMonth = lm;
				break;
			}
		}
		if (lunarMonth != null) {
			double jd = localToJD(lunarMonth);
			return localFromJD(jd + dmy.getDay() - 1);
		} else {
			throw new RuntimeException("Incorrect input!");
		}
	}

	/* ket thuc chuyen doi ngay am duong */

	/* bat dau can chi */

	public static int[] can(DayMonthYear dmy) {
		DayMonthYear lunar = solar2Lunar(dmy);
		int y = lunar.getYear();

		// return can ngay - thang - nam
		return new int[] { INT(localToJD(dmy) + 10.5) % 10,
				(y * 12 + lunar.getMonth() + 3) % 10, (y + 6) % 10 };
	}

	public static int[] chi(DayMonthYear dmy) {
		DayMonthYear lunar = solar2Lunar(dmy);

		// return chi ngay - thang - nam
		return new int[] { INT(localToJD(dmy) + 2.5) % 12,
				(lunar.getMonth() + 1) % 12, (lunar.getYear() + 8) % 12 };
	}

	public static int thu(DayMonthYear dmy) {
		return (int) (localToJD(dmy) + 2.5) % 7;
	}

	/* ket thuc can chi */

	public static int maxDayOfMonth(int m, int y) {
		if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10
				|| m == 12)
			return 31;
		if (m == 4 || m == 6 || m == 9 || m == 11)
			return 30;
		if (m == 2) {
			if ((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) {
				return 29;
			} else
				return 28;
		}
		return 0;
	}

	public static DayMonthYear addDay(DayMonthYear dmy, int add) {
		int D = dmy.getDay();
		int M = dmy.getMonth();
		int Y = dmy.getYear();

		D += add;
		if (D > maxDayOfMonth(M, Y)) {
			D = 1;
			M++;
			if (M > 12) {
				M = 1;
				Y++;
			}
		}

		return new DayMonthYear(D, M, Y);
	}

	public static DayMonthYear[] tietKhiMoc(int Y) {
		DayMonthYear[] ret = new DayMonthYear[25];

		int i = 0;
		int D = 5;
		int M = 1;

		while (i < 24) {
			DayMonthYear a = new DayMonthYear(D, M, Y);

			if (i == 5)
				while (sunLongitude(localToJD(a)) > SUNLONG_MAJOR[6]) {
					a.setDay(D + 1);
					D++;
				}
			else
				while (sunLongitude(localToJD(a)) < SUNLONG_MAJOR[i]) {
					a.setDay(D + 1);
					D++;
				}

			ret[i] = new DayMonthYear(a.getDay() - 1, M);

			D = addDay(a, 14).getDay();
			M = addDay(a, 14).getMonth();
			i++;
		}

		ret[i] = new DayMonthYear(31, 12);

		return ret;
	}

	public static int tietKhi(DayMonthYear dmy) {
		DayMonthYear[] moc = tietKhiMoc(dmy.getYear());
		long a, b;

		if (dmy.getDay() == 31 && dmy.getMonth() == 12)
			return 23;
		else {
			for (int i = 0; i < 24; i++) {
				DayMonthYear moc1 = new DayMonthYear(moc[i].getDay(),
						moc[i].getMonth(), dmy.getYear());
				DayMonthYear moc2 = new DayMonthYear(moc[i + 1].getDay(),
						moc[i + 1].getMonth(), dmy.getYear());
				a = daysBetween2Dates(moc1, dmy);
				b = daysBetween2Dates(moc1, moc2);
				if (a < 0) {
					return 23;
				} else {
					if (a < b) {
						return i;
					}
				}
			}
		}

		return -1;
	}

	public static int truc(DayMonthYear dmy) {
		int i;
		int Y = dmy.getYear();

		for (i = 0; i < 24; i += 2) {
			DayMonthYear a = new DayMonthYear(tietKhiMoc(Y)[i].getDay(),
					tietKhiMoc(Y)[i].getMonth(), Y);
			if (daysBetween2Dates(dmy, a) > 0) {
				return (chi(dmy)[0] + 12 - i / 2) % 12;
			}
		}
		return (chi(dmy)[0]) % 12;
	}

	public static long daysBetween2Dates(DayMonthYear dmy1, DayMonthYear dmy2) {

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		// Định nghĩa 2 mốc thời gian ban đầu
		Date date1 = Date.valueOf(dmy1.getYear() + "-" + dmy1.getMonth() + "-"
				+ dmy1.getDay());
		Date date2 = Date.valueOf(dmy2.getYear() + "-" + dmy2.getMonth() + "-"
				+ dmy2.getDay());

		c1.setTime(date1);
		c2.setTime(date2);

		// Công thức tính số ngày giữa 2 mốc thời gian:
		long noDay = (c2.getTime().getTime() - c1.getTime().getTime())
				/ (24 * 3600 * 1000);

		return noDay;
	}

	public static int nhiThapBatTu(DayMonthYear dmy) {
		DayMonthYear a = new DayMonthYear(1, 1, 1975);
		long longDay = daysBetween2Dates(a, dmy) % 28;
		// System.out.println(SAO[(int) longDay]);
		return (int) longDay;
	}

	// result=0: ngay hac dao
	// result=1: ngay hoang dao
	// result=-1: error
	public static int ngayHoangDao(DayMonthYear dmy) {
		int chi = chi(dmy)[0];
		DayMonthYear lunar = solar2Lunar(dmy);

		int[] hoangDao = new int[] { 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0 };
		int i = -1, result;

		switch (lunar.getMonth()) {
		case 1:
		case 7:
			i = 0;
			break;

		case 2:
		case 8:
			i = 2;
			break;

		case 3:
		case 9:
			i = 4;
			break;

		case 4:
		case 10:
			i = 6;
			break;

		case 5:
		case 11:
			i = 8;
			break;

		case 6:
		case 12:
			i = 10;
			break;

		default:
			i = -1;
			break;
		}

		if (i != -1) {
			result = (chi - i + 12) % 12;
			return hoangDao[result];
		} else
			return -1;
	}

	public static int[] gioHoangDao(DayMonthYear dmy) {
		int chi = chi(dmy)[0];
		int[] gio;

		switch (chi) {
		case 0:
		case 6:
			gio = new int[] { 0, 1, 3, 6, 8, 9 };
			break;

		case 1:
		case 7:
			gio = new int[] { 2, 3, 5, 8, 10, 11 };
			break;

		case 2:
		case 8:
			gio = new int[] { 0, 1, 4, 5, 7, 10 };
			break;

		case 3:
		case 9:
			gio = new int[] { 0, 2, 3, 6, 7, 9 };
			break;

		case 4:
		case 10:
			gio = new int[] { 2, 4, 5, 8, 9, 11 };
			break;

		case 5:
		case 11:
			gio = new int[] { 1, 4, 6, 7, 10, 11 };
			break;

		default:
			gio = null;
			break;
		}

		return gio;
	}

	static int countSaoTot;

	public static int[] saoTot(DayMonthYear dmy) {
		int[] ret = new int[5];
		int[][] sao = { { 5, 7, 9, 11, 1, 3, 5, 7, 9, 11, 1, 3 },
				{ 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 },
				{ 6, 8, 10, 0, 2, 4, 6, 8, 10, 0, 2, 4 },
				{ 10, 11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 2, 8, 3, 9, 4, 10, 5, 11, 6, 0, 7, 1 },
				{ 6, 7, 8, 9, 10, 11, 0, 1, 2, 3, 4, 5 },
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 },
				{ 7, 9, 11, 1, 3, 5, 7, 9, 7, 1, 3, 5 },
				{ 10, 0, 2, 4, 6, 8, 10, 0, 2, 4, 6, 8 },
				{ 6, 8, 10, 0, 2, 4, 6, 8, 10, 0, 2, 4 },
				{ 9, 3, 10, 4, 11, 5, 0, 6, 1, 7, 2, 8 },
				{ 8, 8, 10, 10, 0, 0, 2, 2, 4, 4, 6, 6 },
				{ 10, 1, 2, 5, 9, 3, 0, 6, 8, 4, 8, 7 } };

		int m = solar2Lunar(dmy).getMonth();
		int chi = chi(dmy)[0];

		countSaoTot = 0;
		for (int i = 0; i < 13; i++) {
			if (chi == sao[i][m - 1]) {
				ret[countSaoTot] = i;
				countSaoTot++;
			}
		}

		return ret;
	}

	static int countSaoXau;

	public static int[] saoXau(DayMonthYear dmy) {
		int[] ret = new int[7];
		int[][] saoChi = { { 5, 0, 7, 2, 9, 4, 11, 6, 1, 8, 3, 10 },
				{ 10, 4, 11, 5, 0, 6, 1, 7, 2, 8, 3, 9 },
				{ 6, 7, 8, 9, 10, 11, 0, 1, 2, 3, 4, 5 },
				{ 5, 6, 7, 8, 9, 10, 11, 0, 1, 2, 3, 4 },
				{ 0, 5, 7, 3, 8, 10, 1, 11, 6, 9, 2, 4 },
				{ 0, 3, 6, 9, 0, 3, 6, 9, 0, 3, 6, 9 },
				{ 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 11 },
				{ 1, 7, 2, 8, 3, 9, 4, 10, 5, 11, 0, 6 },
				{ 8, 10, 10, 11, 1, 1, 2, 4, 4, 5, 7, 7 },
				{ 5, 0, 1, 8, 3, 10, 11, 6, 7, 2, 9, 4 },
				{ 11, 11, 11, 2, 2, 2, 5, 5, 5, 8, 8, 8 },
				{ 2, 5, 8, 11, 3, 6, 9, 0, 4, 7, 10, 1 },
				{ 10, 11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 4, 5, 6, 7, 8, 9, 10, 11, 0, 1, 2, 3 } };
		int[][] saoCan = { { 0, 1, 4, 2, 3, 5, 6, 7, 5, 8, 9, 4 },
				{ 6, 7, 5, 8, 9, 4, 0, 1, 5, 2, 3, 4 } };

		int m = solar2Lunar(dmy).getMonth();
		int chi = chi(dmy)[0];

		countSaoXau = 0;
		for (int i = 0; i < 14; i++) {
			if (chi == saoChi[i][m - 1]) {
				ret[countSaoXau] = i;
				countSaoXau++;
			}
		}

		int can = can(dmy)[0];
		for (int i = 0; i < 2; i++) {
			if (can == saoCan[i][m - 1]) {
				ret[countSaoXau] = i;
				countSaoXau++;
			}
		}

		return ret;
	}

	public static int[][] arrayBatTuong(int counter) {
		int[][] ret = null;

		switch (counter) {
		case 0:
			ret = new int[][] { { 2, 2 }, { 3, 3 }, { 4, 4 }, { 3, 1 },
					{ 4, 2 }, { 5, 3 }, { 6, 4 }, { 6, 2 }, { 7, 3 }, { 7, 1 },
					{ 2, 4 } };
			break;
		case 1:
			ret = new int[][] { { 2, 2 }, { 3, 3 }, { 2, 5 }, { 5, 3 },
					{ 4, 0 }, { 6, 2 }, { 7, 3 } };
			break;
		case 2:
			ret = new int[][] { { 1, 1 }, { 2, 0 }, { 3, 1 }, { 2, 10 },
					{ 4, 0 }, { 5, 1 }, { 4, 10 }, { 6, 0 }, { 6, 10 } };
			break;
		case 3:
			ret = new int[][] { { 1, 1 }, { 3, 1 }, { 1, 9 }, { 5, 1 },
					{ 3, 9 }, { 5, 9 } };
			break;
		case 4:
			ret = new int[][] { { 0, 0 }, { 0, 10 }, { 2, 0 }, { 0, 8 },
					{ 1, 9 }, { 2, 10 }, { 4, 0 }, { 2, 8 }, { 3, 9 },
					{ 4, 10 } };
			break;
		case 5:
			ret = new int[][] { { 9, 9 }, { 0, 10 }, { 9, 7 }, { 0, 8 },
					{ 1, 9 }, { 2, 10 }, { 1, 7 }, { 2, 8 }, { 4, 0 }, { 5, 7 } };
			break;
		case 6:
			ret = new int[][] { { 8, 8 }, { 9, 9 }, { 0, 10 }, { 8, 6 },
					{ 9, 7 }, { 0, 8 }, { 1, 9 }, { 0, 6 }, { 1, 7 },
					{ 4, 10 }, { 5, 7 }, { 8, 10 } };
			break;
		case 7:
			ret = new int[][] { { 5, 5 }, { 8, 8 }, { 9, 9 }, { 8, 6 },
					{ 9, 7 }, { 0, 8 }, { 1, 9 }, { 9, 5 }, { 0, 6 }, { 1, 7 },
					{ 1, 5 }, { 4, 6 }, { 5, 7 } };
			break;
		case 8:
			ret = new int[][] { { 4, 4 }, { 5, 5 }, { 7, 7 }, { 7, 5 },
					{ 8, 6 }, { 9, 7 }, { 9, 5 }, { 9, 3 }, { 4, 6 }, { 5, 7 } };
			break;
		case 9:
			ret = new int[][] { { 5, 5 }, { 6, 6 }, { 7, 7 }, { 7, 5 },
					{ 8, 6 }, { 9, 7 }, { 9, 5 }, { 9, 3 }, { 4, 6 }, { 5, 7 } };
			break;
		case 10:
			ret = new int[][] { { 4, 4 }, { 6, 6 }, { 4, 2 }, { 5, 3 },
					{ 6, 4 }, { 8, 6 }, { 6, 2 }, { 7, 3 }, { 8, 4 }, { 8, 2 },
					{ 9, 3 }, { 4, 6 } };
			break;
		case 11:
			ret = new int[][] { { 3, 3 }, { 4, 4 }, { 5, 5 }, { 3, 1 },
					{ 5, 3 }, { 6, 4 }, { 7, 5 }, { 8, 4 }, { 7, 1 }, { 3, 5 } };
			break;
		}

		return ret;
	}

	public static ArrayList<DayMonthYear> ngayBatTuong(int Y) {

		ArrayList<DayMonthYear> ret = new ArrayList<DayMonthYear>();
		int[][] arrBatTuong = arrayBatTuong(0);
		DayMonthYear[] tietKhi = tietKhiMoc(Y);
		DayMonthYear a = new DayMonthYear();

		int can, chi;
		int counter = 0;

		a.setDay(tietKhi[0].getDay());
		a.setMonth(tietKhi[0].getMonth());
		a.setYear(Y);

		while (counter < 24) {

			can = can(a)[0];
			chi = chi(a)[0];

			for (int i = 0; i < arrBatTuong.length; i++) {
				if (can == arrBatTuong[i][1] && chi == arrBatTuong[i][0]) {
					ret.add(a);
					// System.out.println(D + "\t" + M + "\t" + counter);

				}
			}

			a = addDay(a, 1);

			if (a.getDay() == tietKhi[counter + 2].getDay()
					&& a.getMonth() == tietKhi[counter + 2].getMonth()) {
				arrBatTuong = arrayBatTuong(counter / 2);
				counter += 2;
			}
			if (a.getDay() == 31 && a.getMonth() == 12)
				break;
		}

		return ret;
	}

	// 11 ngay tot cho cuoi xin
	public static ArrayList<DayMonthYear> ngayTot(int Y) {

		ArrayList<DayMonthYear> ret = new ArrayList<DayMonthYear>();

		int[][] arrNgayTot = new int[][] { { 2, 2 }, { 3, 3 }, { 2, 5 },
				{ 4, 2 }, { 5, 3 }, { 2, 10 }, { 4, 0 }, { 6, 2 }, { 8, 2 },
				{ 9, 3 }, { 1, 5 } };

		DayMonthYear dmy;
		int d, m, can, chi;

		for (m = 1; m <= 12; m++) {
			for (d = 1; d <= maxDayOfMonth(m, Y); d++) {
				dmy = new DayMonthYear(d, m, Y);
				can = can(dmy)[0];
				chi = chi(dmy)[0];

				for (int i = 0; i < 11; i++) {
					if (can == arrNgayTot[i][0] && chi == arrNgayTot[i][1]) {
						ret.add(dmy);

						// System.out.println(d + "\t" + m);
					}
				}
			}
		}

		return ret;
	}

	// gap truc thanh cho cuoi xin
	public static ArrayList<DayMonthYear> trucThanh(int Y) {
		ArrayList<DayMonthYear> ret = new ArrayList<DayMonthYear>();

		DayMonthYear dmy;
		int d, m;

		for (m = 1; m <= 12; m++) {
			for (d = 1; d <= maxDayOfMonth(m, Y); d++) {
				dmy = new DayMonthYear(d, m, Y);
				if (truc(dmy) == 8) {
					ret.add(dmy);

					// System.out.println(d + "\t" + m);
				}
			}
		}
		return ret;
	}

	public static int[] thangTot(DayMonthYear dmy) {

		int[] ret = new int[2];
		int chi = chi(dmy)[2];

		switch (chi) {
		case 0:
		case 6:
			ret[0] = 6;
			ret[1] = 12;
			break;
		case 1:
		case 7:
			ret[0] = 5;
			ret[1] = 11;
			break;
		case 2:
		case 8:
			ret[0] = 2;
			ret[1] = 8;
			break;
		case 3:
		case 9:
			ret[0] = 1;
			ret[1] = 7;
			break;
		case 4:
		case 10:
			ret[0] = 4;
			ret[1] = 10;
			break;
		case 5:
		case 11:
			ret[0] = 3;
			ret[1] = 9;
			break;
		}

		return ret;

	}

	public static ArrayList<DayMonthYear> cuoiGa(DayMonthYear dmy1,
			DayMonthYear dmy2) {

		ArrayList<DayMonthYear> result = new ArrayList<DayMonthYear>();

		ArrayList<DayMonthYear> ret = new ArrayList<DayMonthYear>();
		ArrayList<DayMonthYear> resultNgayTot = ngayTot(dmy1.getYear());
		ArrayList<DayMonthYear> resultTrucThanh = trucThanh(dmy1.getYear());

		long longDays1, longDays2;

		ret.addAll(ngayBatTuong(dmy1.getYear()));

		int counter = ret.size();
		int k = 0;

		for (int i = 0; i < resultNgayTot.size(); i++) {
			for (int j = 0; j < counter; j++) {
				if (resultNgayTot.get(i).getDay() == ret.get(j).getDay()
						&& resultNgayTot.get(i).getMonth() == ret.get(j)
								.getMonth()) {
					k = 0;
					break;
				} else
					k++;

			}

			if (k == counter) {
				ret.add(resultNgayTot.get(i));
			}

			k = 0;
		}

		counter = ret.size();
		k = 0;
		for (int i = 0; i < resultTrucThanh.size(); i++) {
			for (int j = 0; j < counter; j++) {
				if (resultTrucThanh.get(i).getDay() == ret.get(j).getDay()
						&& resultTrucThanh.get(i).getMonth() == ret.get(j)
								.getMonth()) {
					k = 0;
					break;
				} else
					k++;

			}

			if (k == counter) {
				ret.add(resultTrucThanh.get(i));
			}

			k = 0;
		}

		System.out.println("************");

		DayMonthYear dmy0;
		for (int i = 0; i < ret.size(); i++) {
			dmy0 = ret.get(i);
			longDays1 = daysBetween2Dates(dmy0, dmy1);
			longDays2 = daysBetween2Dates(dmy0, dmy2);
			if (longDays1 <= 0 && longDays2 >= 0) {
				result.add(dmy0);
				// dmy0.printInfo();
			}
		}
		sortResult(result);
		ngayXau(result);

		return result;
	}

	public static void sortResult(ArrayList<DayMonthYear> result) {

		Collections.sort(result, new Comparator<DayMonthYear>() {

			@Override
			public int compare(DayMonthYear o1, DayMonthYear o2) {
				// TODO Auto-generated method stub
				if (o1.getMonth() > o2.getMonth())
					return 1;
				else {
					if (o1.getMonth() == o2.getMonth()) {
						if (o1.getDay() > o2.getDay())
							return 1;
						else {
							if (o1.getDay() == o2.getDay())
								return 0;
							else
								return -1;
						}
					} else
						return -1;
				}
			}
		});

	}

	public static void ngayXau(ArrayList<DayMonthYear> result) {

		DayMonthYear dmy = new DayMonthYear();
		int mua, chi;

		for (int i = 0; i < result.size(); i++) {
			dmy = result.get(i);
			chi = chi(dmy)[0];
			mua = tietKhi(dmy);

			if (mua < 2 || mua >= 20) {
				if (chi == 1 || chi == 2 || chi == 8 || chi == 9)
					result.remove(i);
			}
			if (mua >= 2 && mua < 8) {
				if (chi == 0 || chi == 4 || chi == 5 || chi == 9)
					result.remove(i);
			}
			if (mua >= 8 && mua < 14) {
				if (chi == 1 || chi == 3 || chi == 7 || chi == 10)
					result.remove(i);
			}
			if (mua >= 14 && mua < 20) {
				if (chi == 2 || chi == 3 || chi == 6 || chi == 8 || chi == 10)
					result.remove(i);
			}
		}
	}

	/*----------MAIN-------------*/

	public static void main(String[] args) {

		// System.out.print("\nNgay duong -> ngay am:\t");
		// result = solar2Lunar(D, M, Y);
		// for (int i = 0; i < 4; i++)
		// System.out.print(result[i] + "\t");

		// System.out.print("\nNgay am -> ngay duong:\t");
		// result = lunar2Solar(1, 1, 2015, 0);
		// for (int i = 0; i < 3; i++)
		// System.out.print(result);[i] + "\t"

		// System.out.print("\nCan chi:\n");
		// int[] can = can(D, M, Y);
		// int[] chi = chi(D, M, Y);
		// System.out.println("ngay:\t" + CAN[can[0]] + "\t" + CHI[chi[0]]);
		// System.out.println("thang:\t" + CAN[can[1]] + "\t" + CHI[chi[1]]);
		// System.out.println("nam:\t" + CAN[can[2]] + "\t" + CHI[chi[2]]);
		//
		// System.out.println("\n" + THU[thu(D, M, Y)]);
		// if (ngayHoangDao(D, M, Y) == 1) {
		// System.out.println("Ngay hoang dao");
		// } else {
		// System.out.println("Ngay hac dao");
		// }
		//
		// System.out.println("Gio hoang dao:");
		// for (int i = 0; i < 6; i++) {
		// System.out.println(CHI[gioHoangDao(D, M, Y)[i]] + "\t"
		// + GIO[gioHoangDao(D, M, Y)[i]]);
		// }

		// for (int i = 0; i < 13; i++) {
		// System.out.print("\n");
		// for (int j = 0; j < 5; j++)
		// System.out.print(lunarYear(2014)[i][j] + "\t");
		// }

		// System.out.println(SunLongitude(LocalToJD(5, 4, 2015)));
		// System.out.println(SUNLONG_MAJOR[5]);

		// for (int i = 0; i < 24; i++) {
		// System.out.println();
		// for (int j = 0; j < 2; j++)
		// System.out.print(tietKhiMoc(2015)[i][j] + "\t");
		// }

		// result = null;
		// result = saoTot(D, M, Y);
		// System.out.print("\nSao tot:\t");
		// for (int i = 0; i < countSaoTot; i++) {
		// System.out.print(SAOTOT[result[i]] + "\t");
		// }
		//
		// result = null;
		// result = saoXau(D, M, Y);
		// System.out.print("\nSao xau:\t");
		// for (int i = 0; i < countSaoXau; i++) {
		// System.out.print(SAOXAU[result[i]] + "\t\t");
		// }
		//
		// System.out.print("\n\nNhi thap bat tu:\t");
		// System.out.println(SAO[nhiThapBatTu(D, M, Y)]);
		// System.out.print("\nTiet khi:\t");
		// System.out.println(TIETKHI[tietKhi(D, M, Y)]);
		//
		// System.out.print("\nTruc:\t");
		//
		// System.out.println(TRUC[truc(D, M, Y)]);
		// sunLongitude(localToJD(4, 4, 2009));

		ArrayList<DayMonthYear> a = cuoiGa(new DayMonthYear(11, 8, 2015),
				new DayMonthYear(1, 12, 2015));
		for (int i = 0; i < a.size(); i++) {
			a.get(i).printInfo();
			
		}

	}
}
