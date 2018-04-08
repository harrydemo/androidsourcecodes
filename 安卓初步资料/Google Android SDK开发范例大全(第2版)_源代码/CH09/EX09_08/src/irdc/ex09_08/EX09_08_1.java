package irdc.ex09_08;

/* import相关class */
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class EX09_08_1 extends Activity
{
  private GridView gView;
  private String userId="";
  private List<String[]> li=new ArrayList<String[]>();
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 设定layout为albumlist.xml */
    setContentView(R.layout.albumlist);
    
    /* 取得Bundle中的userId */
    Intent intent=this.getIntent();
    Bundle bunde = intent.getExtras();
    userId = bunde.getString("userId");

    /* 调用getAlbumList()取得相簿信息 */
    li=this.getAlbumList(userId);
    /* 设定gView的Adapter为自定义的AlbumAdapter */
    gView=(GridView) findViewById(R.id.myGrid);
    gView.setAdapter(new AlbumAdapter(this,li));
    
    /* 设定GridView的onItemClick事件 */
    gView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() 
    { 
      @Override
      public void onItemClick(AdapterView<?> arg0,View arg1,
                              int arg2,long arg3)
      {
        /* 把账号、相簿ID、相簿名称放入Bundle传给下一个Activity */
        Intent intent = new Intent();
        intent.setClass(EX09_08_1.this,EX09_08_2.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        bundle.putString("albumId",li.get(arg2)[0]);
        bundle.putString("title",li.get(arg2)[2]);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
      } 
    }); 
  }
  
  /* 剖析XML取得相簿信息的method */
  private List<String[]> getAlbumList(String id)
  {
    List<String[]> data=new ArrayList<String[]>();
    URL url = null;
    String path="http://picasaweb.google.com/data/feed/api/user/"
                +id.trim();
    try
    {
      url = new URL(path);
      /* 以自订的AlbumHandler作为解析XML的Handler */
      AlbumHandler alHandler = new AlbumHandler(); 
      Xml.parse(url.openConnection().getInputStream(),
                Xml.Encoding.UTF_8,alHandler);
      
      /* 取得相簿信息 */
      data =alHandler.getParsedData(); 
    }
    catch (Exception e)
    { 
      /* 发生错误时回传result回上一个activity */
      Intent intent=new Intent();
      Bundle bundle = new Bundle();
      bundle.putString("error",""+e);
      intent.putExtras(bundle);
      /* 错误的回传值设定为99 */
      EX09_08_1.this.setResult(99, intent);
      EX09_08_1.this.finish();
    }
    return data;
  }
  
  /* 重写 onActivityResult()*/
  @Override
  protected void onActivityResult(int requestCode,int resultCode,
                                  Intent data)
  {
    switch (resultCode)
    { 
      case 99:
        /* 回传错误时以Dialog显示 */
        Bundle bunde = data.getExtras();
        String error = bunde.getString("error");
        showDialog(error);
        break;     
      default: 
        break; 
    } 
  } 
  
  /* 显示Dialog的method */
  private void showDialog(String mess){
    new AlertDialog.Builder(EX09_08_1.this).setTitle("Message")
     .setMessage(mess)
     .setNegativeButton("确定",new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {
        }
      })
      .show();
  }
}

