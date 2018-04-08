// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   URLEncodeUtils.java

package weibo4android.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.BitSet;

public class URLEncodeUtils
{

	static BitSet dontNeedEncoding;

	public URLEncodeUtils()
	{
	}

	public static final String decodeURL(String s)
	{
		String s1;
		try
		{
			s1 = URLDecoder.decode(s, "utf-8");
		}
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			throw new RuntimeException(unsupportedencodingexception);
		}
		return s1;
	}

	public static final String encodeURL(String s)
	{
		String s1;
		try
		{
			s1 = URLEncoder.encode(s, "utf-8");
		}
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			throw new RuntimeException(unsupportedencodingexception);
		}
		return s1;
	}

	public static final boolean isURLEncoded(String s)
	{
		/*if (s != null && !"".equals(s)) goto _L2; else goto _L1
_L1:
		int i = 0;
_L4:
		return i;
_L2:
		char ac[];
		boolean flag;
		int j;
		ac = s.toCharArray();
		flag = false;
		i = ac.length;
		j = 0;
_L5:
		char c;
		if (j >= i)
		{
			if (!flag)
				i = 0;
			else
				i = 1;
		} else
		{
			c = ac[j];
			if (Character.isWhitespace(c))
			{
				i = 0;
			} else
			{
label0:
				{
					if (dontNeedEncoding.get(c))
						break label0;
					i = 0;
				}
			}
		}
		if (true) goto _L4; else goto _L3
_L3:
		if (c != '%');
		j++;
		  goto _L5*/
		return false;
	}

	static 
	{
		/*int i;
		dontNeedEncoding = new BitSet(256);
		i = 97;
_L5:
		if (i <= 122) goto _L2; else goto _L1
_L1:
		i = 65;
_L6:
		if (i <= 90) goto _L4; else goto _L3
_L3:
		i = 48;
_L7:
		if (i > 57)
		{
			dontNeedEncoding.set(32);
			dontNeedEncoding.set(45);
			dontNeedEncoding.set(95);
			dontNeedEncoding.set(46);
			dontNeedEncoding.set(42);
			dontNeedEncoding.set(43);
			dontNeedEncoding.set(37);
			return;
		}
		break MISSING_BLOCK_LABEL_125;
_L2:
		dontNeedEncoding.set(i);
		i++;
		  goto _L5
_L4:
		dontNeedEncoding.set(i);
		i++;
		  goto _L6
		dontNeedEncoding.set(i);
		i++;
		  goto _L7*/
	}
}
