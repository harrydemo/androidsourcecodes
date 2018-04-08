package com.alex.media;

import java.util.HashMap;

import android.content.Context;
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
		if (albums[position].length()>15){
			album.setText(albums[position].substring(0, 12)+"...");
		} else {
			album.setText(albums[position]);
		}
		/**
		 * 设置艺术家姓名
		 */
		TextView artist = (TextView)convertView.findViewById(R.id.mysinger);
		if (albums[position].equals("sdcard")){
			artist.setText("未知艺术家");
		} else{
			artist.setText(myMap.get(albums[position]));
		}
		
		ImageView Albumsitem = (ImageView)convertView.findViewById(R.id.Albumsitem);
		Albumsitem.setImageResource(R.drawable.album);
		return convertView;
	}
	
	

}
