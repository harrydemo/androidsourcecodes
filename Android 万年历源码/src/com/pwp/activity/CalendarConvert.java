package com.pwp.activity;

import com.pwp.borderText.BorderTextView;
import com.pwp.calendar.LunarCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

/**
 * ����ת��
 * @author jack_peng
 *
 */
public class CalendarConvert extends Activity {

	private LunarCalendar lc = null;
	private BorderTextView convertDate = null;
	private BorderTextView convertBT = null;
	private TextView lunarDate = null;
	
	private int year_c;
	private int month_c;
	private int day_c;
	
	public CalendarConvert(){
		lc = new LunarCalendar();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convert);

		convertDate = (BorderTextView) findViewById(R.id.convertDate);
		convertBT = (BorderTextView) findViewById(R.id.convert);
		lunarDate = (TextView) findViewById(R.id.convertResult);
		
		Intent intent = getIntent();
		int[] date = intent.getIntArrayExtra("date");
		year_c = date[0];
		month_c = date[1];
		day_c = date[2];
		convertDate.setText(year_c+"��"+month_c+"��"+day_c);
		
		convertDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new DatePickerDialog(CalendarConvert.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {

						if(year < 1901 || year > 2049){
							//���ڲ�ѯ��Χ��
							new AlertDialog.Builder(CalendarConvert.this).setTitle("��������").setMessage("��ת���ڷ�Χ(1901/1/1-2049/12/31)").setPositiveButton("ȷ��", null).show();
						}else{
							year_c = year;
							month_c = monthOfYear+1;
							day_c = dayOfMonth;
							convertDate.setText(year_c+"��"+month_c+"��"+day_c);
						}
					}
				}, year_c, month_c-1, day_c).show();
			}
		});
		
		convertBT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String lunarDay = getLunarDay(year_c,month_c,day_c);
				String lunarYear = String.valueOf(lc.getYear());
				String lunarMonth = lc.getLunarMonth();
				
				lunarDate.setText(lunarYear+"��"+lunarMonth+lunarDay);
			}
		});
		
	}
	
	/**
	 * �������ڵ������շ�����������
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {������ȡ��������Ӧ����������ʱ������������ڶ�Ӧ����������Ϊ"��һ"���ͱ����ó����·�(��:���£����¡�������)},�����ڴ˾�Ҫ�жϵõ������������Ƿ�Ϊ�·ݣ�������·ݾ�����Ϊ"��һ"
		if (lunarDay.substring(1, 2).equals("��")) {
			lunarDay = "��һ";
		}
		return lunarDay;
	}
}
