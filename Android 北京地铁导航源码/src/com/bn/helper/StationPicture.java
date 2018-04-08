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
class StationPicture extends Overlay
{    
	final static int picWidth=8;  //����ͼ�Ŀ��
	final static int picHeight=8; //����ͼ�ĸ߶�
	final static int picWidthbig=11;  //����ͼ�Ŀ��
	final static int picHeightbig=11; //����ͼ�ĸ߶�
	
	
    Paint paint;//����       
    public StationPicture()
    {   	   	
    	paint = new Paint();//��������
    	paint.setAntiAlias(true);//�򿪿����
    	paint.setStrokeWidth(5);//����·�߻��ƿ��    	
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//�����մ������¼�
		return false;
	}

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
     
     //����վ���ͼ��    
     Vector<String> listArr1=new Vector<String>();
     Vector<String> listArr11=new Vector<String>();
     Vector<String> listArr2=new Vector<String>();
     Vector<String> listArr22=new Vector<String>();
     Vector<String> listArr3=new Vector<String>();
     Vector<String> listArr33=new Vector<String>();
     if(TrainSystemHelperActivity.flag1==true||TrainSystemHelperActivity.str.equals("����1����"))
     {
    	 listArr1=DBUtil.searchSinglejw("����1����");
    	 listArr11=DBUtil.checktransforstop("����1����");
     }
     if(TrainSystemHelperActivity.flag2==true||TrainSystemHelperActivity.str.equals("����2����"))
     {
    	 listArr2=DBUtil.searchSinglejw("����2����");
    	 listArr22=DBUtil.checktransforstop("����2����");
     }
     if(TrainSystemHelperActivity.flag3==true||TrainSystemHelperActivity.str.equals("����13����"))
     {
    	 listArr3=DBUtil.searchSinglejw("����13����");
    	 listArr33=DBUtil.checktransforstop("����13����");
     }
     
     Vector<GeoPoint> poin1=new Vector<GeoPoint>();
     poin1=MiddleTrainOverl.getPoint1(listArr1);
     Vector<GeoPoint> poin2=new Vector<GeoPoint>();
     poin2=MiddleTrainOverl.getPoint1(listArr2);
     Vector<GeoPoint> poin3=new Vector<GeoPoint>();
     poin3=MiddleTrainOverl.getPoint1(listArr3);
     
     //��תվ
     Vector<GeoPoint> poin11=new Vector<GeoPoint>();
     poin11=MiddleTrainOverl.getPoint1(listArr11);
     Vector<GeoPoint> poin22=new Vector<GeoPoint>();
     poin22=MiddleTrainOverl.getPoint1(listArr22);
     Vector<GeoPoint> poin33=new Vector<GeoPoint>();
     poin33=MiddleTrainOverl.getPoint1(listArr33);
     
     for(int i=0;i<poin1.size();i=i+1)
     {
     //��վ��    	
    	 	Point pStart=getPoint(mapView,poin1.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.smallstation, pStart.x-picWidth/2, pStart.y-picHeight/2, null);   	  	 
     }
     
     for(int i=0;i<poin2.size();i=i+1)
     {
     //��վ��
    	 	Point pStart=getPoint(mapView,poin2.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.smallstation, pStart.x-picWidth/2, pStart.y-picHeight/2, null);   	  	 
     }
     for(int i=0;i<poin3.size();i=i+1)
     {
     //��վ��
    	 	Point pStart=getPoint(mapView,poin3.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.smallstation, pStart.x-picWidth/2, pStart.y-picHeight/2, null);   	  	 
     }
     
     //����תվ
     for(int i=0;i<poin11.size();i=i+1)
     {
     //��վ��    	
    	 	Point pStart=getPoint(mapView,poin11.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.station, pStart.x-picWidthbig/2, pStart.y-picHeightbig/2, null);   	  	 
     }
     
     for(int i=0;i<poin22.size();i=i+1)
     {
     //��վ��
    	 	Point pStart=getPoint(mapView,poin22.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.station, pStart.x-picWidthbig/2, pStart.y-picHeightbig/2, null);   	  	 
     }
     for(int i=0;i<poin33.size();i=i+1)
     {
     //��վ��
    	 	Point pStart=getPoint(mapView,poin33.get(i));    	 
    		canvas.drawBitmap(MapNavigateActivity.station, pStart.x-picWidthbig/2, pStart.y-picHeightbig/2, null);   	  	 
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
}



