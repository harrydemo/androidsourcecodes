package irdc.ex03_19; 
import android.app.Activity; 
import android.app.ProgressDialog; 
import android.os.Bundle;
import android.view.View; 
import android.widget.Button; 
public class EX03_19 extends Activity 
{ 
  /*����һ��ȫ�ֵ����Ա����������ΪProgressDialog����*/ 
  public ProgressDialog myDialog = null; 
  @Override 
  public void onCreate(Bundle icicle)
  { 
    super.onCreate(icicle);
    /* ����һ����ť���� */ 
    Button btnButton1 = new Button(this); 
    this.setContentView(btnButton1);
    btnButton1.setText(R.string.str_btn1);
    /* Ϊ�����õİ�ť����ָ��OnClicklistener * �༴���°�ť��ִ�е��¼� * �����¼�����������ʾProgressBar */ 
    btnButton1.setOnClickListener(myShowProgressBar); 
    } 
  /** ���°�ťִ�е�OnClickListener�¼����� */
  Button.OnClickListener myShowProgressBar =
    new Button.OnClickListener() 
  { 
    // @Override 
    public void onClick(View arg0) 
    { 
      CharSequence strDialogTitle = getString(R.string.str_dialog_title);
      CharSequence strDialogBody = getString(R.string.str_dialog_body); 
      // ��ʾProgress�Ի���
      myDialog = ProgressDialog.show 
      ( 
          EX03_19.this, strDialogTitle, strDialogBody, true );
      new Thread()
      {
        public void run()
        {
          try
          {
            /* ������д��Ҫִ�еĳ���Ƭ�� */
            /* Ϊ�����Կ���Ч��������ͣ3����Ϊʾ�� */ 
            sleep(3000);
            }
          catch (Exception e)
          { }
          // ж����������myDialog����
          myDialog.dismiss();
          }
        }.start();
        /* ��ʼִ���߳� */ 
        }
    };
    }
  
