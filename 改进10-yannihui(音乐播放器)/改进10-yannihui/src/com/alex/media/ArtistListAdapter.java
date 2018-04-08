package com.alex.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArtistListAdapter extends BaseAdapter{

	private Context myCon;
	private String[] artists;
	private int[] counts;
	
	public ArtistListAdapter(Context con,String[] str1,int[] counts){
		myCon = con;
		artists = str1;
		this.counts = counts;
	}
	@Override
	public int getCount() {
		return artists.length;
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
		convertView = LayoutInflater.from(myCon).inflate(R.layout.artistslist,
				null);
		
		//设置艺术家姓名
		TextView artist = (TextView)convertView.findViewById(R.id.artist);
		if (artists[position].length()>15)
			artist.setText(artists[position].substring(0, 12)+"...");
		else 
			artist.setText(artists[position]);
		
		//设置歌手拥有的歌曲数
		TextView musicCounts = (TextView)convertView.findViewById(R.id.counts);
		musicCounts.setText("共有" + counts[position] + "首歌曲");
		//设置列表项图标
		ImageView Artistsitem = (ImageView)convertView.findViewById(R.id.Artistsitem);
		Artistsitem.setImageResource(R.drawable.artist);
		return convertView;
	}

}
