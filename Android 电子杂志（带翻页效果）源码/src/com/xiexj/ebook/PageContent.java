package com.xiexj.ebook;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class PageContent extends View{
	
	private Bitmap img;
	
	private Paint paint;
	
	private int imgResId;
	
	private BookLayout blo;
	
	//内容ID
	private int contentId = -1;
	
	//是否是封面
	private boolean isCover = false;;
	
	//是否是内容页
	private boolean isContentPage = false;

	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}

	public boolean isContentPage() {
		return isContentPage;
	}

	public void setContentPage(boolean isContentPage) {
		this.isContentPage = isContentPage;
	}

	public PageContent(Context context,int imgResId,BookLayout blo) {
		super(context);
		this.imgResId = imgResId;
		paint = new Paint();
		paint.setAntiAlias(true);
		this.blo = blo;
	}
	
	private int x;
	
	private int y;
	
	private static final int padding = 5;

	private int width;
	
	private int height;
	
	private static final int linePadding = 20;
	
	public void setPosition(float x,float y){
		this.x = (int)x;
		this.y = (int)y;
	}
	
	public void setSize(int w,int h){
		width = w;
		height = h;
	}
	
	public void create(){
		if(imgResId>-1&&img==null){
			img = BitmapFactory.decodeResource(getResources(), imgResId);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isCover){
			if(img!=null){
				canvas.drawBitmap(img, new Rect(0,0,width-2*padding,height-2*padding), new Rect(x+padding,y+padding,x+width-padding,y+height-padding), paint);
			}
		}
		if(isContentPage){
			int sx = x+padding;
			int sy = y+padding;
			if(img!=null){
				canvas.drawBitmap(img, new Rect(0,0,width-2*padding,(height-2*padding)/2), new Rect(x+padding,y+padding,x+width-padding,y+height/2), paint);
				sy = y+height/2;
			}
			sy = sy+linePadding;
			paint.setARGB(255, 0, 0, 0);
			paint.setStrokeWidth(1);
			int i=0;
			
			ArrayList<String> cl = null;
			if(blo.getContentList()!=null&&contentId!=-1&&contentId<blo.getContentList().size())
				cl = blo.getContentList().get(contentId);
			String c = null;
			while(sy<y+height-padding){
				if(cl!=null&&i<cl.size()){
					c = cl.get(i);
					canvas.drawText(c, sx, sy-2, paint);
				}
				canvas.drawLine(sx, sy, x+width-padding, sy, paint);
				sy = sy+linePadding;
			}
		}
	}
	
	/**
	 * 计算目前有多少内容行
	 * @return
	 */
	public static int getPageContentLine(int height){
		int sy = height/2;
		int lines = 0;
		sy = sy+linePadding;
		while(sy<height-padding){
			sy = sy+linePadding;
			lines++;
		}
		return lines;
	}
	
	/**
	 * 一行的宽度
	 * @param width
	 * @return
	 */
	public static float getPageContentWidth(float width){
		return width-2*padding;
	}
	
	public void destory(){
		if(img!=null){
			img.recycle();
			img = null;
		}
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	
}
