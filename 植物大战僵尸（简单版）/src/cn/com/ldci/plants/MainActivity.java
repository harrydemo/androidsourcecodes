package cn.com.ldci.plants;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	GameView gameView;//GameView的引用
	WelcomeView welcomeView;//WelcomeView的引用
	FailView failView;//游戏失败界面的引用
	WinView winView;//欢迎界面的引用
	ProcessView processView;//进度条界面的引用
	public static int nScreenWidth = 0;
	public static int nScreenHeight = 0;
	public static boolean[][] grid = new boolean[9][5];
	/**
	 * 背景表格的x开始坐标
	 */
	public final static float SX = 60 ;
	/**
	 * 背景表格的x结束坐标
	 */
	public final static float EX = 456 ;
	/**
	 * 背景表格的y开始坐标
	 */
	public final static float SY = 40 ;
	/**
	 * 背景表格的y结束坐标
	 */
	public final static float EY = 290 ;
	public static final int XL = 10 ;
	public static final int YL = 10 ;
	/**
	 * 每一个表格的宽度
	 */
	public static final int EXL = 44 ;
	/**
	 * 每一个表格的高度
	 */
	public static final int EYL = 51 ;
	 Handler myHandler = new Handler(){//用来更新UI线程中的控件
		 public void handleMessage(Message msg) {
			 if(msg.what == 1){//游戏失败
	        		if(gameView != null){
	        			gameView.bulletThread.setFlag(false);
	        			gameView.moveThread.setFlag(false);
	        			gameView.ap.flag=false;
	        			gameView = null;
	        		}
	        		initFailView();//切换到FialView
	        	}
	        	else if(msg.what == 2){//切换到GameView
	        		if(welcomeView != null){
	        			welcomeView = null;//释放欢迎界面
	        		}
	        		if(gameView != null){
	        			gameView = null;
	        		}
	        		if(processView != null){
	        			processView = null;//释放加载界面
	        		}
	        		processView = new ProcessView(MainActivity.this,2);//初始化进度条并切换到进度条View
	            	MainActivity.this.setContentView(processView);
	            	new Thread(){//线程
	            		public void run(){
	            			Looper.prepare();
	            			gameView = new GameView(MainActivity.this);//初始化GameView
	            			Looper.loop();
	            		}
	            	}.start();//启动线程
	        		
	        	}
	        	
	        	else if(msg.what == 4){
	        		
	        		toWelcomeView();//切换到WelcomeView界面 
	        	}
	        	else if(msg.what == 5){
	        		if(gameView != null){
	        			gameView.bulletThread.setFlag(false);
	        			gameView.moveThread.setFlag(false);
	        			gameView.ap.flag=false;
	        			gameView = null;
	        		}
	        		initWinView();//切换到WinView界面 
	        	}
	        	else if(msg.what == 6){
	        		toGameView();//去游戏界面
	        	}
	        	
	        	else if(msg.what == 7){
	        		if(welcomeView != null){//释放欢迎界面
	        			welcomeView = null;
	        		}
	        		if(processView != null){//释放加载界面
	        			processView = null;
	        		}
	        		processView = new ProcessView(MainActivity.this,1);//初始化进度条并切换到进度条View
	        		MainActivity.this.setContentView(processView);
	            	new Thread(){//线程
	            		public void run(){//重写的run方法
	            			Looper.prepare();
	            			welcomeView = new WelcomeView(MainActivity.this);//初始化WelcomeView
	            			Looper.loop();
	            		}
	            	}.start();//启动线程
	        	}
	      
		 }
	 };
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    WindowManager windowManager = this.getWindowManager();
		// 屏幕
		Display display = windowManager.getDefaultDisplay();
		nScreenHeight = display.getHeight();
		nScreenWidth = display.getWidth();
	    //全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		processView = new ProcessView(this,1);//初始化进度条并切换到进度条View
    	this.setContentView(processView);//设置加载界面
    	new Thread(){//线程
    		public void run(){
    			Looper.prepare();
    			welcomeView = new WelcomeView(MainActivity.this);//初始化WelcomeView
    			Looper.loop();
    		}
    	}.start();//启动线程
    	initGrid();
	}
	public void toWelcomeView(){//切换到欢迎界面     	
    	this.setContentView(welcomeView);
    }
    public void toGameView(){//初始游戏界面
    	this.setContentView(gameView);
    }
    public void initFailView(){//初始游戏失败界面
    	failView = new FailView(this);
    	this.setContentView(failView);
    }
    public void initWinView(){//初始胜利界面
    	winView = new WinView(this);
    	this.setContentView(winView);
    }
    
    //初始化表格
    private void initGrid(){
		for(int i = 0; i < grid.length ; i++){
			for(int j = 0 ; j < grid[i].length ; j ++){
				grid[i][j] = true ; //true表示能放东西
			}
		}
	}
}
