package wyf.wpf;
import static wyf.wpf.ConstantUtil.SERVER_ADDRESS;
import static wyf.wpf.ConstantUtil.SERVER_PORT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UploadActivity extends Activity{
	MyConnector mc = null;
	List<String []> albumList = new ArrayList<String []>();
	String uno = null;			//存放用户编号
	byte [] data = null;		//存储图片的字节数组
	String newAlbum = null;		//存放新相册的名称
	String xid = null;			//要上传到的相册ID
	String name = null;			//上传的照片名称
	String des = null;			//上传的照片描述
	ProgressDialog pd = null;	//声明进度对话框对象
	Spinner sp = null;
	BaseAdapter baSpinner = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(UploadActivity.this);
			tv.setTextSize(18.5f);
			tv.setTextColor(R.color.character);
			String [] sa = albumList.get(position);
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
			return albumList.size();
		}
	};
	Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				sp.setAdapter(baSpinner);
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);		//设置显示屏幕
		Intent intent = getIntent();			//获得Intent
		uno = intent.getStringExtra("uno");			//读取用户编号
		data = intent.getByteArrayExtra("data");	//读取字节数组
		getAlbumList();							//获得相册列表
		sp = (Spinner)findViewById(R.id.AlbumSpinner);				//获得Spinner对象
		sp.setAdapter(baSpinner);											//设置Spiiner的Adapter
		Button btnNewAlbum = (Button)findViewById(R.id.btnNewAlbum);		//获得添加新相册按钮
		btnNewAlbum.setOnClickListener(new View.OnClickListener() {			//为按钮添加监听器
			@Override
			public void onClick(View v) {						//重写onClick方法
				final View dialog_view = LayoutInflater.from(UploadActivity.this).inflate(R.layout.new_album, null);	//获得对话框视图
				new AlertDialog.Builder(UploadActivity.this)
				.setView(dialog_view)
				.setPositiveButton(
						R.string.btnOk, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								EditText et = (EditText)dialog_view.findViewById(R.id.etAlbumName);
								newAlbum = et.getEditableText().toString().trim();
								if(newAlbum.equals("")){		//如果
									Toast.makeText(UploadActivity.this, "请输入新相册的名称", Toast.LENGTH_LONG).show();
									return;
								}
								createNewAlbum();
							}
						}).show();
			}
		});
		Button btnUpload = (Button)findViewById(R.id.btnConfirmUpload);			//上传按钮
		btnUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {						//点下上传按钮后出发
				int position = sp.getSelectedItemPosition();
				if( position < 0){
					Toast.makeText(UploadActivity.this, "您还没有选择上传相册！", Toast.LENGTH_LONG).show();
					return;
				}				
				xid = albumList.get(position)[0];		//获得相册的id
				EditText etName = (EditText)findViewById(R.id.etPhotoName);			//获得照片名称的EditText控件
				EditText etDes = (EditText)findViewById(R.id.etPhotoDes);			//获得照片描述的EditText控件
				name = etName.getEditableText().toString().trim();		//获得照片名称
				des = etDes.getEditableText().toString().trim();			//获得照片描述
				if(name.equals("") || des.equals("")){			//没有输入任何消息
					Toast.makeText(UploadActivity.this, "请输入照片 名称", Toast.LENGTH_LONG).show();
					return;
				}
				pd = ProgressDialog.show(UploadActivity.this, "请稍候", "正在上传图片...",true,true);
				uploadMyPhoto();			//调用方法上传图片
			}
		});
		Button btnUploadBack = (Button)findViewById(R.id.btnUploadBack);
		btnUploadBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadActivity.this.finish();				//结束该Activity
			}
		});
	}
	//方法：获得相册列表
	public void getAlbumList(){
		new Thread(){
			public void run(){
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					mc.dout.writeUTF("<#GET_ALBUM_LIST#>"+uno);					//发出获取相册列表请求
					String reply = mc.din.readUTF();					//读取相册列表
					if(reply.equals("<#NO_ALBUM#>")){			//如果无相册
						Toast.makeText(UploadActivity.this, "您还没有创建任何相册！", Toast.LENGTH_LONG).show();
						albumList = new ArrayList<String []>();
						albumList.add(new String[]{null,"无相册"});
						return;
					}
					String albumArray [] = reply.split("\\$");				//切割字符串
					albumList = null;
					albumList = new ArrayList<String []>();
					for(String s:albumArray){
						String [] sa = s.split("\\|");					//切割字符串
						albumList.add(sa);
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				myHandler.sendEmptyMessage(0);
			}
		}.start();
	}
	//方法：与服务器交互，创建一个新相册
	public void createNewAlbum(){
		new Thread(){
			public void run(){
				Looper.prepare();			//启动一个消息循环
				try{
					if(mc == null){
						mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
					}
					mc.dout.writeUTF("<#NEW_ALBUM#>"+newAlbum+"|"+uno);		//向服务器发出创建新相册的请求
					String reply = mc.din.readUTF();			//接收服务器的消息
					if(reply.equals("<#NEW_ALBUM_SUCCESS#>")){			//如果创建成功
						getAlbumList();		//重新获得相册列表
						Toast.makeText(UploadActivity.this, "相册创建成功！", Toast.LENGTH_LONG).show();
						Looper.loop();			//执行本线程中的消息队列
					}
					else{
						Toast.makeText(UploadActivity.this, "相册创建失败，请重试！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Looper.myLooper().quit();			//在线程最后关闭消息队列
			}
		}.start();
	}
	//方法：向服务器上传照片
	public void uploadMyPhoto(){
		new Thread(){
			public void run(){
				Looper.prepare();			//为该线程初始化一个消息队列
				if(mc == null){
					mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
				}
				try{
					String msg = "<#NEW_PHOTO#>"+name+"|"+des+"|"+xid;
					mc.dout.writeUTF(msg);			//发出消息
					mc.dout.writeInt(data.length);
					for(int i=0;i<data.length;i++){
						mc.dout.writeByte(data[i]);			//一个一个字节的发
					}
					mc.dout.flush();						//????????????????????????????????????????????????/
					String reply = mc.din.readUTF();	//接收回复
					if(reply.equals("<#NEW_PHOTO_SUCCESS#>")){			//上传成功
						pd.dismiss();
						Toast.makeText(UploadActivity.this, "照片上传成功！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
					else{
						pd.dismiss();
						Toast.makeText(UploadActivity.this, "照片上传失败！", Toast.LENGTH_LONG).show();
						Looper.loop();
					}
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
		}.start();
	}
	@Override
	protected void onDestroy() {
		if(mc != null){
			mc.sayBye();
		}
		super.onDestroy();
	}
	
}