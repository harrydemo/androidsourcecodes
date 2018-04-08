package test.gf;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddButtonprojActivity extends Activity {
	private List list = new ArrayList();
	int count=1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn = (Button)findViewById(R.id.Button01);
        btn.setOnClickListener(new OnClickListener(){
        		public void onClick(View v){
        			addTableRow(v);
        		}
        } );
        

        Button btnDefine = (Button)findViewById(R.id.btnDefine);
        btnDefine.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			toSystemOut(v);
    		}
        } );
    }
    
   
    public void addTableRow(View v){
    	TableLayout ll=(TableLayout)findViewById(R.id.lla);
		TableRow tableRow = new TableRow(v.getContext());
		
		String msg=AddButtonprojActivity.this.getResources().getString(R.string.buttons);
		
		Spinner tempB=new Spinner(AddButtonprojActivity.this);
		tempB.setPrompt("");
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors ,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		tempB.setAdapter(adapter);
		tempB.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            	System.out.println("Spinner1: position=" + position + " id=" + id);
            	toChangeTempCSpinner(position,view);
            }
            public void onNothingSelected(AdapterView<?> parent) {
              
            }
        });
		tableRow.addView(tempB);
		
		Spinner tempC=new Spinner(AddButtonprojActivity.this);
		tempC.setPrompt("");
		tableRow.addView(tempC);
		
		
		Button tempD=new Button(AddButtonprojActivity.this);
		tempD.setOnClickListener(new OnClickListener(){
     		public void onClick(View v){
     			deleteTableRow(v);
     		}
		 } );
		
		tempD.setText("删除");
		tempD.setWidth(180);
		tableRow.addView(tempD);
		tableRow.setId(count);
		
		ll.addView(tableRow);
    }
    
    
    public void deleteTableRow(View v){
    	
    	TableRow parent = (TableRow) v.getParent();     

        ((TableLayout) parent.getParent()).removeView(parent);   
          
    }
    
    
    public void toSystemOut(View v){
    	TableLayout ll = (TableLayout)findViewById(R.id.lla);
    	String out = "";
    	for(int i =0 ; i < count ; i ++){

        	TableRow row = (TableRow)ll.getChildAt(i);
        	System.out.println("row ="+ i +"   "+ row);
        	Button btn = (Button)row.getChildAt(1);
        	if(row!=null){
        		System.out.println("btn ="+ i +"   "+ btn.getText());
        	}
        	if(row==null){
        		return;
        	}
    	}
    }
    
    
    public void toChangeTempCSpinner(int position,View v){
    	Spinner spinnerB = (Spinner) v.getParent(); 
    	
    	TableRow parent = (TableRow) spinnerB.getParent(); 
    	
    	Spinner tempC = (Spinner)parent.getChildAt(1);
    	
    	ArrayAdapter<CharSequence> adapterC;
    	if(position < 3){
    		adapterC= ArrayAdapter.createFromResource(this, R.array.search_menuModes ,android.R.layout.simple_spinner_item); 
    	}else{
    		adapterC= ArrayAdapter.createFromResource(this, R.array.planets ,android.R.layout.simple_spinner_item);
    	}
    	
		adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		tempC.setAdapter(adapterC);
		tempC.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){}
                public void onNothingSelected(AdapterView<?> parent) {
                  
                }
        });
    }
    


}