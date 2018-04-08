package com.example.dairyclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegActivity extends Activity {

    private BufferedReader                _bufferedReader        = null;
    private  PrintWriter  _printWriter    = null;
    Socket mySocket=null;
	EditText uIDEditText,pWDEditText,pWDEditText2;
	Button regButton;
	 String _message="";
	 private  String SERVERIP        = "192.168.1.25";
     private  int    SERVERPORT      = 12111;
     OutputStream out;  
	 Runnable connectRunnable=new Runnable() 
     {
			@Override
			public void run() {
				try {
					mySocket = new Socket();
					SocketAddress remoteAddr = new InetSocketAddress(SERVERIP,SERVERPORT);
					mySocket.connect(remoteAddr,3000); 
	                _bufferedReader = new BufferedReader(new InputStreamReader(mySocket.getInputStream(), "gb2312"));  
	                _printWriter  =  new PrintWriter( new BufferedWriter( new OutputStreamWriter(mySocket.getOutputStream(),"gb2312")), true );	 
	                
				} catch (IOException e) 
				{
					_exceptionhandler.sendMessage(_handler.obtainMessage()); 
				}
			}
		};
     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reglayout);
		uIDEditText=(EditText)findViewById(R.id.uidrg);
		pWDEditText=(EditText)findViewById(R.id.pwdrg);
		pWDEditText2=(EditText)findViewById(R.id.pwdrgm);
		regButton=(Button)findViewById(R.id.regnowBtn);
		SERVERIP=getIntent().getExtras().getString("ip");
		SERVERPORT=getIntent().getExtras().getInt("port");
		
		
		if (mySocket==null) {
			Thread conThread=new Thread(connectRunnable);
			conThread.setPriority(Thread.MAX_PRIORITY);
			conThread.start();
		}
	    
	    
	    regButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String reguidString=uIDEditText.getText().toString();
				String regpwdString=pWDEditText.getText().toString();
				String regpwd2String=pWDEditText2.getText().toString();
				if (!regpwdString.equals(regpwd2String))
				{
					Toast.makeText(getApplicationContext(), "�������벻һ�£�", Toast.LENGTH_SHORT).show();
					return;
				}
				String sendString="#<REG>#"+reguidString+"|"+regpwdString;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				_printWriter.println(sendString);
				_printWriter.flush();
				Thread recThread=new Thread(_runnable);
				recThread.start();
			}
		});
	}
	private Runnable _runnable = new Runnable() 
    {
            public void run()
            {
                    while(true)
                    {
                            try
                            {
                                    if((_message = _bufferedReader.readLine()) != null)
                                    {
                                            // ������Ϣ
                                            _handler.sendMessage(_handler.obtainMessage());
                                    }
                            }
                            catch (Exception e)
                            {
                            }
                    }
            }
    };
    Handler  _handler = new Handler() 
    {                                                                                
              public void handleMessage(Message msg)                                                                                
              {                                                                                        
                      super.handleMessage(msg);           
                      // ˢ����Ϣ                                                                                        
                     
                      if (_message.startsWith("#<REGYES>#")) 
                      {
						
                    	  Toast.makeText(getApplicationContext(), "ע��ɹ�", Toast.LENGTH_LONG).show();
                    	  finish();
					
                      } 
                      else 
                      {
                    	   if (_message.startsWith("#<REPUID>#")) 
                           {
 							
                         	  Toast.makeText(getApplicationContext(), "�ظ����û�����", Toast.LENGTH_SHORT).show();
 						
                           } 
                    	   else
                    	   {
                    		   if (_message.startsWith("#<SEREE>#")) 
                               {
     							
                             	  Toast.makeText(getApplicationContext(), "����������", Toast.LENGTH_SHORT).show();
                             	  
                               } 
                    	   }
                      }
              }                                                                        
     };
     
     
     Handler  _exceptionhandler = new Handler() 
     {                                                                                
         public void handleMessage(Message msg)                                                                                
         {                                                                                        
                 super.handleMessage(msg);           
                 // ˢ����Ϣ                                                                                        
    
                 Toast.makeText(getApplicationContext(), "����ʧ�ܣ�����������Ƿ�����", Toast.LENGTH_LONG).show();
         }                                                                        
};
     
}
