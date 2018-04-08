// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   QuotedPrintableCodec.java

package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.*;

public class QuotedPrintableCodec
	implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{

	private static byte ESCAPE_CHAR = 61;
	private static final BitSet PRINTABLE_CHARS;
	private static byte SPACE;
	private static byte TAB;
	private String charset;

	public QuotedPrintableCodec()
	{
		charset = "UTF-8";
	}

	public QuotedPrintableCodec(String s)
	{
		charset = "UTF-8";
		charset = s;
	}

	public static final byte[] decodeQuotedPrintable(byte abyte0[])
		throws DecoderException
	{
	/*	if (abyte0 != null) goto _L2; else goto _L1
_L1:
		Object obj = null;
_L8:
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
			break MISSING_BLOCK_LABEL_141;
		byte0 = abyte0[i];
		byte byte1 = ESCAPE_CHAR;
		if (byte0 != byte1)
			break;  Loop/switch isn't completed 
		i++;
		int k;
		int l;
		try
		{
			k = Character.digit((char)abyte0[i], 16);
			i++;
			l = Character.digit((char)abyte0[i], 16);
			if (k == -1 || l == -1)
				throw new DecoderException("Invalid quoted-printable encoding");
		}
		catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
		{
			throw new DecoderException("Invalid quoted-printable encoding");
		}
		obj = (char)((k << 4) + l);
		bytearrayoutputstream.write(((int) (obj)));
_L5:
		i++;
		if (true) goto _L4; else goto _L3
_L3:
		bytearrayoutputstream.write(byte0);
		  goto _L5
		if (true) goto _L4; else goto _L6
_L6:
		obj = bytearrayoutputstream.toByteArray();
		if (true) goto _L8; else goto _L7
_L7:*/
		return null;
	}

	private static final void encodeQuotedPrintable(int i, ByteArrayOutputStream bytearrayoutputstream)
	{
		byte byte0 = ESCAPE_CHAR;
		bytearrayoutputstream.write(byte0);
		char c = Character.toUpperCase(Character.forDigit(i >> 4 & 0xf, 16));
		char c1 = Character.toUpperCase(Character.forDigit(i & 0xf, 16));
		bytearrayoutputstream.write(c);
		bytearrayoutputstream.write(c1);
	}

	public static final byte[] encodeQuotedPrintable(BitSet bitset, byte abyte0[])
	{
		byte abyte1[];
		if (abyte0 == null)
		{
			abyte1 = null;
		} else
		{
			if (bitset == null)
				bitset = PRINTABLE_CHARS;
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
					bytearrayoutputstream.write(k);
				else
					encodeQuotedPrintable(k, bytearrayoutputstream);
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
			String s2 = stringbuffer.append(s1).append(" cannot be quoted-printable decoded").toString();
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
		return decodeQuotedPrintable(abyte0);
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
			String s2 = stringbuffer.append(s1).append(" cannot be quoted-printable encoded").toString();
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
		return encodeQuotedPrintable(PRINTABLE_CHARS, abyte0);
	}

	public String getDefaultCharset()
	{
		return charset;
	}

	static 
	{
		PRINTABLE_CHARS = new BitSet(256);
		TAB = 9;
		SPACE = 32;
		for (int i = 33; i <= 60; i++)
			PRINTABLE_CHARS.set(i);

		for (int j = 62; j <= 126; j++)
			PRINTABLE_CHARS.set(j);

		BitSet bitset = PRINTABLE_CHARS;
		byte byte0 = TAB;
		bitset.set(byte0);
		BitSet bitset1 = PRINTABLE_CHARS;
		byte byte1 = SPACE;
		bitset1.set(byte1);
	}
}
