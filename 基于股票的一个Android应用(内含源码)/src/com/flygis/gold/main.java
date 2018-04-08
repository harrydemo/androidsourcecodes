package com.flygis.gold;


import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flygis.gold.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
//import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class main extends Activity implements OnTouchListener{
	private DBhelp db;
	private static final int INSERT_ID_Quit = 0;
	private static final int INSERT_ID_Prev = 1;
	private static final int INSERT_ID_Next = 2;
	private static final int INSERT_ID_Update = 3;
	private static final int INSERT_ID_Config = 4;
	private static final String goldDir = "/mnt/sdcard/gold/";
	
    private ProgressBar downloadbar;
    //同时更新多个文件，进度叠加
    private static int iallmax=0;
    private static int icurpro=0;
    
	private ImageView img;
	private int fileSize = 0;
	private Map<Integer, String> PicData;
	private int iCur = 0;
	private int icount = 0;
	private static int ii=0;
	private Bitmap nopicbit;
	private EditText edt;
	private Button badd;
	private Button bCls;
	private Button bbck;
	
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int size = msg.getData().getInt("size");
				icurpro += size;
				//float result = (float)size/ (float)downloadbar.getMax();
				//int p = (int)(result*100);
				//Toast.makeText(main.this,p+"%", 1).show();
				downloadbar.setProgress(icurpro);
				//Toast.makeText(main.this,downloadbar.getProgress() + "//" + downloadbar.getMax(),1).show();
				if(downloadbar.getProgress()==iallmax){//downloadbar.getMax()){
					//downloadbar.setVisibility(4);
					downloadbar.setProgress(0);
					iallmax = 0;
					icurpro = 0;
					downloadbar.setMax(0);
				}
				break;
			case 2:				
				int isize = msg.getData().getInt("size");
				iallmax += isize;
				downloadbar.setMax(iallmax);
				//Toast.makeText(main.this,"文件大小："+isize+"字节", 1).show();
				break;
			case 3:
				String errostr = msg.getData().getString("info");
				String urlpath = msg.getData().getString("path");
				Toast.makeText(main.this,urlpath + " 更新错误：" + errostr, 1).show();
				break;
			case 4:
				String urlpath1 = msg.getData().getString("path");
				Toast.makeText(main.this, urlpath1 + " 更新完成", 1).show();
				break;
			case -1:
				break;
			}			
		}    	
    };
	
	private GestureDetector gd;	//用于捕捉touch的详细手势(gesture)
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);;
        HideStatusBar();  
        setContentView(R.layout.main); 
    	
        img = (ImageView)this.findViewById(R.id.img);
        img.setOnClickListener(new ImageView.OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			openOptionsMenu();
		}
		});
        
        gd = new GestureDetector (this, new GDetector()); //创建一个GestureDetector实例，下文重写其onFling()方法
    	//tv_log = (TextView) findViewById(R.id.tv_log);	//没啥特别，用于显示调试信息的TextView
        //img = (ImageView) findViewById(R.id.ImageView01); //我要用手指蹂躏的那张图片
        img.setOnTouchListener(this); //这里如果没有 implements OnTouchListener 会报错
        img.setLongClickable(true);	//不加这个滑动就没反应了     
        
        nopicbit = BitmapFactory.decodeResource(getResources(), R.drawable.wutu);
        downloadbar = (ProgressBar)this.findViewById(R.id.progress);
        PicData = new HashMap<Integer, String>();
        PicData.clear();
        
        ReadXML();
        icount = PicData.size();
    	try {
    		ShowPic();
		} catch (Exception e) {
			Toast.makeText(main.this, "没有图片", 1).show();
			//throw new RuntimeException("没有图片");
		}
    }
    
	// 创建OptionsMenu publi c bool ean onCreateOpti onsMenu(Menu menu)
	// 处理选择事件publi c bool ean onOpti onsIt emSel ect ed(MenuIt em i t em)
	public boolean onCreateOptionsMenu(Menu menu) {			
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID_Quit, 0, "退出");
		menu.add(0, INSERT_ID_Prev, 0, "上一个");
		menu.add(0, INSERT_ID_Next, 0, "下一个");
		menu.add(0, INSERT_ID_Update, 0, "更新");
		//menu.add(0, INSERT_ID_Config, 0, "配置");		
		return result;
	}// 创建菜单
	
	public boolean onTouch(View v, MotionEvent event) {	//把touch的event传递给gestureListener处理
		return gd.onTouchEvent(event);
	}


	class GDetector extends SimpleOnGestureListener {	//GDetector 名字是随便起的	
	@Override
	public boolean onFling(	MotionEvent e1,  	//按下时的状态,位置
						MotionEvent e2, 	//松手时的状态，位置
						float vx,			//x坐标的移动速度，单位: px/秒
						float vy){			//y坐标的移动速度
		if((e1.getX()-e2.getX()>200) && (Math.abs(vx)>50)) {	//滑动速度足够快至少50点/秒，手指起落点减起点是正值且>200 判断属于向左滑动
			//tv_log.setText("Fling to Left.\n"+tv_log.getText());	//debug.输出到 tv_log
			//iv.setImageResouce(...);	//向左划的时候如何怎样切换图片..略..
			if (iCur < icount - 1){
            	iCur ++;
            	ShowPic();
			}
        	else{
        		Toast.makeText(main.this, "已经最后一个了", 1).show();
        	}
		}else if ((e2.getX()-e1.getX()>200) && (Math.abs(vx)>50)) { //同理判断是向右滑动
			//tv_log.setText("Fling to Right.\n"+tv_log.getText()); 	//debug
        	if (iCur > 0){
            	iCur --;
            	String imgname = PicData.get(iCur);
            	imgname = imgname.substring(imgname.lastIndexOf('/') + 1);      
				File f=new File(goldDir + imgname);     
		        if(f.exists())   
		        {  
		        	Bitmap bit = BitmapFactory.decodeFile(goldDir + imgname);			        	
					img.setImageBitmap(bit);
				}else{
					img.setImageBitmap(nopicbit);				
				}
			}
        	else{
        		Toast.makeText(main.this, "已经第一个了", 1).show();
        	}
		}
		return true;
	}
	//随着我指尖在屏幕上的游走不断地输出onScroll 的信息，
	//最后onScroll() 和 onFling() 我都用上了， Scroll 的时候随手指的移动切换，当手指松开之后用Fling实现惯性的切换，这样就显得相当的自然了 :-)
	public boolean onScroll(		MotionEvent e1,  	//按下时的状态,位置
			MotionEvent e2, 	//松手时的状态，位置
			float dx,			//注意！这里不是速度，是距离了！
			float dy){			//y的距离，单位px
		return true;
		//tv_log.setText("moved"+(e1.getX()-e2.getX())+" distanceX "+ dx + ".\n"+tv_log.getText());	//debug
	}
	
	}
	
	private void UpdatePic() {
		if( !isNetworkAvailable( this) ){
			Toast.makeText(main.this, "没有网络连接可用", 1).show();
			return;
		}
		if (icount<=0){
			Toast.makeText(main.this, "Config Error", 1).show();
			return;
		}
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File dir1 = Environment.getExternalStorageDirectory();//文件保存目录
			File dir = new File(dir1.getAbsolutePath()+ "/gold");
//			for(Map.Entry<Integer, String> entry : PicData.entrySet()){
//				download(entry.getValue(), dir);
//				Toast.makeText(main.this, entry.getValue() + "下载完成", 1).show();
//			}
			download(PicData.get(iCur),dir);
		}
		else{
			Toast.makeText(main.this, "SD Card 错误", 1).show();
		}
	}	

	private void ConfigPic() {
		/* 将layout 改成mylayout.xml */
		setContentView(R.layout.configl);
		if(ii < 100){
			return;			
		}
		/* 以findViewById()取得Button 对象，并添加onClickListener */
		edt = (EditText) findViewById(R.id.text2);
		badd = (Button) findViewById(R.id.button2);		
		badd.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			//SQLiteDatabase sqldb = db.getWritableDatabase();
			//db.AddRec(sqldb, mEditText01.getText().toString());
			PicData.put(ii,edt.getText().toString() );
			ii++;
		}
		});
		
		bCls = (Button) findViewById(R.id.btncls);
		bCls.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//SQLiteDatabase sqldb = db.getWritableDatabase();
				//db.delall(sqldb);
				PicData.clear();
				ii = 0;
		}
		});
		
		bbck = (Button) findViewById(R.id.btnbck);		
		bbck.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//db.close();
				setContentView(R.layout.main);
		}
		});
	}	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
        {
            case INSERT_ID_Quit:
            	System.exit(0);	
                break;
            case INSERT_ID_Prev:
            	if (iCur > 0){
	            	iCur --;
	            	ShowPic();
				}
            	else{
            		Toast.makeText(main.this, "已经第一个了", 1).show();
            	}
                break;
            case INSERT_ID_Next:
            	if (iCur < icount - 1){
	            	iCur ++;
	            	ShowPic();
				}
            	else{
            		Toast.makeText(main.this, "已经最后一个了", 1).show();
            	}
                break;
            case INSERT_ID_Update:
            	UpdatePic();
                break;
            case INSERT_ID_Config:
            	//Toast.makeText(main.this, "还没有", 1).show();
            	//ConfigPic();
            	ReadXML();
                break;            	
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
	}
    
    private void HideStatusBar() {
    	//隐藏标题
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	//定义全屏参数
    	int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
    	//获得窗口对象
    	Window myWindow=this.getWindow();
    	//设置Flag标识
    	myWindow.setFlags(flag,flag);
    }    
	/**
	 * 获取文件名
	 */
	private String getFileName(String path,HttpURLConnection conn) {
		String filename = path.substring(path.lastIndexOf('/') + 1);
		if(filename==null || "".equals(filename.trim())){//如果获取不到文件名称
			for (int i = 0;; i++) {
				String mine = conn.getHeaderField(i);
				if (mine == null) break;
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
					if(m.find()) return m.group(1);
				}
			}
			filename = UUID.randomUUID()+ ".tmp";//默认取一个文件名
		}
		return filename;
	}
	
    private void download(final String path, final File dir){
    	new Thread(new Runnable() {
			private File saveFile;

			@Override
			public void run() {
				if(!dir.exists()) dir.mkdirs();
		    	HttpURLConnection conn = null;
				try {
					URL url = new URL(path);			
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5*1000);
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
					conn.setRequestProperty("Accept-Language", "zh-CN");
					conn.setRequestProperty("Referer", path); 
					conn.setRequestProperty("Charset", "UTF-8");
					conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.connect();
					
					if (conn.getResponseCode()==200) {
						fileSize = conn.getContentLength();//根据响应获取文件大小
						if (fileSize <= 0) throw new RuntimeException("Unkown file size ");
						//得到大小，设置进度条
						Message msg = new Message();
						msg.what = 2;
						msg.getData().putInt("size", fileSize);							
						handler.sendMessage(msg);			
						String filename = getFileName(path,conn);
						this.saveFile = new File(dir, filename);/* 保存文件 */
					}			
				} catch (Exception e) {
					//throw new RuntimeException("连接失败");
					//下载错误
					Message msg = new Message();
					msg.what = 3;
					msg.getData().putString("info", "连接失败");	
					msg.getData().putString("path", path);
					handler.sendMessage(msg);	
					throw new RuntimeException();
				} 		
				int downLength=0;
				try {
					InputStream inStream = conn.getInputStream();
					byte[] buffer = new byte[2048];
					int offset = 0;
					RandomAccessFile threadfile = new RandomAccessFile(saveFile, "rwd");
					threadfile.seek(0);
					while ((offset = inStream.read(buffer, 0, 2048)) != -1) {
						threadfile.write(buffer, 0, offset);
						downLength += offset;
						//更新进度
						Message msg = new Message();
						msg.what = 1;
						msg.getData().putInt("size", downLength);
						msg.getData().putString("path", path);
						handler.sendMessage(msg);
					}
					//下载完成
					Message msg = new Message();
					msg.what = 4;
					msg.getData().putString("path", path);
					handler.sendMessage(msg);					
					threadfile.close();
					inStream.close();			
				} catch (Exception e) {
					//throw new RuntimeException("下载失败");
					//下载错误
					Message msg = new Message();
					msg.what = 3;
					msg.getData().putString("info", "下载失败");
					msg.getData().putString("path", path);
					handler.sendMessage(msg);	
					throw new RuntimeException();
				}
			}
    	}).start();
    }
    
    public static boolean isNetworkAvailable( Activity mActivity ) { 
        Context context = mActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {    
          return false;
        } else {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();    
            if (info != null) {        
                for (int i = 0; i<info.length; i++) {           
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {              
                        return true; 
                    }        
                }     
            } 
        }   
        return false;
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		//按下键盘上返回按钮
		if(keyCode == KeyEvent.KEYCODE_BACK){
			System.exit(0);		
			return true;
		}else{		
			return super.onKeyDown(keyCode, event);
		}
	} 
    
    private void ShowPic()
    {
    	String imgname = PicData.get(iCur);
    	imgname = imgname.substring(imgname.lastIndexOf('/') + 1);
		File f=new File(goldDir + imgname);     
        if(f.exists())   
        {  
        	Bitmap bit = BitmapFactory.decodeFile(goldDir + imgname);			        	
			img.setImageBitmap(bit);
		}else{
			img.setImageBitmap(nopicbit);				
		}
    }
    
	private void ReadXML()
    {
		DocumentBuilderFactory docBuilderFactory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			File f=new File(goldDir + "goldcfg.xml");     
	        if(!f.exists()){
	        	Toast.makeText(main.this, "没有配置文件", 1).show();
	        	return;
	        }
			doc = docBuilder.parse(f);
			// root element
			Element root = doc.getDocumentElement();
			// Do something here
			// get a NodeList by tagname
			NodeList nodeList = root.getElementsByTagName("item");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nd = nodeList.item(i);
				//System.out.print();
				PicData.put(i,nd.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
			Toast.makeText(main.this, e.getMessage(), 1).show();
		} finally {
			doc = null;
			docBuilder = null;
			docBuilderFactory = null;
		}
	}

}
