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
			 //设置当前线程下载的起点，终点
			length = httpURLConnection.getContentLength();
			int startPosition = 0;
			
			// 设置断点续传的开始位置
//			httpURLConnection.setRequestProperty("Range", "bytes=" + (length-startPosition));
			// ????????????????????????Cannot set method after connection is made
			Log.d("debug","getContentLength:"+length);
			System.out.println("getContentLength:"+length);
			inputStream = httpURLConnection.getInputStream();
			
//			//设置User-Agent 
//			httpURLConnection.setRequestProperty("User-Agent","NetFox"); 
//			//设置断点续传的开始位置 
//			httpURLConnection.setRequestProperty("RANGE","bytes=4096"); 
			
			
			File outFile = new File(path+"/"+"qinghua"+position+".wma");
			Log.d("debug","outFile:"+path+"/"+"qinghua"+position+".wma");
			 //使用java中的RandomAccessFile 对文件进行随机读写操作
			outputStream = new RandomAccessFile(outFile,"rw");
			//设置开始写文件的位置
			outputStream.seek(startPosition);
			
			byte[] buf = new byte[1024*10];
			int read = 0;
			int curSize = startPosition;
			Log.d("debug","buf："+buf.length);
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
				// 当调用这个方法的时候会自动去调用onProgressUpdate方法，传递下载进度
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
		// 这里的返回值将会被作为onPostExecute方法的传入参数
		return String.valueOf(position);
	}
	/**
	 * 暂停下载
	 */
	public void pause()
	{
		paused = true;
		Log.d("debug","paused------------"+paused);
	}
	/**
	 * 继续下载
	 */
	public void continued()
	{
		paused = false;
		Log.d("debug","paused------------"+paused);
	}
	/**
	 * 停止下载
	 */
	@Override
	protected void onCancelled()
	{
		Log.d("debug","onCancelled");
		finished = false;
		super.onCancelled();
	}
	/**
	 * 当一个下载任务成功下载完成的时候回来调用这个方法，这里的result参数就是doInBackground方法的返回值
	 */
	@Override
	protected void onPostExecute(String result) 
	{
		int pos = -1;
		try 
		{
			pos = Integer.parseInt(result);
			// 判断但前结束的这个任务在任务列表中是否还存在，如果存在就移除
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
	 * 更新下载进度，当publishProgress方法被调用的时候就会自动来调用这个方法
	 */
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		listPb.get(values[1]).setProgress(values[0]);
		super.onProgressUpdate(values);
	}
}
