package com.bus.shenyang.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.bus.shenyang.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class More extends Activity{
	private final String[] mLabelArray = { "分享", "关于" ,"说明","积分"};
	private final int[] tuPian = { R.drawable.ic_launcher,
			R.drawable.ic_launcher ,R.drawable.ic_launcher ,R.drawable.ic_launcher};
	Intent i;

	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
		   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		   setContentView(R.layout.more);
 
			GridView gridview = (GridView) findViewById(R.id.GridView);
			ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < 4; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ItemImage", tuPian[i]);
				map.put("ItemText", mLabelArray[i]);
				meumList.add(map);
			}

			SimpleAdapter saMenuItem = new SimpleAdapter(this, meumList, //
					R.layout.menuitem, //
					new String[] { "ItemImage", "ItemText" }, //
					new int[] { R.id.ItemImage, R.id.ItemText }); //

			gridview.setAdapter(saMenuItem);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0,
						android.view.View arg1, int arg2, long arg3) {
					switch (arg2) {
					case 0:
						Intent i = new Intent();
						i.setAction(Intent.ACTION_SEND);
						i.putExtra(Intent.EXTRA_TEXT, "大家好，我正在使用“沈阳离线公交”，很好用的撒，"
								+ "不在为自己起一个出行而烦恼不信你就试试。可以到安智市场下载噢");
						i.setType("text/*");
						startActivity(i);
						break;
					case 1:

						LayoutInflater inflater = getLayoutInflater();
						View view1 = inflater.inflate(R.layout.about,
								(ViewGroup) findViewById(R.id.about_layout));
						// TextView textview01 = (TextView)
						// view1.findViewById(R.id.namemailbox);
						TextView textview02 = (TextView) view1
								.findViewById(R.id.weibo);
						textview02.setText("http://weibo.com/yiqi786281067\n");
						TextView textview03 = (TextView) view1
								.findViewById(R.id.mailbox);
						textview03.setText("qi19901212@gmail.com"+"\n");
						TextView textview04 = (TextView) view1
								.findViewById(R.id.phone);
						textview04.setText("15104001964\n");
						TextView textview05 = (TextView) view1
								.findViewById(R.id.introduction);
						textview05.setText("本软件的相关声明如下：网络数据来自于百度地图。" +
								"离线的内容来自于网络，有错误的线路请指点，可联系本人，欢迎您的来信交流。"
								+"本软件免费使用不收取任何费用，但是在网络查询状态下需要一定的流量，" +
										"产生的流量就是您购买运营商的流量，wifi更迅速快捷！奇迹作品版权所有2012");
						AlertDialog.Builder builder = new AlertDialog.Builder(
								More.this);
						builder.setView(view1);
						AlertDialog dialog = builder.create();
						dialog.show();

						break;
					case 2:
						 Dialog dialog1 = new AlertDialog.Builder(More.this)
                         .setTitle("提示")
                         .setMessage("首先感谢您的下载与使用【沈阳离线公交】，本软件由沈师大在校学生所写，如果你有什么疑问和建议。"
								+ "欢迎通过以上的联系方式联系本人，加以修改，给大家更好的用户体验。" + "谢谢您的下载与使用")
                         .setPositiveButton("确定",
                         new DialogInterface.OnClickListener()
                         {
                             public void onClick(DialogInterface dialog, int whichButton)
                             {
                                 dialog.cancel();
                             }
                         }).create();//创建按钮
          
                 dialog1.show();
						break;
					case 3:
						 Dialog dialog2 = new AlertDialog.Builder(More.this)
                         .setTitle("关于广告的相关说明")
                         .setMessage("首先感谢您的下载与使用【沈阳离线公交】，本软件含有广告。" +
                         		"在网络查询中有相应的广告。广告是开发者继续开发的动力，望谅解，" +
                         		"当您点击一定次数的时候，将彻底没有广告。每点击一次消耗一定的积分，" +
                         		"这个可以通过下载或运行相应的软件，来获得一定的积分。"
								 + "谢谢您的下载与使用")
                         .setPositiveButton("确定",
                         new DialogInterface.OnClickListener()
                         {
                             public void onClick(DialogInterface dialog, int whichButton)
                             {
                                 dialog.cancel();
                             }
                         }).create();
						  dialog2.show();
						break;
					}
				}
			});
		}
	   @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				new AlertDialog.Builder(More.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("沈阳离线公交")
						.setMessage("你确定退出了哦?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										android.os.Process.killProcess(android.os.Process.myPid());
									}

								}).setNegativeButton("取消", null).show();
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		
	}
