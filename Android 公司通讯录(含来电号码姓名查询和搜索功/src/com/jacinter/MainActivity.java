package com.jacinter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

//����������棬��Ҫ�������ݲ�����ʾ����
public class MainActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
    MySQLiteHelper myHelper;  
    private Button searchButton= null;
    private EditText searchBox=null;
    private String searchText=null;
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //�����绰�����������
        //startService(new Intent(this, CallShowService.class));
        startService(new Intent(this, FloatingWindowService.class));
        
        searchButton=(Button)findViewById(R.id.button1);
        searchBox=(EditText)findViewById(R.id.editText1);
        searchButton.setOnClickListener(new ButtonListener());
        //Ϊһ����Ŀ�ṩ����                   
        //Groups  ����,���ж�����17���飬����ֱ������������������֣������Ѳ���
        String[] str={"��˾�쵼","�ۺϹ���","����","����һ��","���۶���","��������","�ͻ�����","������","������","�г���","�ܾ���","�ͻ�����","����һ��","��������","��������","����һ��","�Ͳ�����"};
        
 

        List<Map<String,Object>> Groups = new ArrayList<Map<String,Object>>();
        for(int i=0;i<str.length;i++){
        	Map<String,Object> group = new HashMap<String,Object>();
        	group.put("groupname", str[i]);
        	Groups.add(group);
        }
        

        
        //��������List<Map<String,Object>>  child1��child2
        //Ϊ������Ŀ�ṩ����
        //child1
        List<List<Map<String,Object>>> Childs = new ArrayList<List<Map<String,Object>>>();

        for (int j=0;j<str.length;j++){
        	List<Map<String,Object>> child = new ArrayList<Map<String,Object>>(); 
        	//�����1Ϊ���ݿ�İ汾�ţ��´θ���ʱ��ֻҪ���ݿ�Ŵ���1�Ϳ���
            myHelper = new MySQLiteHelper(this, "my.db", null, 2); 
            SQLiteDatabase db = myHelper.getReadableDatabase();  
            Cursor cursor = db.rawQuery("SELECT name,telephone FROM jacnamelist where department = ?", new String[]{String.valueOf(j)});  

            if( cursor != null ){
               if( cursor.moveToFirst() ){
                   do{
   			        Map<String,Object> child1date = new HashMap<String,Object>();
   			        	child1date.put("UserName", cursor.getString(0));
   			        	child1date.put("Telephone", cursor.getString(1) );
   			        	child.add(child1date);
                        }while( cursor.moveToNext());
                  }
             }
            cursor.close();
            db.close();
            
	        Childs.add(child);
        }
        /**
         * ʹ��SimpleExpandableListAdapter��ʾExpandableListView
         * ����1.�����Ķ���Context
         * ����2.һ����ĿĿ¼����
         * ����3.һ����Ŀ��Ӧ�Ĳ����ļ�
         * ����4.fromto������map�е�key��ָ��Ҫ��ʾ�Ķ���
         * ����5.�����4��Ӧ��ָ��Ҫ��ʾ��groups�е�id
         * ����6.������ĿĿ¼����
         * ����7.������Ŀ��Ӧ�Ĳ����ļ�
         * ����8.fromto������map�е�key��ָ��Ҫ��ʾ�Ķ���
         * ����9.�����8��Ӧ��ָ��Ҫ��ʾ��childs�е�id
         */

        SimpleExpandableListAdapter simpleExpandListAdapter = new SimpleExpandableListAdapter(MainActivity.this, 
        		Groups, R.layout.group, new String[]{"groupname"},new int[]{R.id.group},
        		Childs, R.layout.child, new String[]{"UserName","Telephone"},new int[]{R.id.id,R.id.name});
        
        setListAdapter(simpleExpandListAdapter);

    }
    class ButtonListener implements OnClickListener{
    	//���ɸ���Ķ��󣬲�����ע�ᵽ�ؼ��ϡ�����ÿؼ����û����£��ͻ�ִ��onClick���� 
		public void onClick(View v) {
			//����һ��Intent����
	        searchText=searchBox.getText().toString();

			Intent intent = new Intent();
			//����Intent����Ҫ������Activity
			Bundle bundle = new Bundle();  
			bundle.putString("searchName", searchText);  
			intent.putExtras(bundle);
			intent.setClass(MainActivity.this, Search.class);
			//ͨ��Intent������������һ��Activity
			MainActivity.this.startActivity(intent);

		}
    	
    }

}