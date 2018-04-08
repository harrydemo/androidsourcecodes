package cn.err0r.android.sms.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.err0r.android.sms.database.SMSINFODao;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

public class BaseActivty extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
	}  
	
	public void log(String xxx){
		SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String smsdate =format.format(new Date()).toString();
		FileOutputStream outStream = null;
		try {
			outStream = this.openFileOutput("smlog.txt",Context.MODE_APPEND);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outStream.write((smsdate+ ":" + xxx +"/r/n").getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void export(){
		File f = new File(android.os.Environment.getExternalStorageDirectory()+"/smsExport.xml");
		String str="<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
		SMSINFODao dao = new SMSINFODao(this);
		Cursor exportSoure = dao.select();
		while (exportSoure.moveToNext()) {
			str +="<SMSINFO>\r\n";
			str +="<Name>"+(exportSoure.getString(1).equals(exportSoure.getString(0))? "Œ¥∂®“Â" : exportSoure.getString(1))+"</Name>\r\n";
			str +="<PN>"+exportSoure.getString(0)+"</PN>\r\n";
			str +="<Body>"+exportSoure.getString(2)+"</Body>\r\n";
			str +="</SMSINFO>\r\n";
			
		}
		exportSoure.close();
		dao.close();
		FileOutputStream fileOS;
		try {
			fileOS = new FileOutputStream(f);
		
			fileOS.write(str.getBytes());
			fileOS.close();
			BufferedWriter buf = new BufferedWriter (new OutputStreamWriter(fileOS));
			buf.write(str,0,str.length());
			buf.flush();
			buf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
