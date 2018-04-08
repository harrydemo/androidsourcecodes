package wyf.wpf;			//声明包语句
import android.graphics.Bitmap;		//引入相关类
import android.graphics.Canvas;		//引入相关类
import android.graphics.Color;		//引入相关类
import android.graphics.Paint;			//引入相关类
/*
 * 该类为自定义的gallery，为实现Gallery的效果
 */
public class CustomGallery{
	Bitmap [] bmpContent;		//Gallery要显示的内容图片
	int length;					//Gallery要显示的图片数组大小
	int currIndex;				//当前被显示的图片的索引
	int startX;					//绘制Gallery时其左上角在屏幕中的X坐标
	int startY;					//绘制Gallery时其左上角在屏幕中的Y坐标
	int cellWidth;				//每个图片的宽度
	int cellHeight;				//每个图片的高度	
	//构造器，初始化主要成员变量
	public CustomGallery(int startX,int startY,int cellWidth,int cellHeight){
		this.startX = startX;
		this.startY = startY;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}	
	public void setContent(Bitmap [] bmpContent){	//方法：为Gallery设置显示内容
		this.bmpContent = bmpContent;
		this.length = bmpContent.length;
	}	
	public void setCurrent(int index){				//方法：设置当前显示的图片
		if(index >=0 && index < length){
			this.currIndex = index;
		}
	}	
	public void drawGallery(Canvas canvas,Paint paint){//方法：绘制自己
		//创建背景的画笔
		Paint paintBack = new Paint();
		paintBack.setARGB(220, 99, 99, 99);	
		//创建边框的画笔
		Paint paintBorder = new Paint();   
		paintBorder.setStyle(Paint.Style.STROKE);   
		paintBorder.setStrokeWidth(4.5f);
		paintBorder.setARGB(255, 150, 150, 150);
		//画左边的图片
		if(currIndex >0){
			canvas.drawRect(startX, startY, startX+cellWidth, startY+cellHeight, paintBack);	//背景
			canvas.drawBitmap(bmpContent[currIndex-1], startX, startY, paint);					//贴图片			
			canvas.drawRect(startX, startY, startX+cellWidth, startY+cellHeight, paintBorder);	//画左边图片的边框
		}		
		//画被选中的图片
		canvas.drawRect(startX+cellWidth, startY, startX+cellWidth*2, startY+cellHeight, paintBack);	//背景
		canvas.drawBitmap(bmpContent[currIndex], startX+cellWidth, startY, paint);						//贴图片	
		//画右边的图片
		if(currIndex<length-1){
			canvas.drawRect(startX+cellWidth*2, startY, startX+cellWidth*3, startY+cellHeight, paintBack);	//背景
			canvas.drawBitmap(bmpContent[currIndex+1], startX+cellWidth*2, startY, paint);					//贴图片			
			paintBorder.setARGB(255, 150, 150, 150);														//画右边图片的边框
			canvas.drawRect(startX+cellWidth*2, startY, startX+cellWidth*3, startY+cellHeight, paintBorder);
		}
		//画选中的边框	
		paintBorder.setColor(Color.RED);		
		canvas.drawRect(startX+cellWidth, startY, startX+cellWidth*2, startY+cellHeight, paintBorder);
	}	
	public void galleryTouchEvnet(int x,int y){		//方法：Gallery的处理点击事件方法
		if(x>startX && x<startX+cellWidth){						//点在了左边那张图片
			if(currIndex > 0){					//判断当前图片的左边还有没有图片
				currIndex --;					//设置当前图片为左边的图片
			}
		}
		else if(x>startX+cellWidth*2 && x<startX+cellWidth*3){		//点在了右边那张图片
			if(currIndex < length-1){			//判断当前图片的右边还有没有图片
				currIndex++;					//设置当前图片为右边的图片
			}
		}
	}
}