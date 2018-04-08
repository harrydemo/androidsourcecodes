/**
 * 程序名称: 熊猫文件管理
 * 版本: 1.5
 * ---------------------------------------------------------------------------------------
 * root 权限
 * 	重新写了复制的代码.system,根目录的读写装载,,添加根目录写
 **/
package com.xjf.filedialog;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjf.utils.Common;
import com.android.xjf.utils.LinuxFileCommand;
import com.android.xjf.utils.LinuxShell;
import com.android.xjf.utils.XDialog;
import com.xjf.filedialog.DDListView.DropListener;
import com.xjf.filedialog.DDListView.DropOutListener;
import com.xjf.filedialog.DDListView.StartDragListener;
import com.xjf.filedialog.FileListAdapter.FileInfo;

/**
 * 文件管理工具.
 * */
public class FileManager extends Activity {
	//显示文件View
	DDListView fileViewList;
	DDGridView fileViewGrid;
	AbsListView fileView;
	
	FileListAdapter fileAdapterList;
	FileGridAdapter fileAdapterGrid;
	FileListAdapter fileAdapter;
	/** 路径栏*/
	HorizontalListView pathScroll;
	LinearLayout	pathLayout;
	
	TextGalleryAdapter pathAdapter;
	//ArrayList<FileInfo> fileInfos;
	//ArrayList<String> pathTemp;
	int currentTemp = -1;
	LinuxFileCommand linux;
	//private Thread thread;
	Handler listViewHandler;
	//RelativeLayout settingsView;
	//private SettingsView sv = null;
	
	//private Animation labelShowAnimation = null;
	private Animation menuShowAnimation = null;
	private Animation menuHideAnimation = null;
	private boolean settingsHide = true;
	
	public final static String PREFERENCE = "filedilaog";
	public String SDCARD_PATH ;
	public final String BACKUPUP_DIR = SDCARD_PATH + "/panda";
	//private String currentPath = "/sdcard";
	
//	private int dragId = -1;
	private FileData currentData = null;
	private FileData dragData = null;
	/** 每个标签对应的文件数据*/
	private ArrayList<FileData> datas;
	private boolean dragging;
	private Resources res;
//	private HorizontalScrollView hsv;
	
	// 设置
	SettingsView settingsView = null;
	
	//ArrayList<String> paste = null;
	ArrayList<String> historyString = null;
	TextView currentTag;
	/** 是否多文件操作*/
	boolean multFile = false;

	static final int MAX_PATH_TEMP = 10;
	static final int SINGLFILE = 91;
	static final int MULTFILE = 92;
	float scale;
	int copyButton = -1;
	int copySelection = 2;
	boolean selectionAll = false; 
	
	/** preferences */
	private boolean preIsRoot = false;
	int preViewStyle = FileListAdapter.STYLE_GRID;
	boolean preHideFile = false;
	boolean preHideTag = false;
	String preBackupDir = BACKUPUP_DIR;
	boolean preShowFileSize = true;
	boolean preShowFileDate = true;

	/** 
	 * 文件库数据结构为
	 * parentList 放 文件库名.
	 * childList 放对应文件库里的文件.
	 * 库名在parentList的位置与其文件在childList的位置一样
	 * */
	private ArrayList<String> parentList = new ArrayList<String>();
	private ArrayList<ArrayList<String>> childList = new ArrayList<ArrayList<String>>();
	private FileLibDialog fileLibDialog;// = new FileLibDialog(this, parentList, childList);
	
	
	
	public int screen_height;
	public int screen_width;

	private final static String PRE_ISROOT = "root";
	private final static String PRE_VIEWSTYLE = "viewstyle";
	private final static String PRE_HIDEFILE = "hidefile";
	private final static String PRE_HIDETAG = "hidetag";
	private final static String PRE_CUR_PATH = "currentPath";
	private final static String PRE_FILE_LIB = "filelib";
	private final static String PRE_LIB_CHILD = "libchild";
	private final static String PRE_SHOW_FILE_SIZE = "showfilesize";
	private final static String PRE_SHOW_FILE_DATE = "showfiledate";
	public final static String PRE_BACKUP_DIR = "backup";
	/** root操作可能会改变文件权限, 修改后如果root还没完, 程序出错退出,
	 *  则可能之前的文件的权限变成修改后的, 所以用这个记录有无修改, 无则为 "nc"
	 *  */
	private final static String PRE_FILE_PERM = "fileperm";
	private final static String PRE_FILE_PATH = "filepath";
	
	
	public final static String SAVE_SETTINGS_HIDE = "settingshide";
	
	
    
    private final static int MENU_CREATE_DIRECTORY = 0;
    private final static int MENU_CREATE_FILE = 1;
    private final static int MENU_PASTE = 2;
    public  final static int MENU_SEARCH = 3;
    private final static int MENU_SHOW_COPY_DIALOG = 4;
    private final static int MENU_APK_MANAGER = 5;
    private final static int MENU_SETTING = 6;
    private final static int MENU_SET_VIEW_STYLE = 7;
    private final static int MENU_FILE_LIB = 8;
    private final static int MENU_FINISH_ACTIVITY = 9;
    
    
	
	ConditionVariable copyDialogLock;
	FileItemClickListener listListener;
	//AlertDialog.Builder listdialog;
	//AlertDialog listLongClickDialog;	
	
	/**opt menu */
	private ImageView optUp;
	private ImageView optHistory;
	private ImageView optTag;
	private ImageView optRefresh;
	private ImageView optMultfile;
	private ImageView optMenu;
	
	private ImageView addTagButton;
	
	//Search view
	private LinearLayout searchLayout;
	private TextView searchText;
	private ImageButton searchBtn;
	private ProgressBar searchBar;
	
	private TableRow tagRow;
	private RelativeLayout tagLayout;
	
	private LinearLayout appMenu;
	private SharedPreferences pre;
	
	
	/**
	CharSequence[] longClickListMenu =  {"Open", "Open in other manner", 
			"New folder", "New file", "Paste", "Copy", "Cut", "Delete",
			"Rename", "Properties", "Select all", "Copy selected", "Cut Selected",
			"Delete Selected" };;
	CharSequence[] longClickListMenu2 = { "Open", "Open in other manner",
			"New folder", "New file", "Paste", "Copy", "Cut", "Delete",
			"Rename", "Properties" };
			/**/
	//private CharSequence[] listViewLongClickMenu = {"paste", "new folder", "new file"};
	static final int MENU_ITEM_OPEN = 0;
	static final int MENU_ITEM_OPEN_IN_OTHER = 1;
	static final int MENU_ITEM_COPY = 2;
	static final int MENU_ITEM_CUT = 3;
	
	static final int MENU_ITEM_PASTE = 4;
	static final int MENU_ITEM_DELETE = 5;
	static final int MENU_ITEM_RENAME = 6;
	static final int MENU_ITEM_SELECT_ALL = 7;
	
	static final int MENU_ITEM_UNDRABLE= 8;
	static final int MENU_ITEM_DRABLE = 9;
	static final int MENU_ITEM_ADD_LIB = 10;
	static final int MENU_ITEM_PROPERTIES = 11;
	
	
	static final int MENU_ITEM_COPY_SELECTED = 12;
	static final int MENU_ITEM_CUT_SELECTED = 13;
	static final int MENU_ITEM_DELETE_SELECTED = 14;
	static final int MENU_ITEM_CREATE_DIRECTORY = 15;
	static final int MENU_ITEM_CREATE_FILE = 16;
	
	
	
	public final static int HANDLER_SHOW_COPY_WARNING_DIALOG = 11;
	public final static int HANDLER_SET_LISTVIEW_SELECTED = 12;
	public final static int HANDLER_REFRESH_LISTVIEW = 13;
	public final static int HANDLER_CLIP_BOARD_EMPTY = 14;
	public final static int HANDLER_COPY_FILE_ERROR = 15;
	public final static int HANDLER_FILE_CLICK = 16;
	public final static int HANDLER_LIST_ADPATER_CHANGED = 17;
	public final static int HANDLER_SEARCHBAR_HIDE = 18;
	public final static int HANDLER_SEARCHSTOP = 19;
	public final static int HANDLER_SET_SEARCHDIR = 20;
	public final static int HANDLER_SET_SEARCH_VISIBLE = 21;
	public final static int HANDLER_ADD_LIST_ITEM = 22;
	
	


    public final static int RESULT_GET_FILE_SIZE = 0;
    
    /**
     * 获取文件/目录的intent
     * */
    public final static String ACTION_GET_FILE = "xjf.intent.action.GETFILE";
    public final static String ACTION_GET_FILES = "xjf.intent.action.GETFILES";
    public final static String ACTION_GET_DIRECTORY = "xjf.intent.action.GETDIRECTORY";
    public final static String EXTR_GET_NAME = "extr_get_name";
    public final static int GET_FILE = 0;
    public final static int GET_FILES = 1;
    public final static int GET_DIRECTORY = 2;
    private Button selectedBtn;
    private LinearLayout selectedLayout;
    private EditText selectedEdit;
    private boolean selectedAction = false;
    private int selectedAct = -1;
	
	
	
	public String ok;
	public String cancel;
	private Button mountBtn;

	public static final String RW_ROOT = "/";
	public static final String RW_SYSTEM = "/system";
	public class Mounts {
		public final String[] fs = {
				RW_ROOT, RW_SYSTEM
		};
		//权限
		public String[] perm = new String[fs.length];
		//设备文件
		public String[] rawDev = new String[fs.length];
		public int index = -1;
		//public final String mountRW = " 读写 "; 
		//public final String mountRO = " 只读 "; 
		public static final String RO = "ro";
		public static final String RW = "rw";
	}
	private final Mounts mounts = new Mounts();
	public boolean canWriteRoot() { return (mounts.perm[0].equals(Mounts.RW));}
	public boolean canWriteSystem() {return (mounts.perm[1].endsWith(Mounts.RW));}
	//private boolean mountV = false;

	public final static boolean D = false;
	public final static String tag = "FileDialog";
	public static final void dbgf(String tag, String msg) {
		if (D) {
			Log.d(tag, msg);
		}
	}
	public static final void dbg(String msg) {
		if (D) {
			Log.d(tag, msg);
		}
	}
	
	public static final void warning(String msg) {
		Log.w(tag, msg);
	}
	public static final void error(String msg) {
		Log.e(tag, msg);
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.setTheme(R.style.blue_theme);
        
        
        setContentView(R.layout.main);
        dbg("Start FileManager");
        res = getResources();

        ok = getString(R.string.ok);
        cancel = getString(R.string.cancel);
        SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

        String act = getIntent().getAction();
        if (!act.equals(Intent.ACTION_MAIN)) {
        	// 不是main entry point开始，是获取文件的intent
        	selectedAction = true;
        	selectedLayout = (LinearLayout) findViewById(R.id.selectedlayout);
        	selectedBtn = (Button) findViewById(R.id.selectedbtn);
        	selectedEdit = (EditText) findViewById(R.id.selectededit);
        }
        
        screen_width = res.getDisplayMetrics().widthPixels;
        screen_height = res.getDisplayMetrics().heightPixels;
        pre = getSharedPreferences(PREFERENCE, MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
        newObject();
        fileLibDialog = new FileLibDialog(this, parentList, childList);
        
        //从xml里获取View
        findView();
        setupToolbar();
        
        initFileAdapter();

    	initFileViewList();
    	initFileViewGrid();
        initViewHandler();
        initAppMenu();
        initMountBtn();
               
        loadPreferences();
        //Log.d(tag, "after loadPreferences");
        initFilePathGallery();
        initTag();
        initSearchStop();
        
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        loadPerm("", 0, true);

        if (selectedAction) {
        	if (act.equals(ACTION_GET_FILE)) {
        		selectedAct = GET_FILE;
        	} else if (act.equals(ACTION_GET_FILES)) {
        		selectedAct = GET_FILES;
        	} else if (act.equals(ACTION_GET_DIRECTORY)) {
        		selectedAct = GET_DIRECTORY;
        	} 
        	initSelectedFile();
        }
	}/** Oncreate()*/
    

    protected void onStop () {
    	super.onStop();
    	storePreferences();
    }
    
    /**
    protected void onStart (){
    	super.onStart();
    }
    /***/
    protected void onResume (){
    	super.onResume();
    }
    protected void onPause () {
    	super.onPause();
    	if (sensorDoing)
    		sensorMgr.unregisterListener(sensorListener);
    }
    /**/
    protected void onSaveInstanceState (Bundle outState){
    	outState.putString(PRE_CUR_PATH, currentPath());
    	outState.putBoolean(SAVE_SETTINGS_HIDE, settingsHide);
    	
    	
    	if (!settingsHide)
    		settingsView.saveInstanceState(outState);
    	super.onSaveInstanceState(outState);
    }
    protected void onRestoreInstanceState (Bundle savedInstanceState){
    	if (savedInstanceState != null){
    		dbg("onRestoreInstanceState");
    		// 如果保存状态前是打开 设置窗口, 则还原之前状态
    		if (!(settingsHide = savedInstanceState.getBoolean(SAVE_SETTINGS_HIDE, true))) {
    			if (settingsView == null) {
    				settingsView = new SettingsView(this);
    			}
    			settingsView.show(savedInstanceState);
    		}
    		
    		
    		currentData.path = savedInstanceState.getString(PRE_CUR_PATH);
    		if (currentData.path == null)
    			currentData.path = SDCARD_PATH;
    		refreshPath(currentPath(), true);
    	}
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
    public final static String paramSNc = "nc";
    public final static int paramINc = 0x999;
    
    /**
     * 加载 根目录('/')和/sytem目录的权限
     * */
    public void loadPerm(String file, int perm, boolean getFromPre) {
    	if (getFromPre) {
	    	perm = pre.getInt(PRE_FILE_PERM, paramINc);
	    	if (perm == paramINc)
	    		return;
	    	file = pre.getString(PRE_FILE_PATH, paramSNc);
	    	if (file.equals(paramSNc))
	    		return;
    	}
       	int ret = 0;
       	if (file == null) {
       		return;
       	}
    	DataOutputStream out = null;
    	BufferedReader br = null;
    	Process p = null;
    	try {
			p = linux.shell.exec("su\n");
			out = new DataOutputStream(p.getOutputStream());
			String cmd = String.format("chmod %x %s\nexit\n", perm, file); 
				//"chmod " + perm + " " + file + "\nexit\n";
			out.writeBytes(cmd);
			out.flush();
			ret = p.waitFor();
			if (ret < 0) {
				br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				Log.d(tag, "chmod error");
				if (br.ready()) {
					Toast.makeText(this, br.readLine(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "chmod 出错", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			p.destroy();
		}
    	
    }
    /** 加载基本设置*/
    private void loadPreferences(){
    	int viewStyle = preViewStyle;
    	preHideTag = pre.getBoolean(PRE_HIDETAG, false);
    	preHideFile = pre.getBoolean(PRE_HIDEFILE, false);
    	preIsRoot = pre.getBoolean(PRE_ISROOT, false);
    	preViewStyle = pre.getInt(PRE_VIEWSTYLE, FileListAdapter.STYLE_GRID);
    	preBackupDir = pre.getString(PRE_BACKUP_DIR, BACKUPUP_DIR);
    	preShowFileDate = pre.getBoolean(PRE_SHOW_FILE_DATE, true);
    	preShowFileSize = pre.getBoolean(PRE_SHOW_FILE_SIZE, true);
    	if (preHideTag || selectedAction){
    		tagLayout.setVisibility(View.GONE);
			optTag.setImageDrawable(res.getDrawable(R.drawable.tag_hide));
    		
    	} else {
    		tagLayout.setVisibility(View.VISIBLE);
			optTag.setImageDrawable(res.getDrawable(R.drawable.tag_show));
    	}
    	if (preViewStyle == FileListAdapter.STYLE_GRID)
    		fileView = fileViewGrid;
    	else
    		fileView = fileViewList;
    	if (viewStyle != preViewStyle)
    		setFileViewStyle(preViewStyle);
    	
    	String libs = pre.getString(PRE_FILE_LIB, null);
    	if (libs != null && !libs.equals("[]") ){
    		libs = libs.substring(1, libs.length() - 1);
        	String[] s = libs.split(", ");
        	String c = null;
    		ArrayList<String> child;
    		int length = s.length;
        	for (int i = 0; i < length; i++){
        		parentList.add(s[i]);
        		c = pre.getString(PRE_LIB_CHILD + i, null);
        		if (c == null)
        			continue;
        		child = new ArrayList<String>();
        		string2ArrayList(c, child);
        		childList.add(child);
        	}
    	}
    }
    
    /** 保存设置*/
    private void storePreferences(){
    	SharedPreferences.Editor editor = pre.edit();
    	editor.putBoolean(PRE_HIDEFILE, preHideFile);
    	editor.putBoolean(PRE_HIDETAG, preHideTag);
    	editor.putBoolean(PRE_ISROOT, preIsRoot);
    	editor.putInt(PRE_VIEWSTYLE, preViewStyle);
    	editor.putString(PRE_BACKUP_DIR, preBackupDir);
    	editor.putString(PRE_FILE_LIB, parentList.toString());
    	editor.putBoolean(PRE_SHOW_FILE_DATE, preShowFileDate);
    	editor.putBoolean(PRE_SHOW_FILE_SIZE, preShowFileSize);
    	int size = parentList.size();
    	ArrayList<String> child;
    	for (int i = 0; i < size; i++) {
    		child = childList.get(i);
    		editor.putString(PRE_LIB_CHILD + i, child.toString());
    	}
    	
    	editor.commit();
    }
    
    public void storePerm(String file, int perm) {
    	SharedPreferences.Editor editor = pre.edit();
    	editor.putInt(PRE_FILE_PERM, perm);
    	editor.putString(PRE_FILE_PATH, file);
    	editor.commit();
    }
    /** 从ArrayList的toString() 得到的字符串转加到ArrayList*/
    private final void string2ArrayList(String src, ArrayList<String> dst) {
    	src = src.substring(1, src.length() - 1);
    	String[] s = src.split(", ");
    	if (s.length == 1 && s[0].equals(""))
    		return;
    	for (int i = 0; i < s.length; i++){
    		dst.add(s[i]);
    	}
    }
    
    
    /** 返回APK备份目录*/
    public String getApkBackupDir() {return preBackupDir;}
    /** 设置APK备份目录*/
    public void setApkBackupDir(String dir) { preBackupDir = dir;}
    public boolean isHideFile() { return preHideFile;}
    public void setHideFile(boolean b) {
    	if (b == preHideFile)	return;
    	preHideFile = b;
    	if (preHideFile == false) {
    		refreshPath(currentPath(), false);
    	} else {
    		ArrayList<FileInfo> infos = currentFileInfo();
    		for (int i = 0; i < infos.size(); ){
    			if (infos.get(i).name().startsWith("."))
    				infos.remove(i);
    			else
    				i++;
    		}
    		fileAdapter.notifyDataSetChanged();
    	}
    }
    public boolean isRoot() { return preIsRoot;}
    public boolean isMultFile() { return multFile;}
    public final boolean showFileSize() { return preShowFileSize;}
    public void setShowFileSize(boolean b) { preShowFileSize = b;}
    public boolean showFileDate() { return preShowFileDate;}
    public void setShowFileDate(boolean b) { preShowFileDate = b;}
    public float getDensity() { return scale;}

	public ArrayList<FileInfo> currentFileInfo() {
		return currentData.fileInfos;
	}
	public ArrayList<Integer> selectedItem() { 
		return currentData.selectedId;
	}
	//public boolean root() { return pre_IsRoot;}
	/** 返回当前tag的目录路径*/
	public String currentPath() { return currentData.path;}

    public void clearFileSlected(){ 
    	currentData.selectedId.clear();
    	if (selectedAction && selectedAct == GET_FILES)
    		selectedEdit.setText("");
    }
    
    public int getCurrentSelectedCount() {return currentData.selectedId.size();}
	private int clickTime = 0;
	/** 点击时的Tag的文件资料*/
	FileListAdapter.FileInfo clickInfo;
	
    
    /**
     * 
     * */
    private void initViewHandler() {
        listViewHandler = new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		switch (msg.what){
        		case FileManager.HANDLER_SHOW_COPY_WARNING_DIALOG:
        			break;
        			/**
        			 * 更新当前选择项,如果arg1 == FileDialog.REFRESH_LISTVIEW
        			 * 则先刷新当前列表.
        			 * */
        		case FileManager.HANDLER_SET_LISTVIEW_SELECTED:
        			int p = fileView.getFirstVisiblePosition();
        			if (msg.arg2 == FileManager.HANDLER_REFRESH_LISTVIEW)
        				refreshPath(currentPath(), false);
        			fileView.setSelection(p);
        			break;
        			/**
        			 * 刷新当前列表,如果arg1 == FileDialog.SET_LISTVIEW_SELECTED
        			 * 则把当前选择到arg2处
        			 * */
        		case FileManager.HANDLER_REFRESH_LISTVIEW:
        			p = fileView.getFirstVisiblePosition();
        			refreshPath(currentPath(), false);
        			if (msg.arg1 == FileManager.HANDLER_SET_LISTVIEW_SELECTED)
        				fileView.setSelection(p);
        			break;
        		case FileManager.HANDLER_CLIP_BOARD_EMPTY:
        			Toast.makeText(FileManager.this, getString(R.string.clipboard_is_empty), 
        					Toast.LENGTH_SHORT).show();
        			break;
        		case FileManager.HANDLER_COPY_FILE_ERROR:
        			Toast.makeText(FileManager.this, listListener.bTmp, Toast.LENGTH_LONG).show();
        			listListener.bTmp = "";
        			break;
        			
        		case FileManager.HANDLER_FILE_CLICK:
        			itemClick(msg.arg1);
        			break;
        			
        		case HANDLER_LIST_ADPATER_CHANGED:
        			fileAdapter.notifyDataSetChanged();
        			break;
        		case HANDLER_SEARCHBAR_HIDE:
        			searchBar.setVisibility(View.GONE);
        			break;
        		case HANDLER_SEARCHSTOP:
        			searchBar.setVisibility(View.GONE);
        			searchBtn.setVisibility(View.GONE);
        			break;
        		case HANDLER_SET_SEARCHDIR:
        			searchText.setText(searchDir);
        			break;
        		case HANDLER_SET_SEARCH_VISIBLE:
            		searchBar.setVisibility(View.VISIBLE);
            		//searchBtn.setVisibility(View.VISIBLE);
            		searchLayout.setVisibility(View.VISIBLE);
            		break;
        		case HANDLER_ADD_LIST_ITEM:
        			Bundle b = msg.getData();
        			if (b != null ) {
        				searchFileInofs.add(new FileInfo(b.getString(SEARCHFILEPATH)));
	            		fileAdapter.notifyDataSetChanged();
        			}
        			break;
        		}
        		
        	}
        };
        
    }
    
    private void itemClick(int position){

		String path = clickInfo.path();
		

		if (selectedAction) {
			switch (selectedAct) {
			case GET_DIRECTORY:
				if (clickInfo.directory) {
					selectedEdit.setText(clickInfo.name);
				}
				return;
			case GET_FILE:
				selectedEdit.setText(clickInfo.name);
				return;
			case GET_FILES:
				break;
			default: return;
			}
		}
		
		if (multFile){
			//fileInfos.get(position).invertSelected();
			if (currentData.selectedId.contains(position)) {
				currentData.selectedId.remove(new Integer(position));
				if (selectedAction) {
					String tmp = selectedEdit.getText().toString();
					selectedEdit.setText(tmp.replaceFirst(clickInfo.name + ",", ""));
				}
			} else {
				currentData.selectedId.add(new Integer(position));
				if (selectedAction) {
					selectedEdit.append(clickInfo.name + ",");
				}
			}
			fileAdapter.notifyDataSetChanged();
			return;
		}
		
		
		// 点击目录
		if (clickInfo.directory) {
			/**
			if (pathTemp.size() > MAX_PATH_TEMP) {
				pathTemp.remove(0);
				currentTemp = MAX_PATH_TEMP - 1;
			}/**/
			if (currentData.searchingTag) {
				addTag(new FileData(new ArrayList<FileListAdapter.FileInfo>(), 
						null, SDCARD_PATH));
				return;
			}
			int pa = pathAdapter.getAbsolutePath().indexOf(path);
			refreshPath(path, pa != 0);
			if (pa == 0){
				pathAdapter.setCurrentPosition(path.split("/").length - 1);
				pathAdapter.notifyDataSetChanged();
			}
		}else{
			// 点击文件
			listListener.doOpenFile(path);
		}
    }
    
    private void initFileAdapter(){
        fileAdapterList = new FileListAdapter(this, currentData, FileListAdapter.STYLE_LIST);
        fileAdapterGrid = new FileGridAdapter(this, currentData, FileListAdapter.STYLE_GRID);
        FileListAdapter.AddOneIconListener aoi = new 
        			FileListAdapter.AddOneIconListener() {
						
						@Override
						public void addOneIcon(int position) {
							// TODO Auto-generated method stub
							//listViewHandler.sendEmptyMessage(HANDLER_REFRESH_LISTVIEW);
						}
					};
        if (preViewStyle == FileListAdapter.STYLE_LIST){
        	fileAdapter = fileAdapterList;
        } else {
        	fileAdapter = fileAdapterGrid;
        }
        fileAdapterList.setAddOneIcon(aoi);
        fileAdapterGrid.setAddOneIcon(aoi);
        listListener = new FileItemClickListener(this);
    }
    
    
    private void setFileViewStyle(int style){
    	preViewStyle = style;
        if (preViewStyle == FileListAdapter.STYLE_LIST){
            fileAdapter = fileAdapterList;
            fileViewList.setVisibility(View.VISIBLE);
            fileViewGrid.setVisibility(View.GONE);
        	fileView = fileViewList;
			viewStyleTextView.setText(getString(R.string.icon));
			viewStyleButton.setBackgroundResource(R.drawable.multicon);
        } else {
        	fileAdapter = fileAdapterGrid;
            fileViewGrid.setVisibility(View.VISIBLE);
            fileViewList.setVisibility(View.GONE);
        	fileView = fileViewGrid;
			viewStyleTextView.setText(getString(R.string.list));
			viewStyleButton.setBackgroundResource(R.drawable.multlist);
        }
        fileAdapter.setData(currentData);
        fileAdapter.notifyDataSetChanged();
    }
    
    private void initFileViewList() {
        /*files lists view*/
    	fileViewList.setFastScrollEnabled(true);
        fileViewList.setItemsCanFocus(true);
        fileViewList.setAdapter(fileAdapterList);
        fileAdapterList.setListView(fileViewList);
        fileViewList.setScrollBarStyle(ListView.SCROLLBARS_INSIDE_INSET);
        fileViewList.setHeaderDividersEnabled(true);
        fileViewList.setOnItemLongClickListener(fileViewLongClickListener);
        fileViewList.setOnItemClickListener(fileViewClickListener); 
        fileViewList.dragMaxX = 40;
        fileViewList.dragMinX = 0;
        fileViewList.setDropListener(dropListener);
        fileViewList.setStartDragListener(stargDragListener);
        fileViewList.setDropOutListener(new DropOutListener() {
			
			@Override
			public void dropOut(int from, int x, int y) {
				// TODO Auto-generated method stub
				dropListener.drop(from, -1);
				dbg("out");
				/**
				if (y > fileViewList.getBottom()){
					dropListener.drop(from, -1);
				}
				/**/
			}
		});
        
    }
    
    public void showAddFileLibDialog() {
    	fileLibDialog.doWhat = FileLibDialog.FILE_LIB_ADD;
    	fileLibDialog.setPath(listListener.getFile());
    	fileLibDialog.show();
    }
    
    /**
     * 文件item 长按 listener
     * */
    private OnItemLongClickListener fileViewLongClickListener 
    			= new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String file = currentFileInfo().get(position).path();
			
			listListener.setFile(file);
			listListener.setPosition(position);
			listListener.setName(currentFileInfo().get(position).name());
			ItemMenuDialog dialog = new ItemMenuDialog(FileManager.this);
			dialog.selectedFile(file);
			
			dialog.show();
			
			//fileViewGrid.setDragable(true);

			return true;
		}
	};
	
	
	/***/
	public void clearClickTime(){ 
		clickTime = 0;
		fileViewGrid.setDragable(false);
	}
	/**
	 * 文件 item 点击 listener
	 * */
    private OnItemClickListener fileViewClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (fileViewGrid.dragable())	return;
				clickInfo = currentFileInfo().get(position);
				new Timer().schedule(new FileClickTimerTask(position), 0);
				/**
					clickTime++;
					if (D) {
						Log.d(tag, "time: " + clickTime + " -- " + System.currentTimeMillis());
					}
					if (clickTime >= 2) {
						if (D) {
							Log.d(tag, "???: " + clickTime + " -- " + System.currentTimeMillis());
						}
						clearClickTime();
						doubleTimer.cancel();
						doubleTimer.purge();
						return;
					}
				
					clickInfo = currentFileInfo().get(position);
					if (pre_ViewStyle == FileAdapter.STYLE_GRID){
						fileViewGrid.setDragable(true);
						doubleTimer = new Timer();
						doubleTimer.schedule(new FileClickTimerTask(position), DOUBLE_CLICK_DURATION);
					} else {
						doubleTimer = new Timer();
						doubleTimer.schedule(new FileClickTimerTask(position), 0);
					}
					if (D) {
						Log.d(tag, "end: " + " -- " + System.currentTimeMillis());
					}
					/**/
			}
	};
	
	class FileClickTimerTask extends TimerTask {
		private int position;
		public FileClickTimerTask(int position){
			this.position = position;
		}
		@Override
		public void run() {

			synchronized (fileViewClickListener) {
				// TODO Auto-generated method stub
				/**
				if (clickTime != 1) {
					clearClickTime();
					return;
				}
				clearClickTime();
				/**/
				Message msg = listViewHandler.obtainMessage(HANDLER_FILE_CLICK, 
						position, 0);
				listViewHandler.sendMessage(msg);
			}
		}
	};
    private void initFileViewGrid(){
    	fileViewGrid.setFastScrollEnabled(true);
    	fileViewGrid.setGravity(Gravity.FILL);
    	fileViewGrid.setAdapter(fileAdapterGrid);
    	fileAdapterGrid.setListView(fileViewGrid);
    	fileViewGrid.setOnItemClickListener(fileViewClickListener);
    	fileViewGrid.setOnItemLongClickListener(fileViewLongClickListener);
    	fileViewGrid.setStartDragListener(stargDragListener);
    	fileViewGrid.setDropListener(dropListener);
    	//fileViewGrid.setParentView(findViewById(R.id.ddv));
    	fileViewGrid.setDropOutListener(new DropOutListener() {
			
			@Override
			public void dropOut(int from, int x, int y) {
				// TODO Auto-generated method stub
				//Log.d(tag, "fileView Bottom : " + fileView.getBottom());
				if (y > 0)
					dropListener.drop(from, -1);
			}
		});
    }
    
    /** 文件拖拉,放手时调用*/
    DropListener dropListener = new DropListener() {
		
		@Override
		public void drop(int from, int to) {
			// TODO Auto-generated method stub
			boolean sameItem = false;
			if (sensorDoing){
				sensorDoing = false;
				sensorMgr.unregisterListener(sensorListener);
			}
			//dragId = -1;
			dragging = false;
			dragFromFile = dragData.fileInfos.get(from).path();
			if (to == -1) {
				dragToPath = currentPath();
			} else {
				dragToPath = currentFileInfo().get(to).path();
			}
			//是否为同一文件
			sameItem = dragToPath.equals(dragFromFile);
			File file = new File(dragToPath);
			if (file.isDirectory() && !sameItem){
				
			} else{
				if (!sameItem)
					return;
				//放手时,指向的位置的文件为拖拉的对象,则表示删除文件
				listListener.setName(Common.getPathName(dragToPath));
				listListener.setFile(dragToPath);
				listListener.setPosition(from);
				listListener.onClick(null, MENU_ITEM_DELETE);
				return;
			}
			AlertDialog.OnClickListener li = new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					ArrayList<String> f = null;
					switch (which){
					case AlertDialog.BUTTON_POSITIVE:
						//复制
						if (multFile && (f = getDragFiles(dragData)) !=null) {
							;
						} else {
							f = new ArrayList<String>();
							f.add(dragFromFile);
						}
						listListener.startCopyService(f, 
								dragToPath,
								false);
						break;
					case AlertDialog.BUTTON_NEUTRAL:
						//剪切
						if (multFile && (f = getDragFiles(dragData)) !=null) {
							;
						} else {
							f = new ArrayList<String>();
							f.add(dragFromFile);
						}
						listListener.startCopyService(f, 
								dragToPath,
								true);
						break;
					case AlertDialog.BUTTON_NEGATIVE:
						break;
					}
					dragData = null;
				}
				
			};
			AlertDialog.Builder b = new AlertDialog.Builder(FileManager.this);
			b.setTitle("拷贝粘贴").setMessage("从: " + dragFromFile + "\n到: " + dragToPath);
			b.setPositiveButton("拷贝", li).setNeutralButton("剪切", li)
				.setCancelable(false)
				.setNegativeButton(cancel, li).create().show();
		}
	};
	
	private ArrayList<String> getDragFiles(FileData d){
		ArrayList<String> files = null;
		if (d == null){
			return files;
		}
		ArrayList<FileInfo> infos = d.fileInfos;
		ArrayList<Integer> sels = d.selectedId;
		if (sels != null && infos != null && !sels.isEmpty()){
			int size = sels.size();
			files = new ArrayList<String>();
			for (int i = 0; i < size; i++)
				files.add(infos.get(sels.get(i)).path);
		}
		return files;
	}
    String dragFromFile, dragToPath;
    //private int dragIndex = 0;
    
    private StartDragListener stargDragListener = 
    			new StartDragListener() {
					
		@Override
		public void startDrag(int from) {
			// TODO Auto-generated method stub
			//dragIndex = 0;
			//dragId = from;
			dragging = true;
			dragData = currentData;
			if (tagRow.getChildCount() != 1) {
				sensorDoing = true;
				sensorMgr.registerListener(sensorListener, 
						accSensor, SensorManager.SENSOR_DELAY_NORMAL);
				sensorLastX = 0;
			}
		}
	};
	private boolean sensorDoing = false;
    private Sensor accSensor;
    private SensorManager sensorMgr;
    private float sensorLastX = 0;
    private long sensorLastTime = 0;
    private SensorEventListener sensorListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			long currentTime = event.timestamp;
			float x = event.values[SensorManager.DATA_X];
			if ( (currentTime > (sensorLastTime + 250000000)) ) {
				//long diffTime = currentTime - sensorLastTime;
				//float speed = Math.abs(x - sensorLastX)
				//		/ diffTime;
				/**
				if ((speed > 0.00000001) && (Math.abs(sensorLastX) < 3)) {
					if ( x > 0) {
						nextTag(KeyEvent.KEYCODE_DPAD_LEFT);
					} else {
						nextTag(KeyEvent.KEYCODE_DPAD_RIGHT);
					}
					sensorLastX = x;
				} else {
					if (Math.abs(x) < 3) {
						sensorLastX = x;
						sensorLastTime = event.timestamp;
					}
				}
				/**/
				if (Math.abs(x) < 3) {
					sensorLastTime = event.timestamp;
					sensorLastX = x;
				}
				if (Math.abs(sensorLastX) > 3) {
					return;
				}
				if (Math.abs(x) > 3.7) {
					if ( x > 0) {
						nextTag(KeyEvent.KEYCODE_DPAD_LEFT);
					} else {
						nextTag(KeyEvent.KEYCODE_DPAD_RIGHT);
					}
					sensorLastX = x;
				}
			}
			
			//event.
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
    
    /**
    @Override
    public boolean onTouchEvent(MotionEvent event){
    	Log.d(tag, "a ont");
		return  onTouchEvent(event);
    }
    /**/
    
	private String[] pathBarString = new String[]{"/", "sdcard"};
    private void initFilePathGallery() {
    	/*file path*/
        pathAdapter = new TextGalleryAdapter(this, pathBarString, pathScroll,
        		pathScroll.getDataChangedListener());
        pathScroll.setAdapter(pathAdapter);
        pathScroll.setSelection(pathAdapter.getCount() - 1);
        pathScroll.setOnItemClickListener(new HorizontalListView.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
		    	String path = pathAdapter.getPath(position);
		    	if (currentPath().equals(path))
		    		return;
				refreshPath(path, false);
				pathScroll.setSelection(position);
				//pathAdapter.refreshPath(currentPath());
			}
		});
        pathScroll.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				final EditText pEdit = new EditText(FileManager.this);
				pEdit.setText(currentPath());
				XDialog.createInputDialog(FileManager.this, null, pEdit)
					.setTitle(getString(R.string.path))
					.setPositiveButton(ok, new AlertDialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							File f = new File(pEdit.getText().toString());
							if (!f.exists()){
								Toast.makeText(FileManager.this, 
										getString(R.string.the_input_path_error_or_not_exsit), 
										Toast.LENGTH_LONG).show();
								return;
							}
							refreshPath(pEdit.getText().toString(), true);
						}
					}).setNegativeButton(cancel, new AlertDialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// do nothing
						}
					}).create().show();
				return true;
			}
		});
    }
    
    
    /**
     * 标签栏
     * */
    private void initTag(){
    	addTag(currentData);
    	addTagButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addTag(new FileData(new ArrayList<FileListAdapter.FileInfo>(), 
						null, SDCARD_PATH)
				);				
			}
		});
    }
    
    /**
     * Add tag 
     * */
    private OnClickListener tagOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//FileData tmp = currentData;
			TextView tv = (TextView)v;
			if (tv == null || tv == currentTag)
				return;
			FileData data = (FileData)tv.getTag();
			currentTag.setBackgroundResource(R.drawable.tag2);
			currentTag = tv;
			currentTag.setBackgroundResource(R.drawable.tag1);
			setCurrentData(data, true);
			if (currentData.searchingTag){
				searchLayout.setVisibility(View.VISIBLE);
			} else if (searchLayout.getVisibility() == View.VISIBLE) {
				searchLayout.setVisibility(View.INVISIBLE);
			}
			showOrHideMount();
		}
	};
	
	private OnLongClickListener tagLongClickListener = 
		new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (tagRow.getChildCount() > 1) {
					TextView tv = (TextView) v;
					if (tv == null)
						return true;
					if (tv == currentTag){
						nextTag(KeyEvent.KEYCODE_DPAD_RIGHT);
					}
					datas.remove(tv.getTag());
					tagRow.removeView(v);
				}
				return true;
			}
		};
    
    private void addTag(FileData data){
		TextView tv = new TextView(FileManager.this);
		tv.setTextColor(0xff000000);
		//tv.setWidth(80);
		tv.setSingleLine();
		tv.setGravity(Gravity.CENTER);
		tv.setEllipsize(TruncateAt.MARQUEE);
		tv.setPadding(5, 5, 5, 5);
		//tv.setBackgroundResource(R.drawable.btag);
		tv.setBackgroundResource(R.drawable.tag1);
			
		tv.setOnClickListener(tagOnClickListener);
		tv.setOnLongClickListener(tagLongClickListener);

		
		if (currentTag != null)
			currentTag.setBackgroundResource(R.drawable.tag2);
		currentTag = tv;
		
		currentTag.setText(SDCARD_PATH);
		currentTag.setTag(data);
		setCurrentData(data, false);
		
		refreshPath(data.path, true);
		
		datas.add(data);
		tagRow.addView(tv);

		if (searchLayout.getVisibility() == View.VISIBLE) {
			searchLayout.setVisibility(View.INVISIBLE);
		}
		
		//hsv.smoothScrollTo(tagRow.getWidth(), 0);
    }
    
    
    /**
     * w --> KeyEvent.KEYCODE_DPAD_RIGHT 
     * 			or
     * 		 KeyEvent.KEYCODE_DPAD_LEFT	
     * */
    private void nextTag(int w){
    	int count = tagRow.getChildCount();
    	if (count == 1)
    		return;
    	int index = datas.indexOf(currentData);
    	if (w == KeyEvent.KEYCODE_DPAD_RIGHT) {
    		index++;
    		if (index >= count)
    			index = 0;
    	} else if (w == KeyEvent.KEYCODE_DPAD_LEFT){
    		index--;
    		if (index < 0)
    			index = count - 1;
    	}

    	currentTag.setBackgroundResource(R.drawable.tag2);
		currentTag = (TextView) tagRow.getChildAt(index);
    	currentTag.setBackgroundResource(R.drawable.tag1);
				
		setCurrentData(datas.get(index), true);
		currentData = datas.get(index);
		if (preViewStyle == FileListAdapter.STYLE_GRID) {
			fileViewGrid.clearDragBG();
		} else {
			fileViewList.clearDragBG();
		}
		
		if (currentData.searchingTag){
			searchLayout.setVisibility(View.VISIBLE);
		} else if (searchLayout.getVisibility() == View.VISIBLE) {
			searchLayout.setVisibility(View.INVISIBLE);
		}
		showOrHideMount();
    }

	/** 是否显示mount 按键*/
    private void showOrHideMount() {
		if (currentPath().equals("/")) {
			showMount(0);
		} else if ( currentPath().contains(mounts.fs[1])) {
			showMount(1);
		} else if (mountBtn.getVisibility() == View.VISIBLE) {
			hideMount();
		}
    }
    
    /**
     * 设置当前文件数据,更新ListView 和 路径栏
     * */
    private void setCurrentData(FileData d, boolean refreshView) { 
    	fileAdapter.setData(d);
    	currentData = d;
    	if (refreshView)
    		fileAdapter.notifyDataSetChanged();
    	refreshTextPath();
    }
    

    /**
     * 设置下方工具栏的点击事件
     * */
    private final void setupToolbar(){
    	optUp.setOnClickListener(toolbarListener);
    	optRefresh.setOnClickListener(toolbarListener);
    	optHistory.setOnClickListener(toolbarListener);
    	optMultfile.setOnClickListener(toolbarListener);
    	optMenu.setOnClickListener(toolbarListener);
    	optTag.setOnClickListener(toolbarListener);
    }
    
    View.OnClickListener toolbarListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.opthistory:
				showHistory();
				break;
			case R.id.optmenu:
	    		if (appMenu.getVisibility() == View.VISIBLE) {
	    			hideAppMenu();
	    		} else {
	    			showAppMenu();
	    		}
				break;
			case R.id.optmultfile:
				if (!selectedAction || selectedAct == GET_FILES)
					multOrSingle(!multFile);
				break;
			case R.id.optrefresh:
				clearFileSlected();
				refreshPath(currentPath(), true);
				break;
			case R.id.opttag:
				if (selectedAction)
					return;
				// TODO Auto-generated method stub
				if (tagLayout.getVisibility() == View.VISIBLE){
					tagLayout.setVisibility(View.GONE);
					preHideTag = true;
					optTag.setImageDrawable(res.getDrawable(R.drawable.tag_hide));
				} else {
					tagLayout.setVisibility(View.VISIBLE);
					preHideTag = false;
					optTag.setImageDrawable(res.getDrawable(R.drawable.tag_show));
				}
				break;
			case R.id.optup:
				File file = new File(currentPath());
				String pa = file.getParent();
				int i;
				if (pa == null) {
					pa = "/";
					i = 0;
				} 
				if (pa.equals("/")) {
					i = 0;
				} else {
					i = pa.split("/").length - 1;
				}
				refreshPath(pa, true);
				pathScroll.setSelection(i);
				break;
			default: break;
			}
		}
    };
    
    
    private void showHistory(){

		int l = historyString.size();
		String[] str = historyString.toArray(new String[l]);
		CharSequence[] ch = new CharSequence[l];
		for (int i = 0; i < l; i++){
			if (str[i].length() == 1) {
				ch[i] = "/";
			} else
				ch[i] = str[i].substring(str[i].lastIndexOf("/") 
						+ 1, str[i].length());
		}
		
		new AlertDialog.Builder(FileManager.this)
			.setTitle(getString(R.string.history))
			.setItems(ch, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// 同时更新路径栏
					String s = (String) historyString.get(which);
					if (currentPath().equals(s))
						return;
					int p = pathAdapter.getAbsolutePath().indexOf(s);
					refreshPath(s, p != 0);	
					FileManager.dbg("--" + s);
					p = s.split("/").length;
					if (p != 0) {
						p--;
					}
					pathScroll.setSelection(p);
				}

			}).setNegativeButton(cancel, new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
				
			}).create().show();
    }
    
    public void callMenu() {    	/***
    	getCurrentInputConnection().sendKeyEvent(

                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));

        getCurrentInputConnection().sendKeyEvent(

                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
                /**/
    	this.openOptionsMenu();
    }
    
    public void selectedAllEle(){
    	multOrSingle(true);
    }
    
    
    /**
     * true for multiple
     * false for single
     * */
    private void multOrSingle(boolean ms){

		if (ms){
			optMultfile.setImageDrawable(res.getDrawable(R.drawable.singlefile));
			multFile = true;
		} else {
			optMultfile.setImageDrawable(res.getDrawable(R.drawable.multfile));
			clearFileSlected();
			fileAdapter.notifyDataSetChanged();
			multFile = false;
		}
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			fileViewGrid.setDragable(false);
			return true;
    	}
		return super.onKeyUp(keyCode, event);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	switch (keyCode) {
    	case KeyEvent.KEYCODE_BACK: {
	    		AlertDialog.OnClickListener lsn = new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (which == AlertDialog.BUTTON_NEGATIVE)
							return;
						if (listListener.copying()) {
							dealCopyingOnExit();
						} else {
							FileManager.this.finish();
						}
					}
				};
	    		new AlertDialog.Builder(this).setMessage(getString(R.string.sure_exit))
	    			.setPositiveButton("\t确定\t", lsn).setNegativeButton("\t取消 \t", lsn)
	    				.create().show();
	    		return true;
	    	}
    	case KeyEvent.KEYCODE_SEARCH:
    		fileViewGrid.setDragable(true);
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	listListener.stopCopyService();
    }
    private void dealCopyingOnExit(){

		AlertDialog.OnClickListener lsn = new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == AlertDialog.BUTTON_NEGATIVE)
					return;
				listListener.cancelCopy();
				FileManager.this.finish();
			}
		};
		
		new AlertDialog.Builder(FileManager.this).setMessage(
				"有复制任务没完成,要取消复制吗?")
				.setPositiveButton(ok, lsn)
				.setNegativeButton(cancel, lsn)
				.create().show();
    }
    
    //public boolean outBound = false;
    /***/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
		if (appMenu.getVisibility() == View.VISIBLE) {
			int y = (int) event.getRawY();
			if (y < screen_height - appMenu.getHeight()) {
				hideAppMenu();
				return true;
			}
		}
    	return super.dispatchTouchEvent(event);
    }
    
    /**
     * 显示菜单栏, 重新实现的Option menu.
     * */
    private void showAppMenu() {
    	if (menuShowAnimation == null) {
    		menuShowAnimation = AnimationUtils
    				.loadAnimation(this, R.anim.menuhide);
    	}
    	appMenu.startAnimation(menuShowAnimation);
    	appMenu.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏菜单栏, 重新实现的Option menu.
     * */
    private void hideAppMenu() {
    	appMenu.setVisibility(View.INVISIBLE);
    	if (menuHideAnimation == null)
    		menuHideAnimation = AnimationUtils
    				.loadAnimation(this, R.anim.menushow);
    	appMenu.startAnimation(menuHideAnimation);
    }
    
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
    	int act = event.getAction();
    	int code = event.getKeyCode();
    	
    	
    	// 拖拉文件时, 左右键为标签转移
    	if (dragging){
    		if (act == KeyEvent.ACTION_DOWN){
    			switch (code){
    			case KeyEvent.KEYCODE_DPAD_LEFT:
    			case KeyEvent.KEYCODE_MENU:
    				nextTag(KeyEvent.KEYCODE_DPAD_LEFT);
    				return true;
    			case KeyEvent.KEYCODE_DPAD_RIGHT:
    			case KeyEvent.KEYCODE_BACK:
    				nextTag(KeyEvent.KEYCODE_DPAD_RIGHT);
    				return true;
    			default: break;
    			}
    		}
    	}
    	// app menu like option menu
    	if (code == KeyEvent.KEYCODE_MENU){
	    	if (act == KeyEvent.ACTION_DOWN){
	    		if (appMenu.getVisibility() == View.VISIBLE) {
	    			hideAppMenu();
	    		} else {
	    			showAppMenu();
	    		}
	    		return true;
	    	}
    	}else if (code == KeyEvent.KEYCODE_BACK){
    		if (appMenu.getVisibility() == View.VISIBLE) {
    			hideAppMenu();
    			return true;
    		}
    	}
    	
    	return super.dispatchKeyEvent(event);
    }
    
    private void initMountBtn() {
    	refreshMountStatus();
    	mountBtn.setClickable(true);
    	mountBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mountFsPerm(mounts.perm[mounts.index].equals(Mounts.RW) ? 
						Mounts.RO : Mounts.RW);
			}
		});
    }

    private int mountFsPerm(String perm) {
    	if (!isRoot()) {
    		Toast.makeText(this, "需要root权限", Toast.LENGTH_SHORT).show();
    		return 0;
    	}
    	int ret = 0;
    	DataOutputStream out = null;
    	BufferedReader br = null;
    	Process p = null;
    	try {
			p = linux.shell.exec("su\n");
			out = new DataOutputStream(p.getOutputStream());
			String cmd = "mount -o " + perm  + ",remount "  
							+ mounts.rawDev[mounts.index] + " "
			                + mounts.fs[mounts.index] + "\nexit\n";
			if (D) Log.d(tag, cmd);
			out.writeBytes(cmd);
			out.flush();
			ret = p.waitFor();
			if (ret != 0) {
				br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				Log.d(tag, "remount error");
				if (br.ready()) {
					Toast.makeText(this, br.readLine(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "重新装载remount出错", Toast.LENGTH_SHORT).show();
				}
				return ret;
			} else {
				if (D) Log.d(tag, "mount sucess"); 
			}
			mounts.perm[mounts.index] = perm; 
			showMount(mounts.index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			p.destroy();
		}
		return ret;
    }
    
    /** 刷新mount的属性*/
    private void refreshMountStatus() {
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		BufferedReader br = null;
		try {
			p = rt.exec("mount");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String content;
			String[] lines;
			while ( (content = br.readLine()) != null ) {
				lines = content.split(" +");
				for (int i = 0; i < mounts.fs.length; i++ ) {
					if (mounts.fs[i].equals(lines[2])) {
						if (lines[5].contains(Mounts.RO)) {
							mounts.perm[i] = Mounts.RO;
						} else {
							mounts.perm[i] = Mounts.RW;
						}
						mounts.rawDev[i] = lines[0];
						continue;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			p.destroy();
		}
    }
    private int previouSelected = 0;
    /**
     * 刷新当前目录的内容,
     * @param
     * 		path 要显示的目录路径
     * @param
     * 		gallery 当为false时不刷新显示当前路径的gallery, 否则刷新.
     * */
    public final void refreshPath(String path, boolean gallery){
    	if (!(new File(path).exists())){
    		Toast.makeText(this, "" + path + " 不存在", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	int s = 0;
    	previouSelected = fileView.getFirstVisiblePosition();
    	if (currentData.searchingTag)
    		return;
    	if (!historyString.contains(path))
    		historyString.add(path);
    	if (historyString.size() > MAX_PATH_TEMP){
    		historyString.remove(0);
    	}
    	if (gallery) {
    		s = 0;
    	}

    	clearFileSlected();
    	currentData.path = path;
    	currentTag.setText(Common.getPathName(path));
    	
    	fileAdapter.setCurrenPath(path);
    	// 清理图片文件的Bitmap
    	ArrayList<FileInfo> fInfos = currentFileInfo();
    	
        findFileInfo(path, fInfos);
		fileAdapter.notifyDataSetChanged();
		fileView.setSelection(s);
		if (gallery){
			refreshTextPath();
		}
		if (isRoot())
			for (int i = 0; i < mounts.fs.length; i++) {
				if (mounts.fs[i].equals(path) 
						|| ((i != 0) && (path.contains(mounts.fs[i])))) {
					showMount(i);
					mounts.index = i;
					return;
				}
			} 
		if (mountBtn.getVisibility() == View.VISIBLE) {
			hideMount();
		} 
    }
    public String getCurrentDirPerm() {
    	if (mounts.index == -1)
    		return null;
    	return mounts.perm[mounts.index];
    }
    private void showMount(int i) {
    	if (mountBtn.getVisibility() != View.VISIBLE)
        	mountBtn.setVisibility(View.VISIBLE);
    	mountBtn.setText(" " + mounts.perm[i] + " ");
    	mounts.index = i;
    }
    
    private void hideMount() {
    	mountBtn.setVisibility(View.GONE);
    	mounts.index = -1;
    }
    
    private void refreshTextPath(){
    	String path = currentPath();
    	if (!path.equals("/")) {
        	pathBarString = path.split("/");
        	pathBarString[0] = "/";
    	}
    	pathAdapter.setPathArray(pathBarString);
    	pathAdapter.setAbsolutePath(path);
		pathAdapter.notifyDataSetChanged();
		pathScroll.setSelection(pathBarString.length - 1);
    }
    
    /** 
     * Find all files in <em>path<em>, and set up fit file informations 
     * @param path 
     * 		the file path.
     * @param list
     * 		File information, it will be clear, and set new informaiton.
     * @throws IOException 
     * */
	private void findFileInfo(String path, List<FileInfo> list){
/**
    	int size = list.size();
    	FileInfo info;
    	//手动清除(recycle)Bitmap
    	for (int i = 0; i < size; i++) {
    		info = list.get(i);
			if (info.type == FileListAdapter.PHOTO){
				info.recycleBitmap(i);
			}
    	}**/
    	synchronized (list) {
			list.clear();
			// fileAdapter.notifyDataSetChanged();
			/***/
			if (preIsRoot == false) {
				File base = new File(path);
				File[] files = base.listFiles();
				if (files == null || files.length == 0)
					return;
				String name;
				String suffix;
				int length = files.length;
				for (int i = 0; i < length; i++) {
					name = files[i].getName();

					if (preHideFile && files[i].isHidden()) {
						continue;
					}
					// date = new Date(files[i].lastModified());
					int la = name.lastIndexOf('.');
					if (la == -1)
						suffix = null;
					else
						suffix = name.substring(la + 1).toLowerCase();
					list.add(new FileInfo(name, files[i].getAbsolutePath(),
							switchIcon(suffix, files[i]), null, // fileSize(files[i].length()),
							files[i].isDirectory()));			// //date.toLocaleString(),
							
				}
			} else {
				/** 带 root */
				BufferedReader errReader = null, reader = null;
				DataOutputStream in = null;
				Process p = null;
				try {
					p = linux.shell.exec("su");
					errReader = new BufferedReader(new InputStreamReader(
							p.getErrorStream()));
					in = new DataOutputStream(p.getOutputStream());
					String cmd = new String("ls -A " + "\"" + path + "/\"\nexit\n");
					in.write(cmd.getBytes());
					in.flush();
					in.close();
					reader = new BufferedReader(new InputStreamReader(
							p.getInputStream()));
					if (p.waitFor() != 0) {
						Toast.makeText(FileManager.this, errReader.readLine(),
								Toast.LENGTH_LONG).show();
						return;
					}
					String sr;
					while ((sr = reader.readLine()) != null) {
						String pt;
						if (currentPath().length() != 1)
							pt = currentPath() + "/" + sr;
						else
							pt = currentPath() + sr;
						File fl = new File(pt);
						if (preHideFile && fl.isHidden())
							continue;
						int type, ps;
						boolean directory = fl.isDirectory();
						if (directory) {
							type = FileListAdapter.DIRECTORY;
						} else {
							ps = sr.lastIndexOf('.');
							if (ps == -1)
								type = FileListAdapter.UNKNOW;
							else
								type = switchIcon(sr.substring(ps + 1), fl);
						}
						list.add(new FileInfo(sr, pt, type, null, directory));
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					p.destroy();
				}
			}
			Collections.sort(list);
    	}
    	/**/
    }
	
	// 对应文件后缀的图标序号
	public static final int switchIcon(String name, File file){
		if (file.isDirectory())
			return FileListAdapter.DIRECTORY;
		if (name == null){
			return FileListAdapter.UNKNOW;
		}
		name = name.toLowerCase();
		if (name.equals("txt") || name.equals("doc") || name.equals("pdf")) {
			return  FileListAdapter.TXT;
		} else if (name.equals("html") || name.equals("htm") ||
				name.equals("chm") || name.equals("xml")){
			return  FileListAdapter.HTM;
		} else if (name.equals("jpeg") || name.equals("jpg") ||
				name.equals("bmp") || name.equals("gif") || name.equals("png")){
			return  FileListAdapter.PHOTO;
		} else if (name.equals("rmvb") || name.equals("rmb") || 
				name.equals("avi") || name.equals("wmv") || name.equals("mp4")
				|| name.equals("3gp") || name.equals("flv")){
			return FileListAdapter.MOVIE;
		} else if (name.equals("mp3") || name.equals("wav") || name.equals("wma")){
			return FileListAdapter.MUSIC;
		} else if (name.equals("apk")){
			return FileListAdapter.APK;
		} else if (name.equals("zip") || name.equals("tar") ||
				name.equals("bar") || name.equals("bz2") || name.equals("bz")
				|| name.equals("gz") || name.equals("rar")) {
			return FileListAdapter.ZIP;
		}
		return FileListAdapter.UNKNOW;
	}
	

    
    
    
    //GridMenuApdater menuApdater;
    private ImageButton viewStyleButton;
    private TextView viewStyleTextView;
    //private TextView rootTextView;
    //private TextView ddTextView;
    private void initAppMenu(){
    	appMenu = (LinearLayout)findViewById(R.id.appmenu);
    	LinearLayout row = (LinearLayout) appMenu.findViewById(R.id.approw1);
    	LayoutInflater infl = getLayoutInflater();
    	View.OnClickListener ocl = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appMenuClick(v.getId());
				hideAppMenu();
			}
		};
		
		int[] drRes = {R.drawable.newfolder, R.drawable.newfile, R.drawable.paste,
				R.drawable.search, R.drawable.dialog, R.drawable.apkmanager,
				R.drawable.setting, R.drawable.multicon, R.drawable.filelib,
				R.drawable.close};
		String[] names = getResources().getStringArray(R.array.appnames);
		for (int i = 0; i < 10; i++){
			if (i == 5) {
				row = (LinearLayout) appMenu.findViewById(R.id.approw2);
			}
			RelativeLayout rl = (RelativeLayout) infl.inflate(R.layout.appmenuitem,
					null);
			ImageButton iv = (ImageButton) rl.findViewById(R.id.menuicon);
			//iv.setImageResource(drRes[i]);
			iv.setBackgroundResource(drRes[i]);
			TextView tv = (TextView) rl.findViewById(R.id.menuname);
			tv.setText(names[i]);
			iv.setId(i);
			iv.setOnClickListener(ocl);
			row.addView(rl);
			if ( i == MENU_SET_VIEW_STYLE){
				viewStyleButton = iv;
				viewStyleTextView = tv;
			}
		}
    	
    }
    
    SearchInputDialog.onSearchListener onSL = new SearchInputDialog.onSearchListener() {
		
		@Override
		public void onSearch(String expr, boolean allMatch, boolean caseSense) {
			// TODO Auto-generated method stub
			if (expr.contains("/")) {
				Toast.makeText(FileManager.this, getString(R.string.key_word_can_not_contain),
						Toast.LENGTH_SHORT).show();
			} else {
				new Thread(new SearchFileThread(expr, allMatch, caseSense)).start();
			}
		}
	};
    private void initSearchStop() {
    	searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (searching) {
					searching = false;
					if (searchProcess != null) {
						searchProcess.destroy();
						searchProcess = null;
					}
				} else {
					currentData.searchingTag = false;
					searchLayout.setVisibility(View.INVISIBLE);
					refreshPath(currentPath(), true);
				}
			}
		});
    }
    private Process searchProcess = null;
    private final static String SEARCHFILEPATH = "SEARCHFILEPATH";
    private ArrayList<FileInfo> searchFileInofs; 
    class SearchFileThread implements Runnable {
    	private String expr;
    	private boolean caseSense, allMatch;
    	public SearchFileThread(String expr, boolean allMatch, boolean caseSense) {
			// TODO Auto-generated constructor stub
    		this.expr = expr;
    		this.allMatch = allMatch;
    		this.caseSense = caseSense;
		}
    	@Override
    	public void run(){
    		Message msg;
    		File parentDir = new File(currentPath());
        	ArrayList<FileInfo> list = currentFileInfo();
        	searchFileInofs = list;
        	list.clear();
        	currentData.selectedId.clear();
    		listViewHandler.sendEmptyMessage(HANDLER_LIST_ADPATER_CHANGED);
    		searchDir = currentPath();
    		listViewHandler.sendEmptyMessage(HANDLER_SET_SEARCHDIR);
    		searching = true;
    		currentData.searchingTag = true;
    		listViewHandler.sendEmptyMessage(HANDLER_SET_SEARCH_VISIBLE);
    		String exprs;
			if (!allMatch) {
				if (!preIsRoot) {
					if (!expr.contains("*"))
						exprs = ".*" + expr + ".*";
					else
						exprs = expr.replace("*", ".*");
				} else {
					exprs = "*" + expr + "*";
				}
			} else {
				exprs = expr;
			}
	    	if (isRoot()) {
	    		listViewHandler.sendEmptyMessage(HANDLER_SET_SEARCHDIR);
	    		try {
	    			searchProcess = linux.shell.exec("su");
					DataOutputStream out = new DataOutputStream(searchProcess.getOutputStream());
					BufferedReader in = new BufferedReader(
							new InputStreamReader(searchProcess.getInputStream()));
					String cases = "-iname";
					if (caseSense) 
						cases = "-name";
					String cmd = "find " + searchDir + "/ " + cases 
									+ " \"" + exprs + "\" 2> /dev/null\nexit\n" ;
					if (D) Log.d(tag, cmd); 
					out.write(cmd.getBytes());
					out.flush();
					
					while ((cmd = in.readLine()) != null) {
			    		if (!searching)
			    			break;
						if (preHideFile && cmd.startsWith(".")) {
							continue;
						}
						//if (D) Log.d(tag, cmd); 
						Bundle b = new Bundle();
						b.putString(SEARCHFILEPATH, cmd);
						msg = listViewHandler.obtainMessage(HANDLER_ADD_LIST_ITEM);
						msg.setData(b);
						listViewHandler.sendMessage(msg);
					}
					/**
					 * file = new File(cmd);
						name = file.getName();
						int la = name.lastIndexOf('.');
						if (la == -1)
							suffix = null;
						else
							suffix = name.substring(la + 1).toLowerCase();
						Bundle b = new Bundle();
						
		    			synchronized (newInfo) {
			    			FileInfo info = new FileInfo(
									name,
									file.getAbsolutePath(),
									switchIcon(suffix, file),
									null, // fileSize(files[i].length()),
											// //date.toLocaleString(),
									file.isDirectory());
				    		newInfo.setNewInfo(info);
				    		Binder b = new Binder();
				    		
				    		listViewHandler.sendEmptyMessage(HANDLER_ADD_LIST_ITEM);
			    			FileManager.dbg("--- " + info.name());
			    		}
					 * */
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "su find", e);
				} finally {
					searchProcess.destroy();
					searchProcess = null;
				}
	    	} else {
	    		doSearchFile(list, parentDir, exprs, caseSense);
	    	}
    		listViewHandler.sendEmptyMessage(HANDLER_SEARCHBAR_HIDE);
    		searching = false;
    		searchDir = getString(R.string.done_search, expr);
    		listViewHandler.sendEmptyMessage(HANDLER_SET_SEARCHDIR);
    				
    	}
    }
    
    private CharSequence searchDir = "";
    private boolean searching = false;
    private void doSearchFile(ArrayList<FileInfo> list, File dir,
    			String expr, boolean caseSense) {
    	CharSequence tmpText = searchText.getText();
    	File[] files = dir.listFiles();
    	if (files == null) {
    		return;
    	}
    	int length = files.length;
		Message msg;
		String name;
		String lowercaseName = "";
		if (!caseSense)
			expr = expr.toLowerCase();
		searchDir = dir.getAbsolutePath();
		listViewHandler.sendEmptyMessage(HANDLER_SET_SEARCHDIR);
    	for (int i = 0; i < length; i++){
    		if (!searching)
    			return;
			if (preHideFile && files[i].isHidden()) {
				continue;
			}
			name = files[i].getName();

			if (!caseSense) {
				lowercaseName = name.toLowerCase();
			} else {
				lowercaseName = name;
			}
			if (lowercaseName.matches(expr)) {
				Bundle b = new Bundle();
				b.putString(SEARCHFILEPATH, files[i].getAbsolutePath());
				msg = listViewHandler.obtainMessage(HANDLER_ADD_LIST_ITEM);
				msg.setData(b);
				listViewHandler.sendMessage(msg);
			}
    		if (files[i].isDirectory())
    			doSearchFile(list, files[i], expr, caseSense);
    	}
    	searchDir = tmpText;
		listViewHandler.sendEmptyMessage(HANDLER_SET_SEARCHDIR);
    }
    /**
     * 
			// date = new Date(files[i].lastModified());
			int la = name.lastIndexOf('.');
			if (la == -1)
				suffix = null;
			else
				suffix = name.substring(la + 1).toLowerCase();
			try {
				if (!caseSense) {
					lowercaseName = name.toLowerCase();
				} else {
					lowercaseName = name;
				}
	    		if (lowercaseName.matches(expr)) {
	    			//Log.d(tag, "add");
	    			synchronized (newInfo) {
	    			FileInfo info = new FileInfo(
								name,
								files[i].getAbsolutePath(),
								switchIcon(suffix, files[i]),
								null, // fileSize(files[i].length()),
										// //date.toLocaleString(),
								files[i].isDirectory());
		    			newInfo.setNewInfo(info);
		    			listViewHandler.sendEmptyMessage(HANDLER_ADD_LIST_ITEM);
	    			}
	    		}
			} catch (PatternSyntaxException e) {
				e.printStackTrace();
			}
     * */
    /**
     * 菜单面板
     * */
	private void appMenuClick(int whitch) {
		switch (whitch){
		case MENU_CREATE_DIRECTORY:
			listListener.onClick(null, MENU_ITEM_CREATE_DIRECTORY);
			break;
		case MENU_CREATE_FILE:
			listListener.onClick(null, MENU_ITEM_CREATE_FILE);
			break;
		case MENU_PASTE:
			listListener.onClick(null, MENU_ITEM_PASTE);
			break;
		case MENU_SEARCH:
			/**
			String expr = "sdfd";
			expr = expr.replace("*", ".*");
			searchFile("an");
			/**/
			if (searching) {
				Toast.makeText(this, getString(R.string.searching), 
						Toast.LENGTH_SHORT).show();
				break;
			}
			SearchInputDialog sid = new SearchInputDialog(this);
			sid.setOnSearchListener(onSL);
			sid.show();
			break;
		case MENU_FINISH_ACTIVITY:
			if (listListener.copying())
				dealCopyingOnExit();
			else
				this.finish();
			break;
		case MENU_SHOW_COPY_DIALOG:
			listListener.showHiddenCopyDialog();
			break;
			
		case MENU_APK_MANAGER:
			Intent intent = new Intent();
			intent.putExtra(PRE_BACKUP_DIR, preBackupDir);
			/**
			intent.setClassName("com.xjf.filedialog",
					"com.xjf.filedialog.XPackageManager");
					/**/
			intent.setAction("com.xjf.apk.EDIT");
			this.startActivity(intent);
			break;
		case MENU_SETTING:
			/**/
			
			if (settingsView == null) {
				settingsHide = true;
				settingsView = new SettingsView(this);
			}
			if (settingsHide == false) {
				settingsHide = true;
				hideSettingsView();
			} else {
				settingsHide = false;
				settingsView.show(null);
			}
			/**/
			break;
		case MENU_FILE_LIB:
			fileLibDialog.doWhat = FileLibDialog.FILE_LIB_OPEN;
			fileLibDialog.show();
			break;
		case MENU_SET_VIEW_STYLE:
			if (preViewStyle == FileListAdapter.STYLE_LIST) {
				setFileViewStyle(FileListAdapter.STYLE_GRID);
			} else {
				setFileViewStyle(FileListAdapter.STYLE_LIST);
			}
			break;
		default: break;
		}
		
	}
	
	/** 改变ROOT权限*/
	public boolean changedRoot(boolean r) {
		/* 尝试获取root权限  */
		if (!r) {
			preIsRoot = false;
			return false;
		}
		try {
			if (LinuxShell.isRoot(Runtime.getRuntime(), 50)){
				preIsRoot = true;
				
				Toast.makeText(FileManager.this, "Root Success", Toast.LENGTH_LONG)
				.show();
			} else {
				preIsRoot = false;
				Toast.makeText(FileManager.this, "Root Fail", Toast.LENGTH_LONG)
					.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return preIsRoot;
	}
	
	/** 隐藏设置界面*/
	void hideSettingsView() {
		settingsHide = true;
		settingsView.hide();
	}
    /**
    private void rootCopy(){
    	if (pre_IsRoot){
    		listListener.rootCopy();
    	} else {
    		Toast.makeText(this, getString(R.string.no_root), Toast.LENGTH_SHORT)
    		.show();
    	}
    }
/**/
    /** Find child views */
    private void findView(){
    	fileViewList = (DDListView)findViewById(R.id.filelist);
    	fileViewGrid = (DDGridView)findViewById(R.id.filegrid);
        pathScroll = (HorizontalListView)findViewById(R.id.pathgallery);
        optUp = (ImageView)findViewById(R.id.optup);
        optHistory = (ImageView)findViewById(R.id.opthistory);
        optTag = (ImageView)findViewById(R.id.opttag);
        optRefresh = (ImageView)findViewById(R.id.optrefresh);
        optMultfile = (ImageView)findViewById(R.id.optmultfile);
        optMenu = (ImageView)findViewById(R.id.optmenu);
        
        tagLayout = (RelativeLayout)findViewById(R.id.tab);
        addTagButton = (ImageView)findViewById(R.id.addtag);
        tagRow = (TableRow)findViewById(R.id.tabrow);
        
        //hsv = (HorizontalScrollView) findViewById(R.id.tabs);
        
        //Search View
        searchLayout = (LinearLayout) findViewById(R.id.searchlayout);
        searchBar = (ProgressBar) searchLayout.findViewById(R.id.searchbar);
        searchText = (TextView) searchLayout.findViewById(R.id.searchpath);
        searchBtn = (ImageButton) searchLayout.findViewById(R.id.searchclose);
        
        mountBtn = (Button) findViewById(R.id.mount);
        
    }

    private void newObject() {
    	DisplayMetrics dm = res.getDisplayMetrics();
        scale = dm.densityDpi;
        linux = new LinuxFileCommand(Runtime.getRuntime());
        historyString = new ArrayList<String>();
        copyDialogLock = new ConditionVariable(false);
        
        datas = new ArrayList<FileData>();
        currentData = new FileData(new ArrayList<FileListAdapter.FileInfo>(), 
        		null, SDCARD_PATH);
        
/**
		String ps = null;
		String cs = null;
		ArrayList<String> al;
		for (int i = 0; i < 3; i++) {
			al = new ArrayList<String>();
			ps = "p" + i;
			parentList.add(ps);
			childList.add(al);
			for (int j = 0; j < 5; j++) {
				cs = "c" + i + j;
				al.add(cs);
			}
			al.add("/sdcard");
		
		}
		/**/
    }
    
    private void initSelectedFile() {
    	
    	selectedLayout.setVisibility(View.VISIBLE);
    	String path = getIntent().getStringExtra(EXTR_GET_NAME);
	    if (path != null) {
	    	if (selectedAct == GET_FILES && !path.equals("")) {
	    		path = path + ",";
	    	}
	    	selectedEdit.setText(path);
	    }
    	selectedEdit.clearFocus();
    	
    	selectedBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fs = selectedEdit.getText().toString();
				if (fs.equals("")) {
					Toast.makeText(FileManager.this, "文件各不能为空,取消请退出"
							, Toast.LENGTH_SHORT).show();
					return;
				}
				Intent data = new Intent();
				if (selectedAct == GET_FILES) {
					String[] files = fs.split(",");
					data.putExtra(EXTR_GET_NAME, files);
					String parent = currentPath();
					for (int i = 0; i < files.length; i++) {
						files[i] = parent + "/" + files[i];
						Log.d(tag, files[i]);
					}
				} else {
					fs = currentPath() + "/" + fs;
					if (selectedAct == GET_DIRECTORY) {
						File f = new File(fs);
						if (f.exists() && f.isFile()) {
							Toast.makeText(FileManager.this, "文件不是目录"
									, Toast.LENGTH_SHORT).show();
							return;
						}
					}
					data.putExtra(EXTR_GET_NAME, fs);
				}
				setResult(RESULT_OK, data);
				FileManager.super.finish();
			}
		});
    }
    

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  
            Intent data){  
    	switch (requestCode){
    	case GET_DIRECTORY:
        	if (resultCode == RESULT_OK) {
        		settingsView.setBackupDir(data.getStringExtra(EXTR_GET_NAME));
        	}
        	selectedAction = false;
    		break;
    	}
    }
    
    public void finish() {
    	if (selectedAction) {
    		this.finishActivity(RESULT_CANCELED);
    	}
    	super.finish();
    }
    
    
    public void onConfigurationChanged(Configuration newConfig) {
    	//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	super.onConfigurationChanged(newConfig);
    }
    
}


/**
 * 当前文件的数据包
 * */
class FileData {
	public ArrayList<FileInfo> fileInfos;
	public ArrayList<Integer> selectedId;
	public String path;
    public boolean searchingTag = false;
	public FileData(ArrayList<FileInfo> fileInfos, ArrayList<Integer> selectedId,
			String path) {
		if (fileInfos == null)
			this.fileInfos = new ArrayList<FileListAdapter.FileInfo>();
		else
			this.fileInfos = fileInfos;
		if (selectedId == null)
			this.selectedId = new ArrayList<Integer>();
		else
			this.selectedId = selectedId;
		if (path == null)
			this.path = "/sdcard";
		else
			this.path = path;
	}
}
/**/

/**/
