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
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.xmobileapp.taskmanager.R;

import com.xmobileapp.taskmanager.TaskListAdapters.ProcessListAdapter;
//import com.xmobileapp.taskmanager.TaskListAdapters.TasksListAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class TaskManager extends Activity {
    public static final boolean DEBUG = true;
    public static final String TAG = "TaskManager";
    private ProcessInfo pinfo = null;
    ActivityManager am = null;
    private PackagesInfo packageinfo = null;
    //private PackageManager pm;
    protected static final String ACTION_LOADFINISH = "com.xmobileapp.taskmanager.ACTION_LOADFINISH";
    private ProcessListAdapter adapter;
    private BroadcastReceiver loadFinish = new LoadFinishReceiver();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.main);
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //pm = this.getApplicationContext().getPackageManager();
        packageinfo = new PackagesInfo(this);
        
        IntentFilter filter = new IntentFilter(ACTION_LOADFINISH);
        this.registerReceiver(loadFinish, filter);
        
    }

    private ListView getListView() {
        return (ListView) this.findViewById(R.id.listbody);
    }
    
    public void refresh() {
        setProgressBarIndeterminateVisibility(true);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pinfo = new ProcessInfo();
				getRunningProcess();

				Intent in = new Intent(ACTION_LOADFINISH);
				TaskManager.this.sendBroadcast(in);
			}

		});
		t.start();
    }

    public ProcessInfo getProcessInfo() {
        return pinfo;
    }

    public PackagesInfo getPackageInfo() {
        return packageinfo;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //从其他Activity切回当前Activity时，进程列表要刷新
    @Override
    protected void onResume() {
        super.onResume();
        packageinfo = new PackagesInfo(this);
        refresh();
    }

    @SuppressWarnings("unchecked")
    public void getRunningProcess() {
        List<RunningAppProcessInfo> list2 = am.getRunningAppProcesses();
        ArrayList<DetailProcess> list = new ArrayList<DetailProcess>();
        for (RunningAppProcessInfo ti : list2) {
            if (ti.processName.equals("system") || ti.processName.equals("com.android.phone")) {
                continue;
            }
            DetailProcess dp = new DetailProcess(this, ti);
            dp.fetchApplicationInfo(packageinfo);
            dp.fetchPackageInfo();
            dp.fetchPsRow(pinfo);
            if (dp.isGoodProcess()) {
                list.add(dp);
            }
        }
        Collections.sort(list);
        adapter = new ProcessListAdapter(this, list);
    }

    private class LoadFinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context ctx, Intent intent) {
            TaskManager.this.setProgressBarIndeterminateVisibility(false);
            TaskManager.this.getListView().setAdapter(adapter);
        }
    }
}
