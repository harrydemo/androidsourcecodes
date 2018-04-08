package com.demo.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;

public class MainActivity extends Activity {
	private Gallery gallery;
	private FocusAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gallery=(Gallery)findViewById(R.id.gallery);
        adapter=new FocusAdapter(this);
        
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("focusImage",R.drawable.focus_1);
        map1.put("intro", "这是内容简介1这是内容简介1这是内容简介1这是内容简介1这是内容简介1这是内容简介1");
        
        Map<String,Object> map2=new HashMap<String,Object>();
        map2.put("focusImage",R.drawable.focus_2);
        map2.put("intro", "这是内容简介2这是内容简介2这是内容简介2这是内容简介2这是内容简介2这是内容简介2");
         
        Map<String,Object> map3=new HashMap<String,Object>();
        map3.put("focusImage",R.drawable.focus_3);
        map3.put("intro", "这是内容简介3这是内容简介3这是内容简介3这是内容简介3这是内容简介3这是内容简介3");
        
        Map<String,Object> map4=new HashMap<String,Object>();
        map4.put("focusImage",R.drawable.focus_4);
        map4.put("intro", "这是内容简介4这是内容简介4这是内容简介4这是内容简介4这是内容简介4这是内容简介4");
        
        Map<String,Object> map5=new HashMap<String,Object>();
        map5.put("focusImage",R.drawable.focus_5);
        map5.put("intro", "这是内容简介5这是内容简介5这是内容简介5这是内容简介5这是内容简介5这是内容简介5");
        
        adapter.addFocus(map1);
        adapter.addFocus(map2);
        adapter.addFocus(map3);
        adapter.addFocus(map4);
        adapter.addFocus(map5);
        gallery.setAdapter(adapter);
    }
}