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
 * �����ǲ˵�����
 *
 */
public class MenuView extends SurfaceView implements SurfaceHolder.Callback {
	ChessActivity activity;//��Activity������
	private TutorialThread thread;//ˢ֡���߳�
	Bitmap startGame;//��ʼ��ϷͼƬ
	Bitmap openSound;//������ͼƬ
	Bitmap closeSound;//�ر�������ͼƬ
	Bitmap help;//������ͼƬ
	Bitmap exit;//�˳���Ϸ��ͼƬ 
	public MenuView(Context context,ChessActivity activity) {//������ 
		super(context);
		this.activity = activity;//�õ�activity����
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);//����ˢ֡�߳�
        initBitmap();//��ʼ��ͼƬ��Դ
	}
	public void initBitmap(){//��ʼ��ͼƬ��ԴͼƬ
		startGame = BitmapFactory.decodeResource(getResources(), R.drawable.startgame);//��ʼ��Ϸ��ť
		openSound = BitmapFactory.decodeResource(getResources(), R.drawable.opensound);//��ʼ������ť
		closeSound = BitmapFactory.decodeResource(getResources(), R.drawable.closesound);//�ر�������ť
		help = BitmapFactory.decodeResource(getResources(), R.drawable.help);//������ť
		exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);//�˳���ť
	}
	public void onDraw(Canvas canvas){//�Լ�д�Ļ��Ʒ���
		canvas.drawColor(Color.BLACK);//����
		canvas.drawBitmap(startGame, 50, 50, null);//����ͼƬ
		if(activity.isSound){//������ʱ�����ƹر�����ͼƬ
			canvas.drawBitmap(closeSound, 50, 150, null);//���ƹر�����
		}else{//û�з�����ʱ���ƴ�����ͼƬ
			canvas.drawBitmap(openSound, 50, 150, null);//���ƿ�ʼ����
		}
		canvas.drawBitmap(help, 50, 250, null);//���ư�����ť
		canvas.drawBitmap(exit, 50, 350, null);//�����˳���ť
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {//����ʱ����ˢ֡
        this.thread.setFlag(true);//����ѭ����־λ
        this.thread.start();//�����߳�
	}
	public void surfaceDestroyed(SurfaceHolder holder) {//�ݻ�ʱ�ͷ�ˢ֡�߳�
        boolean retry = true;//ѭ����־λ
        thread.setFlag(false);//����ѭ����־λ
        while (retry) {//ѭ��
            try {
                thread.join();//�ȴ��߳̽���
                retry = false;//ֹͣѭ��
            }catch (InterruptedException e){}//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
        }
	}
	public boolean onTouchEvent(MotionEvent event) {//��Ļ����
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX()>105 && event.getX()<220
					&&event.getY()>60 && event.getY()<95){//������ǿ�ʼ��Ϸ
				activity.myHandler.sendEmptyMessage(2);
			}else if(event.getX()>105 && event.getX()<220
					&&event.getY()>160 && event.getY()<195){//�������������ť
				activity.isSound = !activity.isSound;//����������ȡ��
				if(!activity.isSound){//��û�з�����ʱ
					if(activity.gamesound != null){//��鵱ǰ�Ƿ��Ѿ����������ڲ���
						if(activity.gamesound.isPlaying()){//����Ϸ�������ڲ���ʱ��
							activity.gamesound.pause();//ֹͣ�����Ĳ���
						}	
					}
				}else{//����Ҫ��������ʱ
					if(activity.gamesound != null){//��gamesound��Ϊ��ʱ
						if(!activity.gamesound.isPlaying()){//�ҵ�ǰ����û���ڲ���
							activity.gamesound.start();//�򲥷�����
						}	
					}
				}
			}else if(event.getX()>105 && event.getX()<220
					&&event.getY()>260 && event.getY()<295){//������ǰ�����ť
				activity.myHandler.sendEmptyMessage(3);//��activity����Hander��Ϣ֪ͨ�л�View
			}else if(event.getX()>105 && event.getX()<220
					&&event.getY()>360 && event.getY()<395){//��������˳���Ϸ
				System.exit(0);//ֱ���˳���Ϸ
			}
		}
		return super.onTouchEvent(event);
	}
	class TutorialThread extends Thread{//ˢ֡�߳�
		private int span = 500;//˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;//SurfaceHolder������
		private MenuView menuView;//MenuView������
		private boolean flag = false;//ѭ�����λ
        public TutorialThread(SurfaceHolder surfaceHolder, MenuView menuView) {//������
            this.surfaceHolder = surfaceHolder;//�õ�surfaceHolder����
            this.menuView = menuView;//�õ�menuView����
        }
        public void setFlag(boolean flag) {//����ѭ�����λ
        	this.flag = flag;
        }
		public void run() {//��д��run����
			Canvas c;//����
            while (this.flag) {//ѭ��
                c = null;
                try {
                	// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {//ͬ����
                    	menuView.onDraw(c);//���û��Ʒ���
                    }
                } finally {//ʹ��finally��֤�������һ����ִ��
                    if (c != null) {
                    	//������Ļ��ʾ����
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//˯��ָ��������
                }catch(Exception e){//�����쳣
                	e.printStackTrace();//���쳣ʱ��ӡ�쳣��ջ��Ϣ
                }
            }
		}
	}
}