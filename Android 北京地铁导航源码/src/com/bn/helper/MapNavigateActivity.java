package com.bn.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;
import com.bn.helper.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;


public class MapNavigateActivity extends MapActivity {
	
	static Bitmap bitmapStart;//�������ͼƬ	 
	static Bitmap bitmapEnd;//�յ�����ͼƬ
	static Bitmap bitmapDirection;//������ͷ����ͼƬ
	static Bitmap station;
	static Bitmap smallstation;
	static boolean inQuery=false;//���ڲ�ѯ�б�־λ
	static boolean ison=false;//�Ƿ���Ƶ�����ͷ��־
	
	static Handler hd;
	static String msg;
	
	MapController mc;//��ͼ������
	MyNavigateOverlay  currMyNa;//��ǰ�ĵ���Overlay 	
	GeoPoint gpLocation;
	boolean flag=false;//�Ƿ���ʾ���Լ�λ��
	
	boolean yyhh=false;//���������־λ
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //����Ϊ����
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //������Ϊ����ȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();        
        String jdStr=bundle.getString("jdStr");//"118.190088";
        String wdStr=bundle.getString("wdStr");//"39.637877";
        String msg=bundle.getString("msg");//"�������\n�й��ӱ�ʡ��ɽ��·�����Ļ�·";
        double jd=Double.parseDouble(jdStr);
        double wd=Double.parseDouble(wdStr);
        //Ŀ���̵�ľ�γ��
        GeoPoint gp = new GeoPoint
        (
        		(int)(wd*1E6), //γ��
        		(int)(jd*1E6) //����
        );
               
       
        new Thread()
        {
        	public void run()
        	{
        		Looper.prepare();
        		
        		hd=new Handler()
                {
                	@Override
                	public void handleMessage(Message msg)
                	{
                		super.handleMessage(msg);
                		
                		switch(msg.what)
                		{
                		   case Constant.DISPLAY_TOAST:
                			Toast.makeText
                       		(
                       			MapNavigateActivity.this, 
                       			MapNavigateActivity.msg, 
                       			Toast.LENGTH_SHORT
                       		).show();
                		   break;
                		}
                	}
                };        		        		
        		Looper.loop();
        	}
        }.start();
        //��ʼ���Զ�����Ϣ������������Ϣѭ���߳�============end===========
        
        
        setContentView(R.layout.map);
        //��ʼ������ͼƬ
        bitmapStart= BitmapFactory.decodeResource(this.getResources(), R.drawable.people);
        bitmapEnd= BitmapFactory.decodeResource(this.getResources(), R.drawable.cart);
        bitmapDirection=BitmapFactory.decodeResource(this.getResources(), R.drawable.carl);
        station=BitmapFactory.decodeResource(this.getResources(), R.drawable.tubiao);
        smallstation=BitmapFactory.decodeResource(this.getResources(), R.drawable.xiaotubiao);
        
        //�Ե�ͼ���г�ʼ��      
        final MapView mv=(MapView)findViewById(R.id.myMapView);
        mv.setBuiltInZoomControls(true);//���õ�ͼ��Ҫ���ſ�����
        mc=mv.getController();//��ȡ��ͼ������
        mc.setZoom(14);//���õ�ͼ���ű���        
        //���õ�ͼ���ĵ㾭γ��
        mc.animateTo(gp);
        
        //����ͼ���һ����ȫ͸����Overlay���ڲ�׽�����¼�
       MyMapOverlay myOverlay = new MyMapOverlay();
       List<Overlay> overlays = mv.getOverlays();
        overlays.add(myOverlay); 
        
////        //============================================begin====
        overlays = mv.getOverlays(); 
    	Overlay first=overlays.get(0);
    	overlays.clear();
    	overlays.add(first);
    	
////    	
        
        
    	 //�����ڻ�ȡȫ�ִ����¼���͸��Overlay�õ�
        MapView mv1=(MapView)findViewById(R.id.myMapView);
        List<Overlay> overlays1 = mv.getOverlays(); 
        Overlay first1=overlays1.get(0);
        //��������Overlay��¼�������б���
		List<Overlay> tol=new ArrayList<Overlay>();
		for(int i=1;i<overlays1.size();i++)
		{
			tol.add(overlays1.get(i));
		}
		//�������Overlay
		overlays1.clear();
		//������ڻ�ȡȫ�ִ����¼���͸��Overlay
		overlays1.add(first1);
        //============================================end=======
    	
         if(TrainSystemHelperActivity.flag2==true||TrainSystemHelperActivity.str.equals("����2����"))
         {
        	 Vector<String> list2=new Vector<String>();         
             list2=DBUtil.inserttotemp("����2����");               
             list2.add(list2.get(0));
             list2.add(list2.get(1));                
             Vector<GeoPoint> pointt=new Vector<GeoPoint>();
             pointt=MiddleTrainOverl.getPoint1(list2);
             TrainOverlay mno=new TrainOverlay(pointt,178,178,255,"����2����"); 
             overlays1.add(mno); 
         }
         else 
         {
        	 NothingOverlay mno=new NothingOverlay();
        	 overlays1.add(mno); 
         }
         
         if(TrainSystemHelperActivity.flag1==true||TrainSystemHelperActivity.str.equals("����1����"))
         {
        	 Vector<String> list3=new Vector<String>();         
             list3=DBUtil.inserttotemp("����1����");                  
             Vector<GeoPoint> pointt3=new Vector<GeoPoint>();
             pointt3=MiddleTrainOverl.getPoint1(list3);
             TrainOverlay mno3=new TrainOverlay(pointt3,250,0,0,"����1����");
             overlays1.add(mno3);
         }
         else 
         {
        	 NothingOverlay mno3=new NothingOverlay();
        	 overlays1.add(mno3);
         }
         
         if(TrainSystemHelperActivity.flag3==true||TrainSystemHelperActivity.str.equals("����13����"))
         {
        	 Vector<String> list4=new Vector<String>();         
             list4=DBUtil.inserttotemp("����13����");                  
             Vector<GeoPoint> pointt4=new Vector<GeoPoint>();
             pointt4=MiddleTrainOverl.getPoint1(list4);
             TrainOverlay mno4=new TrainOverlay(pointt4,255,128,0,"����13����");
             overlays1.add(mno4);
         }
         else 
         {
        	 NothingOverlay mno4=new NothingOverlay();
        	 overlays1.add(mno4);
         }
         
         
         //ͼƬ��overlay
         
        
       //����г�·��Overlay
		
		
		
		StationPicture mnopicture=new StationPicture();
		overlays1.add(mnopicture);
       //�������Overlay
		for(Overlay o:tol)
		{
			overlays1.add(o);  
       	}
       //����֮����ִ����ô�ද����Ϊ�˽��г�·��Overlay
       //�ŵ���㡢�յ�����Overlay���棬�����ڵ��Ӿ�Ч������
     //�ڵ��λ������������
      	MyBallonOverlay mbo=new MyBallonOverlay
       	(
       			gp,		//���������
       			msg,  //�������Ϣ
       			MapNavigateActivity.bitmapEnd
       	);               
      		mbo.showWindow=true;
           overlays.add(mbo);                 
           //��¼���λ��
           MyMapOverlay.gpEnd=gp;
       
       //�л�״̬����ʼ״̬
       MapController mc=mv1.getController();//��ȡ��ͼ������
                                            
        //����һ���߳���ʱ��ȡMapView�ĳߴ�
        new Thread()
        {
        	public void run()
        	{
        		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		Constant.MAP_VIEW_WIDTH=mv.getWidth();
                Constant.MAP_VIEW_HEIGHT=mv.getHeight();
        	}
        }.start(); 
        
    ////��ȡλ�ù�����ʵ��
        String serviceName=Context.LOCATION_SERVICE;
        LocationManager lm=(LocationManager)this.getSystemService(serviceName);        
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
            gpLocation = new GeoPoint
            (
            		(int)(tempLo.getLatitude()*1E6), //γ��
            		(int)(tempLo.getLongitude()*1E6) //����
            );
        }
        else
        {
        	Toast.makeText(this, "���GPS����ȷ��������������", Toast.LENGTH_LONG).show();
        }

        
       
        LocationListener ll=new LocationListener()
        {
			@Override
			public void onLocationChanged(Location location) 
			{
				gpLocation = new GeoPoint
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
        
  
        lm.requestLocationUpdates
        (
        		LocationManager.GPS_PROVIDER, 	//ʹ��GPS��λ
        		2000, 							//ʱ��ֱ��� ms
        		5, 								//����ֱ���m
        		ll								//λ�ñ仯������
        );
        Toast.makeText(this, "�밴�²˵������������·��ؼ����أ�", Toast.LENGTH_LONG).show();
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//�Բ˵����г�ʼ��
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem ok=menu.add(0,0,0,"����");
		//ok.setIcon(R.drawable.daohang); 
    	OnMenuItemClickListener lsn=new OnMenuItemClickListener()
    	{//ʵ�ֲ˵������¼������ӿ�
			@Override
			public boolean onMenuItemClick(MenuItem item) { 
				//�������Ѿ��ڲ����У��򷵻�
				if(MapNavigateActivity.ison==true)
				{
					Toast.makeText
					(
							MapNavigateActivity.this, 
							"���������в����ٴε�����\n��ȶ�����������ٴ�����������", 
							Toast.LENGTH_LONG
					).show();
					return false;
				}	
				
				if(flag==false) 
				{
					Toast.makeText
					(
							MapNavigateActivity.this, 
							"����ѡ��\"��ʾ�Լ�λ��\"�˵���\n��ͼ����ʾ�Լ���λ���ٵ�����", 
							Toast.LENGTH_LONG
					).show();
					return false;
				}
				
				//��ȡ��ͼ������Overlay���б�
				MapView mv=(MapView)findViewById(R.id.myMapView);
				List<Overlay> overlays = mv.getOverlays();
				for(Overlay o:overlays)
				{
					if(o instanceof MyNavigateOverlay)
					{						
						//�õ�����Overlay������
						currMyNa=(MyNavigateOverlay)o;	
						yyhh=true;
						new Thread()
						{
							public void run()
							{
								//��ȡMapView
								MapView mv=(MapView)findViewById(R.id.myMapView);
								GeoPoint[] points=currMyNa.points;								
								for(int i=0;i<points.length-1;i++)
								{	
									if(yyhh==false)
									{										
										MapNavigateActivity.ison=false;
										break;
									}
									int dLat=points[i].getLatitudeE6()-points[i+1].getLatitudeE6();
									int dLon=points[i].getLongitudeE6()-points[i+1].getLongitudeE6();
									double distanceE6=Math.sqrt(dLat*dLat+dLon*dLon);
									int totalSteps=(int)(distanceE6/100);//100Ϊÿһ���ĳ��ȵ�λ
									//����ǰ��·���ܲ���Ϊ0�������
									if(totalSteps==0)
									{
										continue;
									}									
									//���㵱ǰ��·���ķ����	
									currMyNa.calDirection(i, mv);
									currMyNa.total=totalSteps;	
									currMyNa.step=0;																		
									if(i==0)
									{
										mv.getController().animateTo(points[i]);
										MapNavigateActivity.this.sleep(2000);										         
							            mv.postInvalidate();
										MapNavigateActivity.ison=true;
									}									
									//ѭ����·��ǰ��·���е�ÿһ��
									for(int s=0;s<totalSteps;s++)
									{
										currMyNa.step=s;										
										MapNavigateActivity.this.sleep(40);
									}										
								}
								//����������Ϻ�ָ��Ƿ���Ƶ�����ͷ��־λΪfalse
								MapNavigateActivity.ison=false;
							}
						}.start();
						break;
					}
				}				
				return true;
			}    		
    	};
    	ok.setOnMenuItemClickListener(lsn);//��ȷ���˵�����Ӽ����� 
    	
    	MenuItem dzjjwd=menu.add(0,0,0,"��ʾ�Լ�λ��");
    	
    	lsn=new OnMenuItemClickListener()
    	{//ʵ�ֲ˵������¼������ӿ�
			@Override
			public boolean onMenuItemClick(MenuItem item) {	 
				if(gpLocation==null)
				{
					Toast.makeText(MapNavigateActivity.this, "���GPS����ȷ��������������", Toast.LENGTH_LONG).show();
					return true;
				}
				
				if(MapNavigateActivity.ison==true)
				{
					yyhh=false;
				}	
				
				MyMapOverlay.gpStart=gpLocation;
				MapView mv=(MapView)findViewById(R.id.myMapView);
				List<Overlay> overlays = mv.getOverlays();    
				
				Overlay ovfd=null;
				for(Overlay ov:overlays)
				{
					if(ov instanceof MyNavigateOverlay)
					{
						ovfd=ov;
					}
				}
				overlays.remove(ovfd);
				
				
            	//�ڵ��λ������������
            	MyBallonOverlay mbo=new MyBallonOverlay
            	(
            			MyMapOverlay.gpStart,		//���������
            			"����λ��",  //�������Ϣ
            			MapNavigateActivity.bitmapEnd
            	);  
            	mbo.showWindow=true;
                overlays.add(mbo);                 
                //�л�״̬��������ú�״̬
                flag=true;
                
                //�����ڲ�ѯ�б�־λ����Ϊtrue��׼����ʼ��ѯ
                MapNavigateActivity.inQuery=true;
                //����һ���̴߳������ϲ�ѯ����㵽�յ���г�·��
                new Thread()
                {
                	public void run()
                	{                		               		
                		//�������ϻ�ȡ��㵽�յ��г�·����Ϣ��XML�ĵ�
                		Document doc=NavigateUtil.getPointsroute(MyMapOverlay.gpStart,MyMapOverlay.gpEnd);
                		if(doc==null)
                		{//���ĵ���ȡʧ�ܣ�����ʾ������ʾ
                			MapNavigateActivity.msg="������ϣ�������ѡ����㡢�յ����ԣ�";
                			MapNavigateActivity.hd.sendEmptyMessage(Constant.DISPLAY_TOAST);
                    		MapNavigateActivity.inQuery=false;  
                    		return;
                		}
                		//ͨ������XML�ĵ��õ��г�·���еĽڵ�����
                        GeoPoint[] points=NavigateUtil.getRoutePoints(doc);
                        if(points==null)
                		{//���ĵ�����ʧ�ܣ�����ʾ������ʾ
                        	MapNavigateActivity.msg="������ϣ�������ѡ����㡢�յ����ԣ�";
                			MapNavigateActivity.hd.sendEmptyMessage(Constant.DISPLAY_TOAST);
                    		MapNavigateActivity.inQuery=false; 
                    		return;
                		}
                        //�����г�·��Overlay
                        MyNavigateOverlay mno=new MyNavigateOverlay(points); 
                        //�����ڻ�ȡȫ�ִ����¼���͸��Overlay�õ�
                        MapView mv=(MapView)findViewById(R.id.myMapView);
                        List<Overlay> overlays = mv.getOverlays(); 
                        Overlay first=overlays.get(0);
                        Overlay second=overlays.get(1);
                        Overlay third=overlays.get(2);
                        Overlay four=overlays.get(3);
                        Overlay five=overlays.get(4);
                        //��������Overlay��¼�������б���
                		List<Overlay> tol=new ArrayList<Overlay>();
                		for(int i=5;i<overlays.size();i++)
                		{
                			tol.add(overlays.get(i));
                		}
                		//�������Overlay
                		overlays.clear();
                		//������ڻ�ȡȫ�ִ����¼���͸��Overlay
                		overlays.add(first);
                		overlays.add(second);
                		overlays.add(third);
                		overlays.add(four);
                		overlays.add(five);
                        //����г�·��Overlay
                        overlays.add(mno); 
                        //�������Overlay
                        for(Overlay o:tol)
                        {
                        	overlays.add(o);  
                        }
                        //����֮����ִ����ô�ද����Ϊ�˽��г�·��Overlay
                        //�ŵ���㡢�յ�����Overlay���棬�����ڵ��Ӿ�Ч������
                        
                        //�л�״̬����ʼ״̬
                        MapController mc=mv.getController();//��ȡ��ͼ������
                        mc.animateTo(MyMapOverlay.gpStart);//����ͼ���ĵ��Ƶ�·�����λ��
                        //�����ڲ�ѯ�б�־λ����Ϊfalse
                        MapNavigateActivity.inQuery=false;
                	}
                }.start();                  
                
				return true;
			}    		
    	};
    	dzjjwd.setOnMenuItemClickListener(lsn);//��ȷ���˵�����Ӽ����� 
		
		return true;		
	}
	
	//��Ϣһ���������ķ���
	public void sleep(int ms)
	{
		try 
		{
			Thread.sleep(ms);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
    @Override
    public void onPause()
    {
    	super.onPause();
		inQuery=false;//���ڲ�ѯ�б�־λ
		ison=false;//�Ƿ���Ƶ�����ͷ��־				
		flag=false;//�Ƿ���ʾ���Լ�λ��			
		yyhh=false;//���������־λ
    }
}