package com.genius.demo.adapter;

import java.util.List;

import com.genius.demo.R;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkinListAdapter extends BaseAdapter{

	private Context mContext;
	
	private LayoutInflater mLayoutInflater;
	
	private List<SkinListItemData> mItemDataList;
	
	private int mCurSelect = -1;
	
	public SkinListAdapter(Context context, List<SkinListItemData> itemDatalist)
	{
		mContext = context;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		mItemDataList = itemDatalist;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mItemDataList == null)
		{
			return 0;
		}
		
		return mItemDataList.size();
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
	
	public void setSelect(int pos)
	{
		if (pos >= 0 && pos < mItemDataList.size())
		{
			mCurSelect = pos;
			notifyDataSetChanged();
		}
			
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(R.layout.skin_listview_item, null);
		}
		
		ImageView imageViewLeft = (ImageView) convertView.findViewById(R.id.imageLeft);
		ImageView imageViewRight = (ImageView) convertView.findViewById(R.id.imageRight);
		TextView textView = (TextView) convertView.findViewById(R.id.skinname);
		
		if (mItemDataList != null)
		{
			imageViewLeft.setImageResource(mItemDataList.get(position).mImageViewLeftID);
			textView.setText(mItemDataList.get(position).mTextView);
			
			imageViewRight.setImageResource(mItemDataList.get(position).mImageViewRightID);	

			Log.i("", "position = " + position + ", curSelect = " + mCurSelect);
			
			if (position == mCurSelect)
			{
				imageViewRight.setImageResource(R.drawable.themeradio_check);
			}
						
		}
		
		return convertView;
	}

}
