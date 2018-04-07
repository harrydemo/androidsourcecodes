package com.parabola.main;
 
import java.util.ArrayList;
import java.util.List;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProvinceActivity extends Activity {
	public static final String TAG = "ProvinceActivity";
	WebServiceHelper wsh = new WebServiceHelper();
	public ProvinceAdapter provinceAdapter;
	public List<String> provinces = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.province);
        
        ListView province = (ListView) this.findViewById(R.id.proviceList);
        
        provinces = wsh.getProvince();
        provinceAdapter = new ProvinceAdapter();
        Log.i(TAG, ""+provinces);
        province.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { 
				Object obj = view.getTag();
				Log.i(TAG, ""+view.getTag());
				if (obj != null) {
					String id1 = obj.toString();
					Log.i(TAG, ""+ obj.toString());
					Intent intent = new Intent(ProvinceActivity.this, CityActivity.class);
					Bundle b = new Bundle();
					b.putString("province", id1);
					intent.putExtras(b);
					startActivity(intent);
				}
				
			}
        	
        });
        province.setAdapter(provinceAdapter);
    }
	
	class ProvinceAdapter extends BaseAdapter {

		@Override
		public int getCount() { 
			return provinces.size();
		}

		@Override
		public Object getItem(int position) { 
			return provinces.get(position);
		}

		@Override
		public long getItemId(int position) { 
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//下面俩种方法都可以
			// LayoutInflaterinflater=getLayoutInflater();
			//LayoutInflaterinflater=(LayoutInflater)mContext.getSystemServic(LAYOUT_INFLATER_SERVICE);

			LayoutInflater mInflater = getLayoutInflater(); 
			convertView =  mInflater.inflate(R.layout.province_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.proviceText);
			tv.setText(provinces.get(position));
			convertView.setTag(tv.getText());
		 
			return convertView;
		}
		
	}
}
