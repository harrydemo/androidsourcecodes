package com.alex.media;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumListAdapter extends BaseAdapter{

	private Context myCon;
	private String[] albums;
	private HashMap<String, String> myMap;

	public AlbumListAdapter(Context con,String[] str1, HashMap<String, String> map){
		myCon = con;
		albums = str1;
		myMap = map;
	}
	
	@Override
	public int getCount() {
		return albums.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(myCon).inflate(R.layout.albumslist,
				null);
		/**
		 * ����ר����
		 */
		TextView album = (TextView)convertView.findViewById(R.id.album);
		if (albums[position].length()>24){
			try {
				String albumName = bSubstring(albums[position],24);
				album.setText(albumName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			album.setText(albums[position]);
		}
		/**
		 * ��������������
		 */
		TextView artist = (TextView)convertView.findViewById(R.id.mysinger);
		if (myMap.get(albums[position]).equals("<unknown>")){
			artist.setText("δ֪������");
		} else{
			artist.setText(myMap.get(albums[position]));
		}
		
		ImageView Albumsitem = (ImageView)convertView.findViewById(R.id.Albumsitem);
		Albumsitem.setImageResource(R.drawable.album);
		return convertView;
	}
	
	/**
	 * �ַ����ü�
	 * @param s
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static String bSubstring(String s, int length) throws Exception  
	{  
	 
	    byte[] bytes = s.getBytes("Unicode");  
	    int n = 0; // ��ʾ��ǰ���ֽ���  
	    int i = 2; // Ҫ��ȡ���ֽ������ӵ�3���ֽڿ�ʼ  
	    for (; i < bytes.length && n < length; i++)  
	    {  
	        // ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�  
	        if (i % 2 == 1)  
	        {  
	            n++; // ��UCS2�ڶ����ֽ�ʱn��1  
	        }  
	        else 
	        {  
	            // ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�  
	            if (bytes[i] != 0)  
	            {  
	                n++;  
	            }  
	        }  
	    }  
	    // ���iΪ����ʱ�������ż��  
	    if (i % 2 == 1)  
	 
	    {  
	        // ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ���  
	        if (bytes[i - 1] != 0)  
	            i = i - 1;  
	        // ��UCS2�ַ�����ĸ�����֣��������ַ�  
	        else 
	            i = i + 1;  
	    }  
	 
	    return new String(bytes, 0, i, "Unicode");  
	}  

}
