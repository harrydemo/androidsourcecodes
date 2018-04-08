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
  /* 重写onCreateEngine()，并返回自定义的MyEngine */
  @Override
  public Engine onCreateEngine()
  {
    return new MyEngine();
  }
  
  /* 自定义MyEngine继承Engine */
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
      /* 初始化背景Bitmap对象 */
      bg=BitmapFactory.decodeResource(getResources(),R.drawable.bg);
      /* 初始化手指按下时出现的Bitmap对象，宽高为120*66 */
      bm=BitmapFactory.decodeResource(getResources(),R.drawable.walk);
      /* enable TouchEvent(预设为false) */
      setTouchEventsEnabled(true);
      
      super.onCreate(surfaceHolder);
    }

    /* 重写onTouchEvent() */
    @Override
    public void onTouchEvent(MotionEvent event)
    {
      if(event.getAction()==MotionEvent.ACTION_DOWN)
      {
        /* 手指按下时记录XY坐标值，并运行draw() */
        mTouchX=event.getX();
        mTouchY=event.getY();
        draw();
      }
      else if(event.getAction()==MotionEvent.ACTION_UP)
      {
        /* 手指离开时重设桌布背景 */
        unDraw();
      }
      else if(event.getAction()==MotionEvent.ACTION_MOVE)
      {
        /* 手指移动时记录XY坐标值，并执行draw() */
        mTouchX=event.getX();
        mTouchY=event.getY();
        draw();
      }
      super.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder)
    {
      /* 设定桌布背景图 */
      Canvas c=holder.lockCanvas();
      if (c != null)
        c.drawBitmap(bg,0,0, mPaint);
      holder.unlockCanvasAndPost(c);
      super.onSurfaceCreated(holder);
    }

    /* ?桌布勺画图的method */
    void draw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* 重设桌布背景 */
        c.drawBitmap(bg,0,0, mPaint);
        /* 设定手指按下时显示国册走路的图 */
        c.drawBitmap(bm,mTouchX-33,mTouchY-120, mPaint);
        holder.unlockCanvasAndPost(c);
      }
    }
    
    /* 重设桌布背景的method */
    void unDraw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* 重设桌布背景 */
        c.drawBitmap(bg,0,0, mPaint);
        holder.unlockCanvasAndPost(c);
      }
    }
  }
}

