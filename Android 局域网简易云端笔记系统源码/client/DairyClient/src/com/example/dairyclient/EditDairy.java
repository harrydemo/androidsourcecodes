package com.example.dairyclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditDairy extends Activity {

	EditText dairyText,dairyTitlEdit;
	Button saveButton;
	Socket theSocket;
	boolean reviseflag;
	String dairyTextString,uidString,dairyTitleString,dairynumString;
	private BufferedReader                _bufferedReader        = null;
    private  PrintWriter  _printWriter    = null;
    private String              _message        = "";
    PrintStream output;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editlayout);
		saveButton=(Button)findViewById(R.id.save);
		dairyText=(EditText)findViewById(R.id.content);
		dairyTitlEdit=(EditText)findViewById(R.id.title);
		dairyText.setText("");
		DairyApp theAPP=(DairyApp)getApplicationContext();
		theSocket=theAPP.getSocket();
		uidString=theAPP.getUser();
	    try {
			_bufferedReader = new BufferedReader(new InputStreamReader(theSocket.getInputStream(), "gb2312"));
			 _printWriter  =  new PrintWriter( new BufferedWriter( new OutputStreamWriter(theSocket.getOutputStream(),"gb2312")), true );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    Bundle bundle=getIntent().getExtras();
	    if(bundle!=null)
	    {
	    	reviseflag=true;
	    	dairynumString=bundle.getString("num");
	    	dairyTitlEdit.setText(bundle.getString("title"));
	    	dairyText.setText(bundle.getString("text"));
	    }
	    else
	    {
	    	reviseflag=false;
	    }
	    
	    saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dairyTextString=dairyText.getText().toString();
				Toast.makeText(getApplicationContext(), dairyTextString+"\r\n"+"sdsdsaad", Toast.LENGTH_SHORT).show();
				dairyTitleString=dairyTitlEdit.getText().toString();
				String sendString = "";
				if (reviseflag) 
				{
					sendString="#<CHADAIRY>#"+dairynumString+"|"+dairyTitleString+"|"+dairyTextString+"#<end>#";
				}
				else 
				{
					sendString="#<NEWDAIRY>#"+uidString+"|"+dairyTitleString+"|"+dairyTextString+"#<end>#";
				}
				new Thread(_runnable).start();
				sendMessage(sendString);
				
			}
		});
	}
	  Handler  _handler = new Handler() 
      {                                                                                
                public void handleMessage(Message msg)                                                                                
                {                                                                                        
                        super.handleMessage(msg);           
                        // 刷新信息                                                                                        
                        if (_message.startsWith("#<SAVEYES>#")) 
                        {
                        	Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
                        	finish();
                        } 
                        else 
                        {
                      	   if (_message.startsWith("#<SEERR>#")) 
                             {
   							
                           	  Toast.makeText(getApplicationContext(), "服务器错误！", Toast.LENGTH_SHORT).show();
   						
                           	  finish();
                             } 
                      	   else 
                      	   {
                      		 if (_message.startsWith("#<CHOK>#")) 
                             {
                             	Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                             	finish();
                             } 
                      	   }
                        }
                }                                                                        
       };
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
                                               // 发送消息
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
       public void sendMessage(final String message)
       {
                       // 发送信息给服务
			_printWriter.println(message);
			_printWriter.flush();
		
       }
}
