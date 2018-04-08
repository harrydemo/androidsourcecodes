package com.chenqi.activity;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity
{
	private GridView gridView;
	private int[] titles = new int[]
	        {R.string.register, R.string.shengxiao, R.string.xingzuo, R.string.xuexing, R.string.xzpei,R.string.xingzuoList, R.string.backgroundMusic,R.string.about};
	//{"注册","生肖","血型","星座","配对","关于" };
	//getResources().getString(R.string.register), getResources().getString(R.string.shengxiao), getResources().getString(R.string.xingzuo), getResources().getString(R.string.xuexing), getResources().getString(R.string.xzpei), getResources().getString(R.string.about)
	private int[] images = new int[]
	{ R.drawable.caoyin, R.drawable.liangyongqi, R.drawable.sunli, R.drawable.wupeici, R.drawable.yanglan, R.drawable.yangmi,R.drawable.huge,R.drawable.zhangjinchu};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gridView = (GridView) findViewById(R.id.gridview);
		PictureAdapter adapter = new PictureAdapter(titles, images, this);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{  Intent intent;
				switch (position)
				{
					case 0:
						intent=new Intent(MainActivity.this,RegisterActivity.class);
						startActivity(intent);
						break;
					case 1:
						intent=new Intent(MainActivity.this,ShengxiaoActivity.class);
						startActivity(intent);
						break;
					case 2:
						intent=new Intent(MainActivity.this,XingzuoActivity.class);
						startActivity(intent);
						break;
					case 3:
						intent=new Intent(MainActivity.this,XuexingActivity.class);
						startActivity(intent);
						break;
					case 4:
						intent=new Intent(MainActivity.this,PeiduiActivity.class);
						startActivity(intent);
						break;
					case 5:
						intent=new Intent(MainActivity.this,XingzuoListActivity.class);
						startActivity(intent);
						break;
					case 6:
						/*intent=new Intent(MainActivity.this,XingzuoListActivity.class);
						startActivity(intent);*/
						break;
					case 7:
						new AlertDialog.Builder(MainActivity.this).setTitle(R.string.title)
						.setIcon(R.drawable.qq).setMessage(R.string.Detail).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								arg0.dismiss();
							}
						}		
						).show();
						break;
				}
			}
		});
	}
}

class PictureAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	private List<Picture> pictures;

	public PictureAdapter(int[] titles, int[] images, Context context)
	{
		super();
		pictures = new ArrayList<Picture>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < images.length; i++)
		{
			Picture picture = new Picture(titles[i], images[i]);
			pictures.add(picture);
		}
	}

	@Override
	public int getCount()
	{
		if (null != pictures)
		{
			return pictures.size();
		} else
		{
			return 0;
		}
	}

	@Override
	public Object getItem(int position)
	{
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.picture_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(pictures.get(position).getTitle());
		viewHolder.image.setImageResource(pictures.get(position).getImageId());
		
		return convertView;
	}

}

class ViewHolder
{
	public TextView title;
	public ImageView image;
}

class Picture
{
	private int title;
	private int imageId;

	public Picture()
	{
		super();
	}

	public Picture(int title, int imageId)
	{
		super();
		this.title = title;
		this.imageId = imageId;
	}

	public int getTitle()
	{
		return title;
	}

	public void setTitle(int title)
	{
		this.title = title;
	}

	public int getImageId()
	{
		return imageId;
	}

	public void setImageId(int imageId)
	{
		this.imageId = imageId;
	}
}
