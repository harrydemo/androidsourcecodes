package com.bus.shenyang.common;

import com.bus.shenyang.R;
import com.bus.shenyang.activity.Internet;
import com.bus.shenyang.activity.Line;
import com.bus.shenyang.activity.More;
import com.bus.shenyang.activity.Stop;
import com.bus.shenyang.activity.Transfer;
import com.bus.shenyang.net.NoSubway;
import com.bus.shenyang.net.TimeFirst;
import com.bus.shenyang.net.TransferFirst;
import com.bus.shenyang.net.WalkFirst;


public class Commons {
	public static String mTextviewArray[] = {"��·��ѯ", "վ���ѯ", "���˲�ѯ", "�����ѯ", "���ڸ���" };
	public static Class mTabClassArray[] = {Line.class,Stop.class,Transfer.class,Internet.class,More.class };
	public static int mImageViewArray[] = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
		R.drawable.ic_launcher, R.drawable.ic_launcher };
	public static String  TextviewArray[]={"����ʱ��", "���ٻ���", "���ٲ���", "��������" };
	public static Class  TabClassArray[] ={TimeFirst.class,TransferFirst.class,WalkFirst.class,NoSubway.class };
	public static int  ImageViewArray[] = {
		R.drawable.ic_launcher, R.drawable.ic_launcher,
		R.drawable.ic_launcher, R.drawable.ic_launcher };
}
