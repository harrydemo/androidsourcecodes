package irdc.ex09_08;

/* import���class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EX09_08 extends Activity
{
  private Button mButton;
  private EditText mEditText1;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* �趨layoutΪmain.xml */
    setContentView(R.layout.main);
    
    /* ��ʼ������ */
    mEditText1=(EditText) findViewById(R.id.myEdit1);
    mButton=(Button) findViewById(R.id.myButton);
    
    /* �趨Button��onClick�¼� */
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        /* �����˺ŵĿհ׼�� */
        String userId=mEditText1.getText().toString();
        if(userId.equals(""))
        {
          showDialog("�˺Ų���Ϊ�հ�!");
        }
        else
        {
          /* ���˺ŷ���Bundle������һ��Activity */
          Intent intent = new Intent();
          intent.setClass(EX09_08.this,EX09_08_1.class);
          Bundle bundle = new Bundle();
          bundle.putString("userId",userId);
          intent.putExtras(bundle);
          startActivityForResult(intent,0);      
        }
      } 
    });
  }

  /* ��д onActivityResult()*/
  @Override
  protected void onActivityResult(int requestCode,int resultCode,
                                  Intent data)
  {
    switch (resultCode)
    { 
      case 99:
        /* �ش�����ʱ��Dialog��ʾ */
        Bundle bunde = data.getExtras();
        String error = bunde.getString("error");
        showDialog(error);
        break;      
      default: 
        break; 
     } 
   } 
  
  /* ��ʾDialog��method */
  private void showDialog(String mess){
    new AlertDialog.Builder(EX09_08.this).setTitle("Message")
     .setMessage(mess)
     .setNegativeButton("ȷ��",new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {          
        }
      })
      .show();
    }
}

