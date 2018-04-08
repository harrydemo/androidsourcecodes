package com.cn.daming;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmClockView extends View{
	
	private MainActivity activity;
	private LinearLayout mLinearLayout;
	private Context context;

	private DataBaseHelper dbHelper;
	private Cursor cursor;
	private ListView mAlarmListView;
	
	private List<String> ids;
	private List<String> times;
	private List<String> repeats;
	private List<String> isopens;
	private List<String> kinds;
	
	private AlarmClockAdapter alarmAdapter;
	
	Calendar c=Calendar.getInstance();
	TextView time1TextView = null;
	TextView time2TextView = null;
	Button setTimeButton1 = null;
	Button setTimeButton2 = null;
	TextView repeat1TextView = null;
	TextView repeat2TextView = null;
	Button repeatButton1 = null;
	Button repeatButton2 = null;
	CheckBox time1CheckBox = null;
	CheckBox time2CheckBox = null;
	
	String tmpS1 = "目前无设置";
	String repeatString1 = "目前无设置";
	String tmpS2 = "目前无设置";
	String repeatString2 = "目前无设置";
	boolean isOpenInt1 = false;
    boolean isOpenInt2 = false;
	String isOpentime1 = null;
	String isOpentime2 = null;
	
	boolean isOpenAlarm = false;
	CheckBox isOpenCheckBox;
	
	int[] repeatArray1 = {0,0,0,0,0,0,0};
	int[] repeatArray2 = {0,0,0,0,0,0,0};
	
	boolean repeat_1_1 = false;
	boolean repeat_1_2 = false;
    boolean repeat_1_3 = false;
	boolean repeat_1_4 = false;
	boolean repeat_1_5 = false;
	boolean repeat_1_6 = false;
	boolean repeat_1_7 = false;
	
	boolean repeat_2_1 = false;
	boolean repeat_2_2 = false;
	boolean repeat_2_3 = false;
	boolean repeat_2_4 = false;
	boolean repeat_2_5 = false;
	boolean repeat_2_6 = false;
	boolean repeat_2_7 = false;
	
	
	public AlarmClockView(final Context context,MainActivity activity, LinearLayout linearLayout) {
		super(context);
			this.context = context;
	        this.activity = activity;
	        this.mLinearLayout = linearLayout;
	        View alarmView = View.inflate(context, R.layout.alarm_listview, null);
	        mLinearLayout.addView(alarmView);  
	        mAlarmListView = (ListView)alarmView.findViewById(R.id.alarm_list);
	        mAlarmListView.setCacheColorHint(0);
	        dbHelper = new DataBaseHelper(context);
	        refreshDBHelper();
	        
	        mAlarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View v, int position,
						long arg3) {
					
					showAlarmDialog(position); //点击闹钟出现设置页面
				}
			});
	}
	
	public void refreshDBHelper()
	{
		cursor = dbHelper.selectAlarmColock();
		ids = new ArrayList<String>();
		times = new ArrayList<String>();
		repeats = new ArrayList<String>();
		isopens = new ArrayList<String>();
		kinds = new ArrayList<String>();
		
		int count = cursor.getCount();
		if(count==0){
			String[] tempStr = new String[4];
        	for(int i=1;i<=2;i++){
        		dbHelper.insertAlarmColock(tempStr);
        	}
        	int count2 = cursor.getCount();
        	if(count2>0){
    	        for(int i=0;i<count2;i++){
    	        	cursor.moveToPosition(i);
    	        	ids.add(cursor.getString(0));
    	        	times.add(cursor.getString(1));
    	        	repeats.add(cursor.getString(2));
    	        	isopens.add(cursor.getString(3));
    	        	kinds.add(cursor.getString(4));
    	        }
            }
        	cursor.close();
            dbHelper.close();
        	refreshDBHelper();
            Toast.makeText(context, R.string.not_dbcursor_values, Toast.LENGTH_SHORT).show();
		}
		else if(count>0){
	        for(int i=0;i<count;i++){
	        	cursor.moveToPosition(i);
	        	ids.add(cursor.getString(0));
	        	times.add(cursor.getString(1));
	        	repeats.add(cursor.getString(2));
	        	isopens.add(cursor.getString(3));
	        	kinds.add(cursor.getString(4));
	        }
        }
		alarmAdapter = new AlarmClockAdapter(context,ids,times,repeats,isopens,kinds);  
		mAlarmListView.setAdapter(alarmAdapter);
		cursor.close();
        dbHelper.close();
	}

	
	
	public void showAlarmDialog(int position){
		
		//设置闹钟弹窗
	    
	    dbHelper = new DataBaseHelper(context);
	    cursor = dbHelper.selectAlarmColock();
        int count = cursor.getCount();
    	
    	if(position == 0){    		
    		LayoutInflater factory = LayoutInflater.from(context);
            final View alarm1View = factory.inflate(R.layout.time_repeat_dialog, null);
            time1TextView = (TextView)alarm1View.findViewById(R.id.text); //标题设置闹钟
            setTimeButton1 = (Button)alarm1View.findViewById(R.id.mButton);//设置时间
            repeat1TextView = (TextView)alarm1View.findViewById(R.id.repeattext);
            repeatButton1 = (Button)alarm1View.findViewById(R.id.repeatButton); //设置重复
            time1CheckBox = (CheckBox)alarm1View.findViewById(R.id.isopen_check);
            String isOpen1 = "关";
            
            if(count==2){
            	for(int i=0;i<count;i++){
                  if(i == 0){
                	  cursor.moveToPosition(i);
                	  if(cursor.getString(1)==null){
                		  time1TextView.setText(tmpS1);
                	  }else{
                		  time1TextView.setText(cursor.getString(1));
                		  tmpS1 = cursor.getString(1);
                	  }
                	  if((cursor.getString(2))==null){
                		  repeat1TextView.setText(repeatString1);
                	  }else{
                		  repeat1TextView.setText(cursor.getString(2));
                		  repeatString1 = cursor.getString(2);
                	  }
                	  isOpen1 = cursor.getString(3);
                	  if(isOpen1!=null){
                		  if(isOpen1.equals("开")){
                			  time1CheckBox.setChecked(true);
                			  isOpenInt1 = true;
                		  }else{
                			  time1CheckBox.setChecked(false);
                			  isOpenInt1 = false;
                		  }
                	  }
                  }
    	        }
            }
            
            time1CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){//是否开启闹钟
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg0.isChecked()){
						isOpenInt1 = true;
					}else{
						isOpenInt1 = false;
					}
				}
            });
            
            
            
            
            setTimeButton1.setOnClickListener(new OnClickListener(){//设置时间
				public void onClick(View arg0) {
			          c.setTimeInMillis(System.currentTimeMillis());//设置当前时间
			          int mHour=c.get(Calendar.HOUR_OF_DAY);
			          int mMinute=c.get(Calendar.MINUTE);
			          int mDay=c.get(Calendar.DAY_OF_WEEK);
			          
			          new TimePickerDialog(context,
			            new TimePickerDialog.OnTimeSetListener()
			            {                
			              public void onTimeSet(TimePicker view,int hourOfDay,
			                                    int minute)
			              {
			                c.setTimeInMillis(System.currentTimeMillis());
			                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
			                c.set(Calendar.MINUTE,minute);
			                c.set(Calendar.SECOND,0);
			                c.set(Calendar.MILLISECOND,0);
			                
			                tmpS1=format(hourOfDay)+"："+format(minute);
			                time1TextView.setText(tmpS1);
		                
							Toast.makeText(context,
									"设置闹钟时间为" + tmpS1,
									Toast.LENGTH_SHORT).show();
			              }          
			            },mHour,mMinute,true).show();
				}
            });
            
            
            
            
            
            
            
            repeatButton1.setOnClickListener(new OnClickListener(){//设置重复
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					repeatString1 = "";
					new AlertDialog.Builder(context)
		            .setTitle(R.string.alert_dialog_multi_choice)
		            .setMultiChoiceItems(R.array.select_dialog_items,
		                    new boolean[]{false, false, false, false, false, false, false},
		                    new DialogInterface.OnMultiChoiceClickListener() {
		                        public void onClick(DialogInterface dialog, int whichButton,
		                                boolean isChecked) {
		                        	/* User clicked on a check box do some stuff */
			                        switch(whichButton){
			                     	   case 0: if(isChecked){
			                     		          repeatArray1[0] = 1;
			                     	           }else{
			                     	        	  repeatArray1[0] = 0;
			                     	           }
			                     	           break;
			                     	   case 1:if(isChecked){
			              		                 repeatArray1[1] = 1;
			             	                  }else{
			             	        	         repeatArray1[1] = 0;
			             	                  }
			                     	           break;
			                     	   case 2:if(isChecked){
			                     		         repeatArray1[2] = 1;
			                     	          }else{
			                     	        	 repeatArray1[2] = 0;
			                     	          }
			             	                   break;
			                     	   case 3:if(isChecked){
			                     		         repeatArray1[3] = 1;
			                     	          }else{
			                     	        	 repeatArray1[3] = 0;
			                     	          }
			             	                   break;
			                     	   case 4:if(isChecked){
			                     		         repeatArray1[4] = 1;
			                     	          }else{
			                     	        	 repeatArray1[4] = 0;
			                     	          }
			             	                   break;
			                     	   case 5:if(isChecked){
			                     		         repeatArray1[5] = 1;
			                     	          }else{
			                     	        	 repeatArray1[5] = 0;
			                     	          }
			             	                   break;
			                     	   case 6:if(isChecked){
			                     		         repeatArray1[6] = 1;
			                     	          }else{
			                     	        	 repeatArray1[6] = 0;
			                     	          }
			             	                   break;
			                     	   default: break;
			                     	}                        	
		                        }
		                    })
		            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() { //设置重复确定按钮
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	
		                    if(repeatArray1[0] == 1){
		                    	repeatString1 += "周一"+",";
		                    	repeat_1_1 = true;
		                    }else{
		                    	repeat_1_1 = false;
		                    }
		                    
		                    if(repeatArray1[1] == 1){
		                    	repeatString1 += "周二"+",";
		                    	repeat_1_2 = true;
		                    }else{
		                    	repeat_1_2 = false;
		                    }
		                    
		                    if(repeatArray1[2] == 1){
		                    	repeatString1 += "周三"+",";
		                    	repeat_1_3 = true;
		                    }else{
		                    	repeat_1_3 = false;
		                    }
		                  
		                    if(repeatArray1[3] == 1){
		                    	repeatString1 += "周四"+",";
		                    	repeat_1_4 = true;
		                    }else{
		                    	repeat_1_4 = false;
		                    }
		                    
		                    if(repeatArray1[4] == 1){
		                    	repeatString1 += "周五"+",";
		                    	repeat_1_5 = true;
		                    }else{
		                    	repeat_1_5 = false;
		                    }
		                    
		                    if(repeatArray1[5] == 1){
		                    	repeatString1 += "周六"+",";
		                    	repeat_1_6 = true;
		                    }else{
		                    	repeat_1_6 = false;
		                    }
		                    
		                    if(repeatArray1[6] == 1){
		                    	repeatString1 += "周日";
		                    	repeat_1_7 = true;
		                    }else{
		                    	repeat_1_7 = false;
		                    }
		                    
		                    if(!(repeat_1_1 || repeat_1_2 || repeat_1_3 || repeat_1_4 ||
		                    		repeat_1_5 || repeat_1_6 || repeat_1_7)){
		                    	repeatString1 = ("无重复");
		                    }
		                    
		                    repeat1TextView.setText(repeatString1);

		                    for(int i=0;i<repeatArray1.length;i++){
		                    	repeatArray1[i] = 0;
		                    } 
		                }
		            })
		            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	/* User clicked No so do some stuff */

		                }
		            })
		           .show();
				}
            });
            
            
            
            
            //show AlertDialog
            new AlertDialog.Builder(context)
            .setIcon(R.drawable.alarm_dialog)
            .setTitle(R.string.alarm_dialog_title)
            .setView(alarm1View)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	if(isOpenInt1){
	                	isOpentime1 = "开";
	                	Intent intent1 = new Intent(context, CallAlarm.class);
	                	intent1.putExtra("RESULT", "alarm1");
	                	
	                	PendingIntent sender=PendingIntent.getBroadcast(
	                			context,0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
	                	AlarmManager am;
	                	am = (AlarmManager)activity.getSystemService(context.ALARM_SERVICE);
	                	int nowDay = Contants.getNowWeek();
	                	int setDay = 0;
	                	if(repeatString1.equals("目前无设置"))
	                	{
	                		if(Contants.differSetTimeAndNowTime(c.getTimeInMillis())){
                				am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),sender);
                			}
	                		else{
	                			Toast.makeText(context, R.string.not_time_right, Toast.LENGTH_SHORT);
	                		}
	                	}
	                	if(!(repeatString1.equals("目前无设置"))){
	                		String[] setStr = repeatString1.split(",");
	                		int[] dayOfNum = Contants.getDayOfNum(setStr);
	                		setDay = Contants.getResultDifferDay(dayOfNum, nowDay);
	                		int differDay = Contants.compareDayNowToNext(nowDay, setDay);
	                		if(differDay == 0){
	                			if(Contants.differSetTimeAndNowTime(c.getTimeInMillis())){
	                				am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),sender);
	                			}else{
	                				am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis() + Contants.getDifferMillis(7),sender);
	                			}
	                		}else{
	                			am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis() + Contants.getDifferMillis(differDay),sender);  
	                		}
	                	}
	                }else{
	                	isOpentime1 = "关";
	                	Intent intent = new Intent(context, CallAlarm.class);
	                    PendingIntent sender=PendingIntent.getBroadcast(
	                                           context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	                    AlarmManager am;
	                    am =(AlarmManager)activity.getSystemService(context.ALARM_SERVICE);
	                    am.cancel(sender);
	                    Toast.makeText(context,R.string.alarm_delete1,
	                                   Toast.LENGTH_SHORT).show();
	                }
                    String[] temStr = new String[7];
                    temStr[0] = tmpS1;
                    temStr[1] = repeatString1;
	                temStr[2] = isOpentime1;
	                dbHelper.updateAlarmColock(1+"", temStr);
	                refreshDBHelper();
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	refreshDBHelper();
                }
            })
            .create().show();
            Log.v("tag", "this is click Item1");
    	}
    	
    	
    	
    	////////下一个
    	
    	
    	else if(position == 1){
    		LayoutInflater factory = LayoutInflater.from(context);
            final View alarm1View = factory.inflate(R.layout.time_repeat_dialog, null);
            time2TextView = (TextView)alarm1View.findViewById(R.id.text);
            setTimeButton2 = (Button)alarm1View.findViewById(R.id.mButton);
            repeat2TextView = (TextView)alarm1View.findViewById(R.id.repeattext);
            repeatButton2 = (Button)alarm1View.findViewById(R.id.repeatButton);
            time2CheckBox = (CheckBox)alarm1View.findViewById(R.id.isopen_check);
           
            String isOpen2 = "关";
            
            if(count==2){
            	for(int i=0;i<count;i++){
                  if(i == 1){
                	  cursor.moveToPosition(i);
                	  if(cursor.getString(1)==null){
                		  time2TextView.setText(tmpS2);
                	  }else{
                		  time2TextView.setText(cursor.getString(1));
                		  tmpS2 = cursor.getString(1);
                	  }
                	  if((cursor.getString(2))==null){
                		  repeat2TextView.setText(repeatString2);
                	  }else{
                		  repeat2TextView.setText(cursor.getString(2));
                		  repeatString2 = cursor.getString(2);
                	  }
                	  isOpen2 = cursor.getString(3);
                	  if(isOpen2!=null){
                		  if(isOpen2.equals("开")){
                			  time2CheckBox.setChecked(true);
                			  isOpenInt2 = true;
                		  }else{
                			  time2CheckBox.setChecked(false);
                			  isOpenInt2 = false;
                		  }
                	  }
                  }
    	        }
            }
            
            time2CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if(arg0.isChecked()){
						isOpenInt2 = true;
					}else{
						isOpenInt2 = false;
					}
				}
            });
            
            setTimeButton2.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
			          c.setTimeInMillis(System.currentTimeMillis());
			          int mHour=c.get(Calendar.HOUR_OF_DAY);
			          int mMinute=c.get(Calendar.MINUTE);
			          int mDay=c.get(Calendar.DAY_OF_WEEK);
			          
			          new TimePickerDialog(context,
			            new TimePickerDialog.OnTimeSetListener()
			            {                
			              public void onTimeSet(TimePicker view,int hourOfDay,
			                                    int minute)
			              {
			                c.setTimeInMillis(System.currentTimeMillis());
			                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
			                c.set(Calendar.MINUTE,minute);
			                c.set(Calendar.SECOND,0);
			                c.set(Calendar.MILLISECOND,0);
			                	
			                tmpS2=format(hourOfDay)+"："+format(minute);
			                time2TextView.setText(tmpS2);
							Toast.makeText(context,
									"设置闹钟时间为" +tmpS2,
									Toast.LENGTH_SHORT).show();

			              }          
			            },mHour,mMinute,true).show();
				}
            });
            
            repeatButton2.setOnClickListener(new OnClickListener(){ //设置重复

				public void onClick(View arg0) {
					repeatString2 = "";
					new AlertDialog.Builder(context)
		            .setTitle(R.string.alert_dialog_multi_choice)
		            .setMultiChoiceItems(R.array.select_dialog_items,
		                    new boolean[]{false, false, false, false, false, false, false},
		                    new DialogInterface.OnMultiChoiceClickListener() {
		                        public void onClick(DialogInterface dialog, int whichButton,
		                                boolean isChecked) {
		                        	/* User clicked on a check box do some stuff */
			                        switch(whichButton){
			                     	   case 0: if(isChecked){
			                     		          repeatArray2[0] = 1;
			                     	           }else{
			                     	        	  repeatArray2[0] = 0;
			                     	           }
			                     	           break;
			                     	   case 1:if(isChecked){
			              		                 repeatArray2[1] = 1;
			             	                  }else{
			             	        	         repeatArray2[1] = 0;
			             	                  }
			                     	           break;
			                     	   case 2:if(isChecked){
			                     		         repeatArray2[2] = 1;
			                     	          }else{
			                     	        	 repeatArray2[2] = 0;
			                     	          }
			             	                   break;
			                     	   case 3:if(isChecked){
			                     		         repeatArray2[3] = 1;
			                     	          }else{
			                     	        	 repeatArray2[3] = 0;
			                     	          }
			             	                   break;
			                     	   case 4:if(isChecked){
			                     		         repeatArray2[4] = 1;
			                     	          }else{
			                     	        	 repeatArray2[4] = 0;
			                     	          }
			             	                   break;
			                     	   case 5:if(isChecked){
			                     		         repeatArray2[5] = 1;
			                     	          }else{
			                     	        	 repeatArray2[5] = 0;
			                     	          }
			             	                   break;
			                     	   case 6:if(isChecked){
			                     		         repeatArray2[6] = 1;
			                     	          }else{
			                     	        	 repeatArray2[6] = 0;
			                     	          }
			             	                   break;
			                     	   default: break;
			                     	}                        	
		                        }
		                    })
		            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) { //设置显示的日期
		                	
		                    if(repeatArray2[0] == 1){
		                    	repeatString2 += "周一"+",";
		                    	repeat_2_1 = true;
		                    }else{
		                    	repeat_2_1 = false;
		                    }
		                    
		                    if(repeatArray2[1] == 1){
		                    	repeatString2 += "周二"+",";
		                    	repeat_2_2 = true;
		                    }else{
		                    	repeat_2_2 = false;
		                    }
		                    
		                    if(repeatArray2[2] == 1){
		                    	repeatString2 += "周三"+",";
		                    	repeat_2_3 = true;
		                    }else{
		                    	repeat_2_3 = false;
		                    }
		                  
		                    if(repeatArray2[3] == 1){
		                    	repeatString2 += "周四"+",";
		                    	repeat_2_4 = true;
		                    }else{
		                    	repeat_2_4 = false;
		                    }
		                    
		                    if(repeatArray2[4] == 1){
		                    	repeatString2 += "周五"+",";
		                    	repeat_2_5 = true;
		                    }else{
		                    	repeat_1_5 = false;
		                    }
		                    
		                    if(repeatArray2[5] == 1){
		                    	repeatString2 += "周六"+",";
		                    	repeat_2_6 = true;
		                    }else{
		                    	repeat_2_6 = false;
		                    }
		                    
		                    if(repeatArray2[6] == 1){
		                    	repeatString2 += "周日";
		                    	repeat_2_7 = true;
		                    }else{
		                    	repeat_2_7 = false;
		                    }
		                    
		                    if(!(repeat_2_1 || repeat_2_2 || repeat_2_3 || repeat_2_4 ||
		                    		repeat_2_5 || repeat_2_6 || repeat_2_7)){
		                    	repeatString2 = ("无设置");
		                    }
		                    
		                    repeat2TextView.setText(repeatString2);
		                    
		                    for(int i=0;i<repeatArray1.length;i++){
		                    	repeatArray2[i] = 0;
		                    } 
		                }
		            })
		            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	/* User clicked No so do some stuff */

		                }
		            })
		            .show();				
				}
            });
            
            // show Dialog 
            new AlertDialog.Builder(context)
            .setIcon(R.drawable.alarm_dialog)
            .setTitle(R.string.alarm_dialog_title)
            .setView(alarm1View)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
   
                	if(isOpenInt2){
	                	isOpentime2 = "开";
	                	Intent intent2 = new Intent(context, CallAlarm.class);
	                	intent2.putExtra("RESULT", "alarm2");
	                	PendingIntent sender=PendingIntent.getBroadcast(
	                			context,1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
	                	AlarmManager am;
	                	am = (AlarmManager)activity.getSystemService(context.ALARM_SERVICE);
	                	int nowDay = Contants.getNowWeek();
	                	int setDay = 0;
	                	if(repeatString2.equals("目前无设置"))
	                	{
	                		if(Contants.differSetTimeAndNowTime(c.getTimeInMillis())){
                				am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),sender);
                			}
	                		else{
	                			Toast.makeText(context, R.string.not_time_right, Toast.LENGTH_SHORT);
	                		}
	                	}
	                	if(!(repeatString2.equals("目前无设置"))){
	                		String[] setStr = repeatString2.split(",");
	                		int[] dayOfNum = Contants.getDayOfNum(setStr);
	                		setDay = Contants.getResultDifferDay(dayOfNum, nowDay);
	                		int differDay = Contants.compareDayNowToNext(nowDay, setDay);
	                		if(differDay == 0){
	                			if(Contants.differSetTimeAndNowTime(c.getTimeInMillis())){
	                				am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),sender);
	                			}else{
	                				am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis() + Contants.getDifferMillis(7),sender);
	                			}
	                		}else{
	                			am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis() + Contants.getDifferMillis(differDay),sender);  
	                		}
	                	}
	                }else{
	                	isOpentime2 = "关";
	                	Intent intent = new Intent(context, CallAlarm.class);
	                	intent.putExtra("RESULT", "cancel");
	                    PendingIntent sender=PendingIntent.getBroadcast(
	                                           context,1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	                    AlarmManager am;
	                    am =(AlarmManager)activity.getSystemService(context.ALARM_SERVICE);
	                    am.cancel(sender);
	                    Toast.makeText(context,R.string.alarm_delete2,
	                                   Toast.LENGTH_SHORT).show();
	                }
	                
	                String[] temStr = new String[7];
	                temStr[0] = tmpS2;
	                temStr[1] = repeatString2;
	                temStr[2] = isOpentime2;
	                dbHelper.updateAlarmColock(2+"", temStr);
	                refreshDBHelper();
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	refreshDBHelper();
                }
            })
            .create().show();
    	}
    	cursor.close();
        dbHelper.close();
	}
	
	
	
	
	
	
	
	private String format(int x)
    {
	    String s=""+x;
	    if(s.length()==1) s="0"+s;
	    return s;
	}
}
