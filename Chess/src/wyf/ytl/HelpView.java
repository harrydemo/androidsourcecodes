package wyf.ytl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * �����ǰ�������
 * ֻ����һ��ͼƬ�Լ�һ����ť���ɣ�
 * ͨ���԰�ť�ļ���ʵ�ַ��ز���
 *
 */
public class HelpView extends SurfaceView implements SurfaceHolder.Callback {
	ChessActivity activity;//Activity������
	private TutorialThread thread;//ˢ֡���߳�
	Bitmap back;//���ذ�ť
	Bitmap helpBackground;//����ͼƬ
	public HelpView(Context context,ChessActivity activity) {//������ 
		super(context);
		this.activity = activity;//�õ�activity����
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);//��ʼ���ػ��߳�
        initBitmap();//��ʼ��ͼƬ��Դ
	}
	public void initBitmap(){//��ʼ�����õ���ͼƬ
		back = BitmapFactory.decodeResource(getResources(), R.drawable.back);//���ذ�ť
		helpBackground = BitmapFactory.decodeResource(
						getResources(), 
						R.drawable.helpbackground);//��ʼ������ͼƬ
	}
	public void onDraw(Canvas canvas){//�Լ�д�Ļ��Ʒ���
		canvas.drawBitmap(helpBackground, 0, 90, new Paint());//���Ʊ���ͼƬ
		canvas.drawBitmap(back, 200, 370, new Paint());//���ư�ť
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	public void surfaceCreated(SurfaceHolder holder) {//������ʱ����ˢ֡�߳�
        this.thread.setFlag(true);//����ѭ����־λ
        this.thread.start();//����ˢ֡�߳�
	}
	public void surfaceDestroyed(SurfaceHolder holder) {//���ݻ�ʱֹͣˢ֡�߳�
        boolean retry = true;//ѭ����־λ
        thread.setFlag(false);//����ѭ����־λ
        while (retry) {
            try {
                thread.join();//�ȴ��߳̽���
                retry = false;//ֹͣѭ��
            }catch (InterruptedException e){}//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
        }
	}
	public boolean onTouchEvent(MotionEvent event) {//��Ļ����
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX()>200 && event.getX()<200+back.getWidth()
					&& event.getY()>370 && event.getY()<370+back.getHeight()){//����˷��ذ�ť
				activity.myHandler.sendEmptyMessage(1);//����Handler��Ϣ
			}
		}
		return super.onTouchEvent(event);
	} 
	class TutorialThread extends Thread{//ˢ֡�߳�
		private int span = 1000;//˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;//SurfaceHolder������
		private HelpView helpView;//���������
		private boolean flag = false;//ѭ�����λ 
        public TutorialThread(SurfaceHolder surfaceHolder, HelpView helpView) {//������
            this.surfaceHolder = surfaceHolder;//�õ�surfaceHolder����
            this.helpView = helpView;//�õ�helpView����
        }
        public void setFlag(boolean flag) {//����ѭ�����λ
        	this.flag = flag;
        }
		public void run() {//��д��run����
			Canvas c;//����
            while (this.flag) {//ѭ��
                c = null;
                try {
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {//ͬ��
                    	helpView.onDraw(c);//���û��Ʒ���
                    }
                } finally {//��finally��䱣֤����Ĵ���һ���ᱻִ��
                    if (c != null) {//������Ļ��ʾ����
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//˯��ָ��������
                }catch(Exception e){//�����쳣
                	e.printStackTrace();//��ӡ�쳣��ջ��Ϣ
                }
            }
		}
	}
}