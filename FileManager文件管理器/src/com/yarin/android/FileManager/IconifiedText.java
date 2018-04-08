package com.yarin.android.FileManager;

import android.graphics.drawable.Drawable;

public class IconifiedText implements Comparable<IconifiedText>
{
	/* �ļ��� */
	private String		mText		= "";
	/* �ļ���ͼ��ICNO */
	private Drawable	mIcon		= null;
	/* �ܷ�ѡ�� */
	private boolean	mSelectable	= true;
	public IconifiedText(String text, Drawable bullet)
	{
		mIcon = bullet;
		mText = text;
	}
	//�Ƿ����ѡ��
	public boolean isSelectable()
	{
		return mSelectable;
	}
	//�����Ƿ����ѡ��
	public void setSelectable(boolean selectable)
	{
		mSelectable = selectable;
	}
	//�õ��ļ���
	public String getText()
	{
		return mText;
	}
	//�����ļ���
	public void setText(String text)
	{
		mText = text;
	}
	//����ͼ��
	public void setIcon(Drawable icon)
	{
		mIcon = icon;
	}
	//�õ�ͼ��
	public Drawable getIcon()
	{
		return mIcon;
	}
	//�Ƚ��ļ����Ƿ���ͬ
	public int compareTo(IconifiedText other)
	{
		if (this.mText != null)
			return this.mText.compareTo(other.getText());
		else
			throw new IllegalArgumentException();
	}
}
