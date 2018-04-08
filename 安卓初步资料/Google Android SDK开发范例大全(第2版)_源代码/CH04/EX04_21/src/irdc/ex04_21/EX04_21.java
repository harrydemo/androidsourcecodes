package irdc.ex04_21;

/* import���class */
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
  /* �������� 
     items�������ʾ������
     paths������ļ�·��
     rootPath����ʼĿ¼
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
    
    /* ����main.xml Layout */
    setContentView(R.layout.main);
    mPath=(TextView)findViewById(R.id.mPath);
    
    getFileDir(rootPath);
  }
  
  /* ȡ���ļ��ܹ���method */
  private void getFileDir(String filePath)
  {
    /* �趨Ŀǰ����·�� */
    mPath.setText(filePath);
    
    items=new ArrayList<String>();
    paths=new ArrayList<String>();  
    File f=new File(filePath);  
    File[] files=f.listFiles();

    if(!filePath.equals(rootPath))
    {
      /* ��һ���趨Ϊ[�ص���Ŀ¼] */
      items.add("Back to "+rootPath);
      paths.add(rootPath);
      /* �ڶ����趨Ϊ[���ϲ�] */
      items.add("Back to ../");
      paths.add(f.getParent());
    }
    /* �������ļ�����ArrayList�� */
    for(int i=0;i<files.length;i++)
    {
      File file=files[i];
      items.add(file.getName());
      paths.add(file.getPath());
    }
    
    /* ����һArrayAdapter��ʹ��file_row���Layout��
                  ����Adapter�趨����ListActivity */
    ArrayAdapter<String> fileList = 
       new ArrayAdapter<String>(this,R.layout.file_row, items);
    setListAdapter(fileList);
  }
  
  /* �趨ListItem������ʱҪ���Ķ��� */
  @Override
  protected void onListItemClick(ListView l,View v,int position,long id)
  {
    File file = new File(paths.get(position));
    if(file.canRead())
    {
      if (file.isDirectory())
      {
        /* ������ļ��о��ٽ�ȥ��ȡ */
        getFileDir(paths.get(position));
      }
      else
      {
        /* ������ļ����򵯳�AlertDialog */
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
      /* ����AlertDialog��ʾȨ�޲��� */
      new AlertDialog.Builder(this)
          .setTitle("Message")
          .setMessage("Ȩ�޲���!")
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

