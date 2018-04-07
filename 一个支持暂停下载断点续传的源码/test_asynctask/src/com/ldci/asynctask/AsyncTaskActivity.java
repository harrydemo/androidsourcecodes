package com.ldci.asynctask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncTaskActivity extends ListActivity
{
	/** 用来存放每一个正在进行的下载任务 */
	public static List<Map<String, Async>> listTask = new ArrayList<Map<String, Async>>();
	private static List<Button> listStart = new ArrayList<Button>();
	private static List<Button> listStop = new ArrayList<Button>();
	private static List<Button> listPause = new ArrayList<Button>();
	private static List<Button> listContinue = new ArrayList<Button>();
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        ListAdapter adapter = new ListAdapter(this);
        setListAdapter(adapter);
        getListView().setFocusable(false);
    }
	
	//=======================================================
	private class ListAdapter extends BaseAdapter implements OnClickListener
	{
		private Context context = null;
		public ListAdapter(Context context)
		{
			this.context = context;
		}
		@Override
		public int getCount()
		{
			return Utils.title.length;
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int id) 
		{
			return id;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if(convertView==null)
			{
				convertView = View.inflate(this.context, R.layout.list_item, null);
			}
			TextView txt_title = (TextView)convertView.findViewById(R.id.txt);
			ProgressBar pb_progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
			Button btn_start = (Button)convertView.findViewById(R.id.btn_start);
			Button btn_pause = (Button)convertView.findViewById(R.id.btn_pause);
			Button btn_stop = (Button)convertView.findViewById(R.id.btn_stop);
			Button btn_continue = (Button)convertView.findViewById(R.id.btn_continue);
			
 			// 给统一列表项的下载、停止、暂停、继续、进度条控件设置相同的Tag
			btn_start.setTag(String.valueOf(position));
			btn_pause.setTag(String.valueOf(position));
			btn_stop.setTag(String.valueOf(position));
			btn_continue.setTag(String.valueOf(position));
			pb_progressBar.setTag(String.valueOf(position));

			txt_title.setText(Utils.title[position]);
			pb_progressBar.setProgress(Utils.progress[position]);
			// 将个下载项的进度条加到进度条列表
			Async.listPb.add(pb_progressBar);
			listStart.add(btn_start);
			listStop.add(btn_stop);
			listPause.add(btn_pause);
			listContinue.add(btn_continue);
			
			// 设置按钮控件的可见性  0 可见，4 不占位不可见 ，8  占位不可见
			listStart.get(position).setVisibility(0);
			listPause.get(position).setVisibility(8);
			listStop.get(position).setVisibility(8);
			listContinue.get(position).setVisibility(8);
			Async.listPb.get(position).setVisibility(8);
			Async.listPb.get(position).setProgress(0);
			// 设置监听
			btn_start.setOnClickListener(this);
			btn_pause.setOnClickListener(this);
			btn_stop.setOnClickListener(this);
			btn_continue.setOnClickListener(this);
			return convertView;
		}
		Async asyncTask = null;
		Map<String, Async> map = null;
		@Override
		public void onClick(View v) 
		{
			switch (v.getId()) 
			{
			case R.id.btn_start:
				Async startTask = null;
				boolean isHas = false;
				// 判断当前要下载的这个连接是否已经正在进行，如果正在进行就阻止在此启动一个下载任务
				for(int i=0;i<listTask.size();i++)
				{
					startTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(startTask!=null)
					{
						isHas = true;
						break;
					}
				}
				// 如果这个连接的下载任务还没有开始，就创建一个新的下载任务启动下载，并这个下载任务加到下载列表中
				if(!isHas)
				{
					asyncTask = new Async();  // 创建新异步
					map = new Hashtable<String, Async>();
					map.put(String.valueOf(v.getTag()), asyncTask);
					listTask.add(map);
					// 当调用AsyncTask的方法execute时，就回去自动调用doInBackground方法
					asyncTask.execute(String.valueOf(v.getTag()));
					
					// 设置按钮控件的可见性  0 可见，4 不占位不可见 ，8  占位不可见
					listStart.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
					listPause.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
					listStop.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
					listContinue.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
					Async.listPb.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
				}
				Log.d("debug","add listTask.size:"+listTask.size());
				break;
			case R.id.btn_pause:
				Async pauseTask = null;
				// 判断但前被停止的这个任务在任务列表中是否存在，如果存在就暂停
				for(int i=0;i<listTask.size();i++)
				{
					pauseTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(pauseTask!=null)
					{
						asyncTask = new Async();
						//  为什么这里的asyncTask.isPaused()一直是false？？？？？？？？？？
						Log.d("debug","-------asyncTask.paused:"+asyncTask.isPaused());
						pauseTask.pause();
						
						// 设置按钮控件的可见性  0 可见，4 不占位不可见 ，8  占位不可见
						listStart.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						listPause.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						listStop.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						listContinue.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						Async.listPb.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						break;
					}
				}
				Log.d("debug","pause listTask.size:"+listTask.size());
				break;
			case R.id.btn_continue:
				Async continueTask = null;
				// 判断但前被停止的这个任务在任务列表中是否存在，如果存在就继续
				for(int i=0;i<listTask.size();i++)
				{
					continueTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(continueTask!=null)
					{
						asyncTask = new Async();
						//  为什么这里的asyncTask.isPaused()一直是false？？？？？？？？？？
						Log.d("debug","-------asyncTask.paused:"+asyncTask.isPaused());
						continueTask.continued();
						
						// 设置按钮控件的可见性  0 可见，4 占位不可见 ，8  不占位不可见
						listStart.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						listPause.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						listStop.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						listContinue.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						Async.listPb.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						break;
					}
				}
				Log.d("debug","pause listTask.size:"+listTask.size());
				break;				
			case R.id.btn_stop:
				Async cancleTask = null;
				// 判断但前被停止的这个任务在任务列表中是否存在，如果存在就移除
				for(int i=0;i<listTask.size();i++)
				{
					cancleTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(cancleTask!=null)
					{
						// 调用cancle方法就回去调用AsyncTask类的onCancelled方法来停止下载任务
						cancleTask.cancel(false);
						Log.d("debug","remove sucess?:"+listTask.remove(i));
						
						// 设置按钮控件的可见性  0 可见，4 不占位不可见 ，8  占位不可见
						listStart.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(0);
						listPause.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						listStop.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						listContinue.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						Async.listPb.get(Integer.parseInt(String.valueOf(v.getTag()))).setVisibility(8);
						Async.listPb.get(Integer.parseInt(String.valueOf(v.getTag()))).setProgress(0);
						break;
					}
				}
				Log.d("debug","remove listTask.size:"+listTask.size());
				break;
			}
		}
	}
}