package com.genius.adapter;

import java.util.List;

import com.genius.demo.R;
import com.genius.demo.R.drawable;
import com.genius.demo.R.id;
import com.genius.demo.R.layout;
import com.genius.musicplay.MusicData;
import com.genius.musicplay.MusicPlayState;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{

	private List<MusicData> mFileList;
	
	private LayoutInflater mLayoutInflater;
	
	private int 		   mCurPlayMusicIndex;
	
	private int 		   mPlayState;
	
	public ListViewAdapter(Context context, List<MusicData> FileList)
	{
		mFileList = FileList;
		mLayoutInflater = LayoutInflater.from(context);
		mCurPlayMusicIndex = -1;
		mPlayState = MusicPlayState.MPS_PREPARE;
	}
	
	public void refreshAdapter(List<MusicData> FileList)
	{
		mFileList = FileList;
		notifyDataSetChanged();
	}
	
	public void setPlayState(int playIndex, int playState)
	{
		mCurPlayMusicIndex = playIndex;
		mPlayState = playState;
		notifyDataSetChanged();
	}
	
	public int getCurPlayIndex()
	{
		return mCurPlayMusicIndex;
	}
	
	public int getCurPlayState()
	{
		return mPlayState;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.listview_item, null);
		
		}

		showPlayStateIcon(convertView, position);
		
		TextView posTextView = (TextView) convertView.findViewById(R.id.musiclistPos);
		String strPosString = String.valueOf(position + 1) + ".";
		posTextView.setText(strPosString);
		
		TextView nametTextView = (TextView) convertView.findViewById(R.id.musicName);
		nametTextView.setText(mFileList.get(position).mMusicName);
		
		TextView timeTextView = (TextView) convertView.findViewById(R.id.musicTime);
		int time = mFileList.get(position).mMusicTime;
		timeTextView.setText(formatTime(time));
		
		
		TextView pathTextView = (TextView) convertView.findViewById(R.id.musicAritst); 
		pathTextView.setText(mFileList.get(position).mMusicAritst);
		
		return convertView;
	}
	
	private void showPlayStateIcon(View view, int position)
	{
		ImageView imageView = (ImageView) view.findViewById(R.id.musicplaystate);
		if (position != mCurPlayMusicIndex)
		{
			imageView.setVisibility(View.GONE);
			return ;
		}
		
		imageView.setVisibility(View.VISIBLE);
		if (mPlayState == MusicPlayState.MPS_PAUSE)
		{
			imageView.setBackgroundResource(R.drawable.list_pause_icon);
		}else{
			imageView.setBackgroundResource(R.drawable.list_play_icon);
		}
	}
	
	private static String formatTime(int time) {
    	int min = time / (1000 * 60 );
    	String sec = time % (1000 * 60) + "";
    	if(sec.length()<2){
    		sec += "000"; 
    	}
    	
    	return min+":"+sec.trim().substring(0,2);
	}

}
