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
	
	static Bitmap bitmapStart;//起点气球图片	 
	static Bitmap bitmapEnd;//终点气球图片
	static Bitmap bitmapDirection;//导航箭头气球图片
	static Bitmap station;
	static Bitmap smallstation;
	static boolean inQuery=false;//正在查询中标志位
	static boolean ison=false;//是否绘制导航箭头标志
	
	static Handler hd;
	static String msg;
	
	MapController mc;//地图控制器
	MyNavigateOverlay  currMyNa;//当前的导航Overlay 	
	GeoPoint gpLocation;
	boolean flag=false;//是否显示了自己位置
	
	boolean yyhh=false;//动画允许标志位
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //设置为横屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //下两句为设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();        
        String jdStr=bundle.getString("jdStr");//"118.190088";
        String wdStr=bundle.getString("wdStr");//"39.637877";
        String msg=bundle.getString("msg");//"华润万家\n中国河北省唐山市路南区文化路";
        double jd=Double.parseDouble(jdStr);
        double wd=Double.parseDouble(wdStr);
        //目标商店的经纬度
        GeoPoint gp = new GeoPoint
        (
        		(int)(wd*1E6), //纬度
        		(int)(jd*1E6) //经度
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
        //初始化自定义消息处理器及其消息循环线程============end===========
        
        
        setContentView(R.layout.map);
        //初始化气球图片
        bitmapStart= BitmapFactory.decodeResource(this.getResources(), R.drawable.people);
        bitmapEnd= BitmapFactory.decodeResource(this.getResources(), R.drawable.cart);
        bitmapDirection=BitmapFactory.decodeResource(this.getResources(), R.drawable.carl);
        station=BitmapFactory.decodeResource(this.getResources(), R.drawable.tubiao);
        smallstation=BitmapFactory.decodeResource(this.getResources(), R.drawable.xiaotubiao);
        
        //对地图进行初始化      
        final MapView mv=(MapView)findViewById(R.id.myMapView);
        mv.setBuiltInZoomControls(true);//设置地图上要缩放控制条
        mc=mv.getController();//获取地图控制器
        mc.setZoom(14);//设置地图缩放比例        
        //设置地图中心点经纬度
        mc.animateTo(gp);
        
        //给地图添加一个完全透明的Overlay用于捕捉触控事件
       MyMapOverlay myOverlay = new MyMapOverlay();
       List<Overlay> overlays = mv.getOverlays();
        overlays.add(myOverlay); 
        
////        //============================================begin====
        overlays = mv.getOverlays(); 
    	Overlay first=overlays.get(0);
    	overlays.clear();
    	overlays.add(first);
    	
////    	
        
        
    	 //将用于获取全局触控事件的透明Overlay拿到
        MapView mv1=(MapView)findViewById(R.id.myMapView);
        List<Overlay> overlays1 = mv.getOverlays(); 
        Overlay first1=overlays1.get(0);
        //将其他的Overlay记录到辅助列表中
		List<Overlay> tol=new ArrayList<Overlay>();
		for(int i=1;i<overlays1.size();i++)
		{
			tol.add(overlays1.get(i));
		}
		//清空所有Overlay
		overlays1.clear();
		//添加用于获取全局触控事件的透明Overlay
		overlays1.add(first1);
        //============================================end=======
    	
         if(TrainSystemHelperActivity.flag2==true||TrainSystemHelperActivity.str.equals("地铁2号线"))
         {
        	 Vector<String> list2=new Vector<String>();         
             list2=DBUtil.inserttotemp("地铁2号线");               
             list2.add(list2.get(0));
             list2.add(list2.get(1));                
             Vector<GeoPoint> pointt=new Vector<GeoPoint>();
             pointt=MiddleTrainOverl.getPoint1(list2);
             TrainOverlay mno=new TrainOverlay(pointt,178,178,255,"地铁2号线"); 
             overlays1.add(mno); 
         }
         else 
         {
        	 NothingOverlay mno=new NothingOverlay();
        	 overlays1.add(mno); 
         }
         
         if(TrainSystemHelperActivity.flag1==true||TrainSystemHelperActivity.str.equals("地铁1号线"))
         {
        	 Vector<String> list3=new Vector<String>();         
             list3=DBUtil.inserttotemp("地铁1号线");                  
             Vector<GeoPoint> pointt3=new Vector<GeoPoint>();
             pointt3=MiddleTrainOverl.getPoint1(list3);
             TrainOverlay mno3=new TrainOverlay(pointt3,250,0,0,"地铁1号线");
             overlays1.add(mno3);
         }
         else 
         {
        	 NothingOverlay mno3=new NothingOverlay();
        	 overlays1.add(mno3);
         }
         
         if(TrainSystemHelperActivity.flag3==true||TrainSystemHelperActivity.str.equals("地铁13号线"))
         {
        	 Vector<String> list4=new Vector<String>();         
             list4=DBUtil.inserttotemp("地铁13号线");                  
             Vector<GeoPoint> pointt4=new Vector<GeoPoint>();
             pointt4=MiddleTrainOverl.getPoint1(list4);
             TrainOverlay mno4=new TrainOverlay(pointt4,255,128,0,"地铁13号线");
             overlays1.add(mno4);
         }
         else 
         {
        	 NothingOverlay mno4=new NothingOverlay();
        	 overlays1.add(mno4);
         }
         
         
         //图片的overlay
         
        
       //添加行车路线Overlay
		
		
		
		StationPicture mnopicture=new StationPicture();
		overlays1.add(mnopicture);
       //添加其他Overlay
		for(Overlay o:tol)
		{
			overlays1.add(o);  
       	}
       //上述之所以执行那么多动作是为了将行车路线Overlay
       //放到起点、终点气球Overlay下面，否则遮挡视觉效果不对
     //在点击位置添加起点气球
      	MyBallonOverlay mbo=new MyBallonOverlay
       	(
       			gp,		//气球的坐标
       			msg,  //气球的信息
       			MapNavigateActivity.bitmapEnd
       	);               
      		mbo.showWindow=true;
           overlays.add(mbo);                 
           //记录起点位置
           MyMapOverlay.gpEnd=gp;
       
       //切换状态到初始状态
       MapController mc=mv1.getController();//获取地图控制器
                                            
        //开启一个线程延时获取MapView的尺寸
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
        
    ////获取位置管理器实例
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
            		(int)(tempLo.getLatitude()*1E6), //纬度
            		(int)(tempLo.getLongitude()*1E6) //经度
            );
        }
        else
        {
        	Toast.makeText(this, "请打开GPS，并确保其正常工作！", Toast.LENGTH_LONG).show();
        }

        
       
        LocationListener ll=new LocationListener()
        {
			@Override
			public void onLocationChanged(Location location) 
			{
				gpLocation = new GeoPoint
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
        
  
        lm.requestLocationUpdates
        (
        		LocationManager.GPS_PROVIDER, 	//使用GPS定位
        		2000, 							//时间分辨率 ms
        		5, 								//距离分辨率m
        		ll								//位置变化监听器
        );
        Toast.makeText(this, "请按下菜单键操作，按下返回键返回！", Toast.LENGTH_LONG).show();
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//对菜单进行初始化
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem ok=menu.add(0,0,0,"导航");
		//ok.setIcon(R.drawable.daohang); 
    	OnMenuItemClickListener lsn=new OnMenuItemClickListener()
    	{//实现菜单项点击事件监听接口
			@Override
			public boolean onMenuItemClick(MenuItem item) { 
				//若动画已经在播放中，则返回
				if(MapNavigateActivity.ison==true)
				{
					Toast.makeText
					(
							MapNavigateActivity.this, 
							"导航进行中不能再次导航，\n请等动画播放完毕再次启动导航！", 
							Toast.LENGTH_LONG
					).show();
					return false;
				}	
				
				if(flag==false) 
				{
					Toast.makeText
					(
							MapNavigateActivity.this, 
							"请先选择\"显示自己位置\"菜单项\n地图中显示自己的位置再导航！", 
							Toast.LENGTH_LONG
					).show();
					return false;
				}
				
				//获取地图中所有Overlay的列表
				MapView mv=(MapView)findViewById(R.id.myMapView);
				List<Overlay> overlays = mv.getOverlays();
				for(Overlay o:overlays)
				{
					if(o instanceof MyNavigateOverlay)
					{						
						//拿到导航Overlay的引用
						currMyNa=(MyNavigateOverlay)o;	
						yyhh=true;
						new Thread()
						{
							public void run()
							{
								//获取MapView
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
									int totalSteps=(int)(distanceE6/100);//100为每一步的长度单位
									//若当前子路径总步数为0，则忽略
									if(totalSteps==0)
									{
										continue;
									}									
									//计算当前子路径的方向角	
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
									//循环走路当前子路径中的每一步
									for(int s=0;s<totalSteps;s++)
									{
										currMyNa.step=s;										
										MapNavigateActivity.this.sleep(40);
									}										
								}
								//导航动画完毕后恢复是否绘制导航箭头标志位为false
								MapNavigateActivity.ison=false;
							}
						}.start();
						break;
					}
				}				
				return true;
			}    		
    	};
    	ok.setOnMenuItemClickListener(lsn);//给确定菜单项添加监听器 
    	
    	MenuItem dzjjwd=menu.add(0,0,0,"显示自己位置");
    	
    	lsn=new OnMenuItemClickListener()
    	{//实现菜单项点击事件监听接口
			@Override
			public boolean onMenuItemClick(MenuItem item) {	 
				if(gpLocation==null)
				{
					Toast.makeText(MapNavigateActivity.this, "请打开GPS，并确保其正常工作！", Toast.LENGTH_LONG).show();
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
				
				
            	//在点击位置添加起点气球
            	MyBallonOverlay mbo=new MyBallonOverlay
            	(
            			MyMapOverlay.gpStart,		//气球的坐标
            			"您的位置",  //气球的信息
            			MapNavigateActivity.bitmapEnd
            	);  
            	mbo.showWindow=true;
                overlays.add(mbo);                 
                //切换状态到起点设置好状态
                flag=true;
                
                //将正在查询中标志位设置为true，准备开始查询
                MapNavigateActivity.inQuery=true;
                //开启一个线程从网络上查询从起点到终点的行车路径
                new Thread()
                {
                	public void run()
                	{                		               		
                		//从网络上获取起点到终点行车路径信息的XML文档
                		Document doc=NavigateUtil.getPointsroute(MyMapOverlay.gpStart,MyMapOverlay.gpEnd);
                		if(doc==null)
                		{//若文档获取失败，则显示错误提示
                			MapNavigateActivity.msg="网络故障，请重新选择起点、终点再试！";
                			MapNavigateActivity.hd.sendEmptyMessage(Constant.DISPLAY_TOAST);
                    		MapNavigateActivity.inQuery=false;  
                    		return;
                		}
                		//通过解析XML文档得到行车路径中的节点数组
                        GeoPoint[] points=NavigateUtil.getRoutePoints(doc);
                        if(points==null)
                		{//若文档分析失败，则显示错误提示
                        	MapNavigateActivity.msg="网络故障，请重新选择起点、终点再试！";
                			MapNavigateActivity.hd.sendEmptyMessage(Constant.DISPLAY_TOAST);
                    		MapNavigateActivity.inQuery=false; 
                    		return;
                		}
                        //创建行车路线Overlay
                        MyNavigateOverlay mno=new MyNavigateOverlay(points); 
                        //将用于获取全局触控事件的透明Overlay拿到
                        MapView mv=(MapView)findViewById(R.id.myMapView);
                        List<Overlay> overlays = mv.getOverlays(); 
                        Overlay first=overlays.get(0);
                        Overlay second=overlays.get(1);
                        Overlay third=overlays.get(2);
                        Overlay four=overlays.get(3);
                        Overlay five=overlays.get(4);
                        //将其他的Overlay记录到辅助列表中
                		List<Overlay> tol=new ArrayList<Overlay>();
                		for(int i=5;i<overlays.size();i++)
                		{
                			tol.add(overlays.get(i));
                		}
                		//清空所有Overlay
                		overlays.clear();
                		//添加用于获取全局触控事件的透明Overlay
                		overlays.add(first);
                		overlays.add(second);
                		overlays.add(third);
                		overlays.add(four);
                		overlays.add(five);
                        //添加行车路线Overlay
                        overlays.add(mno); 
                        //添加其他Overlay
                        for(Overlay o:tol)
                        {
                        	overlays.add(o);  
                        }
                        //上述之所以执行那么多动作是为了将行车路线Overlay
                        //放到起点、终点气球Overlay下面，否则遮挡视觉效果不对
                        
                        //切换状态到初始状态
                        MapController mc=mv.getController();//获取地图控制器
                        mc.animateTo(MyMapOverlay.gpStart);//将地图中心点移到路线起点位置
                        //将正在查询中标志位设置为false
                        MapNavigateActivity.inQuery=false;
                	}
                }.start();                  
                
				return true;
			}    		
    	};
    	dzjjwd.setOnMenuItemClickListener(lsn);//给确定菜单项添加监听器 
		
		return true;		
	}
	
	//休息一定毫秒数的方法
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
		inQuery=false;//正在查询中标志位
		ison=false;//是否绘制导航箭头标志				
		flag=false;//是否显示了自己位置			
		yyhh=false;//动画允许标志位
    }
}