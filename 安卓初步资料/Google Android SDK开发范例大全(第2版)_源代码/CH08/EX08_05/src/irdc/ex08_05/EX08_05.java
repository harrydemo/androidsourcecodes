package irdc.ex08_05;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class EX08_05 extends Activity
{
  private Gallery myGallery01;
  /* ��ַ���ַ��� */
  private String[] myImageURL = new String[]
  {
      "http://lh4.ggpht.com/_2N-HvtdpHZY/Sac5ahGHGeE/AAAAAAAABRc/"
          + "3txi_fNEe3U/s144-c/20090226.jpg",
      "http://lh3.ggpht.com/_2N-HvtdpHZY/Sac43BcwNWE/AAAAAAAABP0/"
          + "apDTAIoyHSE/s144-c/20090225.jpg",
      "http://lh5.ggpht.com/_2N-HvtdpHZY/SZ35ddDLtbE/AAAAAAAABNA/"
          + "Ze_TpD3FFfE/s144-c/20090215.jpg",
      "http://lh6.ggpht.com/_2N-HvtdpHZY/SZ357lAfZNE/AAAAAAAABOE/"
          + "dfxBtdINgPA/s144-c/20090220.jpg",
      "http://lh5.ggpht.com/_2N-HvtdpHZY/SYjwSZO8emE/AAAAAAAABGI/"
          + "EHe7N52mywg/s144-c/20090129.jpg" };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myGallery01 = (Gallery) findViewById(R.id.myGallery01);
    myGallery01.setAdapter(new myInternetGalleryAdapter(this));
  }

  /* ʵ��BaseAdapter */
  public class myInternetGalleryAdapter extends BaseAdapter
  {
    /* ���Ա myContextΪContext���� */
    private Context myContext;
    private int mGalleryItemBackground;

    /* ������ֻ��һ����������Ҫ�����Context */
    public myInternetGalleryAdapter(Context c)
    {
      this.myContext = c;
      TypedArray a = myContext
          .obtainStyledAttributes(R.styleable.Gallery);

      /* ȡ��Gallery���Ե�Index id */
      mGalleryItemBackground = a.getResourceId(
          R.styleable.Gallery_android_galleryItemBackground, 0);

      /* �ö����styleable�����ܹ�����ʹ�� */
      a.recycle();
    }

    /* �ش������Ѷ����ͼƬ������ */
    public int getCount()
    {
      return myImageURL.length;
    }

    /* ����getItem������ȡ��Ŀǰ������Ӱ�������ID */
    public Object getItem(int position)
    {
      return position;
    }

    public long getItemId(int position)
    {
      return position;
    }

    /* ���ݾ��������λ���� ����getScale�ش�views�Ĵ�С(0.0f to 1.0f) */
    public float getScale(boolean focused, int offset)
    {
      /* Formula: 1 / (2 ^ offset) */
      return Math.max(0, 1.0f / (float) Math.pow(2, Math
          .abs(offset)));
    }

    /* ȡ��Ŀǰ����ʾ��Ӱ��View����������IDֵʹ֮��ȡ����� */
    @Override
    public View getView(int position, View convertView,
        ViewGroup parent)
    {
      // TODO Auto-generated method stub
      /* ����һ��ImageView���� */

      ImageView imageView = new ImageView(this.myContext);
      try
      {
        /* new URL�������ַ���� */
        URL aryURI = new URL(myImageURL[position]);
        /* ȡ������ */
        URLConnection conn = aryURI.openConnection();
        conn.connect();
        /* ȡ�ûش���InputStream */
        InputStream is = conn.getInputStream();
        /* ��InputStream���Bitmap */
        Bitmap bm = BitmapFactory.decodeStream(is);
        /* �ر�InputStream */
        is.close();
        /* �趨Bitmap��ImageView�� */
        imageView.setImageBitmap(bm);
      } catch (IOException e)
      {
        e.printStackTrace();
      }

      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      /* �趨���ImageView����Ŀ�ߣ���λΪdip */
      imageView.setLayoutParams(new Gallery.LayoutParams(200, 150));
      /* �趨Gallery����ͼ */
      imageView.setBackgroundResource(mGalleryItemBackground);
      return imageView;
    }
  }
}

