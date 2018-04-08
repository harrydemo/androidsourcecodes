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
	private String tagType = "δ֪";
	private String trackName;
	private String artistName;
	private String albumName;
	private String year;
	private Bitmap albumImage;
	private RandomAccessFile randomAccessFile;
	
	private static final String Error = "�޷���ȡ";
	
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
					//��ǩ�������ݳ��ȿ�ʼ�ڵ�5���ֽ�,��4���ֽڳ�
					randomAccessFile.seek(current + 4);
					datas = new byte[4];
					//��ȡ��ǩ���ݳ��ȵ��ֽ�����
					randomAccessFile.read(datas);
					//�����ǩ���ݳ���
					contentSize =datas[0]*0x10000*0x10000 + datas[1]*0x10000 + datas[2]*0x100 + datas[3];
					
					//��ǩ���ı�־��ʼ�ڵ�һ���ֽڣ���4���ֽڳ�,�ɱ�־�����洢������
					randomAccessFile.seek(current);
					datas = new byte[4];
					randomAccessFile.read(datas);
					tag = new String(datas);
					//System.out.println("tag   ------>   " + tag);
					
					//��ǩ���ĳ��ȹ�10���ֽڣ���������ű�ǩ����Ӧ�ı�ǩ����
					randomAccessFile.seek(current + 10);
					datas = new byte[contentSize];
					//��ȡ��ǩ���ݵ��ֽ�����
					randomAccessFile.read(datas);
					
					//���ݱ�ǩ���ı�־��ͬ���Ѷ�ParMp3Tag����Ĳ�ͬʵ����ֵ
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
						//ͼƬ���ݵ�ͷ14���ֽڻ���23���ֽ���û�õģ�ȥ����ſ��Զ�ȡ��ͼƬ
						randomAccessFile.seek(current + 10 + 14);
						datas = new byte[contentSize - 14];
						randomAccessFile.read(datas);
						//����ȡͼƬ���ɹ��ǽ��Ϊnull,��ʱ�ٳ��Դ�23���Խں��ȡͼƬ
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
