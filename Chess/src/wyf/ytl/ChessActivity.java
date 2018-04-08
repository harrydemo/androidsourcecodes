package wyf.ytl;

import android.app.Activity;//引入相关的包
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class ChessActivity extends Activity {
	boolean isSound = true;//是否播放声音
	MediaPlayer startSound;//开始和菜单时的音乐
	MediaPlayer gamesound;//游戏声音
	
	Handler myHandler = new Handler(){//用来更新UI线程中的控件
        public void handleMessage(Message msg) {
        	if(msg.what == 1){	//WelcomeView或HelpView或GameView传来的消息，切换到MenuView
        		initMenuView();//初始化并切换到菜单界面
        	}
        	else if(msg.what == 2){//MenuView传来的消息，切换到GameView
        		initGameView();//初始化并切换到游戏界面
        	}
        	else if(msg.what == 3){//MenuView传来的消息，切换到HelpView
        		initHelpView();//初始化并切换到帮助界面
        	}
        }
	}; 	
    public void onCreate(Bundle savedInstanceState) {//重写的onCreate
        super.onCreate(savedInstanceState);
		//全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		startSound = MediaPlayer.create(this, R.raw.startsound);//加载欢迎声音
		startSound.setLooping(true);//设置游戏声音循环播放 
		gamesound  = MediaPlayer.create(this, R.raw.gamesound);//游戏过程的背景声音
		gamesound.setLooping(true);//设置游戏声音循环播放 
        this.initWelcomeView();//初始化欢迎界面
    }
    public void initWelcomeView(){//初始化欢迎界面 
    	this.setContentView(new WelcomeView(this,this));//切换到欢迎界面
    	if(isSound){//需要播放声音时
    		startSound.start();//播放声音
		}
    }
    
    public void initGameView(){//初始化游戏界面
    	this.setContentView(new GameView(this,this)); //切换到游戏界面
    }
    
    public void initMenuView(){//初始化菜单界面
    	if(startSound != null){//停止
    		startSound.stop();//停止播放声音
    		startSound = null;
    	}
    	if(this.isSound){//是否播放声音
    		gamesound.start();//播放声音
    	}
    	this.setContentView(new MenuView(this,this));//切换View
    } 
    public void initHelpView(){//初始化帮助界面
    	this.setContentView(new HelpView(this,this));//切换到帮助界面
    }
}