package irdc.ex07_19_1;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class EX07_19_1 extends Activity
{
  private ImageView image1;
  private Bitmap bm;
  private int width=0;
  private int height=0;
  private int pointX=0;
  private int pointY=0;
  private int scale=0;
  private List<Bitmap> li=new ArrayList<Bitmap>();
  private GestureDetector detector;
  private myGestureListener gListener;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
   
    /* 隐藏状态栏 */
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    /* 隐藏标题栏 */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    /* 取得屏幕宽高 */    
    width=this.getWindowManager().getDefaultDisplay().getWidth();
    height=this.getWindowManager().getDefaultDisplay().getHeight();
    
    /* 原始Bitmap设定(960*1440) */
    bm=BitmapFactory.decodeResource(getResources(),R.drawable.photo);
    /* 预因产生亡种尺廾的Bitmap */
    int[][] f={{320,480},{640,960}};
    for(int i=0;i<f.length;i++)
    {
      Bitmap tmp=Bitmap.createScaledBitmap(bm,f[i][0],f[i][1],true);
      li.add(tmp);
    }
    li.add(bm);
        
    /* ImageView初始化 */
    image1=(ImageView)findViewById(R.id.image1);
    image1.setImageBitmap(li.get(0));
    /* GestureDetector设定 */
    gListener = new myGestureListener();
    detector = new GestureDetector(EX07_19_1.this,gListener);
    /* 设定GestureDetector的OnDoubleTapListener */
    detector.setOnDoubleTapListener(
        new GestureDetector.OnDoubleTapListener()
    {
      /* 点击画面做放大的操作 */
      @Override
      public boolean onSingleTapConfirmed(MotionEvent e)
      {
        /* 仅允许放大两次 */
        if(scale!=2)
        {
          scale++;
          pointX+=(width/2);
          pointY+=(height/2);
          /* 重新设定显示的影像 */
          Bitmap newB=Bitmap.createBitmap(li.get(scale),pointX,
                                          pointY,width,height);
          image1.setImageBitmap(newB);
        }
        return false;
      }
      
      @Override
      public boolean onDoubleTapEvent(MotionEvent e)
      {
        return false;
      }
      /* 双击画面做缩小的操作 */
      @Override
      public boolean onDoubleTap(MotionEvent e)
      {
        /* 缩最小就是跟画面几样己 */
        if(scale!=0)
        {
          scale--;
          Bitmap b=li.get(scale);
          int tmpW=b.getWidth();
          int tmpH=b.getHeight();
          /* 计算X轴基准点缩小后的位置 */
          if(pointX-(width/2)>=0)
          {
            if(pointX-(width/2)+width<=tmpW)
            {
              pointX-=(width/2);    
            }
            else
            {
              pointX=tmpW-width;
            }
          }
          else
          {
            pointX=0;
          }
          /* 计算Y轴基准点缩小后的位置 */
          if(pointY-(height/2)>=0)
          {
            if(pointY-(height/2)+height<=tmpH)
            {
              pointY-=(height/2);    
            }
            else
            {
              System.out.println("Y2");
              pointY=tmpH-height;
            }
          }
          else
          {
            pointY=0;
          }
          /* 重新设定显示的影像 */
          Bitmap newB=Bitmap.createBitmap(b,pointX,pointY,
                                          width,height);
          image1.setImageBitmap(newB);
        }
        return false;
      }
    });
  }
  /* 当Activity的onTouchEvent()被触发时，
   * 触发GestureDetector的onTouchEvent() */
  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    if (detector.onTouchEvent(event))
    {
      return detector.onTouchEvent(event);
    }  
    else
    {
      return super.onTouchEvent(event);
    }
  }
  
  /* 告定义GestureListener */
  public class myGestureListener implements GestureDetector.OnGestureListener
  {
    /* 手指?屏幕勺拖拉时触发否method */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX,  float distanceY)
    {
      /* 取得目前显示的Bitmap */
      Bitmap b=li.get(scale);
      int tmpW=b.getWidth();
      int tmpH=b.getHeight();
      /* 计算X轴基准点移动后的吵置 */
      if(pointX+distanceX>=0){
        if((pointX+distanceX)>(tmpW-width))
        {  
          pointX=tmpW-width;
        }
        else
        {
          pointX+=distanceX;
        }
      }
      else
      {
        pointX=0;
      }
      /* 计算Y轴基准点移动后的吵置 */
      if(pointY+distanceY>=0)
      {  
        if((pointY+distanceY)>(tmpH-height))
        {  
          pointY=tmpH-height;
        }
        else
        {
          pointY+=distanceY;
        }
      }
      else
      {
        pointY=0;
      }
      /* ?果有移动，则更新Bitmap设定 */
      if(distanceX!=0&&distanceY!=0)
      {
        Bitmap newB=Bitmap.createBitmap(b,pointX,pointY,
                                        width,height);
        image1.setImageBitmap(newB);
      }
      return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
      return false;
    }
    
    @Override
    public boolean onDown(MotionEvent arg0)
    {
      return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
        float velocityX, float velocityY)
    {
      return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
    }
  }
}

