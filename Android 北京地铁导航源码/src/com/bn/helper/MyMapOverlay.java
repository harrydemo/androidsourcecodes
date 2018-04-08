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

//����������ͼ��׽�����¼���͸��OverLay
class MyMapOverlay extends Overlay{
	boolean flagMove=false;//�Ƿ�Ϊ�ƶ�������־λ
	static int nState=0; //״̬��־Ϊ 0��ʾ��ʼ״̬ 1��ʾ�Ѿ�������ʼ���״̬
	static GeoPoint gpStart;//��ʼ�㾭γ��
	static GeoPoint gpEnd;//�����㾭γ��
	
	float previousX;//�ϴδ��ص�X����
	float previousY;//�ϴδ��ص�Y����
	
	static final int MOVE_THRESHOLD=10;//�����ص��ƶ���Χ��������ֵʱ����Ϊ�ǵ��
	static Bitmap bitmapStart;
	 @Override 
	    public boolean onTouchEvent(MotionEvent event, MapView mv) {
	        if(event.getAction() == MotionEvent.ACTION_MOVE)
	        {//���ƶ��˴��ر��������ƶ���־Ϊtrue
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
	        {//�������˴��ر��������ƶ���־Ϊfalse������¼���´��رʵ�λ��
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
	        		 (int) event.getX(),  //���ر�����Ļ�ϵ�X����
	        		 (int) event.getY()   //���ر�����Ļ�ϵ�Y����
	            );	            
	        	//��ʾ������ľ�γ��
	            String latStr=Math.round(gp.getLatitudeE6()/1000.00)/1000.0+"";//γ��
	        	String longStr=Math.round(gp.getLongitudeE6()/1000.00)/1000.0+"";//����
	        	
	        	
	        	for(int n=0;n<ve.size()-1;n=n+3)
	        	{
	        		if(TrainOverlay.calculationdistance(longStr, latStr, ve.get(n).toString(), ve.get(n+1).toString())<XIU_ZHENG)
	        		{
	        			double jj=Double.parseDouble(ve.get(n));  //ת������
	        	        double ww=Double.parseDouble(ve.get(n+1));  //ת��Ϊ����
	        	        GeoPoint gpp = new GeoPoint
	        	        (
	        	        		(int)(ww*1E6),  //γ��
	        	        		(int)(jj*1E6)  //����
	        	        );
	        			
	    	        	List<Overlay> overlays = mv.getOverlays(); 
	    	        	int i=0;
	    	        	for(Overlay ol:overlays)
	    	        	{//������������showWindow���
	    	        				i++;	    	        		
	    	        	} 
	    	        	if(i<6)
	    	        	{
	    	        		MyBallonOverlay mbo=new MyBallonOverlay
	    		        	(
	    		        			gpp,		//���������
	    		        			ve.get(n+2).toString()+"\n���ȣ�"+longStr+"\nγ�ȣ�"+latStr, //�������Ϣ
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
	    	        		//�������Overlay
	    	        		overlays.clear();
	    	        		//������ڻ�ȡȫ�ִ����¼���͸��Overlay
	    	        		overlays.add(first1);
	    	        		overlays.add(first2);
	    	        		overlays.add(first3);
	    	        		overlays.add(first4);
	    	        		overlays.add(first5);
	    	        		MyBallonOverlay mbo=new MyBallonOverlay
	    		        	(
	    		        			gpp,		//���������
	    		        			ve.get(n+2).toString()+"\n���ȣ�"+longStr+"\nγ�ȣ�"+latStr, //�������Ϣ
	    		        			MapNavigateActivity.bitmapEnd
	    		        	);
	    	        		gpEnd=gpp;
	    	        		mbo.showWindow=true;
	    		            overlays.add(mbo); 
	    	        	}	        	
	        		}//"��ǰվ�㣺\n"+ve.get(n+2).toString()'''"��������Ϊ��\n���ȣ�"+longStr+"\nγ�ȣ�"+latStr
	        	}	        		        	
	            return true;
	        }
	        return false;
	    }

}