package irdc.ex04_21;

/* import相关class */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EX04_21 extends ListActivity
{
  /* 对象声明 
     items：存放显示的名称
     paths：存放文件路径
     rootPath：起始目录
  */
  private List<String> items=null;
  private List<String> paths=null;
  private String rootPath="/";
  private TextView mPath;
  
  /** Called when the activity is first created. */
  @Override
  protected void onCreate(Bundle icicle)
  {
    super.onCreate(icicle);
    
    /* 加载main.xml Layout */
    setContentView(R.layout.main);
    mPath=(TextView)findViewById(R.id.mPath);
    
    getFileDir(rootPath);
  }
  
  /* 取得文件架构的method */
  private void getFileDir(String filePath)
  {
    /* 设定目前所在路径 */
    mPath.setText(filePath);
    
    items=new ArrayList<String>();
    paths=new ArrayList<String>();  
    File f=new File(filePath);  
    File[] files=f.listFiles();

    if(!filePath.equals(rootPath))
    {
      /* 第一笔设定为[回到根目录] */
      items.add("Back to "+rootPath);
      paths.add(rootPath);
      /* 第二笔设定为[回上层] */
      items.add("Back to ../");
      paths.add(f.getParent());
    }
    /* 将所有文件加入ArrayList中 */
    for(int i=0;i<files.length;i++)
    {
      File file=files[i];
      items.add(file.getName());
      paths.add(file.getPath());
    }
    
    /* 声明一ArrayAdapter，使用file_row这个Layout，
                  并将Adapter设定给此ListActivity */
    ArrayAdapter<String> fileList = 
       new ArrayAdapter<String>(this,R.layout.file_row, items);
    setListAdapter(fileList);
  }
  
  /* 设定ListItem被按下时要做的动作 */
  @Override
  protected void onListItemClick(ListView l,View v,int position,long id)
  {
    File file = new File(paths.get(position));
    if(file.canRead())
    {
      if (file.isDirectory())
      {
        /* 如果是文件夹就再进去读取 */
        getFileDir(paths.get(position));
      }
      else
      {
        /* 如果是文件，则弹出AlertDialog */
        new AlertDialog.Builder(this)
            .setTitle("Message")
            .setMessage("["+file.getName()+"] is File!")
            .setPositiveButton("OK",
              new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface dialog,int which)
                {
                }
              }).show();         
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
}

