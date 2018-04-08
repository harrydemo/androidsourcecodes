package com.yarin.android.FileManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class FileManager extends ListActivity
{
	private List<IconifiedText>	directoryEntries = new ArrayList<IconifiedText>();
	private File				currentDirectory = new File("/");
	private File 				myTmpFile 		 = null;
	private int 				myTmpOpt		 = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		browseToRoot();
		this.setSelection(0);
	}
	//����ļ�ϵͳ�ĸ�Ŀ¼
	private void browseToRoot() 
	{
		browseTo(new File("/"));
    }
	//������һ��Ŀ¼
	private void upOneLevel()
	{
		if(this.currentDirectory.getParent() != null)
			this.browseTo(this.currentDirectory.getParentFile());
	}
	//���ָ����Ŀ¼,������ļ�����д򿪲���
	private void browseTo(final File file)
	{
		this.setTitle(file.getAbsolutePath());
		if (file.isDirectory())
		{
			this.currentDirectory = file;
			fill(file.listFiles());
		}
		else
		{
			fileOptMenu(file);
		}
	}
	//��ָ���ļ�
	protected void openFile(File aFile)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(aFile.getAbsolutePath());
		// ȡ���ļ���
		String fileName = file.getName();
		// ���ݲ�ͬ���ļ����������ļ�
		if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
		{
			intent.setDataAndType(Uri.fromFile(file), "image/*");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
		{
			intent.setDataAndType(Uri.fromFile(file), "audio/*");
		}
		else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
		{
			intent.setDataAndType(Uri.fromFile(file), "video/*");
		}
		startActivity(intent);
	}
	//����������Ϊ����ListActivity��Դ
	private void fill(File[] files)
	{
		//����б�
		this.directoryEntries.clear();

		//���һ����ǰĿ¼��ѡ��
		this.directoryEntries.add(new IconifiedText(getString(R.string.current_dir), getResources().getDrawable(R.drawable.folder)));
		//������Ǹ�Ŀ¼�������һ��Ŀ¼��
		if (this.currentDirectory.getParent() != null)
			this.directoryEntries.add(new IconifiedText(getString(R.string.up_one_level), getResources().getDrawable(R.drawable.uponelevel)));

		Drawable currentIcon = null;
		for (File currentFile : files)
		{
			//�ж���һ���ļ��л���һ���ļ�
			if (currentFile.isDirectory())
			{
				currentIcon = getResources().getDrawable(R.drawable.folder);
			}
			else
			{
				//ȡ���ļ���
				String fileName = currentFile.getName();
				//�����ļ������ж��ļ����ͣ����ò�ͬ��ͼ��
				if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage)))
				{
					currentIcon = getResources().getDrawable(R.drawable.image);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingWebText)))
				{
					currentIcon = getResources().getDrawable(R.drawable.webtext);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage)))
				{
					currentIcon = getResources().getDrawable(R.drawable.packed);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio)))
				{
					currentIcon = getResources().getDrawable(R.drawable.audio);
				}
				else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo)))
				{
					currentIcon = getResources().getDrawable(R.drawable.video);
				}
				else
				{
					currentIcon = getResources().getDrawable(R.drawable.text);
				}
			}
			//ȷ��ֻ��ʾ�ļ���������ʾ·���磺/sdcard/111.txt��ֻ����ʾ111.txt
			int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
			this.directoryEntries.add(new IconifiedText(currentFile.getAbsolutePath().substring(currentPathStringLenght), currentIcon));
		}
		Collections.sort(this.directoryEntries);
		IconifiedTextListAdapter itla = new IconifiedTextListAdapter(this);
		//�������õ�ListAdapter��
		itla.setListItems(this.directoryEntries);
		//ΪListActivity���һ��ListAdapter
		this.setListAdapter(itla);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		// ȡ��ѡ�е�һ����ļ���
		String selectedFileString = this.directoryEntries.get(position).getText();
		
		if (selectedFileString.equals(getString(R.string.current_dir)))
		{
			//���ѡ�е���ˢ��
			this.browseTo(this.currentDirectory);
		}
		else if (selectedFileString.equals(getString(R.string.up_one_level)))
		{
			//������һ��Ŀ¼
			this.upOneLevel();
		}
		else
		{
					
			File clickedFile = null;
			clickedFile = new File(this.currentDirectory.getAbsolutePath()+ this.directoryEntries.get(position).getText());
			if(clickedFile != null)
				this.browseTo(clickedFile);
		}
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
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "�½�Ŀ¼").setIcon(R.drawable.addfolderr);
		menu.add(0, 1, 0, "ɾ��Ŀ¼").setIcon(R.drawable.delete);
		menu.add(0, 2, 0, "ճ���ļ�").setIcon(R.drawable.paste);
		menu.add(0, 3, 0, "��Ŀ¼").setIcon(R.drawable.goroot);
		menu.add(0, 4, 0, "��һ��").setIcon(R.drawable.uponelevel);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case 0:
				Mynew();
				break;
			case 1:
				//ע�⣺ɾ��Ŀ¼�������������������ṩ��
				//deleteFile��ɾ���ļ�����deleteFolder��ɾ������Ŀ¼��
				MyDelete();
				break;
			case 2:
				MyPaste();
				break;
			case 3:
				this.browseToRoot();
				break;
			case 4:
				this.upOneLevel();
				break;
		}
		return false;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		return super.onPrepareOptionsMenu(menu);
	}
	//ճ������
	public void MyPaste()
	{
		if ( myTmpFile == null )
		{
			Builder builder = new Builder(FileManager.this);
			builder.setTitle("��ʾ");
			builder.setMessage("û�и��ƻ���в���");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();	
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		else
		{
			if ( myTmpOpt == 0 )//���Ʋ���
			{
				if(new File(GetCurDirectory()+"/"+myTmpFile.getName()).exists())
				{
					Builder builder = new Builder(FileManager.this);
					builder.setTitle("ճ����ʾ");
					builder.setMessage("��Ŀ¼����ͬ���ļ����Ƿ���Ҫ���ǣ�");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									copyFile(myTmpFile,new File(GetCurDirectory()+"/"+myTmpFile.getName()));
									browseTo(new File(GetCurDirectory()));
								}
							});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}	
				else
				{
					copyFile(myTmpFile,new File(GetCurDirectory()+"/"+myTmpFile.getName()));
					browseTo(new File(GetCurDirectory()));
				}
			}
			else if(myTmpOpt == 1)//ճ������
			{
				if(new File(GetCurDirectory()+"/"+myTmpFile.getName()).exists())
				{
					Builder builder = new Builder(FileManager.this);
					builder.setTitle("ճ����ʾ");
					builder.setMessage("��Ŀ¼����ͬ���ļ����Ƿ���Ҫ���ǣ�");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									moveFile(myTmpFile.getAbsolutePath(),GetCurDirectory()+"/"+myTmpFile.getName());
									browseTo(new File(GetCurDirectory()));
								}
							});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}	
				else
				{
					moveFile(myTmpFile.getAbsolutePath(),GetCurDirectory()+"/"+myTmpFile.getName());
					browseTo(new File(GetCurDirectory()));	
				}
			}
		}
	}
	//ɾ�������ļ���
	public void MyDelete()
	{
		//ȡ�õ�ǰĿ¼
		File tmp=new File(this.currentDirectory.getAbsolutePath());
		//������һ��Ŀ¼
		this.upOneLevel();
		//ɾ��ȡ�õ�Ŀ¼
		if ( deleteFolder(tmp) )
		{
			Builder builder = new Builder(FileManager.this);
			builder.setTitle("��ʾ");
			builder.setMessage("ɾ���ɹ�");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();	
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		else 
		{
			Builder builder = new Builder(FileManager.this);
			builder.setTitle("��ʾ");
			builder.setMessage("ɾ��ʧ��");
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
		}
		this.browseTo(this.currentDirectory);	
	}
	//�½��ļ���
	public void Mynew()
	{
		final LayoutInflater factory = LayoutInflater.from(FileManager.this);
		final View dialogview = factory.inflate(R.layout.dialog, null);
		//����TextView
		((TextView) dialogview.findViewById(R.id.TextView_PROM)).setText("�������½��ļ��е����ƣ�");
		//����EditText
		((EditText) dialogview.findViewById(R.id.EditText_PROM)).setText("�ļ�������...");
		
		Builder builder = new Builder(FileManager.this);
		builder.setTitle("�½��ļ���");
		builder.setView(dialogview);
		builder.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String value = ((EditText) dialogview.findViewById(R.id.EditText_PROM)).getText().toString();
						if ( newFolder(value) )
						{
							Builder builder = new Builder(FileManager.this);
							builder.setTitle("��ʾ");
							builder.setMessage("�½��ļ��гɹ�");
							builder.setPositiveButton(android.R.string.ok,
									new AlertDialog.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											//���ȷ����ť֮��,����ִ����ҳ�еĲ���
											dialog.cancel();
										}
									});
							builder.setCancelable(false);
							builder.create();
							builder.show();
						}
						else
						{
							Builder builder = new Builder(FileManager.this);
							builder.setTitle("��ʾ");
							builder.setMessage("�½��ļ���ʧ��");
							builder.setPositiveButton(android.R.string.ok,
									new AlertDialog.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											//���ȷ����ť֮��,����ִ����ҳ�еĲ���
											dialog.cancel();
										}
									});
							builder.setCancelable(false);
							builder.create();
							builder.show();	
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						dialog.cancel();
					}
				});
		builder.show();
	}
	//�½��ļ���
	public boolean newFolder(String file)
	{
		File dirFile = new File(this.currentDirectory.getAbsolutePath()+"/"+file);
		try
		{
			if (!(dirFile.exists()) && !(dirFile.isDirectory()))
			{
				boolean creadok = dirFile.mkdirs();
				if (creadok)
				{
					this.browseTo(this.currentDirectory);
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}
	//ɾ���ļ�
    public boolean deleteFile(File file)
	{
		boolean result = false;
		if (file != null)
		{
			try
			{
				File file2 = file;
				file2.delete();
				result = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	} 
    //ɾ���ļ���
	public boolean deleteFolder(File folder)
	{
		boolean result = false;
		try
		{
			String childs[] = folder.list();
			if (childs == null || childs.length <= 0)
			{
				if (folder.delete())
				{
					result = true;
				}
			}
			else
			{
				for (int i = 0; i < childs.length; i++)
				{
					String childName = childs[i];
					String childPath = folder.getPath() + File.separator + childName;
					File filePath = new File(childPath);
					if (filePath.exists() && filePath.isFile())
					{
						if (filePath.delete())
						{
							result = true;
						}
						else
						{
							result = false;
							break;
						}
					}
					else if (filePath.exists() && filePath.isDirectory())
					{
						if (deleteFolder(filePath))
						{
							result = true;
						}
						else
						{
							result = false;
							break;
						}
					}
				}
				folder.delete();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	} 
	
	//�����ļ��������򿪣��������Ȳ���
	public void fileOptMenu(final File file)
	{
		OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				if (which == 0)
				{
					openFile(file);
				}
				else if (which == 1)
				{
					//�Զ���һ��������ĶԻ�����TextView��EditText����
					final LayoutInflater factory = LayoutInflater.from(FileManager.this);
					final View dialogview = factory.inflate(R.layout.rename, null);
					//����TextView����ʾ��Ϣ
					((TextView) dialogview.findViewById(R.id.TextView01)).setText("������");
					//����EditText������ʼֵ
					((EditText) dialogview.findViewById(R.id.EditText01)).setText(file.getName());
					
					Builder builder = new Builder(FileManager.this);
					builder.setTitle("������");
					builder.setView(dialogview);
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									//���ȷ��֮��
									String value = GetCurDirectory()+"/"+((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
									if(new File(value).exists())
									{
										Builder builder = new Builder(FileManager.this);
										builder.setTitle("������");
										builder.setMessage("�ļ����ظ����Ƿ���Ҫ���ǣ�");
										builder.setPositiveButton(android.R.string.ok,
												new AlertDialog.OnClickListener() {
													public void onClick(DialogInterface dialog, int which) {
														String str2 = GetCurDirectory()+"/"+((EditText) dialogview.findViewById(R.id.EditText01)).getText().toString();
														file.renameTo(new File(str2));
														browseTo(new File(GetCurDirectory()));
													}
												});
										builder.setNegativeButton(android.R.string.cancel,
												new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int which) {
														dialog.cancel();
													}
												});
										builder.setCancelable(false);
										builder.create();
										builder.show();
									}
									else 
									{
										//������
										file.renameTo(new File(value));
										browseTo(new File(GetCurDirectory()));
									}
								}
							});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
					builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
								public void onCancel(DialogInterface dialog) {
									dialog.cancel();
								}
							});
					builder.show();
				}
				else if ( which == 2 )
				{
					Builder builder = new Builder(FileManager.this);
					builder.setTitle("ɾ���ļ�");
					builder.setMessage("ȷ��ɾ��"+file.getName()+"��");
					builder.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if ( deleteFile(file) )
									{
										Builder builder = new Builder(FileManager.this);
										builder.setTitle("��ʾ�Ի���");
										builder.setMessage("ɾ���ɹ�");
										builder.setPositiveButton(android.R.string.ok,
												new AlertDialog.OnClickListener() {
													public void onClick(DialogInterface dialog, int which) {
														//���ȷ����ť֮��
														dialog.cancel();
														browseTo(new File(GetCurDirectory()));
													}
												});
										builder.setCancelable(false);
										builder.create();
										builder.show();
									}
									else 
									{
										Builder builder = new Builder(FileManager.this);
										builder.setTitle("��ʾ�Ի���");
										builder.setMessage("ɾ��ʧ��");
										builder.setPositiveButton(android.R.string.ok,
												new AlertDialog.OnClickListener() {
													public void onClick(DialogInterface dialog, int which) {
														//���ȷ����ť֮��
														dialog.cancel();
													}
												});
										builder.setCancelable(false);
										builder.create();
										builder.show();	
									}
								}
							});
					builder.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							});
					builder.setCancelable(false);
					builder.create();
					builder.show();
				}
				else if ( which == 3 )//����
				{
					//�������Ǹ��Ƶ��ļ�Ŀ¼
					myTmpFile = file;
					//����������0��ʾ���Ʋ���
					myTmpOpt = 0;
				}
				else if ( which == 4 )//����
				{
					//�������Ǹ��Ƶ��ļ�Ŀ¼
					myTmpFile = file;
					//����������0��ʾ���в���
					myTmpOpt = 1;	 
				}
			}
		};
		//��ʾ�����˵�
	    String[] menu={"��","������","ɾ��","����","����"};
	    new AlertDialog.Builder(FileManager.this)
	        .setTitle("��ѡ����Ҫ���еĲ���")
	        .setItems(menu,listener)
	        .show();
	}
	//�õ���ǰĿ¼�ľ���·��
	public String GetCurDirectory()
	{
		return this.currentDirectory.getAbsolutePath();
	}
	//�ƶ��ļ�
	public void moveFile(String source, String destination)
	{
		new File(source).renameTo(new File(destination));   
	}
	//�����ļ�
	public void copyFile(File src, File target)
	{
		InputStream in = null;
		OutputStream out = null;

		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(target);
			bin = new BufferedInputStream(in);
			bout = new BufferedOutputStream(out);

			byte[] b = new byte[8192];
			int len = bin.read(b);
			while (len != -1)
			{
				bout.write(b, 0, len);
				len = bin.read(b);
			}

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (bin != null)
				{
					bin.close();
				}
				if (bout != null)
				{
					bout.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
