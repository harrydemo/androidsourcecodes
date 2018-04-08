package irdc.ex08_13;

/* import���class */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class EX08_13_2 extends Activity
{
  /* �������� */
  private TextView mTitle;
  private TextView mDesc;
  private TextView mLink;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* �趨layoutΪnewscontent.xml */
    setContentView(R.layout.newscontent);
    /* ��ʼ������ */
    mTitle=(TextView) findViewById(R.id.myTitle);
    mDesc=(TextView) findViewById(R.id.myDesc);
    mLink=(TextView) findViewById(R.id.myLink);

    /* ȡ��Intent�е�Bundle���� */
    Intent intent=this.getIntent();
    Bundle bunde = intent.getExtras();
    /* ȡ��Bundle�����е����� */
    mTitle.setText(bunde.getString("title"));
    mDesc.setText(bunde.getString("desc")+"....");
    mLink.setText(bunde.getString("link"));
    /* �趨mLinkΪ��ҳ���� */
    Linkify.addLinks(mLink,Linkify.WEB_URLS);
  }
}

