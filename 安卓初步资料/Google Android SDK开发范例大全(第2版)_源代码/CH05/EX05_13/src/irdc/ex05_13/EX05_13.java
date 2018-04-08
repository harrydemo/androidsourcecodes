package irdc.ex05_13; 
import java.io.IOException; 
import java.io.InputStream; 
import android.app.Activity;
import android.app.AlertDialog; 
import android.content.Context; 
import android.content.DialogInterface;
import android.content.res.Resources; 
import android.content.res.TypedArray; 
import android.os.Bundle; 
import android.view.View; 
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter; 
import android.widget.Gallery;
import android.widget.ImageView; 
import android.widget.Toast;
public class EX05_13 extends Activity
{ 
  protected static InputStream is; 
  private ImageAdapter mImageAdapter01;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    /*设定图片*/ 
    Integer[] myImageIds = { R.drawable.google, R.drawable.helloking, R.drawable.chamberlain, R.drawable.king, R.drawable.with, };
    mImageAdapter01 = new ImageAdapter(EX05_13.this, myImageIds); 
    /*设定图为Gallery的显示方式*/ 
    Gallery g = (Gallery) findViewById(R.id.mygallery);
    g.setAdapter(mImageAdapter01);
    g.setOnItemClickListener(new Gallery.OnItemClickListener()
    { 
      @Override 
      public void onItemClick(AdapterView <?> parent, View v, final int position, long id)
      { 
        // TODO Auto-generated method stub 
        new AlertDialog.Builder(EX05_13.this) .setTitle(R.string.app_about) 
        /*设定弹出窗口的图式*/
        .setIcon(mImageAdapter01.myImageIds[position]) 
        /*设定弹出窗口的讯息*/
        .setMessage(R.string.app_about_msg)
        /*确认窗口*/ 
        .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() 
        { 
          public void onClick(DialogInterface dialoginterface, int i) 
          { 
            Resources resources = getBaseContext().getResources(); 
            is = resources.openRawResource(mImageAdapter01.myImageIds [position]);
            try 
            {
              /*更换桌布*/ 
              setWallpaper(is);
              /*用Toast来显示桌布已更换*/
              Toast.makeText(EX05_13.this, getString(R.string. my_gallery_text_pre),Toast.LENGTH_SHORT).show();
              } 
            catch (Exception e) 
            {
              e.printStackTrace();
              };
              } 
          })
          /*设定跳出窗口的返回事件*/
          .setNegativeButton(R.string.str_no, new DialogInterface.OnClickListener() 
          { 
            public void onClick(DialogInterface dialoginterface, int i)
         
          { 
            /*用Toast来显示桌布已取消*/
              Toast.makeText(EX05_13.this, getString(R.string. my_gallery_text_no),Toast.LENGTH_SHORT).show(); } }) .show(); 
        } 
      }); 
    } 
  public class ImageAdapter extends BaseAdapter 
  {
    int mGalleryItemBackground;
    private Context mContext;
    private Integer[] myImageIds; 
    public ImageAdapter(Context c, Integer[] aid) 
    {
      mContext = c; myImageIds = aid;
      TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
      mGalleryItemBackground = a.getResourceId( R.styleable.Gallery_android_galleryItemBackground, 0);
      a.recycle(); 
      } 
    @Override
    public int getCount()
    {
      // TODO Auto-generated method stub 
      return myImageIds.length; 
      } 
    @Override
    public Object getItem(int position)
    { 
      // TODO Auto-generated method stub
      return null; 
      } 
    @Override public long getItemId(int position)
    {
      // TODO Auto-generated method stub 
      return position; 
      } 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    { 
      // TODO Auto-generated method stub 
      /*产生ImageView对象*/
      ImageView i = new ImageView(mContext);
      /*设定图片给imageView对象*/ 
      i.setImageResource(myImageIds[position]); 
      /*重新设定图片的宽高*/
      i.setScaleType(ImageView.ScaleType.FIT_XY); 
      /*重新设定Layout的宽高*/
      i.setLayoutParams(new Gallery.LayoutParams(138, 108)); 
      /*设定Gallery背景图*/
      i.setBackgroundResource(mGalleryItemBackground);
      /*传回imageView对象*/
      return i;
      } 
    }
  @Override
  public void setWallpaper(InputStream data) throws IOException 
  { 
    // TODO Auto-generated method stub 
    super.setWallpaper(data);
    }
  }
 
   
   
   
  
  
 
        
         
           
          
        
       


 
