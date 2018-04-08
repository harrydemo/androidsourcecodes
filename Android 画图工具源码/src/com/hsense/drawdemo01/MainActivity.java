package com.hsense.drawdemo01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

	//各种画图工作类：直线、矩形、圆等等
	private Draw_Line dLine;
	private Draw_Rectangle dRectangle;
	private Draw_Circle dCircle;
	private Draw_triangle dTriangle;
	private Draw_Cube dCube;
	private Draw_Column dColumn;
	private Draw_Path dPath;

	Button draw_btn;//选择图形的按钮
	
	/* 设置每个view的布局大小
	 * This set of layout parameters defaults the width and the height 
	 * 
	 * LayoutParams.MATCH_PARENT和xml中定义控件大小的match_parent属性一样
	 * height：设置为700，如果设置为match_parent、wrap_content则view画占满全屏挡住draw_btn按钮控件
	 */
	LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, 700);
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        draw_btn = (Button)findViewById(R.id.draw_button);
        draw_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*
				 * 单击按钮后会弹出选择图形对话框
				 */
				final String[] mItems = {"直线","矩形","圆形","三角形","立方体","圆柱体","涂鸦"};
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);   
		        builder.setTitle("选择形状");  
				builder.setItems(mItems, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 选择后，根据选择的内容实例化相应的类
						switch (which) {
						case 0: //直线
							//实例化直线view类
							dLine = new Draw_Line(getApplicationContext());
							//将直线view类加入到当前activity
							addContentView(dLine, lParams);
							break;
						case 1:
							dRectangle = new Draw_Rectangle(getApplicationContext());
							addContentView(dRectangle, lParams);
							break;
						case 2:
							dCircle = new Draw_Circle(getApplicationContext());
							addContentView(dCircle, lParams);
							break;
						case 3:
							dTriangle = new Draw_triangle(getApplicationContext());
							addContentView(dTriangle,
									lParams);
							break;
						case 4:
							dCube = new Draw_Cube(getApplicationContext());
							addContentView(dCube, lParams);
							break;
						case 5:
							dColumn = new Draw_Column(getApplicationContext());
							addContentView(dColumn, lParams);
							break;
						default:
							dPath = new Draw_Path(getApplicationContext());
							addContentView(dPath, lParams);
							break;
						}
					}
				});  
		        builder.create().show();  
			}
		});
        
    }

    
    
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
