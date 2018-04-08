package irdc.ex10_12_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class EX10_12_1 extends WallpaperService
{  
  /* ��дonCreateEngine()���������Զ����MyEngine */
  @Override
  public Engine onCreateEngine()
  {
    return new MyEngine();
  }
  
  /* �Զ���MyEngine�̳�Engine */
  class MyEngine extends Engine
  {
    private final Paint mPaint = new Paint();
    private float mTouchX;
    private float mTouchY;
    Bitmap bg;
    Bitmap bm;

    @Override
    public void onCreate(SurfaceHolder surfaceHolder)
    {
      /* ��ʼ������Bitmap���� */
      bg=BitmapFactory.decodeResource(getResources(),R.drawable.bg);
      /* ��ʼ����ָ����ʱ���ֵ�Bitmap���󣬿��Ϊ120*66 */
      bm=BitmapFactory.decodeResource(getResources(),R.drawable.walk);
      /* enable TouchEvent(Ԥ��Ϊfalse) */
      setTouchEventsEnabled(true);
      
      super.onCreate(surfaceHolder);
    }

    /* ��дonTouchEvent() */
    @Override
    public void onTouchEvent(MotionEvent event)
    {
      if(event.getAction()==MotionEvent.ACTION_DOWN)
      {
        /* ��ָ����ʱ��¼XY����ֵ��������draw() */
        mTouchX=event.getX();
        mTouchY=event.getY();
        draw();
      }
      else if(event.getAction()==MotionEvent.ACTION_UP)
      {
        /* ��ָ�뿪ʱ������������ */
        unDraw();
      }
      else if(event.getAction()==MotionEvent.ACTION_MOVE)
      {
        /* ��ָ�ƶ�ʱ��¼XY����ֵ����ִ��draw() */
        mTouchX=event.getX();
        mTouchY=event.getY();
        draw();
      }
      super.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder)
    {
      /* �趨��������ͼ */
      Canvas c=holder.lockCanvas();
      if (c != null)
        c.drawBitmap(bg,0,0, mPaint);
      holder.unlockCanvasAndPost(c);
      super.onSurfaceCreated(holder);
    }

    /* ?�����׻�ͼ��method */
    void draw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* ������������ */
        c.drawBitmap(bg,0,0, mPaint);
        /* �趨��ָ����ʱ��ʾ������·��ͼ */
        c.drawBitmap(bm,mTouchX-33,mTouchY-120, mPaint);
        holder.unlockCanvasAndPost(c);
      }
    }
    
    /* ��������������method */
    void unDraw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* ������������ */
        c.drawBitmap(bg,0,0, mPaint);
        holder.unlockCanvasAndPost(c);
      }
    }
  }
}

