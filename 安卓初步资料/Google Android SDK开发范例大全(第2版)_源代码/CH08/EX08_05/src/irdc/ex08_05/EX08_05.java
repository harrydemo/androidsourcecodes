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
  /* 网址列字符串 */
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

  /* 实做BaseAdapter */
  public class myInternetGalleryAdapter extends BaseAdapter
  {
    /* 类成员 myContext为Context对象 */
    private Context myContext;
    private int mGalleryItemBackground;

    /* 建构子只有一个参数，即要保存的Context */
    public myInternetGalleryAdapter(Context c)
    {
      this.myContext = c;
      TypedArray a = myContext
          .obtainStyledAttributes(R.styleable.Gallery);

      /* 取得Gallery属性的Index id */
      mGalleryItemBackground = a.getResourceId(
          R.styleable.Gallery_android_galleryItemBackground, 0);

      /* 让对象的styleable属性能够反复使用 */
      a.recycle();
    }

    /* 回传所有已定义的图片总数量 */
    public int getCount()
    {
      return myImageURL.length;
    }

    /* 利用getItem方法，取得目前容器中影像的数组ID */
    public Object getItem(int position)
    {
      return position;
    }

    public long getItemId(int position)
    {
      return position;
    }

    /* 依据距离中央的位移量 利用getScale回传views的大小(0.0f to 1.0f) */
    public float getScale(boolean focused, int offset)
    {
      /* Formula: 1 / (2 ^ offset) */
      return Math.max(0, 1.0f / (float) Math.pow(2, Math
          .abs(offset)));
    }

    /* 取得目前欲显示的影像View，传入数组ID值使之读取与成像 */
    @Override
    public View getView(int position, View convertView,
        ViewGroup parent)
    {
      // TODO Auto-generated method stub
      /* 建立一个ImageView对象 */

      ImageView imageView = new ImageView(this.myContext);
      try
      {
        /* new URL物件将网址传入 */
        URL aryURI = new URL(myImageURL[position]);
        /* 取得联机 */
        URLConnection conn = aryURI.openConnection();
        conn.connect();
        /* 取得回传的InputStream */
        InputStream is = conn.getInputStream();
        /* 将InputStream变成Bitmap */
        Bitmap bm = BitmapFactory.decodeStream(is);
        /* 关闭InputStream */
        is.close();
        /* 设定Bitmap于ImageView中 */
        imageView.setImageBitmap(bm);
      } catch (IOException e)
      {
        e.printStackTrace();
      }

      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      /* 设定这个ImageView对象的宽高，单位为dip */
      imageView.setLayoutParams(new Gallery.LayoutParams(200, 150));
      /* 设定Gallery背景图 */
      imageView.setBackgroundResource(mGalleryItemBackground);
      return imageView;
    }
  }
}

