package irdc.ex08_13;

/* import相关class */
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class EX08_13_1 extends ListActivity
{
  /* 变量声明 */
  private TextView mText;
  private String title="";
  private List<News> li=new ArrayList<News>();
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /* 设定layout为newslist.xml */
    setContentView(R.layout.newslist);
    
    mText=(TextView) findViewById(R.id.myText);
    /* 取得Intent中的Bundle对象 */
    Intent intent=this.getIntent();
    Bundle bunde = intent.getExtras();
    /* 取得Bundle对象中的数据 */
    String path = bunde.getString("path");
    /* 调用getRss()取得解析后的List */
    li=getRss(path);
    mText.setText(title);
    /* 设定自定义的MyAdapter */
    setListAdapter(new MyAdapter(this,li));
  }
  
  /* 设定ListItem被按下时要做的动作 */
  @Override
  protected void onListItemClick(ListView l,View v,int position,
                                 long id)
  {
    /* 取得News对象 */
    News ns=(News)li.get(position);
    /* new一个Intent对象，并指定class */
    Intent intent = new Intent();
    intent.setClass(EX08_13_1.this,EX08_13_2.class);
    /* new一个Bundle对象，并将要传递的数据传入 */
    Bundle bundle = new Bundle();
    bundle.putString("title",ns.getTitle());
    bundle.putString("desc",ns.getDesc());
    bundle.putString("link",ns.getLink());
    /* 将Bundle对象assign给Intent */
    intent.putExtras(bundle);
    /* 调用Activity EX08_13_2 */
    startActivity(intent);
  }
  
  /* 剖析XML的method */
  private List<News> getRss(String path)
  {
    List<News> data=new ArrayList<News>();
    URL url = null;     
    try
    {  
      url = new URL(path);
      /* 产生SAXParser对象 */ 
      SAXParserFactory spf = SAXParserFactory.newInstance(); 
      SAXParser sp = spf.newSAXParser(); 
      /* 产生XMLReader对象 */ 
      XMLReader xr = sp.getXMLReader(); 
      /* 设定自定义的MyHandler给XMLReader */ 
      MyHandler myExampleHandler = new MyHandler(); 
      xr.setContentHandler(myExampleHandler);     
      /* 解析XML */
      xr.parse(new InputSource(url.openStream()));
      /* 取得RSS标题与内容列表 */
      data =myExampleHandler.getParsedData(); 
      title=myExampleHandler.getRssTitle();
    }
    catch (Exception e)
    { 
      /* 发生错误时回传result回上一个activity */
      Intent intent=new Intent();
      Bundle bundle = new Bundle();
      bundle.putString("error",""+e);
      intent.putExtras(bundle);
      /* 错误的回传值设定为99 */
      EX08_13_1.this.setResult(99, intent);
      EX08_13_1.this.finish();
    }
    return data;
  }
}

