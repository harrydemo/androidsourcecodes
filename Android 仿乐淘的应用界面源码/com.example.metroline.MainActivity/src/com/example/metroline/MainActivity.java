package com.example.metroline;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;	
import com.example.metroline.Data;


public class MainActivity extends Activity {
	private ArrayAdapter<String> adapter; 
	private List<String> list = new ArrayList<String>();
	
	private ArrayAdapter<String> adapterstation; 
	private List<String> listStation = new ArrayList<String>();
	
	private Spinner inputSpinnerbegin;
	private Spinner inputSpinnerend;
	
	private Spinner inputSpinnerBeginStation;
	private Spinner inputSpinnerEndStation;
	
	private Button  inputButtonSearch;
	private Button  inputButtonEsc;
	
	Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.input);        
        
		//��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���     
        for(int i=0; i<Data.lineName.length; i++)
          list.add(Data.lineName[i]);     
       
          inputSpinnerbegin = (Spinner)this.findViewById(R.id.beginLine);
          inputButtonSearch = (Button)this.findViewById(R.id.searchButton);
          inputButtonEsc	= (Button)this.findViewById(R.id.escButton);
          adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          inputSpinnerbegin.setAdapter(adapter);
          
          inputSpinnerBeginStation = (Spinner)this.findViewById(R.id.startStation);
          inputSpinnerEndStation = (Spinner)this.findViewById(R.id.endStation);
          
          for(int i=0; i<Data.line1.length; i++)
              listStation.add(Data.line1[i]);
          
          adapterstation = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listStation);
          adapterstation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          inputSpinnerBeginStation.setAdapter(adapterstation);
          inputSpinnerEndStation.setAdapter(adapterstation);
          
          
  		//��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���     
//            list.add("һ����");     
//            list.add("������");     
//            list.add("������");     
//            list.add("�ĺ���");     
//            list.add("�����");
            inputSpinnerend = (Spinner)this.findViewById(R.id.endLine);
//            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            inputSpinnerend.setAdapter(adapter);
            
            SpinnerListener s = new SpinnerListener();
            inputSpinnerbegin.setOnItemSelectedListener(s);
            inputSpinnerend.setOnItemSelectedListener(s);
            
            InputListener l = new InputListener();
            inputButtonSearch.setOnClickListener(l);
            inputButtonEsc.setOnClickListener(l);
            
    }
    public class SpinnerListener implements Spinner.OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if(arg0 == inputSpinnerbegin){
				String [] stationName = Data.lineStation[arg2];
				
				listStation.clear();
		          for(int i=0; i<stationName.length; i++)
		              listStation.add(stationName[i]);
		          adapterstation = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, listStation);
		          adapterstation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		          inputSpinnerBeginStation.setAdapter(adapterstation);
			}
			else if(arg0 == inputSpinnerend){
				String [] stationName = Data.lineStation[arg2];
				
				listStation.clear();
		          for(int i=0; i<stationName.length; i++)
		              listStation.add(stationName[i]);
		          adapterstation = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, listStation);
		          adapterstation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		          inputSpinnerEndStation.setAdapter(adapterstation);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    public class InputListener implements View.OnClickListener{
    	public void onClick(View v) {
    	if(inputButtonEsc == v){
			System.exit(0);
    		}
    	else if(inputButtonSearch == v){
    		String strBeginLine = inputSpinnerbegin.getSelectedItem().toString();
    		String strBeginStation = inputSpinnerBeginStation.getSelectedItem().toString();
    		String strEndLine = inputSpinnerend.getSelectedItem().toString();
    		String strEndStation = inputSpinnerEndStation.getSelectedItem().toString();    		
    		searchRoad(strBeginLine, strBeginStation, strEndLine, strEndStation);
    	//	setContentView(R.layout.resoult);
    	}
    	}
    }
    
    TreeNode tree;
  
    Boolean checkThisNodeRemove(String lineName, TreeNode node){
    	
    	do{
    		node = node.parent;
	    	if(lineName.equals(node.name))
	    	{
	    		return true;
	    	}    		
    	}while(tree != node);

    	
    	return false;
    }
    //ͨ����·���ƻ�ȡ��·վ������
    String [] getLineDataByName(String name){
    	
    	for (int i=0; i<Data.lineName.length; i++){
    		if(name.equals(Data.lineName[i])){
    			return Data.lineStation[i];
    		}
    	}
    	return null;
    }
    //ͨ����·���ƻ�ȡ��·վ������
 
    
    String [] searchRoad(String strBeginLine, String strBeginStation, String strEndLine, String strEndStation){
    	if(strBeginLine.equals(strEndLine)){
    		;//ͬһ����·
    	}
    	else{
    	  QueryLine ql=new QueryLine();
    		//StrBenlin ���    strenLin �ص�
    		if(ql.queryLine(strBeginLine,strBeginStation, strEndLine,strEndStation))
    		{
    			String result="�����վ��ʼ����:---->"+"��·��"+strBeginLine+" ��վ��"+strBeginStation;

      			for(int i=0;i<ql.recordsLine.size();i++)
      			{
      				result+=("\n����"+ql.recordsLine.get(i)+"��"+ql.recordsStation.get(i)
      					+"--->");
      				
      			}
      			result+=("\n�յ�վ��---��"+"4������Ȧ�ֶ����");
      			
      			startActivity(new Intent(MainActivity.this,LineInfo.class).putExtra("result", result));
    		}
    		else{
    			String result="δ�ҵ�";
    			startActivity(new Intent(MainActivity.this,LineInfo.class).putExtra("result", result));
    		}
    		    	
    		
    	}
    	return  null;
    }

}
