package wyf.wpf;							//声明包语句
import static wyf.wpf.ConstantUtil.*;		//引入相关类
import android.content.res.Resources;		//引入相关类
import android.graphics.Bitmap;				//引入相关类
import android.graphics.BitmapFactory;		//引入相关类
import android.graphics.Canvas;				//引入相关类
import android.graphics.Paint;				//引入相关类
/*
 * 该类提供一些静态方法和成员，封装了游戏用到的所有图片资源，防止引用外泄，统一管理，易于维护
 */
public class BitmapManager{
	private static Bitmap[] currentStage;//存放当前关卡的图片资源
	private static Bitmap[] welcomePublic;//存放系统总要用到的图片资源
	private static Bitmap[] gamePublic;	//存放游戏中总要用到的图片资源
	private static Bitmap[] systemPublic;	//存放系统要用到的图片
	private static int [][] heroAniList;	//存放英雄的动画段列表
	
	
	//锁定构造器
	private BitmapManager(){
	}
	//方法：加载系统用到的图片资源
	public static void loadSystemPublic(Resources r){
		systemPublic = new Bitmap[SYSTEM_BITMAP_LENGTH];
		systemPublic[0] = BitmapFactory.decodeResource(r, R.drawable.loading);
	}
	//方法：加载欢迎界面要用到的图片资源
	public static void loadWelcomePublic(Resources r){
		welcomePublic = new Bitmap[WELCOME_BITMAP_LENGTH];
		welcomePublic[0] = BitmapFactory.decodeResource(r, R.drawable.welcome_back);//欢迎界面背景
		}
	//方法：加载关卡图片，在每一关开始前调用，关数从0开始
	public static void loadCurrentStage(Resources r,int stage){
		currentStage = null;
		currentStage = new Bitmap[STAGE_BITMAP_LENGTH[stage]];
		switch(stage){
		case 0://第一关地图图片
			currentStage[0] = BitmapFactory.decodeResource(r, R.drawable.road_1);//第一关路
			currentStage[1] = BitmapFactory.decodeResource(r, R.drawable.grass_1);//第一关草地
			currentStage[2] = BitmapFactory.decodeResource(r, R.drawable.stake_1);//第一关木桩
			currentStage[3] = BitmapFactory.decodeResource(r, R.drawable.tree_1);//第一关树
			currentStage[4] = BitmapFactory.decodeResource(r, R.drawable.flower_1);//第一关花 		
			break;
		
		}
	}
	//方法：游戏界面中的公共图片，如英雄,家等
	public static void loadGamePublic(Resources r){
		gamePublic = new Bitmap[GAME_BITMAP_LENGTH];
		gamePublic[0] = BitmapFactory.decodeResource(r, R.drawable.game_pass);//游戏过关图片		
		
		Bitmap tmp = BitmapFactory.decodeResource(r, R.drawable.hero);		//加载英雄大图片 
		heroAniList = cutBitmap(tmp,gamePublic,3,8,2);						//切割英雄大图片
		
		gamePublic[35] = BitmapFactory.decodeResource(r, R.drawable.help_view);//帮助图片
		gamePublic[36] = BitmapFactory.decodeResource(r, R.drawable.home);		//加载家图片
	}
	//方法：根据行数和列数切割大图
	public static int [][] cutBitmap(Bitmap source,Bitmap [] result,int start,int rows,int cols){
		int [][] aniList = new int[rows][cols];
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				gamePublic[start+i*cols+j] = Bitmap.createBitmap(source, j*SPRITE_WIDTH, i*SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
				aniList[i][j] = start+i*cols+j;		//存储图片的下标，即ID
			}
		}
		return aniList;
	}
	//方法：根据图片ID绘制系统公共图片
	public static void drawSystemPublic(int imgId,Canvas canvas,int x,int y,Paint paint){
		canvas.drawBitmap(systemPublic[imgId], x, y, paint);
	}
	//方法：根据图片ID绘制系统欢迎界面图片
	public static void drawWelcomePublic(int imgId,Canvas canvas,int x,int y,Paint paint){
		canvas.drawBitmap(welcomePublic[imgId], x, y, paint);
	}
	//方法：根据图片ID绘制游戏中的公共图片
	public static void drawGamePublic(int imgId,Canvas canvas,int x,int y){
		canvas.drawBitmap(gamePublic[imgId], x, y, null);
	}
	//方法：根据图片ID绘制关卡图片
	public static void drawCurrentStage(int imgId,Canvas canvas,int x,int y){
		canvas.drawBitmap(currentStage[imgId], x, y, null);
	}
	//方法：获得英雄的动画段列表
	public static int[][] getHeroFrmList() {
		return heroAniList;
	}
	
}