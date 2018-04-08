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
	private final String[] mLabelArray = { "����", "����" ,"˵��","����"};
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
						i.putExtra(Intent.EXTRA_TEXT, "��Һã�������ʹ�á��������߹��������ܺ��õ�����"
								+ "����Ϊ�Լ���һ�����ж����ղ���������ԡ����Ե������г�������");
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
						textview05.setText("�����������������£��������������ڰٶȵ�ͼ��" +
								"���ߵ��������������磬�д������·��ָ�㣬����ϵ���ˣ���ӭ�������Ž�����"
								+"��������ʹ�ò���ȡ�κη��ã������������ѯ״̬����Ҫһ����������" +
										"����������������������Ӫ�̵�������wifi��Ѹ�ٿ�ݣ��漣��Ʒ��Ȩ����2012");
						AlertDialog.Builder builder = new AlertDialog.Builder(
								More.this);
						builder.setView(view1);
						AlertDialog dialog = builder.create();
						dialog.show();

						break;
					case 2:
						 Dialog dialog1 = new AlertDialog.Builder(More.this)
                         .setTitle("��ʾ")
                         .setMessage("���ȸ�л����������ʹ�á��������߹����������������ʦ����Уѧ����д���������ʲô���ʺͽ��顣"
								+ "��ӭͨ�����ϵ���ϵ��ʽ��ϵ���ˣ������޸ģ�����Ҹ��õ��û����顣" + "лл����������ʹ��")
                         .setPositiveButton("ȷ��",
                         new DialogInterface.OnClickListener()
                         {
                             public void onClick(DialogInterface dialog, int whichButton)
                             {
                                 dialog.cancel();
                             }
                         }).create();//������ť
          
                 dialog1.show();
						break;
					case 3:
						 Dialog dialog2 = new AlertDialog.Builder(More.this)
                         .setTitle("���ڹ������˵��")
                         .setMessage("���ȸ�л����������ʹ�á��������߹���������������й�档" +
                         		"�������ѯ������Ӧ�Ĺ�档����ǿ����߼��������Ķ��������½⣬" +
                         		"�������һ��������ʱ�򣬽�����û�й�档ÿ���һ������һ���Ļ��֣�" +
                         		"�������ͨ�����ػ�������Ӧ������������һ���Ļ��֡�"
								 + "лл����������ʹ��")
                         .setPositiveButton("ȷ��",
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
						.setTitle("�������߹���")
						.setMessage("��ȷ���˳���Ŷ?")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										android.os.Process.killProcess(android.os.Process.myPid());
									}

								}).setNegativeButton("ȡ��", null).show();
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		
	}
