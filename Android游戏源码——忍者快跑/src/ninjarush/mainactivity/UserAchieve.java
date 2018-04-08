package ninjarush.mainactivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import ninjarush.relatedclass.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UserAchieve extends Activity{
	//�ɾ�����
	private static String[] achieve={"�������ܴ���\n��������Ϊ500","�м����ܴ���\n��������Ϊ1000������ĵ����İ���","�߼����ܴ���\n��������Ϊ2000������С�","�ǻҼ����ܴ���\n��������4000���ϣ�����ôԶ�㲻��ѽ��","��Ͱ��\n�Ե�ʳ�ﳬ��10�����ϣ��Ա���û��Ҫ�������㣡","����ɱ��\nɱ��������5","�м���\nɱ��������15�����в�Ҫ�����������������ˣ�","�Ϲ�ɱ��\n�ɹ���ɱ�Ϲ�һ�Σ����ǲ�ɱ��һ�Σ���Ҫ̫���ţ�","�Ϲֿ�\n���ɱ¾�Ϲִﵽ3�����ϣ����϶����Ϲ��г�"};
	private ListView listView;
	private SimpleAdapter adapter;
	private Bitmap completed,uncompleted;
	public static int[] userAchieve;
	private List<Map<String, Object>> data;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achieve);
		listView=(ListView)findViewById(R.id.listview);
		completed=BitmapFactory.decodeResource(getResources(), R.drawable.completed);
		uncompleted=BitmapFactory.decodeResource(getResources(), R.drawable.uncompleted);
		listView.setAdapter(new MyAdapter(UserAchieve.this));
	}
	public static int[] getAchive(Context context){
		int[] userAchieve = new int[achieve.length];
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(context.openFileInput("achieve.txt")));
			String u=br.readLine();
			br.close();
			String[] temp=u.split(",");
			for(int i=0;i<temp.length;i++)
				userAchieve[i]=Integer.parseInt(temp[i]);
			return userAchieve;
		} catch (Exception e) {
			e.printStackTrace();
			for(int i=0;i<achieve.length;i++)
				userAchieve[i]=0;
			return userAchieve;
		}
	}
	public static void saveAchive(Context context){
		 try {
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(context.openFileOutput("achieve.txt", Context.MODE_PRIVATE)));
			for(int i=0;i<UserAchieve.userAchieve.length;i++)
				bw.write(UserAchieve.userAchieve[i]+",");
			bw.close();
		 } catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "�洢ʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	public class MyAdapter extends BaseAdapter{
		private Context context;
		public MyAdapter(Context context){
			this.context=context;
		}

		public int getCount() {
			return achieve.length;
		}

		public Object getItem(int position) {
			
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view=LayoutInflater.from(context).inflate(R.layout.listview, null);
			ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
			TextView textView=(TextView)view.findViewById(R.id.textview);
			if(userAchieve[position]==1)
				imageView.setImageBitmap(completed);
			else
				imageView.setImageBitmap(uncompleted);
			textView.setText(achieve[position]);
			return view;
		}
		
	}

}
