package com.bn.helper;


import java.util.ArrayList;
import java.util.List;

import com.bn.helper.Address;
import com.bn.helper.DBUtil;
import com.bn.helper.MapNavigateActivity;
import com.bn.helper.R;
import com.bn.helper.DBUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView.LayoutParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import static com.bn.helper.Constant.SUM;
import static com.bn.helper.Constant.*;

public class TrainSystemHelperActivity extends Activity {
    /** Called when the activity is first created. */
	  String currLine;
	  String currStation;
	  int screenWidth;
	  int screenHeight;
	  int state=0;//state=0时列表选择state==1时最短路径查询
	  static boolean flag1=true,flag2=true,flag3=true;
	  static String str;
	  GeoPoint gpLocation1;
	  ArrayList<Address> nearlist=new ArrayList<Address>();
	  static final int NEAR_DISTANCE=1;
	  Dialog nearDialog;
	  SeekBar sb;
	  
	  Handler hd=new Handler() //接受信息界面跳转
	    {
		   @Override
		  public void handleMessage(Message msg)//重写方法  
		  {
			 switch(msg.what)
			 {
			     case 0:
			    	 gotoMainView();  //主界面
				    break;		     
			 }
		 }
	   };	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;
        screenHeight=dm.heightPixels;
        
        //设置为横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //下两句为设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		gotoSurfaceView();
		
		
    }
    
  //欢迎界面
    public void gotoSurfaceView()
    {
    	MySurfaceView mView=new MySurfaceView(this);
    	setContentView(mView);
    }
    
    public void gotoMainView()
    {
    	setContentView(R.layout.main);  
		DBUtil.initData();
		initlinesSpinner();    
		List<String> linesList=DBUtil.searchLineList();
		currLine=linesList.get(0);
		initstationSpinner(currLine);
		List<String> stationList=DBUtil.searchStationList(currLine);
		currStation=stationList.get(0);
		
		final CheckBox cb1=(CheckBox)this.findViewById(R.id.checkbox01);
		final CheckBox cb2=(CheckBox)this.findViewById(R.id.checkbox02);
		final CheckBox cb3=(CheckBox)this.findViewById(R.id.checkbox03);
		
		cb1.setOnClickListener(
			new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					flag1=!flag1;
				}
				
			}
		);
		cb2.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						flag2=!flag2;
					}
					
				}
			);
		cb3.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						flag3=!flag3;
					}
					
				}
			);
		
		ImageButton im=(ImageButton)this.findViewById(R.id.ImageButton01);
		im.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(state==0)
						{
							if(cb1.isChecked()==true)
							{
								flag1=true;
							}
							if(cb2.isChecked()==true)
							{
								flag2=true;
							}
							if(cb3.isChecked()==true)
							{
								flag3=true;
							}
							String[] jwd=DBUtil.searchJWD(currLine,currStation);
							Bundle bundle=new Bundle();
							bundle.putString("jdStr", jwd[0]);
							bundle.putString("wdStr", jwd[1]);
							bundle.putString("msg",currStation+"\n经度："+getjwdfromString(jwd[0])+"\n纬度："+getjwdfromString(jwd[1]));
							str=currLine;
							Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);
							intent.putExtras(bundle);					
							TrainSystemHelperActivity.this.startActivity(intent);
						}
						else if(state==1) 
						{
							flag1=true;
							flag2=true;
							flag3=true;
							sb=(SeekBar)findViewById(R.id.seekbar011);						
							int current=sb.getProgress();
							double middle=(current)/100.00*SUM;
							
							List<String> totallist=new ArrayList<String>();
							totallist.clear();
							totallist=DBUtil.searchTotaljw();
							//定位自己的位置
						    ////获取位置管理器实例
					        String serviceName=Context.LOCATION_SERVICE;
					        LocationManager lm=(LocationManager)getSystemService(serviceName);        
					        Criteria criteria = new Criteria();
					        criteria.setAccuracy(Criteria.ACCURACY_FINE);
					        criteria.setAltitudeRequired(false);
					        criteria.setBearingRequired(false);
					        criteria.setCostAllowed(true);
					        criteria.setPowerRequirement(Criteria.POWER_LOW);
					        String provider = lm.getBestProvider(criteria, true);
					        Location  tempLo= lm.getLastKnownLocation(provider);
					        if(tempLo!=null)
					        {
					            gpLocation1 = new GeoPoint
					            (
					            		(int)(tempLo.getLatitude()*1E6), //纬度
					            		(int)(tempLo.getLongitude()*1E6) //经度
					            );
					        }
					        else
					        {
					        	Toast.makeText(TrainSystemHelperActivity.this, "请打开GPS，并确保其正常工作！", Toast.LENGTH_LONG).show();
					        }				        
					        //位置变化监听器
					        LocationListener ll=new LocationListener()
					        {
								@Override
								public void onLocationChanged(Location location) 
								{//当位置变化时触发
									gpLocation1 = new GeoPoint
						            (
						            		(int)(location.getLatitude()*1E6), //纬度
						            		(int)(location.getLongitude()*1E6) //经度
						            );	
								}

								@Override
								public void onProviderDisabled(String provider) 
								{//Location Provider被禁用时更新				
									
								}

								@Override
								public void onProviderEnabled(String provider) 
								{//Location Provider被启用时更新					
									
								}

								@Override
								public void onStatusChanged(String provider, int status,
										Bundle extras) 
								{
									//当Provider硬件状态变化是更新
									
								}      	
					        };
					        
					        //添加位置变化监听器
					        lm.requestLocationUpdates
					        (
					        		LocationManager.GPS_PROVIDER, 	//使用GPS定位
					        		2000, 							//时间分辨率 ms
					        		5, 								//距离分辨率m
					        		ll								//位置变化监听器
					        );
					        
					        
					        nearlist.clear();
					        try {				        	
					        	for(int i=0;i<totallist.size();)
								{
					        		int j=i;								
									if(calculationdistance(tempLo.getLongitude(),tempLo.getLatitude(),totallist.get(j),totallist.get(j+1))<=middle)
									{
									Address ai=new Address 
									(
											totallist.get(j),    //经度
											totallist.get(j+1),     //纬度
											totallist.get(j+3)+"\n经度："+getjwdfromString(totallist.get(j))+"\n纬度："+getjwdfromString(totallist.get(j+1)),
											totallist.get(j+3)
									);
															
									nearlist.add(ai); 							
									}							
										i=i+4;	
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
													
							if(nearlist.size()>0)
			                {//如果成功获取了经纬度取列表中的第一条								
								if(nearlist.size()==1)  
								{											                				                   			                   	
									Bundle bundle=new Bundle(); //初始化Bandle
									bundle.putString("jdStr", totallist.get(0));  //添加数据
									bundle.putString("wdStr", totallist.get(1));
									bundle.putString("msg",totallist.get(3)+"\n经度："+getjwdfromString(totallist.get(0))+"\n纬度："+getjwdfromString(totallist.get(1)));
									Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);//Intent
								    intent.putExtras(bundle);		//添加数据包			
								    TrainSystemHelperActivity.this.startActivity(intent);  //开启另一个Activity
								}
								else
								{								
									showDialog(NEAR_DISTANCE);      //Dialog显示
								}
										
			                }
							else            //没有查到地址的情况
							{
								Toast.makeText            //Toast信息显示
								(
										TrainSystemHelperActivity.this,      
									"对不起，在此范围内没有您要找的俱乐部！", //提示信息
									Toast.LENGTH_SHORT                 //短时间显示
								).show();
							}
							
							
							
						}
					}				      		
	        	}
	        );
		
		final LinearLayout l001=(LinearLayout)findViewById(R.id.linear01);
        final LinearLayout l002=(LinearLayout)findViewById(R.id.linear02);
        RadioButton rb1=(RadioButton)findViewById(R.id.radio01);
        rb1.setOnClickListener(
        	new OnClickListener()
        	{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					state=0;
					l001.setVisibility(1);
					l002.setVisibility(-1);
				}       		
        	}
        );
        
        RadioButton rb2=(RadioButton)findViewById(R.id.radio02);
        rb2.setOnClickListener(
        	new OnClickListener()
        	{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					state=1;
					l001.setVisibility(-1);
					l002.setVisibility(1);
				}       		
        	}
        );
        
        
        
    }
    
    @Override
    public Dialog onCreateDialog(int id)
    {    	
    	Dialog result=null;
    	switch(id)
    	{	    	
	    	case NEAR_DISTANCE:
	    		AlertDialog.Builder b2=new AlertDialog.Builder(this); 
				  b2.setItems(
					null, 
				    null
				   );
				  nearDialog=b2.create();       //创建Dialog
				  result=nearDialog;	    		
		    	break;
    	}
        return result;
    }
    
    //初始化线路列表
    public void initlinesSpinner()
    {
    	//初始化Spinner
        Spinner sp=(Spinner)this.findViewById(R.id.Spinner00);
        final List<String> linesList=DBUtil.searchLineList();//通过线路名得到站名
        
        //为Spinner准备内容适配器
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return linesList.size();//总共三个选项
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * 动态生成每个下拉项对应的View，每个下拉项View由一个TextView构成
				*/
				
				//初始化TextView
				TextView tv=new TextView(TrainSystemHelperActivity.this);
				tv.setText(linesList.get(arg0));//设置内容
				tv.setTextSize(18);//设置字体大小
				tv.setTextColor(Color.BLACK);//设置字体颜色				
				return tv;
			}        	
        };        
        sp.setAdapter(ba);//为Spinner设置内容适配器
        //设置选项选中的监听器
        sp.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//重写选项被选中事件的处理方法				
				TextView tvn=(TextView)arg1;//获取其中的TextView 
				currLine=tvn.getText().toString();
		        initstationSpinner(currLine);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }        	   
           }
        );
}
    public void initstationSpinner(String L_name)
    {
    	Spinner sp=(Spinner)this.findViewById(R.id.Spinner01);
    	final List<String> stationList=DBUtil.searchStationList(L_name);
    	 //为Spinner准备内容适配器
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return stationList.size();//总共三个选项
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * 动态生成每个下拉项对应的View，每个下拉项View由一个TextView构成
				*/
				
				//初始化TextView
				TextView tv=new TextView(TrainSystemHelperActivity.this);
				tv.setText(stationList.get(arg0));//设置内容
				tv.setTextSize(18);//设置字体大小
				tv.setTextColor(Color.BLACK);//设置字体颜色				
				return tv;
			}        	
        }; 
        sp.setAdapter(ba);//为Spinner设置内容适配器
        sp.setOnItemSelectedListener(
                new OnItemSelectedListener()
                {
     			@Override
     			public void onItemSelected(AdapterView<?> arg0, View arg1,
     					int arg2, long arg3) {//重写选项被选中事件的处理方法				
     				TextView tvn=(TextView)arg1;//获取其中的TextView 
     				if(tvn!=null)
     				{
     					currStation=tvn.getText().toString();
     				}
     			}

     			@Override
     			public void onNothingSelected(AdapterView<?> arg0) { }        	   
                }
             );  
    }
    
    @Override
    public void onPrepareDialog(int id, Dialog dialog)//每次弹出对话框时被回调以动态更新对话框内容的方法
    {
    	//若不是历史对话框则返回
    	if(id!=NEAR_DISTANCE)return;
	   	
	   	//对话框对应的总垂直方向LinearLayout
	   	LinearLayout ll=new LinearLayout(TrainSystemHelperActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);		//设置朝向	
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setBackgroundResource(R.drawable.dialog);
		
		//标题行的水平LinearLayout
		LinearLayout lln=new LinearLayout(TrainSystemHelperActivity.this);
		lln.setOrientation(LinearLayout.HORIZONTAL);		//设置朝向	
		lln.setGravity(Gravity.LEFT);
		lln.setLayoutParams(new ViewGroup.LayoutParams(280, LayoutParams.WRAP_CONTENT));
		
		//标题行的图标
		ImageView iv=new ImageView(TrainSystemHelperActivity.this);
		iv.setImageResource(R.drawable.history);
		iv.setLayoutParams(new ViewGroup.LayoutParams(24, 24));
		lln.addView(iv);
		
		//标题行的文字
		TextView tvTitle=new TextView(TrainSystemHelperActivity.this);
		tvTitle.setText("请选择地点");
		tvTitle.setTextSize(20);//设置字体大小
		tvTitle.setTextColor(Color.WHITE);//设置字体颜色
		lln.addView(tvTitle);
		
		//将标题行添加到总LinearLayout
		ll.addView(lln);		
	   	
	   	//为对话框中的历史记录条目创建ListView
	    //初始化ListView
        ListView lv=new ListView(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);     
	   	if(id==NEAR_DISTANCE) 
	   	{
	   	//为ListView准备内容适配器
	        BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return nearlist.size();//总共几个选项
				}

				@Override
				public Object getItem(int arg0) { return null; }

				@Override
				public long getItemId(int arg0) { return 0; }

				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {
					//动态生成每条历史记录对应的TextView
					TextView tv=new TextView(TrainSystemHelperActivity.this);
					tv.setGravity(Gravity.LEFT);
					tv.setText(nearlist.get(arg0).listStr);//设置内容
					tv.setTextSize(20);//设置字体大小
					tv.setTextColor(Color.WHITE);//设置字体颜色
					tv.setPadding(0,0,0,0);//设置四周留白				
					return tv;
				}        	
	        };       
	        lv.setAdapter(ba);//为ListView设置内容适配器
	        
	        //设置选项被单击的监听器
	        lv.setOnItemClickListener(
	           new OnItemClickListener()
	           {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {//重写选项被单击事件的处理方法
					//获取历史记录中当前选中的TextView 
					Address ai=nearlist.get(arg2);
					Bundle bundle=new Bundle();
					bundle.putString("jdStr", ai.jdStr);
					bundle.putString("wdStr", ai.wdStr);
					bundle.putString("msg",ai.msgStr);
					Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);
				    intent.putExtras(bundle);					
				    TrainSystemHelperActivity.this.startActivity(intent);				
					nearDialog.cancel();
				}        	   
	           }
	        );             
	        //将历史记录条的ListView加入总LinearLayout
	        ll.addView(lv);
	        dialog.setContentView(ll);
	   	}
	   		
    } 
    
    public static double calculationdistance(double jA,double wA,String jd2,String wd2) 
	{
		double wB=Double.parseDouble(wd2.trim());		
		double jB=Double.parseDouble(jd2.trim());
		double AB=6371.004*Math.acos((Math.sin(wA*Math.PI/180))*(Math.sin(wB*Math.PI/180))+(Math.cos(wA*Math.PI/180))*(Math.cos(wB*Math.PI/180))*(Math.cos(jB*Math.PI/180-jA*Math.PI/180)));
		return AB;
	}
    
    public static String getjwdfromString(String num)
    {
		String result;
    	double nummiddle=Double.parseDouble(num);
		double nummiddle1=Math.round(nummiddle*1000)/1000.0;
		result=nummiddle1+"";
    	return result;
   	
    }
    
    protected boolean isRouteDisplayed() {
		return false;
	}
}