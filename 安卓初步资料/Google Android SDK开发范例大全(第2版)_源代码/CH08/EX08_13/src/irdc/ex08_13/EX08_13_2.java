package irdc.ex08_13;

/* import相关class */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class EX08_13_2 extends Activity
{
  /* 变量声明 */
  private TextView mTitle;
  private TextView mDesc;
  private TextView mLink;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 设定layout为newscontent.xml */
    setContentView(R.layout.newscontent);
    /* 初始化对象 */
    mTitle=(TextView) findViewById(R.id.myTitle);
    mDesc=(TextView) findViewById(R.id.myDesc);
    mLink=(TextView) findViewById(R.id.myLink);

    /* 取得Intent中的Bundle对象 */
    Intent intent=this.getIntent();
    Bundle bunde = intent.getExtras();
    /* 取得Bundle对象中的数据 */
    mTitle.setText(bunde.getString("title"));
    mDesc.setText(bunde.getString("desc")+"....");
    mLink.setText(bunde.getString("link"));
    /* 设定mLink为网页连结 */
    Linkify.addLinks(mLink,Linkify.WEB_URLS);
  }
}

