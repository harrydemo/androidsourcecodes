package com.test;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* �Զ����Adapter���̳�android.widget.BaseAdapter */
public class MyAdapter extends BaseAdapter {
  
	/* �������� 
     mIcon1���ص���Ŀ¼��ͼ�ļ�
     mIcon2���ص���һ���ͼ��
     mIcon3���ļ��е�ͼ�ļ�
     mIcon4���ļ���ͼ��
  */
  private LayoutInflater mInflater;
  private Bitmap mIcon1;
  private Bitmap mIcon2;
  private Bitmap mIcon3;
  private Bitmap mIcon4;
  private List<String> items;
  private List<String> paths;
  
  /* MyAdapter�Ĺ�������������������  */  
  public MyAdapter(Context context,List<String> it,List<String> pa){
    /* ������ʼ�� */
    mInflater = LayoutInflater.from(context);
    items = it;
    paths = pa;
    mIcon1 = BitmapFactory.decodeResource(context.getResources(),
                                          R.drawable.back01);
    mIcon2 = BitmapFactory.decodeResource(context.getResources(),
                                          R.drawable.back02);
    mIcon3 = BitmapFactory.decodeResource(context.getResources(),
                                          R.drawable.folder);
    mIcon4 = BitmapFactory.decodeResource(context.getResources(),
                                          R.drawable.doc);
  }
  
  /* ��̳�BaseAdapter���踲�����·��� */
  public int getCount()
  {
    return items.size();
  }

  public Object getItem(int position)
  {
    return items.get(position);
  }
  
  public long getItemId(int position)
  {
    return position;
  }
  
  public View getView(int position,View convertView,ViewGroup parent)
  {
    ViewHolder holder;
    
    if(convertView == null){
      /* ʹ���Զ����file_row��ΪLayout */
      convertView = mInflater.inflate(R.layout.file_row, null);
      /* ��ʼ��holder��text��icon */
      holder = new ViewHolder();
      holder.text = (TextView) convertView.findViewById(R.id.text);
      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
      
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }

    File f=new File(paths.get(position).toString());
    /* ����[�ص���Ŀ¼]��������icon */
    if(items.get(position).toString().equals("b1"))
    {
      holder.text.setText("Back to /");
      holder.icon.setImageBitmap(mIcon1);
    }
    /* ����[�ص���һ��]��������icon */
    else if(items.get(position).toString().equals("b2"))
    {
      holder.text.setText("Back to ..");
      holder.icon.setImageBitmap(mIcon2);
    }
    /* ����[�ļ����ļ���]��������icon */
    else
    {
      holder.text.setText(f.getName());
      if(f.isDirectory())
      {
        holder.icon.setImageBitmap(mIcon3);
      }
      else
      {
        holder.icon.setImageBitmap(mIcon4);
      }
    }
    return convertView;
  }
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
    ImageView icon;
  }
}
