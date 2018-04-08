package com.ly.control;

import java.io.InputStream;



import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ly.control.R;
import com.ly.bean.ApplyBean;
import com.ly.bean.AttentionBean;
import com.ly.bean.FriendListBean;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Toast;

/**
 * �̳�ExpandableListActivity��
 */
public class LYFriendsActivity extends Activity {
	
	private	HashMap<String,Object> map;
	
	private ArrayList<String[]> list;
	private ArrayList<String[]> lista;
	private ArrayList<String[]> list2;
	private ArrayList<String[]> listb;
	private ArrayList<String[]> list1;
	private ArrayList<String[]> listc;
	private String ueid;
	private String aid;
	private String fid;
	List<Map<String, String>> child1,child2,child3;
	List<List<Map<String, String>>> childs;
	SimpleExpandableListAdapter adapter;
	List<Map<String, String>> groups;
	ExpandableListView elvExpandableListView;
	private String id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		
		id=getIntent().getStringExtra("result");
		Thread t1= new Thread(r3);
		t1.start();
		Thread t2= new Thread(r4);
		t2.start();
		Thread t3= new Thread(r1);
		t3.start();
		//System.out.println(id);
	   elvExpandableListView = (ExpandableListView) findViewById(R.id.list);
		// ����һ����Ŀ
		groups = new ArrayList<Map<String, String>>();
		// ��������һ����Ŀ����
		Map<String, String> group1 = new HashMap<String, String>();
		group1.put("group", "�ҵĺ�������");
		Map<String, String> group2 = new HashMap<String, String>();
		group2.put("group", "�ҵĺ���");
		Map<String, String> group3 = new HashMap<String, String>();
		group3.put("group", "�ҵĹ�ע��");
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		// ����һ����Ŀ�µĵĶ�����Ŀ
//		List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
//		// ͬ������һ����ĿĿ¼�´���������Ӧ�Ķ�����ĿĿ¼
//		Map<String, String> childdata1 = new HashMap<String, String>();
//		childdata1.put("child", "���ʽ�");
//		Map<String, String> childdata2 = new HashMap<String, String>();
//		childdata2.put("child", "��Ԫ��");
//		child1.add(childdata1);
//		child1.add(childdata2);
//		// ͬ��
		child1 = new ArrayList<Map<String, String>>();
		child2 = new ArrayList<Map<String, String>>();
	/*	Map<String, String> childdata3 = new HashMap<String, String>();
		childdata3.put("child", "��̩");
		Map<String, String> childdata4 = new HashMap<String, String>();
		childdata4.put("child", "����");
		child2.add(childdata3);
		child2.add(childdata4);*/

	
	// ͬ��
	    child3 = new ArrayList<Map<String, String>>();
//		Map<String, String> childdata5 = new HashMap<String, String>();
//		childdata5.put("child", "��չ��");
//		Map<String, String> childdata6 = new HashMap<String, String>();
//		childdata6.put("child", "ĪС��");
//		child3.add(childdata5);
//		child3.add(childdata6);
//		// ��������Ŀ����һ�����������ʾʱʹ��
	    childs = new ArrayList<List<Map<String, String>>>();
		childs.add(child1);
		childs.add(child2);
		childs.add(child3);

		/**
		 * ʹ��SimpleExpandableListAdapter��ʾExpandableListView 
		 * ����1.�����Ķ���Context
		 * ����2.һ����ĿĿ¼���� 
		 * ����3.һ����Ŀ��Ӧ�Ĳ����ļ� 
		 * ����4.fromto������map�е�key��ָ��Ҫ��ʾ�Ķ���
		 * ����5.�����4��Ӧ��ָ��Ҫ��ʾ��groups�е�id 
		 * ����6.������ĿĿ¼���� 
		 * ����7.������Ŀ��Ӧ�Ĳ����ļ�
		 * ����8.fromto������map�е�key��ָ��Ҫ��ʾ�Ķ��� 
		 * ����9.�����8��Ӧ��ָ��Ҫ��ʾ��childs�е�id
		 */
	     adapter = new SimpleExpandableListAdapter(
					this, groups, R.layout.groups, new String[] { "group" },
					new int[] { R.id.group }, childs, R.layout.childs,
					new String[] { "child" }, new int[] { R.id.child });
	     elvExpandableListView.setAdapter(adapter);
		 registerForContextMenu(elvExpandableListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);
		// �����������Ԫ��
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			// ��index
			int groupPos = ExpandableListView
					.getPackedPositionGroup(info.packedPosition);

			switch (groupPos) {

			// ����������
			case 0:
				menu.setHeaderTitle("�Ƿ�ͬ������");
				menu.add(0, 0, 0, "ͬ��");
				menu.add(0, 1, 0, "��ͬ��");
				break;

			// ������
			case 1:
				menu.setHeaderTitle("���Ѳ˵�");
				menu.add(0, 0, 0, "�鿴���ѵ����м���");
				menu.add(0, 1, 0, "�����ѷ�����Ϣ");
				break;

			// ��ע����
			case 2:
				menu.setHeaderTitle("��ע�߲˵�");
				menu.add(0, 0, 0, "�鿴��ע�ߵ����м���");
				break;
			}

		}

		super.onCreateContextMenu(menu, v, menuInfo);

	}

	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// ��ȡ�������б�ĸ�����Ϣ
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();

		// ��ȡ���ͣ������ǩ��չ�ֲ˵����������ӱ�ǩ��չ�ֲ˵���
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);
		// ������ӱ�ǩ��չ�ֲ˵�
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			// ��ȡ��Index
			int groupPos = ExpandableListView
					.getPackedPositionGroup(info.packedPosition);
			// ��ȡ��Ԫ���ڵ�ǰ���Index
			int childPos = ExpandableListView
					.getPackedPositionChild(info.packedPosition);

			// �ж����
			switch (groupPos) {

			// ��������
			case 0:

				switch (item.getItemId()) {
				
				case 0:// ͬ�����Ϊ����
					Toast.makeText(this, "ͬ���Ϊ���� ", Toast.LENGTH_SHORT).show();
					aid = list2.get(item.getItemId())[0];
					ueid = list2.get(item.getItemId())[1];
					Thread t6 = new Thread(r2);
					t6.start();
					resetms();
					break;
				case 1:// ��ͬ�����Ϊ����
					Toast.makeText(this, "��ͬ���Ϊ���� ", Toast.LENGTH_SHORT).show();
					
					fid = list1.get(item.getItemId()-1)[0];
					aid = list2.get(item.getItemId()-1)[0];
					
					Thread t7 = new Thread(r6);
					t7.start();	
					resetms();
					break;
				}
				break;
			case 1:
				switch (item.getItemId()) {
				case 0:// �鿴���м���
					Toast.makeText(this, "�鿴���Ѽ��� ", Toast.LENGTH_SHORT).show();
					//ueid = list.get(item.getItemId())[3];
					 ueid = list.get(childPos)[3];
					
					
					Intent intent =new Intent(LYFriendsActivity.this,LYFriendsMeActivity.class); 
					intent.putExtra("ueid", ueid);
					intent.putExtra("result", id);
					startActivity(intent);
					break;
				case 1:// ������Ϣ
					Toast.makeText(this, "������Ϣ", Toast.LENGTH_LONG).show();
					//ueid = list.get(item.getItemId()-1)[3];
					 ueid = list.get(childPos)[3];
					Intent intent2 =new Intent(LYFriendsActivity.this,LYSendInfoActivity.class); 
					intent2.putExtra("ueid", ueid);
					intent2.putExtra("result", id);
					startActivity(intent2);
					break;
				}

				break;

			case 2:

				// �鿴��ע����Ϣ
				Toast.makeText(this, "�鿴��ע�߼���", Toast.LENGTH_SHORT).show();
				//ueid = list.get(item.getItemId())[3];
				 ueid = list.get(childPos)[3];
				Intent intent =new Intent(LYFriendsActivity.this,LYFriendsMeActivity.class); 
				intent.putExtra("ueid", ueid);
				intent.putExtra("result", id);
				startActivity(intent);
				break;

			}

			return true;
		}

		return false;
	}
	 //  �����߳�
Runnable r3 = new Runnable() {
		

		public void run() {
			//LoginUserHandler luh = new LoginUserHandler();
			try {
				URL url = new URL("http://10.0.2.2:8080/Lvyou/FriendListServlet");
				HttpURLConnection htc = (HttpURLConnection) url.openConnection();
				htc.setDoInput(true);
				htc.setDoOutput(true);
				htc.setRequestMethod("POST");
				
				OutputStream out= htc.getOutputStream();
				StringBuilder sb = new StringBuilder();
				
				sb.append("<user>");
				sb.append("<uid>");					
				sb.append(id);
				sb.append("</uid>");
				sb.append("</user>");
				
				byte userXML[] = sb.toString().getBytes();
				out.write(userXML);
				
				if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
				{
					InputStream in =htc.getInputStream();
					FriendListBean flb= new FriendListBean();
					list = flb.friendlist(in);
					
					Message msg = new Message();
					msg.obj=list;
					h.sendMessage(msg);
					
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	Handler  h=new  Handler()
	  {
		 public void handleMessage(android.os.Message msg) {
			
			 list1 =(ArrayList<String[]>) msg.obj;
			 
			 Map<String, String> childdata =null;
			 
			 if(list1.size()==0&&id==null){
				 Toast.makeText(getApplicationContext(), "δ��¼", Toast.LENGTH_SHORT).show();
			 }else{
				 for(int i=0;i<list1.size();i++){
					  childdata = new HashMap<String, String>();
					 childdata.put("child",list1.get(i)[4] );
					 child2.add(childdata);
					 }
				 for(int i=0;i<child2.size();i++){
						//System.out.println( "===="+child2.get(i).get("child"));
					 }
					
					 childs.add(child2);
			 }
			 
			
			
			
			// System.out.println(list.size()+":handler...");
		  };
		 
	  };
	
	  //  ��ע���߳�
	  Runnable r4 = new Runnable() {
			

			public void run() {
				//LoginUserHandler luh = new LoginUserHandler();
				try {
					URL url = new URL("http://10.0.2.2:8080/Lvyou/AttentionServlet");
					HttpURLConnection htc = (HttpURLConnection) url.openConnection();
					htc.setDoInput(true);
					htc.setDoOutput(true);
					htc.setRequestMethod("POST");
					
					
					OutputStream out= htc.getOutputStream();
					StringBuilder sb = new StringBuilder();
					
					sb.append("<user>");
					sb.append("<uid>");					
					sb.append(id);
					sb.append("</uid>");
					sb.append("</user>");
					
					byte userXML[] = sb.toString().getBytes();
					out.write(userXML);
					if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
					{
						InputStream in =htc.getInputStream();
						AttentionBean atb= new AttentionBean();
						
						lista = atb.attention(in);
						
						Message msg = new Message();
						msg.obj=lista;
						haa.sendMessage(msg);
						
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		Handler  haa=new  Handler()
		  {
			  public void handleMessage(android.os.Message msg) {
				 lista =(ArrayList<String[]>) msg.obj;
				 Map<String, String> childdata =null;
				 if(lista.size()==0){
					 //Toast.makeText(getApplicationContext(), "δ��¼", Toast.LENGTH_SHORT).show();
				 }else{
				 for(int i=0;i<lista.size();i++){
				  childdata = new HashMap<String, String>();
				 childdata.put("child",lista.get(i)[4] );
				 child3.add(childdata);
				 }
				/* for(int i=0;i<child3.size();i++){
					System.out.println( "===="+child3.get(i).get("child"));
				 }*/
				 childs.add(child3);
				// System.out.println(lista.size()+":ahandler...");
				 }
				 
			  };
		  };

			  Runnable r1 = new Runnable() {
					

					public void run() {
						//LoginUserHandler luh = new LoginUserHandler();
						try {
							URL url = new URL("http://10.0.2.2:8080/Lvyou/LYApplyServlet");
							HttpURLConnection htc = (HttpURLConnection) url.openConnection();
							htc.setDoInput(true);
							htc.setDoOutput(true);
							htc.setRequestMethod("POST");
							
							
							OutputStream out= htc.getOutputStream();
							StringBuilder sb = new StringBuilder();
							
							sb.append("<user>");
							sb.append("<uid>");					
							sb.append(id);
							sb.append("</uid>");
							sb.append("</user>");
							
							byte userXML[] = sb.toString().getBytes();
							out.write(userXML);
							if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
							{
								InputStream in =htc.getInputStream();
								ApplyBean flb= new ApplyBean();
								listb = flb.applys(in);
								
								Message msg = new Message();
								msg.obj=listb;
								ha.sendMessage(msg);
								
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				};
				Handler  ha=new  Handler()
				  {
					
					  public void handleMessage(android.os.Message msg) {
						  child1.clear();
						  list2 =(ArrayList<String[]>) msg.obj;
						 Map<String, String> childdata =null;
						 if(list2.size()==0){
							 //Toast.makeText(getApplicationContext(), "δ��¼", Toast.LENGTH_SHORT).show();
						 }else{
						 for(int i=0;i<list2.size();i++){
						  childdata = new HashMap<String, String>();
						 childdata.put("child","�û�"+list2.get(i)[4]+"����������Ϊ����!" );
						 child1.add(childdata);
						 }
						/* for(int i=0;i<child3.size();i++){
							System.out.println( "===="+child3.get(i).get("child"));
						 }*/
						 childs.add(child1);
						// System.out.println(lista.size()+":ahandler...");
						 }
						 
					  };
				  };
				  Runnable r2 = new Runnable() {
						

						public void run() {
							//LoginUserHandler luh = new LoginUserHandler();
							try {
								URL url = new URL("http://10.0.2.2:8080/Lvyou/DeleteApplyServlet");
								HttpURLConnection htc = (HttpURLConnection) url.openConnection();
								htc.setDoInput(true);
								htc.setDoOutput(true);
								htc.setRequestMethod("POST");
								
								
								OutputStream out= htc.getOutputStream();
								StringBuilder sb = new StringBuilder();
								
								sb.append("<user>");
								sb.append("<uid>");					
								sb.append(aid);
								sb.append("</uid>");
								sb.append("<hostid>");					
								sb.append(id);
								sb.append("</hostid>");
								sb.append("<otherid>");					
								sb.append(ueid);
								sb.append("</otherid>");
								sb.append("</user>");
								
								byte userXML[] = sb.toString().getBytes();
								out.write(userXML);
								if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
								{
									InputStream in =htc.getInputStream();
									
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					};
//					 Runnable r5 = new Runnable() {
//							
//
//							public void run() {
//								//LoginUserHandler luh = new LoginUserHandler();
//								try {
//									URL url = new URL("http://10.0.2.2:18080/FX_Donkey/FXAddFriendsServlet");
//									HttpURLConnection htc = (HttpURLConnection) url.openConnection();
//									htc.setDoInput(true);
//									htc.setDoOutput(true);
//									htc.setRequestMethod("POST");
//									
//									
//									OutputStream out= htc.getOutputStream();
//									StringBuilder sb = new StringBuilder();
//									
//									sb.append("<user>");
//									sb.append("<hostid>");					
//									sb.append(id);
//									sb.append("</hostid>");
//									sb.append("<otherid>");					
//									sb.append(ueid);
//									sb.append("</otherid>");
//									sb.append("</user>");
//									
//									byte userXML[] = sb.toString().getBytes();
//									out.write(userXML);
//								
//
//								} catch (Exception e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//								
//							}
//						};
						
						
						
						  Runnable r6 = new Runnable() {
								

								public void run() {
									//LoginUserHandler luh = new LoginUserHandler();
									try {
										URL url = new URL("http://10.0.2.2:8080/Lvyou/DeleteFriendsServlet");
										HttpURLConnection htc = (HttpURLConnection) url.openConnection();
										htc.setDoInput(true);
										htc.setDoOutput(true);
										htc.setRequestMethod("POST");
										
										
										OutputStream out= htc.getOutputStream();
										StringBuilder sb = new StringBuilder();
										
										sb.append("<user>");
										sb.append("<uid>");					
										sb.append(aid);
										sb.append("</uid>");
										sb.append("<fid>");					
										sb.append(fid);
										sb.append("</fid>");
										
										sb.append("</user>");
										
										byte userXML[] = sb.toString().getBytes();
										out.write(userXML);
										if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
										{
											InputStream in =htc.getInputStream();
											
										}

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
							};
							private void resetms() {
								//LoginUserHandler luh = new LoginUserHandler();
								try {
									URL url = new URL("http://10.0.2.2:8080/Lvyou/LYApplyyServlet");
									HttpURLConnection htc = (HttpURLConnection) url.openConnection();
									htc.setDoInput(true);
									htc.setDoOutput(true);
									htc.setRequestMethod("POST");
									
									
									OutputStream out= htc.getOutputStream();
									StringBuilder sb = new StringBuilder();
									
									sb.append("<user>");
									sb.append("<uid>");					
									sb.append(id);
									sb.append("</uid>");
									sb.append("</user>");
									
									byte userXML[] = sb.toString().getBytes();
									out.write(userXML);
									if(htc.getResponseCode()==HttpURLConnection.HTTP_OK)
									{
										InputStream in =htc.getInputStream();
										ApplyBean flb= new ApplyBean();
										listb = flb.applys(in);
										
										Message msg = new Message();
										msg.obj=listb;
										ha.sendMessage(msg);
										
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
}
