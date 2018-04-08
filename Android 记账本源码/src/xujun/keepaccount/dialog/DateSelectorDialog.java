package xujun.keepaccount.dialog;

import java.util.Calendar;

import xujun.keepaccount.R;
import xujun.util.CalendarUtil;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class DateSelectorDialog extends Activity
{
	private Button btnCancel;
	private Button btnOk;
	
	private DatePicker startDataPicker;
	private DatePicker endDatePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dateselector_dialog);
		//设置图标必须在setContentView之后,否则不显示
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.calendar);
		
		startDataPicker = (DatePicker) findViewById(R.id.date_start);
		endDatePicker = (DatePicker) findViewById(R.id.date_end);
		//开始时间默认是结束时间之前一周
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.YEAR,endDatePicker.getYear());
		endDate.set(Calendar.MONTH,endDatePicker.getMonth());
		endDate.set(Calendar.DATE,endDatePicker.getDayOfMonth());
		
		endDate.add(Calendar.DATE, -7);
		
		startDataPicker.init(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DATE), null);
		
		btnCancel = (Button) findViewById(R.id.btn_setDateCancel);
		btnOk = (Button) findViewById(R.id.btn_setDateOK);
		
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {			
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		btnOk.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String startDate = CalendarUtil.getDataString(startDataPicker.getYear(), startDataPicker.getMonth()+1, startDataPicker.getDayOfMonth());
				String endDate = CalendarUtil.getDataString(endDatePicker.getYear(), endDatePicker.getMonth()+1, endDatePicker.getDayOfMonth());
				Intent resultIntent = new Intent();
				resultIntent.putExtra("startDate", startDate);
				resultIntent.putExtra("endDate", endDate);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
	}
}
