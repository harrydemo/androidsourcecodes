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

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class IntentList {
    private static List<ResolveInfo> infoList = null;

    public static synchronized List<ResolveInfo> getRunableList(PackageManager pm, boolean reload) {
        if (infoList == null || reload == true) {
            Intent baseIntent = new Intent(Intent.ACTION_MAIN);
            baseIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            infoList = pm.queryIntentActivities(baseIntent, 0);
        }
        return infoList;
    }

    public static Intent getIntent(String packageName, PackageManager pm) {
        List<ResolveInfo> list = getRunableList(pm, false);
        for (ResolveInfo info : list) {
            if (packageName.equals(info.activityInfo.packageName)) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                i.setClassName(packageName, info.activityInfo.name);
                return i;
            }
        }
        return null;
    }

}
