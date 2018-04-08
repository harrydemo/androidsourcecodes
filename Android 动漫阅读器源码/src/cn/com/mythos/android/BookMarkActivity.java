package cn.com.mythos.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mythos.android.Contents.BookMark;
import cn.com.mythos.android.Contents.Contents;
import cn.com.mythos.android.Contents.Utils;
import cn.com.mythos.touhoucartoonreader.R;

public class BookMarkActivity extends BaseActivity{
	private ListView bookmarkList;
	private ImageButton goBack;
	List<Map<String, Object>> list;
	Map<String, Object> picInfo;
	private Intent intent;
	private Map<String, String> picMap;
	private Bundle bundle;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bookmark_list);
		bookmarkList = (ListView) findViewById(R.id.bookmarkList);
		goBack = (ImageButton) findViewById(R.id.goBack);
		goBack.setOnClickListener(goBackListener);
		setBookMarkAdapter();
	}
	
	//返回图片路径，该路径是访问当前窗口之前MainActivity正在浏览的图片
	private String getPicPath() {
		bundle = this.getIntent().getExtras();
		if(bundle != null && bundle.size() > 0) {
			return bundle.getString("picPath");
		}
		return null;
	}
	
	//书签适配器
	private void setBookMarkAdapter() {
		list = getBookMarkList();
		bookmarkList.setAdapter(new ImageAdapter(list, BookMarkActivity.this));
		bookmarkList.setOnItemClickListener(goBookMarkInfo);
	}
	
	//获取图片内容
	private List<Map<String, Object>> getBookMarkList() {
		List<BookMark> bookMarks = getFileContent();
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(BookMark bookMark : bookMarks) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imagePath", bookMark.getImagePath());
			map.put("bookMarkName", bookMark.getBookMarkName());
			map.put("saveTime", bookMark.getSaveTime());
			map.put("imageId", bookMark.getImageId());
			list.add(map);
		}
		return list;
	}
	
	//将书签信息转化为BookMark对象存到列表中
	public List<BookMark> getFileContent() {
		List<BookMark> list = new ArrayList<BookMark>();
		try{
			String content = Utils.getFileRead(Contents.BOOKMARKS).trim();
			if(content != null && !"".equals(content)) {
				String[] bookMarks = content.split(";");
				for(int i = 0;i < bookMarks.length;i++) {
					BookMark bm = new BookMark();
					String book = bookMarks[i];
					bm.setBookMarkName(book.substring(0, book.indexOf("|")));
					bm.setSaveTime(book.substring(book.indexOf("|") + 1, book.indexOf(",")));
					bm.setImagePath(book.substring(book.indexOf(",") + 1, book.indexOf("#")));
					bm.setImageId(book.substring(book.indexOf("#") + 1, book.length()));
					list.add(bm);
					bm = null;
				}
			}else {
				showMsg(R.string.nobookmark);
			}
		}catch (Exception e) {
			
		}
		return list;
	}
	
	//自定义书签布局
	public class ImageAdapter extends BaseAdapter {
		Context context;
		List<Map<String, Object>> imagesList;
		public ImageAdapter(List<Map<String, Object>> list, Context context) {
			this.imagesList = list;
			this.context = context;
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return imagesList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			RelativeLayout view = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_bookmark, null);
			ImageView imagePath = (ImageView) view.findViewById(R.id.imagePath);
			TextView bookMarkName = (TextView) view.findViewById(R.id.bookMarkName);
			TextView saveTime = (TextView) view.findViewById(R.id.saveTime);
			TextView imageId = (TextView) view.findViewById(R.id.imageId);
			if(convertView != null) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = 2;
				Bitmap bitmap = null;
				picMap = (Map)imagesList.get(position);
				String picPath = picMap.get("imagePath");
				bitmap = BitmapFactory.decodeFile(picPath, opts);
				imagePath.setImageBitmap(bitmap);
				bookMarkName.setText(picMap.get("bookMarkName"));
				saveTime.setText(picMap.get("saveTime"));
				int page = Integer.parseInt(picMap.get("imageId")) + 1;
				String str = "" + page;
				imageId.setText("上次阅览第" + str+ "页");
			}
			return view;
		}
		
	}
	
	//显示书签列表
	private ListView.OnItemClickListener goBookMarkInfo = new ListView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			picInfo = list.get(position);
			showDialog();
		}
	};
	
	//创建对话框
	public void showDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(BookMarkActivity.this);
		dialog.setTitle(R.string.bookmarkSetUp);
		dialog.setPositiveButton(R.string.picCancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setItems(Contents.BOOKMARKARRAY, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						if(picInfo != null) {
							String imagePath = (String) picInfo.get("imagePath");
							intent = new Intent (BookMarkActivity.this, MainActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("picPath", imagePath);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}
						break;
					case 1:
						if(picInfo != null) {
							String bookMarkName = (String) picInfo.get("bookMarkName");
							Utils.removeBookMarkByName(bookMarkName);
							setBookMarkAdapter();
						}
						break;
					case 2:
						AlertDialog.Builder myDialog = new AlertDialog.Builder(BookMarkActivity.this);
						myDialog.setTitle(R.string.bookmarkDeleteAll);
						myDialog.setPositiveButton(R.string.bookmarkSubmit, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								Utils.removeAllBookMarks();
								setBookMarkAdapter();
							}
						});
						myDialog.setNegativeButton(R.string.bookmarkCancel, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						myDialog.show();
						break;
				}
			}
		});
		dialog.show();
	}
	
	//信息提示
	public void showMsg(int id) {
		Toast.makeText(BookMarkActivity.this, id, Toast.LENGTH_SHORT);
	}
	
	//返回主页
	public View.OnClickListener goBackListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String picPath = getPicPath();
			Intent intent = new Intent(BookMarkActivity.this, MainActivity.class);
			if(picPath != null) {
				bundle = new Bundle();
				bundle.putString("picPath", picPath);
				intent.putExtras(bundle);
			}
			startActivity(intent);
			finish();
		}
	};
	
}
