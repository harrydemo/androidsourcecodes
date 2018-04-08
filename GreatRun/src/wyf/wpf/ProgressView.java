package wyf.wpf;		//���������
import android.graphics.Canvas;		//���������
import android.graphics.Color;		//���������
import android.graphics.Paint;//���������
import android.view.SurfaceHolder;//���������
import android.view.SurfaceView;//���������

public class ProgressView extends SurfaceView implements SurfaceHolder.Callback{
	RunActivity father;
	ProgressDrawThread pdt;
	int progress; //����ֵ
	int target = -1;//����������������Ŀ�꣬0ΪGameView��3ΪWelcomeView
	
	public ProgressView(RunActivity father,int target) {
		super(father);
		this.father = father;
		this.target = target;
		getHolder().addCallback(this);
		pdt = new ProgressDrawThread(this,getHolder());
	}
	//��Ļ�Ļ��Ʒ���
	public void doDraw(Canvas canvas){
		//��������
		BitmapManager.drawSystemPublic(0, canvas, 0, 0, null);
		//���ڵ���
		Paint paint = new Paint();
		paint.setColor(Color.DKGRAY);
		int p = this.progress;
		if(p == 100){//�ж��Ƿ����������
			father.myHandler.sendEmptyMessage(target);		
		}
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	
	public void surfaceCreated(SurfaceHolder holder) {
		pdt.flag = true;//����ѭ�����Ʊ���
		if(!pdt.isAlive()){//�Ƿ���Ҫ�����߳�
			pdt.start();
		}
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) {
		pdt.flag = false;//ͣ����̨�����߳�
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
}