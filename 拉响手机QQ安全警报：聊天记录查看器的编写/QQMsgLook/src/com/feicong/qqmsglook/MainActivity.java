package com.feicong.qqmsglook;

import java.util.List;
import com.feicong.Utils.RootUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);            
     
        getDBFilesFromQQPath(); //获取DB文件
        setControls();  //动态设置按钮  
    }

	private void getDBFilesFromQQPath()
	{
		try{
        	DBFiles.cleanDBFiles();
        }catch(Exception e){
        	e.printStackTrace();
        }
        if(!RootUtils.hasRootPermission()){
        	Toast.makeText(this, "检测到手机无法获取ROOT权限，程序即将退出", 
        			Toast.LENGTH_LONG).show();
        	this.finish();
        }
        RootUtils.RootCommand("killall com.tencent.mobileqq\n");  //启动前干掉QQ进程
        
        String str1 = "";
        String str2 = "";       
    	int i = Build.VERSION.SDK_INT;
    	if(i >= 14){
    		str1 = System.getenv("LD_LIBRARY_PATH");
    	}
    	if ((i >= 14) && (str1 != ""))
        {
          str2 = " env LD_LIBRARY_PATH=" + str1 + " ";
        }
    	StringBuilder sb = new StringBuilder(str2).append("dalvikvm -cp ");
    	sb.append(getApplication().getPackageCodePath());
    	sb.append(" com.feicong.qqmsglook.DBFiles\n");
        Toast.makeText(this, "正在获取已登陆QQ的聊天记录信息", 
    			Toast.LENGTH_SHORT).show();
        RootUtils.RootCommand(sb.toString()); //执行DBFiles类，作用是复制DB文件到本程序databases目录            
    
        List<String> strings = DBFiles.getDBPaths(); 
        Log.i("DBFiles", strings.toString());
        for (String str : strings){
        	Log.i("DBFiles", str);
        	if (str.length() == 0) continue;
        	String permission="chmod 666 "+ str + '\n'; 
        	RootUtils.RootCommand(permission);   //加上读写权限
        }
	}

	private void setControls()
	{
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout_main);
        //     layout.setOrientation(LinearLayout.VERTICAL);
        //     layout.setGravity(Gravity.CENTER);  
        
		List<String> strings = DBFiles.getDBNames();  //获取已登陆过的QQ记录
        for (String str : strings){
        	if (str.length() == 0) continue;
        	Button btn = new Button(this);
        	btn.setText(str);
	    	btn.setTextSize((float) 24.0);
	    	btn.setGravity(Gravity.CENTER);
	    	btn.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));        	        	
	    	layout.addView(btn);
	    	btn.setOnClickListener(MainActivity.this);
        } 
        setContentView(layout);
	}
    
	public void onClick(View v)
	{
		String qqUin = ((Button)v).getText().toString();
		Intent intent = new Intent();
		intent.putExtra("qqUin", qqUin);
		intent.setClass(MainActivity.this, QQMainListActivity.class);
		MainActivity.this.startActivity(intent);		
	}	
}