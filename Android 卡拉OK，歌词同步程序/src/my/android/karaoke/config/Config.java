package my.android.karaoke.config;

import android.graphics.Color;
import android.os.Environment;

public class Config {
 
	public static String RootPath =Environment.getExternalStorageDirectory().getAbsolutePath()+ "/learneasy";
	
	public static final int IconSize = 70; //默认图标大小
	public static final int FONTCOLOR = Color.BLACK; //默认字体颜色
	
	public static final String XML_ENCODING="UTF-8";
	public static final float FONTSIZE = 20;//默认字体
	public static final int SELECTED_FONTCOLOR = Color.RED;//文字选中的颜色
}
