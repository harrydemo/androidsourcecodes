// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RFC1522Codec.java

package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

abstract class RFC1522Codec
{

	RFC1522Codec()
	{
	}

	protected String decodeText(String s)
		throws DecoderException, UnsupportedEncodingException
	{
		String s1;
		if (s == null)
		{
			s1 = null;
		} else
		{
			if (!s.startsWith("=?") || !s.endsWith("?="))
				throw new DecoderException("RFC 1522 violation: malformed encoded content");
			int i = s.length() - 2;
			int j = 2;
			int k = s.indexOf("?", j);
			if (k == -1 || k == i)
				throw new DecoderException("RFC 1522 violation: charset token not found");
			String s2 = s.substring(j, k);
			if (s2.equals(""))
				throw new DecoderException("RFC 1522 violation: charset not specified");
			j = k + 1;
			k = s.indexOf("?", j);
			if (k == -1 || k == i)
				throw new DecoderException("RFC 1522 violation: encoding token not found");
			String s3 = s.substring(j, k);
			if (!getEncoding().equalsIgnoreCase(s3))
			{
				String s4 = "This codec cannot decode " + s3 + " encoded content";
				throw new DecoderException(s4);
			}
			int l = k + 1;
			int i1 = s.indexOf("?", l);
			byte abyte0[] = s.substring(l, i1).getBytes("US-ASCII");
			byte abyte1[] = doDecoding(abyte0);
			s1 = new String(abyte1, s2);
		}
		return s1;
	}

	protected abstract byte[] doDecoding(byte abyte0[])
		throws DecoderException;

	protected abstract byte[] doEncoding(byte abyte0[])
		throws EncoderException;

	protected String encodeText(String s, String s1)
		throws EncoderException, UnsupportedEncodingException
	{
		String s2;
		if (s == null)
		{
			s2 = null;
		} else
		{
			StringBuffer stringbuffer = new StringBuffer();
			StringBuffer stringbuffer1 = stringbuffer.append("=?");
			StringBuffer stringbuffer2 = stringbuffer.append(s1);
			StringBuffer stringbuffer3 = stringbuffer.append('?');
			String s3 = getEncoding();
			StringBuffer stringbuffer4 = stringbuffer.append(s3);
			StringBuffer stringbuffer5 = stringbuffer.append('?');
			byte abyte0[] = s.getBytes(s1);
			byte abyte1[] = doEncoding(abyte0);
			String s4 = new String(abyte1, "US-ASCII");
			StringBuffer stringbuffer6 = stringbuffer.append(s4);
			StringBuffer stringbuffer7 = stringbuffer.append("?=");
			s2 = stringbuffer.toString();
		}
		return s2;
	}

	protected abstract String getEncoding();
}
