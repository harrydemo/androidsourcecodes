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
		 * 设置专辑名
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
		 * 设置艺术家姓名
		 */
		TextView artist = (TextView)convertView.findViewById(R.id.mysinger);
		if (myMap.get(albums[position]).equals("<unknown>")){
			artist.setText("未知艺术家");
		} else{
			artist.setText(myMap.get(albums[position]));
		}
		
		ImageView Albumsitem = (ImageView)convertView.findViewById(R.id.Albumsitem);
		Albumsitem.setImageResource(R.drawable.album);
		return convertView;
	}
	
	/**
	 * 字符串裁剪
	 * @param s
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static String bSubstring(String s, int length) throws Exception  
	{  
	 
	    byte[] bytes = s.getBytes("Unicode");  
	    int n = 0; // 表示当前的字节数  
	    int i = 2; // 要截取的字节数，从第3个字节开始  
	    for (; i < bytes.length && n < length; i++)  
	    {  
	        // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节  
	        if (i % 2 == 1)  
	        {  
	            n++; // 在UCS2第二个字节时n加1  
	        }  
	        else 
	        {  
	            // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节  
	            if (bytes[i] != 0)  
	            {  
	                n++;  
	            }  
	        }  
	    }  
	    // 如果i为奇数时，处理成偶数  
	    if (i % 2 == 1)  
	 
	    {  
	        // 该UCS2字符是汉字时，去掉这个截一半的汉字  
	        if (bytes[i - 1] != 0)  
	            i = i - 1;  
	        // 该UCS2字符是字母或数字，则保留该字符  
	        else 
	            i = i + 1;  
	    }  
	 
	    return new String(bytes, 0, i, "Unicode");  
	}  

}
