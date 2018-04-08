// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Emotion.java

package weibo4android;

import java.util.ArrayList;
import java.util.List;
import weibo4android.http.Response;
import weibo4android.org.json.JSONArray;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

// Referenced classes of package weibo4android:
//			WeiboResponse, WeiboException

public class Emotion extends WeiboResponse
{

	private static final long serialVersionUID = 0xc72531b534078ca2L;
	private String category;
	private boolean is_common;
	private boolean is_hot;
	private int order_number;
	private String phrase;
	private String type;
	private String url;

	public Emotion()
	{
	}

	public Emotion(Response response)
		throws WeiboException
	{
		super(response);
		JSONObject jsonobject = response.asJSONObject();
		try
		{
			String s = jsonobject.getString("phrase");
			phrase = s;
			String s1 = jsonobject.getString("type");
			type = s1;
			String s2 = jsonobject.getString("url");
			url = s2;
			boolean flag = jsonobject.getBoolean("is_hot");
			is_hot = flag;
			int i = jsonobject.getInt("order_number");
			order_number = i;
			String s3 = jsonobject.getString("category");
			category = s3;
			boolean flag1 = jsonobject.getBoolean("is_common");
			is_common = flag1;
			return;
		}
		catch (JSONException jsonexception)
		{
			String s4 = String.valueOf(jsonexception.getMessage());
			StringBuilder stringbuilder = (new StringBuilder(s4)).append(":");
			String s5 = jsonobject.toString();
			String s6 = stringbuilder.append(s5).toString();
			throw new WeiboException(s6, jsonexception);
		}
	}

	public Emotion(JSONObject jsonobject)
		throws WeiboException
	{
		try
		{
			String s = jsonobject.getString("phrase");
			phrase = s;
			String s1 = jsonobject.getString("type");
			type = s1;
			String s2 = jsonobject.getString("url");
			url = s2;
			boolean flag = jsonobject.getBoolean("is_hot");
			is_hot = flag;
			int i = jsonobject.getInt("order_number");
			order_number = i;
			String s3 = jsonobject.getString("category");
			category = s3;
			boolean flag1 = jsonobject.getBoolean("is_common");
			is_common = flag1;
			return;
		}
		catch (JSONException jsonexception)
		{
			String s4 = String.valueOf(jsonexception.getMessage());
			StringBuilder stringbuilder = (new StringBuilder(s4)).append(":");
			String s5 = jsonobject.toString();
			String s6 = stringbuilder.append(s5).toString();
			throw new WeiboException(s6, jsonexception);
		}
	}

	static List constructEmotions(Response response)
		throws WeiboException
	{
	/*	JSONArray jsonarray;
		int i;
		ArrayList arraylist;
		int j;
		jsonarray = response.asJSONArray();
		i = jsonarray.length();
		arraylist = new ArrayList(i);
		j = 0;
_L1:
		if (j >= i)
			return arraylist;
		JSONObject jsonobject = jsonarray.getJSONObject(j);
		Emotion emotion = new Emotion(jsonobject);
		boolean flag = arraylist.add(emotion);
		j++;
		  goto _L1
		JSONException jsonexception;
		jsonexception;
		throw new WeiboException(jsonexception);
		throw ;*/
		return null;
	}

	public String getCategory()
	{
		return category;
	}

	public int getOrder_number()
	{
		return order_number;
	}

	public String getPhrase()
	{
		return phrase;
	}

	public String getType()
	{
		return type;
	}

	public String getUrl()
	{
		return url;
	}

	public boolean isIs_common()
	{
		return is_common;
	}

	public boolean isIs_hot()
	{
		return is_hot;
	}

	public void setCategory(String s)
	{
		category = s;
	}

	public void setIs_common(boolean flag)
	{
		is_common = flag;
	}

	public void setIs_hot(boolean flag)
	{
		is_hot = flag;
	}

	public void setOrder_number(int i)
	{
		order_number = i;
	}

	public void setPhrase(String s)
	{
		phrase = s;
	}

	public void setType(String s)
	{
		type = s;
	}

	public void setUrl(String s)
	{
		url = s;
	}

	public String toString()
	{
		StringBuilder stringbuilder = new StringBuilder("Emotion [phrase=");
		String s = phrase;
		StringBuilder stringbuilder1 = stringbuilder.append(s).append(", type=");
		String s1 = type;
		StringBuilder stringbuilder2 = stringbuilder1.append(s1).append(", url=");
		String s2 = url;
		StringBuilder stringbuilder3 = stringbuilder2.append(s2).append(", is_hot=");
		boolean flag = is_hot;
		StringBuilder stringbuilder4 = stringbuilder3.append(flag).append(", is_common=");
		boolean flag1 = is_common;
		StringBuilder stringbuilder5 = stringbuilder4.append(flag1).append(", order_number=");
		int i = order_number;
		StringBuilder stringbuilder6 = stringbuilder5.append(i).append(", category=");
		String s3 = category;
		return stringbuilder6.append(s3).append("]").toString();
	}
}
