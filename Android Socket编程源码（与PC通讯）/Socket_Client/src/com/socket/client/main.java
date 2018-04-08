package com.socket.client;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class main extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	//����������Ҫ�õ���UIԪ��
	private EditText edtmsgcontent;
	private Button btnSend;
	private String ip="169.254.191.14";
	private int port=1818;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        InitView();
    }
    private void InitView()
    {
    	//��ʾ������
    	setContentView(R.layout.main);
    	
    	//ͨ��id��ȡuiԪ�ض���
    	edtmsgcontent=(EditText)findViewById(R.id.msgcontent);
    	btnSend=(Button)findViewById(R.id.btnsend);
    	
    	//Ϊbtnsend���õ���¼�
    	btnSend.setOnClickListener(this);
    }
    
    public void onClick(View bt)
    {
    	try 
		{
			String msg=edtmsgcontent.getText().toString();
			if(!TextUtils.isEmpty(msg))
				SendMsg(ip,port,msg);
			else
			{
				Toast.makeText(this,"��������Ҫ���͵�����", Toast.LENGTH_LONG);
				edtmsgcontent.requestFocus();
			}
		}
	 catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 	}
	 }
    public void SendMsg(String ip,int port,String msg) throws UnknownHostException, IOException
    {
    	try
    	{
    	Socket socket=null;
    	socket=new Socket(ip,port);
    	BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    	writer.write(msg);
    	writer.flush();
    	writer.close();
    	socket.close();
    	}
    	catch(UnknownHostException e)
    	{
    		e.printStackTrace();
    	} catch (IOException e) 
    	{
    	    e.printStackTrace();
        }
    }
}