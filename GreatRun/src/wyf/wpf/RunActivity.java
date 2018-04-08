package wyf.wpf;						//���������
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
	View currentView;					//��¼��ǰView
	WelcomeView wv;						//WelcomeView����
	GameView gv;						//GameView������
	ProgressView pv;					//ProgressView��������
	HelpView hv;						//HelpView��������
	MediaPlayer mMediaPlayer;
	//boolean wantSound=true;			//�Ƿ�������,Ĭ�ϲ���
	int keyState;			//��¼����״̬��1��2��4��8������������
	
	//�����Զ����Handler
	Handler myHandler = new Handler(){					//�����Զ����Handler
		public void handleMessage(Message msg) {			//��д������Ϣ�ķ���
			switch(msg.what){							//�ж�Message���������
			case 0:									//�л�����ʼ��Ϸǰ�Ľ���������
				pv = new ProgressView(RunActivity.this, 4);	//targetΪ0�������������ȥGameview
				setContentView(pv);						//�л���Ļ��ProgressView
				currentView = pv;						//��¼��ǰView
				wv = null;								//����WelcomeView����Ϊnull
				new Thread(){ 							//����������һ�����߳�
					public void run(){
						Looper.prepare();
						BitmapManager.loadGamePublic(getResources());	//������Ϸ����ͼƬ��Դ
					gv = new GameView(RunActivity.this);			//����GameView����
						pv.progress=100;//���½�����
					}
				}.start();
				break;
			case 4:									//��ʼ��Ϸ
				setContentView(gv);						//�л���Ļ��GameView
				currentView = gv;						//��¼��ǰView
				pv = null;								//����pvָ���ProgressView��
				break;
			case 1:									//��ʾ����
				hv = new HelpView(RunActivity.this);			//����HelpView����
				setContentView(hv);						//�л���Ļ��HelpView
				currentView = hv;						//��¼��ǰView
				break;
			case 2:									//�˳���Ϸ
				android.os.Process.killProcess(android.os.Process.myPid());
//				System.exit(0);							//�˳�����
				break;
			case 3:									//��ʾ��ӭ����
				setContentView(wv);						//���õ�ǰ��ĻΪWelcomeView����
				currentView = wv;						//��¼��ǰView
				pv = null;								//��pvָ���ProgressView����Ϊ����
				break;
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����ȫ��
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BitmapManager.loadSystemPublic(getResources());
        pv = new ProgressView(this, 3);					//����һ��ProgressView����Ŀ��Ϊ3����������ȥWelcomeView
        setContentView(pv);
        currentView = pv;
    	new Thread(){
    		public void run(){
    			Looper.prepare();
    			BitmapManager.loadWelcomePublic(getResources());	//���ػ�ӭ�����ͼƬ��Դ
    			wv = new WelcomeView(RunActivity.this);//��ʼ��WelcomeView
    			pv.progress=100;
    		}
    	}.start();
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			int x = (int)event.getX();			//��ȡ��Ļ�������X����
			int y = (int)event.getY();			//��ȡ��Ļ�������Y����
			if(currentView == wv){				//�����ǰViewΪWelcomeView
				return wv.myTouchEvent(x, y);	//����GameView������¼�������
			}
			else if(currentView == gv){			//�����ǰViewΪGameView
				return gv.myTouchEvent(x, y);	//����GameView������¼�������
			}
		}
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		//��дonKeyDown����
		switch(keyCode){
		case 19:			//����
			keyState = keyState | 0x1;
			keyState = keyState & 0x1;//���ε�ͬʱ���µļ�
			break;
		case 20:			//����
			keyState = keyState | 0x2;
			keyState = keyState & 0x2;//���ε�ͬʱ���µļ�
			break;
		case 21:			//����
			keyState = keyState | 0x4;
			keyState = keyState & 0x4;//���ε�ͬʱ���µļ�
			break;
		case 22:			//����
			keyState = keyState | 0x8;
			keyState = keyState & 0x8;//���ε�ͬʱ���µļ�
			break;
		default:
			break;
		}
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {		//��дonKeyUp����
		if(currentView == gv){
			
		}
		else if(currentView == hv){//�ǰ�������ʱ���¼�
			if(keyCode == 4){//���µ��Ƿ��Ƿ��ؼ�
				setContentView(wv); //���õ�ǰ��ĻΪHelpView
				currentView = wv;
			}
		}
		return true;
	}
}