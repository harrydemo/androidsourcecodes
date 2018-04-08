package irdc.ex07_20;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class EX07_20 extends Activity
{
  private TextView myText1;
  private TextView myText2;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* ����main.xml Layout */
    setContentView(R.layout.main);
    /* TextView��ʼ�� */
    myText1=(TextView)findViewById(R.id.text1);
    myText2=(TextView)findViewById(R.id.text2);
  }
  
  /* ��дonTouchEvent() */  
  @Override
  public boolean onTouchEvent(MotionEvent event) 
  {   
    /* ��ʾ����������� */
    myText2.setText(""+event.getPointerCount());
    
    /* event��Action?�� */
    switch(event.getAction())
    {
      /* �����¼����� */
      case MotionEvent.ACTION_DOWN:
        myText1.setText(getResources().getString(R.string.act1));
        break;
      /* �����¼����� */
      case MotionEvent.ACTION_UP:
        myText1.setText(getResources().getString(R.string.act2));
        /* ��ʾ����Ϊ0 */
        myText2.setText("0");
        break;
      /* �ڼ��������㱻���� */
      case MotionEvent.ACTION_POINTER_1_DOWN:
        myText1.setText(getResources().getString(R.string.act3));
        break;
      /* �ڼ��������㱻�Ƴ� */
      case MotionEvent.ACTION_POINTER_1_UP:
        myText1.setText(getResources().getString(R.string.act4));
        break;
      /* ���ָ������㱻���� */
      case MotionEvent.ACTION_POINTER_2_DOWN:
        myText1.setText(getResources().getString(R.string.act5));
        break;
      /* ���ָ������㱻�Ƴ� */
      case MotionEvent.ACTION_POINTER_2_UP:
        myText1.setText(getResources().getString(R.string.act6));
        break;
      default:
        break;
    }
    return super.onTouchEvent(event);
  }
}

