package irdc.ex07_02;

/* import相关class */
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class multi extends Activity
{
  public float x1, x2, y1, y2;
  private MyView myView;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    // getWindow().setFlags(flags, mask)
    x1 = 30;
    y1 = 30;
    x2 = 200;
    y2 = 30;
    super.onCreate(savedInstanceState);
    /* 设置ContentView为自定义的MyView */
    myView = new MyView(this);
    setContentView(myView);

  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
//    if(event.getX(1)==event.getX(0)&&event.getY(1)==event.getY(0))
    if (event.getPointerCount() == 2)
    {
      x2 = event.getX(1);
      y2 = event.getY(1);
    }
    x1 = event.getX(0);
    y1 = event.getY(0);
    myView.invalidate();
    Log.d("multi01", event.getPointerCount() + " xy1: " + x1 + ", "
        + y1 + "  xy2: " + x2 + ", " + y2);
    return super.onTouchEvent(event);
  }

  /* 自定义继承View的MyView */
  private class MyView extends View
  {
    public MyView(Context context)
    {
      super(context);
    }

    /* 覆盖onDraw() */
    @Override
    protected void onDraw(Canvas canvas)
    {
      super.onDraw(canvas);
      /* 设置背景为白色 */
      canvas.drawColor(Color.WHITE);

      Paint paint = new Paint();
      /* 去锯齿 */
      paint.setAntiAlias(true);

      /* 设置paint的style为FILL：实心 */
      paint.setStyle(Paint.Style.FILL);
      /* 设置paint的颜色 */
      paint.setColor(Color.BLUE);

      /* 画一个实心圆 */
      canvas.drawCircle(x1, y1, 30, paint);
      /* 画一个实心正方形 */
      canvas.drawRect(x2 - 30, y2 - 30, x2 + 30, y2 + 30, paint);
      paint.setColor(Color.RED);
      canvas.drawLine(x1, y1, x2, y2, paint);
      /* 设置渐变色 */
      Shader mShader = new LinearGradient(0, 0, 100, 100, new int[]
      { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW }, null,
          Shader.TileMode.REPEAT);
      paint.setShader(mShader);

      /* 写字 */
      paint.setTextSize(14);
      canvas.drawText("xy1: " + x1 + "; " + y1, 80, 50, paint);
      canvas.drawText("xy2: " + x2 + "; " + y2, 80, 120, paint);
    }
  }
}
