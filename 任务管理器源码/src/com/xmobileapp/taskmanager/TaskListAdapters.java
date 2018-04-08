/*
 * [程序名称] Android 任务管理器
 * [作者] xmobileapp团队
 * [参考资料] http://code.google.com/p/freetaskmanager/ 
 * [开源协议] GNU General Public License v2 (http://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 */

package com.xmobileapp.taskmanager;

import java.util.ArrayList;
import java.util.List;

import com.xmobileapp.taskmanager.R;

import com.xmobileapp.taskmanager.ProcessInfo.PsRow;

import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapters {

    public final static class ProcessListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<DetailProcess> list;
        private TaskManager ctx;
        private PackageManager pm;

        public ProcessListAdapter(TaskManager context, ArrayList<DetailProcess> list) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            this.list = list;
            this.ctx = context;
            this.pm = ctx.getPackageManager();
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
            	//通过LayoutInflater对象整合ListView与新的在list_main.xml中定义的布局
                convertView = mInflater.inflate(R.layout.list_main, null);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.list_icon);
                holder.text_name = (TextView) convertView.findViewById(R.id.list_name);
                holder.text_size = (TextView) convertView.findViewById(R.id.list_size);

                // 设置TAG，以便读写其中各个控件的内容
                convertView.setTag(holder);
            } else {
            	// 获取TAG
                holder = (ViewHolder) convertView.getTag();
            }

            // 获取进程列表
            final DetailProcess dp = list.get(position);
            convertView.setVisibility(View.VISIBLE);
            String cmd = dp.getRuninfo().processName;
            
            // 通过TAG赋值
            holder.icon.setImageDrawable(dp.getAppinfo().loadIcon(pm));
            holder.text_name.setText(dp.getTitle());
            PsRow row = dp.getPsrow();
            if (row == null) {
                holder.text_size.setText(R.string.memory_unknown);
            } else {
                holder.text_size.setText((int) Math.ceil(row.mem / 1024) + "K");
            }
            
            // 添加点击事件处理机制，以支持弹出菜单
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    MiscUtil.getTaskMenuDialog(ctx, dp).show();
                }
                
            });
            
            return convertView;
        }

    }

    private static class ViewHolder {
        ImageView icon;
        TextView text_name;
        TextView text_size;
    }

}
