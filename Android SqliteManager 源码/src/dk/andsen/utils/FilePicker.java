/**
 * Part of one of andsens open source project (a41cv / aSQLiteManager) 
 *
 * @author andsen
 *
 */
package dk.andsen.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dk.andsen.asqlitemanager.DBViewer;
import dk.andsen.asqlitemanager.R;

/**
 * @author andsen
 *
 */
public class FilePicker extends ListActivity {

	private List<String> item = null;
	private List<String> path = null;
	private String root="/";
	private TextView myPath;
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	private Context context = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filepicker);
		myPath = (TextView)findViewById(R.id.path);
		context = this.getBaseContext();
		File path = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			path = Environment.getExternalStorageDirectory();
			File programDirectory = new File(path.getAbsolutePath());
			// have the object build the directory structure, if needed.
			programDirectory.mkdirs();
			getDir(programDirectory.getAbsolutePath());
		} else {
			// No SDCard
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.sqlite_icon)
			.setTitle(getText(R.string.NoSDCard))
			.setPositiveButton(R.string.OK, 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
	}

	/**
	 * Updates the item and path lists
	 * @param dirPath
	 */
	private void getDir(String dirPath)
	{
		myPath.setText(getText(R.string.Path)+ " " + dirPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(dirPath);
		File[] files = f.listFiles();
		if(!dirPath.equals(root))
		{
			item.add(root);
			path.add(root);
			item.add("../");
			path.add(f.getParent());
		}
		Arrays.sort(files, new FileComparator());
		for(int i=0; i < files.length; i++)
		{
			File file = files[i];
			path.add(file.getPath());
			if(file.isDirectory())
				item.add(file.getName() + "/");
			else
				item.add(file.getName());
		}
		ArrayAdapter<String> fileList =
			new ArrayAdapter<String>(this, R.layout.filerow, item);
		setListAdapter(fileList);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final File file = new File(path.get(position));
		if (file.isDirectory())
		{
			if(file.canRead())
				getDir(path.get(position));
			else
			{
				new AlertDialog.Builder(this)
				.setIcon(R.drawable.sqlite_icon)
				.setTitle("[" + file.getName() + "] " + getText(R.string.ReadOnlyFolder))
				.setPositiveButton("OK", 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
			}
		} else {
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.sqlite_icon)
			.setTitle(getText(R.string.Open) + "\n [" + file.getName() + "]?")
			.setPositiveButton("OK", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent i = new Intent(context, DBViewer.class);
					i.putExtra("db", ""+ file.getAbsolutePath());
					startActivity(i);
				}
			}).setNegativeButton(getText(R.string.No), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//dialog.dismiss();
				}
			}) .show();
		}
	}
	
	/**
	 * Sort files first directories then files	
	 * @author andsen
	 *
	 */
	class FileComparator implements Comparator<File> {
	   
	    public int compare(File file1, File file2){
	    	String f1 = ((File)file1).getName();
	    	String f2 = ((File)file2).getName();
	    	int f1Length = f1.length();
	    	int f2Length = f2.length();
	    	boolean f1Dir = (((File)file1).isDirectory()) ? true: false;
	    	boolean f2Dir = (((File)file2).isDirectory()) ? true: false;
	    	int shortest = (f1Length > f2Length) ? f2Length : f1Length;
	    	// one of the files is a directory
	    	if (f1Dir && !f2Dir)
	    		return -1;
	    	if (f2Dir && !f1Dir)
	    		return 1;
	    	// sort alphabetically
	    	for (int i = 0; i < shortest; i++) {
	    		if (f1.charAt(i) > f2.charAt(i))
	    			return 1;
	    		else if (f1.charAt(i) < f2.charAt(i))
	    			return -1;
	    	}
	    	if (f1Length > f2Length)
	    		return 1;
	    	else
	    		return 0; 
	    }
	}
}