// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UserTrend.java

package weibo4android;

import java.util.ArrayList;
import java.util.List;
import weibo4android.http.Response;
import weibo4android.org.json.JSONArray;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

// Referenced classes of package weibo4android:
//			WeiboResponse, WeiboException

public class UserTrend extends WeiboResponse
{

	private static final long serialVersionUID = 0x1aba5f6b262a250aL;
	private String hotword;
	private String num;
	private String trend_id;

	public UserTrend()
	{
		hotword = null;
		trend_id = null;
	}

	public UserTrend(Response response)
		throws WeiboException
	{
		super(response);
		hotword = null;
		trend_id = null;
		JSONObject jsonobject = response.asJSONObject();
		try
		{
			String s = jsonobject.getString("num");
			num = s;
			String s1 = jsonobject.getString("hotword");
			hotword = s1;
			String s2 = jsonobject.getString("trend_id");
			trend_id = s2;
			if (jsonobject.getString("topicid") != null)
			{
				String s3 = jsonobject.getString("topicid");
				trend_id = s3;
			}
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

	public UserTrend(JSONObject jsonobject)
		throws WeiboException
	{
		hotword = null;
		trend_id = null;
		try
		{
			String s = jsonobject.getString("num");
			num = s;
			String s1 = jsonobject.getString("hotword");
			hotword = s1;
			String s2 = jsonobject.getString("trend_id");
			trend_id = s2;
			return;
		}
		catch (JSONException jsonexception)
		{
			String s3 = String.valueOf(jsonexception.getMessage());
			StringBuilder stringbuilder = (new StringBuilder(s3)).append(":");
			String s4 = jsonobject.toString();
			String s5 = stringbuilder.append(s4).toString();
			throw new WeiboException(s5, jsonexception);
		}
	}

	static List constructTrendList(Response response)
		throws WeiboException
	{
		/*JSONArray jsonarray;
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
		UserTrend usertrend = new UserTrend(jsonobject);
		boolean flag = arraylist.add(usertrend);
		j++;
		  goto _L1
		JSONException jsonexception;
		jsonexception;
		throw new WeiboException(jsonexception);
		throw ;*/
		return null;
	}
	

	public String getHotword()
	{
		return hotword;
	}

	public String getNum()
	{
		return num;
	}

	public String getTrend_id()
	{
		return trend_id;
	}

	public void setHotword(String s)
	{
		hotword = s;
	}

	public void setNum(String s)
	{
		num = s;
	}

	public void setTrend_id(String s)
	{
		trend_id = s;
	}

	public String toString()
	{
		StringBuilder stringbuilder = new StringBuilder("Trend [num=");
		String s = num;
		StringBuilder stringbuilder1 = stringbuilder.append(s).append(", hotword=");
		String s1 = hotword;
		StringBuilder stringbuilder2 = stringbuilder1.append(s1).append(", trend_id=");
		String s2 = trend_id;
		return stringbuilder2.append(s2).append("]").toString();
	}
}
