package my.android.karaoke.config;

import android.graphics.Color;
import android.os.Environment;

public class Config {
 
	public static String RootPath =Environment.getExternalStorageDirectory().getAbsolutePath()+ "/learneasy";
	
	public static final int IconSize = 70; //Ĭ��ͼ���С
	public static final int FONTCOLOR = Color.BLACK; //Ĭ��������ɫ
	
	public static final String XML_ENCODING="UTF-8";
	public static final float FONTSIZE = 20;//Ĭ������
	public static final int SELECTED_FONTCOLOR = Color.RED;//����ѡ�е���ɫ
}
