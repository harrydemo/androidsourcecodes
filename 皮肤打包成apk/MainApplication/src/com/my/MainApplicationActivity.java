
package com.my;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.style.ParagraphStyle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.my.R;

public class MainApplicationActivity extends Activity implements OnClickListener {
    TextView tvShow;
    ArrayList<PackageInfo> skinList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //根据项目包名得到所有皮肤包
        skinList = getAllSkin();
        initView();
    }
    void initView() {
        tvShow = (TextView)findViewById(R.id.tvShow);
        findViewById(R.id.btnNormal).setOnClickListener(this);
    }
    /**
     * 得到所有表情包
     * 
     * @return
     */
    private ArrayList<PackageInfo> getAllSkin() {
        ArrayList<PackageInfo> skinList = new ArrayList<PackageInfo>();
        //得到系统所有已安装项目的包名
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            if (isSkinPackage(p.packageName))
                skinList.add(p);
        }
        return skinList;
    }
    /**
     * 判断是否为表情包
     * 
     * @param packageName
     * @return
     */
    private boolean isSkinPackage(String packageName) {
        String rex = "com.myskin.\\w";
        Pattern pattern = Pattern.compile(rex);
        Matcher matcher = pattern.matcher(packageName);
        return matcher.find(); 
    }
    int i = 0;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNormal:                
                Context friendContext = null;
                try {
                    //得到皮肤项目上下文
                    friendContext = this.createPackageContext(skinList.get(i).packageName,
                            Context.CONTEXT_IGNORE_SECURITY);
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                //通过context 得到填充activity的布局文件 注意：布局文件是从skin1 或 skin2 项目中获取的 此处是关键                
                View view = View.inflate(friendContext, R.layout.main, null);
                //指定Activity布局
                setContentView(view);
                //初始化布局
                initView();
                i++;
                if (i >= skinList.size()) {
                    i = 0;
                }
                break;

            default:
                break;
        }
    }
}
