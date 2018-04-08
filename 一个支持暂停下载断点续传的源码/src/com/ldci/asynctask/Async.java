package com.ldci.asynctask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

// AsyncTask<Params, Progress, Result> 
public class Async extends AsyncTask<String, Integer, String> 
{
	private boolean finished = true;
	private boolean paused = false;
	public static List<ProgressBar> listPb = new ArrayList<ProgressBar>();

	public boolean isPaused() 
	{
		return paused;
	}
	@Override
	protected String doInBackground(String... Params)
	{
		int position = Integer.parseInt(Params[0]);
		Log.d("debug","position:"+position);
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		RandomAccessFile outputStream = null;
		String path = Environment.getExternalStorageDirectory().getPath();
		int length = 0;
		try 
		{
			url = new URL(Utils.url[position]);
			httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setAllowUserInteraction(true);
			 //���õ�ǰ�߳����ص���㣬�յ�
			length = httpURLConnection.getContentLength();
			int startPosition = 0;
			
			// ���öϵ������Ŀ�ʼλ��
//			httpURLConnection.setRequestProperty("Range", "bytes=" + (length-startPosition));
			// ????????????????????????Cannot set method after connection is made
			Log.d("debug","getContentLength:"+length);
			System.out.println("getContentLength:"+length);
			inputStream = httpURLConnection.getInputStream();
			
//			//����User-Agent 
//			httpURLConnection.setRequestProperty("User-Agent","NetFox"); 
//			//���öϵ������Ŀ�ʼλ�� 
//			httpURLConnection.setRequestProperty("RANGE","bytes=4096"); 
			
			
			File outFile = new File(path+"/"+"qinghua"+position+".wma");
			Log.d("debug","outFile:"+path+"/"+"qinghua"+position+".wma");
			 //ʹ��java�е�RandomAccessFile ���ļ����������д����
			outputStream = new RandomAccessFile(outFile,"rw");
			//���ÿ�ʼд�ļ���λ��
			outputStream.seek(startPosition);
			
			byte[] buf = new byte[1024*10];
			int read = 0;
			int curSize = startPosition;
			Log.d("debug","buf��"+buf.length);
			while(finished)
			{
				while(paused)
				{
					Thread.sleep(500);
				}
				read = inputStream.read(buf);
				if(read==-1)
				{
					break;
				}
				outputStream.write(buf,0,read);
				curSize = curSize+read;
				// ���������������ʱ����Զ�ȥ����onProgressUpdate�������������ؽ���
				publishProgress((int)(curSize*100.0f/length),position);
				if(curSize == length)
				{
					break;
				}
				Thread.sleep(10);
			}
			inputStream.close();
			outputStream.close();
			httpURLConnection.disconnect();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			finished = false;
			if(inputStream!=null)
			{
				try 
				{
					inputStream.close();
					if(outputStream!=null)
					{
						outputStream.close();
					}
					if(httpURLConnection!=null)
					{
						httpURLConnection.disconnect();
					}
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		// ����ķ���ֵ���ᱻ��ΪonPostExecute�����Ĵ������
		return String.valueOf(position);
	}
	/**
	 * ��ͣ����
	 */
	public void pause()
	{
		paused = true;
		Log.d("debug","paused------------"+paused);
	}
	/**
	 * ��������
	 */
	public void continued()
	{
		paused = false;
		Log.d("debug","paused------------"+paused);
	}
	/**
	 * ֹͣ����
	 */
	@Override
	protected void onCancelled()
	{
		Log.d("debug","onCancelled");
		finished = false;
		super.onCancelled();
	}
	/**
	 * ��һ����������ɹ�������ɵ�ʱ�����������������������result��������doInBackground�����ķ���ֵ
	 */
	@Override
	protected void onPostExecute(String result) 
	{
		int pos = -1;
		try 
		{
			pos = Integer.parseInt(result);
			// �жϵ�ǰ��������������������б����Ƿ񻹴��ڣ�������ھ��Ƴ�
			for(int i=0;i<AsyncTaskActivity.listTask.size();i++)
			{
				if(AsyncTaskActivity.listTask.get(i).get(String.valueOf(pos))!=null)
				{
					finished = false;
					Log.d("debug","remove sucess?:"+AsyncTaskActivity.listTask.remove(i));
					break;
				}
			}
			Log.d("debug","onPostExecute:"+AsyncTaskActivity.listTask.size());
		} 
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
	}
	/**
	 * �������ؽ��ȣ���publishProgress���������õ�ʱ��ͻ��Զ��������������
	 */
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		listPb.get(values[1]).setProgress(values[0]);
		super.onProgressUpdate(values);
	}
}
