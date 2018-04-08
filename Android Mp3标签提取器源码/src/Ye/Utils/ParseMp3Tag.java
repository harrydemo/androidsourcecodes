package Ye.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Size;

public class ParseMp3Tag 
{	
	public ParseMp3Tag(String path)
	{
		File f = new File(path);
		try 
		{
			randomAccessFile = new RandomAccessFile(f, "r");
			byte[] datas = new byte[3];
			randomAccessFile.read(datas);
			tag = new String(datas).trim();
			if(tag.equals("ID3"))
			{
				byte[] size = new byte[4];
				randomAccessFile.seek(6);
				randomAccessFile.read(size);
				tagSize = (size[0]&0x7f)*0x200000 + (size[1]&0x7f)*0x400 + (size[2]&0x7f)*0x80 + (size[3]&0x7f);
				tagType = "ID3v2";
			}
			else
			{
				datas = new byte[128];
				randomAccessFile.seek(f.length() - datas.length);
				randomAccessFile.read(datas);
				tagType = "ID3v1";
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public boolean isValid() 
	{
		return isValid;
	}
	
	public String getTagType()
	{
		return tagType;
	}

	public String getTrackName() 
	{
		if(isValid)
		{
			return trackName;
		}
		else 
			return Error;
	}

	public String getArtistName() 
	{
		if(isValid)
			return artistName;		
		else
			return Error;
	}

	public String getAlbumName() 
	{
		if(isValid)
			return albumName;
		else
			return Error;
	}

	public String getYear() 
	{
		if(isValid)
			return year;
		else
			return Error;
	}

	public Bitmap getAlbumImage() 
	{
		if(isValid)
			return albumImage;
		else 
			return null;
	}

	private byte[] datas;
	private boolean isValid = false;
	private String tag;
	private int tagSize;
	private String tagType = "未知";
	private String trackName;
	private String artistName;
	private String albumName;
	private String year;
	private Bitmap albumImage;
	private RandomAccessFile randomAccessFile;
	
	private static final String Error = "无法读取";
	
	public void parse(String charsetName)
	{
		if(tagType.equals("ID3v1"))
		{
			tag = new String(datas, 0, 3);
			if(tag.equals("TAG"))
			{
				isValid = true;
				try 
				{
					trackName = new String(datas, 3, 30, charsetName).trim();
					artistName = new String(datas, 33, 30, charsetName).trim();
					albumName = new String(datas, 63, 33, charsetName).trim();
					year = new String(datas, 93, 4, charsetName).trim();
				} 
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if(tagType.equals("ID3v2"))
		{
			isValid = true;
			int contentSize = 0;
			int current = 10;
			int emptyTag = 0;
			while(current < tagSize)
			{		
				try 
				{
					//标签祯的内容长度开始于第5个字节,共4个字节长
					randomAccessFile.seek(current + 4);
					datas = new byte[4];
					//获取标签内容长度的字节数组
					randomAccessFile.read(datas);
					//计算标签内容长度
					contentSize =datas[0]*0x10000*0x10000 + datas[1]*0x10000 + datas[2]*0x100 + datas[3];
					
					//标签祯的标志开始于第一个字节，共4个字节长,由标志决定存储的内容
					randomAccessFile.seek(current);
					datas = new byte[4];
					randomAccessFile.read(datas);
					tag = new String(datas);
					//System.out.println("tag   ------>   " + tag);
					
					//标签祯的长度共10个字节，后面跟随着标签祯对应的标签内容
					randomAccessFile.seek(current + 10);
					datas = new byte[contentSize];
					//获取标签内容的字节数组
					randomAccessFile.read(datas);
					
					//根据标签祯的标志不同，把对ParMp3Tag对象的不同实例赋值
					if(tag.equals("TIT2"))
					{
						trackName = new String(datas, 1, datas.length-1, charsetName);
					}
					else if(tag.equals("TALB"))
					{
						albumName = new String(datas, 1, datas.length-1, charsetName);
					}
					else if(tag.equals("TYER"))
					{
						year = new String(datas, 1, datas.length-1, charsetName);
					}
					else if(tag.equals("TPE1"))
					{
						artistName = new String(datas, 1, datas.length-1, charsetName);
					}
					else if(tag.equals("APIC"))
					{
						//System.out.println("has a photo.....");
						//图片内容的头14个字节或者23个字节是没用的，去掉后才可以读取到图片
						randomAccessFile.seek(current + 10 + 14);
						datas = new byte[contentSize - 14];
						randomAccessFile.read(datas);
						//当读取图片不成功是结果为null,此时再尝试从23个自节后读取图片
						albumImage = BitmapFactory.decodeByteArray(datas, 0, datas.length);
						if(albumImage == null)
						{
							randomAccessFile.seek(current + 10 + 23);
							datas = new byte[contentSize - 23];
							randomAccessFile.read(datas);
							albumImage = BitmapFactory.decodeByteArray(datas, 0, datas.length);
						}
					}
					else
					{
						if(emptyTag >= 5)
							break;
						System.out.println("tag   ------>   " + tag);
						emptyTag++;
					}
					
					current += 10 + contentSize;
					//System.out.println("current   ------>   " + current);
					//System.out.println("contentSize   ------>   " + contentSize);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
