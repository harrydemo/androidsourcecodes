/*
 * 系统名称：
 * 模块名称：
 * 描述：
 * 作者：徐骏
 * version 1.0
 * time  2010-10-14 上午10:29:12
 * copyright Anymusic Ltd.
 */
package xujun.keepaccount.tabbar;

import xujun.keepaccount.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * @author 徐骏
 * @data   2010-10-14
 */
public class ButtonDemo extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button);
		Button btn = (Button) findViewById(R.id.btn1);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				Builder dialog = new AlertDialog.Builder(ButtonDemo.this);
				dialog.setTitle("ffff");
				dialog.setMessage("你点击了按钮");
				dialog.setIcon(R.drawable.icon);
				
				//dialog.setCancelable(false);//这个太毒了，不给取消了
				//注意这里的OnClickListener和上面的不是同一个类型
				dialog.setNegativeButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						setTitle("你在对话框上点了确定");
					}
				});
				dialog.setPositiveButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						setTitle("你在对话框上点了取消");
					}
				});
				dialog.setNeutralButton("放弃", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						setTitle("你在对话框上点了放弃");
					}
				});
				dialog.create().show();
			}
		});
		//ToggleButton使用
		LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
		ToggleButton toggleButton = new ToggleButton(this);
		toggleButton.setText("停止"); //按钮初始显示的文本。如果不设置，默认显示"OFF"
		toggleButton.setTextOn("运行");
		toggleButton.setTextOff("停止");
		
		layout.addView(toggleButton);
	}
}
