package com.bn.helper;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;

import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;
import static com.bn.helper.Constant.*;

//用于绘制导航路线图的Overlay
class StationPicture extends Overlay
{    
	final static int picWidth=8;  //气球图的宽度
	final static int picHeight=8; //气球图的高度
	final static int picWidthbig=11;  //气球图的宽度
	final static int picHeightbig=11; //气球图的高度
	
	
    Paint paint;//画笔       
    public StationPicture()
    {   	   	
    	paint = new Paint();//创建画笔
    	paint.setAntiAlias(true);//打开抗锯齿
    	paint.setStrokeWidth(5);//设置路线绘制宽度    	
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//不接收处理触控事件
		return false;
	}

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
     
     //绘制站点的图标    
     Vector<String> listArr1=new Vector<String>();
     Vector<String> listArr11=new Vector<String>();
     Vector<String> listArr2=new Vector<String>();
     Vector<String> listArr22=new Vector<String>();
     Vector<String> listArr3=new Vector<String>();
     Vector<String> listArr33=new Vector<String>();
     if(TrainSystemHelperActivity.flag1==true||TrainSystemHelperActivity.str.equals("地铁1号线"))
     {
    	 listArr1=DBUtil.searchSinglejw("地铁1号线");
    	 listArr11=DBUtil.checktransforstop("地铁1号线");
     }
     if(TrainSystemHelperActivity.flag2==true||TrainSystemHelperActivity.str.equals("地铁2号线"))
     {
    	 listArr2=DBUtil.searchSinglejw("地铁2号线");
    	 listArr22=DBUtil.checktransforstop("地铁2号线");
     }
     if(TrainSystemHelperActivity.flag3==true||TrainSystemHelperActivity.str.equals("地铁13号线"))
     {
    	 listArr3=DBUtil.searchSinglejw("地铁13号线");
    	 listArr33=DBUtil.checktransforstop("地铁13号线");
     }
     
     Vector<GeoPoint> poin1=new Vector<GeoPoint>();
     poin1=MiddleTrainOverl.getPoint1(listArr1);
     Vector<GeoPoint> poin2=new Vector<GeoPoint>();
     poin2=MiddleTrainOverl.getPoint1(listArr2);
     Vector<GeoPoint> poin3=new Vector<GeoPoint>();
     poin3=MiddleTrainOverl.getPoint1(listArr3);
     
     //中转站
     Vector<GeoPoint> poin11=new Vector<GeoPoint>();
     poin11=MiddleTrainOverl.getPoint1(listArr11);
     Vector<GeoPoint> poin22=new Vector<GeoPoint>();
     poin22=MiddleTrainOverl.getPoint1(listArr22);
     Vector<GeoPoint> poin33=new Vector<GeoPoint>();
     poin33=MiddleTrainOverl.getPoint1(listArr33);
     
     for(int i=0;i<poin1.size();i=i+1)
     {
     //画站点    	
    	 	Point pStart=getPoint(mapView,poin1.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.smallstation, pStart.x-picWidth/2, pStart.y-picHeight/2, null);   	  	 
     }
     
     for(int i=0;i<poin2.size();i=i+1)
     {
     //画站点
    	 	Point pStart=getPoint(mapView,poin2.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.smallstation, pStart.x-picWidth/2, pStart.y-picHeight/2, null);   	  	 
     }
     for(int i=0;i<poin3.size();i=i+1)
     {
     //画站点
    	 	Point pStart=getPoint(mapView,poin3.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.smallstation, pStart.x-picWidth/2, pStart.y-picHeight/2, null);   	  	 
     }
     
     //画中转站
     for(int i=0;i<poin11.size();i=i+1)
     {
     //画站点    	
    	 	Point pStart=getPoint(mapView,poin11.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.station, pStart.x-picWidthbig/2, pStart.y-picHeightbig/2, null);   	  	 
     }
     
     for(int i=0;i<poin22.size();i=i+1)
     {
     //画站点
    	 	Point pStart=getPoint(mapView,poin22.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.station, pStart.x-picWidthbig/2, pStart.y-picHeightbig/2, null);   	  	 
     }
     for(int i=0;i<poin33.size();i=i+1)
     {
     //画站点
    	 	Point pStart=getPoint(mapView,poin33.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.station, pStart.x-picWidthbig/2, pStart.y-picHeightbig/2, null);   	  	 
     }
           
     //调用父类绘制
     super.draw(canvas, mapView, shadow);
     
}
    
    public static Point getPoint(MapView mapView,GeoPoint gp)
    {//将经纬度翻译成屏幕上的XY坐标
    	Projection projettion = mapView.getProjection();
    	Point p = new Point();
    	projettion.toPixels(gp, p); 
    	return p;
    }     
}



