package cn.android.browser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.android.browser.HelpTabAct;
import cn.android.browser.HistoryBean;
import cn.android.browser.HttpData;
import cn.android.browser.SQLiteHelper;
import cn.android.browser.WriteFavoriteXml;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity 
{
	private WebView mWebView = null;    
    final Activity context = this;   
    private SQLiteHelper mOpenHelper; 
    public static Cursor myCursor_one;
    Intent directCall;
    private WriteFavoriteXml writeXml = new WriteFavoriteXml();
    private ImageButton btn = null;
    private EditText edit = null;
    private ImageButton forwardBtn = null;
    private ImageButton backBtn = null;
    private ListView list = null;
    private Button go_back = null;
    private ImageButton menuBtn = null;
    
    private final static int HISTORY_ITEM = 0;	//历史记录
	private final static int HTTP_ITEM = 1;	//关于
	private final static int SHORTCUT_ITEM = 2;	//快捷方式
	private final static int ADD_FAVORITE = 3;	//加入收藏夹
	private final static int FAVORITE_ITEM = 4;	//收藏夹
	private final static int PREFERENCE_ITEM = 5;		//帮助网页
	private final static int EXIT_ITEM = 7;		//退出
	
	private String cur_url = "http://m.hao123.com";
	private final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	List<Map<String, Object>> history_data = new ArrayList<Map<String, Object>>();
	List<HistoryBean> xml_data = new ArrayList<HistoryBean>();	
	String[] dialog_data = new String[]{};
	public int selectId = 0;
	
	SharedPreferences sp;
	Drawable drawable;
	
	private static String SAVE_KEY = "save-view";
	
	public static Main instance;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.w("debug.onCreate","onCreate");
	        requestWindowFeature(Window.FEATURE_PROGRESS);//让进度条显示在标题栏上 
	        
	        mOpenHelper = new SQLiteHelper(this);
	        directCall = new Intent(Intent.ACTION_MAIN);//快捷方式
	        onInit();	
	        instance = this;	        
	        
		    if (savedInstanceState == null) 		        
		    {
		    	deleteTable();
	        } 
	        else 
	        {
	            Bundle map = savedInstanceState.getBundle(SAVE_KEY);
	            if (map != null) 
	            {
	                restoreState(map);
	            }
	        }
	 }
	
    private void onInit() {
        setContentView(R.layout.main);
        	
        	edit = (EditText)findViewById(R.id.edit_1);

        	mWebView = (WebView) findViewById(R.id.wv1); 

        	btn = (ImageButton)findViewById(R.id.button_1);

        	forwardBtn = (ImageButton)findViewById(R.id.forward_btn);

        	backBtn = (ImageButton)findViewById(R.id.back_btn);

        	menuBtn = (ImageButton)findViewById(R.id.menu_btn);

        btn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	String str = edit.getText().toString();
            	if(str != "")
            	{
            		cur_url = str;
            		setTitle();
            		mWebView.loadUrl(str); 
            	}
            }
        } );
        
        forwardBtn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	if(mWebView.canGoForward())
            		mWebView.goForward();
            }
        } );
        backBtn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	if(mWebView.canGoBack())
            		mWebView.goBack();
            }
        } );
        menuBtn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
            	context.openOptionsMenu();
            }
        } );
        
        mWebView.setWebViewClient(new WebViewClient(){     
        	public boolean shouldOverrideUrlLoading(WebView  view, String url) {     
        		mWebView.loadUrl(url);  
        		cur_url = url;
        		setTitle();
        		//insertTable(url,1,mWebView.getTitle());
        		return true;     
        	}     
        	}); 
        
        mWebView.setWebChromeClient(new WebChromeClient() {   
            public void onProgressChanged(WebView view, int progress) {   
              //Activity和Webview根据加载程度决定进度条的进度大小   
             //当加载到100%的时候 进度条自动消失   
              context.setProgress(progress * 100); 
              if(progress>=100)
              {
            	  insertTable(cur_url,1,view.getTitle());
              }
              //Log.d("TTTTTTTTT",progress+","+view.getTitle());
            }           
        	});   
        
        	mWebView.loadUrl(cur_url);        
        	setTitle();

        Log.i("debug.Init",cur_url);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
    	 Log.i("onCreateContextMenu",v.toString());
    	 Log.i("onCreateContextMenu",String.valueOf(v.getId()));
    }   

    
    private void setTitle()
    {
    	Bitmap bitmap = mWebView.getFavicon();
        drawable = new BitmapDrawable(bitmap);     
        //edit.setCompoundDrawables(drawable, null, null, null);
        drawable = this.getResources().getDrawable(R.drawable.history);
        edit.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);  
        edit.setText(cur_url);
        //edit.setMaxLines(1);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, HISTORY_ITEM, HISTORY_ITEM, R.string.history).setIcon(R.drawable.history); //setIcon,setText
    	menu.add(0, HTTP_ITEM, HTTP_ITEM, R.string.http_name).setIcon(R.drawable.about);
    	menu.add(0, SHORTCUT_ITEM, SHORTCUT_ITEM, R.string.shortcut).setIcon(R.drawable.icon);
    	menu.add(0, ADD_FAVORITE, ADD_FAVORITE, R.string.addFavorite).setIcon(R.drawable.add_favorite);
    	menu.add(0, FAVORITE_ITEM, FAVORITE_ITEM, R.string.favorite).setIcon(R.drawable.favorite);
    	menu.add(1, PREFERENCE_ITEM, PREFERENCE_ITEM, R.string.preference).setIcon(R.drawable.help);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId()){
    		case HISTORY_ITEM:
    			goto_history_view();
    			break;
    		case HTTP_ITEM:
    			showDialog(HTTP_ITEM);
    			break;
    		case SHORTCUT_ITEM:
    			createShortcut();
    			break;
    		case ADD_FAVORITE:
    			add_favorite();
    			break;
    		case FAVORITE_ITEM:
    			open_favorite();
    			break;
    		case PREFERENCE_ITEM:
    			goto_help_act();
    			break;
    		default:
    			break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    private void goto_history_view()
    {
    	getHistory();
    	
    	setContentView(R.layout.history);
    	list = (ListView)findViewById(R.id.list);
        go_back = (Button)findViewById(R.id.go_back);
        
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_2, new String[] {"网页","网址"},
                new int[] { android.R.id.text1 , android.R.id.text2});

//    	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                		android.R.layout.simple_list_item_1, history_data);
        
    	list.setAdapter(adapter);
    	go_back.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	onInit();
            }
        } );
    	list.setOnItemClickListener(new OnItemClickListener() 
    	{
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
                cur_url = history_data.get(position).get("网址").toString();
                onInit();
            }
        });
    }
    public void copyHistoryData(WebBackForwardList mylist)
    {
    	int i;  	
        for (i=0;i<mylist.getSize();i++)
        {
        	Map<String, Object> item = new HashMap<String, Object>();
        	item.put("网页", mylist.getItemAtIndex(i).getTitle());
            item.put("网址", mylist.getItemAtIndex(i).getUrl());
            history_data.add(item);

            //history_data.add(mylist.getItemAtIndex(i).getUrl().toString()); //查看浏览器历史
        }
    }
    private List<Map<String, Object>> getData()
    { 
    	return history_data; 
    } 
    
    protected Dialog onCreateDialog(int id) //只在第一次创建时调用
	{	
		if(id == FAVORITE_ITEM)
		{
    		return new AlertDialog.Builder(Main.this)
            .setTitle(R.string.fav_name)
             .setSingleChoiceItems(dialog_data, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	selectId = whichButton;
                    }
                })
            .setNeutralButton(R.string.open_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	if(selectId>=0)
                    	{
                    		cur_url = xml_data.get(selectId).getURL();
                    		edit.setText(cur_url);
                    		mWebView.loadUrl(cur_url);
                    	}
                    }
                })
            .setPositiveButton(R.string.del_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	if(selectId>=0)
                    	writeXml.Write(context,"history.xml",writeXml.deleteElement(dialog_data[selectId]));
                		selectId = 0;
                		removeDialog(FAVORITE_ITEM);
                    }
                })
            .setNegativeButton(R.string.close_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	removeDialog(FAVORITE_ITEM);
                    }
                })
            .create();
		}
		if(id == HTTP_ITEM)
		{
            return new AlertDialog.Builder(Main.this)
                .setTitle(R.string.http_name)
                .setItems(R.array.http_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();   
                            intent.setClass(Main.this, HttpData.class);   
                            Bundle b = new Bundle();   
                            b.putInt("id", which+1);
                            b.putString("url", cur_url);
                            intent.putExtras(b);   
                            startActivity(intent);   
                      
                    }
                })
                .create();
		}
		
		if(id == EXIT_ITEM)
		{
	            return new AlertDialog.Builder(Main.this)
	                .setIcon(R.drawable.icon)
	                .setTitle(R.string.exit_title)
	                .setMessage(R.string.exit_message)
	                .setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	finish();
	                    }
	                })
	                .setNegativeButton(R.string.no_btn, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	
	                    }
	                })
	                .create();
		}
		return null;
	}
    
    /* 往表中插入数据 */
	private void insertTable(String url, int time, String title) 
	{
		time = (int)Math.floor(System.currentTimeMillis() / 1000 );
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		myCursor_one = db.rawQuery("SELECT * FROM "+SQLiteHelper.TB_NAME + " where name=?" , new String[]{String.valueOf(title)});
		String sql;
		String tip;
		if (myCursor_one.moveToFirst()) 
		{
			sql = "update " + SQLiteHelper.TB_NAME 
			+ " set " + HistoryBean.TIME + "=" + time + " where " + HistoryBean.NAME + "='" + title + "'";
			Log.i("update",title);
			tip = "更新";
		}
		else
		{
		 sql = "insert into " + SQLiteHelper.TB_NAME 
			+ " (" + HistoryBean.URL + ", " + HistoryBean.TIME + ", " + HistoryBean.NAME + ") " 
			+ "values('"+url+"','"+time+"','"+title+"');";
		 	Log.i("insert",title);
		 	tip = "插入";
		}
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast.makeText(Main.this, tip+"记录出错", Toast.LENGTH_LONG).show();
			return;
		}
				
		Toast.makeText(Main.this, tip+"了记录", Toast.LENGTH_LONG).show();
	}
	/* 删除过时的历史记录 */
	private void deleteTable() 
	{
		int time = (int)Math.floor(System.currentTimeMillis() / 1000 ) - 86400;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String sql = "delete from " + SQLiteHelper.TB_NAME 
			+ " where "+ time + ">" + HistoryBean.TIME;
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast.makeText(Main.this, "删除记录出错", Toast.LENGTH_LONG).show();
		}	
	}
	
	private void getHistory() 
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		myCursor_one = db.rawQuery("SELECT * FROM "+SQLiteHelper.TB_NAME, null);
		int url = myCursor_one.getColumnIndex(HistoryBean.URL);
		int name = myCursor_one.getColumnIndex(HistoryBean.NAME);
		history_data.clear();
		if (myCursor_one.moveToFirst()) 
		{
			do {
				Map<String, Object> item = new HashMap<String, Object>();
	        	item.put("网页", myCursor_one.getString(name));
	            item.put("网址", myCursor_one.getString(url));
	            history_data.add(item);
				//history_data.add(myCursor_one.getString(url));
			} while (myCursor_one.moveToNext());
		}
		myCursor_one.close();  	
	}
	
	private void createShortcut()
	{
		Intent addShortcut = new Intent(ACTION_ADD_SHORTCUT);  
    	String numToDial = null;  
    	Parcelable icon = null;  
    	
    	numToDial = "MyBrowser";  
		icon = Intent.ShortcutIconResource.fromContext(  
				this,R.drawable.icon);  
		
		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,  numToDial);   
		
		directCall.addCategory(Intent.CATEGORY_LAUNCHER);        
		directCall.setComponent(new ComponentName(this.getPackageName(),     
				this.getPackageName()+".Main")); 		
		
    	addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,  directCall);  
    	addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);  
    	sendBroadcast(addShortcut); 
	}
	
	private void add_favorite()
	{
		String name = mWebView.getTitle();
    	String url = mWebView.getUrl();
    	if(name!=""&&url!="")
    	{     
    		writeXml.Write(context,"history.xml",writeXml.insertElement(name,url));
    		writeXml.onReadXml();
    		dialog_data = writeXml.getDialogData();
    		xml_data = writeXml.getXmlData();
    		showDialog(FAVORITE_ITEM);
    	}
	}
	
	private void open_favorite()
	{
		writeXml.onReadXml();
		dialog_data = writeXml.getDialogData();
		xml_data = writeXml.getXmlData();
		showDialog(FAVORITE_ITEM);
	}
	
	private void goto_help_act()
	{
		Intent intent = new Intent();
		intent.setClass(context, HelpTabAct.class);
		startActivity(intent);
	}
	
	public void setBlockImage(boolean flag)
	{
		Log.e("setBlockImage",flag==true?"true":"false");
		WebSettings webSettings = mWebView.getSettings(); 
		webSettings.setBlockNetworkImage(flag);  
	}	
	public void setCacheMode(boolean flag)
	{
		Log.e("setCacheMode",flag==true?"true":"false");
		WebSettings webSettings = mWebView.getSettings(); 
		if(flag)
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		else
			webSettings.setCacheMode(webSettings.LOAD_NO_CACHE);
	}
	public void setJavaScript(boolean flag)
	{
		Log.e("setJavaScript",flag==true?"true":"false");
		WebSettings webSettings = mWebView.getSettings(); 
		webSettings.setJavaScriptEnabled(flag);  
	}
	
	@Override protected void onResume() {
        super.onResume();
        Log.w("debug.onResume","onResume");
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
    	outState.putBundle(SAVE_KEY, saveState());
    	Log.w("debug.onSaveInstanceState","onSaveInstanceState");
    }
    @Override protected void onPause() {
        super.onPause();
        Log.w("debug.onPause","onPause");
    }	
    @Override protected void onStart() {
        super.onStart();
        Log.w("debug.onStart","onStart");
    }
    @Override protected void onRestart() {
        super.onRestart();
        Log.w("debug.onRestart","onRestart");
    }
    @Override protected void onStop() {
        super.onStop();
        Log.w("debug.onStop","onStop");
    }
    @Override protected void onDestroy() {
    	showDialog(EXIT_ITEM);
        //super.onDestroy();
        Log.w("debug.onDestroy","onDestroy");
    } 
    
    public void restoreState(Bundle icicle) 
    {
    	cur_url = icicle.getString("URL");
    	mWebView.loadUrl(cur_url);
    	setTitle();
    }
    
    public Bundle saveState()
    {
    	Bundle map = new Bundle();
    	map.putString("URL", cur_url);
        return map;
    }
    
    @Override  
    public void onBackPressed() 
    {  
        dialog();  
    } 
    protected void dialog() 
    {  
        AlertDialog.Builder builder = new Builder(Main.this);  
        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.exit_title);
        builder.setMessage(R.string.exit_message);
        builder.setPositiveButton(R.string.ok_btn,  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                android.os.Process.killProcess(android.os.Process.myPid());  
            }  
        });  
        builder.setNegativeButton(R.string.no_btn,  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder.create().show();  
    }  
	
}
