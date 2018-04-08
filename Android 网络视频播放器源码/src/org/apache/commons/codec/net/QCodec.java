// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   QCodec.java

package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.*;

// Referenced classes of package org.apache.commons.codec.net:
//			RFC1522Codec, QuotedPrintableCodec

public class QCodec extends RFC1522Codec
	implements StringEncoder, StringDecoder
{

	private static byte BLANK = 32;
	private static final BitSet PRINTABLE_CHARS;
	private static byte UNDERSCORE = 95;
	private String charset;
	private boolean encodeBlanks;

	public QCodec()
	{
		charset = "UTF-8";
		encodeBlanks = false;
	}

	public QCodec(String s)
	{
		charset = "UTF-8";
		encodeBlanks = false;
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
			String s3 = stringbuffer.append(s2).append(" cannot be decoded using Q codec").toString();
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
		/*if (abyte0 != null) goto _L2; else goto _L1
_L1:
		byte abyte1[] = null;
_L4:
		return abyte1;
_L2:
label0:
		{
			boolean flag = false;
			int i = 0;
			byte abyte2[];
label1:
			do
			{
label2:
				{
					int j = abyte0.length;
					if (i < j)
					{
						byte byte0 = abyte0[i];
						byte byte1 = UNDERSCORE;
						if (byte0 != byte1)
							break label2;
						flag = true;
					}
					if (!flag)
						break label0;
					abyte2 = new byte[abyte0.length];
					i = 0;
					do
					{
						int k = abyte0.length;
						if (i >= k)
							break;
						byte byte2 = abyte0[i];
						byte byte3 = UNDERSCORE;
						if (byte2 != byte3)
						{
							abyte2[i] = byte2;
						} else
						{
							byte byte4 = BLANK;
							abyte2[i] = byte4;
						}
						i++;
					} while (true);
					break label1;
				}
				i++;
			} while (true);
			abyte1 = QuotedPrintableCodec.decodeQuotedPrintable(abyte2);
			continue;  Loop/switch isn't completed 
		}
		abyte1 = QuotedPrintableCodec.decodeQuotedPrintable(abyte0);
		if (true) goto _L4; else goto _L3
_L3:*/
		return null;
	}

	protected byte[] doEncoding(byte abyte0[])
		throws EncoderException
	{
		byte abyte1[];
		if (abyte0 == null)
		{
			abyte1 = null;
		} else
		{
			byte abyte2[] = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, abyte0);
			if (encodeBlanks)
			{
				int i = 0;
				do
				{
					int j = abyte2.length;
					if (i >= j)
						break;
					byte byte0 = abyte2[i];
					byte byte1 = BLANK;
					if (byte0 == byte1)
					{
						byte byte2 = UNDERSCORE;
						abyte2[i] = byte2;
					}
					i++;
				} while (true);
			}
			abyte1 = abyte2;
		}
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
			String s3 = stringbuffer.append(s2).append(" cannot be encoded using Q codec").toString();
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
	/*	if (s != null) goto _L2; else goto _L1
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
		return "Q";
	}

	public boolean isEncodeBlanks()
	{
		return encodeBlanks;
	}

	public void setEncodeBlanks(boolean flag)
	{
		encodeBlanks = flag;
	}

	static 
	{
		PRINTABLE_CHARS = new BitSet(256);
		PRINTABLE_CHARS.set(32);
		PRINTABLE_CHARS.set(33);
		PRINTABLE_CHARS.set(34);
		PRINTABLE_CHARS.set(35);
		PRINTABLE_CHARS.set(36);
		PRINTABLE_CHARS.set(37);
		PRINTABLE_CHARS.set(38);
		PRINTABLE_CHARS.set(39);
		PRINTABLE_CHARS.set(40);
		PRINTABLE_CHARS.set(41);
		PRINTABLE_CHARS.set(42);
		PRINTABLE_CHARS.set(43);
		PRINTABLE_CHARS.set(44);
		PRINTABLE_CHARS.set(45);
		PRINTABLE_CHARS.set(46);
		PRINTABLE_CHARS.set(47);
		for (int i = 48; i <= 57; i++)
			PRINTABLE_CHARS.set(i);

		PRINTABLE_CHARS.set(58);
		PRINTABLE_CHARS.set(59);
		PRINTABLE_CHARS.set(60);
		PRINTABLE_CHARS.set(62);
		PRINTABLE_CHARS.set(64);
		for (int j = 65; j <= 90; j++)
			PRINTABLE_CHARS.set(j);

		PRINTABLE_CHARS.set(91);
		PRINTABLE_CHARS.set(92);
		PRINTABLE_CHARS.set(93);
		PRINTABLE_CHARS.set(94);
		PRINTABLE_CHARS.set(96);
		for (int k = 97; k <= 122; k++)
			PRINTABLE_CHARS.set(k);

		PRINTABLE_CHARS.set(123);
		PRINTABLE_CHARS.set(124);
		PRINTABLE_CHARS.set(125);
		PRINTABLE_CHARS.set(126);
	}
}
