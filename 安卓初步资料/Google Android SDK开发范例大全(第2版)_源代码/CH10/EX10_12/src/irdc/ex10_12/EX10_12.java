package irdc.ex10_12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class EX10_12 extends WallpaperService
{
  private final Handler mHandler = new Handler();
  
  /* 重写onCreateEngine()，并返回自定义的MyEngine */
  @Override
  public Engine onCreateEngine()
  {
    return new MyEngine();
  }
  
  /* 告定义MyEngine继承Engine */
  class MyEngine extends Engine
  {
    private final Paint mPaint = new Paint();
    private float centerX;
    private float centerY;
    private boolean mVisible;
    Bitmap bm1;
    Bitmap bm2;
    Bitmap bm3;
    Bitmap bm4;
    private int x=0;
    
    private final Runnable myDraw = new Runnable()
    {
      public void run()
      {
        /* 执行draw() */
        draw();
      }
    };

    @Override
    public void onCreate(SurfaceHolder surfaceHolder)
    {
      /* 初始化四个预存的Bitmap对象 */
      bm1=BitmapFactory.decodeResource(getResources(),R.drawable.d1);
      bm2=BitmapFactory.decodeResource(getResources(),R.drawable.d2);
      bm3=BitmapFactory.decodeResource(getResources(),R.drawable.d3);
      bm4=BitmapFactory.decodeResource(getResources(),R.drawable.d4);
      super.onCreate(surfaceHolder);
    }

    @Override
    public void onVisibilityChanged(boolean visible)
    {
      mVisible = visible;
      if(visible)
      {
        /* 桌布为可视时运行draw() */
        draw();
      }
      else
      {
        /* 桌布不可视时停止myDraw */
        mHandler.removeCallbacks(myDraw);
      }
    }

    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format,
                                 int width, int height)
    {
      /* 保存屏幕中点坐标 */
      centerX = width/2.0f;
      centerY = height/2.0f;
      draw();
      super.onSurfaceChanged(holder, format, width, height);
    }
    
    @Override
    public void onDestroy()
    {
      super.onDestroy();
      /* 程序结束时停止myDraw */
      mHandler.removeCallbacks(myDraw);
    }
    
    /* 产生桌布动画的method */
    public void draw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* 依照x的值决定要出现哪个Bitmap */
        if(x==0)
        {
          c.drawBitmap(bm1,centerX-90,centerY-90, mPaint);
          x++;
        }
        else if(x==1)
        {
          c.drawBitmap(bm2,centerX-90,centerY-90,mPaint);
          x++;
        }
        else if(x==2)
        {
          c.drawBitmap(bm3,centerX-90,centerY-90, mPaint);
          x++;
        }
        else
        {
          c.drawBitmap(bm4,centerX-90,centerY-90, mPaint);
          x=0;
        }
        holder.unlockCanvasAndPost(c);
      }
      /* 停止myDraw */
      mHandler.removeCallbacks(myDraw);
      /* 设定几秒之后圳次执行 */
      if (mVisible)
      {
        mHandler.postDelayed(myDraw, 1000);
      }
    }
  }
}

