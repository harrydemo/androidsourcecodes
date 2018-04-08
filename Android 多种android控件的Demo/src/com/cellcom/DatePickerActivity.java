package com.cellcom;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

//DatePicker����ѡ������TimePickerʱ��ѡ���� ʹ��
public class DatePickerActivity extends Activity {

	private Button datePickerButton;
	private DatePicker datePicker1;
	private TimePicker timePicker1;
	
	//Ĭ������ϵͳ��ǰʱ��
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");		
	String str=sdf.format(new Date());
	int year=Integer.parseInt(str.substring(0, 4));
	int month=Integer.parseInt(str.substring(5,7))-1;
	int day=Integer.parseInt(str.substring(8,10));
	int hour=Integer.parseInt(str.substring(11,13));
	int minute=Integer.parseInt(str.substring(14,16));
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_picker);
		setTitle("DatePicker��TimePickerʾ��!");
		
		datePicker1=(DatePicker)findViewById(R.id.datePicker1);
		timePicker1=(TimePicker)findViewById(R.id.timePicker1);
		datePickerButton=(Button)findViewById(R.id.datePickerButton1);

		datePicker1.init(year,month,day, null);
		
		timePicker1.setCurrentHour(hour);
		timePicker1.setCurrentMinute(minute);
		
		timePicker1.setIs24HourView(true);
		
		datePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setTitle("��ѡ����ǣ�"+datePicker1.getYear()+"��"+datePicker1.getMonth()+"��"+datePicker1.getDayOfMonth()+"�� "+timePicker1.getCurrentHour()+"Сʱ"+timePicker1.getCurrentMinute()+"��");
			}
		});
	}

}
