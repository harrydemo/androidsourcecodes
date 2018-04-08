// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   URLCodec.java

package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.*;

public class URLCodec
	implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{

	protected static byte ESCAPE_CHAR = 37;
	protected static final BitSet WWW_FORM_URL;
	protected String charset;

	public URLCodec()
	{
		charset = "UTF-8";
	}

	public URLCodec(String s)
	{
		charset = "UTF-8";
		charset = s;
	}

	public static final byte[] decodeUrl(byte abyte0[])
		throws DecoderException
	{
		/*if (abyte0 != null) goto _L2; else goto _L1
_L1:
		Object obj = null;
_L7:
		return ((byte []) (obj));
_L2:
		ByteArrayOutputStream bytearrayoutputstream;
		int i;
		bytearrayoutputstream = new ByteArrayOutputStream();
		i = 0;
_L4:
		byte byte0;
		int j = abyte0.length;
		if (i >= j)
			break MISSING_BLOCK_LABEL_152;
		byte0 = abyte0[i];
		if (byte0 != 43)
			break;  Loop/switch isn't completed 
		bytearrayoutputstream.write(32);
_L5:
		i++;
		if (true) goto _L4; else goto _L3
_L3:
		if (byte0 != 37)
			break MISSING_BLOCK_LABEL_143;
		i++;
		int k;
		int l;
		try
		{
			k = Character.digit((char)abyte0[i], 16);
			i++;
			l = Character.digit((char)abyte0[i], 16);
			if (k == -1 || l == -1)
				throw new DecoderException("Invalid URL encoding");
		}
		catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
		{
			throw new DecoderException("Invalid URL encoding");
		}
		obj = (char)((k << 4) + l);
		bytearrayoutputstream.write(((int) (obj)));
		  goto _L5
		bytearrayoutputstream.write(byte0);
		  goto _L5
		obj = bytearrayoutputstream.toByteArray();
		if (true) goto _L7; else goto _L6
_L6:*/
		return null;
	}

	public static final byte[] encodeUrl(BitSet bitset, byte abyte0[])
	{
		byte abyte1[];
		if (abyte0 == null)
		{
			abyte1 = null;
		} else
		{
			if (bitset == null)
				bitset = WWW_FORM_URL;
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			int i = 0;
			do
			{
				int j = abyte0.length;
				if (i >= j)
					break;
				int k = abyte0[i];
				if (k < 0)
					k += 256;
				if (bitset.get(k))
				{
					if (k == 32)
						k = 43;
					bytearrayoutputstream.write(k);
				} else
				{
					bytearrayoutputstream.write(37);
					char c = Character.toUpperCase(Character.forDigit(k >> 4 & 0xf, 16));
					char c1 = Character.toUpperCase(Character.forDigit(k & 0xf, 16));
					bytearrayoutputstream.write(c);
					bytearrayoutputstream.write(c1);
				}
				i++;
			} while (true);
			abyte1 = bytearrayoutputstream.toByteArray();
		}
		return abyte1;
	}

	public Object decode(Object obj)
		throws DecoderException
	{
		Object obj1;
		if (obj == null)
			obj1 = null;
		else
		if (obj instanceof byte[])
		{
			byte abyte0[] = (byte[])obj;
			obj1 = decode(abyte0);
		} else
		if (obj instanceof String)
		{
			String s = (String)obj;
			obj1 = decode(s);
		} else
		{
			StringBuffer stringbuffer = (new StringBuffer()).append("Objects of type ");
			String s1 = obj.getClass().getName();
			String s2 = stringbuffer.append(s1).append(" cannot be URL decoded").toString();
			throw new DecoderException(s2);
		}
		return obj1;
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
		String s3;
		try
		{
			String s2 = getDefaultCharset();
			s3 = decode(s, s2);
		}
		// Misplaced declaration of an exception variable
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			String s4 = unsupportedencodingexception.getMessage();
			throw new DecoderException(s4);
		}
		s1 = s3;
		if (true) goto _L4; else goto _L3
_L3:*/
		return "";
	}

	public String decode(String s, String s1)
		throws DecoderException, UnsupportedEncodingException
	{
		String s2;
		if (s == null)
		{
			s2 = null;
		} else
		{
			byte abyte0[] = s.getBytes("US-ASCII");
			byte abyte1[] = decode(abyte0);
			s2 = new String(abyte1, s1);
		}
		return s2;
	}

	public byte[] decode(byte abyte0[])
		throws DecoderException
	{
		return decodeUrl(abyte0);
	}

	public Object encode(Object obj)
		throws EncoderException
	{
		Object obj1;
		if (obj == null)
			obj1 = null;
		else
		if (obj instanceof byte[])
		{
			byte abyte0[] = (byte[])obj;
			obj1 = encode(abyte0);
		} else
		if (obj instanceof String)
		{
			String s = (String)obj;
			obj1 = encode(s);
		} else
		{
			StringBuffer stringbuffer = (new StringBuffer()).append("Objects of type ");
			String s1 = obj.getClass().getName();
			String s2 = stringbuffer.append(s1).append(" cannot be URL encoded").toString();
			throw new EncoderException(s2);
		}
		return obj1;
	}

	public String encode(String s)
		throws EncoderException
	{
		/*if (s != null) goto _L2; else goto _L1
_L1:
		String s1 = null;
_L4:
		return s1;
_L2:
		UnsupportedEncodingException unsupportedencodingexception;
		String s3;
		try
		{
			String s2 = getDefaultCharset();
			s3 = encode(s, s2);
		}
		// Misplaced declaration of an exception variable
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			String s4 = unsupportedencodingexception.getMessage();
			throw new EncoderException(s4);
		}
		s1 = s3;
		if (true) goto _L4; else goto _L3
_L3:*/
		return "";
	}

	public String encode(String s, String s1)
		throws UnsupportedEncodingException
	{
		String s2;
		if (s == null)
		{
			s2 = null;
		} else
		{
			byte abyte0[] = s.getBytes(s1);
			byte abyte1[] = encode(abyte0);
			s2 = new String(abyte1, "US-ASCII");
		}
		return s2;
	}

	public byte[] encode(byte abyte0[])
	{
		return encodeUrl(WWW_FORM_URL, abyte0);
	}

	public String getDefaultCharset()
	{
		return charset;
	}

	public String getEncoding()
	{
		return charset;
	}

	static 
	{
		WWW_FORM_URL = new BitSet(256);
		for (int i = 97; i <= 122; i++)
			WWW_FORM_URL.set(i);

		for (int j = 65; j <= 90; j++)
			WWW_FORM_URL.set(j);

		for (int k = 48; k <= 57; k++)
			WWW_FORM_URL.set(k);

		WWW_FORM_URL.set(45);
		WWW_FORM_URL.set(95);
		WWW_FORM_URL.set(46);
		WWW_FORM_URL.set(42);
		WWW_FORM_URL.set(32);
	}
}
