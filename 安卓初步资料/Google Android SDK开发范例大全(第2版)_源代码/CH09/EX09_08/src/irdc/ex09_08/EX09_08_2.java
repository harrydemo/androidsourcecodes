package irdc.ex09_08;

/* import���class */
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.util.Xml;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public class EX09_08_2 extends Activity implements ViewFactory
{
  private TextView mText;
  private ImageSwitcher mSwitcher;
  private Gallery mGallery;  
  private List<String> smallPhoto=new ArrayList<String>();
  private List<String> bigPhoto=new ArrayList<String>();
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* �趨layoutΪphotoshow.xml */
    setContentView(R.layout.photoshow);

    /* ȡ��Bundle�еı��� */
    Intent intent=this.getIntent();
    Bundle bunde = intent.getExtras();
    String userId = bunde.getString("userId");
    String albumId = bunde.getString("albumId");
    String title = bunde.getString("title");
    
    /* ����getPhotoList()ȡ�ý������List */
    this.getPhotoList(userId,albumId);
    
    /* �趨�ಾ���� */
    mText=(TextView) findViewById(R.id.title);
    mText.setText(title);
    /*�趨Switcher*/
    mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
    mSwitcher.setFactory(this);
    /*�趨����Switcher��ģʽ*/
    mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
            android.R.anim.fade_in));
    /*�趨���Switcher��ģʽ*/
    mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
            android.R.anim.fade_out));
    
    mGallery = (Gallery) findViewById(R.id.gallery);
    /* �趨Gallery��AdapterΪ�Զ����PhotoAdapter 
     * Gallery����ʾ�ֱ���Ϊ72����Ƭ */
    mGallery.setAdapter(new PhotoAdapter(this,smallPhoto));  
    /* �趨Gallery��ͼƬѡ���¼� */
    mGallery.setOnItemSelectedListener(
        new Gallery.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1,
                                 int arg2, long arg3)
      {
        /* ѡ��GalleryͼƬ���û�Switcher����ʾͼƬ */
        URL url;
        try
        {
          /* Switcher����ʾ�ֱ���Ϊ288����Ƭ */
          url = new URL(bigPhoto.get(arg2).toString());
          URLConnection conn = url.openConnection(); 
          conn.connect();
          mSwitcher.setImageDrawable(
              Drawable.createFromStream(conn.getInputStream(),
              "PHOTO"));
        } 
        catch (Exception e)
        {
          /* ��������ʱ�ش�result����һ��activity */
          Intent intent=new Intent();
          Bundle bundle = new Bundle();
          bundle.putString("error",""+e);
          intent.putExtras(bundle);
          /* ����Ļش�ֵ�趨Ϊ99 */
          EX09_08_2.this.setResult(99, intent);
          EX09_08_2.this.finish();
        } 
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0)
      {
      }     
    });
  }  
  
  /*����Switcher��ģʽ*/
  public View makeView() 
  {
    ImageView i = new ImageView(this);
    i.setBackgroundColor(0xFFFFFFFF);
    i.setScaleType(ImageView.ScaleType.FIT_CENTER);
    i.setLayoutParams(new ImageSwitcher.LayoutParams(
        LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
    
    return i;
  }
  
  /* ����XMLȡ����Ƭ��Ϣ��method */
  private void getPhotoList(String userId,String albumId)
  {
    URL url = null;
    String path="http://picasaweb.google.com/data/feed/api/user/"
                +userId.trim()+"/albumid/"+albumId.trim();
    try
    {  
      url = new URL(path);
      /* ���Զ���PhotoHandler��Ϊ����XML��Handler */
      PhotoHandler handler = new PhotoHandler(); 
      Xml.parse(url.openConnection().getInputStream(),
                Xml.Encoding.UTF_8,handler);
      
      /* ȡ�����ֱַ��ʵ���Ƭ·��(72��288) */
      smallPhoto =handler.getSmallPhoto();
      bigPhoto =handler.getBigPhoto();
    }
    catch (Exception e)
    { 
      /* ��������ʱ�ش�result����һ��activity */
      Intent intent=new Intent();
      Bundle bundle = new Bundle();
      bundle.putString("error",""+e);
      intent.putExtras(bundle);
      /* ����Ļش�ֵ�趨Ϊ99 */
      EX09_08_2.this.setResult(99, intent);
      EX09_08_2.this.finish();
    }
  }
}

