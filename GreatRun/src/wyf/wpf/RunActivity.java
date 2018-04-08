package wyf.wpf;						//声明包语句
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class RunActivity extends Activity {
	View currentView;					//记录当前View
	WelcomeView wv;						//WelcomeView引用
	GameView gv;						//GameView类引用
	ProgressView pv;					//ProgressView对象引用
	HelpView hv;						//HelpView对象引用
	MediaPlayer mMediaPlayer;
	//boolean wantSound=true;			//是否开启声音,默认不开
	int keyState;			//记录键盘状态，1，2，4，8代表上下左右
	
	//创建自定义的Handler
	Handler myHandler = new Handler(){					//创建自定义的Handler
		public void handleMessage(Message msg) {			//重写处理消息的方法
			switch(msg.what){							//判断Message对象的类型
			case 0:									//切换到开始游戏前的进度条界面
				pv = new ProgressView(RunActivity.this, 4);	//target为0，进度条走完后去Gameview
				setContentView(pv);						//切换屏幕到ProgressView
				currentView = pv;						//记录当前View
				wv = null;								//声明WelcomeView对象为null
				new Thread(){ 							//创建并启动一个新线程
					public void run(){
						Looper.prepare();
						BitmapManager.loadGamePublic(getResources());	//加载游戏界面图片资源
					gv = new GameView(RunActivity.this);			//创建GameView对象
						pv.progress=100;//更新进度条
					}
				}.start();
				break;
			case 4:									//开始游戏
				setContentView(gv);						//切换屏幕到GameView
				currentView = gv;						//记录当前View
				pv = null;								//声明pv指向的ProgressView的
				break;
			case 1:									//显示帮助
				hv = new HelpView(RunActivity.this);			//创建HelpView对象
				setContentView(hv);						//切换屏幕到HelpView
				currentView = hv;						//记录当前View
				break;
			case 2:									//退出游戏
				android.os.Process.killProcess(android.os.Process.myPid());
//				System.exit(0);							//退出程序
				break;
			case 3:									//显示欢迎界面
				setContentView(wv);						//设置当前屏幕为WelcomeView对象
				currentView = wv;						//记录当前View
				pv = null;								//将pv指向的ProgressView声明为垃圾
				break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BitmapManager.loadSystemPublic(getResources());
        pv = new ProgressView(this, 3);					//创建一个ProgressView对象，目标为3，即走满后去WelcomeView
        setContentView(pv);
        currentView = pv;
    	new Thread(){
    		public void run(){
    			Looper.prepare();
    			BitmapManager.loadWelcomePublic(getResources());	//加载欢迎界面的图片资源
    			wv = new WelcomeView(RunActivity.this);//初始化WelcomeView
    			pv.progress=100;
    		}
    	}.start();
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			int x = (int)event.getX();			//获取屏幕点击处的X坐标
			int y = (int)event.getY();			//获取屏幕点击处的Y坐标
			if(currentView == wv){				//如果当前View为WelcomeView
				return wv.myTouchEvent(x, y);	//调用GameView的相关事件处理方法
			}
			else if(currentView == gv){			//如果当前View为GameView
				return gv.myTouchEvent(x, y);	//调用GameView的相关事件处理方法
			}
		}
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		//重写onKeyDown方法
		switch(keyCode){
		case 19:			//向上
			keyState = keyState | 0x1;
			keyState = keyState & 0x1;//屏蔽掉同时按下的键
			break;
		case 20:			//向下
			keyState = keyState | 0x2;
			keyState = keyState & 0x2;//屏蔽掉同时按下的键
			break;
		case 21:			//向左
			keyState = keyState | 0x4;
			keyState = keyState & 0x4;//屏蔽掉同时按下的键
			break;
		case 22:			//向右
			keyState = keyState | 0x8;
			keyState = keyState & 0x8;//屏蔽掉同时按下的键
			break;
		default:
			break;
		}
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {		//重写onKeyUp方法
		if(currentView == gv){
			
		}
		else if(currentView == hv){//是帮助界面时按下键
			if(keyCode == 4){//按下的是否是返回键
				setContentView(wv); //设置当前屏幕为HelpView
				currentView = wv;
			}
		}
		return true;
	}
}