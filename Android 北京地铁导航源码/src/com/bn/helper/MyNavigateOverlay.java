package com.bn.helper;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;

//���ڻ��Ƶ���·��ͼ��Overlay
class MyNavigateOverlay extends Overlay
{    
	
	static int DWidth=20;
	static int DHeight=29;
	
	GeoPoint[] points;//·���нڵ㾭γ������
    Paint paint;//����

    double direction;//�����    
    int StartIn;//��ǰ��·���������
    int step;//��ǰ��·����������
    int total;//��ǰ��·���ܲ���
    GeoPoint gpCurr;//���������ε�ǰλ��
    
	public MyNavigateOverlay(GeoPoint[] points)
	{
		this.points=points;	
		paint = new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����
		paint.setARGB(90,0,0,255);;//���û�����ɫ
		paint.setStrokeWidth(5);//����·�߻��ƿ��
	}
    
	@Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//�����մ������¼�
		return false;
	}
	
    @Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    	
    	//ѭ�����Ƶ���·��
    	for(int i=0;i<points.length-1;i++)
    	{
    		//ȡ��ÿ����·������㡢�յ�
    		Point Start=getPoint(mapView,points[i]);
    		Point End=getPoint(mapView,points[i+1]);
    			
    		paint.setARGB(160,0,0,255);;//���û�����ɫ
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
            
            //�ػ��ͼ            
            mapView.postInvalidate();
            //�õ����������ε�XY����
			float dnX=pCurr.x;
			float dnY=pCurr.y;
			
    		Matrix m1=new Matrix();
    		m1.setRotate((float)direction,dnX,dnY);	  
    		Matrix m2=new Matrix();
    		m2.setTranslate(dnX-DWidth/2,dnY-DHeight/2);
    		Matrix mz=new Matrix();
    		mz.setConcat(m1, m2);    	
    		paint.setARGB(200,0,0,255);;//���û�����ɫ
    		canvas.drawBitmap(MapNavigateActivity.bitmapDirection, mz, paint);
    	}
    	
    	//���ø������
    	super.draw(canvas, mapView, shadow);
	}
    
    public Point getPoint(MapView mapView,GeoPoint gp)
    {//����γ�ȷ������Ļ�ϵ�XY����
    	Projection projettion = mapView.getProjection();
		Point p = new Point();
		projettion.toPixels(gp, p); 
		return p;
    }
    
    //���ݵ�ǰ�Ľڵ���㵼�������η���� 
    public void calDirection(int dIndex, MapView mapView)
    {
    	StartIn=dIndex;    	
    	Point dp=getPoint(mapView,points[dIndex]);   
		if(dIndex<points.length-1)
		{//û�е����һ������Ҫ���㷽��
			Point dpNext=getPoint(mapView,points[dIndex+1]); 					
			float dx=dpNext.x-dp.x;
			float dy=-(dpNext.y-dp.y);

			int c=1;
			//����һ������˵��غϣ�����ȡ��һ����
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
    			{//��һ����
					direction=Math.toDegrees(Math.atan(dx/dy));
    			}
				else if(dx<0&&dy>0)
				{//�ڶ�����
					direction=360-Math.toDegrees(Math.atan(-dx/dy));
				} 
				else if(dx<0&&dy<0)
				{//��������
					direction=180+Math.toDegrees(Math.atan(dx/dy));
				}
				else if(dx>0&&dy<0)
				{//��������
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


