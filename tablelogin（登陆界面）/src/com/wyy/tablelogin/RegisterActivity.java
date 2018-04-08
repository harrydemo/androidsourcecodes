package com.wyy.tablelogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class RegisterActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //因为后续相关控件要在Button的侦听器(采用的匿名内部类的方式)中访问，所以下面的变量前要加final关键字
        //获得用户名 <EditText android:id="@+id/uname" .../>
        final EditText unameEt=(EditText)findViewById(R.id.uname);
        //获得密码框 <EditText android:id="@+id/upass" .../>
        final EditText upassEt=(EditText)findViewById(R.id.upass);
        //获得RadioButton所在的容器 RadioGroup <RadioGroup android:id="@+id/rg" .../>
        final RadioGroup rg=(RadioGroup)findViewById(R.id.rg);
        //获得CheckBox所在的容器 <LinearLayout android:id="@+id/linear" .../>
        final LinearLayout linear=(LinearLayout)findViewById(R.id.linear);
        //获得Spinner <Spinner android:id="@+id/province" .../>
        final Spinner spinner=(Spinner)findViewById(R.id.province);
        //准备初始化Spinner的数据
        String[] datas=new String[]{"上海","浙江","山东"};
        //实例化Adapter
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,datas);
        //设置Spinner下拉的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将Adapter与Spinner绑定
        spinner.setAdapter(adapter);
        //获得按钮<Button android:id="@+id/btn"..>
        Button btn=(Button)findViewById(R.id.btn);
        //向按钮注册单击事件
        btn.setOnClickListener(
        		new View.OnClickListener() {
					public void onClick(View v) {						
						//获得用户名：调用EditText的getText()方法
						String uname=unameEt.getText().toString();
				        //获得密码：调用EditText的getText()方法
						String password=upassEt.getText().toString();
				        //获得性别：获得父容器，然后遍历父容器中所有RadioButton,找到选中的并获得值
						String sex="";
						for(int i=0;i<rg.getChildCount();i++)
						{
							RadioButton rdo=(RadioButton)rg.getChildAt(i);
							if(rdo.isChecked())
							{
								sex=rdo.getText().toString();
								break;//跳出循环
							}
						}
				        //获得爱好：获得父容器，然后遍历父容器中所有CheckBox,找到选中的并获得值
						String hobbys="";
						for(int i=0;i<linear.getChildCount();i++)
						{
							if(linear.getChildAt(i) instanceof CheckBox)
							{
								CheckBox chk=(CheckBox)linear.getChildAt(i);
								if(chk.isChecked())
								{
									hobbys+=chk.getText();
								}
							}
						}
				        //获得爱好：调用Spinner的getSelectedItem()方法
						String province=(String)spinner.getSelectedItem();
				        //将所有信息串联起来：用"+"进行连接
						//String result=uname+password+province+sex+hobbys;
				        //提示信息给用户
						//Toast.makeText(MainActivity.this, result, 1000).show();
						
						SharedPreferences references
							=RegisterActivity.this.getSharedPreferences("account", Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
						
						Editor editor=references.edit();
						
						editor.putString("username", uname);
						editor.putString("password", password);
						
						editor.commit();
						
					}
				}
        ); 
        
    }
}



//题目如下：一个奇怪的动物庄园里住着猫和狗, 
//狗比猫多180只. 有20% 的狗错认为自己是猫；
//有20% 的猫错认为自己是狗. 
//在所有的猫和狗中, 有32% 认为自己是猫, 那么狗有（     ）只.




