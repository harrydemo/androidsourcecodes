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

//���ڻ��Ƶ���·��ͼ��Overlay
class TrainOverlay extends Overlay
{    
//������ͷͼƬ�ĳߴ�
static int DWidth=15;
static int DHeight=17;
final static int picWidth=10;  //����ͼ�Ŀ��
final static int picHeight=17; //����ͼ�ĸ߶�

	Vector<GeoPoint> points=new Vector<GeoPoint>();//·���нڵ㾭γ������
    Paint paint;//����    
    public String wdstr;
    int R,G,B;
    public static String L_name;
    
    
    public TrainOverlay(Vector<GeoPoint> points,int R,int G,int B,String L_name)
    {   	
    	this.points=points;
    	paint = new Paint();//��������
    	paint.setAntiAlias(true);//�򿪿����
    	paint.setStrokeWidth(5);//����·�߻��ƿ��
    	this.R=R;
    	this.G=G;
    	this.B=B;
    	this.L_name=L_name;
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//�����մ������¼�
		return false;
	}

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
     
     //ѭ�����Ƶ���·��
    // points=getPoint1();    
     for(int i=0;i<points.size()-1;i++)
     {
     //ȡ��ÿ����·������㡢�յ�
     Point pStart=getPoint(mapView,points.get(i));
     Point pEnd=getPoint(mapView,points.get(i+1));
     double direction1=calDirection1(pStart, pEnd);
     int middle=(int) Math.round( valueofline(pStart,pEnd)/SINGLE_LONG+0.5);
    
     //���ƴ���������·��    
     
     for(int j=0;j<middle;j++)
     {    	
    	 float xStart=(float)(pStart.x+j*SINGLE_LONG*Math.sin(direction1*Math.PI/180.0));
    	 float yStart=(float)(pStart.y+j*SINGLE_LONG*Math.cos(direction1*Math.PI/180.0));
    	 float xEnd1=(float)(pStart.x+(j+1)*SINGLE_LONG*Math.sin(direction1*Math.PI/180.0));
    	 float yEnd1=(float)(pStart.y+(j+1)*SINGLE_LONG*Math.cos(direction1*Math.PI/180.0));    	
    	 
    	 if(j%2==0&&j!=middle-1)
    	 {
    		 paint.setARGB(255,R,G,B);;//���û�����ɫ
    		 canvas.drawLine(xStart, yStart, xEnd1, yEnd1, paint);
    		 paint.setARGB(255,255,255,255);;//���û�����ɫ
    	 }
    	 else if(j%2==1&&j!=middle-1)
    	 {
    		 paint.setARGB(255,255,255,255);;//���û�����ɫ
    		 canvas.drawLine(xStart, yStart, xEnd1, yEnd1, paint);
    		 paint.setARGB(255,R,G,B);;//���û�����ɫ
    	 }
    	 else if(j==middle-1)
    	 {
    		 //paint.setARGB(255,255,255,255);;//���û�����ɫ
    		 canvas.drawLine(xStart, yStart, pEnd.x, pEnd.y, paint);
    	 }    	 
 	 }
     }                  
     //���ø������
     super.draw(canvas, mapView, shadow);
     
}
    
    public static Point getPoint(MapView mapView,GeoPoint gp)
    {//����γ�ȷ������Ļ�ϵ�XY����
    	Projection projettion = mapView.getProjection();
    	Point p = new Point();
    	projettion.toPixels(gp, p); 
    	return p;
    }
    
    //�����ص�ľ�γ�ȵľ���
    public static double calculationdistance(String jAA,String wAA,String jBB,String wBB) 
	{
    	double jA=Double.parseDouble(jAA.trim());
    	double wA=Double.parseDouble(wAA.trim());
    	double jB=Double.parseDouble(jBB.trim());
    	double wB=Double.parseDouble(wBB.trim());
    	double AB=6371.004*Math.acos((Math.sin(wA*Math.PI/180))*(Math.sin(wB*Math.PI/180))+(Math.cos(wA*Math.PI/180))*(Math.cos(wB*Math.PI/180))*(Math.cos(jB*Math.PI/180-jA*Math.PI/180)));
		return AB;
	}
    
    //����վ������Ļ�ϵľ���
    public static double valueofline(Point pStart,Point pEnd)
    {
		double result=0.0;
		result=Math.sqrt((pEnd.y-pStart.y)*(pEnd.y-pStart.y)+(pEnd.x-pStart.x)*(pEnd.x-pStart.x));
    	return result;
    	
    }
    
  //��������վ��ķ���
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



