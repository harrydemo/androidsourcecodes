package com.bn.helper;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;

//��ʾ�����Overlay
class MyBallonOverlay extends Overlay{
	final static int picWidth=33;  //����ͼ�Ŀ��
	final static int picHeight=34; //����ͼ�ĸ߶�
	final static int R=8;//��Ϣ���ڵ�Բ�ǰ뾶
	
	static MyBallonOverlay currentPIC=null;//��ʾ��ǰѡ�е�����
	String msg;	//�������Ӧ��������Ϣ
	Bitmap bm;//��Ӧ��ͼ��
	
	boolean showWindow=false;//�Ƿ���ʾ������Ϣ���ڵı�־λ     Ϊtrue��ʾ������Ϣ����
	
	GeoPoint gp;//�������Ӧ�ľ�γ�� 
   
	public MyBallonOverlay(GeoPoint gp,String msg,Bitmap bm)
	{
		this.gp=gp;
		this.msg=msg;
		this.bm=bm;
	}
	
    @Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) {
        if(currentPIC!=null&&currentPIC!=this)
        {
        	return false;
        }
    	
    	if(event.getAction() == MotionEvent.ACTION_DOWN)
        {    	
        	int x=(int)event.getX();
            int y=(int)event.getY();
            Point p= getPoint(mv); 
            
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;
            
            if(x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {    	
            	currentPIC=this;
            	return true;
            }
        }
    	else if (event.getAction() == MotionEvent.ACTION_MOVE) 
    	{
    		return currentPIC!=null;
    	}    		
        else if (event.getAction() == MotionEvent.ACTION_UP) 
        {
        	//��ȡ���ر�λ��
            int x=(int)event.getX();
            int y=(int)event.getY();
            
            //��ȡ��������Ļ�ϵ����귶Χ
            Point p= getPoint(mv);             
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;           
            
            if(currentPIC==this&&x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {
            	//��ʾ�����ݺ���յ�ǰ����
            	currentPIC=null;     
            	showWindow=!showWindow;
            	
            	List<Overlay> overlays = mv.getOverlays();
            	overlays.remove(this);//ɾ�������������
            	overlays.add(this);//�������λ����������
            	return true;
            }
            else if(currentPIC==this)
            {//����ǰ����Ϊ�Լ���̧�𴥿ز����Լ������������״̬������true
            	currentPIC=null;
            	return true;            	
            }            
        }
        return false;
    }
    
    @Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {			   	
    	Point p= getPoint(mapView);  	    	
		canvas.drawBitmap(bm, p.x-picWidth/2+3, p.y-picHeight-12, null);
		
		if(showWindow)
		{
			drawWindow(canvas,p,170);
		}
		
		//���ø������
		super.draw(canvas, mapView, shadow);
	}
    
    public Point getPoint(MapView mapView)
    {//����γ�ȷ������Ļ�ϵ�XY����
    	Projection projettion = mapView.getProjection();
		Point p = new Point();
		projettion.toPixels(gp, p); 
		return p;
    }
    
	public void drawWindow(Canvas canvas,Point p,int winWidth) 
	{//������Ϣ���ڵķ���
		int singleSize=15;
		int textSize=16;
		int leftPadding=10;
		
		//Ϊ��Ϣ����
		int line_Width=winWidth-leftPadding*2;//ÿ�еĿ��
		int lineCharCount=line_Width/(singleSize);	//ÿ���ַ���
		//ɨ��������Ϣ�ַ������з���
		ArrayList<String> totalRows=new ArrayList<String>();//��¼�����е�ArrayList
		String currentRow="";//��ǰ�е��ַ���
		for(int i=0;i<msg.length();i++)
		{			
			char c=msg.charAt(i);
			if(c!='\n')
			{//����ǰ�ַ���Ϊ���з�����뵽��ǰ����
				currentRow=currentRow+c;
			}
			else
			{
				if(currentRow.length()>0)
				{
					totalRows.add(currentRow);
				}				
				currentRow="";//��յ�ǰ��
			}
			if(currentRow.length()==lineCharCount)	
			{//����ǰ�еĳ��ȴﵽһ�й涨���ַ�����
		     //����ǰ�м����¼�����е�ArrayList
				totalRows.add(currentRow);
				currentRow="";//��յ�ǰ��
			}
		}
		if(currentRow.length()>0)
		{//����ǰ�г��ȴ������򽫵�ǰ�м����¼�����е�ArrayList
			totalRows.add(currentRow);
		}
		int lineCount=totalRows.size();//���������
		int winHeight=lineCount*(singleSize)+2*R;//�Զ�������Ϣ����߶�
		//����paint����
		Paint paint=new Paint();
		paint.setAntiAlias(true);//�򿪿����
		paint.setTextSize(textSize);//�������ִ�С	
		//���� ��Ϣ����Բ�Ǿ���
		paint.setARGB(255, 255,251,239);
		int x1=p.x-winWidth/2;
		int y1=p.y-picHeight-winHeight-12;
		int x2=p.x+winWidth/2;
		int y2=p.y-picHeight-12;		
		RectF r=new RectF(x1,y1,x2,y2);		
		canvas.drawRoundRect(r, R, R, paint);
		//���� ��Ϣ����Բ�Ǿ��α߿�
		paint.setARGB(255,198,195,198);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRoundRect(r, R, R, paint);
		
		//ѭ������ÿ������
		paint.setStrokeWidth(0);
		paint.setARGB(255, 10, 10, 10);		
		int lineY=y1+R+singleSize;
		for(String lineStr:totalRows)
		{//��ÿһ�н���ѭ��	
			for(int j=0;j<lineStr.length();j++)
			{//��һ���е�ÿ����ѭ��
				String colChar=lineStr.charAt(j)+"";				
				canvas.drawText(colChar, x1+leftPadding+j*singleSize, lineY, paint);
			}
			lineY=lineY+singleSize;//y����������һ��
		}
	}
}


