package com.zhuyan.adapter;

import com.zhuyan.R;
import java.util.ArrayList;
import java.util.List;

import com.zhuyan.model.Person;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author qjc
 * 
 */
public class DialogListViewAdapter extends BaseAdapter {

	private final static String TAG = "DialogListViewAdapter";

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	private Context context;

	private List<Person> personList = null;
	
	public DialogListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		personList = getPersonList();
		Log.i(TAG,""+personList.size());
	}

	private List<Person> getPersonList() {
		List<Person> list = new ArrayList<Person>();
		String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME,
				Phone.NUMBER };
		ContentResolver resolver = context.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				list.add(new Person(contactName, phoneNumber));
			}
		}

		phoneCursor.close();
		return list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return personList.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return personList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.dialog_listview_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) row.findViewById(R.id.contact_item);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		holder.textView.setText(personList.get(position).getContactName() + ":"
				+ personList.get(position).getPhoneNumber());
		return row;
	}

	public static class ViewHolder {

		TextView textView;

	}

}
