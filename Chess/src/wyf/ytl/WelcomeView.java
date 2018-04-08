package wyf.ytl;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * ��ӭ����
 * ͨ��WelcomeViewThread��ı�����ʵ�ֶ���Ч��
 *
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback {
	ChessActivity activity;
	private TutorialThread thread;//ˢ֡���߳�
	private WelcomeViewThread moveThread;//����ƶ����߳�
	Bitmap welcomebackage;//�󱳾�
	Bitmap logo;
	Bitmap boy;//С����ͼƬ
	Bitmap oldboy;//��ͷ��ͼƬ
	Bitmap bordbackground;//���ֱ���
	Bitmap logo2;
	Bitmap menu;//�˵���ť
	
	int logoX = -120;//��ʼ����Ҫ�ƶ���ͼƬ����Ӧ����
	int boyX = -100;
	int oldboyX = -120;
	int logo2X = 320;
	
	int bordbackgroundY = -100;//�������y����
	int menuY = 520;//�˵���y����
	public WelcomeView(Context context,ChessActivity activity) {//������ 
		super(context);
		this.activity = activity;//�õ�activity����
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);//��ʼ��ˢ֡�߳�
        this.moveThread = new WelcomeViewThread(this);//��ʼ��ͼƬ�ƶ��߳�
        initBitmap();//��ʼ������ͼƬ
	}
	public void initBitmap(){//��ʼ������ͼƬ
		welcomebackage = BitmapFactory.decodeResource(getResources(), R.drawable.welcomebackage);
		logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		boy = BitmapFactory.decodeResource(getResources(), R.drawable.boy);
		oldboy = BitmapFactory.decodeResource(getResources(), R.drawable.oldboy);
		bordbackground = BitmapFactory.decodeResource(getResources(), R.drawable.bordbackground);
		logo2 = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
		menu = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
	}
	public void onDraw(Canvas canvas){//�Լ�д�Ļ��Ʒ���,������д��
		//����������z��ģ��󻭵ĻḲ��ǰ�滭��
		canvas.drawColor(Color.BLACK);//����
		canvas.drawBitmap(welcomebackage, 0, 100, null);//����welcomebackage
		canvas.drawBitmap(logo, logoX, 110, null);//����logo
		canvas.drawBitmap(boy, boyX, 210, null);//����boy
		canvas.drawBitmap(oldboy, oldboyX, 270, null);//����oldboy
		canvas.drawBitmap(bordbackground, 150, bordbackgroundY, null);//����bordbackground
		canvas.drawBitmap(logo2, logo2X, 100, null);//����logo2
		canvas.drawBitmap(menu, 200, menuY, null);//����menu
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {//����ʱ������Ӧ����
        this.thread.setFlag(true);//����ѭ����־λ
        this.thread.start();//�����߳�
        
        this.moveThread.setFlag(true);//����ѭ����־λ
        this.moveThread.start();//�����߳�
	}
	public void surfaceDestroyed(SurfaceHolder holder) {//�ݻ�ʱ�ͷ���Ӧ����
        boolean retry = true;
        thread.setFlag(false);//����ѭ����־λ
        moveThread.setFlag(false);
        while (retry) {//ѭ��
            try {
                thread.join();//�ȴ��߳̽���
                moveThread.join();
                retry = false;//ֹͣѭ��
            } 
            catch (InterruptedException e) {//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
            }
        }
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {//��Ļ����
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX()>200 && event.getX()<200+menu.getWidth()
					&& event.getY()>355 && event.getY()<355+menu.getHeight()){//����˵���ť
				activity.myHandler.sendEmptyMessage(1);
			}
		}
		return super.onTouchEvent(event);
	}
	class TutorialThread extends Thread{//ˢ֡�߳�
		private int span = 100;//˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;//SurfaceHolder����
		private WelcomeView welcomeView;//WelcomeView����
		private boolean flag = false;
        public TutorialThread(SurfaceHolder surfaceHolder, WelcomeView welcomeView) {//������
            this.surfaceHolder = surfaceHolder;//�õ�SurfaceHolder����
            this.welcomeView = welcomeView;//�õ�WelcomeView����
        }
        public void setFlag(boolean flag) {//����ѭ�����λ
        	this.flag = flag;
        }
		@Override
		public void run() {//��д��run����
			Canvas c;//����
            while (this.flag) {//ѭ��
                c = null;
                try {
                	// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {//ͬ��
                    	welcomeView.onDraw(c);//����
                    }
                } finally {//ʹ��finally��䱣֤����Ĵ���һ���ᱻִ��
                    if (c != null) {
                    	//������Ļ��ʾ����
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//˯��ָ��������
                }
                catch(Exception e){//�����쳣
                	e.printStackTrace();//��ӡ��ջ��Ϣ
                }
            }
		}
	}
}