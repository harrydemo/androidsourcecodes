package net.sf.andpdf.pdfviewer;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Bob
 * @date July 18, 2009
 */
public class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Bitmap mIconRoot;
	private Bitmap mIconUp;
	private Bitmap mIconFolder;
	private Bitmap mIconDoc;
	private Bitmap mIconPdf;
	
	private List<String> items;
	private List<String> paths;
	
	public MyAdapter(Context context, List<String> it, List<String> pa)
	{
		mInflater = LayoutInflater.from(context);
		items = it;
		paths = pa;
		mIconRoot = BitmapFactory.decodeResource(context.getResources(), R.drawable.back01);
		mIconUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.back02);
		mIconFolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.folder);
		mIconDoc = BitmapFactory.decodeResource(context.getResources(), R.drawable.doc);
		mIconPdf = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.file_row, null);
			holder = new ViewHolder();
			holder.text = (TextView)convertView.findViewById(R.id.text);
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		File f = new File(paths.get(position).toString());
		if(items.get(position).toString().equals("back2root"))
		{
			holder.text.setText("Back to /");
			holder.icon.setImageBitmap(mIconRoot);
		}
		else if(items.get(position).toString().equals("back2up"))
		{
			holder.text.setText("Back to ..");
			holder.icon.setImageBitmap(mIconUp);
		}
		else
		{
			String fName = f.getName();
			holder.text.setText(fName);			
			if(f.isDirectory())
				holder.icon.setImageBitmap(mIconFolder);
			else if(fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase().equals("pdf"))
				holder.icon.setImageBitmap(mIconPdf);
			else
				holder.icon.setImageBitmap(mIconDoc);
		}
		return convertView;
	}
	
	/** class ViewHolder*/
	private class ViewHolder
	{
		TextView text;
		ImageView icon;
	}

}
