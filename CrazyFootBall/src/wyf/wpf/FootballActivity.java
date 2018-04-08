package wyf.wpf;					//���������
import android.app.Activity;		//���������
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
 * ��Ϸ�����࣬�����л���ͼ�����պͲ����û��ļ������벢����Ӧ����
 * ��Ϸ�Ļ�ӭView�����ؽ��ȵ�View����Ϸ��ͼView�����ﶼ�����ã�����
 * �л���ͨ��onTouchEvent�����������������û������Ļ�¼�
 */
public class FootballActivity extends Activity{
	View current;				//��¼��ǰView
	GameView gv;				//GameView����
	WelcomeView welcome;		//��ӭ����
	LoadingView lv;				//���������ؽ���

	int keyState = 0;			//xxxx00Ϊ������xxxx10Ϊ����,xxxx01Ϊ����
	PlayerMoveThread pmt;		//�ƶ���Աλ�õ��߳�
	boolean wantSound = true;	//�Ƿ񲥷�������־λ
	int [] layoutArray;			//��ʾ��Ա��վλ������	
	MediaPlayer mpWelcomeMusic;		//��Ϸ��ʼǰ�Ļ�ӭ����	
	MediaPlayer mpKick;				//������Ч
	MediaPlayer mpCheerForWin;		//Ӯ�˵�����
	MediaPlayer mpCheerForLose;		//���˵�����
	MediaPlayer mpCheerForGoal;		//����������
	MediaPlayer mpIce;				//ײ����ɽ�������
	MediaPlayer mpLargerGoal;		//ײ�������ź������	
	Rect [] rectPlus;			//����������Ա��ť�ľ��ο�
	Rect [] rectMinus;			//���������Ա��ť�ľ��ο�
	Rect rectSound;				//�Ƿ񲥷�������ť�ľ��ο�
	Rect rectStart;				//��ʼ��ť�ľ��ο�
	Rect rectQuit;				//�˳���ť�ľ��ο�
	Rect rectGallery;			//��ʾGallery�ľ��ο�	
	int [] imageIDs ={			//���8�����ֲ���ͼƬID
			R.drawable.club_1,
			R.drawable.club_2,
			R.drawable.club_3,
			R.drawable.club_4,
			R.drawable.club_5,
			R.drawable.club_6, 
			R.drawable.club_7,
			R.drawable.club_8
		};
	int clubID = imageIDs[0];		//��¼�û�ѡ��ľ��ֲ���ID	
    @Override
    public void onCreate(Bundle savedInstanceState) {			//��дonCreate����
        super.onCreate(savedInstanceState);         
        initWelcomeSound(this);       //��ʼ��������      
        requestWindowFeature(Window.FEATURE_NO_TITLE);		 	 //����ȫ��
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN
        		);        
        welcome = new WelcomeView(this);				//����Ļ�е���ӭ����
        setContentView(welcome);
        current = welcome;
        if(wantSound && mpWelcomeMusic!=null){			//����Ҫ��������Ӧ����
        	mpWelcomeMusic.start();
        }        
        initRects();		//��ʼ������ƥ�����¼��ľ��ο�
    }
    //��������ʼ����ӭ���������
    public void initWelcomeSound(Context context){
		mpWelcomeMusic = MediaPlayer.create(context, R.raw.music);	
    }
    //��������ʼ�����ο�
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
	public boolean onTouchEvent(MotionEvent event) {//��дonTouchEvent����
		if(event.getAction()== MotionEvent.ACTION_UP){//�ж��¼�����
			int x = (int)event.getX();		//��õ������X����
			int y = (int)event.getY();		//��õ������Y����				
			if(current == welcome){//�����ǰ�����ǻ�ӭ����
				if(rectGallery.contains(x, y)){			//�û��������Gallery
					welcome.cg.galleryTouchEvnet(x, y);		//����Gallery���������¼�
				}
				else if(rectSound.contains(x, y)){		//���µ�������ѡ��
					this.wantSound = !this.wantSound;	//��������ѡ��
					return true;
				}
				else if(rectStart.contains(x, y)){		//���¿�ʼ��
					if(checkLayout(welcome.layout)){		//������ѡ��Ĳ����Ƿ���ȷ
						layoutArray = welcome.layout;		//������ѡ��վλ����								
						lv = new LoadingView(this);			//������ȡ����View
						this.setContentView(lv);			//����Ļ��Ϊ��ȡ���ȵ�LoadingView
						this.current = lv;					//��¼��ǰView
						lv.lt.start();						//����LoadingView��ˢ���߳�
						new Thread(){						//����һ�����̣߳������д���GameView����
							public void run(){
								Looper.prepare();
								if(wantSound){
									initSound();//��ʼ������
								}									
								//����
								gv = new GameView(FootballActivity.this,imageIDs[welcome.cg.currIndex]);//������Ϸ����	
								lv.progress = 100;
								welcome = null;			//�ͷŵ�WelcomeView	
							}
						}.start();
					}
				}
				else if(rectQuit.contains(x,y)){		//�����˳���
					System.exit(0);						//�����˳�
				}
				else{									//����Ƿ������޸Ķ�Ավλ�ļӺźͼ��Ű�ť
					for(int i=0;i<3;i++){
						if(rectPlus[i].contains(x,y)){	//����мӺŰ�ť���£������Ӷ�Ӧ����������������
							//����и�������ټ�
							if(welcome.layout[0]+welcome.layout[1]+welcome.layout[2] <10){	
								welcome.layout[i]++;
							}								
							break;
						}
						if(rectMinus[i].contains(x, y)){//����м��Ű�ť���£��ͼ�����Ӧ����
							if(welcome.layout[i] > 0){	//����ô�������Ϊ�㣬�ͼ���һ��
								welcome.layout[i]--;			
							}								
							break;
						}
					}					
				}				
			}
			else if(current == gv){				//�����ǰ��ʾ��ViewΪGameView
				if(gv.rectMenu.contains(x,y)){	//��������˲˵���ť
					gv.isShowDialog = true;		//������ʾ�Ի���
					gv.ball.isPlaying = false;	//����ֹͣ�ƶ�
					pmt.flag = false;				//ʹPlayerMoveThread��ת
				}
				else if(gv.rectYesToDialog.contains(x,y)){		//������µ��ǶԻ����еġ��ǡ���ť
					if(gv.isShowDialog){							//���Ի����ǲ���������ʾ
				        welcome = new WelcomeView(this);			//�½�һ��WelcomeView
				        setContentView(welcome);					//���õ�ǰ��ĻΪWelcomeView
				        welcome.status = 3;							//ֱ����Ϊ����״̬
				        current = welcome;							//��¼��ǰ��Ļ
				        gv = null;								//��GameViewָ��Ķ�������Ϊ����
				        if(wantSound && mpWelcomeMusic!=null){								//����Ҫ����������
				        	mpWelcomeMusic.start();
				        } 
					}
				}
				else if(gv.rectNoToDialog.contains(x,y)){		//������µ��ǶԻ����еġ��񡰰�ť
					if(gv.isShowDialog){							//���Ի����ǲ���������ʾ
						gv.isShowDialog = false;					//����ʾ�Ի���
						pmt.flag = true;							//����˫����Ա���ƶ�
						gv.ball.isPlaying = true;				//����������ƶ�
					}
				}
			}	
			else if(current == lv){									//�����ǰ��ĻΪLoadingView
				if(lv.progress == 100){								//������ȴﵽ100%
					setContentView(gv);							//��Ļ�л���GameView
					current = gv;								//��¼��ǰView
					lv = null;										//lvָ��Ķ�������Ϊ����
					if(mpWelcomeMusic.isPlaying()){					//����Ҫ��������Ӧ����
						mpWelcomeMusic.stop();
					}	
					gv.startGame();								//��ʼ��Ϸ
				}
			}
		}
		return true;
	}
	//������������Ϸ���õ�������
	public void initSound(){			
		mpKick = MediaPlayer.create(this, R.raw.kick);
		updateProgressView();//���½�����
		mpCheerForWin = MediaPlayer.create(this, R.raw.cheer_win);
		updateProgressView();//���½�����
		mpCheerForLose = MediaPlayer.create(this, R.raw.cheer_lose);
		updateProgressView();//���½�����
		mpCheerForGoal = MediaPlayer.create(this, R.raw.cheer_goal);	
		updateProgressView();//���½�����
		mpLargerGoal = MediaPlayer.create(this, R.raw.lager_goal);
		updateProgressView();//���½�����
		mpIce = MediaPlayer.create(this, R.raw.ice);
		updateProgressView();//���½�����
    }	
	//���½������Ľ���
    public void updateProgressView(){
    	lv.progress+=15;
    }	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//������̰����¼��Ļص�����
		switch(keyCode){
		case 21:					//��
			keyState = keyState | 2;
			keyState = keyState & 0xfffffffe;		//����������ļ���״̬
			break;
		case 22:					//��
			keyState = keyState | 1;
			keyState = keyState & 0xffffffd;		//����������ļ���״̬
			break;
		default:
			break;
		}
		return true;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {	//�������̧���¼��Ļص�����
		switch(keyCode){
		case 21:					//��
			keyState = keyState & 0xffffffd;		//�����״̬λ
			break;
		case 22:					//��
			keyState = keyState & 0xfffffffe;		//�����״̬λ
			break;
		default:
			break;
		}
		return true;
	}
	//����û������layout�ϲ��Ϸ�
	public boolean checkLayout(int [] layout){
		int sum=0;
		for(int i=0;i<layout.length;i++){	//���������Ավλ������
			if(layout[i]<0){				//�������ĳ������/���������ϵ���ԱΪ����
				return false;
			}
			else{
				sum+=layout[i];		//�����������ϵ���Ա�������
			}
		}
		if(sum == 10){						//�����Ϊ10�����վλ�Ϸ�
			return true;
		}
		else{
			return false;			//����false
		}
	}

}