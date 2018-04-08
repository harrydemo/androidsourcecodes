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
	private ImageAdapter adapter;//����Դ
	private ArrayList<Bean> beans = new ArrayList<Bean>(); //�ڲ���
	
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
	          //�����ͼƬ���ȡ��û��������
	           if (b.getImage() != null) {
	              i.setImageBitmap(b.getImage());
	           }
	           return i;    
	 
	       }
	}
	
	/**
	 * //�첽����ͼƬ��Ϊ�˼����������ͼƬ�����ȹ̶���ÿ��ȡ��һ��ͼƬ�͸��£��������Ƚϼ򵥣���Ȼ���������
	 * �ȽϺõģ���ܶ�õ������������Ч������һ��loading��Ч����һ����ͼƬ�˾�ȥ��loading����ʾͼƬ��
	 * 
	 * //���ﻹ����һ�����AsyncTask�������صķ���doInBackground���������漰������UI���棬��Ȼ�����
	 * ������˵��������ʱ�����������������ʱ���Ǻ���֪������������ԭ���µġ�
	 * 
	 * �첽����һ��viewʱ�����
	 * �Ǹ�view������EditText��EditText��ÿ�μ���view���ᴥ�����㣬��ʱ���첽�ͻ��������������˵��̫�����
	 * ����������Ծͻᷢ�����������
	 */
	public class ImageLoadTask extends AsyncTask<Void, Void, Void> {
	       private ImageAdapter adapter;
	       // ��ʼ��
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
	              publishProgress(); // ֪ͨȥ����UI
	           }

	           return null;
	       }

	       public void onProgressUpdate(Void... voids) {
	           if (isCancelled())
	              return;
	           // ����UI
	           adapter.notifyDataSetChanged();
	       }
	    }
}
