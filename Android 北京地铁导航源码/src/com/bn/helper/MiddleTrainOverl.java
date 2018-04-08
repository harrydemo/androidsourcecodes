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

class MiddleTrainOverl
{    
    public static Vector<GeoPoint> getPoint1(List<String> list)
    {    	     
    	final Vector<GeoPoint> points1=new Vector<GeoPoint>();
    	points1.clear();    	
    	for(int i=0;i<=list.size()-2;i=i+2)
     {
    	 	double jd1=Double.parseDouble(list.get(i+0).toString());
            double wd1=Double.parseDouble(list.get(i+1).toString());

            final GeoPoint gpStart = new GeoPoint
            (
             (int)(wd1*1E6), //γ��
             (int)(jd1*1E6) //����
            );
            points1.add(gpStart);          
     }             
     return points1;
     
    } 
    
    public static Vector<Point> getxy(MapView mapView,Vector<GeoPoint> georesult)
    {
		Vector<Point> result=new Vector<Point>();
		for(int i=0;i<georesult.size()-1;i++)
	     {
	     //ȡ��ÿ����·������㡢�յ�
			Point pStart=TrainOverlay.getPoint(mapView,georesult.get(i));
			result.add(pStart);
	     }
    	return result;
    	
    }
}



