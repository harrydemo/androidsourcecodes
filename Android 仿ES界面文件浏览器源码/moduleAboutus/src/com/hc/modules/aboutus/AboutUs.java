package com.hc.modules.aboutus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AboutUs {
    public static AlertDialog getAboutUsDialog(Context context){
        return createAboutUsDialog(context);
    }
    public static AlertDialog getAboutUsDialog(Context context,int viewID,int titleId){
        return createAboutUsDialog(context, viewID, titleId);
    }
    public static AlertDialog getAboutUsDialog(Context context,int viewID,String title){
        return createAboutUsDialog(context, viewID, title);
    }
    private static AlertDialog createAboutUsDialog(Context context){
        TextView tv = new TextView(context);
        String app_version_name = "";
        String appName = "";
        PackageManager manager = context.getPackageManager();
        ApplicationInfo info;
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            info = packageInfo.applicationInfo;
            app_version_name = packageInfo.versionName;
            appName = info.loadLabel(manager).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        tv.setText(context.getString(R.string.tip_version) + app_version_name);
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        ll.setPadding(0, 20, 0, 20);
        ll.addView(tv, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(appName)
        .setView(ll)
        .create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    private static AlertDialog createAboutUsDialog(Context context,int viewID,int titleId){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(viewID, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(titleId)
        .setView(view)
        .create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    private static AlertDialog createAboutUsDialog(Context context,int viewID,String title){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(viewID, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(title)
        .setView(view)
        .create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
