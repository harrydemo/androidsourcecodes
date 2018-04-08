package cn.edu.xtu.tilepuzzle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{
	// ����Context
	private Context		mContext;
	/**
	 * ������������ ��ͼƬԴ
	 * 0	��Ů
	 * 1	����
	 * 2	����
	 * 3	����
	 * */
	private int flag;
	// ������������ ��ͼƬԴ
	private int[][] mImageIds = ClassGameDB.mImageIds;

	public ImageAdapter(Context c,int flag)
	{
		mContext = c;
		this.flag=flag;
	}

	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return mImageIds[flag].length;
	}

	// ��ȡͼƬ�ڿ��е�λ��
	public Object getItem(int position)
	{
		return position;
	}


	// ��ȡͼƬID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			// ��ImageView������Դ
			imageView = new ImageView(mContext);
			// ���ò��� ͼƬ150��150��ʾ
			imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
			// ������ʾ��������
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mImageIds[flag][position]);
		return imageView;
	}

}

