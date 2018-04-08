package wyf.wpf;
import static wyf.wpf.ConstantUtil.IMAGESWITCHER_HEIGHT;
import static wyf.wpf.ConstantUtil.PHOTO_HEIGHT;
import static wyf.wpf.ConstantUtil.PHOTO_WIDTH;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlbumActivity extends Activity implements ViewSwitcher.ViewFactory{
	List<String []> photoInfoList = new ArrayList<String []>();
	Bitmap [] photoList;			//存放图片的数组
	Gallery gl = null;				//Gallery对象的引用
	ImageSwitcher is = null;		//ImageSwitcher对象的引用
	Spinner sp = null;				//Spinner对象的引用
	MyConnector mc = null;			//MyConnector对象的引用
	String xid = "";				//存放相册的ID
	String uno = "";				//存放用户ID
	String visitor = "";			//存放访问者的ID
	String pid = "";				//存放当前显示的照片ID
	int from = -1;					//启动该Activity的来源，0为MyAlbumListActivity，1为AlbumListActivity 
	List<String []> albumInfoList = new ArrayList<String []>();		//存放相册信息，id和相册名称
	//Spinner的Adapter
	BaseAdapter baSpinner = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(AlbumActivity.this);
			tv.setTextSize(18.5f);
			tv.setTextColor(Color.DKGRAY);
			String [] sa = albumInfoList.get(position);
			tv.setText(sa[1]);
			return tv;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public int getCount() {
			return albumInfoList.size();
		}
	};
	//Gallery的Adapter
	BaseAdapter baGallery= new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv = new ImageView(AlbumActivity.this);
			iv.setAdjustViewBounds(true);
			iv.setLayoutParams(new Gallery.LayoutParams(PHOTO_WIDTH, PHOTO_HEIGHT));
			iv.setMaxHeight(PHOTO_HEIGHT);						//设置ImageView显示的高度
			iv.setMaxWidth(PHOTO_WIDTH);						//设置ImageView显示的宽度	
			iv.setImageBitmap(photoList[position]);				//设置ImageView显示的内容
			return iv;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public int getCount() {
			if(photoList != null){
				return photoList.length;
			}
			else{
				return 0;
			}
		}
	};
	//点下Gallery中的图片后触发的事件监听器
	OnItemClickListener myListener = new OnItemClickListener() {	
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			if(is != null){			//如果ImageSwitcher不为空
				Drawable d = new BitmapDrawable(photoList[position]);
				is.setImageDrawable(d);
			}
		}
	};
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				gl.setAdapter(baGallery);
				is.setImageDrawable(new BitmapDrawable(photoList[0]));
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album);					//设置当前屏幕
		Intent intent = getIntent();					//获取启动该Activity的Intent
		uno = intent.getStringExtra("uno");				//获得Extra字段的uno
		visitor = intent.getStringExtra("visitor");
		from = intent.getIntExtra("from", -1);		//获得Extra字段的from
		int position = intent.getIntExtra("position", 0);	//获得被选中的相册
		String [] albumInfoArray = intent.getStringArrayExtra("albumlist");	//获得相册信息数组
		xid = intent.getStringExtra("xid");			//获得被选中的相册编号
		albumInfoList = new ArrayList<String []>();
		for(String s:albumInfoArray){			//遍历信息数组
			String [] sa = s.split("\\|");
			albumInfoList.add(sa);				//构建相册信息列表
		}
		sp = (Spinner)findViewById(R.id.spAlbum);		//获得Spinner对象
		sp.setAdapter(baSpinner);				//设置Spinner对象的Adapter
		sp.setSelection(position);				//选中在前一个Activity中被选中的相册
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {	//为Spinner添加监听器
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				xid = albumInfoList.get(position)[0];			//修改存储相册ID的成员变量
				getPhotoList();							//获得相册中的相片列表
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		gl = (Gallery)findViewById(R.id.galleryPhoto);		//获得Gallery对象
		gl.setOnItemClickListener(myListener);				//设置Gallery的OnItemClickListener监听器
		is = (ImageSwitcher)findViewById(R.id.isPhoto);		//获得ImageSwitcher对象
		is.setFactory(this);								//设置ImageSwitcher的Factory
		is.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));	//设置ImageSwitcher的In动画
		is.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));//设置ImageSwitcher的Out动画
		Button btnBack = (Button)findViewById(R.id.btnAlbumBack);		//获得返回按钮btnBack
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(from){		//判断启动该Activity的来源
				case 0:			//有MyAlbumListActivity启动
					Intent intent1 = new Intent(AlbumActivity.this,FunctionTabActivity.class);
					intent1.putExtra("uno", uno);
					intent1.putExtra("tab", "tab5");
					startActivity(intent1);		
					finish();
					break;
				case 1:			//由AlbumListActivity启动
					Intent intent2 = new Intent(AlbumActivity.this,HomePageActivity.class);
					intent2.putExtra("uno", uno);
					intent2.putExtra("visitor", visitor);
					intent2.putExtra("tab", "tab2");
					startActivity(intent2);		
					finish();
					break;
				}
			}
		});
		Button btnDeletePhoto = (Button)findViewById(R.id.btnDeletePhoto);
		if(visitor != null){		//不是查看自己的相册
			btnDeletePhoto.setVisibility(View.GONE);		//如果不是自己的相册，隐藏删除按钮
		}
		btnDeletePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AlbumActivity.this)
					.setTitle("提示")
					.setIcon(R.drawable.alert_icon)
					.setMessage("确认删除该照片？")
					.setPositiveButton(
						"确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int pos = gl.getSelectedItemPosition();
								pid = photoInfoList.get(pos)[0];
								deletePhoto();
							}
						})
					.setNegativeButton(
						"取消", 
						null)
					.show();
			}
		});
	}

	public void getPhotoList(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					mc = null;
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					mc.dout.writeUTF("<#GET_ALBUM#>"+xid);
					int size = 0;		//获得相册长度
					size = mc.din.readInt();			//读取相册的长度
					if(size == 0){
						Toast.makeText(AlbumActivity.this, "该相册还未上传照片", Toast.LENGTH_LONG).show();
						Looper.loop();
						return;
					}
					photoInfoList = new ArrayList<String []>(size);
					photoList = new Bitmap[size];
					for(int i=0;i<size;i++){			//循环获取图片数据
						String photoInfo = mc.din.readUTF();		//读取相片信息
						String [] sa = photoInfo.split("\\|");			//切割字符串
						photoInfoList.add(sa);				//将相片信息添加到列表中
						int photoLength = mc.din.readInt();		//读取相片长度
						byte [] buf = new byte[photoLength];		//创建相应长度的数组
						mc.din.read(buf);			//读入图片数据
						photoList[i] = BitmapFactory.decodeByteArray(buf, 0, photoLength);	//创建Bitmap
					}
					myHandler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	//方法：删除指定照片
	public void deletePhoto(){
		new Thread(){
			public void run(){
				Looper.prepare();
				try{
					if(mc ==null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					String msg = "<#DELETE_PHOTO#>"+pid;
					mc.dout.writeUTF(msg);
					String reply = mc.din.readUTF();		
					if(reply.equals("<#DELETE_PHOTO_SUCCESS#>")){	
						Toast.makeText(AlbumActivity.this, "照片删除成功！", Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(AlbumActivity.this, "删除失败，请重试！", Toast.LENGTH_LONG).show();
					}
					getPhotoList();
					Looper.loop();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	@Override
	public View makeView() {
		ImageView iv = new ImageView(this);
		iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		iv.setLayoutParams(new ImageSwitcher.LayoutParams(IMAGESWITCHER_HEIGHT,IMAGESWITCHER_HEIGHT));
		return iv;
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
			mc = null;
		}
		super.onDestroy();
	}
}