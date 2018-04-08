package cn.edu.xtu.tilepuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChoosePhotoActivity extends Activity
{
	//private static final String[]	m_Countries	= { "��Ů", "����", "����","����" };

	//private TextView				m_TextView;
	//private Spinner					m_Spinner;
	//private ArrayAdapter<String>	adapter;
	private GridView gridview;
	//private 
	//View view;
	//private Menu menu;
	/**
	 * ������������ ��ͼƬԴ
	 * 0	��Ů
	 * 1	����
	 * 2	����
	 * 3	����
	 * */
	private int flag=1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_photo);
	/*	//LayoutInflater factory = LayoutInflater.from(ChoosePhotoActivity.this);
		// �õ��Զ���Ի���
		//view = factory.inflate(R.layout.choose_photo, null);
		

		//m_TextView = (TextView) findViewById(R.id.TextView1);
		m_Spinner = (Spinner) findViewById(R.id.PhostoSpinner);

		//����ѡ������ArrayAdapter����
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_Countries);

		//���������б�ķ��
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//��adapter��ӵ�m_Spinner��
		m_Spinner.setAdapter(adapter);

		//���Spinner�¼�����
		m_Spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//m_TextView.setText("���Ѫ���ǣ�" + m_Countries[arg2]);
				System.out.println("=================================ͼƬ�����ǣ�" +m_Countries[arg2]);
				//������ʾ��ǰѡ�����
				arg0.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
			}

		});
		//m_Spinner.isEnabled();
		//setContentView(view);
		//m_Spinner.VISIBLE;
	*/	
		//ȡ��GridView����
		gridview = (GridView) findViewById(R.id.gridview);
		//���Ԫ�ظ�gridview
		gridview.setAdapter(new ImageAdapter(this,flag));

		// ����Gallery�ı���
		gridview.setBackgroundResource(R.drawable.bg);

		//�¼�����
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Toast.makeText(ChoosePhotoActivity.this, "��ѡ����" + position  + " ��ͼƬ", Toast.LENGTH_SHORT).show();
				System.out.println("��ѡ����" + flag+"��� "+position + " ��ͼƬ��ID="+ClassGameDB.mImageIds[flag][position]);
			//	Bitmap bitmap=((BitmapDrawable) getResources().getDrawable(GameDB.mImageIds[flag][position])).getBitmap(); //�õ�ͼƬ��
				Bundle bundle=new Bundle();			
				bundle.putInt("flag", flag);
				bundle.putInt("position", position);
				Intent intent =new Intent();	
			//	long temp=GameDB.mImageIds[flag][position];
				intent.putExtras(bundle);
				//intent.putExtra("number",GameDB.mImageIds[flag][position]);
				intent.setClass(ChoosePhotoActivity.this, ShowPhotoActivity.class);
				ChoosePhotoActivity.this.startActivity(intent);
				//System.out.println(GameDB.mImageIds[flag][position]);
			}
		});
	}
	
	
	
	
	
	/*����menu*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		//����menu����Ϊres/menu/menu.xml
		inflater.inflate(R.menu.menuphoto, menu);
		//this.menu=menu;
		return true;
	}
/*
	@Override	
	public void onCreateContextMenu(Menu menu, View v,ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		//����menu����Ϊres/menu/menu.xml
		inflater.inflate(R.menu.menuplay, menu);
		this.menu=menu;
		//super.onCreateContextMenu(menu, v, menuInfo);
		super.onCreateContextMenu(menu, boardView, menuInfo);
	}
*/
	/*����˵��¼�*/
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//�õ���ǰѡ�е�MenuItem��ID,
		int item_id = item.getItemId();

		switch (item_id)
		{			
		
			case R.menu_photo_id.exitItem:
				System.out.println("R.menu_photo_id.exitItem:"+item_id);
				ChoosePhotoActivity.this.finish();
				this.onDestroy();
				break;
			case R.menu_photo_id.mmItem:	
				System.out.println("R.menu_photo_id.mmItem:"+item_id);
				flag=ClassGameDB.PHOTO_MM_NUMBERS;
				gridview.setAdapter(new ImageAdapter(this,flag));
				break;
			case R.menu_photo_id.dongmanItem:				
				System.out.println("R.menu_photo_id.dongmanItem:"+item_id);
				flag=ClassGameDB.PHOTO_DONGMAN_NUMBERS;
				gridview.setAdapter(new ImageAdapter(this,flag));
				break;
			case R.menu_photo_id.chuangyiItem:	
				System.out.println("R.menu_photo_id.chuangyiItem:"+item_id);
				flag=ClassGameDB.PHOTO_CHUANGYI_NUMBERS;
				gridview.setAdapter(new ImageAdapter(this,flag));
				break;
			case R.menu_photo_id.otherItem:
				System.out.println("R.menu_photo_id.otherItem:"+item_id);
				flag=ClassGameDB.PHOTO_OTHER_NUMBERS;
				gridview.setAdapter(new ImageAdapter(this,flag));
				break;
			default:
				System.out.println("R.menu_photo_id.default:"+item_id);
				break;
		}
		return true;
	}
}
