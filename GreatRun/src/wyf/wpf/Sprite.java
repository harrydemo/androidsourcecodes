package wyf.wpf;			//声明包语句
import static wyf.wpf.ConstantUtil.*;		//引入相关类
import java.util.ArrayList;					//引入相关类
import android.graphics.Canvas;				//引入相关类
/*
 * 该类对象代表游戏中在平铺层地图上移动的精灵。玩家控制的英雄和追赶玩家的怪物均继承自此类
 * 该类中主要包括精灵的动画段列表，该列表中包含精灵所有的动画片段的图片数组对象，并且可以
 * 通过成员方法来添加或删除。同时还有成员变量记录当前动画段以及当前动画段的动画帧，成员
 * 方法nextFrame用来修改当前动画段的当前动画帧实现动画效果。内部线程用于定时调用nextFrame方法。
 */
public class Sprite{
	ArrayList<int[]> animationSegmentList = new ArrayList<int[]>();	//动画段列表，包括向上，向下等动画段
	//当前的动画段在列表中的索引。静止：0下,1左，2右，3上，移动：4下，5左，6右，7上
	int currentSegment;							//当前动画段的索引						
	int currentFrame;								//当前动画段的动画帧索引
	int x;		//精灵在大地图的坐标
	int y;		//精灵在大地图的坐标
	int col;	//精灵在地图上的列数，通过求靠下31×31方块的中心所在的地方求出
	int row;	//精灵在地图上的行数，通过求靠下31×31方块的中心所在的地方求出
	SpriteThread st;
	//构造器
	public Sprite(int col,int row){
		this.col = col;
		this.row = row;
		this.x = col * TILE_SIZE;								//x坐标对应于图片左上角
		this.y = row * TILE_SIZE+TILE_SIZE-SPRITE_HEIGHT;		//y坐标对应于图片左上角
		st = new SpriteThread(this);
	}
	//修改当前动画段的当前动画帧
	public void nextFrame(){
			int [] currSeg = animationSegmentList.get(currentSegment);
			currentFrame = (currentFrame + 1)%currSeg.length;		
	}
	//方法：制作自己的动画段列表
	public void makeAnimation(int [][] imgId){
		for(int [] imgIDs:imgId){
			addAnimationSegment(imgIDs);
		}
	}
	//方法：向动画段列表中添加动画段
	public void addAnimationSegment(int [] ani){
		this.animationSegmentList.add(ani);		//向动画段列表中添加动画段
	}
	//方法：设置动画段
	public void setAnimationSegment(int segment){
		this.currentSegment = segment;		//设置动画段
		this.currentFrame = 0;				//从头开始循环帧
	}
	//方法：将自己在屏幕上画出
	public void drawSelf(Canvas canvas,int screenX,int screenY){
		int imgId = animationSegmentList.get(currentSegment)[currentFrame];	//获取要绘制的那个帧
		BitmapManager.drawGamePublic(imgId, canvas, screenX, screenY);		//调用BitmapManager的静态方法绘制图片
	}
	//方法：开启动画
	public void startAnimation(){
		st.isGameOn = true;
		if(!st.isAlive()){
			st.start();
		}
	}
	//方法：暂停动画
	public void pauseAnimation(){
		st.isGameOn = false;
	}
	//方法：销毁动画线程
	public void stopAnimation(){
		st.flag = false;
		st.isGameOn = false;
	}
}