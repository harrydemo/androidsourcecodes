package irdc.ex07_02;
/* import���class */ 
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
import android.view.View; 
public class EX07_02 extends Activity 
{ 
  @Override
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* �趨ContentViewΪ�Զ����MyView */
    MyView myView = new MyView(this);
    setContentView(myView); 
    } 
  /* �Զ���̳�View��MyView */ 
  private class MyView extends View 
  {
    public MyView(Context context) 
    { 
      super(context);
      } 
    /* ��дonDraw() */
    @Override 
    protected void onDraw(Canvas canvas)
    { 
      super.onDraw(canvas); 
      /* �趨����Ϊ��ɫ */
      canvas.drawColor(Color.WHITE); 
      Paint paint = new Paint(); 
      /* ȥ��� */ 
      paint.setAntiAlias(true); 
      /* �趨paint����ɫ */ 
      paint.setColor(Color.RED); 
      /* �趨paint��styleΪSTROKE�����ĵ� */ 
      paint.setStyle(Paint.Style.STROKE);
      /* �趨paint������� */ 
      paint.setStrokeWidth(3); 
      /* ��һ������Բ�� */ 
      canvas.drawCircle(40,40,30, paint);
      /* ��һ������������ */ 
      canvas.drawRect(10,90,70,150,paint); 
      /* ��һ�����ĳ����� */ 
      canvas.drawRect(10,170,70,200,paint);
      /* ��һ��������Բ�� */ 
      RectF re=new RectF(10,220,70,250); 
      canvas.drawOval(re, paint); 
      /* ��һ������������ */ 
      Path path = new Path(); 
      path.moveTo(10,330); 
      path.lineTo(70,330); 
      path.lineTo(40,270); 
      path.close(); 
      canvas.drawPath(path, paint);
      /* ��һ���������� */ 
      Path path1 = new Path();
      path1.moveTo(10,410); 
      path1.lineTo(70,410); 
      path1.lineTo(55,350); 
      path1.lineTo(25,350); 
      path1.close(); 
      canvas.drawPath(path1, paint); 
      /* �趨paint��styleΪFILL��ʵ�� */
      paint.setStyle(Paint.Style.FILL); 
      /* �趨paint����ɫ */ 
      paint.setColor(Color.BLUE); 
      /* ��һ��ʵ��Բ */ 
      canvas.drawCircle(120, 40, 30, paint);
      /* ��һ��ʵ�������� */ 
      canvas.drawRect(90,90,150,150,paint);
      /* ��һ��ʵ�ĳ����� */ 
      canvas.drawRect(90,170,150,200,paint);
      /* ��һ��ʵ����Բ�� */ 
      RectF re2=new RectF(90,220,150,250); canvas.drawOval(re2, paint); /* ��һ��ʵ�������� */ Path path2 = new Path(); path2.moveTo(90,330); path2.lineTo(150,330); path2.lineTo(120,270); path2.close(); canvas.drawPath(path2, paint); /* ��һ��ʵ������ */ Path path3 = new Path(); path3.moveTo(90,410); path3.lineTo(150,410); path3.lineTo(135,350); path3.lineTo(105,350); path3.close(); canvas.drawPath(path3, paint); /* �趨����ɫ */ Shader mShader=new LinearGradient(0, 0,100,100, new int[]{Color.RED, Color.GREEN,Color.BLUE,Color.YELLOW}, null, Shader.TileMode.REPEAT); paint.setShader(mShader); /* ��һ������ɫ��Բ�� */ canvas.drawCircle(200,40, 30, paint); /* ��һ������ɫ�������� */ canvas.drawRect(170,90,230,150,paint); /* ��һ������ɫ�ĳ����� */ canvas.drawRect(170,170,230,200,paint); /* ��һ������ɫ����Բ�� */ RectF re3=new RectF(170,220,230,250); canvas.drawOval(re3, paint); /* ��һ������ɫ�������� */ Path path4 = new Path(); path4.moveTo(170,330); path4.lineTo(230,330); path4.lineTo(200,270); path4.close(); canvas.drawPath(path4, paint); /* ��һ������ɫ������ */ Path path5 = new Path(); path5.moveTo(170,410); path5.lineTo(230,410); path5.lineTo(215,350); path5.lineTo(185,350); path5.close(); canvas.drawPath(path5, paint); /* д�� */ paint.setTextSize(24); canvas.drawText(getResources().getString(R.string.str_text1), 240,50,paint); canvas.drawText(getResources().getString(R.string.str_text2), 240,120,paint); canvas.drawText(getResources().getString(R.string.str_text3), 240,190,paint); canvas.drawText(getResources().getString(R.string.str_text4), 240,250,paint); canvas.drawText(getResources().getString(R.string.str_text5), 240,320,paint); canvas.drawText(getResources().getString(R.string.str_text6), 240,390,paint); } } }