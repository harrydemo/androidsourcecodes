package com.xiexj.ebook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class SinglePage extends View {

	private boolean isLeftPage = false;
	
	private int pageNo;
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private Paint paint;
	
	private int dis = 10;
	
	//当前选中的角
	private int chooseCorner = -1;
	
	public static final int LEFT_UP_CORNER = 0;
	
	public static final int LEFT_BOTTOM_CORNER = 1;
	
	public static final int RIGHT_UP_CORNER = 2;
	
	public static final int RIGHT_BOTTOM_CORNER = 3;
	
	//A点的坐标是手指目前的坐标
	private float aX = -1;
	
	private float aY = -1;
	
	//B点的坐标是A点翻起后跟下一页的交叉点
	private float bX = -1;
	
	private float bY = -1;
	
	//C点的坐标是D点翻起后跟下一页的交叉点，可能与D点重复
	private float cX = -1;
	
	private float cY = -1;
	
	//D点的坐标是本页的另一个角的点的坐标
	private float dX = -1;
	
	private float dY = -1;
	
	//旋转的角度
	private float angle = 0;
	
	//翻动页背面的阴影角度
	private float shadowAngle = 0;
	
	//阴影长度
	private float shadowLength = 0;
	
	//阴影的坐标x
	private float sdx = 0;
	
	//阴影的坐标y
	private float sdy = 0;
	
	//是否隐藏层
	private boolean isMask;
	
	//是否是当前显示页
	private boolean isLookPage;
	
	//隐藏层的显示区域
	private Path maskPath;
	
	//翻动页的fx坐标
	private float fx;
	
	//翻动页的fy坐标
	private float fy;
	
	//背景色
	private int bc;
	
	//显示的角
	private int cr;
	
	private BookLayout blo;
	
	private PageContent content;
	
	public SinglePage(Context context,boolean isLeftPage,int pageNo,BookLayout blo,PageContent content) {
		super(context);
		this.isLeftPage = isLeftPage;
		this.pageNo = pageNo;
		this.blo = blo;
		this.content = content;
		paint = new Paint();
		bc = Color.WHITE;
		paint.setARGB(255, Color.red(bc), Color.green(bc), Color.blue(bc));
		paint.setAntiAlias(true);
		isMask = true;
		isLookPage = false;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//抗锯齿
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
		if(isMask){//如果是遮罩层
//			if(blo.getCurrentFlipPage()!=null&&this==blo.getMaskPage(blo.getCurrentFlipPage())){
//				Log.e("SinglePage", "------->pageNo:"+pageNo);
//			}
			if(maskPath==null) return;
			float tx = 0;
			float ty = 0;
			switch(cr){
			case LEFT_UP_CORNER:{
				tx = fx;
				ty = fy;
				break;
			}
			case RIGHT_UP_CORNER:{
				tx = fx-width;
				ty = fy;
				break;
			}
			case LEFT_BOTTOM_CORNER:{
				tx = fx;
				ty = fy-height;
				break;
			}
			case RIGHT_BOTTOM_CORNER:{
				tx = fx-width;
				ty = fy-height;
				break;
			}
			}
			canvas.clipPath(maskPath);
			canvas.save();
			canvas.rotate(angle, fx, fy);
			paint.setStyle(Style.FILL);
			paint.setARGB(255, Color.red(bc), Color.green(bc), Color.blue(bc));
			canvas.drawRect(tx,ty,tx+width,ty+height, paint);
			content.setPosition(tx, ty);
			content.draw(canvas);
			paint.setStyle(Style.STROKE);
			paint.setARGB(255,0,0,0);
			canvas.drawRect(tx,ty,tx+width,ty+height, paint);
			if(content!=null&&content.isContentPage()){
				if(isLeftPage){
					canvas.drawText("第"+(pageNo-1)+"页", tx+5, ty+height-5, paint);
				}else{
					canvas.drawText("第"+(pageNo-1)+"页", tx+width-5-paint.measureText("第"+(pageNo-1)+"页"), ty+height-5, paint);
				}
			}
			canvas.restore();
			canvas.save();
			canvas.rotate(shadowAngle, sdx, sdy);
			paint.setStyle(Style.FILL);
			shadowLength = (float)Math.sqrt(width*width+height*height);
			if(isLeftPage){
				Shader linearShader = new LinearGradient(sdx-50,sdy,sdx,sdy,Color.argb(0, 0, 0, 0),Color.argb(33, 0, 0, 0),TileMode.CLAMP);
				paint.setShader(linearShader);
				canvas.drawRect(sdx-50, sdy, sdx, sdy+shadowLength, paint);
			}else{
				Shader linearShader = new LinearGradient(sdx,sdy,sdx+50,sdy,Color.argb(33, 0, 0, 0),Color.argb(0, 0, 0, 0),TileMode.CLAMP);
				paint.setShader(linearShader);
				canvas.drawRect(sdx, sdy, sdx+50, sdy+shadowLength, paint);
			}
			paint.setShader(null);
			canvas.restore();
		}else{//显示层
			Path backPath = null;
			if(aX!=-1&&aY!=-1){
				backPath = new Path();
				backPath.moveTo(aX, aY);
				backPath.lineTo(bX, bY);
				backPath.lineTo(cX, cY);
				if(cX!=dX||cY!=dY){
					backPath.lineTo(dX, dY);
				}
				backPath.lineTo(aX, aY);
				float px = -1;
				float py = -1;
				float ox = -1;
				float oy = -1;
				float ex = -1;
				float ey = -1;
				switch(chooseCorner){
				case RIGHT_UP_CORNER:{
					px = x;
					py = y;
					ox = x;
					oy = y+height;
					ex = x+width;
					ey = y+height;
					break;
				}
				case RIGHT_BOTTOM_CORNER:{
					px = x;
					py = y+height;
					ox = x;
					oy = y;
					ex = x+width;
					ey = y;
					break;
				}
				case LEFT_UP_CORNER:{
					px = width+x;
					py = y;
					ox = width+x;
					oy = height+y;
					ex = x;
					ey = y+height;
					break;
				}
				case LEFT_BOTTOM_CORNER:{
					px = width+x;
					py = height+y;
					ox = width+x;
					oy = y;
					ex = x;
					ey = y;
					break;
				}
				}
				maskPath = new Path();
				maskPath.moveTo(bX, bY);
				maskPath.lineTo(px, py);
				maskPath.lineTo(ox, oy);
				if(cX==dX&&cY==dY){
					maskPath.lineTo(ex, ey);
				}
				maskPath.lineTo(cX, cY);
				maskPath.lineTo(bX, bY);
			}
			if(maskPath!=null){
				canvas.clipPath(maskPath);
				canvas.save();
			}
			paint.setStyle(Style.FILL);
			paint.setARGB(255, Color.red(bc), Color.green(bc), Color.blue(bc));
			canvas.drawRect(x,y,x+width,y+height, paint);
			paint.setARGB(255,0,0,0);		
			paint.setStyle(Style.STROKE);
			content.setPosition(x, y);
			canvas.drawRect(x,y,x+width,y+height, paint);
			content.draw(canvas);
			if(content!=null&&content.isContentPage()){
				if(isLeftPage){
					canvas.drawText("第"+(pageNo-1)+"页", x+5, y+height-5, paint);
				}else{
					canvas.drawText("第"+(pageNo-1)+"页", x+width-5-paint.measureText("第"+(pageNo-1)+"页"), y+height-5, paint);
				}
			}
			//显示书轴阴影
			paint.setStyle(Style.FILL);
			if(!blo.isFirstOrLastPage(this)){
				if(isLeftPage){
					Shader linearShader = new LinearGradient(x+width-50,y,x+width,y,Color.argb(0, 0, 0, 0),Color.argb(33, 0, 0, 0),TileMode.CLAMP);
					paint.setShader(linearShader);
					canvas.drawRect(x+width-50, y, x+width, y+height, paint);
				}else{
					Shader linearShader = new LinearGradient(x,y,x+50,y,Color.argb(33, 0, 0, 0),Color.argb(0, 0, 0, 0),TileMode.CLAMP);
					paint.setShader(linearShader);
					canvas.drawRect(x, y, x+50, y+height, paint);
				}
				paint.setShader(null);
			}
			
			if(maskPath!=null){
				canvas.restore();
			}
			if(backPath!=null){
				float sdx = -1;
				float sdy = -1;
				if(bY>cY){
					sdy = cY;
					sdx = cX;
				}else{
					sdy = bY;
					sdx = bX;
				}
				blo.flipPage(this, backPath,angle,shadowAngle,aX,aY,sdx,sdy,chooseCorner);
			}
		}
	}
	
	/**
	 * 显示反页遮罩层的区域
	 * @param path
	 * @param angle
	 * @param x
	 * @param y
	 */
	public void onMaskPathDraw(Path path,float angle,float shadowAngle,float x,float y,float sdx,float sdy,int chooseCorner){
		this.maskPath = path;
		this.angle = angle;
		this.shadowAngle = shadowAngle;
		this.fx = x;
		this.fy = y;
		this.sdx = sdx;
		this.sdy = sdy;
		switch(chooseCorner){
		case LEFT_UP_CORNER:{
			cr = RIGHT_UP_CORNER;
			break;
		}
		case RIGHT_UP_CORNER:{
			cr = LEFT_UP_CORNER;
			break;
		}
		case LEFT_BOTTOM_CORNER:{
			cr = RIGHT_BOTTOM_CORNER;
			break;
		}
		case RIGHT_BOTTOM_CORNER:{
			cr = LEFT_BOTTOM_CORNER;
			break;
		}
		}
		postInvalidate();
	}
	
	public void setSize(int w,int h){
		width = w;
		height = h;
		content.setSize(width, height);
	}
	
	public void setPosition(int x,int y){
		this.x = x;
		this.y = y;
		fx = x;
		fy = y;
	}
	
	public void setBackgroundColor(int color){
		this.bc = color;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isLookPage||blo.isAutoFlip()){
			return false;
		}
		int act = event.getAction();
		float hx = event.getX();
		float hy = event.getY();
		switch(act){
		case MotionEvent.ACTION_DOWN:{
			if(isNearCorner(hx, hy)&&isEnableChoose(hx, hy)){
				//初始化翻页的角度
				aX = hx;
				aY = hy;
				calculate();
				postInvalidate();
				blo.setCurrentFlipPage(this);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE:{
			if(chooseCorner!=-1&&isEnableChoose(hx, hy)){
				aX = hx;
				aY = hy;
				calculate();
				postInvalidate();
			}
			break;
		}
		case MotionEvent.ACTION_UP:{
			if(chooseCorner!=-1){
				successOrResetPage();
			}
			break;
		}
		}
		if(chooseCorner==-1){
			return false;
		}
		return true;
	}
	
	//判断是否在页角附近
	private boolean isNearCorner(float hx,float hy){
		//左上角
		int lux = x;
		int luy = y;
		
		//左下角
		int lbx = x;
		int lby = y+height;
		
		//右上角
		int rux = x+width;
		int ruy = y;
		
		//右下角
		int rbx = x+width;
		int rby = y+height;
		int[][] pd = {
				{lux,luy},
				{lbx,lby},
				{rux,ruy},
				{rbx,rby}
		};
		for(int i=0;i<pd.length;i++){
			//计算是否在页角附近，并返回是哪个页角
			if(isLeftPage){
				if(i>=2) continue;
			}else{
				if(i<2) continue;
			}
			if((pd[i][0]-hx)*(pd[i][0]-hx)+(pd[i][1]-hy)*(pd[i][1]-hy)<=2*dis*dis){
				chooseCorner = i;
				return true;
			}
		}
		return false;
	}

	//判断是否是有效的翻页坐标
	private boolean isEnableChoose(float hx,float hy){
		boolean flag = false;
		switch(chooseCorner){
		case LEFT_UP_CORNER:{
			if((hx-(width+x))*(hx-(width+x))+(hy-y)*(hy-y)<=width*width && (hx-(width+x))*(hx-(width+x))+(hy-(height+y))*(hy-(height+y))<=width*width+height*height){
				flag = true;
			}
			break;
		}
		case LEFT_BOTTOM_CORNER:{
			if((hx-(width+x))*(hx-(width+x))+(hy-(height+y))*(hy-(height+y))<=width*width && (hx-(width+x))*(hx-(width+x))+(hy-y)*(hy-y)<=width*width+height*height){
				flag = true;
			}
			break;
		}
		case RIGHT_UP_CORNER:{
			if((hx-x)*(hx-x)+(hy-y)*(hy-y)<=width*width && (hx-x)*(hx-x)+(hy-(height+y))*(hy-(height+y))<=width*width+height*height){
				flag = true;
			}
			break;
		}
		case RIGHT_BOTTOM_CORNER:{
			if((hx-x)*(hx-x)+(hy-(height+y))*(hy-(height+y))<=width*width && (hx-x)*(hx-x)+(hy-y)*(hy-y)<=width*width+height*height){
				flag = true;
			}
			break;
		}
		}
//		Log.e("Page","isEnableChoose "+flag);
		return flag;
	}
	
	//判断是否成功翻页还是取消翻页
	private void successOrResetPage(){
		float sx = -1;
		float sy = -1;
		float fx = -1;
		float fy = -1;
		
		//手松开的时候，如果另一侧的角翻动幅度更大的话，以那个角作为A点，启动自动翻书动作
		if(isLeftPage){
			if(dX>aX){
				aX = dX;
				aY = dY;
				if(chooseCorner==LEFT_UP_CORNER){
					chooseCorner = LEFT_BOTTOM_CORNER;
				}else{
					chooseCorner = LEFT_UP_CORNER;
				}
			}
		}else{
			if(dX<aX){
				aX = dX;
				aY = dY;
				if(chooseCorner==RIGHT_UP_CORNER){
					chooseCorner = RIGHT_BOTTOM_CORNER;
				}else{
					chooseCorner = RIGHT_UP_CORNER;
				}
			}
		}
		calculate();
		
		//计算成功或失败翻页后，被触碰的页角需要达到的坐标
		switch(chooseCorner){
		case LEFT_UP_CORNER:{
			sx = x+width*2;
			sy = y;
			fx = x;
			fy = y;
			break;
		}
		case LEFT_BOTTOM_CORNER:{
			sx = x+width*2;
			sy = y+height;
			fx = x;
			fy = y+height;
			break;
		}
		case RIGHT_UP_CORNER:{
			sx = x-width;
			sy = y;
			fx = x+width;
			fy = y;
			break;
		}
		case RIGHT_BOTTOM_CORNER:{
			sx = x-width;
			sy = y+height;
			fx = x+width;
			fy = y+height;
			break;
		}
		}
		if(isLeftPage&&(aX>=x+width*3/2||dX>=x+width*3/2)){
			autoFlipPage(sx, sy, true);
		}else if(!isLeftPage&&(aX<=x-width/2||dX<=x-width/2)){
			autoFlipPage(sx, sy, true);
		}else{
			autoFlipPage(fx,fy,false);
		}
	}
	
	//当在翻页过程中，手松开页角的时候，自动执行翻页动作，让翻页更真实
	private void autoFlipPage(final float tx,final float ty,final boolean isSuccessFlip){
		blo.setAutoFlip(true);
		new Thread(){
			public void run(){
				int count = 1;
				boolean flag = false;
				while(true){
					try {
						flag=autoMovePageCorner(tx, ty,count++);
						Thread.sleep(100);
						if(flag) break;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//清除翻页参数
				chooseCorner = -1;
				clearPosition();
				if(isSuccessFlip){
					handler.post(new Runnable(){
						public void run(){
							blo.setCurrentFlipPage(null);
							blo.successFlipPage(SinglePage.this, true);
						}
					});
				}else{
					handler.post(new Runnable(){
						public void run(){
							blo.setCurrentFlipPage(null);
							blo.successFlipPage(SinglePage.this, false);
						}
					});
				}
				blo.setAutoFlip(false);
			}
		}.start();
	}
	
	private Handler handler = new Handler();
	
	//线程内部，自动移动页角
	private boolean autoMovePageCorner(float tx,float ty,int num){
		boolean isFinish = false;
		if((aX-tx)*(aX-tx)+(aY-ty)*(aY-ty)<=dis*dis){
			isFinish = true;
		}else{
			handler.post(new Runnable(){
				public void run(){
					postInvalidate();
				}
			});
			aX = aX-num*(aX-tx)/10;
			aY = aY-num*(aY-ty)/10;
			calculate();
		}
		return isFinish;
	}
	
	//根据A点计算其余点
	private void calculate(){
		float px = -1;
		float py = -1;
		float ox = -1;
		float oy = -1;
		switch(chooseCorner){
		case LEFT_UP_CORNER:{
			px = x;
			py = y;
			ox = x;
			oy = y+height;
			bY = y;
			cX = x;
			cY = y+height;
			break;
		}
		case LEFT_BOTTOM_CORNER:{
			px = x;
			py = y+height;
			ox = x;
			oy = y;
			bY = y+height;
			cX = x;
			cY = y;
			break;
		}
		case RIGHT_UP_CORNER:{
			px = width+x;
			py = y;
			ox = width+x;
			oy = height+y;
			bY = y;
			cX = x+width;
			cY = y+height;
			break;
		}
		case RIGHT_BOTTOM_CORNER:{
			px = width+x;
			py = height+y;
			ox = width+x;
			oy = y;
			bY = y+height;
			cX = x+width;
			cY = y;
			break;
		}
		}
		if(aY==py){
			bY = aY;
			bX = (aX+px)/2;
			dY = oy;
			dX = aX;
			cX = (aX+ox)/2;
			cY = oy;
			angle = 0;
			return;
		}
		MNLine lineOne = MNLine.initLine(aX, aY, px, py);
		MNLine lineTwo = lineOne.getPBLine((aX+px)/2, (aY+py)/2);
		bX = lineTwo.getXbyY(bY);
		float tempCY = lineTwo.getYbyX(cX);
		if(tempCY>=y&&tempCY<=y+height){//在左或右边交叉，是三角型，
			cY = tempCY;
			dX = cX;
			dY = cY;
		}else{
			cX = lineTwo.getXbyY(cY);
			//将直线平移，经过页的另一个角
			lineOne.change(ox, oy);
			float[] xAndY = lineOne.getCross(lineTwo);
			dX = 2*xAndY[0]-ox;
			dY = 2*xAndY[1]-oy;
		}
		if(((Float)bX).isNaN()||((Float)bY).isNaN()||((Float)cX).isNaN()||((Float)cY).isNaN()||((Float)dX).isNaN()||((Float)dY).isNaN()){
			clearPosition();
		}else{
			MNLine lineThree = MNLine.initLine(aX, aY, dX, dY);
			angle = (float)(Math.atan(lineThree.getA())*180/Math.PI);
			if(angle>0){
				if(aX<=dX&&aY<=dY&&chooseCorner==RIGHT_BOTTOM_CORNER){
					angle = 90+angle;
				}else if(aX>=dX&&aY>=dY&&chooseCorner==LEFT_UP_CORNER){
					angle = 90+angle;
				}else{
					angle = angle-90;
				}
			}else{
				if(aX<=dX&&aY>=dY&&chooseCorner==RIGHT_UP_CORNER){
					angle = -90+angle;
				}else if(aX>=dX&&aY<=dY&&chooseCorner==LEFT_BOTTOM_CORNER){
					angle = -90+angle;
				}else{
					angle = angle+90;
				}
			}
			shadowAngle = (float)(Math.atan(lineTwo.getA())*180/Math.PI);
			if(shadowAngle>0){
				shadowAngle = shadowAngle-90;
			}else{
				shadowAngle = shadowAngle+90;
			}
		}
	}
	
	public void setMaskPage(boolean flag){
		isMask = flag;
	}
	
	public void setLookPage(boolean flag){
		isLookPage = flag;
		isMask = !flag;
	}
	
	public void reset(){
		clearPosition();
		angle = 0;
		shadowAngle = 0;
		shadowLength = 0;
		fx = x;
		fy = y;
		sdx = -1;
		sdy = -1;
		cr = -1;
		maskPath = null;
		isMask = true;
		isLookPage = false;
	}
	
	public PageContent getPageContent(){
		return content;
	}
	
	private void clearPosition(){
		aX = -1;
		aY = -1;
		bX = -1;
		bY = -1;
		cX = -1;
		cY = -1;
		dX = -1;
		dY = -1;
	}
}
