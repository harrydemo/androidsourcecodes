package com.bn.helper;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;

//用于绘制导航路线图的Overlay
class MyNavigateOverlay extends Overlay
{    
	
	static int DWidth=20;
	static int DHeight=29;
	
	GeoPoint[] points;//路线中节点经纬度数组
    Paint paint;//画笔

    double direction;//方向角    
    int StartIn;//当前子路径起点索引
    int step;//当前子路径步数索引
    int total;//当前子路径总步数
    GeoPoint gpCurr;//导航三角形当前位置
    
	public MyNavigateOverlay(GeoPoint[] points)
	{
		this.points=points;	
		paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿
		paint.setARGB(90,0,0,255);;//设置画笔颜色
		paint.setStrokeWidth(5);//设置路线绘制宽度
	}
    
	@Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//不接收处理触控事件
		return false;
	}
	
    @Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    	
    	//循环绘制导航路径
    	for(int i=0;i<points.length-1;i++)
    	{
    		//取出每条子路径的起点、终点
    		Point Start=getPoint(mapView,points[i]);
    		Point End=getPoint(mapView,points[i+1]);
    			
    		paint.setARGB(160,0,0,255);;//设置画笔颜色
    		canvas.drawLine(Start.x, Start.y, End.x, End.y, paint);
    	}
    	
    		
    	if(MapNavigateActivity.ison&total!=0)
    	{
    		
            GeoPoint gp=points[StartIn];
            
            GeoPoint gpNext=points[StartIn+1];
            
            gpCurr=new GeoPoint
            (
                  gp.getLatitudeE6()+(gpNext.getLatitudeE6()-gp.getLatitudeE6())*step/total,
                  gp.getLongitudeE6()+(gpNext.getLongitudeE6()-gp.getLongitudeE6())*step/total
            );
            
            Point pCurr=getPoint(mapView,gpCurr); 
            
           
            if(pCurr.x<Constant.BORDER_WIDTH||pCurr.x>Constant.MAP_VIEW_WIDTH-Constant.BORDER_WIDTH||
               pCurr.y<Constant.BORDER_WIDTH||pCurr.y>Constant.MAP_VIEW_HEIGHT-Constant.BORDER_WIDTH)
            {
               mapView.getController().setCenter(gpCurr);            	
            }
            
            //重绘地图            
            mapView.postInvalidate();
            //拿到导航三角形的XY坐标
			float dnX=pCurr.x;
			float dnY=pCurr.y;
			
    		Matrix m1=new Matrix();
    		m1.setRotate((float)direction,dnX,dnY);	  
    		Matrix m2=new Matrix();
    		m2.setTranslate(dnX-DWidth/2,dnY-DHeight/2);
    		Matrix mz=new Matrix();
    		mz.setConcat(m1, m2);    	
    		paint.setARGB(200,0,0,255);;//设置画笔颜色
    		canvas.drawBitmap(MapNavigateActivity.bitmapDirection, mz, paint);
    	}
    	
    	//调用父类绘制
    	super.draw(canvas, mapView, shadow);
	}
    
    public Point getPoint(MapView mapView,GeoPoint gp)
    {//将经纬度翻译成屏幕上的XY坐标
    	Projection projettion = mapView.getProjection();
		Point p = new Point();
		projettion.toPixels(gp, p); 
		return p;
    }
    
    //根据当前的节点计算导航三角形方向角 
    public void calDirection(int dIndex, MapView mapView)
    {
    	StartIn=dIndex;    	
    	Point dp=getPoint(mapView,points[dIndex]);   
		if(dIndex<points.length-1)
		{//没有到最后一个点需要计算方向
			Point dpNext=getPoint(mapView,points[dIndex+1]); 					
			float dx=dpNext.x-dp.x;
			float dy=-(dpNext.y-dp.y);

			int c=1;
			//若下一个点与此点重合，则再取下一个点
            while(dx==0&&dy==0)
            {
            	c++;
            	dpNext=getPoint(mapView,points[dIndex+c]); 
            	dx=dpNext.x-dp.x;
    			dy=-(dpNext.y-dp.y);
            }
            
			if(dx!=0||dy!=0)
			{
				if(dx>0&&dy>0)
    			{//第一象限
					direction=Math.toDegrees(Math.atan(dx/dy));
    			}
				else if(dx<0&&dy>0)
				{//第二象限
					direction=360-Math.toDegrees(Math.atan(-dx/dy));
				} 
				else if(dx<0&&dy<0)
				{//第三象限
					direction=180+Math.toDegrees(Math.atan(dx/dy));
				}
				else if(dx>0&&dy<0)
				{//第四象限
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
		}
		
    }
}


