package wyf.wpf;										//声明包语句
import android.content.Context;							//引入相关类
import android.content.res.Resources;					//引入相关类
import android.graphics.Bitmap;							//引入相关类
import android.graphics.BitmapFactory;					//引入相关类
import android.graphics.Canvas;							//引入相关类
import android.graphics.Color;							//引入相关类
import android.graphics.Matrix;							//引入相关类
import android.graphics.Paint;							//引入相关类
import android.view.SurfaceHolder;						//引入相关类
import android.view.SurfaceView;						//引入相关类
/*
 * 该类继承自View，实现欢迎动画的播放，以及主菜单的显示
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback{
	WelcomeThread wt;				//后台修改数据线程
	WelcomeDrawThread wdt;			//后台重绘线程
	FootballActivity father;		//Activity的引用
	int index = 0;					//开场3个动画帧的索引
	int status = -1;				//0代表足球动画，1代表背景转进来，2代表全部渐显，3代表待命
	int alpha = 255;				//透明度，初始为255，即不透明
	int [] layout = {3,3,4};		//玩家球员的站位数组，3个值分别代表前场、中场、后场
	CustomGallery cg;				//自定义的Gallery类，用于选择俱乐部logo	
	Bitmap [] bmpLayout;			//代表前场、中场、后场3个阵线的图片数组
	Bitmap bmpPlus;					//加号图片
	Bitmap bmpMinus;  				//减号图片	
	Bitmap bmpPlayer;				//玩家图片
	Bitmap [] bmpSound;				//声音开关图片数组
	Bitmap bmpStart;				//开始按钮图片
	Bitmap bmpQuit;					//退出按钮图片
	Bitmap [] bmpGallery;			//存储Gallery对象要显示的内容
	Bitmap [] bmpAnimaition;		//存储欢迎动画帧的数组
	Bitmap bmpBack;					//背景图片
	Matrix matrix;					//Matrix对象，用来旋转背景图
	//构造器：初始化成员变量
	public WelcomeView(FootballActivity father) {
		super(father);
		this.father = father;
		getHolder().addCallback(this);	
		initBitmap(father);							//初始化图片
		matrix = new Matrix();						//创建Matrix对象
		cg = new CustomGallery(10,10,100,100);		//创建CustomGallery对象
		cg.setContent(bmpGallery);					//为CustomGallery对象设置显示内容
		cg.setCurrent(2);							//设置CustomGallery当前显示的图片
		wt = new WelcomeThread(this);				//创建WelcomeThread对象
		wdt = new WelcomeDrawThread(this,getHolder());			//创建WelcomeDrawThread对象
		status = 0;												//设置初始状态值为0
	}		
	public void initBitmap(Context context){//初始化图片
		Resources r = context.getResources();			//获取Resources对象
		bmpBack = BitmapFactory.decodeResource(r, R.drawable.welcome);	//创建背景图片
		bmpLayout = new Bitmap[3];//创建表示前场、中场、后场的图片数组
		bmpLayout[0] = BitmapFactory.decodeResource(r, R.drawable.fwd_field);
		bmpLayout[1] = BitmapFactory.decodeResource(r, R.drawable.mid_field);
		bmpLayout[2] = BitmapFactory.decodeResource(r, R.drawable.bck_field);
		bmpPlus = BitmapFactory.decodeResource(r, R.drawable.plus);		//创建加号图片
		bmpMinus = BitmapFactory.decodeResource(r, R.drawable.minus);	//创建减号图片
		bmpPlayer = BitmapFactory.decodeResource(r, R.drawable.player20);	//创建球员图片
		bmpSound = new Bitmap[2];											//创建声音开关图片数组
		bmpSound[0] = BitmapFactory.decodeResource(r, R.drawable.sound1);	
		bmpSound[1] = BitmapFactory.decodeResource(r, R.drawable.sound2);
		bmpStart = BitmapFactory.decodeResource(r, R.drawable.start);		//创建开始图片按钮
		bmpQuit = BitmapFactory.decodeResource(r, R.drawable.quit);			//创建开始图片按钮
		bmpAnimaition = new Bitmap[3];										//创建动画数组
		bmpAnimaition[0] = BitmapFactory.decodeResource(r, R.drawable.p1);
		bmpAnimaition[1] = BitmapFactory.decodeResource(r, R.drawable.p2);
		bmpAnimaition[2] = BitmapFactory.decodeResource(r, R.drawable.p3);
		//初始化Gallery的图片资源   
		bmpGallery = new Bitmap[8];											//创建自定义Gallery要显示的内容图片数组
		for(int i=0;i<bmpGallery.length;i++){
			bmpGallery[i] = BitmapFactory.decodeResource(r, father.imageIDs[i]);
		}		 
	}	
	public void doDraw(Canvas canvas) {//方法：用于根据不同状态绘制屏幕
		Paint paint = new Paint();		//创建画笔
		switch(status){
		case 0://显示3个动画帧
			canvas.drawBitmap(bmpAnimaition[index], 0, 0, null);
			break;
		case 1://背景图片旋转而进
			canvas.drawColor(Color.BLACK);		//清屏幕
			Bitmap bmpTemp = Bitmap.createBitmap(bmpBack, 0, 0,
					bmpBack.getWidth(), bmpBack.getHeight(), matrix, true);//旋转背景图
			canvas.drawBitmap(bmpTemp, 0, 0, null);			//绘制背景图
			break;
		case 2://全场透明
		case 3://全场待命--------------这两个画法一样，只是透明度不同
			canvas.drawColor(Color.BLACK);			//清屏幕
			paint.setAlpha(alpha);					//设置透明度
			canvas.drawBitmap(bmpBack, 0, 0, paint);//画背景
			cg.drawGallery(canvas,paint);		//画自定义的Gallery
			for(int i=0;i<layout.length;i++){	//对于球场上各个阵线上的信息进行绘制
				canvas.drawBitmap(bmpLayout[i], 0, 200+40*i, paint);//绘制阵线名称即前场、中场、后场
				for(int j=0;j<layout[i];j++){
					canvas.drawBitmap(bmpPlayer, 65+j*18, 205+40*i, paint);	//根据阵线上人数绘制球员
				}
				canvas.drawBitmap(bmpPlus, 244, 200+40*i, paint);			//绘制加号按钮
				canvas.drawBitmap(bmpMinus, 280, 200+40*i, paint);			//绘制减号按钮
			}
			canvas.drawBitmap(bmpSound[father.wantSound?0:1], 135, 370, paint);	//绘制声音开关
			canvas.drawBitmap(bmpStart, 205, 425, paint);						//绘制开始按钮
			canvas.drawBitmap(bmpQuit, 25, 425, paint);							//绘制退出按钮
			break;
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {										//重写surfaceChanged方法
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {			//重写surfaceCreated方法
		if(!wt.isAlive()){			//启动后台修改数据线程
			wt.start();
		}
		if(!wdt.isAlive()){			//启动后台绘制线程
			wdt.start();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {		//重写surfaceDestroyed方法
		if(wt.isAlive()){				//停止后台修改数据线程
			wt.isWelcoming = false;
		}
		if(wdt.isAlive()){				//停止后台绘制线程
			wdt.flag = false;
		}		
	}	
}