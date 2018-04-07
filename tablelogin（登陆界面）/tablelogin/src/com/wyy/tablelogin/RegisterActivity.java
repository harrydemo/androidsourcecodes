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
        //��Ϊ������ؿؼ�Ҫ��Button��������(���õ������ڲ���ķ�ʽ)�з��ʣ���������ı���ǰҪ��final�ؼ���
        //����û��� <EditText android:id="@+id/uname" .../>
        final EditText unameEt=(EditText)findViewById(R.id.uname);
        //�������� <EditText android:id="@+id/upass" .../>
        final EditText upassEt=(EditText)findViewById(R.id.upass);
        //���RadioButton���ڵ����� RadioGroup <RadioGroup android:id="@+id/rg" .../>
        final RadioGroup rg=(RadioGroup)findViewById(R.id.rg);
        //���CheckBox���ڵ����� <LinearLayout android:id="@+id/linear" .../>
        final LinearLayout linear=(LinearLayout)findViewById(R.id.linear);
        //���Spinner <Spinner android:id="@+id/province" .../>
        final Spinner spinner=(Spinner)findViewById(R.id.province);
        //׼����ʼ��Spinner������
        String[] datas=new String[]{"�Ϻ�","�㽭","ɽ��"};
        //ʵ����Adapter
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,datas);
        //����Spinner��������ʽ
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //��Adapter��Spinner��
        spinner.setAdapter(adapter);
        //��ð�ť<Button android:id="@+id/btn"..>
        Button btn=(Button)findViewById(R.id.btn);
        //��ťע�ᵥ���¼�
        btn.setOnClickListener(
        		new View.OnClickListener() {
					public void onClick(View v) {						
						//����û���������EditText��getText()����
						String uname=unameEt.getText().toString();
				        //������룺����EditText��getText()����
						String password=upassEt.getText().toString();
				        //����Ա𣺻�ø�������Ȼ�����������������RadioButton,�ҵ�ѡ�еĲ����ֵ
						String sex="";
						for(int i=0;i<rg.getChildCount();i++)
						{
							RadioButton rdo=(RadioButton)rg.getChildAt(i);
							if(rdo.isChecked())
							{
								sex=rdo.getText().toString();
								break;//����ѭ��
							}
						}
				        //��ð��ã���ø�������Ȼ�����������������CheckBox,�ҵ�ѡ�еĲ����ֵ
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
				        //��ð��ã�����Spinner��getSelectedItem()����
						String province=(String)spinner.getSelectedItem();
				        //��������Ϣ������������"+"��������
						//String result=uname+password+province+sex+hobbys;
				        //��ʾ��Ϣ���û�
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



//��Ŀ���£�һ����ֵĶ���ׯ԰��ס��è�͹�, 
//����è��180ֻ. ��20% �Ĺ�����Ϊ�Լ���è��
//��20% ��è����Ϊ�Լ��ǹ�. 
//�����е�è�͹���, ��32% ��Ϊ�Լ���è, ��ô���У�     ��ֻ.




