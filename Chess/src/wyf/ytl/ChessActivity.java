package wyf.ytl;

import android.app.Activity;//������صİ�
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class ChessActivity extends Activity {
	boolean isSound = true;//�Ƿ񲥷�����
	MediaPlayer startSound;//��ʼ�Ͳ˵�ʱ������
	MediaPlayer gamesound;//��Ϸ����
	
	Handler myHandler = new Handler(){//��������UI�߳��еĿؼ�
        public void handleMessage(Message msg) {
        	if(msg.what == 1){	//WelcomeView��HelpView��GameView��������Ϣ���л���MenuView
        		initMenuView();//��ʼ�����л����˵�����
        	}
        	else if(msg.what == 2){//MenuView��������Ϣ���л���GameView
        		initGameView();//��ʼ�����л�����Ϸ����
        	}
        	else if(msg.what == 3){//MenuView��������Ϣ���л���HelpView
        		initHelpView();//��ʼ�����л�����������
        	}
        }
	}; 	
    public void onCreate(Bundle savedInstanceState) {//��д��onCreate
        super.onCreate(savedInstanceState);
		//ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		startSound = MediaPlayer.create(this, R.raw.startsound);//���ػ�ӭ����
		startSound.setLooping(true);//������Ϸ����ѭ������ 
		gamesound  = MediaPlayer.create(this, R.raw.gamesound);//��Ϸ���̵ı�������
		gamesound.setLooping(true);//������Ϸ����ѭ������ 
        this.initWelcomeView();//��ʼ����ӭ����
    }
    public void initWelcomeView(){//��ʼ����ӭ���� 
    	this.setContentView(new WelcomeView(this,this));//�л�����ӭ����
    	if(isSound){//��Ҫ��������ʱ
    		startSound.start();//��������
		}
    }
    
    public void initGameView(){//��ʼ����Ϸ����
    	this.setContentView(new GameView(this,this)); //�л�����Ϸ����
    }
    
    public void initMenuView(){//��ʼ���˵�����
    	if(startSound != null){//ֹͣ
    		startSound.stop();//ֹͣ��������
    		startSound = null;
    	}
    	if(this.isSound){//�Ƿ񲥷�����
    		gamesound.start();//��������
    	}
    	this.setContentView(new MenuView(this,this));//�л�View
    } 
    public void initHelpView(){//��ʼ����������
    	this.setContentView(new HelpView(this,this));//�л�����������
    }
}