package irdc.ex05_11;

/* import相关class */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class EX05_11 extends ListActivity
{
  /* 变量声明 
     items：存放显示的名称
     paths：存放文件路径
     rootPath：起始目录         */
  private List<String> items=null;
  private List<String> paths=null;
  private String rootPath="/";
  private TextView mPath;
  
  @Override
  protected void onCreate(Bundle icicle)
  {
    super.onCreate(icicle);
    
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    /* 初始化mPath，用以显示目前路径 */
    mPath=(TextView)findViewById(R.id.mPath);
    getFileDir(rootPath);
  }

  /* 取得文件架构的method */
  private void getFileDir(String filePath)
  {
    /* 设定目前所存路径 */
    mPath.setText(filePath);
    items=new ArrayList<String>();
    paths=new ArrayList<String>();
    File f=new File(filePath);  
    File[] files=f.listFiles();
    
    if(!filePath.equals(rootPath))
    {
      /* 第一笔设定为[并到根目录] */
      items.add("b1");
      paths.add(rootPath);
      /* 第二笔设定为[并到勺几层] */
      items.add("b2");
      paths.add(f.getParent());
    }
    /* 将所有文件存入ArrayList中 */
    for(int i=0;i<files.length;i++)
    {
      File file=files[i];
      items.add(file.getName());
      paths.add(file.getPath());
    }
    
    /* 使用告定义的MyAdapter来将数据传入ListActivity */
    setListAdapter(new MyAdapter(this,items,paths));
  }
  
  /* 设定ListItem被按下时要做的动作 */
  @Override
  protected void onListItemClick(ListView l,View v,
                                 int position,long id)
  {
    File file=new File(paths.get(position));
    if(file.canRead())
    {
      if (file.isDirectory())
      {
        /* 如果是文件夹就运行getFileDir() */
        getFileDir(paths.get(position));
      }
      else
      {
        /* 如果是文件就运行openFile() */
        openFile(file);
      }
    }
    else
    {
      /* 弹出AlertDialog显示权限不足 */
      new AlertDialog.Builder(this)
          .setTitle("Message")
          .setMessage("权限不足!")
          .setPositiveButton("OK",
            new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog,int which)
              {
              }
            }).show();
    }
  }
  
  /* ?手机勺打开文件的method */
  private void openFile(File f) 
  {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(android.content.Intent.ACTION_VIEW);
    
    /* 调用getMIMEType()来取得MimeType */
    String type = getMIMEType(f);
    /* 设定intent的file与MimeType */
    intent.setDataAndType(Uri.fromFile(f),type);
    startActivity(intent); 
  }

  /* 判断文件MimeType的method */
  private String getMIMEType(File f)
  {
    String type="";
    String fName=f.getName();
    /* 取得扩展名 */
    String end=fName.substring(fName.lastIndexOf(".")+1,
                               fName.length()).toLowerCase(); 
    
    /* 依扩展名的类型决定MimeType */
    if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
       end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
    {
      type = "audio"; 
    }
    else if(end.equals("3gp")||end.equals("mp4"))
    {
      type = "video";
    }
    else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
            end.equals("jpeg")||end.equals("bmp"))
    {
      type = "image";
    }
    else
    {
      type="*";
    }
    /* 如果无法直接打开，就弹出软件列表给用户选择 */
    type += "/*"; 
    return type; 
  }
}

