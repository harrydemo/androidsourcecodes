package ninjarush.mainactivity;


import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class DeveloperActivity extends Activity {
	
	//������ʾ������Ա��ListView
	private ListView lv;
	private TextView tv;
	//listview����Ŀ�Ĳ���
	private TextView tv_name,tv_mission;
	//��������������
	private String [] name={"���Ǵ�","�ۿ�","����","��ΰ","����Ұ","������","��ΰǫ"};
	//���������񣬸�������򼯺�
	private String [] mission={"GameMenu���棬��������,������Ч",
			"���ǵ�һϵ�ж������Լ��ɾͽ���",
			"�����࣬�ӵ��࣬ʳ���࣬������ײ����Ч��","��ͣ���棬�Լ�����ͼ����P",
			"Boss�࣬����Boss������",
			"GameIng���棬GameOver����,Loading���棬Developer����",
			"�����ţ������Ÿ��ֳ�"};
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.developer);
			tv=(TextView)findViewById(R.id.developer_tv);
			lv=(ListView)findViewById(R.id.deceloper_xml_listview);
			tv_name=(TextView)findViewById(R.id.developer_items_name);
			tv_mission=(TextView)findViewById(R.id.developer_items_mission);
			
			lv.setAdapter(new myAdapter());
		}
		
		
		
		class myAdapter extends BaseAdapter{

			public int getCount() {
				
				return name.length;
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				View view = LayoutInflater.from(DeveloperActivity.this).inflate(R.layout.developer_items, null);
				tv_name=(TextView) view.findViewById(R.id.developer_items_name);
				tv_mission=(TextView)view.findViewById(R.id.developer_items_mission);
				tv_name.setText(name[position]);
				tv_mission.setText(mission[position]);
				return view;
			}
			
		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode==KeyEvent.KEYCODE_BACK){
				this.finish();
			}
			return super.onKeyDown(keyCode, event);
		}
}
