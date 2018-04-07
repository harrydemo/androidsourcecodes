package cn.android.browser;

import java.io.BufferedReader;   
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;   
import java.io.InputStream;   
import java.io.InputStreamReader;   
import java.io.OutputStream;   
import java.net.HttpURLConnection;   
import java.net.MalformedURLException;   
import java.net.URL;   
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;   
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;   
import android.content.res.TypedArray;
import android.graphics.Bitmap;   
import android.graphics.BitmapFactory;   
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;   
import android.os.Environment;
import android.util.Log;   
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;   
import android.widget.TextView;   
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
   
public class HttpData extends Activity 
{   
    private TextView tv = null;   
    private Bitmap mBitmap = null;   
    private Gallery g = null;
    
    private String httpStr = "";
    private List<String> http_list = null;  
    private List<Drawable> mImageIds = new ArrayList<Drawable>();
    ProgressDialog pd;
    
    public static final int[] Gallery1 = {
        0x0101004c
    };
    public static final int Gallery1_android_galleryItemBackground = 0;
    private final int MENU_ITEM_SAVE = 0;
    private final int MENU_ITEM_EXIT = 1;
    
    @Override   
    public void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
;   
        Intent intent = this.getIntent();   
        Bundle b = intent.getExtras();   
        int id = b.getInt("id");
        String url = b.getString("url");
        
        if(id==1)
        {
        	open_view_1(url);
        }
        else
        {
        	open_view_2(url);
        }
         
    }  
    
    private void open_view_1(String u)
    {
    	setContentView(R.layout.http);   
    	tv = (TextView) this.findViewById(R.id.TextView_HTTP); 
    	startLoading();
    	
    	 select_1(u);
    }
    private void open_view_2(String u)
    {
    	setContentView(R.layout.gall);   
    	g = (Gallery) findViewById(R.id.gallery);
    	startLoading();
    	  	
    	select_1(u);
     	http_list = getImgStr(httpStr);
     	if(http_list!=null)
     		select_2();
         
         g.setAdapter(new ImageAdapter(this));
         
         g.setOnItemClickListener(new OnItemClickListener() {
         	 public void onItemClick(AdapterView parent, View v, int position, long id) {
                  
              }
         });
         
         registerForContextMenu(g);
    }
    
    private void select_1(String u)
    {
    	String httpUrl = u;   
        URL url = null;   
        try {   
            url = new URL(httpUrl);   
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        }   
        if (url != null) {   
            try {   
                HttpURLConnection urlConn = (HttpURLConnection) url   
                        .openConnection();// 打开连接，此处只是创建一个实力，并没有真正的连接   
                urlConn.connect();// 连接   
                InputStream input = urlConn.getInputStream();   
                InputStreamReader inputReader = new InputStreamReader(input);   
                BufferedReader reader = new BufferedReader(inputReader);   
                String inputLine = null;   
                StringBuffer sb = new StringBuffer();   
                while ((inputLine = reader.readLine()) != null) {   
                    sb.append(inputLine).append("\n");   
                }   
                reader.close();   
                inputReader.close();   
                input.close();   
                urlConn.disconnect();   
                {   
                	httpStr = sb.toString();
                	if(tv!=null)
                	{
                		//pd.dismiss();
                		tv.setText(sb.toString());   
                	}
                }   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }else{   
            Log.i("TAG", "url is null");   
        }   
    }
    
    private void select_2()
    {
    	for(int i=0;i<http_list.size();i++)
    	{
    	String httpUrl = http_list.get(i);   
        URL url = null;   
        try {   
            url = new URL(httpUrl);   
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        }   
        if (url != null) {   
            try {   
                HttpURLConnection urlConn = (HttpURLConnection) url   
                        .openConnection();// 打开连接，此处只是创建一个实力，并没有真正的连接   
                urlConn.connect();// 连接   
                InputStream input = urlConn.getInputStream();   
                mBitmap = BitmapFactory.decodeStream(input);   
                if(mBitmap != null)
                {   
                    //iv.setImageBitmap(mBitmap);   
                	Drawable drawable = new BitmapDrawable(mBitmap);
                	mImageIds.add(drawable);
                	int n = mImageIds.size();
                	Log.w("mImageIds",""+n);
                }  
                input.close();
                urlConn.disconnect();
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }else{   
            Log.i("TAG", "url is null");   
        }   
      }//end for
      //pd.dismiss();
    }//end function
    
    public static List<String> getImgStr(String htmlStr)
    {         
        String img="";         
        Pattern p_image;         
        Matcher m_image;         
        List<String> pics = new ArrayList<String>();      
        
        String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址         
        p_image = Pattern.compile       
                (regEx_img,Pattern.CASE_INSENSITIVE);         
       m_image = p_image.matcher(htmlStr);       
       while(m_image.find())
       {         
            img = img + "," + m_image.group();         
            Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src      
            while(m.find())
            {
            	String str = m.group(1);
            	if(str.indexOf("http://")!=-1)
            	{
            		if(!pics.contains(str))
            		{
		               pics.add(m.group(1));   
		               Log.i("image url:",m.group(1));
            		}
            	}
            }      
        }         
           return pics;         
    }     
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
    	 menu.add(0, MENU_ITEM_SAVE, 0, "保存");   
    	 menu.add(0, MENU_ITEM_EXIT, 0, "取消");   
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) 
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) 
        {   
        case MENU_ITEM_SAVE:   
        	int time = (int)Math.floor(System.currentTimeMillis() / 1000 );
        	Drawable d = mImageIds.get(info.position);
        	if(d!=null)
        	{
        		BitmapDrawable bd = (BitmapDrawable)d;
        		Bitmap bm = bd.getBitmap();
        		if(writeImageToFile(bm, String.valueOf(time)))
        			Toast.makeText(this, "保存: " + time + ".png成功", Toast.LENGTH_SHORT).show();
        		else
            		Toast.makeText(this, "保存: " + time + ".png失败", Toast.LENGTH_SHORT).show();
        	}     	
        	return true;   
        case MENU_ITEM_EXIT:     
            return true;   
        default:   
            return super.onContextItemSelected(item);   
        }   
    }
    
    public boolean writeImageToFile(Bitmap bitmap,String filename)
    {
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    	{
    		FileOutputStream os = null;	
    		File sdcarDir = new File("/sdcard/" + filename + ".png");
    		try
    		{
    			os = new FileOutputStream(sdcarDir);
    			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    			
    		}
    		catch (FileNotFoundException e){e.printStackTrace();return false;}
    		finally 
    		{
                if (os != null) 
                {
                    try 
                    {
                        os.flush();
                        os.close();
                    } 
                    catch (IOException e) {e.printStackTrace();return false;}
                }
    		}
    	}
    	return true;
    }

    public class ImageAdapter extends BaseAdapter 
    {
        int mGalleryItemBackground;
        
        public ImageAdapter(Context c) {
            mContext = c;
            // See res/values/attrs.xml for the <declare-styleable> that defines
            // Gallery1.
            TypedArray a = obtainStyledAttributes(Gallery1);
            mGalleryItemBackground = a.getResourceId(
                    Gallery1_android_galleryItemBackground, 0);
            a.recycle();
        }

        public int getCount() {
            return mImageIds.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            i.setImageDrawable(mImageIds.get(position));
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setLayoutParams(new Gallery.LayoutParams(150, 100));
            
            // The preferred Gallery item background
            i.setBackgroundResource(mGalleryItemBackground);
            
            return i;
        }
        private Context mContext;
    }
    
    private void startLoading()
    {
    	pd = ProgressDialog.show(HttpData.this, "", "正在加载，请稍候...",true);
    	  new Thread()
    	  {
    		  @Override
    		  public void run() 
    		  {
	    		  try 
	    		  {
	    			  sleep(6000);
	    		  } 
	    		  catch (InterruptedException e) 
	    		  {
	    			  e.printStackTrace();
	    		  }
	    		  finally
	    		  {
	    			  pd.dismiss();
	    		  }
	    	   }
	       }.start();
    }
    
           
}   