package com.yarin.android.FileManager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconifiedTextView extends LinearLayout
{
	//һ���ļ������ļ�����ͼ��
	//����һ����ֱ���Բ���
	private TextView	mText	= null;
	private ImageView	mIcon	= null;
	public IconifiedTextView(Context context, IconifiedText aIconifiedText) 
	{
		super(context);
		//���ò��ַ�ʽ
		this.setOrientation(HORIZONTAL);
		mIcon = new ImageView(context);
		//����ImageViewΪ�ļ���ͼ��
		mIcon.setImageDrawable(aIconifiedText.getIcon());
		//����ͼ���ڸò����е����λ��
		mIcon.setPadding(8, 12, 6, 12); 
		//��ImageView��ͼ����ӵ��ò�����
		addView(mIcon,  new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		//�����ļ�������䷽ʽ�������С
		mText = new TextView(context);
		mText.setText(aIconifiedText.getText());
		mText.setPadding(8, 6, 6, 10); 
		mText.setTextSize(26);
		//���ļ�����ӵ�������
		addView(mText, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	//�����ļ���
	public void setText(String words)
	{
		mText.setText(words);
	}
	//����ͼ��
	public void setIcon(Drawable bullet)
	{
		mIcon.setImageDrawable(bullet);
	}
}

