package com.lfp.busactivity;


import com.lfp.domain.Bus;
import com.lfp.service.BusService;
import com.lfp.service.ImportDBFile;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BusActivity extends Activity {
    /** Called when the activity is first created. */
	private BusService busService;
	public ImportDBFile dbfile;
	private SQLiteDatabase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//�������ݿ�
        dbfile = new ImportDBFile(this);

        dbfile.openDatabase();

        dbfile.closeDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText key = (EditText) this.findViewById(R.id.key);
        final EditText station = (EditText) this.findViewById(R.id.station);
        Button seach = (Button) this.findViewById(R.id.seach);
        final TextView result = (TextView) this.findViewById(R.id.result);
        

        //���ݿ������
		//busService = new BusService(this);
        
        database = SQLiteDatabase.openOrCreateDatabase(ImportDBFile.DB_PATH + "/" + ImportDBFile.DB_NAME, null);
        //��·�������,��ý���ʱ�����վ��
        key.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						station.setText("");
						return false;
					}
            });
        //������ť����
        seach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String busline = key.getText().toString();
				String busstation = station.getText().toString();
				//Bus bus = busService.find(ddd);
				Bus bus = new Bus();
				StringBuffer lines= new StringBuffer(); 
				Cursor cursor = null;
				//boolean flag = (ddd).matches("[0-9]*");
				if(busstation != null && !busstation.equals("") && busstation.length()>1){
					cursor = database.rawQuery("select id,line,station from bus_line where station like '%"+busstation+"%'", null);
					if(cursor.moveToNext()){
						while(cursor.moveToNext()){//������¼��
							bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
							lines.append(cursor.getString(1));
							lines.append("*");
							bus.setStation(cursor.getString(2));
						}
						bus.setLine(lines.toString());//������·������һ��
					}else{
						bus = null;
					}
				}else if(busline != null && !busline.equals("")){
					cursor = database.rawQuery("select id,line,station from bus_line where line like '%"+busline+"%'", null);
					if(cursor.moveToNext()){//������¼��
						bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
						lines.append(cursor.getString(1));
						lines.append("*");
						bus.setStation(cursor.getString(2));
						bus.setLine(lines.toString());//������·������һ��
					}else{
						bus = null;
					}
					
				}else{
					cursor = null;
					bus = null;
				}
				
				
				if(bus == null){
					result.setText("û����Ҫ�ҵ���Ϣ");
				}else{
					if(busstation != null && !busstation.equals("") && busstation.length()>1){
						result.setText(bus.getLine());
					}else{
						result.setText(bus.getStation());
					}
				}
			}
		});
        
    }
    private static final int OK = 1;
	private static final int CANCLE = 2;
	private static final int ABOUT = 3;
    @Override
    //�����Menu��ʱ��򿪲˵������˵���һ�α���ʱ����ܻص��÷���
    public boolean onCreateOptionsMenu(Menu menu){
    	//Ϊ�˵����һ��idΪ1������Ϊ����ʼ����Ԫ��
    	menu.add(0, OK, 0, "����");
    	menu.add(0, CANCLE, 0, "��ϵ��");
    	menu.add(0, ABOUT, 0, "�˳�");
    	//Ϊ�˵����һ���Ӳ˵���idΪ3������Ϊ�����ڡ��������ظ��Ӳ˵�����Ϊfile
    	//Menu file = menu.addSubMenu(0, ABOUT, 0, "�˳�");
    	//�õ�һ��MenuInflater
    	MenuInflater inflater = getMenuInflater();
    	//����inflater��inflater��������ȡ��Դ�ļ��ж����Ԫ��
    	//������ЩԪ����ӽ��ƶ���Menu����file
    	//inflater.inflate(R.menu.menu, file);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	//���ݱ�ѡ�е�Item���в�ͬ�Ĵ���
    	final TextView result = (TextView) this.findViewById(R.id.result);
    	switch(item.getItemId()){
    	case OK:
    		this.setTitle("��������������ѯ��������");
    		result.setText("�ոսӴ�android��д����������������ѯ��\n1.������·��ѯ�����Բ�ѯ������·������վ�㡣\n\n2.����վ�㣬���Բ�ѯ��ͣ����վ�Ĺ�������\n\n3.���˹����Ժ������ƣ�");
    		return true;
    	case CANCLE:
    		this.setTitle("��������������ѯ������ϵ��");
    		result.setText("��ϵ��ʽ��\n email:bjlfp@163.com; \n\n��ӭ��ҽ����������ݴ���ĸ��ҷ��ʼ�лл��");
    		return true;
    	case ABOUT:
    		this.setTitle("�˳�");
    		BusActivity.this.finish();
    		return true;
    	}
    	return false;
    }
}