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
	/** �������ÿһ�����ڽ��е��������� */
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
			
 			// ��ͳһ�б�������ء�ֹͣ����ͣ���������������ؼ�������ͬ��Tag
			btn_start.setTag(String.valueOf(position));
			btn_pause.setTag(String.valueOf(position));
			btn_stop.setTag(String.valueOf(position));
			btn_continue.setTag(String.valueOf(position));
			pb_progressBar.setTag(String.valueOf(position));

			txt_title.setText(Utils.title[position]);
			pb_progressBar.setProgress(Utils.progress[position]);
			// ����������Ľ������ӵ��������б�
			Async.listPb.add(pb_progressBar);
			listStart.add(btn_start);
			listStop.add(btn_stop);
			listPause.add(btn_pause);
			listContinue.add(btn_continue);
			
			// ���ð�ť�ؼ��Ŀɼ���  0 �ɼ���4 ��ռλ���ɼ� ��8  ռλ���ɼ�
			listStart.get(position).setVisibility(0);
			listPause.get(position).setVisibility(8);
			listStop.get(position).setVisibility(8);
			listContinue.get(position).setVisibility(8);
			Async.listPb.get(position).setVisibility(8);
			Async.listPb.get(position).setProgress(0);
			// ���ü���
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
				// �жϵ�ǰҪ���ص���������Ƿ��Ѿ����ڽ��У�������ڽ��о���ֹ�ڴ�����һ����������
				for(int i=0;i<listTask.size();i++)
				{
					startTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(startTask!=null)
					{
						isHas = true;
						break;
					}
				}
				// ���������ӵ���������û�п�ʼ���ʹ���һ���µ����������������أ��������������ӵ������б���
				if(!isHas)
				{
					asyncTask = new Async();  // �������첽
					map = new Hashtable<String, Async>();
					map.put(String.valueOf(v.getTag()), asyncTask);
					listTask.add(map);
					// ������AsyncTask�ķ���executeʱ���ͻ�ȥ�Զ�����doInBackground����
					asyncTask.execute(String.valueOf(v.getTag()));
					
					// ���ð�ť�ؼ��Ŀɼ���  0 �ɼ���4 ��ռλ���ɼ� ��8  ռλ���ɼ�
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
				// �жϵ�ǰ��ֹͣ����������������б����Ƿ���ڣ�������ھ���ͣ
				for(int i=0;i<listTask.size();i++)
				{
					pauseTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(pauseTask!=null)
					{
						asyncTask = new Async();
						//  Ϊʲô�����asyncTask.isPaused()һֱ��false��������������������
						Log.d("debug","-------asyncTask.paused:"+asyncTask.isPaused());
						pauseTask.pause();
						
						// ���ð�ť�ؼ��Ŀɼ���  0 �ɼ���4 ��ռλ���ɼ� ��8  ռλ���ɼ�
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
				// �жϵ�ǰ��ֹͣ����������������б����Ƿ���ڣ�������ھͼ���
				for(int i=0;i<listTask.size();i++)
				{
					continueTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(continueTask!=null)
					{
						asyncTask = new Async();
						//  Ϊʲô�����asyncTask.isPaused()һֱ��false��������������������
						Log.d("debug","-------asyncTask.paused:"+asyncTask.isPaused());
						continueTask.continued();
						
						// ���ð�ť�ؼ��Ŀɼ���  0 �ɼ���4 ռλ���ɼ� ��8  ��ռλ���ɼ�
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
				// �жϵ�ǰ��ֹͣ����������������б����Ƿ���ڣ�������ھ��Ƴ�
				for(int i=0;i<listTask.size();i++)
				{
					cancleTask = listTask.get(i).get(String.valueOf(v.getTag()));
					if(cancleTask!=null)
					{
						// ����cancle�����ͻ�ȥ����AsyncTask���onCancelled������ֹͣ��������
						cancleTask.cancel(false);
						Log.d("debug","remove sucess?:"+listTask.remove(i));
						
						// ���ð�ť�ؼ��Ŀɼ���  0 �ɼ���4 ��ռλ���ɼ� ��8  ռλ���ɼ�
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