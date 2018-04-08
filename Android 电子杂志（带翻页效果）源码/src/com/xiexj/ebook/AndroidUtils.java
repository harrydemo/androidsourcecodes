package com.xiexj.ebook;

import java.util.ArrayList;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class AndroidUtils {

    public static DisplayMetrics getDisplayMetrics(Context cx) {
        DisplayMetrics dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        return dm;
    }
	
	public static ArrayList<ArrayList<String>> getPageContentStringInfo(Paint m_paint,String content,int pageLines,float pageWidth)
	{
		char ch;
		int w = 0;
		int istart = 0;
		int lineNum = 0;
		ArrayList<ArrayList<String>> contentList = new ArrayList<ArrayList<String>>();
		ArrayList<String> cl = null;
		for (int i = 0; i < content.length(); i++)
		{
			if(cl == null)
				cl = new ArrayList<String>();
			ch = content.charAt(i);
			float[] widths = new float[1];
			String srt = String.valueOf(ch);
			m_paint.getTextWidths(srt, widths);

			if (ch == '\n')
			{
				lineNum++;
				cl.add(content.substring(istart, i));
				istart = i + 1;
				w = 0;
			}
			else
			{
				w += (int) (Math.ceil(widths[0]));
				if (w > pageWidth)
				{
					lineNum++;
					cl.add(content.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				}
				else
				{
					if (i == (content.length() - 1))
					{
						lineNum++;
						cl.add(content.substring(istart, content.length()));
					}
				}
			}
			if(lineNum==pageLines||i==(content.length()-1)){
				contentList.add(cl);
				cl = null;
				lineNum = 0;
			}
		}
		return contentList;
	}
	
	public static void setFullNoTitleScreen(Activity context){
		context.setTheme(R.style.Theme_Black_NoTitleBar_Fullscreen);
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
}
