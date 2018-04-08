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
	
		  /* 变量声明 
		     items：存放显示的名称
		     paths：存放文件路径
		     rootPath：起始目录         */
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

		  /* 取得文件架构的方法 */
		  private void getFileDir(String filePath)
		  {
		    /* 设置目前所在路径 */
		    mPath.setText(filePath);
		    items=new ArrayList<String>();
		    paths=new ArrayList<String>();
		    File f=new File(filePath);  
		    File[] files=f.listFiles();
		    
		    if(!filePath.equals(rootPath))
		    {
		      /* 第一笔设置为[回到根目录] */
		      items.add("b1");
		      paths.add(rootPath);
		      /* 第二笔设置为[回到上一层] */
		      items.add("b2");
		      paths.add(f.getParent());
		    }
		    /* 将所有文件添加ArrayList中 */
		    for(int i=0;i<files.length;i++)
		    {
		      File file=files[i];
		      items.add(file.getName());
		      paths.add(file.getPath());
		    }
		    
		    /* 使用自定义的MyAdapter来将数据传入ListActivity */
		    setListAdapter(new MyAdapter(this,items,paths));
		  }
		  
		  /* 设置ListItem被点击时要做的动作 */
		  protected void onListItemClick(ListView l,View v,int position,
		                                 long id)
		  {
		    File file=new File(paths.get(position));
		    if (file.isDirectory())
		    {
		      /* 如果是文件夹就再运行getFileDir() */
		      getFileDir(paths.get(position));
		    }
		    else
		    {
		      /* 如果是文件就运行openFile() */
		      openFile(file);
		    }
		  }
		  
		  /* 在手机上打开文件的方法 */
		  private void openFile(File f) 
		  {
		    Intent intent = new Intent();
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.setAction(android.content.Intent.ACTION_VIEW);
		    
		    /* 调用getMIMEType()来取得MimeType */
		    String type = getMIMEType(f);
		    /* 设置intent的file与MimeType */	    
		    intent.setDataAndType(Uri.fromFile(f),type);
		    startActivity(intent); 
		  }

		  /* 判断文件MimeType的方法 */
		  private String getMIMEType(File f)
		  {
		    String type="";
		    String fName=f.getName();
		    /* 取得扩展名 */
		    String end=fName.substring(fName.lastIndexOf(".")+1,
		                  fName.length()).toLowerCase(); 
		    
		    /* 依附档名的类型决定MimeType */
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
		      /* 如果无法直接打开，就跳出软件列表给用户选择 */
		      type="*";
		    }
		    type += "/*"; 
		    return type; 
		  }
}