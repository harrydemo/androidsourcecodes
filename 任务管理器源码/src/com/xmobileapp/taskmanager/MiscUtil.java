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

import com.xmobileapp.taskmanager.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;

public class MiscUtil {

    public static final int MENU_CANCEL = 0;
    public static final int MENU_SWITCH = 1;
    public static final int MENU_KILL = 2;
    public static final int MENU_DETAIL = 3;
    public static final int MENU_UNINSTALL = 4;

    public static PackageInfo getPackageInfo(PackageManager pm, String name) {
        PackageInfo ret = null;
        try {
            ret = pm.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
        	//TODO: 异常处理
        }
        return ret;
    }

    public static Dialog getTaskMenuDialog(final TaskManager ctx, final DetailProcess dp) {

        return new AlertDialog.Builder(ctx).setTitle(R.string.operation).setItems(
                R.array.menu_task_operation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case MENU_KILL: {
                                ctx.am.restartPackage(dp.getPackageName());
                                if (dp.getPackageName().equals(ctx.getPackageName())) return;
                                ctx.refresh();
                                return;
                            }
                            case MENU_SWITCH: {
                                if (dp.getPackageName().equals(ctx.getPackageName())) return;
                                Intent i = dp.getIntent();
                                if (i == null) {
                                    Toast.makeText(ctx, R.string.message_switch_fail, Toast.LENGTH_LONG)
                                            .show();
                                    return;
                                }
                                try {
                                    ctx.startActivity(i);
                                } catch (Exception ee) {
                                    Toast.makeText(ctx, ee.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                return;
                            }
                            case MENU_UNINSTALL: {
                                Uri uri = Uri.fromParts("package", dp.getPackageName(), null);
                                Intent it = new Intent(Intent.ACTION_DELETE, uri);
                                try {
                                    ctx.startActivity(it);
                                } catch (Exception e) {
                                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                return;
                            }
                            case MENU_DETAIL: {
                                Intent detailsIntent = new Intent();
                                detailsIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                detailsIntent.putExtra("com.android.settings.ApplicationPkgName", dp.getPackageName());
                                ctx.startActivity(detailsIntent);
                                return;
                            }
                        }
                    }
                }).create();
    }
}
