package irdc.ex07_05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class EX07_05 extends Activity 
{
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);   
    Gallery g = (Gallery) findViewById(R.id.mygallery);
            
    /*������ImageAdapter���趨��Gallery����*/
    g.setAdapter(new ImageAdapter(this,getSD()));

    /*�趨һ��itemclickListener�¼�*/
    g.setOnItemClickListener(new OnItemClickListener() 
    {
      public void onItemClick(AdapterView<?> parent, 
                       View v, int position, long id) 
      { 
        
      }
    });
  }
  
  private List<String> getSD()
  {
    /* �趨Ŀǰ����·�� */
    List<String> it=new ArrayList<String>();      
    File f=new File("/sdcard/");  
    File[] files=f.listFiles();
 
    /* �������ļ�����ArrayList�� */
    for(int i=0;i<files.length;i++)
    {
      File file=files[i];
      if(getImageFile(file.getPath()))
        it.add(file.getPath());
    }
    return it;
  }
    
  private boolean getImageFile(String fName)
  {
    boolean re;
    
    /* ȡ����չ�� */
    String end=fName.substring(fName.lastIndexOf(".")+1,
                  fName.length()).toLowerCase(); 
    
    /* ����չ�������;���MimeType */
    if(end.equals("jpg")||end.equals("gif")||end.equals("png")
            ||end.equals("jpeg")||end.equals("bmp"))
    {
      re=true;
    }
    else
    {
      re=false;
    }
    return re; 
  }

  /*��дBaseAdapter�Զ���һImageAdapter class*/
  public class ImageAdapter extends BaseAdapter 
  {
    /*��������*/
    int mGalleryItemBackground;
    private Context mContext;
    private List<String> lis;
    
    /*ImageAdapter�Ĺ����*/
    public ImageAdapter(Context c,List<String> li) 
    {
      mContext = c;
      lis=li;
      /* ʹ��res/values/attrs.xml�е�<declare-styleable>����
      * ��Gallery����.*/
      TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
      /*ȡ��Gallery���Ե�Index id*/
      mGalleryItemBackground = a.getResourceId(
          R.styleable.Gallery_android_galleryItemBackground, 0);
      /*�ö����styleable�����ܹ�����ʹ��*/ 
      a.recycle();
    }
    
    /*����Ҫ��д�ķ���getCount,����ͼƬ��Ŀ*/
    public int getCount() 
    {
      return lis.size();
    }
    
    /*һ��Ҫ��д�ķ���getItem,����position*/
    public Object getItem(int position) 
    {
      return position;
    }
    
    /*һ��Ҫ��д�ķ���getItemId,����position*/
    public long getItemId(int position) 
    {
      return position;
    }
    
    /*����Ҫ��д�ķ���getView,������View����*/
    public View getView(int position, View convertView, 
                          ViewGroup parent) 
    {
      /*����ImageView����*/
      ImageView i = new ImageView(mContext);
      /*�趨ͼƬ��imageView����*/
      Bitmap bm = BitmapFactory.decodeFile(lis.
                            get(position).toString());
      i.setImageBitmap(bm);
      /*�����趨ͼƬ�Ŀ��*/
      i.setScaleType(ImageView.ScaleType.FIT_XY);
      /*�����趨Layout�Ŀ��*/
      i.setLayoutParams(new Gallery.LayoutParams(136, 88));
      /*�趨Gallery����ͼ*/
      i.setBackgroundResource(mGalleryItemBackground);
      /*����imageView����*/
      return i;
    }     
  } 
}

