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
    //ͬʱ���¶���ļ������ȵ���
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
				//Toast.makeText(main.this,"�ļ���С��"+isize+"�ֽ�", 1).show();
				break;
			case 3:
				String errostr = msg.getData().getString("info");
				String urlpath = msg.getData().getString("path");
				Toast.makeText(main.this,urlpath + " ���´���" + errostr, 1).show();
				break;
			case 4:
				String urlpath1 = msg.getData().getString("path");
				Toast.makeText(main.this, urlpath1 + " �������", 1).show();
				break;
			case -1:
				break;
			}			
		}    	
    };
	
	private GestureDetector gd;	//���ڲ�׽touch����ϸ����(gesture)
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
        
        gd = new GestureDetector (this, new GDetector()); //����һ��GestureDetectorʵ����������д��onFling()����
    	//tv_log = (TextView) findViewById(R.id.tv_log);	//ûɶ�ر�������ʾ������Ϣ��TextView
        //img = (ImageView) findViewById(R.id.ImageView01); //��Ҫ����ָ���������ͼƬ
        img.setOnTouchListener(this); //�������û�� implements OnTouchListener �ᱨ��
        img.setLongClickable(true);	//�������������û��Ӧ��     
        
        nopicbit = BitmapFactory.decodeResource(getResources(), R.drawable.wutu);
        downloadbar = (ProgressBar)this.findViewById(R.id.progress);
        PicData = new HashMap<Integer, String>();
        PicData.clear();
        
        ReadXML();
        icount = PicData.size();
    	try {
    		ShowPic();
		} catch (Exception e) {
			Toast.makeText(main.this, "û��ͼƬ", 1).show();
			//throw new RuntimeException("û��ͼƬ");
		}
    }
    
	// ����OptionsMenu publi c bool ean onCreateOpti onsMenu(Menu menu)
	// ����ѡ���¼�publi c bool ean onOpti onsIt emSel ect ed(MenuIt em i t em)
	public boolean onCreateOptionsMenu(Menu menu) {			
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID_Quit, 0, "�˳�");
		menu.add(0, INSERT_ID_Prev, 0, "��һ��");
		menu.add(0, INSERT_ID_Next, 0, "��һ��");
		menu.add(0, INSERT_ID_Update, 0, "����");
		//menu.add(0, INSERT_ID_Config, 0, "����");		
		return result;
	}// �����˵�
	
	public boolean onTouch(View v, MotionEvent event) {	//��touch��event���ݸ�gestureListener����
		return gd.onTouchEvent(event);
	}


	class GDetector extends SimpleOnGestureListener {	//GDetector ������������	
	@Override
	public boolean onFling(	MotionEvent e1,  	//����ʱ��״̬,λ��
						MotionEvent e2, 	//����ʱ��״̬��λ��
						float vx,			//x������ƶ��ٶȣ���λ: px/��
						float vy){			//y������ƶ��ٶ�
		if((e1.getX()-e2.getX()>200) && (Math.abs(vx)>50)) {	//�����ٶ��㹻������50��/�룬��ָ�������������ֵ��>200 �ж��������󻬶�
			//tv_log.setText("Fling to Left.\n"+tv_log.getText());	//debug.����� tv_log
			//iv.setImageResouce(...);	//���󻮵�ʱ����������л�ͼƬ..��..
			if (iCur < icount - 1){
            	iCur ++;
            	ShowPic();
			}
        	else{
        		Toast.makeText(main.this, "�Ѿ����һ����", 1).show();
        	}
		}else if ((e2.getX()-e1.getX()>200) && (Math.abs(vx)>50)) { //ͬ���ж������һ���
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
        		Toast.makeText(main.this, "�Ѿ���һ����", 1).show();
        	}
		}
		return true;
	}
	//������ָ������Ļ�ϵ����߲��ϵ����onScroll ����Ϣ��
	//���onScroll() �� onFling() �Ҷ������ˣ� Scroll ��ʱ������ָ���ƶ��л�������ָ�ɿ�֮����Flingʵ�ֹ��Ե��л����������Ե��൱����Ȼ�� :-)
	public boolean onScroll(		MotionEvent e1,  	//����ʱ��״̬,λ��
			MotionEvent e2, 	//����ʱ��״̬��λ��
			float dx,			//ע�⣡���ﲻ���ٶȣ��Ǿ����ˣ�
			float dy){			//y�ľ��룬��λpx
		return true;
		//tv_log.setText("moved"+(e1.getX()-e2.getX())+" distanceX "+ dx + ".\n"+tv_log.getText());	//debug
	}
	
	}
	
	private void UpdatePic() {
		if( !isNetworkAvailable( this) ){
			Toast.makeText(main.this, "û���������ӿ���", 1).show();
			return;
		}
		if (icount<=0){
			Toast.makeText(main.this, "Config Error", 1).show();
			return;
		}
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File dir1 = Environment.getExternalStorageDirectory();//�ļ�����Ŀ¼
			File dir = new File(dir1.getAbsolutePath()+ "/gold");
//			for(Map.Entry<Integer, String> entry : PicData.entrySet()){
//				download(entry.getValue(), dir);
//				Toast.makeText(main.this, entry.getValue() + "�������", 1).show();
//			}
			download(PicData.get(iCur),dir);
		}
		else{
			Toast.makeText(main.this, "SD Card ����", 1).show();
		}
	}	

	private void ConfigPic() {
		/* ��layout �ĳ�mylayout.xml */
		setContentView(R.layout.configl);
		if(ii < 100){
			return;			
		}
		/* ��findViewById()ȡ��Button ���󣬲����onClickListener */
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
            		Toast.makeText(main.this, "�Ѿ���һ����", 1).show();
            	}
                break;
            case INSERT_ID_Next:
            	if (iCur < icount - 1){
	            	iCur ++;
	            	ShowPic();
				}
            	else{
            		Toast.makeText(main.this, "�Ѿ����һ����", 1).show();
            	}
                break;
            case INSERT_ID_Update:
            	UpdatePic();
                break;
            case INSERT_ID_Config:
            	//Toast.makeText(main.this, "��û��", 1).show();
            	//ConfigPic();
            	ReadXML();
                break;            	
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
	}
    
    private void HideStatusBar() {
    	//���ر���
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	//����ȫ������
    	int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
    	//��ô��ڶ���
    	Window myWindow=this.getWindow();
    	//����Flag��ʶ
    	myWindow.setFlags(flag,flag);
    }    
	/**
	 * ��ȡ�ļ���
	 */
	private String getFileName(String path,HttpURLConnection conn) {
		String filename = path.substring(path.lastIndexOf('/') + 1);
		if(filename==null || "".equals(filename.trim())){//�����ȡ�����ļ�����
			for (int i = 0;; i++) {
				String mine = conn.getHeaderField(i);
				if (mine == null) break;
				if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())){
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
					if(m.find()) return m.group(1);
				}
			}
			filename = UUID.randomUUID()+ ".tmp";//Ĭ��ȡһ���ļ���
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
						fileSize = conn.getContentLength();//������Ӧ��ȡ�ļ���С
						if (fileSize <= 0) throw new RuntimeException("Unkown file size ");
						//�õ���С�����ý�����
						Message msg = new Message();
						msg.what = 2;
						msg.getData().putInt("size", fileSize);							
						handler.sendMessage(msg);			
						String filename = getFileName(path,conn);
						this.saveFile = new File(dir, filename);/* �����ļ� */
					}			
				} catch (Exception e) {
					//throw new RuntimeException("����ʧ��");
					//���ش���
					Message msg = new Message();
					msg.what = 3;
					msg.getData().putString("info", "����ʧ��");	
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
						//���½���
						Message msg = new Message();
						msg.what = 1;
						msg.getData().putInt("size", downLength);
						msg.getData().putString("path", path);
						handler.sendMessage(msg);
					}
					//�������
					Message msg = new Message();
					msg.what = 4;
					msg.getData().putString("path", path);
					handler.sendMessage(msg);					
					threadfile.close();
					inStream.close();			
				} catch (Exception e) {
					//throw new RuntimeException("����ʧ��");
					//���ش���
					Message msg = new Message();
					msg.what = 3;
					msg.getData().putString("info", "����ʧ��");
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
		//���¼����Ϸ��ذ�ť
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
	        	Toast.makeText(main.this, "û�������ļ�", 1).show();
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
