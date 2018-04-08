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
	  int state=0;//state=0ʱ�б�ѡ��state==1ʱ���·����ѯ
	  static boolean flag1=true,flag2=true,flag3=true;
	  static String str;
	  GeoPoint gpLocation1;
	  ArrayList<Address> nearlist=new ArrayList<Address>();
	  static final int NEAR_DISTANCE=1;
	  Dialog nearDialog;
	  SeekBar sb;
	  
	  Handler hd=new Handler() //������Ϣ������ת
	    {
		   @Override
		  public void handleMessage(Message msg)//��д����  
		  {
			 switch(msg.what)
			 {
			     case 0:
			    	 gotoMainView();  //������
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
        
        //����Ϊ����
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //������Ϊ����ȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		gotoSurfaceView();
		
		
    }
    
  //��ӭ����
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
							bundle.putString("msg",currStation+"\n���ȣ�"+getjwdfromString(jwd[0])+"\nγ�ȣ�"+getjwdfromString(jwd[1]));
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
							//��λ�Լ���λ��
						    ////��ȡλ�ù�����ʵ��
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
					            		(int)(tempLo.getLatitude()*1E6), //γ��
					            		(int)(tempLo.getLongitude()*1E6) //����
					            );
					        }
					        else
					        {
					        	Toast.makeText(TrainSystemHelperActivity.this, "���GPS����ȷ��������������", Toast.LENGTH_LONG).show();
					        }				        
					        //λ�ñ仯������
					        LocationListener ll=new LocationListener()
					        {
								@Override
								public void onLocationChanged(Location location) 
								{//��λ�ñ仯ʱ����
									gpLocation1 = new GeoPoint
						            (
						            		(int)(location.getLatitude()*1E6), //γ��
						            		(int)(location.getLongitude()*1E6) //����
						            );	
								}

								@Override
								public void onProviderDisabled(String provider) 
								{//Location Provider������ʱ����				
									
								}

								@Override
								public void onProviderEnabled(String provider) 
								{//Location Provider������ʱ����					
									
								}

								@Override
								public void onStatusChanged(String provider, int status,
										Bundle extras) 
								{
									//��ProviderӲ��״̬�仯�Ǹ���
									
								}      	
					        };
					        
					        //���λ�ñ仯������
					        lm.requestLocationUpdates
					        (
					        		LocationManager.GPS_PROVIDER, 	//ʹ��GPS��λ
					        		2000, 							//ʱ��ֱ��� ms
					        		5, 								//����ֱ���m
					        		ll								//λ�ñ仯������
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
											totallist.get(j),    //����
											totallist.get(j+1),     //γ��
											totallist.get(j+3)+"\n���ȣ�"+getjwdfromString(totallist.get(j))+"\nγ�ȣ�"+getjwdfromString(totallist.get(j+1)),
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
			                {//����ɹ���ȡ�˾�γ��ȡ�б��еĵ�һ��								
								if(nearlist.size()==1)  
								{											                				                   			                   	
									Bundle bundle=new Bundle(); //��ʼ��Bandle
									bundle.putString("jdStr", totallist.get(0));  //�������
									bundle.putString("wdStr", totallist.get(1));
									bundle.putString("msg",totallist.get(3)+"\n���ȣ�"+getjwdfromString(totallist.get(0))+"\nγ�ȣ�"+getjwdfromString(totallist.get(1)));
									Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);//Intent
								    intent.putExtras(bundle);		//������ݰ�			
								    TrainSystemHelperActivity.this.startActivity(intent);  //������һ��Activity
								}
								else
								{								
									showDialog(NEAR_DISTANCE);      //Dialog��ʾ
								}
										
			                }
							else            //û�в鵽��ַ�����
							{
								Toast.makeText            //Toast��Ϣ��ʾ
								(
										TrainSystemHelperActivity.this,      
									"�Բ����ڴ˷�Χ��û����Ҫ�ҵľ��ֲ���", //��ʾ��Ϣ
									Toast.LENGTH_SHORT                 //��ʱ����ʾ
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
				  nearDialog=b2.create();       //����Dialog
				  result=nearDialog;	    		
		    	break;
    	}
        return result;
    }
    
    //��ʼ����·�б�
    public void initlinesSpinner()
    {
    	//��ʼ��Spinner
        Spinner sp=(Spinner)this.findViewById(R.id.Spinner00);
        final List<String> linesList=DBUtil.searchLineList();//ͨ����·���õ�վ��
        
        //ΪSpinner׼������������
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return linesList.size();//�ܹ�����ѡ��
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * ��̬����ÿ���������Ӧ��View��ÿ��������View��һ��TextView����
				*/
				
				//��ʼ��TextView
				TextView tv=new TextView(TrainSystemHelperActivity.this);
				tv.setText(linesList.get(arg0));//��������
				tv.setTextSize(18);//���������С
				tv.setTextColor(Color.BLACK);//����������ɫ				
				return tv;
			}        	
        };        
        sp.setAdapter(ba);//ΪSpinner��������������
        //����ѡ��ѡ�еļ�����
        sp.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//��дѡ�ѡ���¼��Ĵ�����				
				TextView tvn=(TextView)arg1;//��ȡ���е�TextView 
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
    	 //ΪSpinner׼������������
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return stationList.size();//�ܹ�����ѡ��
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * ��̬����ÿ���������Ӧ��View��ÿ��������View��һ��TextView����
				*/
				
				//��ʼ��TextView
				TextView tv=new TextView(TrainSystemHelperActivity.this);
				tv.setText(stationList.get(arg0));//��������
				tv.setTextSize(18);//���������С
				tv.setTextColor(Color.BLACK);//����������ɫ				
				return tv;
			}        	
        }; 
        sp.setAdapter(ba);//ΪSpinner��������������
        sp.setOnItemSelectedListener(
                new OnItemSelectedListener()
                {
     			@Override
     			public void onItemSelected(AdapterView<?> arg0, View arg1,
     					int arg2, long arg3) {//��дѡ�ѡ���¼��Ĵ�����				
     				TextView tvn=(TextView)arg1;//��ȡ���е�TextView 
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
    public void onPrepareDialog(int id, Dialog dialog)//ÿ�ε����Ի���ʱ���ص��Զ�̬���¶Ի������ݵķ���
    {
    	//��������ʷ�Ի����򷵻�
    	if(id!=NEAR_DISTANCE)return;
	   	
	   	//�Ի����Ӧ���ܴ�ֱ����LinearLayout
	   	LinearLayout ll=new LinearLayout(TrainSystemHelperActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);		//���ó���	
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setBackgroundResource(R.drawable.dialog);
		
		//�����е�ˮƽLinearLayout
		LinearLayout lln=new LinearLayout(TrainSystemHelperActivity.this);
		lln.setOrientation(LinearLayout.HORIZONTAL);		//���ó���	
		lln.setGravity(Gravity.LEFT);
		lln.setLayoutParams(new ViewGroup.LayoutParams(280, LayoutParams.WRAP_CONTENT));
		
		//�����е�ͼ��
		ImageView iv=new ImageView(TrainSystemHelperActivity.this);
		iv.setImageResource(R.drawable.history);
		iv.setLayoutParams(new ViewGroup.LayoutParams(24, 24));
		lln.addView(iv);
		
		//�����е�����
		TextView tvTitle=new TextView(TrainSystemHelperActivity.this);
		tvTitle.setText("��ѡ��ص�");
		tvTitle.setTextSize(20);//���������С
		tvTitle.setTextColor(Color.WHITE);//����������ɫ
		lln.addView(tvTitle);
		
		//����������ӵ���LinearLayout
		ll.addView(lln);		
	   	
	   	//Ϊ�Ի����е���ʷ��¼��Ŀ����ListView
	    //��ʼ��ListView
        ListView lv=new ListView(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);     
	   	if(id==NEAR_DISTANCE) 
	   	{
	   	//ΪListView׼������������
	        BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return nearlist.size();//�ܹ�����ѡ��
				}

				@Override
				public Object getItem(int arg0) { return null; }

				@Override
				public long getItemId(int arg0) { return 0; }

				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {
					//��̬����ÿ����ʷ��¼��Ӧ��TextView
					TextView tv=new TextView(TrainSystemHelperActivity.this);
					tv.setGravity(Gravity.LEFT);
					tv.setText(nearlist.get(arg0).listStr);//��������
					tv.setTextSize(20);//���������С
					tv.setTextColor(Color.WHITE);//����������ɫ
					tv.setPadding(0,0,0,0);//������������				
					return tv;
				}        	
	        };       
	        lv.setAdapter(ba);//ΪListView��������������
	        
	        //����ѡ������ļ�����
	        lv.setOnItemClickListener(
	           new OnItemClickListener()
	           {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {//��дѡ������¼��Ĵ�����
					//��ȡ��ʷ��¼�е�ǰѡ�е�TextView 
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
	        //����ʷ��¼����ListView������LinearLayout
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