package net.testSocket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//客户端的实现
public class TestSocket extends Activity {
	private TextView text1;
	private Button but1;
	private EditText edit1;
	private final String DEBUG_TAG="mySocketAct";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        text1=(TextView)findViewById(R.id.text1);
        but1=(Button)findViewById(R.id.but1);
        edit1=(EditText)findViewById(R.id.edit);
        
        but1.setOnClickListener(new Button.OnClickListener()
        {
        	@Override
			public void onClick(View v) {
				Socket socket=null;
				String mesg=edit1.getText().toString()+"\r\n";
				edit1.setText("");
				Log.e("dddd", "sent id");
				
				try {
					socket=new Socket("192.168.2.113",54321);
					//向服务器发送信息
					PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
					out.println(mesg);
					
					//接受服务器的信息
					BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String mstr=br.readLine();
					if(mstr!=null)
					{
						text1.setText(mstr);
					}else
					{
						text1.setText("数据错误");
					}
					out.close();
					br.close();
					socket.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}catch(Exception e)
				{
					Log.e(DEBUG_TAG,e.toString());
				}
			}        	
        });
    }
}
