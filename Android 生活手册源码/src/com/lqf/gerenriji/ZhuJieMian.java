package com.lqf.gerenriji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lqf.riji.DuanXin;
import com.lqf.riji.RiJi;
import com.lqf.riji.XiaoHua;
import com.lqf.rili.RiLi;
import com.lqf.shezhi.GuanYu;
import com.lqf.shezhi.SheZhi;

public class ZhuJieMian extends Activity {
	//获取系统自带闹钟
	Map<String, Object> item;
	private ArrayList<String> pagList;
	// 定义所需要的控件
	private Button riji, naozhong, rili, shezhi,xiaohua2,duanxin2,guanyu2,zhangben2;

	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);	//设置布局标题
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.zhujiemian);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.biaoti1);//调用自定义标题布局
		
		// 获取控件
		riji = (Button) findViewById(R.id.button1);
		naozhong = (Button) findViewById(R.id.button2);
		pagList = new ArrayList<String>();//闹钟数组
		rili = (Button) findViewById(R.id.button3);
		shezhi = (Button) findViewById(R.id.button4);
		guanyu2 = (Button) findViewById(R.id.guanyu2);
		xiaohua2 = (Button) findViewById(R.id.xiaohua2);
		duanxin2 = (Button) findViewById(R.id.duanxin2);
		zhangben2 = (Button) findViewById(R.id.zhangben2);
		// 绑定Button事件
		riji.setOnClickListener(new MyButton());
		naozhong.setOnClickListener(new MyButton());
		rili.setOnClickListener(new MyButton());
		shezhi.setOnClickListener(new MyButton());
		guanyu2.setOnClickListener(new MyButton());
		xiaohua2.setOnClickListener(new MyButton());
		duanxin2.setOnClickListener(new MyButton());
	}

	// 点击Button按钮触发的事件
	public class MyButton implements OnClickListener {

		public void onClick(View v) {
			// 使用Switch循环判断
			switch (v.getId()) {
			case R.id.button1:
				Intent intent1 = new Intent(ZhuJieMian.this,RiJi.class);
				startActivityForResult(intent1, 0);
				overridePendingTransition(R.anim.my_left, R.anim.my_right);
				break;
			case R.id.button2:
				listPackages();
				Log.d("mxt", "paglist的大小：" + pagList.size());
				for (int i = 0; i < pagList.size(); i++) {
					Log.d("mxt", pagList.get(i));
				}
				
				PackageManager pm = getPackageManager();   

				 Intent i = pm.getLaunchIntentForPackage(pagList.get(0));   
	                //如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL   
	                if (i != null) {
	                	ZhuJieMian.this.startActivity(i);  
	                }  else{
	                	Intent i2= new Intent(Settings.ACTION_DATE_SETTINGS);
	                	ZhuJieMian.this.startActivity(i); 
	                }
				break;
			case R.id.button3:
				Intent intent3 = new Intent(ZhuJieMian.this,RiLi.class);
				startActivityForResult(intent3, 0);
				overridePendingTransition(R.anim.my_left, R.anim.my_right);
				break;
			case R.id.button4:
				Intent intent4 = new Intent(ZhuJieMian.this,SheZhi.class);
				startActivityForResult(intent4, 0);
				overridePendingTransition(R.anim.my_left, R.anim.my_right);
				break;
//			case R.id.zhangben2:
//				Intent intent5 = new Intent(ZhuJieMian.this,GuanYu.class);
//				startActivity(intent5);
//				break;
			case R.id.guanyu2:
				Intent intent6 = new Intent(ZhuJieMian.this,GuanYu.class);
				startActivity(intent6);
				break;
			case R.id.duanxin2:
				Intent intent7 = new Intent(ZhuJieMian.this,DuanXin.class);
				startActivity(intent7);
				break;
			case R.id.xiaohua2:
				Intent intent8 = new Intent(ZhuJieMian.this,XiaoHua.class);
				startActivity(intent8);
				break;
			}
		}

	}
	/**
	 * 获取系统自定义闹钟的方法与判断条件
	 * @author Administrator
	 *
	 */
	class PInfo {
		private String appname = "";
		private String pname = "";
		private String versionName = "";
		private int versionCode = 0;
		private Drawable icon;

		private void prettyPrint() {
			Log.i("taskmanger", appname + "\t" + pname + "\t" + versionName
					+ "\t" + versionCode + "\t");
		}
	}

	private void listPackages() {
		ArrayList<PInfo> apps = getInstalledApps(false); /*
														 * false = no system
														 * packages
														 */
		final int max = apps.size();
		for (int i = 0; i < max; i++) {
			apps.get(i).prettyPrint();
			item = new HashMap<String, Object>();

			int aa = apps.get(i).pname.length();
			// String
			// bb=apps.get(i).pname.substring(apps.get(i).pname.length()-11);
			// Log.d("mxt", bb);

			if (aa > 11) {
				Log.d("lxf", "进来了11");
				if (apps.get(i).pname.indexOf("clock") != -1) {
					if (!(apps.get(i).pname.indexOf("widget") != -1)) {
						try {
							PackageInfo pInfo = getPackageManager().getPackageInfo(apps.get(i).pname, 0); 
					    	   if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {  
					    		   Log.d("mxt","是系统自带的");
					    		   Log.d("mxt","找到了"
													+ apps.get(i).pname.substring(apps
															.get(i).pname.length() - 5)
													+ "  全名：" + apps.get(i).pname + " "
													+ apps.get(i).appname);
									item.put("pname", apps.get(i).pname);
									item.put("appname", apps.get(i).appname);
									pagList.add(apps.get(i).pname); 
					    	   }
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
				}
			}

			/*
			 * if(apps.get(i).pname.subSequence(apps.get(i).pname.length()-11,
			 * apps.get(i).pname.length()) != null){
			 * 
			 * }
			 */
		}
	}

	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
		ArrayList<PInfo> res = new ArrayList<PInfo>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((!getSysPackages) && (p.versionName == null)) {
				continue;
			}
			PInfo newInfo = new PInfo();
			newInfo.appname = p.applicationInfo.loadLabel(getPackageManager())
					.toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
			res.add(newInfo);
		}
		return res;
	}

	public boolean isSystemApp(PackageInfo pInfo) {
		return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	public boolean isSystemUpdateApp(PackageInfo pInfo) {
		return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
	}
}

