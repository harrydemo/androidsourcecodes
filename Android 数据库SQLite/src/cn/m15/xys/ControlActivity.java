package cn.m15.xys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 
 * @author ������
 * email:xuanyusong@gmail.com
 * blog:http://blog.csdn.net/xys289187120
 */
public class ControlActivity extends Activity {
   
    Context mContext = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        /**������ɾ�����ݿ�**/
        Button botton0 = (Button)findViewById(R.id.button0);
        botton0.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,NewSQLite.class); 
		 startActivity(intent);
	    }
	}); 
        /**������ɾ�����ݿ��е�Ԫ��**/
        Button botton1 = (Button)findViewById(R.id.button1);
        botton1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,NewTable.class); 
		 startActivity(intent);
	    }
	}); 
       
        /**�� ɾ �� �� ���ݿ���е�����**/
        Button botton2 = (Button)findViewById(R.id.button2);
        botton2.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,Newdate.class); 
		 startActivity(intent);
	    }
	}); 
    }
}