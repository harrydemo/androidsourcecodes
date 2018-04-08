package com.example.dairyclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;


import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class UserMainActivity extends Activity {


	String []allDisString;
	String useridString,alldairyinfoString;
	ListView dairyListView;
	Button newdairyButton;
	Socket theclientSocket;
	private BufferedReader                _bufferedReader        = null;
	private  PrintWriter  _printWriter    = null;

    private String              _message        = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dairylist);

		DairyApp theAPP=(DairyApp)getApplicationContext();
		theclientSocket=theAPP.getSocket();
		useridString=theAPP.getUser();

		dairyListView=(ListView)findViewById(R.id.listView1);
		newdairyButton=(Button)findViewById(R.id.ndbt);
		
		newdairyButton.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent editIntent=new Intent(UserMainActivity.this, EditDairy.class);
				startActivity(editIntent);
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Thread(_Getrunnable).start();
	}
	private Runnable _Getrunnable = new Runnable() 
	{
		public void run()
		{
				try {
					_bufferedReader = new BufferedReader(new InputStreamReader(theclientSocket.getInputStream(), "gb2312"));
					_printWriter  =  new PrintWriter( new BufferedWriter( new OutputStreamWriter(theclientSocket.getOutputStream(),"gb2312")), true );
					_printWriter.println("#<GETDAIRY>#"+useridString);
					_printWriter.flush();
					new Thread(_runnable).start();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		}
	};
	  Handler  _handler = new Handler() 
      {                                                                                
                public void handleMessage(Message msg)                                                                                
                {                                                                                        
                        super.handleMessage(msg);           
                        // 刷新信息                                                                                              
                        if (_message.startsWith("#<GETOK>#")) 
                        {
                        	ArrayList<Dairy> DList = new ArrayList<Dairy>(); 
                        	Toast.makeText(getApplicationContext(), "获取成功！", Toast.LENGTH_SHORT).show();
                        	alldairyinfoString=_message.substring(9);
                        	allDisString=alldairyinfoString.split("\\|");
                    		if (Integer.parseInt(allDisString[0])>0) 
                    		{
                    			for (int i = 0; i < Integer.parseInt(allDisString[0]); i++) 
                    			{
                    				String []theinfo=allDisString[i+1].split("\\$");
                    				Dairy theDairy=new Dairy();
                    				theDairy.dairynum=theinfo[0];
                    				theDairy.dairytext=theinfo[1];
                    				theDairy.dairytitle=theinfo[2];
                    				DList.add(theDairy);
                    			}
                    		}
                    		DairyAdapter myAdapter=new DairyAdapter(UserMainActivity.this, DList);
                    		dairyListView.setAdapter(myAdapter);
                        } 
                        else 
                        {
                      	   if (_message.startsWith("#<GETERR>#")) 
                             {
   							
                           	  Toast.makeText(getApplicationContext(), "服务器错误！", Toast.LENGTH_SHORT).show();
   						
                           	  finish();
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
}
