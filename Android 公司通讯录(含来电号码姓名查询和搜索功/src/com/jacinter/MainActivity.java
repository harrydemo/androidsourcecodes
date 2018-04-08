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

//程序的主界面，主要用来根据部门显示姓名
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
        //启动电话号码监听服务
        //startService(new Intent(this, CallShowService.class));
        startService(new Intent(this, FloatingWindowService.class));
        
        searchButton=(Button)findViewById(R.id.button1);
        searchBox=(EditText)findViewById(R.id.editText1);
        searchButton.setOnClickListener(new ButtonListener());
        //为一级条目提供数据                   
        //Groups  分组,其中定义了17个组，可以直接在这里增加组的名字，功能已测试
        String[] str={"公司领导","综合管理部","财务部","销售一部","销售二部","销售三部","客户服务部","物流部","技术部","市场部","总经办","客户管理部","生产一部","生产二部","生产三部","制造一部","就餐中心"};
        
 

        List<Map<String,Object>> Groups = new ArrayList<Map<String,Object>>();
        for(int i=0;i<str.length;i++){
        	Map<String,Object> group = new HashMap<String,Object>();
        	group.put("groupname", str[i]);
        	Groups.add(group);
        }
        

        
        //定义两个List<Map<String,Object>>  child1和child2
        //为二级条目提供数据
        //child1
        List<List<Map<String,Object>>> Childs = new ArrayList<List<Map<String,Object>>>();

        for (int j=0;j<str.length;j++){
        	List<Map<String,Object>> child = new ArrayList<Map<String,Object>>(); 
        	//下面的1为数据库的版本号，下次更新时，只要数据库号大于1就可以
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
         * 使用SimpleExpandableListAdapter显示ExpandableListView
         * 参数1.上下文对象Context
         * 参数2.一级条目目录集合
         * 参数3.一级条目对应的布局文件
         * 参数4.fromto，就是map中的key，指定要显示的对象
         * 参数5.与参数4对应，指定要显示在groups中的id
         * 参数6.二级条目目录集合
         * 参数7.二级条目对应的布局文件
         * 参数8.fromto，就是map中的key，指定要显示的对象
         * 参数9.与参数8对应，指定要显示在childs中的id
         */

        SimpleExpandableListAdapter simpleExpandListAdapter = new SimpleExpandableListAdapter(MainActivity.this, 
        		Groups, R.layout.group, new String[]{"groupname"},new int[]{R.id.group},
        		Childs, R.layout.child, new String[]{"UserName","Telephone"},new int[]{R.id.id,R.id.name});
        
        setListAdapter(simpleExpandListAdapter);

    }
    class ButtonListener implements OnClickListener{
    	//生成该类的对象，并将其注册到控件上。如果该控件被用户按下，就会执行onClick方法 
		public void onClick(View v) {
			//生成一个Intent对象
	        searchText=searchBox.getText().toString();

			Intent intent = new Intent();
			//设置Intent对象要启动的Activity
			Bundle bundle = new Bundle();  
			bundle.putString("searchName", searchText);  
			intent.putExtras(bundle);
			intent.setClass(MainActivity.this, Search.class);
			//通过Intent对象启动另外一个Activity
			MainActivity.this.startActivity(intent);

		}
    	
    }

}