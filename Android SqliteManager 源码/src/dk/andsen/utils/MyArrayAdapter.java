package dk.andsen.utils;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dk.andsen.asqlitemanager.R;

public class MyArrayAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final List<String> names;
	private final String[] filetypes;

	/**
	 * Build a list of files, directories based on the list of filenames.
	 * Directories are marked by the ic_folder, normal files by ic_document
	 * and file types from the list of file types with ic_app 
	 * @param context
	 * @param names List of files directories ending with /
	 * @param filtetypes String[] of file types to mark with ic_app
	 */
	public MyArrayAdapter(Activity context, List<String> names, String[] filtetypes) {
		super(context, R.layout.row_layout, names);
		this.context = context;
		this.names = names;
		this.filetypes = filtetypes;
	}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout

		ViewHolder holder;
		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.row_layout, null, true);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.label);
			holder.imageView = (ImageView) rowView.findViewById(R.id.icon);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		holder.textView.setText(names.get(position));
		// Change the icon according to type of file - folder, sqlite or other 
		String s = names.get(position);
		if (s.endsWith("/"))  {
			holder.imageView.setImageResource(R.drawable.ic_folder);
		} else if (fileType(s, filetypes))
			holder.imageView.setImageResource(R.drawable.ic_app);
		else
			holder.imageView.setImageResource(R.drawable.ic_document);
		return rowView;
	}
	
	/**
	 * Return true if the fileName ends with one of the types in fileTypes
	 * @param fileName
	 * @param fileTypes
	 * @return
	 */
	private boolean fileType(String fileName, String[] fileTypes) {
		if (fileTypes == null)
			return false;
		for (int i = 0; i < fileTypes.length; i++) {
			if (fileName.endsWith(fileTypes[i]))
				return true;
		}
		return false;
	}
}