package irdc.ex07_07;

import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;

public class EX07_07 extends Activity implements 
  AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory  
{
  protected static InputStream is;
  private ImageSwitcher mSwitcher;
  private Context mContext;
  
  /*�趨Gallery��ͼƬ*/
  private Integer[] mThumbIds = 
  {
      R.drawable.sample_p1, R.drawable.sample_p2, 
      R.drawable.sample_p3, R.drawable.sample_p4,
      R.drawable.sample_p5, R.drawable.sample_p6,
      R.drawable.sample_p7, R.drawable.sample_p8
  };

  /*�趨��Switcher��ͼƬ*/
  private Integer[] mImageIds = 
  {
    R.drawable.p1, R.drawable.p2, R.drawable.p3,
    R.drawable.p4, R.drawable.p5, R.drawable.p6,
    R.drawable.p7, R.drawable.p8
  };
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);

    /*�趨Switcher*/
    mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
    mSwitcher.setFactory(this);
    /*�趨����Switcher��ģʽ*/
    mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
            android.R.anim.slide_in_left));
    /*�趨���Switcher��ģʽ*/
    mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
            android.R.anim.slide_out_right));

    Gallery g = (Gallery) findViewById(R.id.gallery);
    g.setAdapter(new ImageAdapter(this));
    g.setOnItemSelectedListener(this);
  
    /*�趨����Gallery��ͼ���¼�*/
    g.setOnItemClickListener(new Gallery.OnItemClickListener() 
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View v, 
          final int position, long id)
      {
        // TODO Auto-generated method stub        
        new AlertDialog.Builder(EX07_07.this)
        .setTitle(R.string.app_about)
        /*�趨��߶���ڵ�ͼʽ*/
        .setIcon(mImageIds[position]) 
        /*�趨��߶���ڵ�ѶϢ*/
        .setMessage(R.string.app_about_msg)
        /*ȷ�ϴ���*/
        .setPositiveButton(R.string.str_ok,
        new DialogInterface.OnClickListener()
       {
         public void onClick(DialogInterface dialoginterface,int i)
         {           
          Resources resources = getBaseContext().getResources();
          is = resources.openRawResource(mImageIds[position]);
           try
            {
             /*��������*/
            setWallpaper(is); 
            /*��Toast����ʾ����?����*/
            Toast.makeText(EX07_07.this, getString(R.string.
                my_text_pre),Toast.LENGTH_SHORT).show();             
            }
           catch (Exception e)
            {            
            e.printStackTrace();
            };
         }
       })
        /*�趨��߶���ڵķ����¼�*/
        .setNegativeButton(R.string.str_no,
         new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialoginterface, 
                              int i)   
         {
            /*��Toast����ʾ����?ȡ��*/
            Toast.makeText(EX07_07.this, getString(R.string.
                my_text_no),Toast.LENGTH_SHORT).show();  
         }
        })
        .show();            
      }     
    });
  }  
  
  /*�趨����Switcher��ͼ��*/
  public void onItemSelected(AdapterView parent, View v,
      int position, long id) 
  {
    mSwitcher.setImageResource(mImageIds[position]);
  }

  public void onNothingSelected(AdapterView parent) 
  {
    
  }

  /*?��Switcher��ģʽ*/
  public View makeView() 
  {
    ImageView i = new ImageView(this);
    i.setBackgroundColor(0xFF000000);
    i.setScaleType(ImageView.ScaleType.FIT_CENTER);
    i.setLayoutParams(new ImageSwitcher.LayoutParams(
        LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
    return i;
  }

  public class ImageAdapter extends BaseAdapter 
  {
    public ImageAdapter(Context c) 
    {
     mContext = c;
    }

    public int getCount() 
    {
      return mImageIds.length;
    }

    public Object getItem(int position)
    {
      return position;
    }

    public long getItemId(int position) 
    {
      return position;
    }

    /*��ʾGallery���ģʽ*/
    public View getView(int position, View convertView,
                          ViewGroup parent) 
    {
      ImageView i = new ImageView(mContext);
      i.setImageResource(mThumbIds[position]);
      i.setAdjustViewBounds(true);
      i.setLayoutParams(new Gallery.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      i.setBackgroundResource(R.drawable.picture_frame);
      return i;
    }
  }
}

