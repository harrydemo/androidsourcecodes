package com.bn.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.bn.helper.MyBallonOverlay;
import static com.bn.helper.Constant.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

//覆盖整个地图捕捉触控事件的透明OverLay
class MyMapOverlay extends Overlay{
	boolean flagMove=false;//是否为移动动作标志位
	static int nState=0; //状态标志为 0表示开始状态 1表示已经设置起始点的状态
	static GeoPoint gpStart;//起始点经纬度
	static GeoPoint gpEnd;//结束点经纬度
	
	float previousX;//上次触控的X坐标
	float previousY;//上次触控的Y坐标
	
	static final int MOVE_THRESHOLD=10;//当触控点移动范围不大于阈值时还认为是点击
	static Bitmap bitmapStart;
	 @Override 
	    public boolean onTouchEvent(MotionEvent event, MapView mv) {
	        if(event.getAction() == MotionEvent.ACTION_MOVE)
	        {//若移动了触控笔则设置移动标志为true
	        	if(flagMove!=true)
	        	{
	        		float dx=Math.abs(event.getX()-previousX);
	        		float dy=Math.abs(event.getY()-previousY);	        		
	        		if(dx>MOVE_THRESHOLD||dy>MOVE_THRESHOLD)
	        		{
	        			flagMove=true;
	        		}
	        	}
	        }
	        else if(event.getAction() == MotionEvent.ACTION_DOWN)
	        {//若按下了触控笔则设置移动标志为false，并记录按下触控笔的位置
	        	flagMove=false;
	        	previousX=event.getX();
	        	previousY=event.getY();
	        }
	        else if (MyBallonOverlay.currentPIC==null&&
	        		 !flagMove&&
	        		 event.getAction() == MotionEvent.ACTION_UP ) 
	        {
	        	Vector<String> ve=new Vector<String>();
	        	ve=DBUtil.searchjwsn();	        	
	        	GeoPoint gp = mv.getProjection().fromPixels
	            (
	        		 (int) event.getX(),  //触控笔在屏幕上的X坐标
	        		 (int) event.getY()   //触控笔在屏幕上的Y坐标
	            );	            
	        	//显示点击处的经纬度
	            String latStr=Math.round(gp.getLatitudeE6()/1000.00)/1000.0+"";//纬度
	        	String longStr=Math.round(gp.getLongitudeE6()/1000.00)/1000.0+"";//经度
	        	
	        	
	        	for(int n=0;n<ve.size()-1;n=n+3)
	        	{
	        		if(TrainOverlay.calculationdistance(longStr, latStr, ve.get(n).toString(), ve.get(n+1).toString())<XIU_ZHENG)
	        		{
	        			double jj=Double.parseDouble(ve.get(n));  //转化数据
	        	        double ww=Double.parseDouble(ve.get(n+1));  //转化为数据
	        	        GeoPoint gpp = new GeoPoint
	        	        (
	        	        		(int)(ww*1E6),  //纬度
	        	        		(int)(jj*1E6)  //经度
	        	        );
	        			
	    	        	List<Overlay> overlays = mv.getOverlays(); 
	    	        	int i=0;
	    	        	for(Overlay ol:overlays)
	    	        	{//清除其他气球的showWindow标记
	    	        				i++;	    	        		
	    	        	} 
	    	        	if(i<6)
	    	        	{
	    	        		MyBallonOverlay mbo=new MyBallonOverlay
	    		        	(
	    		        			gpp,		//气球的坐标
	    		        			ve.get(n+2).toString()+"\n经度："+longStr+"\n纬度："+latStr, //气球的信息
	    		        			MapNavigateActivity.bitmapEnd
	    		        	);
	    	        		gpEnd=gp;
	    	        		mbo.showWindow=true;
	    		            overlays.add(mbo); 
	    	        	}
	    	        	if(i>=6)
	    	        	{	    	        		
	    	        	   	Overlay first1=overlays.get(0);
	    	        	   	Overlay first2=overlays.get(1);
	    	        	   	Overlay first3=overlays.get(2);
	    	        	   	Overlay first4=overlays.get(3);
	    	        	   	Overlay first5=overlays.get(4);
	    	        		List<Overlay> tol=new ArrayList<Overlay>();
	    	        		for(int j=5;j<overlays.size();j++)
	    	        		{
	    	        			tol.add(overlays.get(j));
	    	        		}
	    	        		//清空所有Overlay
	    	        		overlays.clear();
	    	        		//添加用于获取全局触控事件的透明Overlay
	    	        		overlays.add(first1);
	    	        		overlays.add(first2);
	    	        		overlays.add(first3);
	    	        		overlays.add(first4);
	    	        		overlays.add(first5);
	    	        		MyBallonOverlay mbo=new MyBallonOverlay
	    		        	(
	    		        			gpp,		//气球的坐标
	    		        			ve.get(n+2).toString()+"\n经度："+longStr+"\n纬度："+latStr, //气球的信息
	    		        			MapNavigateActivity.bitmapEnd
	    		        	);
	    	        		gpEnd=gpp;
	    	        		mbo.showWindow=true;
	    		            overlays.add(mbo); 
	    	        	}	        	
	        		}//"当前站点：\n"+ve.get(n+2).toString()'''"地理坐标为：\n经度："+longStr+"\n纬度："+latStr
	        	}	        		        	
	            return true;
	        }
	        return false;
	    }

}