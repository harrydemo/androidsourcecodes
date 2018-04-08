package cn.edu.xtu.tilepuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

public class ShowUserInfoActivity extends ListActivity {
	private ClassBoardModel classBoardModel ;
	private ArrayList<HashMap<String,String>> listItem;  
              
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.listview);
		this.classBoardModel = (ClassBoardModel) getApplication();
		populateList();

		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// ����Դ
				R.layout.user_info,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { "Query", "Name", "Time" },
				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.user_info_id.Query, R.user_info_id.Name, R.user_info_id.Time });
		System.out.println("listItemAdapter.getCount()" + listItemAdapter.getCount());
		setListAdapter(listItemAdapter);
		//setContentView(R.layout.user_info);
             /*   
                //��ӵ��  
                listView.setOnItemClickListener(new OnItemClickListener() {  
          
                    @Override  
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
                        setTitle("�����"+arg2+"����Ŀ");  
                    } 
                }); 
                //��ӳ������  
                listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
                      
                    @Override  
                    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
                        menu.setHeaderTitle("�����˵�-ContextMenu");     
                        menu.add(0, 0, 0, "���������˵�0");  
                        menu.add(0, 1, 0, "���������˵�1");     
                    }					
                }); 
                */
    }             
    // �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		setTitle("����˳����˵�����ĵ�" + item.getItemId() + "����Ŀ");
		return super.onContextItemSelected(item);
	}

	private void populateList() {
		String[][] userInfo = classBoardModel.classSQLite.getUserInfo();
		userInfo = sortPeopleInfos(userInfo);
		listItem = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> temp;
		if(userInfo.length>0){
			for (int i = 0; i < userInfo.length; i++) {
				temp = new HashMap<String, String>();
				temp.put("Query", formatQuery(i));
				temp.put("Name", userInfo[i][0]);
				temp.put("Time", formatTime(Long.valueOf(userInfo[i][1])));
				listItem.add(temp);
			}
		}
	}

	public String formatQuery(int i) {
		if ((1 + i) > 0 && (1 + i) < 10)
			return  "��" + " " + (1+i) + " " + "��";
		else if ((1 + i) < 100 && (1 + i) >= 10)
			return "��" +  " " + (1+i) + "��";
		else
			return "��" + (1+i) + "��";
		/*if ((1 + i) > 0 && (1 + i) < 10)
			return "    " + (1 + i);
		else if ((1 + i) < 100 && (1 + i) >= 10)
			return "  " + (1 + i);
		else
			return "" + (1 + i);*/
		/*
		 * if ((1+i) >0 && (1+i) < 10) return "��" + " " + (1+i) + " " + "��";
		 * else if ((1+i) < 100 && (1+i) >= 10) return "��" + (1+i) + " " + "��";
		 * else return "��" + (1+i) + "��";
		 */
	}
            
    public String[][] sortPeopleInfos(String[][] userInfo) {
		int len = userInfo.length, i, j, flag;
		// System.out.println("����������"+len);
		String temp[];
		for (i = 0; i < len - 1; i++) {
			temp = userInfo[i];
			flag = i;
			for (j = i + 1; j < len; j++) {
				if (Long.valueOf(temp[1]) > Long.valueOf(userInfo[j][1])) {
					// System.out.println(temp.time +","+peopleInfo[j].time);
					temp[0] = userInfo[j][0];
					temp[1] = userInfo[j][1];
					flag = j;
				}
			}
			if (flag != i) {
				userInfo[flag] = userInfo[i];
				userInfo[i] = temp;
			}
		}
		return userInfo;
	}

	public static String formatTime(long sumTime) {
		String timeString = "";
		long h = 0;
		long m = 0;
		long s = 0;
		h = sumTime / (60 * 60);
		sumTime = sumTime % (60 * 60);
		m = sumTime / 60;
		sumTime = sumTime % 60;
		s = sumTime;
		if (h > 0)
			timeString += h + "ʱ";
		//else timeString += "0" + h + "ʱ";

		if (m == 0) {
			if (h > 0 )
				timeString += "00��";
		} else if (m > 0)
			timeString += m + "��";
		//else timeString += "0" + m + "��";
		if (s == 0) {
			if (h > 0 || m > 0)
				timeString += "00��";
		} else if (s > 0)
			timeString += s + "��";
		//else timeString += s + "��";

		return timeString;
	}
     
         	@Override
        	protected void onStart() {
        		System.out.println("ShowUserInfoActivity===>>onStart");	
        		super.onStart();
        	}

        	@Override
        	protected void onRestart() {
        		System.out.println("ShowUserInfoActivity===>>onRestart");
        		super.onRestart();		
        	}

        	@Override
        	protected void onResume() {
        		System.out.println("ShowUserInfoActivity===>>onResume");
        		super.onResume();
        	}

        	@Override
        	protected void onPause() {
        		System.out.println("ShowUserInfoActivity===>>onPause");
        		super.onPause();
        	}

        	@Override
        	protected void onStop() {
        		System.out.println("ShowUserInfoActivity===>>onStop");
        		super.onStop();
        	}

        	@Override
        	protected void onDestroy() {
        		System.out.println("ShowUserInfoActivity====>>onDestroy");
        		super.onDestroy();
        	}
}  

