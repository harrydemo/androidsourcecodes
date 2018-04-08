// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BCodec.java

package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.*;
import org.apache.commons.codec.binary.Base64;

// Referenced classes of package org.apache.commons.codec.net:
//			RFC1522Codec

public class BCodec extends RFC1522Codec
	implements StringEncoder, StringDecoder
{

	private String charset;

	public BCodec()
	{
		charset = "UTF-8";
	}

	public BCodec(String s)
	{
		charset = "UTF-8";
		charset = s;
	}

	public Object decode(Object obj)
		throws DecoderException
	{
		String s;
		if (obj == null)
			s = null;
		else
		if (obj instanceof String)
		{
			String s1 = (String)obj;
			s = decode(s1);
		} else
		{
			StringBuffer stringbuffer = (new StringBuffer()).append("Objects of type ");
			String s2 = obj.getClass().getName();
			String s3 = stringbuffer.append(s2).append(" cannot be decoded using BCodec").toString();
			throw new DecoderException(s3);
		}
		return s;
	}

	public String decode(String s)
		throws DecoderException
	{
		/*if (s != null) goto _L2; else goto _L1
_L1:
		String s1 = null;
_L4:
		return s1;
_L2:
		UnsupportedEncodingException unsupportedencodingexception;
		String s2;
		try
		{
			s2 = decodeText(s);
		}
		// Misplaced declaration of an exception variable
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			String s3 = unsupportedencodingexception.getMessage();
			throw new DecoderException(s3);
		}
		s1 = s2;
		if (true) goto _L4; else goto _L3
_L3:*/
		return "";
	}

	protected byte[] doDecoding(byte abyte0[])
		throws DecoderException
	{
		byte abyte1[];
		if (abyte0 == null)
			abyte1 = null;
		else
			abyte1 = Base64.decodeBase64(abyte0);
		return abyte1;
	}

	protected byte[] doEncoding(byte abyte0[])
		throws EncoderException
	{
		byte abyte1[];
		if (abyte0 == null)
			abyte1 = null;
		else
			abyte1 = Base64.encodeBase64(abyte0);
		return abyte1;
	}

	public Object encode(Object obj)
		throws EncoderException
	{
		String s;
		if (obj == null)
			s = null;
		else
		if (obj instanceof String)
		{
			String s1 = (String)obj;
			s = encode(s1);
		} else
		{
			StringBuffer stringbuffer = (new StringBuffer()).append("Objects of type ");
			String s2 = obj.getClass().getName();
			String s3 = stringbuffer.append(s2).append(" cannot be encoded using BCodec").toString();
			throw new EncoderException(s3);
		}
		return s;
	}

	public String encode(String s)
		throws EncoderException
	{
		String s1;
		if (s == null)
		{
			s1 = null;
		} else
		{
			String s2 = getDefaultCharset();
			s1 = encode(s, s2);
		}
		return s1;
	}

	public String encode(String s, String s1)
		throws EncoderException
	{
		/*if (s != null) goto _L2; else goto _L1
_L1:
		String s2 = null;
_L4:
		return s2;
_L2:
		UnsupportedEncodingException unsupportedencodingexception;
		String s3;
		try
		{
			s3 = encodeText(s, s1);
		}
		// Misplaced declaration of an exception variable
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			String s4 = unsupportedencodingexception.getMessage();
			throw new EncoderException(s4);
		}
		s2 = s3;
		if (true) goto _L4; else goto _L3
_L3:*/
		return "";
	}

	public String getDefaultCharset()
	{
		return charset;
	}

	protected String getEncoding()
	{
		return "B";
	}
}
