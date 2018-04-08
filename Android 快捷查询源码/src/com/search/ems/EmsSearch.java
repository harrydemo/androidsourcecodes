package com.search.ems;

import com.search.R;
import com.search.common.ActivityUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 快递查询
 * 
 * @author Administrator
 *
 */
public class EmsSearch extends Activity {
	
	private Spinner emsCompanies;
	
	private EditText emsOrder;
	
	private Resources res;
	
	private String[] emsCodes;
	
	private int pos = 0;
	
	private String selectedCom;
	
	private Search search;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ems);
        
        res = this.getResources();
        
        emsCodes = res.getStringArray(R.array.ems_code);

        emsOrder = (EditText)this.findViewById(R.id.ems_order);
        
        emsCompanies = (Spinner)this.findViewById(R.id.ems_com);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ems_company, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emsCompanies.setAdapter(adapter);
        emsCompanies.setOnItemSelectedListener(selectedListener);
        
        search = new Search();
    }
    
    
    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			// 用户选择了，记录下用户选择的公司所对应的代码
			//String item = (String)parent.getItemAtPosition(position);
			pos = position;
			selectedCom = (String)parent.getItemAtPosition(position);
			//Toast.makeText(EmsSearch.this, "position:"+position, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
    
	public String getCode(){
		
		return emsCodes[pos-1]; //因为城市spinner中多了一个请选择
	}

	//检验是否选择了一个快递公司
	public boolean validateSelect(){
		if(pos < 1 || pos > emsCodes.length){
			return false;
		}else{
			return true;
		}
	}

	

    public void onClick(View v){
    	
    	if(v.getId() == R.id.ems_search){
    		String order = this.emsOrder.getText().toString().trim();
    		if(this.validateSelect()){
    			if(ActivityUtils.validateNull(order)){
    				//校验成功
    				String companyCode = this.getCode();
    				//Log.v("CompanyCode", companyCode);
    				Bundle bundle = new Bundle();
    				bundle.putString("company", selectedCom);
    				bundle.putString("order", order);
    				this.getIntent().putExtras(bundle); //记录下公司和单号
    				EmsListener listener = new EmsListener(this);
    				search.asyncRequest(companyCode, order, listener);
    				
    			}else{
    				ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.ems_order_error));
    			}
    		}else{
    			ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), res.getString(R.string.ems_noselect));
    		}
    			
    	}
    }
}
