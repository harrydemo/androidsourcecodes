package yzy.Tank;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	private WelcomeView welcomeView = null;
	private MenuView menuView = null;
	private GameView gameView = null;
	private GameOverView gameOverView = null;
	private VictoryView victoryView = null;
	 MediaPlayer mediaPlayer;//播放背景音乐
	boolean isSound = true;//是否播放声音
	
	Handler myHandler = new Handler() {// 用来更新UI线程中的控件
		public void handleMessage(Message msg) {
			if(msg.what == 1){//把欢迎界面切换成MenuView界面
				toMenuView();
			}
			if(msg.what == 2){
				toGameView();
			}
			if(msg.what == 3){//坦克没血  游戏失败
				togameOverView();
			}
			if(msg.what == 4){//回到欢迎页面
				toWelcomeView();
			}
			if(msg.what ==5){//BOSS没血 显示胜利页面
				toVictoryView();
			}
		}

		
	};
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
        welcomeView = new WelcomeView(this);
        setContentView(welcomeView);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic);//得到背景音乐
        mediaPlayer.setVolume(0.4f, 0.4f);//设置左右音量  0.1-1
    	mediaPlayer.setLooping(true); //循环播放
    	mediaPlayer.start(); //开始播放
    }
    protected void onDestroy() {
    	mediaPlayer.stop();//停止
    	mediaPlayer.release();//释放掉mediaPlayer
    	this.finish();//释放activity
    	System.exit(0);//关闭程序
    	super.onDestroy();
    }
    public void toMenuView() {//切换到MenuView页面
    	menuView = new MenuView(this);
    	this.setContentView(menuView);
	}
    public void toGameView() {
    	gameView = new GameView(this);
    	this.setContentView(gameView);
	}
    public void togameOverView() {
    	gameOverView = new GameOverView(this);
    	this.setContentView(gameOverView);
    }
    public void toWelcomeView() {
    	welcomeView = new WelcomeView(this);
        this.setContentView(welcomeView);
    }
    private void toVictoryView() {
    	victoryView = new VictoryView(this);
    	this.setContentView(victoryView);
		
	}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	mediaPlayer.stop();//停止播放背景音乐
		return super.onKeyDown(keyCode, event);
	}
}