package cn.sharp.android.ncr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.sharp.android.ncr.display.ContactPerson;
import cn.sharp.android.ncr.display.ContactPerson.OnSelectItemType;
import cn.sharp.android.ncr.ocr.OCRItems;
import cn.sharp.android.ncr.ocr.OCRManager;

public class DisplayRecResult extends ListActivity implements OnSelectItemType,
		OnClickListener, OnItemSelectedListener {
	private final static String TAG = "DisplayRecResult";
	public final static String ACTION = "cn.sharp.android.ncr.DisplayRecResult";
	private final static int DIALOG_SHOW_CONTACT_ITEM_PHONE = 0;
	private final static int DIALOG_SHOW_CONTACT_ITEM_EMAIL = 1;
	private final static int DIALOG_SHOW_CONTACT_ITEM_ADDRESS = 2;
	private final static int DIALOG_SHOW_CONTACT_ITEM_ORG = 3;
	private final static int DIALOG_SHOW_CONTACT_ITEM_URL = 4;

	private final static int DIALOG_NAME_EMPTY = 5;

	private final static int DIALOG_SAVING_CONTACT_PROGRESS = 6;
	private final static int DIALOG_DISCARD_CONTACT_CONFIRM = 7;
	private final static int DIALOG_CONTACT_NOT_SAVED_WARNING = 8;

	private final static int MENU_SAVE_CONTACT = 0;
	private final static int MENU_DISCARD_CONTACT = 1;
	private ViewGroup phoneViewGroup, emailViewGroup, addressViewGroup,
			orgViewGroup, urlViewGroup;
	private EditText nameText, noteText;
	private Spinner contactGroupSpinner;
	private Button btnSave, btnDiscard;
	private ImageButton btnAddPhone, btnAddEmail, btnAddAddress, btnAddOrg,
			btnAddUrl;
	private TextView nameLabel, phoneLabel, emailLabel, addressLabel, orgLabel,
			urlLabel, noteLabel;
	private View nameViewGroup, noteViewGroup, opViewGroup;
	private LayoutInflater inflater;

	private ContactPerson contact;
	private List<ContactGroup> contactGroupList;
	private int contactGroupIndexSelected;;

	private class DataHolder {
		public int contactGroupIndexSelected;
		public List<ContactGroup> contactGroupList;
		public ContactPerson contact;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		nameViewGroup = inflater.inflate(R.layout.contact_info_name, null);
		contactGroupSpinner = (Spinner) nameViewGroup
				.findViewById(R.id.contact_group);
		nameLabel = (TextView) nameViewGroup
				.findViewById(R.id.contact_name_label);
		nameLabel.setText(R.string.contact_label_name);

		nameText = (EditText) nameViewGroup.findViewById(R.id.contact_name);

		phoneViewGroup = (ViewGroup) inflater.inflate(
				R.layout.contact_info_common, null);
		phoneLabel = (TextView) phoneViewGroup
				.findViewById(R.id.contact_info_label);
		phoneLabel.setText(R.string.contact_label_phone);
		btnAddPhone = (ImageButton) phoneViewGroup
				.findViewById(R.id.contact_info_add_item);

		emailViewGroup = (ViewGroup) inflater.inflate(
				R.layout.contact_info_common, null);
		emailLabel = (TextView) emailViewGroup
				.findViewById(R.id.contact_info_label);
		emailLabel.setText(R.string.contact_label_email);
		btnAddEmail = (ImageButton) emailViewGroup
				.findViewById(R.id.contact_info_add_item);

		addressViewGroup = (ViewGroup) inflater.inflate(
				R.layout.contact_info_common, null);
		addressLabel = (TextView) addressViewGroup
				.findViewById(R.id.contact_info_label);
		addressLabel.setText(R.string.contact_label_address);
		btnAddAddress = (ImageButton) addressViewGroup
				.findViewById(R.id.contact_info_add_item);

		orgViewGroup = (ViewGroup) inflater.inflate(
				R.layout.contact_info_common, null);
		orgLabel = (TextView) orgViewGroup
				.findViewById(R.id.contact_info_label);
		orgLabel.setText(R.string.contact_label_org);
		btnAddOrg = (ImageButton) orgViewGroup
				.findViewById(R.id.contact_info_add_item);

		urlViewGroup = (ViewGroup) inflater.inflate(
				R.layout.contact_info_common, null);
		urlLabel = (TextView) urlViewGroup
				.findViewById(R.id.contact_info_label);
		urlLabel.setText(R.string.contact_label_url);
		btnAddUrl = (ImageButton) urlViewGroup
				.findViewById(R.id.contact_info_add_item);

		noteViewGroup = inflater.inflate(R.layout.rec_result_row_single2, null);
		noteLabel = (TextView) noteViewGroup
				.findViewById(R.id.contact_info_item_label);
		noteLabel.setText(R.string.contact_label_note);

		noteText = (EditText) noteViewGroup
				.findViewById(R.id.contact_info_item_value);

		opViewGroup = inflater
				.inflate(R.layout.contact_info_save_discard, null);

		btnSave = (Button) opViewGroup.findViewById(R.id.btn_save_contact);
		btnDiscard = (Button) opViewGroup.findViewById(R.id.btn_discard);

		btnAddPhone.setOnClickListener(this);
		btnAddEmail.setOnClickListener(this);
		btnAddAddress.setOnClickListener(this);
		btnAddOrg.setOnClickListener(this);
		btnAddUrl.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnDiscard.setOnClickListener(this);

		ContactInfoAdapter adapter = new ContactInfoAdapter();
		setListAdapter(adapter);

		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			Log.i(TAG, "data==null");
			contactGroupIndexSelected = queryContactGroups();

			OCRItems ocrItems = (OCRItems) getIntent().getSerializableExtra(
					OCRManager.OCR_ITEMS);
			if (ocrItems != null) {
				Log.d(TAG, "get ocritems from intent");
				contact = new ContactPerson(this, ocrItems);
				contact.setOnSelectItemType(this);
				if (contactGroupList.size() > 0) {
					contact.setGroupId(contactGroupList
							.get(contactGroupIndexSelected).id);
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String dateStr = dateFormat.format(Calendar.getInstance()
						.getTime());
				contact.updateItemValue(ContactPerson.ITEM_NOTE, -1,
						getResources().getString(R.string.contact_default_note)
								+ "\t" + dateStr);
			} else {
				Log.e(TAG, "ocritem==null, activity exit now");
				setResult(RESULT_CANCELED);
				finish();
			}
		} else {
			Log.i(TAG, "get previously saved data object");
			DataHolder dataSaved = (DataHolder) data;
			contactGroupIndexSelected = dataSaved.contactGroupIndexSelected;
			contact = dataSaved.contact;
			contactGroupList = dataSaved.contactGroupList;
			contact.registerNewContext(this);
		}

		if (contactGroupList.size() > 0) {
			List<CharSequence> groupNameList = new ArrayList<CharSequence>();
			for (int i = 0; i < contactGroupList.size(); i++) {
				groupNameList.add(contactGroupList.get(i).name);
			}
			ArrayAdapter<CharSequence> contactGroupAdapter = new ArrayAdapter<CharSequence>(
					this, android.R.layout.simple_spinner_item, groupNameList);
			contactGroupAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			contactGroupSpinner.setAdapter(contactGroupAdapter);
			if (contactGroupIndexSelected < contactGroupList.size())
				contactGroupSpinner.setSelection(contactGroupIndexSelected);
			else
				contactGroupSpinner.setSelection(0);
		} else {
			Log.e(TAG, "no contact group found");
		}

		contact.setNameView(nameText);
		contact.setPhoneViewGroup(phoneViewGroup);
		contact.setEmailViewGroup(emailViewGroup);
		contact.setAddressViewGroup(addressViewGroup);
		contact.setOrgViewGroup(orgViewGroup);
		contact.setUrlViewGroup(urlViewGroup);
		contact.setNoteView(noteText);

		contact.bindDataToView();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MessageId.SAVE_CONTACT_SUCCESS:
				dismissDialog(DIALOG_SAVING_CONTACT_PROGRESS);
				Toast.makeText(DisplayRecResult.this, R.string.contact_saved,
						Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "onRetainNonConfigurationInstance");
		updateContact();
		DataHolder dataHolder = new DataHolder();
		dataHolder.contactGroupIndexSelected = contactGroupIndexSelected;
		dataHolder.contact = contact;
		dataHolder.contactGroupList = contactGroupList;
		contact.removeAllContextObject();
		return dataHolder;
	}

	private int queryContactGroups() {
		String[] projection = new String[] { Contacts.Groups._ID,
				Contacts.Groups.NAME, Contacts.Groups.SYSTEM_ID };
		Cursor cursor = getContentResolver().query(Contacts.Groups.CONTENT_URI,
				projection, null, null, null);
		contactGroupList = new ArrayList<ContactGroup>();
		int defaultPos = 0;
		if (cursor.moveToFirst()) {
			do {
				ContactGroup group = new ContactGroup();
				group.id = cursor.getLong(0);
				group.name = cursor.getString(1);
				group.systemId = cursor.getString(2);
				if (group.systemId != null)
					defaultPos = contactGroupList.size();
				contactGroupList.add(group);
			} while (cursor.moveToNext());
		}
		return defaultPos;
	}

	private class ContactInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 8;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return super.isEmpty();
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			switch (position) {
			case 0:
				return nameViewGroup;
			case 1:
				return phoneViewGroup;
			case 2:
				return emailViewGroup;
			case 3:
				return addressViewGroup;
			case 4:
				return orgViewGroup;
			case 5:
				return urlViewGroup;
			case 6:
				return noteViewGroup;
			case 7:
				return opViewGroup;
			}
			return convertView;
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SHOW_CONTACT_ITEM_PHONE:
		case DIALOG_SHOW_CONTACT_ITEM_EMAIL:
		case DIALOG_SHOW_CONTACT_ITEM_ADDRESS:
		case DIALOG_SHOW_CONTACT_ITEM_ORG:
		case DIALOG_SHOW_CONTACT_ITEM_URL:
			Log.v(TAG, "contact info type str[0]"
					+ currentContactInfoTypeStrs[0]);
			DialogClickListener clickListener = new DialogClickListener(id,
					currentContactInfoTypes, currentItemName, currentItemIndex);
			return new AlertDialog.Builder(this).setItems(
					currentContactInfoTypeStrs, clickListener).create();
		case DIALOG_SAVING_CONTACT_PROGRESS:
			ProgressDialog savingContactProgress = ProgressDialog.show(
					DisplayRecResult.this, "", getResources().getString(
							R.string.saving_contact_progress), true);
			savingContactProgress.setCancelable(false);
			return savingContactProgress;
		case DIALOG_DISCARD_CONTACT_CONFIRM:
			return new AlertDialog.Builder(this).setTitle(R.string.attention)
					.setMessage(R.string.discard_contact_confirm).setIcon(
							android.R.drawable.ic_dialog_alert)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Log.d(TAG, "contact discarded by user");
									setResult(RESULT_CANCELED);
									finish();
								}
							}).setNegativeButton(R.string.no, null).create();
		case DIALOG_CONTACT_NOT_SAVED_WARNING:
			return new AlertDialog.Builder(this).setTitle(R.string.attention)
					.setMessage(R.string.contact_not_saved_warning).setIcon(
							android.R.drawable.ic_dialog_alert)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									saveContact();
								}
							}).setNeutralButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Log.d(TAG, "contact discarded by user");
									setResult(RESULT_CANCELED);
									finish();

								}
							}).setNegativeButton(R.string.cancel, null)
					.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem itemSave = menu.add(0, MENU_SAVE_CONTACT, 0,
				R.string.save_contact);
		MenuItem itemDiscard = menu.add(0, MENU_DISCARD_CONTACT, 0,
				R.string.discard);
		itemSave.setIcon(R.drawable.contact_save);
		itemDiscard.setIcon(R.drawable.contact_discard);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE_CONTACT:
			saveContact();
			return true;
		case MENU_DISCARD_CONTACT:
			showDialog(DIALOG_DISCARD_CONTACT_CONFIRM);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {
		int dialogId;
		int[] contactInfoTypes;
		int itemName;
		int itemIndex;

		public DialogClickListener(int dialogId, int[] contactInfoTypes,
				int itemName, int itemIndex) {
			this.dialogId = dialogId;
			this.contactInfoTypes = contactInfoTypes;
			this.itemName = itemName;
			this.itemIndex = itemIndex;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			int newType = contactInfoTypes[which];
			if (contact.updateItemType(itemName, itemIndex, newType)) {
				/**
				 * update the label of button
				 */
				ViewGroup viewGroup = null;
				switch (dialogId) {
				case DIALOG_SHOW_CONTACT_ITEM_PHONE:
					if (itemIndex < phoneViewGroup.getChildCount()) {
						// skip the label row
						viewGroup = (ViewGroup) phoneViewGroup
								.getChildAt(itemIndex + 1);
					} else {
						Log.e(TAG, "invalid item index:" + itemIndex);
					}
					break;
				case DIALOG_SHOW_CONTACT_ITEM_EMAIL:
					if (itemIndex < emailViewGroup.getChildCount()) {
						viewGroup = (ViewGroup) emailViewGroup
								.getChildAt(itemIndex + 1);
					}
					break;
				case DIALOG_SHOW_CONTACT_ITEM_ADDRESS:
					if (itemIndex < addressViewGroup.getChildCount()) {
						viewGroup = (ViewGroup) addressViewGroup
								.getChildAt(itemIndex + 1);
					}
					break;
				case DIALOG_SHOW_CONTACT_ITEM_ORG:
					if (itemIndex < orgViewGroup.getChildCount()) {
						viewGroup = (ViewGroup) orgViewGroup
								.getChildAt(itemIndex + 1);
					}
					break;
				case DIALOG_SHOW_CONTACT_ITEM_URL:
					if (itemIndex < urlViewGroup.getChildCount()) {
						viewGroup = (ViewGroup) urlViewGroup
								.getChildAt(itemIndex + 1);
					}
					break;
				}
				if (viewGroup != null && viewGroup.getChildCount() > 0) {
					Button button = (Button) viewGroup.getChildAt(0);
					if (button != null) {
						button.setText(contact.getTypeName(newType));
						Log.d(TAG, "update label of type button with newType:"
								+ newType + ", itemName:" + itemName
								+ ", itemIndex:" + itemIndex);
					} else {
						Log.e(TAG, "button==null, update button label failed");
					}
				} else {
					Log
							.e(
									TAG,
									"viewGroup==null or viewGroup's child count<0, update button label failed, dialogId:"
											+ dialogId);
				}
			}
		}
	}

	/**
	 * private class ContactDetailAdapter extends BaseAdapter { private
	 * LayoutInflater infalter;
	 * 
	 * public ContactDetailAdapter(Context context) { infalter =
	 * LayoutInflater.from(context); }
	 * 
	 * @Override public int getCount() { // TODO Auto-generated method stub
	 *           return 0; }
	 * @Override public Object getItem(int position) { // TODO Auto-generated
	 *           method stub return null; }
	 * @Override public long getItemId(int position) { // TODO Auto-generated
	 *           method stub return 0; }
	 * @Override public View getView(int position, View convertView, ViewGroup
	 *           parent) { ViewHolder holder = null; if (convertView == null) {
	 *           convertView = infalter.inflate(R.layout.rec_result_row, null);
	 *           holder = new ViewHolder(); holder.itemType = (Spinner)
	 *           convertView .findViewById(R.id.item_type); holder.itemValue =
	 *           (EditText) convertView .findViewById(R.id.item_value);
	 *           convertView.setTag(holder); } else { holder = (ViewHolder)
	 *           convertView.getTag(); }
	 * 
	 *           return convertView; }
	 * 
	 *           class ViewHolder { public Spinner itemType; public EditText
	 *           itemValue; } }
	 **/
	/**
	 * private class ContactGroupAdapter extends BaseAdapter { private Context
	 * context; private LayoutInflater inflater;
	 * 
	 * public ContactGroupAdapter(Context context) { this.context = context;
	 * this.inflater = LayoutInflater.from(context); }
	 * 
	 * @Override public int getCount() { return contactGroupList.size(); }
	 * @Override public Object getItem(int position) { return
	 *           contactGroupList.get(position); }
	 * @Override public long getItemId(int position) { return
	 *           contactGroupList.get(position).id; }
	 * @Override public View getView(int position, View convertView, ViewGroup
	 *           parent) { TextView textView = null; if (convertView == null) {
	 *           textView = new TextView(context); convertView = textView;
	 * 
	 *           } else { textView = (TextView) convertView; }
	 *           textView.setText(contactGroupList.get(position).name); return
	 *           convertView; }
	 * @Override public View getDropDownView(int position, View convertView,
	 *           ViewGroup parent) { TextView textView = null; if (convertView
	 *           == null) { convertView =
	 *           inflater.inflate(R.layout.contact_group_list, null); textView =
	 *           (TextView) convertView .findViewById(R.id.contact_group_item);
	 *           convertView.setTag(textView); } else { textView = (TextView)
	 *           convertView.getTag(); }
	 *           textView.setText(contactGroupList.get(position).name); return
	 *           convertView; }
	 * 
	 *           }
	 **/

	public class ContactGroup {
		public long id;
		public String systemId;
		public String name;
	}

	private String[] currentContactInfoTypeStrs;
	private int[] currentContactInfoTypes;
	private int currentItemName, currentItemIndex;

	@Override
	public void displayItems(ContactPerson contact, int itemName, int index,
			int[] types, String[] typeStrs) {
		currentItemName = itemName;
		currentItemIndex = index;
		currentContactInfoTypes = types;
		currentContactInfoTypeStrs = typeStrs;

		int id = -1;

		switch (itemName) {
		case ContactPerson.ITEM_PHONE:
			// showDialog(DIALOG_SHOW_CONTACT_ITEM_PHONE);
			id = DIALOG_SHOW_CONTACT_ITEM_PHONE;
			break;
		case ContactPerson.ITEM_EMAIL:
			// showDialog(DIALOG_SHOW_CONTACT_ITEM_EMAIL);
			id = DIALOG_SHOW_CONTACT_ITEM_EMAIL;
			break;
		case ContactPerson.ITEM_ADDRESS:
			// showDialog(DIALOG_SHOW_CONTACT_ITEM_ADDRESS);
			id = DIALOG_SHOW_CONTACT_ITEM_ADDRESS;
			break;
		case ContactPerson.ITEM_ORG:
			// showDialog(DIALOG_SHOW_CONTACT_ITEM_ORG);
			id = DIALOG_SHOW_CONTACT_ITEM_ORG;
			break;
		case ContactPerson.ITEM_URL:
			// showDialog(DIALOG_SHOW_CONTACT_ITEM_URL);
			id = DIALOG_SHOW_CONTACT_ITEM_URL;
			break;
		default:
			Log.e(TAG, "itemName with no multi types:" + itemName);
		}
		if (id != -1) {
			// DialogClickListener clickListener = new DialogClickListener(id,
			// types, currentItemName, currentItemIndex);
			// AlertDialog alertDialog = new AlertDialog.Builder(this).setItems(
			// typeStrs, clickListener).create();
			// alertDialog.show();
			Log.d(TAG, "currentContactInfoTypeStrs :"
					+ currentContactInfoTypeStrs.length);
			showDialog(id);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnSave) {
			Log.d(TAG, "Save button clicked");
			saveContact();
		} else if (v == btnDiscard) {
			Log.d(TAG, "Discard button clicked");
			showDialog(DIALOG_DISCARD_CONTACT_CONFIRM);
		} else if (v == btnAddPhone) {
			Log.d(TAG, "insert phone-add row");
			contact.add(ContactPerson.ITEM_PHONE, null);

		} else if (v == btnAddEmail) {
			contact.add(ContactPerson.ITEM_EMAIL, null);
		} else if (v == btnAddAddress) {
			contact.add(ContactPerson.ITEM_ADDRESS, null);
		} else if (v == btnAddOrg) {
			contact.add(ContactPerson.ITEM_ORG, null);
		} else if (v == btnAddUrl) {
			contact.add(ContactPerson.ITEM_URL, null);
		}
	}

	private void saveContact() {
		updateContact();
		contact.startSave(false, handler);
		showDialog(DIALOG_SAVING_CONTACT_PROGRESS);
	}

	private void updateContact() {
		String name = nameText.getText().toString().trim();
		contact.updateItemValue(ContactPerson.ITEM_NAME, -1, name);
		for (int i = 1; i < phoneViewGroup.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) phoneViewGroup.getChildAt(i);
			EditText text = (EditText) viewGroup.getChildAt(2);
			if (text != null) {
				String value = text.getText().toString().trim();
				if (value.length() > 0) {
					contact.updateItemValue(ContactPerson.ITEM_PHONE, i - 1,
							value);
					Log.d(TAG, "update phone item " + i);
				} else {
					Log.i(TAG, "phone item " + i + " is empty, ignored");
				}
			} else {
				Log.e(TAG, "edittext is null when getting phones");
			}
		}

		for (int i = 1; i < emailViewGroup.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) emailViewGroup.getChildAt(i);
			EditText text = (EditText) viewGroup.getChildAt(2);
			if (text != null) {
				String value = text.getText().toString().trim();
				if (value.length() > 0) {
					contact.updateItemValue(ContactPerson.ITEM_EMAIL, i - 1,
							value);
					Log.d(TAG, "update email item " + i);
				} else {
					Log.i(TAG, "email item " + i + " is empty, ignored");
				}
			} else {
				Log.e(TAG, "edittext is null when getting emails");
			}
		}

		for (int i = 1; i < addressViewGroup.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) addressViewGroup.getChildAt(i);
			EditText text = (EditText) viewGroup.getChildAt(2);
			if (text != null) {
				String value = text.getText().toString().trim();
				if (value.length() > 0) {
					contact.updateItemValue(ContactPerson.ITEM_ADDRESS, i - 1,
							value);
					Log.d(TAG, "update address item " + i);
				} else {
					Log.i(TAG, "address item " + i + " is empty, ignored");
				}
			} else {
				Log.e(TAG, "edittext is null when getting address");
			}
		}

		for (int i = 1; i < orgViewGroup.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) orgViewGroup.getChildAt(i);
			EditText text1 = (EditText) viewGroup.getChildAt(2);
			EditText text2 = (EditText) viewGroup.getChildAt(3);
			if (text1 != null && text2 != null) {
				String value1 = text1.getText().toString().trim();
				String value2 = text2.getText().toString().trim();
				if (value1.length() > 0 || value2.length() > 0) {
					contact.updateItemValue(ContactPerson.ITEM_ORG, i - 1,
							value1, value2);
					Log.d(TAG, "update org item " + i);
				} else {
					Log.i(TAG, "org item " + (i - 1) + " is empty, ignored");
				}
			} else {
				Log.e(TAG, "edittext is null when getting org");
			}
		}

		for (int i = 1; i < urlViewGroup.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) urlViewGroup.getChildAt(i);
			EditText text = (EditText) viewGroup.getChildAt(2);
			if (text != null) {
				String value = text.getText().toString().trim();
				if (value.length() > 0) {
					contact.updateItemValue(ContactPerson.ITEM_URL, i - 1,
							value);
					Log.d(TAG, "update url item " + (i - 1));
				} else {
					Log.i(TAG, "url item " + (i - 1) + " is empty, ignored");
				}
			} else {
				Log.e(TAG, "edittext is null when getting url");
			}
		}

		String note = noteText.getText().toString();
		if (note.length() >= 0) {
			Log.d(TAG, "update note:" + note);
			contact.updateItemValue(ContactPerson.ITEM_NOTE, -1, note);
		} else {
			Log.i(TAG, "note is empty, ignored");
			// do *NOT* remove note whether it is empty
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showDialog(DIALOG_CONTACT_NOT_SAVED_WARNING);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg2 < contactGroupList.size()) {
			Log.d(TAG, "update contact group id to "
					+ contactGroupList.get(arg2).id);
			contact.setGroupId(contactGroupList.get(arg2).id);
			contactGroupIndexSelected = arg2;
		} else {
			Log.e(TAG, "invalid group id");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}
