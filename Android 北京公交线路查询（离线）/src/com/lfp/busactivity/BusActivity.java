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
    	//导入数据库
        dbfile = new ImportDBFile(this);

        dbfile.openDatabase();

        dbfile.closeDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText key = (EditText) this.findViewById(R.id.key);
        final EditText station = (EditText) this.findViewById(R.id.station);
        Button seach = (Button) this.findViewById(R.id.seach);
        final TextView result = (TextView) this.findViewById(R.id.result);
        

        //数据库服务类
		//busService = new BusService(this);
        
        database = SQLiteDatabase.openOrCreateDatabase(ImportDBFile.DB_PATH + "/" + ImportDBFile.DB_NAME, null);
        //线路输入监听,获得焦点时，清空站点
        key.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						station.setText("");
						return false;
					}
            });
        //搜索按钮监听
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
						while(cursor.moveToNext()){//迭代记录集
							bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
							lines.append(cursor.getString(1));
							lines.append("*");
							bus.setStation(cursor.getString(2));
						}
						bus.setLine(lines.toString());//所有线路连接在一起
					}else{
						bus = null;
					}
				}else if(busline != null && !busline.equals("")){
					cursor = database.rawQuery("select id,line,station from bus_line where line like '%"+busline+"%'", null);
					if(cursor.moveToNext()){//迭代记录集
						bus.setId(cursor.getInt(cursor.getColumnIndex("id")));
						lines.append(cursor.getString(1));
						lines.append("*");
						bus.setStation(cursor.getString(2));
						bus.setLine(lines.toString());//所有线路连接在一起
					}else{
						bus = null;
					}
					
				}else{
					cursor = null;
					bus = null;
				}
				
				
				if(bus == null){
					result.setText("没有您要找的信息");
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
    //当点击Menu键时会打开菜单，当菜单第一次被打开时，框架回掉该方法
    public boolean onCreateOptionsMenu(Menu menu){
    	//为菜单添加一个id为1，标题为“开始”的元素
    	menu.add(0, OK, 0, "帮助");
    	menu.add(0, CANCLE, 0, "联系我");
    	menu.add(0, ABOUT, 0, "退出");
    	//为菜单添加一个子菜单，id为3，标题为“关于”，并返回该子菜单对象为file
    	//Menu file = menu.addSubMenu(0, ABOUT, 0, "退出");
    	//得到一个MenuInflater
    	MenuInflater inflater = getMenuInflater();
    	//调用inflater的inflater方法，获取资源文件中定义的元素
    	//并将这些元素添加进制定的Menu——file
    	//inflater.inflate(R.menu.menu, file);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	//根据被选中的Item进行不同的处理
    	final TextView result = (TextView) this.findViewById(R.id.result);
    	switch(item.getItemId()){
    	case OK:
    		this.setTitle("北京公交地铁查询——帮助");
    		result.setText("刚刚接触android，写个北京公交地铁查询：\n1.公交线路查询，可以查询到该线路经过的站点。\n\n2.输入站点，可以查询到停靠此站的公交车。\n\n3.换乘功能以后逐步完善！");
    		return true;
    	case CANCLE:
    		this.setTitle("北京公交地铁查询——联系我");
    		result.setText("联系方式：\n email:bjlfp@163.com; \n\n欢迎大家交流！有数据错误的给我发邮件谢谢！");
    		return true;
    	case ABOUT:
    		this.setTitle("退出");
    		BusActivity.this.finish();
    		return true;
    	}
    	return false;
    }
}