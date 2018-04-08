package mif.apps.ringtonepro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class InputFile extends Activity
{
	private List<InputFileListItem>	directoryEntries = new ArrayList<InputFileListItem>();
	private File				currentDirectory = new File("/sdcard/");

	/*�ļ��������ݿ�*/
	MyDataBaseAdapter m_MyDatabaseHelper;
	//ListView
	ListView fileList;
	InputFileListAdapter listAdapter;
	
	private Button confim;
	


	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle)
	{   
		super.onCreate(icicle);
		setContentView(R.layout.input_file_list);
		fileList = (ListView) findViewById(R.id.listview);
		browseToRoot();
		/*�������ݿ�*/
		m_MyDatabaseHelper = new MyDataBaseAdapter(this);
		m_MyDatabaseHelper.open();
		
		/*ȷ�ϰ�ť*/
		confim = (Button)findViewById(R.id.footer_button);
		confim.setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				//�����������	
				int count = 0;
					for(int i=0;i<directoryEntries.size();i++){
						if(listAdapter.map.get(i)){
							m_MyDatabaseHelper.insertData(directoryEntries.get(i).getName(), directoryEntries.get(i).getDir());
							count++;
						}
					}
					Toast.makeText(getApplicationContext(), Integer.toString(count)+"�����������", Toast.LENGTH_SHORT).show();
//				}
			}
		});
		
		//����ListView�����ʱ�ļ���
		fileList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				String selectedFileString = directoryEntries.get(position).getName();
				
				if (selectedFileString.equals(getString(R.string.up_one_level)))
				{
					//������һ��Ŀ¼
					upOneLevel();
				}
				else
				{
							
					File clickedFile = null;
					Log.i("InputFile", directoryEntries.get(position).getDir());
					clickedFile = new File(directoryEntries.get(position).getDir());
					if(directoryEntries.get(position).isFolder()){
						browseTo(clickedFile);
					}
					else{
						//����CheckBox
						CheckBox checkBox = (CheckBox) v.findViewById(R.id.multiple_checkbox);
		                //���ʱ��ѡ��״̬����
		                checkBox.toggle();
		                //��ѡ��״̬����map����
		                listAdapter.map.put(position, checkBox.isChecked());
					}

				}
				
			}
			
		});
	}
	//���SD���ĸ�Ŀ¼
	private void browseToRoot() 
	{
		browseTo(new File("/sdcard/"));
    }
	//������һ��Ŀ¼
	private void upOneLevel()
	{
		if(this.currentDirectory.getParent() != null)
			this.browseTo(this.currentDirectory.getParentFile());
//			filename.clear();
	}
	//���ָ����Ŀ¼
	private void browseTo(final File file)
	{
		this.setTitle(file.getAbsolutePath());
		if (file.isDirectory())
		{
			this.currentDirectory = file;
			fill(file.listFiles());
		}
	}
	
	//����������Ϊ����ListActivity��Դ
	private void fill(File[] files)
	{
		//����б�
		this.directoryEntries.clear();
		
		//������Ǹ�Ŀ¼�������һ��Ŀ¼��
		if (this.currentDirectory.getParent() != null){
			this.directoryEntries.add(new InputFileListItem(getString(R.string.up_one_level),getString(R.string.none), getResources().getDrawable(R.drawable.uponelevel),true));
		}
			

		Drawable AudioIcon = getResources().getDrawable(R.drawable.audio);
		Drawable FolderIcon = getResources().getDrawable(R.drawable.folder);
		for (File currentFile : files)
		{
			//ȡ���ļ���
			String fileName = currentFile.getName();
			
			if(currentFile.isDirectory()){
				this.directoryEntries.add(new InputFileListItem(fileName,currentFile.getAbsolutePath(), FolderIcon,true));
			}
			else if(checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio))){//���˵���Ƶ�ļ�������ļ�
				this.directoryEntries.add(new InputFileListItem(fileName,currentFile.getAbsolutePath(), AudioIcon,false));
			}
		}
		Collections.sort(this.directoryEntries);
		//����Adapter
		listAdapter = new InputFileListAdapter(this,this.directoryEntries);
		//���Adapter
		fileList.setAdapter(listAdapter);
	}
	

	//ͨ���ļ����ж���ʲô���͵��ļ�
	private boolean checkEndsWithInStringArray(String checkItsEnd, 
					String[] fileEndings)
	{
		for(String aEnd : fileEndings)
		{
			if(checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	
	//��ӵײ������˵�
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "ȫѡ").setIcon(R.drawable.selectall);
		menu.add(0, 1, 0, "ȫ��ѡ").setIcon(R.drawable.unselectall);
		menu.add(0, 2, 0, "��ѡ").setIcon(R.drawable.toggleall);

		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case 0:
				selectAllItem();
				break;
			case 1:
				unselectAllItem();
				break;
			case 2:
				toggleAllItem();
				break;
			
		}
		return false;
	}
	private void toggleAllItem() {
		// TODO Auto-generated method stub
		for(int i=0;i<directoryEntries.size();i++){
			if(!directoryEntries.get(i).isFolder()){
				listAdapter.map.put(i,!listAdapter.map.get(i) );
			}
		}
		listAdapter.notifyDataSetChanged();//����Adapter
	}
	private void unselectAllItem() {
		// TODO Auto-generated method stub
		for(int i=0;i<directoryEntries.size();i++){
			if(!directoryEntries.get(i).isFolder()){
				listAdapter.map.put(i, false);
			}
		}
		listAdapter.notifyDataSetChanged();
	}
	private void selectAllItem() {
		// TODO Auto-generated method stub
		for(int i=0;i<directoryEntries.size();i++){
			if(!directoryEntries.get(i).isFolder()){
				listAdapter.map.put(i, true);
			}
		}
		listAdapter.notifyDataSetChanged();
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}
	
}