package com.bn.helper;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;

//表示气球的Overlay
class MyBallonOverlay extends Overlay{
	final static int picWidth=33;  //气球图的宽度
	final static int picHeight=34; //气球图的高度
	final static int R=8;//信息窗口的圆角半径
	
	static MyBallonOverlay currentPIC=null;//表示当前选中的气球
	String msg;	//此气球对应的文字信息
	Bitmap bm;//对应的图标
	
	boolean showWindow=false;//是否显示文字信息窗口的标志位     为true显示文字信息窗口
	
	GeoPoint gp;//此气球对应的经纬度 
   
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
        	//获取触控笔位置
            int x=(int)event.getX();
            int y=(int)event.getY();
            
            //获取气球在屏幕上的坐标范围
            Point p= getPoint(mv);             
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;           
            
            if(currentPIC==this&&x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {
            	//显示完内容后清空当前气球
            	currentPIC=null;     
            	showWindow=!showWindow;
            	
            	List<Overlay> overlays = mv.getOverlays();
            	overlays.remove(this);//删除此气球再添加
            	overlays.add(this);//此气球就位于最上面了
            	return true;
            }
            else if(currentPIC==this)
            {//若当前气球为自己但抬起触控不再自己上则清空气球状态并返回true
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
		
		//调用父类绘制
		super.draw(canvas, mapView, shadow);
	}
    
    public Point getPoint(MapView mapView)
    {//将经纬度翻译成屏幕上的XY坐标
    	Projection projettion = mapView.getProjection();
		Point p = new Point();
		projettion.toPixels(gp, p); 
		return p;
    }
    
	public void drawWindow(Canvas canvas,Point p,int winWidth) 
	{//绘制信息窗口的方法
		int singleSize=15;
		int textSize=16;
		int leftPadding=10;
		
		//为信息分行
		int line_Width=winWidth-leftPadding*2;//每行的宽度
		int lineCharCount=line_Width/(singleSize);	//每行字符数
		//扫描整个信息字符串进行分行
		ArrayList<String> totalRows=new ArrayList<String>();//记录所有行的ArrayList
		String currentRow="";//当前行的字符串
		for(int i=0;i<msg.length();i++)
		{			
			char c=msg.charAt(i);
			if(c!='\n')
			{//若当前字符不为换行符则加入到当前行中
				currentRow=currentRow+c;
			}
			else
			{
				if(currentRow.length()>0)
				{
					totalRows.add(currentRow);
				}				
				currentRow="";//清空当前行
			}
			if(currentRow.length()==lineCharCount)	
			{//若当前行的长度达到一行规定的字符数则
		     //将当前行加入记录所有行的ArrayList
				totalRows.add(currentRow);
				currentRow="";//清空当前行
			}
		}
		if(currentRow.length()>0)
		{//若当前行长度大于零则将当前行加入记录所有行的ArrayList
			totalRows.add(currentRow);
		}
		int lineCount=totalRows.size();//获得总行数
		int winHeight=lineCount*(singleSize)+2*R;//自动计算信息窗体高度
		//创建paint对象
		Paint paint=new Paint();
		paint.setAntiAlias(true);//打开抗锯齿
		paint.setTextSize(textSize);//设置文字大小	
		//绘制 信息窗体圆角矩形
		paint.setARGB(255, 255,251,239);
		int x1=p.x-winWidth/2;
		int y1=p.y-picHeight-winHeight-12;
		int x2=p.x+winWidth/2;
		int y2=p.y-picHeight-12;		
		RectF r=new RectF(x1,y1,x2,y2);		
		canvas.drawRoundRect(r, R, R, paint);
		//绘制 信息窗体圆角矩形边框
		paint.setARGB(255,198,195,198);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRoundRect(r, R, R, paint);
		
		//循环绘制每行文字
		paint.setStrokeWidth(0);
		paint.setARGB(255, 10, 10, 10);		
		int lineY=y1+R+singleSize;
		for(String lineStr:totalRows)
		{//对每一行进行循环	
			for(int j=0;j<lineStr.length();j++)
			{//对一行中的每个字循环
				String colChar=lineStr.charAt(j)+"";				
				canvas.drawText(colChar, x1+leftPadding+j*singleSize, lineY, paint);
			}
			lineY=lineY+singleSize;//y坐标移向下一行
		}
	}
}


