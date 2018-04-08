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
 * 主页面
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
			zuJian.alarmTitle = (TextView) convertView.findViewById(R.id.alarm_title); //日期提醒标题
			zuJian.alarmTimeView = (TextView) convertView.findViewById(R.id.alarm_time); //闹钟时间
			zuJian.repeatAlarmView = (TextView) convertView.findViewById(R.id.alarm_repeat_time); //重复时间
			zuJian.showISOpenView = (ImageView) convertView.findViewById(R.id.show_open_close);//是否开关
			zuJian.addButton=(Button) convertView.findViewById(R.id.add_textview);
			convertView.setTag(zuJian);
		} else {
			zuJian = (ZuJian) convertView.getTag();
		}
		if (alarm_kinds.get(position) == null || alarm_kinds.get(position) == "") {
			if(position == 0){
				zuJian.alarmTitle.setText("日期提醒一");
			}
			if(position == 1){
				zuJian.alarmTitle.setText("日期提醒二");
			}
		} else {
			zuJian.alarmTitle.setText(alarm_kinds.get(position));
		}

		if (alarm_times.get(position) == null || alarm_times.get(position) == "") {
			zuJian.alarmTimeView.setText("目前无设置");
		} else {
			zuJian.alarmTimeView.setText(alarm_times.get(position));
		}

		if (alarm_repeats.get(position) == null || alarm_repeats.get(position) == "") {
			zuJian.repeatAlarmView.setText("目前无设置");
		} else {
			zuJian.repeatAlarmView.setText(alarm_repeats.get(position));
		}

		if (alarm_isopens.get(position) == null || alarm_isopens.get(position) == "") {
		   ((ImageView) zuJian.showISOpenView).setBackgroundResource(R.drawable.alarm_dialog); //闹钟关闭
		} else {
			if(alarm_isopens.get(position).equals("开")){
				((ImageView) zuJian.showISOpenView).setBackgroundResource(R.drawable.clock); //闹钟开启
			}
			if(alarm_isopens.get(position).equals("关")){
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
