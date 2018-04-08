package grimbo.android.demo.adapter;


import grimbo.android.demo.slidingmenu.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PurpleAdapter extends ArrayListAdapter<PurpleEntry> {
	
	public PurpleAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row=convertView;

		ViewHolder holder;

		if (row==null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			System.out.println("inflater"+inflater);
			row=inflater.inflate(R.layout.purple_row, null);

			holder = new ViewHolder();
			holder.image = (ImageView)row.findViewById(R.id.PurpleImageView);
			holder.text = (TextView)row.findViewById(R.id.PurpleRowTextView);

			row.setTag(holder);
		}
		else{
			holder = (ViewHolder) row.getTag();
		}

		if(mList.get(position).getText() != null){
			holder.text.setText(mList.get(position).getText());
		} else if(mList.get(position).getTextId() != null){
			holder.text.setText(mList.get(position).getTextId());
		}
		if(mList.get(position).getDrawable() != null){
			holder.image.setImageResource(mList.get(position).getDrawable());
		} else {
			holder.image.setVisibility(View.GONE);
		}

		return row;
	}
	
	/**
	 * Class implementing holder pattern,
	 * performance boost
	 * 
	 * @author Lukasz Wisniewski
	 */
	static class ViewHolder {
		ImageView image;
		TextView text;
	}

}
