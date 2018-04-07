package com.levono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ApplicatonList extends Activity {

    private List<Map<String, Object>> data;
    Map<String, Object> item;
    private ListView listView = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        data = new ArrayList<Map<String, Object>>();
        listPackages();
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2, new String[] {"appname","pname" }, new int[] {
                 android.R.id.text1, android.R.id.text2, });
        listView.setAdapter(adapter);
        setContentView(listView);	
    }
    
    class PInfo {     
        private String appname = "";     
        private String pname = "";     
        private String versionName = "";     
        private int versionCode = 0;     
        private Drawable icon;     
        private void prettyPrint() {     
            Log.i("taskmanger",appname + "\t" + pname + "\t" + versionName + "\t" + versionCode + "\t");     
        }     
    } 
    
    private void listPackages() {     
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */     
        final int max = apps.size();     
        for (int i=0; i<max; i++) {     
            apps.get(i).prettyPrint();   
            item = new HashMap<String, Object>();
            item.put("appname", apps.get(i).appname);
            item.put("pname", apps.get(i).pname);
            data.add(item);     
        }     
    }   
    
    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {     
        ArrayList<PInfo> res = new ArrayList<PInfo>();             
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);     
        for(int i=0;i<packs.size();i++) {     
            PackageInfo p = packs.get(i);     
            if ((!getSysPackages) && (p.versionName == null)) {     
                continue ;     
            }     
            PInfo newInfo = new PInfo();     
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();     
            newInfo.pname = p.packageName;     
            newInfo.versionName = p.versionName;     
            newInfo.versionCode = p.versionCode;     
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());  
            res.add(newInfo);     
        }     
        return res;      
    }    

	
}