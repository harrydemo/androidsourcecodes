package com.china.square;

import java.util.ArrayList;
import java.util.List;

import com.china.square.elos.Elos;
import com.china.square.eloscomplex.ElosComplex;
import com.china.square.elossap.ElosSap;
import com.china.square.linksee.LinkSee;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Square extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent("SQUARE", null);
        mainIntent.addCategory("android.intent.category.SQUARE");
        List<ResolveInfo> ls = pm.queryIntentActivities(mainIntent, 0);
        
        int i = 0;
        ArrayList<String>al = new ArrayList<String>();
        setTitle(String.valueOf(ls.size()));
        while (i < ls.size()){
        	ResolveInfo ri = ls.get(i);
        	al.add(ri.loadLabel(pm).toString());
        	i++;
        }
        ArrayAdapter<String>a = new  ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        
        ListView lv = (ListView)this.findViewById(R.id.lv);
        lv.setAdapter(a);
        lv.setOnItemClickListener(new ItemClickListener());
    }
    private class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String strText = ((TextView)arg1).getText().toString();
			
			if (strText == null) return;
			else if (strText.equals("¶íÂÞË¹·½¿é")){
				Intent in = new Intent(Square.this, Elos.class);
				startActivity(in);
			}else if (strText.equals("¶íÂÞË¹·½¿é-¸´ÔÓ°æ")){
				Intent in = new Intent(Square.this, ElosComplex.class);
				startActivity(in);
			}else if (strText.equals("Á¬Á¬¿´")){
				Intent in = new Intent(Square.this, LinkSee.class);
				startActivity(in);
			}
			else if (strText.equals("É¨À×")){
				Intent in = new Intent(Square.this, ElosSap.class);
				startActivity(in);
			}
		}
    	
    }
}