package hbq.android.myapp.listview;


import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
/**
 * 
 * @author huangbq
 *
 */
public class ButtonLoadingActivity extends ListActivity    {
	private static final String TAG = "ButtonLoadingActivity";
       
    private listViewAdapter adapter = new listViewAdapter();
    private ListView listView ;
    /**
	 * 设置布局显示的属性
	 */
    private LayoutParams mLayoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    /**
	 * 设置布局显示目标最大化属性
	 */
    private LayoutParams FFlayoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
    
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
             Log.i(TAG, "onCreate(Bundle savedInstanceState)>>>>>>>>>>>>>>>" );
              LinearLayout layout = new LinearLayout(this);
              layout.setOrientation(LinearLayout.HORIZONTAL);
             
              Button button = new Button(this);
              button.setText("点击加载下10条...");
              button.setGravity(Gravity.CENTER_VERTICAL);
               
              layout.addView(button,FFlayoutParams);
              layout.setGravity(Gravity.CENTER);
              LinearLayout loadingLayout = new LinearLayout(this);
              loadingLayout.addView(layout,mLayoutParams);
              loadingLayout.setGravity(Gravity.CENTER);
               
               
              listView = getListView();
               
              listView.addFooterView(loadingLayout);
               
              button.setOnClickListener(new Button.OnClickListener()   
              {         @Override      
                 public void onClick(View v)  
              {         
                  adapter.count += 10;  
                  Log.i(TAG, "setOnClickListener:" +  adapter.count);
                  adapter.notifyDataSetChanged();
				  int currentPage=adapter.count/10;
				  Toast.makeText(getApplicationContext(), "第"+currentPage+"页", Toast.LENGTH_LONG).show();
                  }      
              });  
             
          setListAdapter(adapter);  
     }  

    class listViewAdapter extends BaseAdapter {
        int count = 10; /* starting amount */

        public int getCount() { return count; }
        public Object getItem(int pos) { return pos; }
        public long getItemId(int pos) { return pos; }

        public View getView(int pos, View v, ViewGroup p) {
			Log.i(TAG, "getView>>>pos:" + pos);
			TextView view;
			if (v==null) {
				view = new TextView(ButtonLoadingActivity.this);
			}
			else {
				view=(TextView)v;
			}
			view.setText("ListItem " + pos);
			view.setTextSize(20f);
			view.setGravity(Gravity.CENTER);
			view.setHeight(60);
			return view;
         }
     }



}