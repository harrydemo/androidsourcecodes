package irdc.ex08_13;

/* import���class */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EX08_13 extends Activity
{
  /* �������� */
  private Button mButton;
  private EditText mEditText;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    /* ��ʼ������ */
    mEditText=(EditText) findViewById(R.id.myEdit);
    mButton=(Button) findViewById(R.id.myButton);
    /* �趨Button��onClick�¼� */
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        String path=mEditText.getText().toString();
        if(path.equals(""))
        {
          showDialog("��ַ����Ϊ�հ�!");
        }
        else
        {
          /* newһ��Intent���󣬲�ָ��class */
          Intent intent = new Intent();
          intent.setClass(EX08_13.this,EX08_13_1.class);
          
          /* newһ��Bundle���󣬲���Ҫ���ݵ����ݴ��� */
          Bundle bundle = new Bundle();
          bundle.putString("path",path);
          /* ��Bundle����assign��Intent */
          intent.putExtras(bundle);
          /* ����Activity EX08_13_1 */
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
    new AlertDialog.Builder(EX08_13.this).setTitle("Message")
    .setMessage(mess)
    .setNegativeButton("ȷ��", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {          
      }
    })
    .show();
  }
}

