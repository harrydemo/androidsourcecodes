package com.sly.android.huangcun.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import com.sly.android.huangcun.entrey.BookMark;
import com.sly.android.huangcun.entrey.ConnectionProvider;
import com.sly.android.huangcun.entrey.MyBookPageFactory;
import com.sly.android.huangcun.widget.MyPageWidget;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
/**
 * 屏幕分辨率:各种型号
 * hmg25's android Type
 *
 *@author SunLuyao
 *
 */
public class StaringAct extends Activity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Called when the activity is first created. */
	private MyPageWidget mPageWidget;
	Context cx;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	MyBookPageFactory pagefactory;
	private String TABLE_NAME = "bookmark";
	private int id;
	private String bookPath;
	private String filenameString;
	private DisplayMetrics dm;
	private String TAG = "StaringAct";
	private ActivityManager am;
	private EditText bookEdit;
	public int bookName =0;
	public int fileNameSingle =0;
	ConnectionProvider cp;
	List<BookMark> listBook = new ArrayList<BookMark>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdManager.init(StaringAct.this,"0f34f539f9d030b4","612c3c56a1fab5c1",50,false); 
		mPageWidget = new MyPageWidget(this);
		setContentView(mPageWidget);
		am = ActivityManager.getInstance();
		am.addActivity(this);
		AdView adView = new AdView(this); 
		cp = new ConnectionProvider(this);
		FrameLayout.LayoutParams params = new 
		FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 
		FrameLayout.LayoutParams.WRAP_CONTENT); 
		params.gravity=Gravity.BOTTOM|Gravity.RIGHT;  
		addContentView(adView, params);  
		dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);    
		int width=dm.widthPixels;
		int height=dm.heightPixels;
		mCurPageBitmap = Bitmap.createBitmap(width, height-50, Bitmap.Config.ARGB_8888);//当前页面大小
		mNextPageBitmap = Bitmap
				.createBitmap(dm.widthPixels,dm.heightPixels-50, Bitmap.Config.ARGB_8888);//下一页
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		
		pagefactory = new MyBookPageFactory(width,height);
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.bookback));
		try {
			Bundle bud=this.getIntent().getExtras();
			id=bud.getInt("id");
			
			copy(id);
			bookPath="/data/data/com.sly.android.huangcun.ui/files/"+id+".txt";
			pagefactory.openbook(bookPath);
				pagefactory.onDraw(mCurPageCanvas);
			
		} catch (IOException e1) {
			e1.printStackTrace();
			Toast.makeText(this, "电子书不存在",
					Toast.LENGTH_SHORT).show();
		}
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		fileNameSingle=bundle.getInt("id");
		bookName=bundle.getInt("id");
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				boolean ret=false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());

						pagefactory.onDraw(mCurPageCanvas);
						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}						
							if(pagefactory.isfirstPage())
								return false;
							pagefactory.onDraw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if(pagefactory.islastPage())return false;
							pagefactory.onDraw(mNextPageCanvas);
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}
                 
					 ret = mPageWidget.doTouchEvent(e);
					return ret;
				}
				return false;
			}

		});
	}

	private void copy(int id) {
		try {
			String filePath="/data/data/com.sly.android.huangcun.ui/files/";
			File file=new File(filePath);
			if(!file.exists()){
				file.mkdir();
			}
			AssetManager assetManage=this.getAssets();
			if(!new File(filePath+id+".txt").exists()){
				InputStream in=assetManage.open(id+".jpg");
				BufferedInputStream bis = new BufferedInputStream(in);
				 BufferedOutputStream bos = new BufferedOutputStream(
				 new FileOutputStream(filePath+id+".txt"));
				 byte[] buffer = new byte[8192];
				 int length = 0;
				 while ((length = (bis.read(buffer))) > 0) {
				 bos.write(buffer, 0, length);
				 }
				 bis.close();
				 bos.close();
				 
			}	
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			
	}

	public String getStringFromFile(String code)
	{
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(filenameString);
			InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if(!new File(filenameString).exists())
			{
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// 读取文件内容
	public byte[] readFile(String fileName) throws Exception {
		byte[] result = null;
		FileInputStream fis = null;
		try {
			File file = new File(fileName);
			fis = new FileInputStream(file);
			result = new byte[fis.available()];
			fis.read(result);
		} catch (Exception e) {
		} finally {
			fis.close();
		}
		return result;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加书签");
		menu.add(0, 2, 2, "选择书签");
//		menu.add(0, 3, 3, "背景音乐");
		menu.add(0, 4, 4, "返回章节");
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			addBookMark();
			break;
		case 2:
			bookMark();
			break;
//		case 3:
//			startActivity(new Intent(this, MusicManager.class));
//		break;
		case 3:
			AlertDialog.Builder adb2 = new Builder(StaringAct .this);
			adb2.setTitle("消息");
			adb2.setMessage("真的要退出吗？");
			adb2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					am.exitAllProgress();
				}
			});
			adb2.setNegativeButton("取消", null);
			adb2.show();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}


	@Override
	protected void onResume() {
		super.onResume();
		MusicManager.play(this, R.raw.py);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MusicManager.stop(this);
	}
//TODO添加书签
	private void addBookMark() {
		// 初始化用于接收书签名的视图
		Log.i(TAG, "-----------------------------添加开始");

		bookEdit = new EditText(this.getBaseContext());

		new AlertDialog.Builder(this).setTitle("添加书签").setView(bookEdit)
				.setView(bookEdit)
				.setPositiveButton("确定", new OnClickListener() {

					
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "-----------------------------点击开始");

						if (bookEdit.getText().toString() != null
								&& bookEdit.getText().toString().trim() != "") {
							SQLiteDatabase db = cp.getConnection();
							int bookName1 = fileNameSingle;
							String bookMarkName = bookEdit.getText().toString();

							for (BookMark bm : listBook) {
								Log.i(TAG, "------------xx" + bm);

								if (bookMarkName.equals(bm.getBookmarkName())) {
									Toast.makeText(StaringAct.this,
											"此书签已经存在，请取别的名字",
											Toast.LENGTH_SHORT).show();
									return;
								}
							}
							int page = pagefactory.getPage();
							int bookmarkID = 0;

							bookmarkID = pagefactory.getBeginNum();
							String sql = "insert into "
									+ TABLE_NAME
									+ " (bookmarkID,bookmarkName,bookPage,bookName) values("
									+ bookmarkID + ",'" + bookMarkName + "',"
									+ page +",'"+bookName1+ "');";
System.out.println("sql====================="+sql);
							Log.i(TAG, "------------" + sql);
							db.execSQL(sql);
							queryTable(cp, fileNameSingle);
							
							for (BookMark bm : listBook) {
								Log.i(TAG, "------------" + bm);
							}
						}
					}
				}).setNegativeButton("取消", new OnClickListener() {

					
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(StaringAct.this, "未添加书签",
								Toast.LENGTH_SHORT);

					}
				}).show();

	}
	public void queryTable(ConnectionProvider cp,int fileNameSingle) {
		Log.i(TAG, "-----------------------" + "query bookmark table");

		String str = "select * from " + TABLE_NAME;
		Cursor cursor = null;
		listBook.clear();
		try {
			cursor = cp.getConnection().rawQuery(str, null);
			if (cursor != null) {
				cursor.moveToFirst();
				while (true) {
					Log.v("login active", "outp.");
					int bookmarkID = cursor.getInt(0);
					String bookmarkName = cursor.getString(1);
					int bookPage = cursor.getInt(2);
					int bookName = cursor.getInt(3);
					BookMark bm = new BookMark();
					bm.setBookmarkID(bookmarkID);
					bm.setBookmarkName(bookmarkName);
					bm.setBookPage(bookPage);
					bm.setBookName(bookName);
					if (bookName==fileNameSingle)
						listBook.add(bm);
					
					if (cursor.isLast()) {
						break;
					}
					cursor.moveToNext();
				}
				Log.i(TAG, "----------------listBook-" + listBook.size());
			}
			Log.i(TAG, "-----------------------" + "bookmark查询结束");

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			if (cursor != null) {
				cursor.close();
			}
			Log.i(TAG, "-----------------------" + "bookmark查询关闭");
		}
	}
	//TODO 添加书签
	String[] books;
	private void bookMark() {
		queryTable(cp, fileNameSingle);
		books = new String[listBook.size()];
		for (int i = 0; i < listBook.size(); i++) {
			books[i] = listBook.get(i).getBookmarkName();
		}
		new AlertDialog.Builder(this)
				.setTitle("单选框")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(books, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String selectBook = books[which];
								for (BookMark bm : listBook) {
									if (bm.getBookmarkName().equals(selectBook)) {
										int page = bm.getBookPage();
										try {
											Log.i(TAG,
													"--------------------------getBookmarkID"
															+ bm.getBookmarkID());

											setNewView(page, bm.getBookmarkID());

											// mPageWidget.invalidate();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();

	}
	private void setNewView(int page, int bookmarkID) throws IOException {
		pagefactory.toSelectPage(page, bookmarkID);
		mPageWidget.invalidate();
		pagefactory.onDraw(mCurPageCanvas);
		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		// pagefactory.onDraw(mNextPageCanvas);
	}

	}












