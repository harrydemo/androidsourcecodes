package hbq.android.myapp.listview;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
/**
 * 
 * @author huangbq
 *
 */
public class MainActivity extends ListActivity implements OnScrollListener {
	
	private static final String TAG = "MainActivity";
	private listViewAdapter adapter = new listViewAdapter();
	ListView listView ;
	private int lastItem = 0;
	LinearLayout loadingLayout;
	/**
	 * ���ò�����ʾ����
	 */
    private LayoutParams mLayoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    /**
	 * ���ò�����ʾĿ���������
	 */
    private LayoutParams FFlayoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
    
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate(Bundle savedInstanceState)>>>>>>>>>>>>>>>" );
	    //���Բ���
		LinearLayout layout = new LinearLayout(this);
	   //���ò��� ˮƽ����
		layout.setOrientation(LinearLayout.HORIZONTAL);
		 //������
		progressBar = new ProgressBar(this);
		 //��������ʾλ��
		progressBar.setPadding(0, 0, 15, 0);
		//�ѽ��������뵽layout��
		layout.addView(progressBar, mLayoutParams);
		//�ı�����
		TextView textView = new TextView(this);
		textView.setText("������...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		//���ı����뵽layout��
		layout.addView(textView, FFlayoutParams);
		//����layout���������򣬼����뷽ʽ��
		layout.setGravity(Gravity.CENTER);
		
		//����ListView��ҳ��layout
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);
		
		//�õ�һ��ListView������ʾ��Ŀ
		listView = getListView();
		//��ӵ���ҳ��ʾ
		listView.addFooterView(loadingLayout);
		//��ListView���������
		setListAdapter(adapter);
		//��ListViewע���������
		listView.setOnScrollListener(this);
	}
	@Override
	public void onScroll(AbsListView v, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.i(TAG , "Scroll>>>first: " + firstVisibleItem + ", visible: " + visibleItemCount + ", total: " + totalItemCount);
		lastItem = firstVisibleItem + visibleItemCount - 1;
		Log.i(TAG , "Scroll>>>lastItem:" + lastItem);
		//��ʾ50��ListItem����0-49����ΪonScroll���ڡ�������ִ�й�֮��Ŵ�����������adapter.count<=41������
		if (adapter.count<=41) {
			if (firstVisibleItem+visibleItemCount==totalItemCount) {
				adapter.count += 10;
				adapter.notifyDataSetChanged();
				listView.setSelection(lastItem);
				int currentPage=adapter.count/10;
				Toast.makeText(getApplicationContext(), "��"+currentPage+"ҳ", Toast.LENGTH_LONG).show();
			}
		}
		else {
			 listView.removeFooterView(loadingLayout); 
		}
		

		
	}
	@Override
	public void onScrollStateChanged(AbsListView v, int state) {
		if (lastItem == adapter.count && state == OnScrollListener.SCROLL_STATE_IDLE) {
			Log.i(TAG,"ScrollStateChanged>>>state:"+state+"lastItem:" + lastItem);
			//��ʾ50��ListItem����0-49����ΪonScrollStateChanged���ڡ��϶�������ִ�й�֮��Ŵ�����������adapter.count<=41������
			if (adapter.count<=41) {
				adapter.count += 10;
				adapter.notifyDataSetChanged();

			}


		}
	}
  /**
   * Ҫ������������ʾ����
   * @author huangbq
   *
   */
	class listViewAdapter extends BaseAdapter {
		int count = 10;

		public int getCount() {
			Log.i(TAG, "getCount>>>count:" + count);
			return count;
		}

		public Object getItem(int pos) {
			Log.i(TAG, "getItem>>>pos:" + pos);
			return pos;
		}

		public long getItemId(int pos) {
			Log.i(TAG, "getItemId>>>ItemId:" + pos);
			return pos;
		}

		public View getView(int pos, View v, ViewGroup p) {
			Log.i(TAG, "getView>>>pos:" + pos);
			TextView view;
			if (v==null) {
				view = new TextView(MainActivity.this);
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