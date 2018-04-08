package cn.itcreator.android.reader;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CatalogAdapter extends ArrayAdapter<String> {
	
	private int resource;

	public CatalogAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context,textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout catalogItem;
		
		if(convertView == null){
			catalogItem = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater)getContext().getSystemService(inflater);
			catalogItem = (LinearLayout) vi.inflate(resource, null);
		}else{
			catalogItem = (LinearLayout)convertView;
		}
		
		TextView tv = (TextView)catalogItem.findViewById(R.id.catalog_item);
		tv.setText(getItem(position));
		
		return catalogItem;
	}

}
