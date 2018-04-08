package com.catt.oss;

import com.catt.oss.common.app.BaseActivity;
import com.catt.oss.common.data.Config;
import com.catt.oss.directory.KnowLedgeSearch;
import com.catt.oss.directory.KnowledgeManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener {
    private Button listManager;
    private Button knowledgeManager;
    private Button knowledgeSearch;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listManager=(Button) this.findViewById(R.id.btlist);       
        knowledgeManager=(Button) this.findViewById(R.id.btknowmanager);       
        knowledgeSearch=(Button) this.findViewById(R.id.btknowsearch); 
        setComponentListener();
    }
    public void setComponentListener(){
    	listManager.setOnClickListener(this);
    	knowledgeManager.setOnClickListener(this);
    	knowledgeSearch.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		 View view=null;
		 switch(v.getId()){
		 case R.id.btlist:{
			 Intent intent=new Intent();
			 intent.putExtra(Config.TITLE, Config.DIRECTORY);
			 intent.setClass(this, KnowledgeManager.class);
			 startActivity(intent);
			 break;
		 }
		 
		case R.id.btknowmanager:
		{
			Intent intent=new Intent();
			intent.putExtra(Config.TITLE, Config.KNOWLEDGE);
			intent.setClass(this, KnowledgeManager.class);
			startActivity(intent);
			break;
		}
		
		case R.id.btknowsearch:{
			Intent intent=new Intent();
			intent.setClass(this, KnowLedgeSearch.class);
			startActivity(intent);
			break;
		}
		 
	}
}
}