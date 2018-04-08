package weibo4android.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

public class TextAutoLink
{
	//加入话题  好友  URL的连结
	public static char strarray[];
	
	public static void addURLSpan(String str,TextView textView)
	{  SpannableString ss=new SpannableString(str); 
	    strarray=str.toCharArray();
	    int l;

	    //处理话题
	    l=str.length();
	    StringBuffer sb=null;
	    boolean start=false;
	    int     startIndex=0;
	    for(int i=0;i<l;i++)
	    {    
	    if(strarray[i]=='@')
	    {start=true;
	     sb=new StringBuffer("weibo://weibo.view/");
	     startIndex=i;
	    }
	    else{
	     if(start)
	     {
	     if(strarray[i]==':')
	     {
	      ss.setSpan(new URLSpan(sb.toString()),  startIndex,i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);      
	      sb=null;
	      start=false;
	     }else{
	    sb.append(strarray[i]); 
	     }
	     }
	    }
	    
	    }

	    textView.setText(ss);  // 设定TextView话题和url和好友 连接
	    textView.setMovementMethod(LinkMovementMethod.getInstance());
	    strarray=null;
	    }
}
