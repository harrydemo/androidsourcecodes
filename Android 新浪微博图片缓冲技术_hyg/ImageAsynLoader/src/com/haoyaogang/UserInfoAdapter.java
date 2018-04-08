package com.haoyaogang;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.httpclient.HttpConnection;

import weibo4android.Status;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoAdapter extends BaseAdapter {

	private Context context;
	private List<Status> user_status;
	
	public UserInfoAdapter(Context context, List<Status> Status)
	{
		this.context = context;
		this.user_status = Status;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return user_status.size();
	}

	@Override
	public Object getItem(int paramInt) {
		// TODO Auto-generated method stub
		return user_status.get(paramInt);
	}

	@Override
	public long getItemId(int paramInt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View paramView, ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		View v = null;
		if(null != paramView)
		{
			v = paramView;
		}
		else
		{
			v = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			//image head user
			ImageView imageView = (ImageView) v.findViewById(R.id.user_head);
			//text
			TextView textView = (TextView) v.findViewById(R.id.user_name);
			
			StringBuilder str_builder = new StringBuilder();
			Status user_info = user_status.get(position);
			
			URL pic_url = user_info.getUser().getProfileImageURL();
			//
			
			//URLConnection conn =  URLConnection. //(new URL(pic_url));
//			try {
//				URL url = new URL(pic_url);
//				URLConnection conn = url.openConnection();
//				
//				BitmapDrawable bitmapDrawable = new BitmapDrawable(conn.getInputStream());
//				imageView.setImageBitmap(bitmapDrawable.getBitmap());
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
//			try {
//				imageView.setImageDrawable(Drawable.createFromStream(pic_url.openStream(), ""));
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			String image_str_url = pic_url.toString();
			
			AsycnBitmapLoader bitmapLoader = new AsycnBitmapLoader(context);
			
			bitmapLoader.loadBitmap(imageView, image_str_url, new AsycnBitmapLoader.ImageCallback() {
				
				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					// TODO Auto-generated method stub
					imageView.setImageBitmap(bitmap);
				}
			});
			
			//
			str_builder.append(user_info.getUser().getScreenName()+"\n");
			str_builder.append(user_info.getText());
			textView.setText(str_builder.toString());
			
			
		}
		
		
		
		return v;
	}

}
