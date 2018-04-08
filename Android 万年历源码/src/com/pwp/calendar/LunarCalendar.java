package com.pwp.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LunarCalendar {
	private int year;   //ũ�������
	private int month;
	private int day;
	private String lunarMonth;   //ũ�����·�
	private boolean leap;
	public int leapMonth = 0;   //������ĸ���
	
	final static String chineseNumber[] = { "һ", "��", "��", "��", "��", "��", "��",
			"��", "��", "ʮ", "ʮһ", "ʮ��" };
	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
			"yyyy��MM��dd��");
	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	//ũ�����ּ���
	final static String[] lunarHoliday = new String[]{
										"0101 ����",
										"0115 Ԫ��",
										"0505 ����",
										"0707 ��Ϧ����",
										"0715 ��Ԫ",
										"0815 ����",
										"0909 ����",
										"1208 ����",
										"1224 С��",
										"0100 ��Ϧ"
	};
	
	//�������ֽڼ���
	final static String[] solarHoliday = new String[]{
										"0101 Ԫ��",
										"0214 ����",
										"0308 ��Ů",
										"0312 ֲ��",
										"0315 ������Ȩ����",
										"0401 ����",
										"0501 �Ͷ�",
										"0504 ����",
										"0512 ��ʿ",
										"0601 ��ͯ",
										"0701 ����",
										"0801 ����",
										"0808 ����",
										"0909 ë����������",
										"0910 ��ʦ",
										"0928 ���ӵ���",
										"1001 ����",
										"1006 ����",
										"1024 ���Ϲ���",
										"1112 ����ɽ��������",
										"1220 ���Żع����",
										"1225 ʥ��",
										"1226 ë�󶫵�������"
	};
	
	// ====== ����ũ�� y���������
	final private static int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	// ====== ����ũ�� y�����µ�����
	final private static int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	// ====== ����ũ�� y�����ĸ��� 1-12 , û�򴫻� 0
	final private static int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	// ====== ����ũ�� y��m�µ�������
	final private static int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	// ====== ����ũ�� y�����Ф
	final public String animalsYear(int year) {
		final String[] Animals = new String[] { "��", "ţ", "��", "��", "��", "��",
				"��", "��", "��", "��", "��", "��" };
		return Animals[(year - 4) % 12];
	}

	// ====== ���� ���յ�offset ���ظ�֧, 0=����
	final private static String cyclicalm(int num) {
		final String[] Gan = new String[] { "��", "��", "��", "��", "��", "��", "��",
				"��", "��", "��" };
		final String[] Zhi = new String[] { "��", "��", "��", "î", "��", "��", "��",
				"δ", "��", "��", "��", "��" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	// ====== ���� offset ���ظ�֧, 0=����
	final public String cyclical(int year) {
		int num = year - 1900 + 36;
		return (cyclicalm(num));
	}

	public static String getChinaDayString(int day) {
		String chineseTen[] = { "��", "ʮ", "إ", "ئ" };
		int n = day % 10 == 0 ? 9 : day % 10 - 1;
		if (day > 30)
			return "";
		if (day == 10)
			return "��ʮ";
		else
			return chineseTen[day / 10] + chineseNumber[n];
	}

	/** */
	/**
	 * ����y��m��d�ն�Ӧ��ũ��. yearCyl3:ũ������1864������� ? monCyl4:��1900��1��31������,������
	 * dayCyl5:��1900��1��31����������,�ټ�40 ?
	 * 
	 * isday: �������Ϊfalse---����Ϊ�ڼ���ʱ���������ھͷ��ؽڼ��� ��true---���������Ƿ�Ϊ�ڼ�����Ȼ���������Ӧ����������
	 * @param cal
	 * @return
	 */
	public String getLunarDate(int year_log, int month_log, int day_log, boolean isday) {
		// @SuppressWarnings("unused")
		int yearCyl, monCyl, dayCyl;
		//int leapMonth = 0;
		String nowadays;
		Date baseDate = null;
		Date nowaday = null;
		try {
			baseDate = chineseDateFormat.parse("1900��1��31��");
		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use
			// Options | File Templates.
		}

		nowadays = year_log + "��" + month_log + "��" + day_log + "��";
		try {
			nowaday = chineseDateFormat.parse(nowadays);
		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use
			// Options | File Templates.
		}

		// �����1900��1��31����������
		int offset = (int) ((nowaday.getTime() - baseDate.getTime()) / 86400000L);
		dayCyl = offset + 40;
		monCyl = 14;

		// ��offset��ȥÿũ���������
		// ���㵱����ũ���ڼ���
		// i���ս����ũ�������
		// offset�ǵ���ĵڼ���
		int iYear, daysOfYear = 0;
		for (iYear = 1900; iYear < 10000 && offset > 0; iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
			monCyl += 12;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
			monCyl -= 12;
		}
		// ũ�����
		year = iYear;
        setYear(year);  //���ù�����Ӧ��ũ�����
		
		yearCyl = iYear - 1864;
		leapMonth = leapMonth(iYear); // ���ĸ���,1-12
		leap = false;

		// �õ��������offset,�����ȥÿ�£�ũ��������������������Ǳ��µĵڼ���
		int iMonth, daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// ����
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else
				daysOfMonth = monthDays(year, iMonth);

			offset -= daysOfMonth;
			// �������
			if (leap && iMonth == (leapMonth + 1))
				leap = false;
			if (!leap)
				monCyl++;
		}
		// offsetΪ0ʱ�����Ҹղż�����·������£�ҪУ��
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
				--monCyl;
			}
		}
		// offsetС��0ʱ��ҲҪУ��
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
			--monCyl;
		}
		month = iMonth;
		setLunarMonth(chineseNumber[month - 1] + "��");  //���ö�Ӧ�������·�
		day = offset + 1;

		if(!isday){
			//�������Ϊ�ڼ��������������򷵻ؽڼ���
			//setLeapMonth(leapMonth);
			for(int i = 0; i < solarHoliday.length; i++){
				//���ع����ڼ�������
				String sd = solarHoliday[i].split(" ")[0];  //�ڼ��յ�����
				String sdv = solarHoliday[i].split(" ")[1]; //�ڼ��յ�����
				String smonth_v = month_log+"";
				String sday_v = day_log+"";
				String smd = "";
				if(month_log < 10){
					smonth_v = "0"+month_log;
				}
				if(day_log < 10){
					sday_v = "0"+day_log;
				}
				smd = smonth_v+sday_v;
				if(sd.trim().equals(smd.trim())){
					return sdv;
				}
			}
			
			for(int i = 0; i < lunarHoliday.length; i++){
				//����ũ���ڼ�������
				String ld =lunarHoliday[i].split(" ")[0];   //�ڼ��յ�����
				String ldv = lunarHoliday[i].split(" ")[1];  //�ڼ��յ�����
				String lmonth_v = month+"";
				String lday_v = day+"";
				String lmd = "";
				if(month < 10){
					lmonth_v = "0"+month;
				}
				if(day < 10){
					lday_v = "0"+day;
				}
				lmd = lmonth_v+lday_v;
				if(ld.trim().equals(lmd.trim())){
					return ldv;       
				}
			}
		}
	    if (day == 1)
			return chineseNumber[month - 1] + "��";
		else
			return getChinaDayString(day);

	}

	
	public String toString() {
		if (chineseNumber[month - 1] == "һ" && getChinaDayString(day) == "��һ")
			return "ũ��" + year + "��";
		else if (getChinaDayString(day) == "��һ")
			return chineseNumber[month - 1] + "��";
		else
			return getChinaDayString(day);
		// return year + "��" + (leap ? "��" : "") + chineseNumber[month - 1] +
		// "��" + getChinaDayString(day);
	}
	
	/*public static void main(String[] args) {
		System.out.println(new LunarCalendar().getLunarDate(2012, 1, 23));
	}*/
	
	public int getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(int leapMonth) {
		this.leapMonth = leapMonth;
	}
	
	/**
	 * �õ���ǰ���ڶ�Ӧ�������·�
	 * @return
	 */
	public String getLunarMonth() {
		return lunarMonth;
	}
	
	public void setLunarMonth(String lunarMonth) {
		this.lunarMonth = lunarMonth;
	}
	
	/**
	 * �õ���ǰ���Ӧ��ũ�����
	 * @return
	 */
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
