package com.hotmail.liuxuewei;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ViewDAO extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dutyname));
		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int pos, long id) {
						Toast.makeText(ViewDAO.this, dutyname[pos],
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	private final static String [] dutyname={"ÊŒ ÊŒ","»Ÿ √√","–€ √√","¥® √√","√ﬁ ∑Â","–° πÍ","±Ã ±Ã","«Â “Ø"};
}