package com.hotmail.liuxuewei;

import java.util.Random;

import com.work.R;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewImage extends ListActivity {
	 private   int random[]=new int[8];//7��0~7���ظ������
     private  Random creatRandom=new Random();
 	private final static String [] week={"������","����һ","���ڶ�","������","������","������","������","��Ϣ"};
	private final static String [] name={"�� ��","�� ��","�� ��","�� ��","�� ��","С ��","�� ��","�� ү"};
	@Override
	public void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);
		setListAdapter(new EfficientAdapter(this));
		 for(int i=0;i<8;i++){ 
             random[i]=(creatRandom.nextInt(8));
         for(int j=0;j<i;j++)
             if(random[j]==random[i])
               {
                    random[i]=(creatRandom.nextInt(8));
                    j=-1; 
               }
            }   
	}

	private  class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Bitmap mIcon0;
		private Bitmap mIcon1;
		private Bitmap mIcon2;
		private Bitmap mIcon3;
		private Bitmap mIcon4;
		private Bitmap mIcon5;
		private Bitmap mIcon6;
		private Bitmap mIcon7;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			mIcon0 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.piaopiao);
			mIcon1= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.rongmei);
			mIcon2 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.xiongmei);
			mIcon3 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.chuanmei);
			mIcon4 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.mianfeng);
			mIcon5 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.xiaogui);
			mIcon6 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.bibi);
			mIcon7 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.qingye);
		}

		public int getCount() {
			return name.length;
		}

		public Object getItem(int position) {
			return name[random[position]];
		}

		public long getItemId(int position) {
			return random[position];
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.main, null);

				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textview);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(name[random[position]]+"   "+week[position]);
			switch (random[position]) {
			case 0:
				holder.icon.setImageBitmap(mIcon0);
				break;
			case 1:
				holder.icon.setImageBitmap(mIcon1);
				break;
			case 2:
				holder.icon.setImageBitmap(mIcon2);
				break;
			case 3:
				holder.icon.setImageBitmap(mIcon3);
				break;
			case 4:
				holder.icon.setImageBitmap(mIcon4);
				break;
			case 5:
				holder.icon.setImageBitmap(mIcon5);
				break;
			case 6:
				holder.icon.setImageBitmap(mIcon6);
				break;
			case 7:
				holder.icon.setImageBitmap(mIcon7);
				break;
			}

			return convertView;
		}

		 class ViewHolder {
			TextView text;
			ImageView icon;
		}
	}

}