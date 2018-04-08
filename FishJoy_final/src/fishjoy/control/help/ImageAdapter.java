package fishjoy.control.help;

import fishjoy.control.menu.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{
	// ����Context
	private Context		mContext;		
	// ������������ ��ͼƬԴ
	private Integer[]	mImageIds = 
	{ 						
			R.drawable.help_1, 
			R.drawable.help_2, 
			R.drawable.help_3, 
			R.drawable.help_4,
	};

	// ���� ImageAdapter
	public ImageAdapter(Context c)
	{
		mContext = c;
	}

	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return mImageIds.length;
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
		ImageView imageview = new ImageView(mContext);

		// ��ImageView������Դ
		imageview.setImageResource(mImageIds[position]);
		// ���ò��� ͼƬ120��120��ʾ
		imageview.setLayoutParams(new Gallery.LayoutParams(300, 300));
		// ������ʾ��������
		imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return imageview;
	}
}

