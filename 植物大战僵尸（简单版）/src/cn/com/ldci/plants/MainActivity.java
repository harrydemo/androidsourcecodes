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
	GameView gameView;//GameView������
	WelcomeView welcomeView;//WelcomeView������
	FailView failView;//��Ϸʧ�ܽ��������
	WinView winView;//��ӭ���������
	ProcessView processView;//���������������
	public static int nScreenWidth = 0;
	public static int nScreenHeight = 0;
	public static boolean[][] grid = new boolean[9][5];
	/**
	 * ��������x��ʼ����
	 */
	public final static float SX = 60 ;
	/**
	 * ��������x��������
	 */
	public final static float EX = 456 ;
	/**
	 * ��������y��ʼ����
	 */
	public final static float SY = 40 ;
	/**
	 * ��������y��������
	 */
	public final static float EY = 290 ;
	public static final int XL = 10 ;
	public static final int YL = 10 ;
	/**
	 * ÿһ�����Ŀ��
	 */
	public static final int EXL = 44 ;
	/**
	 * ÿһ�����ĸ߶�
	 */
	public static final int EYL = 51 ;
	 Handler myHandler = new Handler(){//��������UI�߳��еĿؼ�
		 public void handleMessage(Message msg) {
			 if(msg.what == 1){//��Ϸʧ��
	        		if(gameView != null){
	        			gameView.bulletThread.setFlag(false);
	        			gameView.moveThread.setFlag(false);
	        			gameView.ap.flag=false;
	        			gameView = null;
	        		}
	        		initFailView();//�л���FialView
	        	}
	        	else if(msg.what == 2){//�л���GameView
	        		if(welcomeView != null){
	        			welcomeView = null;//�ͷŻ�ӭ����
	        		}
	        		if(gameView != null){
	        			gameView = null;
	        		}
	        		if(processView != null){
	        			processView = null;//�ͷż��ؽ���
	        		}
	        		processView = new ProcessView(MainActivity.this,2);//��ʼ�����������л���������View
	            	MainActivity.this.setContentView(processView);
	            	new Thread(){//�߳�
	            		public void run(){
	            			Looper.prepare();
	            			gameView = new GameView(MainActivity.this);//��ʼ��GameView
	            			Looper.loop();
	            		}
	            	}.start();//�����߳�
	        		
	        	}
	        	
	        	else if(msg.what == 4){
	        		
	        		toWelcomeView();//�л���WelcomeView���� 
	        	}
	        	else if(msg.what == 5){
	        		if(gameView != null){
	        			gameView.bulletThread.setFlag(false);
	        			gameView.moveThread.setFlag(false);
	        			gameView.ap.flag=false;
	        			gameView = null;
	        		}
	        		initWinView();//�л���WinView���� 
	        	}
	        	else if(msg.what == 6){
	        		toGameView();//ȥ��Ϸ����
	        	}
	        	
	        	else if(msg.what == 7){
	        		if(welcomeView != null){//�ͷŻ�ӭ����
	        			welcomeView = null;
	        		}
	        		if(processView != null){//�ͷż��ؽ���
	        			processView = null;
	        		}
	        		processView = new ProcessView(MainActivity.this,1);//��ʼ�����������л���������View
	        		MainActivity.this.setContentView(processView);
	            	new Thread(){//�߳�
	            		public void run(){//��д��run����
	            			Looper.prepare();
	            			welcomeView = new WelcomeView(MainActivity.this);//��ʼ��WelcomeView
	            			Looper.loop();
	            		}
	            	}.start();//�����߳�
	        	}
	      
		 }
	 };
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    WindowManager windowManager = this.getWindowManager();
		// ��Ļ
		Display display = windowManager.getDefaultDisplay();
		nScreenHeight = display.getHeight();
		nScreenWidth = display.getWidth();
	    //ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		processView = new ProcessView(this,1);//��ʼ�����������л���������View
    	this.setContentView(processView);//���ü��ؽ���
    	new Thread(){//�߳�
    		public void run(){
    			Looper.prepare();
    			welcomeView = new WelcomeView(MainActivity.this);//��ʼ��WelcomeView
    			Looper.loop();
    		}
    	}.start();//�����߳�
    	initGrid();
	}
	public void toWelcomeView(){//�л�����ӭ����     	
    	this.setContentView(welcomeView);
    }
    public void toGameView(){//��ʼ��Ϸ����
    	this.setContentView(gameView);
    }
    public void initFailView(){//��ʼ��Ϸʧ�ܽ���
    	failView = new FailView(this);
    	this.setContentView(failView);
    }
    public void initWinView(){//��ʼʤ������
    	winView = new WinView(this);
    	this.setContentView(winView);
    }
    
    //��ʼ�����
    private void initGrid(){
		for(int i = 0; i < grid.length ; i++){
			for(int j = 0 ; j < grid[i].length ; j ++){
				grid[i][j] = true ; //true��ʾ�ܷŶ���
			}
		}
	}
}
