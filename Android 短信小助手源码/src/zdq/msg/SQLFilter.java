package zdq.msg;

import java.util.ArrayList;
import java.util.List;
import zdq.data.PnBean;
import zdq.data.SqliteDatebase;
import zdq.msg.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SQLFilter extends Activity{
	private ListView listview;
	private Cursor cursor;
	private SqliteDatebase db;
	private ListAdapter listAdapter;
	
	private EditText et_pn;
	private Button bt_add;
	private Button bt_back;
	
	private List<PnBean> pnList = new ArrayList<PnBean>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.sqlfilter);
    	
    	this.setTitle("黑名单设置");
    	et_pn = (EditText) findViewById(R.id.et_pn);
    	bt_add = (Button) findViewById(R.id.bt_add);
    	bt_back=(Button)findViewById(R.id.bt_back);
    	
    	try{
    		db = new SqliteDatebase(this);	 	
        	/* 查询表，得到cursor对象 */
        	
    		cursor = db.getpn();
        	
        	cursor.moveToFirst();
        	while(!cursor.isAfterLast() && (cursor.getString(1) != null)){    
        		PnBean pn = new PnBean();
        		pn.setId(cursor.getString(0));
        		pn.setPn(cursor.getString(1));
        		pnList.add(pn);
        		cursor.moveToNext();
        	}
    	}catch(IllegalArgumentException e){
    		//当用SimpleCursorAdapter装载数据时，表ID列必须是_id，否则报错column '_id' does not exist
    		e.printStackTrace();
    	}
    	listview = (ListView)findViewById(R.id.listView);
    	listAdapter = new ListAdapter();
    	listview.setAdapter(listAdapter);
    	
    	
    	/* 插入表数据并ListView显示更新 */
    	bt_add.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(et_pn.getText().length() > 1){
					//插入数据 用ContentValues对象也即HashMap操作,并返回ID号
					Long id=db.insertpn(et_pn.getText().toString().trim());
					PnBean pn = new PnBean();
					pn.setId(""+id);
	        		pn.setPn(et_pn.getText().toString().trim());
	        		pnList.add(pn);
	        		listview.setAdapter(new ListAdapter());	
	        		resetForm();
				}
			}
		});
    	
    	
    	bt_back.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
    	
    }
    
    
    
    /* 重值form */
    public void resetForm(){
		et_pn.setText("");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    private class ListAdapter extends BaseAdapter{
    	public ListAdapter(){
    		super();
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pnList.size();
		}

		@Override
		public Object getItem(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}

		@Override
		public long getItemId(int postion) {
			// TODO Auto-generated method stub
			return postion;
		}
		
		
		@Override
		public View getView(final int postion, View view, ViewGroup parent) {
			view = getLayoutInflater().inflate(R.layout.listview, null);
			TextView tv = (TextView) view.findViewById(R.id.tvCity);
			tv.setText("" + pnList.get(postion).getPn());
			TextView bu = (TextView) view.findViewById(R.id.btRemove);
			bu.setText(R.string.delete);
			bu.setId(Integer.parseInt(pnList.get(postion).getId()));
			
			/* 删除表数据 */
			bu.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View view) {
					try{
						db.delpn(view.getId());
						pnList.remove(postion);
						listview.setAdapter(new ListAdapter());						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			return view;
		}
    }
}