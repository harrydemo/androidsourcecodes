package Crinson.Blog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class mainWnd extends Activity 
{
	//������Ҫ���õ�C++����
	public native void setName(String _strName);
	
	//��ΪC++�������Android�ĺ�������Ҫ��̬����������Ҫ
	//��Activityָ�뱣��������showNameInAndroid()����ʹ��
	public static Activity thisActivity = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        thisActivity = this;
        
        ///////////��ť��Ӧ����//////////////
        Button btnSend = (Button)findViewById(R.id.ButtonSend);
        btnSend.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View _view)
        	{
        		EditText etName = (EditText)findViewById(R.id.EditTextName);
        		
        		//����C++����
        		setName(etName.getText().toString());
        	}
        });
        ///////////////////////////////////
    }//end of onCreate
    
    static public void showNameInAndroid(String _strName)
    {
    	String strName = "���������" + _strName;
    	
    	Dialog showDialog = new AlertDialog.Builder(thisActivity)
    	.setTitle("�˶Ի���ΪC++�������!")
    	.setMessage(strName)
    	.setPositiveButton("ȷ��", 
			    	    	new DialogInterface.OnClickListener()
						    {
						    	public void onClick(DialogInterface dialog, int whichButton)
						    	{
						    		dialog.dismiss();
						    	}
						    })
    	.create();
    	
    	showDialog.show();

    }//end of showNameInAndroid
    

	static 
	{
		System.loadLibrary("C_to_Android");
		System.loadLibrary("Android_to_C");
	}   

    
}