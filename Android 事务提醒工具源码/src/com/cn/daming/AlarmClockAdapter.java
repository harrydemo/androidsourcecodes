package com.cn.daming;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ��ҳ��
 * */
public class AlarmClockAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater;  
    private Context context; 
    private List<String> alarm_ids;
    private List<String> alarm_times;
    private List<String> alarm_repeats;
    private List<String> alarm_isopens;
    private List<String> alarm_kinds;
    ZuJian zuJian;
    
    public AlarmClockAdapter(Context context,List<String> ids,List<String> times,List<String> repeats,
    		List<String> isopens,List<String> kinds) {  
        this.context = context;  
        this.alarm_ids = ids;
        this.alarm_times = times;
        this.alarm_repeats = repeats;
        this.alarm_isopens = isopens;
        this.alarm_kinds = kinds;
        this.layoutInflater = LayoutInflater.from(context);  
    }  
    
	public int getCount() {
		return alarm_times.size(); 
	}

	public Object getItem(int position) {
		return alarm_times.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		zuJian =  new ZuJian();
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.alarm_clock, null);
			zuJian.alarmTitle = (TextView) convertView.findViewById(R.id.alarm_title); //�������ѱ���
			zuJian.alarmTimeView = (TextView) convertView.findViewById(R.id.alarm_time); //����ʱ��
			zuJian.repeatAlarmView = (TextView) convertView.findViewById(R.id.alarm_repeat_time); //�ظ�ʱ��
			zuJian.showISOpenView = (ImageView) convertView.findViewById(R.id.show_open_close);//�Ƿ񿪹�
			zuJian.addButton=(Button) convertView.findViewById(R.id.add_textview);
			convertView.setTag(zuJian);
		} else {
			zuJian = (ZuJian) convertView.getTag();
		}
		if (alarm_kinds.get(position) == null || alarm_kinds.get(position) == "") {
			if(position == 0){
				zuJian.alarmTitle.setText("��������һ");
			}
			if(position == 1){
				zuJian.alarmTitle.setText("�������Ѷ�");
			}
		} else {
			zuJian.alarmTitle.setText(alarm_kinds.get(position));
		}

		if (alarm_times.get(position) == null || alarm_times.get(position) == "") {
			zuJian.alarmTimeView.setText("Ŀǰ������");
		} else {
			zuJian.alarmTimeView.setText(alarm_times.get(position));
		}

		if (alarm_repeats.get(position) == null || alarm_repeats.get(position) == "") {
			zuJian.repeatAlarmView.setText("Ŀǰ������");
		} else {
			zuJian.repeatAlarmView.setText(alarm_repeats.get(position));
		}

		if (alarm_isopens.get(position) == null || alarm_isopens.get(position) == "") {
		   ((ImageView) zuJian.showISOpenView).setBackgroundResource(R.drawable.alarm_dialog); //���ӹر�
		} else {
			if(alarm_isopens.get(position).equals("��")){
				((ImageView) zuJian.showISOpenView).setBackgroundResource(R.drawable.clock); //���ӿ���
			}
			if(alarm_isopens.get(position).equals("��")){
				((ImageView) zuJian.showISOpenView).setBackgroundResource(R.drawable.alarm_dialog);
			}
		}
		
		return convertView;
	}

    final class ZuJian {
		public TextView alarmTitle;
		public TextView alarmTimeView;
		public TextView repeatAlarmView;
		public ImageView showISOpenView;
		public Button addButton;
		
	}
}
