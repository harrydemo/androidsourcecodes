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
	 MediaPlayer mediaPlayer;//���ű�������
	boolean isSound = true;//�Ƿ񲥷�����
	
	Handler myHandler = new Handler() {// ��������UI�߳��еĿؼ�
		public void handleMessage(Message msg) {
			if(msg.what == 1){//�ѻ�ӭ�����л���MenuView����
				toMenuView();
			}
			if(msg.what == 2){
				toGameView();
			}
			if(msg.what == 3){//̹��ûѪ  ��Ϸʧ��
				togameOverView();
			}
			if(msg.what == 4){//�ص���ӭҳ��
				toWelcomeView();
			}
			if(msg.what ==5){//BOSSûѪ ��ʾʤ��ҳ��
				toVictoryView();
			}
		}

		
	};
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ��Ϊ����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//���ó�ȫ��ģʽ
        welcomeView = new WelcomeView(this);
        setContentView(welcomeView);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic);//�õ���������
        mediaPlayer.setVolume(0.4f, 0.4f);//������������  0.1-1
    	mediaPlayer.setLooping(true); //ѭ������
    	mediaPlayer.start(); //��ʼ����
    }
    protected void onDestroy() {
    	mediaPlayer.stop();//ֹͣ
    	mediaPlayer.release();//�ͷŵ�mediaPlayer
    	this.finish();//�ͷ�activity
    	System.exit(0);//�رճ���
    	super.onDestroy();
    }
    public void toMenuView() {//�л���MenuViewҳ��
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
    	mediaPlayer.stop();//ֹͣ���ű�������
		return super.onKeyDown(keyCode, event);
	}
}