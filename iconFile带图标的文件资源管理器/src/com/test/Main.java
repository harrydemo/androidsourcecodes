package com.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends ListActivity {
	
		  /* �������� 
		     items�������ʾ������
		     paths������ļ�·��
		     rootPath����ʼĿ¼         */
		  private List<String> items=null;
		  private List<String> paths=null;
		  private String rootPath="/";
		  private TextView mPath;
		  
		  protected void onCreate(Bundle icicle) {
			  
		    super.onCreate(icicle);
		    setContentView(R.layout.main);
		    mPath=(TextView)findViewById(R.id.mPath);
		    getFileDir(rootPath);
		  }

		  /* ȡ���ļ��ܹ��ķ��� */
		  private void getFileDir(String filePath)
		  {
		    /* ����Ŀǰ����·�� */
		    mPath.setText(filePath);
		    items=new ArrayList<String>();
		    paths=new ArrayList<String>();
		    File f=new File(filePath);  
		    File[] files=f.listFiles();
		    
		    if(!filePath.equals(rootPath))
		    {
		      /* ��һ������Ϊ[�ص���Ŀ¼] */
		      items.add("b1");
		      paths.add(rootPath);
		      /* �ڶ�������Ϊ[�ص���һ��] */
		      items.add("b2");
		      paths.add(f.getParent());
		    }
		    /* �������ļ����ArrayList�� */
		    for(int i=0;i<files.length;i++)
		    {
		      File file=files[i];
		      items.add(file.getName());
		      paths.add(file.getPath());
		    }
		    
		    /* ʹ���Զ����MyAdapter�������ݴ���ListActivity */
		    setListAdapter(new MyAdapter(this,items,paths));
		  }
		  
		  /* ����ListItem�����ʱҪ���Ķ��� */
		  protected void onListItemClick(ListView l,View v,int position,
		                                 long id)
		  {
		    File file=new File(paths.get(position));
		    if (file.isDirectory())
		    {
		      /* ������ļ��о�������getFileDir() */
		      getFileDir(paths.get(position));
		    }
		    else
		    {
		      /* ������ļ�������openFile() */
		      openFile(file);
		    }
		  }
		  
		  /* ���ֻ��ϴ��ļ��ķ��� */
		  private void openFile(File f) 
		  {
		    Intent intent = new Intent();
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.setAction(android.content.Intent.ACTION_VIEW);
		    
		    /* ����getMIMEType()��ȡ��MimeType */
		    String type = getMIMEType(f);
		    /* ����intent��file��MimeType */	    
		    intent.setDataAndType(Uri.fromFile(f),type);
		    startActivity(intent); 
		  }

		  /* �ж��ļ�MimeType�ķ��� */
		  private String getMIMEType(File f)
		  {
		    String type="";
		    String fName=f.getName();
		    /* ȡ����չ�� */
		    String end=fName.substring(fName.lastIndexOf(".")+1,
		                  fName.length()).toLowerCase(); 
		    
		    /* �������������;���MimeType */
		    if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")
		       ||end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
		    {
		      type = "audio"; 
		    }
		    else if(end.equals("3gp")||end.equals("mp4"))
		    {
		      type = "video";
		    }
		    else if(end.equals("jpg")||end.equals("gif")||end.equals("png")
		            ||end.equals("jpeg")||end.equals("bmp"))
		    {
		      type = "image";
		    }
		    else
		    {
		      /* ����޷�ֱ�Ӵ򿪣�����������б���û�ѡ�� */
		      type="*";
		    }
		    type += "/*"; 
		    return type; 
		  }
}