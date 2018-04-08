package com.yarin.android.FileManager;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//ʹ��BaseAdapter���洢ȡ�õ��ļ�
public class IconifiedTextListAdapter extends BaseAdapter
{
	private Context				mContext	= null;
	// ������ʾ�ļ����б�
	private List<IconifiedText>	mItems		= new ArrayList<IconifiedText>();
	public IconifiedTextListAdapter(Context context)
	{
		mContext = context;
	}
	//���һ�һ���ļ���
	public void addItem(IconifiedText it) { mItems.add(it); }
	//�����ļ��б�
	public void setListItems(List<IconifiedText> lit) { mItems = lit; }
	//�õ��ļ�����Ŀ,�б�ĸ���
	public int getCount() { return mItems.size(); }
	//�õ�һ���ļ�
	public Object getItem(int position) { return mItems.get(position); }
	//�ܷ�ȫ��ѡ��
	public boolean areAllItemsSelectable() { return false; }
	//�ж�ָ���ļ��Ƿ�ѡ��
	public boolean isSelectable(int position) 
	{ 
		return mItems.get(position).isSelectable();
	}
	//�õ�һ���ļ���ID
	public long getItemId(int position) { return position; }
	//��дgetView����������һ��IconifiedTextView�������Զ�����ļ����֣�����
	public View getView(int position, View convertView, ViewGroup parent) {
		IconifiedTextView btv;
		if (convertView == null) 
		{
			btv = new IconifiedTextView(mContext, mItems.get(position));
		} 
		else 
		{
			btv = (IconifiedTextView) convertView;
			btv.setText(mItems.get(position).getText());
			btv.setIcon(mItems.get(position).getIcon());
		}
		return btv;
	}
}

