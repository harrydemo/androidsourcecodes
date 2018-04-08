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
class TrainOverlay extends Overlay
{    
//导航箭头图片的尺寸
static int DWidth=15;
static int DHeight=17;
final static int picWidth=10;  //气球图的宽度
final static int picHeight=17; //气球图的高度

	Vector<GeoPoint> points=new Vector<GeoPoint>();//路线中节点经纬度数组
    Paint paint;//画笔    
    public String wdstr;
    int R,G,B;
    public static String L_name;
    
    
    public TrainOverlay(Vector<GeoPoint> points,int R,int G,int B,String L_name)
    {   	
    	this.points=points;
    	paint = new Paint();//创建画笔
    	paint.setAntiAlias(true);//打开抗锯齿
    	paint.setStrokeWidth(5);//设置路线绘制宽度
    	this.R=R;
    	this.G=G;
    	this.B=B;
    	this.L_name=L_name;
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//不接收处理触控事件
		return false;
	}

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
     
     //循环绘制导航路径
    // points=getPoint1();    
     for(int i=0;i<points.size()-1;i++)
     {
     //取出每条子路径的起点、终点
     Point pStart=getPoint(mapView,points.get(i));
     Point pEnd=getPoint(mapView,points.get(i+1));
     double direction1=calDirection1(pStart, pEnd);
     int middle=(int) Math.round( valueofline(pStart,pEnd)/SINGLE_LONG+0.5);
    
     //绘制此条导航子路径    
     
     for(int j=0;j<middle;j++)
     {    	
    	 float xStart=(float)(pStart.x+j*SINGLE_LONG*Math.sin(direction1*Math.PI/180.0));
    	 float yStart=(float)(pStart.y+j*SINGLE_LONG*Math.cos(direction1*Math.PI/180.0));
    	 float xEnd1=(float)(pStart.x+(j+1)*SINGLE_LONG*Math.sin(direction1*Math.PI/180.0));
    	 float yEnd1=(float)(pStart.y+(j+1)*SINGLE_LONG*Math.cos(direction1*Math.PI/180.0));    	
    	 
    	 if(j%2==0&&j!=middle-1)
    	 {
    		 paint.setARGB(255,R,G,B);;//设置画笔颜色
    		 canvas.drawLine(xStart, yStart, xEnd1, yEnd1, paint);
    		 paint.setARGB(255,255,255,255);;//设置画笔颜色
    	 }
    	 else if(j%2==1&&j!=middle-1)
    	 {
    		 paint.setARGB(255,255,255,255);;//设置画笔颜色
    		 canvas.drawLine(xStart, yStart, xEnd1, yEnd1, paint);
    		 paint.setARGB(255,R,G,B);;//设置画笔颜色
    	 }
    	 else if(j==middle-1)
    	 {
    		 //paint.setARGB(255,255,255,255);;//设置画笔颜色
    		 canvas.drawLine(xStart, yStart, pEnd.x, pEnd.y, paint);
    	 }    	 
 	 }
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
    
    //两个地点的经纬度的距离
    public static double calculationdistance(String jAA,String wAA,String jBB,String wBB) 
	{
    	double jA=Double.parseDouble(jAA.trim());
    	double wA=Double.parseDouble(wAA.trim());
    	double jB=Double.parseDouble(jBB.trim());
    	double wB=Double.parseDouble(wBB.trim());
    	double AB=6371.004*Math.acos((Math.sin(wA*Math.PI/180))*(Math.sin(wB*Math.PI/180))+(Math.cos(wA*Math.PI/180))*(Math.cos(wB*Math.PI/180))*(Math.cos(jB*Math.PI/180-jA*Math.PI/180)));
		return AB;
	}
    
    //两个站点在屏幕上的距离
    public static double valueofline(Point pStart,Point pEnd)
    {
		double result=0.0;
		result=Math.sqrt((pEnd.y-pStart.y)*(pEnd.y-pStart.y)+(pEnd.x-pStart.x)*(pEnd.x-pStart.x));
    	return result;
    	
    }
    
  //计算两个站点的方向
    public double calDirection1(Point start, Point end)
    {
    	double direction = 0;				
		float dx=end.x-start.x;
		float dy=(end.y-start.y);          
		if(dx!=0||dy!=0)
		{
				if(dx>0&&dy>0)
				{
				direction=Math.toDegrees(Math.atan(dx/dy));
    			}
				else if(dx<0&&dy>0)
				{
					direction=360-Math.toDegrees(Math.atan(-dx/dy));
				} 
				else if(dx<0&&dy<0)
				{
					direction=180+Math.toDegrees(Math.atan(dx/dy));
				}
				else if(dx>0&&dy<0)
				{
					direction=180-Math.toDegrees(Math.atan(dx/-dy));
				}	
				else if(dx==0)
				{
					if(dy>0)
					{
						direction=0;
					}
					else
					{
						direction=180;
					}
				}
				else if(dy==0)
				{
					if(dx>0)
					{
						direction=90;
					}
					else
					{
						direction=270;
					}
				}
			}		
		return direction;
		
    }
    
           
   
}



