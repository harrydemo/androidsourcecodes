package com.parabola.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListViewReadImageAscy extends Activity {
	private ImageAdapter adapter;//数据源
	private ArrayList<Bean> beans = new ArrayList<Bean>(); //内部类
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 15; i++) {
            Bean b = new Bean();
            b.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.icon));

            beans.add(b);
        }

        adapter = new ImageAdapter(this);
        ListView listview = new ListView(this);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setContentView(listview);

        new ImageLoadTask(this, adapter).execute();
    }
	
	public class Bean {
	       private Bitmap image;

	       public Bitmap getImage() {
	           return image;
	       }
 
	       public void setImage(Bitmap image) {
	           this.image = image;
	       }

	    }
	
	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context context) {
		}
 
	       @Override
	       public int getCount() {
	           return beans.size();
	       }

	       @Override
	       public Bean getItem(int position) {
	           return beans.get(position);
	       }

	       @Override
	       public long getItemId(int position) {
	           return position;
	       }

	       @Override
	       public View getView(int position, View convertView, ViewGroup parent) {
	           ImageView i = new ImageView(ListViewReadImageAscy.this);  //getApplicationContext()
	           Bean b = beans.get(position);
	          //如果有图片则读取，没有则跳过
	           if (b.getImage() != null) {
	              i.setImageBitmap(b.getImage());
	           }
	           return i;    
	 
	       }
	}
	
	/**
	 * //异步加载图片，为了简洁这里请求图片链接先固定。每读取好一个图片就更新，这里界面比较简单，当然你可以做成
	 * 比较好的，像很多好的软件都有这种效果，先一个loading的效果，一旦有图片了就去掉loading，显示图片。
	 * 
	 * //这里还啰嗦一点就是AsyncTask里面重载的方法doInBackground操作不能涉及到更新UI界面，不然会出错。
	 * 虽这样说，但是有时候遇到像我这种情况时，是很难知道出错就是这个原因导致的。
	 * 
	 * 异步加载一个view时，如果
	 * 那个view里面有EditText，EditText在每次加载view都会触发焦点，这时候异步就会出错。可能我这样说不太清楚，
	 * 但是如果试试就会发现这种情况。
	 */
	public class ImageLoadTask extends AsyncTask<Void, Void, Void> {
	       private ImageAdapter adapter;
	       // 初始化
	       public ImageLoadTask(Context context, ImageAdapter adapter) {
	    	   this.adapter = adapter;
	       }
 
	       @Override
	       protected Void doInBackground(Void... params) {
	           for (int i = 0; i < adapter.getCount(); i++) {
	              Bean bean = adapter.getItem(i);
	              Bitmap bitmap = BitmapFactory
	                     .decodeStream(Request
	                             .HandlerData("http://avatar.profile.csdn.net/A/E/5/2_piaopiaohu123.jpg"));
	              bean.setImage(bitmap);
	              publishProgress(); // 通知去更新UI
	           }

	           return null;
	       }

	       public void onProgressUpdate(Void... voids) {
	           if (isCancelled())
	              return;
	           // 更新UI
	           adapter.notifyDataSetChanged();
	       }
	    }
}
