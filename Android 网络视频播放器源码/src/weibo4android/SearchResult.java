// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SearchResult.java

package weibo4android;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import weibo4android.http.Response;
import weibo4android.org.json.JSONArray;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

// Referenced classes of package weibo4android:
//			WeiboResponse, WeiboException

public class SearchResult extends WeiboResponse
	implements Serializable
{

	private static final long serialVersionUID = 0x722d7e8255df2773L;
	private Date createdAt;
	private String from_user;
	private long from_user_id;
	private long id;
	private String iso_language_code;
	private String profileImageUrl;
	private String source;
	private String text;
	private String to_user;
	private long to_user_id;

	public SearchResult(JSONObject jsonobject)
		throws WeiboException, JSONException
	{
		Date date = parseDate(jsonobject.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
		createdAt = date;
		long l = jsonobject.getLong("to_user_id");
		to_user_id = l;
		String s = jsonobject.getString("to_user");
		to_user = s;
		String s1 = jsonobject.getString("text");
		text = s1;
		String s2 = jsonobject.getString("source");
		source = s2;
		long l1 = jsonobject.getLong("id");
		id = l1;
		long l2 = jsonobject.getLong("from_user_id");
		from_user_id = l2;
		String s3 = jsonobject.getString("from_user");
		from_user = s3;
		String s4 = jsonobject.getString("iso_language_code");
		iso_language_code = s4;
		String s5 = jsonobject.getString("profile_image_url");
		profileImageUrl = s5;
	}

	public static List constructResults(Response response)
		throws WeiboException
	{
		/*JSONObject jsonobject = response.asJSONObject();
		JSONArray jsonarray;
		int i;
		ArrayList arraylist;
		int j;
		jsonarray = jsonobject.getJSONArray("results");
		i = jsonarray.length();
		arraylist = new ArrayList(i);
		j = 0;
_L1:
		if (j >= i)
			return arraylist;
		JSONObject jsonobject1 = jsonarray.getJSONObject(j);
		SearchResult searchresult = new SearchResult(jsonobject1);
		boolean flag = arraylist.add(searchresult);
		j++;
		  goto _L1
		JSONException jsonexception;
		jsonexception;
		throw new WeiboException(jsonexception);*/
		return null;
	}

	public boolean equals(Object obj)
	{
		/*if (this != obj) goto _L2; else goto _L1
_L1:
		boolean flag = true;
_L4:
		return flag;
_L2:
		if (obj == null)
		{
			flag = false;
			continue;  Loop/switch isn't completed 
		}
		int i = getClass();
		int j = obj.getClass();
		if (i != j)
		{
			flag = false;
			continue;  Loop/switch isn't completed 
		}
		SearchResult searchresult = (SearchResult)obj;
		if (from_user == null)
		{
			if (searchresult.from_user != null)
			{
				flag = false;
				continue;  Loop/switch isn't completed 
			}
		} else
		{
			String s = from_user;
			String s1 = searchresult.from_user;
			if (!s.equals(s1))
			{
				flag = false;
				continue;  Loop/switch isn't completed 
			}
		}
		long l = from_user_id;
		long l1 = searchresult.from_user_id;
		if (l != l1)
		{
			flag = false;
			continue;  Loop/switch isn't completed 
		}
		long l2 = id;
		long l3 = searchresult.id;
		if (l2 != l3)
		{
			flag = false;
			continue;  Loop/switch isn't completed 
		}
		if (to_user == null)
		{
			if (searchresult.to_user != null)
			{
				flag = false;
				continue;  Loop/switch isn't completed 
			}
		} else
		{
			String s2 = to_user;
			String s3 = searchresult.to_user;
			if (!s2.equals(s3))
			{
				flag = false;
				continue;  Loop/switch isn't completed 
			}
		}
		long l4 = to_user_id;
		long l5 = searchresult.to_user_id;
		if (l4 != l5)
			flag = false;
		else
			flag = true;
		if (true) goto _L4; else goto _L3
_L3:*/
		return false;
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public String getFromUser()
	{
		return from_user;
	}

	public long getFromUserId()
	{
		return from_user_id;
	}

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return iso_language_code;
	}

	public URL getProfileImageURL()
	{
		URL url;
		try
		{
			String s = profileImageUrl;
			url = new URL(s);
		}
		catch (MalformedURLException malformedurlexception)
		{
			url = null;
		}
		return url;
	}

	public String getSource()
	{
		return source;
	}

	public String getText()
	{
		return text;
	}

	public String getToUser()
	{
		return to_user;
	}

	public long getToUserId()
	{
		return to_user_id;
	}

	public int hashCode()
	{
		int i = 1 * 31;
		int j;
		int k;
		long l;
		long l1;
		int i1;
		int j1;
		long l2;
		long l3;
		int k1;
		int i2;
		int j2;
		long l4;
		long l5;
		int k2;
		if (from_user == null)
			j = 0;
		else
			j = from_user.hashCode();
		k = (j + 31) * 31;
		l = from_user_id;
		l1 = from_user_id >>> 32;
		i1 = (int)(l ^ l1);
		j1 = (k + i1) * 31;
		l2 = id;
		l3 = id >>> 32;
		k1 = (int)(l2 ^ l3);
		j = (j1 + k1) * 31;
		if (to_user == null)
			i2 = 0;
		else
			i2 = to_user.hashCode();
		j2 = (j + i2) * 31;
		l4 = to_user_id;
		l5 = to_user_id >>> 32;
		k2 = (int)(l4 ^ l5);
		return j2 + k2;
	}

	public String toString()
	{
		StringBuilder stringbuilder = new StringBuilder("Result{ ");
		long l = to_user_id;
		StringBuilder stringbuilder1 = stringbuilder.append(l).append(",");
		String s = to_user;
		StringBuilder stringbuilder2 = stringbuilder1.append(s).append(",");
		String s1 = text;
		StringBuilder stringbuilder3 = stringbuilder2.append(s1).append(",");
		long l1 = id;
		StringBuilder stringbuilder4 = stringbuilder3.append(l1).append(",");
		long l2 = from_user_id;
		StringBuilder stringbuilder5 = stringbuilder4.append(l2).append(",");
		String s2 = from_user;
		StringBuilder stringbuilder6 = stringbuilder5.append(s2).append(",");
		String s3 = iso_language_code;
		StringBuilder stringbuilder7 = stringbuilder6.append(s3).append(",");
		String s4 = source;
		StringBuilder stringbuilder8 = stringbuilder7.append(s4).append(",");
		String s5 = profileImageUrl;
		StringBuilder stringbuilder9 = stringbuilder8.append(s5).append(",");
		Date date = createdAt;
		return stringbuilder9.append(date).append('}').toString();
	}
}
