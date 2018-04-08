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
	//声明想要调用的C++函数
	public native void setName(String _strName);
	
	//因为C++反向调用Android的函数必须要静态，所以这里要
	//把Activity指针保存起来供showNameInAndroid()方法使用
	public static Activity thisActivity = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        thisActivity = this;
        
        ///////////按钮响应函数//////////////
        Button btnSend = (Button)findViewById(R.id.ButtonSend);
        btnSend.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View _view)
        	{
        		EditText etName = (EditText)findViewById(R.id.EditTextName);
        		
        		//调用C++方法
        		setName(etName.getText().toString());
        	}
        });
        ///////////////////////////////////
    }//end of onCreate
    
    static public void showNameInAndroid(String _strName)
    {
    	String strName = "你的名字是" + _strName;
    	
    	Dialog showDialog = new AlertDialog.Builder(thisActivity)
    	.setTitle("此对话框为C++代码调出!")
    	.setMessage(strName)
    	.setPositiveButton("确定", 
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