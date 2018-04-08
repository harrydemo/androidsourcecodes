package wyf.wpf;					//声明包语句
import android.app.Activity;		//引入相关类
import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
/*
 * 游戏的主类，负责切换视图，接收和捕获用户的键盘输入并做相应处理。
 * 游戏的欢迎View，加载进度的View和游戏视图View在这里都有引用，可以
 * 切换，通过onTouchEvent方法处理函数来接受用户点击屏幕事件
 */
public class FootballActivity extends Activity{
	View current;				//记录当前View
	GameView gv;				//GameView对象
	WelcomeView welcome;		//欢迎界面
	LoadingView lv;				//进度条加载界面

	int keyState = 0;			//xxxx00为不动，xxxx10为向左,xxxx01为向右
	PlayerMoveThread pmt;		//移动球员位置的线程
	boolean wantSound = true;	//是否播放声音标志位
	int [] layoutArray;			//表示球员球场站位的数组	
	MediaPlayer mpWelcomeMusic;		//游戏开始前的欢迎音乐	
	MediaPlayer mpKick;				//踢球音效
	MediaPlayer mpCheerForWin;		//赢了的音乐
	MediaPlayer mpCheerForLose;		//输了的音乐
	MediaPlayer mpCheerForGoal;		//进球后的音乐
	MediaPlayer mpIce;				//撞到冰山后的音乐
	MediaPlayer mpLargerGoal;		//撞到打开球门后的音乐	
	Rect [] rectPlus;			//代表增加球员按钮的矩形框
	Rect [] rectMinus;			//代表减少球员按钮的矩形框
	Rect rectSound;				//是否播放声音按钮的矩形框
	Rect rectStart;				//开始按钮的矩形框
	Rect rectQuit;				//退出按钮的矩形框
	Rect rectGallery;			//表示Gallery的矩形框	
	int [] imageIDs ={			//存放8个俱乐部的图片ID
			R.drawable.club_1,
			R.drawable.club_2,
			R.drawable.club_3,
			R.drawable.club_4,
			R.drawable.club_5,
			R.drawable.club_6, 
			R.drawable.club_7,
			R.drawable.club_8
		};
	int clubID = imageIDs[0];		//记录用户选择的俱乐部的ID	
    @Override
    public void onCreate(Bundle savedInstanceState) {			//重写onCreate方法
        super.onCreate(savedInstanceState);         
        initWelcomeSound(this);       //初始化声音库      
        requestWindowFeature(Window.FEATURE_NO_TITLE);		 	 //设置全屏
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN
        		);        
        welcome = new WelcomeView(this);				//将屏幕切到欢迎界面
        setContentView(welcome);
        current = welcome;
        if(wantSound && mpWelcomeMusic!=null){			//如需要，播放相应声音
        	mpWelcomeMusic.start();
        }        
        initRects();		//初始化用于匹配点击事件的矩形框
    }
    //方法：初始化欢迎界面的声音
    public void initWelcomeSound(Context context){
		mpWelcomeMusic = MediaPlayer.create(context, R.raw.music);	
    }
    //方法：初始化矩形框
    public void initRects(){
    	rectPlus = new Rect[3];
    	rectMinus = new Rect[3];
    	for(int i=0;i<3;i++){
    		rectPlus[i] = new Rect(244,200+40*i,280,236+40*i);
    		rectMinus[i] = new Rect(280,200+40*i,316,236+40*i);
    	}
    	rectSound = new Rect(135,370,185,420);
    	rectStart = new Rect(205,425,295,475);
    	rectQuit = new Rect(25,425,115,475);
    	rectGallery = new Rect(10,10,310,110);
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {//重写onTouchEvent方法
		if(event.getAction()== MotionEvent.ACTION_UP){//判断事件类型
			int x = (int)event.getX();		//获得点击处的X坐标
			int y = (int)event.getY();		//获得点击处的Y坐标				
			if(current == welcome){//如果当前界面是欢迎界面
				if(rectGallery.contains(x, y)){			//用户点击的是Gallery
					welcome.cg.galleryTouchEvnet(x, y);		//交给Gallery来处理点击事件
				}
				else if(rectSound.contains(x, y)){		//点下的是声音选项
					this.wantSound = !this.wantSound;	//更改声音选项
					return true;
				}
				else if(rectStart.contains(x, y)){		//点下开始键
					if(checkLayout(welcome.layout)){		//检查玩家选择的布局是否正确
						layoutArray = welcome.layout;		//获得玩家选择站位布局								
						lv = new LoadingView(this);			//创建读取进度View
						this.setContentView(lv);			//将屏幕设为读取进度的LoadingView
						this.current = lv;					//记录当前View
						lv.lt.start();						//启动LoadingView的刷屏线程
						new Thread(){						//启动一个新线程，在其中创建GameView对象
							public void run(){
								Looper.prepare();
								if(wantSound){
									initSound();//初始化声音
								}									
								//创建
								gv = new GameView(FootballActivity.this,imageIDs[welcome.cg.currIndex]);//创建游戏界面	
								lv.progress = 100;
								welcome = null;			//释放掉WelcomeView	
							}
						}.start();
					}
				}
				else if(rectQuit.contains(x,y)){		//按下退出键
					System.exit(0);						//程序退出
				}
				else{									//检查是否按下了修改队员站位的加号和减号按钮
					for(int i=0;i<3;i++){
						if(rectPlus[i].contains(x,y)){	//如果有加号按钮点下，就增加对应进攻防守线上人数
							//如果有富余的人再加
							if(welcome.layout[0]+welcome.layout[1]+welcome.layout[2] <10){	
								welcome.layout[i]++;
							}								
							break;
						}
						if(rectMinus[i].contains(x, y)){//如果有减号按钮点下，就减少相应人数
							if(welcome.layout[i] > 0){	//如果该处人数不为零，就减少一个
								welcome.layout[i]--;			
							}								
							break;
						}
					}					
				}				
			}
			else if(current == gv){				//如果当前显示的View为GameView
				if(gv.rectMenu.contains(x,y)){	//如果点下了菜单按钮
					gv.isShowDialog = true;		//设置显示对话框
					gv.ball.isPlaying = false;	//足球停止移动
					pmt.flag = false;				//使PlayerMoveThread空转
				}
				else if(gv.rectYesToDialog.contains(x,y)){		//如果点下的是对话框中的”是“按钮
					if(gv.isShowDialog){							//检查对话框是不是正在显示
				        welcome = new WelcomeView(this);			//新建一个WelcomeView
				        setContentView(welcome);					//设置当前屏幕为WelcomeView
				        welcome.status = 3;							//直接设为待命状态
				        current = welcome;							//记录当前屏幕
				        gv = null;								//将GameView指向的对象声明为垃圾
				        if(wantSound && mpWelcomeMusic!=null){								//如需要，播放声音
				        	mpWelcomeMusic.start();
				        } 
					}
				}
				else if(gv.rectNoToDialog.contains(x,y)){		//如果点下的是对话框中的”否“按钮
					if(gv.isShowDialog){							//检查对话框是不是正在显示
						gv.isShowDialog = false;					//不显示对话框
						pmt.flag = true;							//设置双方球员可移动
						gv.ball.isPlaying = true;				//设置足球可移动
					}
				}
			}	
			else if(current == lv){									//如果当前屏幕为LoadingView
				if(lv.progress == 100){								//如果进度达到100%
					setContentView(gv);							//屏幕切换到GameView
					current = gv;								//记录当前View
					lv = null;										//lv指向的对象声明为垃圾
					if(mpWelcomeMusic.isPlaying()){					//如需要，播放相应声音
						mpWelcomeMusic.stop();
					}	
					gv.startGame();								//开始游戏
				}
			}
		}
		return true;
	}
	//方法：加载游戏中用到的声音
	public void initSound(){			
		mpKick = MediaPlayer.create(this, R.raw.kick);
		updateProgressView();//更新进度条
		mpCheerForWin = MediaPlayer.create(this, R.raw.cheer_win);
		updateProgressView();//更新进度条
		mpCheerForLose = MediaPlayer.create(this, R.raw.cheer_lose);
		updateProgressView();//更新进度条
		mpCheerForGoal = MediaPlayer.create(this, R.raw.cheer_goal);	
		updateProgressView();//更新进度条
		mpLargerGoal = MediaPlayer.create(this, R.raw.lager_goal);
		updateProgressView();//更新进度条
		mpIce = MediaPlayer.create(this, R.raw.ice);
		updateProgressView();//更新进度条
    }	
	//更新进度条的进度
    public void updateProgressView(){
    	lv.progress+=15;
    }	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//处理键盘按下事件的回调方法
		switch(keyCode){
		case 21:					//左
			keyState = keyState | 2;
			keyState = keyState & 0xfffffffe;		//清除掉其他的键盘状态
			break;
		case 22:					//右
			keyState = keyState | 1;
			keyState = keyState & 0xffffffd;		//清除掉其他的键盘状态
			break;
		default:
			break;
		}
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {	//处理键盘抬起事件的回调方法
		switch(keyCode){
		case 21:					//左
			keyState = keyState & 0xffffffd;		//清楚该状态位
			break;
		case 22:					//右
			keyState = keyState & 0xfffffffe;		//清楚该状态位
			break;
		default:
			break;
		}
		return true;
	}
	//检查用户输入的layout合不合法
	public boolean checkLayout(int [] layout){
		int sum=0;
		for(int i=0;i<layout.length;i++){	//遍历存放球员站位的数组
			if(layout[i]<0){				//如果发现某个进攻/防守阵线上的球员为负数
				return false;
			}
			else{
				sum+=layout[i];		//将各个阵线上的球员个数相加
			}
		}
		if(sum == 10){						//如果和为10，则该站位合法
			return true;
		}
		else{
			return false;			//返回false
		}
	}

}