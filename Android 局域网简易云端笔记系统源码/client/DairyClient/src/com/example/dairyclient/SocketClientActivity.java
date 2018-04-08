package com.example.dairyclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SocketClientActivity extends Activity 
{
        // ���÷�����IP�Ͷ˿�
        private  String SERVERIP        = "192.168.1.25";
        private  int    SERVERPORT      = 12111;
        private  Thread       _thread         = null;
        public   Socket                                _socket                    = null;
        private BufferedReader                _bufferedReader        = null;
        private  PrintWriter  _printWriter    = null;
        private String              _message        = "";
        TextView mTextView;
        Runnable connectRunnable,sendRunnable;
        EditText userid;
        EditText userpwd;
        EditText serIP;
        EditText serPort;
        String uidString;
       
        boolean conflag=false;
       
      @Override
    protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    }
        
      synchronized void setflag()
      {
      	 conflag=true;
      	 notifyAll();
      }
      
      synchronized void waitflag() throws InterruptedException
      {
      	 while(conflag==false)
      	 {
      		 wait();
      	 }
      }     
      
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);    

        
        Button LogBtn  = (Button)findViewById(R.id.login);
        Button regButton= (Button)findViewById(R.id.register);
        serIP = (EditText)findViewById(R.id.sip);
        serPort = (EditText)findViewById(R.id.sport);
        userid=(EditText)findViewById(R.id.uid);
        userpwd=(EditText)findViewById(R.id.pwd);
        
        
        
       connectRunnable=new Runnable() 
        {
			@Override
			public void run() {
				try {
					_socket = new Socket();
					SocketAddress remoteAddr = new InetSocketAddress(SERVERIP,SERVERPORT);
					_socket.connect(remoteAddr,3000);
					//_socket = new Socket(SERVERIP, SERVERPORT);  
	                _bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream(), "gb2312"));  
	                _printWriter  =  new PrintWriter( new BufferedWriter( new OutputStreamWriter(_socket.getOutputStream(),"gb2312")), true );	          
	                setflag();
				} catch (IOException e) 
				{
					_exceptionhandler.sendMessage(_handler.obtainMessage());
				}
			}
		};
        
		
		
		LogBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SERVERIP=serIP.getText().toString();
				SERVERPORT=Integer.parseInt(serPort.getText().toString());
				uidString=userid.getText().toString().trim();
				String pwdString=userpwd.getText().toString().trim();
			    String sendString="#<LOGIN>#"+uidString+"|"+pwdString;
				if (_socket==null||((_socket!=null)&&!_socket.isConnected())) 
				{
					Thread conThread=new Thread(connectRunnable);
					conThread.setPriority(Thread.MAX_PRIORITY);
					conThread.start();
					sendMessage(sendString);
				    _thread = new Thread(_runnable);
	                _thread.start();
				} 
				else 
				{
					sendMessage(sendString);
					//Toast.makeText(getApplicationContext(), "debug��", Toast.LENGTH_LONG).show();
					  _thread = new Thread(_runnable);
		                _thread.start();
				}
			}
		});
        
		
        regButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent regIntent=new Intent(SocketClientActivity.this, RegActivity.class);
				regIntent.putExtra("ip", SERVERIP);
				regIntent.putExtra("port", SERVERPORT);
				startActivity(regIntent);
				
			}
		});
         
    }
     
    // ������Ϣ��������
    public void sendMessage(final String message)
        {
                        // ������Ϣ��������
    
    	sendRunnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				if(!_socket.isConnected())
				{
				try {
					waitflag();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				_printWriter.println(message);
				_printWriter.flush();
			}
			
		};     
		new Thread(sendRunnable).start();
        }
     
    // ������������������Ϣ
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
                                                break;
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
                         
                          if (_message.startsWith("#<LOGYES>#")) 
                          {
                        	  String dairyString=_message.substring(10);
                        	  String []allString=dairyString.split("\\|");
                        	//  Toast.makeText(getApplicationContext(), "��½�ɹ�"+_message, Toast.LENGTH_LONG).show();
                        	  DairyApp theApp=(DairyApp)getApplicationContext();
                        	  theApp.setSocket(_socket);
                        	  theApp.setUser(uidString);
                        	  Intent lOGIntent=new Intent(SocketClientActivity.this,UserMainActivity.class);
                        	  lOGIntent.putExtra("dairy", uidString+"|"+dairyString);
                        	  startActivity(lOGIntent);
                          } 
                          else 
                          {
                        	   if (_message.startsWith("#<NOUSER>#")) 
                               {
     							
                             	  Toast.makeText(getApplicationContext(), "δע����û�����", Toast.LENGTH_LONG).show();
     						
                               } 
                        	   else
                        	   {
                        		   if (_message.startsWith("#<WPWD>#")) 
                                   {
         							
                                 	  Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_LONG).show();
         						
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
    protected void onPause() 
    {
    	super.onPause();
    
    };
}
