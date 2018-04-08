package cn.sharp.android.ncr.display;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.sharp.android.ncr.MessageId;
import cn.sharp.android.ncr.R;
import cn.sharp.android.ncr.display.domain.Address;
import cn.sharp.android.ncr.display.domain.BaseDomain;
import cn.sharp.android.ncr.display.domain.Email;
import cn.sharp.android.ncr.display.domain.Organization;
import cn.sharp.android.ncr.display.domain.Phone;
import cn.sharp.android.ncr.display.domain.Url;
import cn.sharp.android.ncr.ocr.OCRItems;

public class ContactPerson {
	private final static String TAG = "ContactPerson";
	public final static int ITEM_NAME = 1;
	public final static int ITEM_PHONE = 2;
	public final static int ITEM_EMAIL = 3;
	public final static int ITEM_ADDRESS = 4;
	public final static int ITEM_ORG = 5;
	public final static int ITEM_URL = 6;
	public final static int ITEM_NOTE = 7;

	public final static int TYPE_HOME = 1;
	public final static int TYPE_WORK = 2;
	public final static int TYPE_OTHER = 3;
	public final static int TYPE_PAGER = 4;
	public final static int TYPE_MOBILE = 5;
	public final static int TYPE_FAX_HOME = 6;
	public final static int TYPE_FAX_WORK = 7;

	private long groupId;
	private String name;
	private List<Email> emails;
	private List<Phone> phones;
	private List<Organization> orgs;
	private List<Address> addresses;
	private List<Url> urls;
	private String note;
	public String typeHome, typeWork, typeOther, typePager, typeMobile,
			typeFaxHome, typeFaxWork;
	private int[] supportedItemTypes1;
	private int[] supportedItemTypes2;
	private int[] supportedItemTypes3;

	private String[] supportedItemTypeStr1;
	private String[] supportedItemTypeStr2;
	private String[] supportedItemTypeStr3;

	private OnSelectItemType onSelectItemType;
	private ContentResolver contentResolver;
	private SelectItemTypeListener onSelectTypeListener;
	private RemoveItemListener removeItemListener;

	private EditText nameView, noteView;
	private ViewGroup phoneViewGroup, emailViewGroup, addressViewGroup,
			orgViewGroup, urlViewGroup;

	private Button itemType;
	private EditText itemValue, itemValue2;
	private ImageButton removeItem;

	private LayoutInflater inflater;

	/**
	 * remove all Context reference, or it will result in memory leak
	 */
	public void removeAllContextObject() {
		onSelectItemType = null;
		contentResolver = null;
		onSelectTypeListener = null;
		removeItemListener = null;
		nameView = null;
		noteView = null;
		phoneViewGroup = emailViewGroup = addressViewGroup = orgViewGroup = urlViewGroup = null;
		inflater = null;
		itemType = null;
		itemValue = null;
		itemValue2 = null;
		removeItem = null;
	}

	public void registerNewContext(Context context) {
		init(context);
	}

	private void initPrimitiveObj() {
		groupId = -1;
		phones = new ArrayList<Phone>();
		orgs = new ArrayList<Organization>();
		emails = new ArrayList<Email>();
		addresses = new ArrayList<Address>();
		urls = new ArrayList<Url>();
		supportedItemTypes1 = new int[] { TYPE_HOME, TYPE_WORK, TYPE_MOBILE,
				TYPE_OTHER, TYPE_PAGER, TYPE_FAX_HOME, TYPE_FAX_WORK };
		supportedItemTypes2 = new int[] { TYPE_WORK, TYPE_OTHER };
		supportedItemTypes3 = new int[] { TYPE_WORK, TYPE_HOME, TYPE_OTHER };

		supportedItemTypeStr1 = new String[] { typeHome, typeWork, typeMobile,
				typeOther, typePager, typeFaxHome, typeFaxWork };
		supportedItemTypeStr2 = new String[] { typeWork, typeOther };
		supportedItemTypeStr3 = new String[] { typeWork, typeHome, typeOther };
		onSelectTypeListener = new SelectItemTypeListener();
		removeItemListener = new RemoveItemListener();
	}

	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		contentResolver = context.getContentResolver();
		Resources resources = context.getResources();
		typeHome = resources.getString(R.string.type_home);
		typeWork = resources.getString(R.string.type_work);
		typeOther = resources.getString(R.string.type_other);
		typePager = resources.getString(R.string.type_pager);
		typeMobile = resources.getString(R.string.type_mobile);
		typeFaxHome = resources.getString(R.string.type_fax_home);
		typeFaxWork = resources.getString(R.string.type_fax_work);
	}

	public ContactPerson(Context context) {
		init(context);
		initPrimitiveObj();
	}

	public ContactPerson(Context context, OCRItems items) {
		init(context);
		initPrimitiveObj();
		if (items.name != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < items.name.length; i++) {
				sb.append(items.name[i]);
			}
			name = sb.toString();
		}
		if (items.fax != null) {
			for (int i = 0; i < items.fax.length; i++) {
				Phone phone = new Phone();
				phone.type = TYPE_FAX_WORK;
				phone.value = items.fax[i];
				phones.add(phone);
			}
		}
		if (items.telephone != null) {
			for (int i = 0; i < items.telephone.length; i++) {
				Phone phone = new Phone();
				phone.type = TYPE_MOBILE;
				phone.value = items.telephone[i];
				phones.add(phone);
			}
		}
		if (items.cellphone != null) {
			for (int i = 0; i < items.cellphone.length; i++) {
				Phone phone = new Phone();
				phone.type = TYPE_MOBILE;
				phone.value = items.cellphone[i];
				phones.add(phone);
			}
		}
		if (items.organization != null) {
			for (int i = 0; i < items.organization.length; i++) {
				Organization org = new Organization();
				org.company = items.organization[i];
				org.type = TYPE_WORK;
				if (items.department != null && items.department.length > i) {
					org.company += "," + items.department[i];
				}
				if (items.title != null && items.title.length > i) {
					org.title = items.title[i];
				} else {
					org.title = "";
				}
				orgs.add(org);
			}
			/**
			 * add the remained items.department fields
			 */
			if (items.department != null) {
				for (int i = items.organization.length; i < items.department.length; i++) {
					Organization org = new Organization();
					org.company = items.department[i];
					org.type = TYPE_WORK;
					if (items.title != null && items.title.length > i) {
						org.title = items.title[i];
					} else {
						org.title = "";
					}
					orgs.add(org);
				}
			}
		} else if (items.department != null) {
			/**
			 * if items.orgnization==null&&items.department!=null, treat
			 * items.department as org.company value
			 */
			for (int i = 0; i < items.department.length; i++) {
				Organization org = new Organization();
				org.company = items.department[i];
				org.type = TYPE_WORK;
				if (items.title != null && items.title.length > i) {
					org.title = items.title[i];
				} else {
					org.title = "";
				}
				orgs.add(org);
			}
		}
		if (items.email != null) {
			for (int i = 0; i < items.email.length; i++) {
				Email email = new Email();
				email.type = TYPE_WORK;
				email.value = items.email[i];
				emails.add(email);
			}
		}
		if (items.address != null) {
			for (int i = 0; i < items.address.length; i++) {
				Address address = new Address();
				address.type = TYPE_WORK;
				address.value = items.address[i];
				addresses.add(address);
				if (items.postcode != null && items.postcode.length > i) {
					address.postcode = items.postcode[i];
				} else {
					address.postcode = "";
				}
			}
		}
		if (items.url != null) {
			for (int i = 0; i < items.url.length; i++) {
				Url url = new Url();
				url.type = TYPE_WORK;
				url.value = items.url[i];
				urls.add(url);
			}
		}
	}

	/**
	 * bind the data to corresponding views
	 * 
	 * @param nameView
	 * @param phoneViewGroup
	 * @param orgViewGroup
	 * @param emailViewGroup
	 * @param addressViewGroup
	 * @param urlGroups
	 * @param noteView
	 */
	public void bindDataToView() {
		Log.d(TAG, "org list size:" + orgs.size());
		if (nameView != null) {
			if (name == null) {
				name = "";
			}
			name = name.trim();
			nameView.setText(name);
		}
		if (phoneViewGroup != null) {
			for (int i = 0; i < phones.size(); i++) {
				addPhoneItem(phones.get(i), false);
			}
		}

		if (emailViewGroup != null) {
			for (int i = 0; i < emails.size(); i++) {
				addEmailItem(emails.get(i), false);
			}
		}

		if (addressViewGroup != null) {
			for (int i = 0; i < addresses.size(); i++) {
				addAddressItem(addresses.get(i), false);
			}
		}

		if (orgViewGroup != null) {
			for (int i = 0; i < orgs.size(); i++) {
				addOrgItem(orgs.get(i), false);
			}
		}
		if (urlViewGroup != null) {
			for (int i = 0; i < urls.size(); i++) {
				addUrlItem(urls.get(i), false);
			}
		}

		if (noteView != null) {
			if (note == null) {
				note = "";
			}
			note = note.trim();
			noteView.setText(note);
		}
	}

	private View inflateViews() {
		View row = inflater.inflate(R.layout.rec_result_row_single, null);
		itemType = (Button) row.findViewById(R.id.item_type);
		itemValue = (EditText) row.findViewById(R.id.item_value);
		removeItem = (ImageButton) row.findViewById(R.id.btn_remove_item);
		return row;
	}

	private View inflateViews2() {
		View row = inflater.inflate(R.layout.rec_result_row_twoline, null);
		itemType = (Button) row.findViewById(R.id.item_type_twoline);
		itemValue = (EditText) row.findViewById(R.id.item_value_twoline_1);
		itemValue2 = (EditText) row.findViewById(R.id.item_value_twoline_2);
		removeItem = (ImageButton) row
				.findViewById(R.id.btn_remove_item_two_line);
		return row;
	}

	public synchronized void startSave(boolean merge, Handler handler) {
		Thread thread = new SaveContactThread(merge, handler);
		thread.start();
	}

	public class SaveContactThread extends Thread {
		private boolean merge;
		private Handler handler;

		public SaveContactThread() {
			merge = false;
		}

		public SaveContactThread(boolean merge) {
			this.merge = merge;
		}

		public SaveContactThread(boolean merge, Handler handler) {
			this.merge = merge;
			this.handler = handler;
		}

		@Override
		public void run() {
			save(merge);

			Log.d(TAG, "contact add, merge:" + merge);
			if (handler != null) {
				Message msg = new Message();
				msg.what = MessageId.SAVE_CONTACT_SUCCESS;
				handler.sendMessage(msg);
				Log.d(TAG, "send success message");
			}
		}

	}

	/**
	 * save the object to system contacts
	 * 
	 * @param merge
	 *            if true and there's a contact already existed whose name is
	 *            the same to this contact's name, the information in this
	 *            contact object will be added to the old contact, else a new
	 *            contact will be created in the system wide. Remember to update
	 *            the values in this of object with the value of binded views
	 */
	public synchronized void save(boolean merge) {
		if (merge) {
			Log.d(TAG, "begin merge contact");
		} else {
			Log.d(TAG, "begin create new contact");
			ContentValues values = new ContentValues();
			/**
			 * insert name and notes to Contact.People table
			 */
			values.put(Contacts.PeopleColumns.NAME, name);
			values.put(Contacts.PeopleColumns.NOTES, note);
			Uri personUrl = contentResolver.insert(Contacts.People.CONTENT_URI,
					values);

			long personId = -1;
			if (personUrl.getLastPathSegment() != null) {
				try {
					personId = Long.parseLong(personUrl.getLastPathSegment());
				} catch (NumberFormatException e) {
					Log.e(TAG, "error when parsing newly inserted person id");
				}
			} else {
				Log
						.e(TAG,
								"last path segment of newly inserted person uri is null");
			}

			/**
			 * contact not inserted, saving progress terminated
			 */
			if (personId == -1) {
				return;
			}
			Log.d(TAG, "insert person, pserson id:" + personId);

			/**
			 * insert contact to group membership table
			 */
			values.clear();
			values.put(Contacts.GroupMembership.GROUP_ID, groupId);
			values.put(Contacts.GroupMembership.PERSON_ID, personId);
			Uri resultUri = contentResolver.insert(
					Contacts.GroupMembership.CONTENT_URI, values);
			if (resultUri != null) {
				Log.d(TAG, "add contact to group " + groupId);
			} else {
				Log.e(TAG, "cannot add contact to group " + groupId);
			}
			/**
			 * insert phones
			 */
			for (int i = 0; i < phones.size(); i++) {
				String phoneStr = phones.get(i).value.trim();
				/**
				 * email is empty, ignore this item
				 */
				if (phoneStr.length() == 0)
					continue;
				values.clear();
				values.put(Contacts.PhonesColumns.NUMBER, phoneStr);
				int type = convertType1(phones.get(i).type);
				if (type == -1) {
					Log.i(TAG, "cannot convert phone type "
							+ phones.get(i).type
							+ ", use default value instead");
					type = Contacts.PhonesColumns.TYPE_OTHER;
				}
				values.put(Contacts.Phones.PERSON_ID, personId);
				values.put(Contacts.PhonesColumns.TYPE, type);
				contentResolver.insert(Contacts.Phones.CONTENT_URI, values);
				Log.d(TAG, "insert phone " + i + ", value:" + phoneStr);
			}
			/**
			 * insert emails
			 */
			for (int i = 0; i < emails.size(); i++) {
				String emailStr = emails.get(i).value.trim();
				/**
				 * email is empty, ignore this item
				 */
				if (emailStr.length() == 0)
					continue;
				/**
				 * check email format, not implemented yet
				 */

				values.clear();
				values.put(Contacts.ContactMethods.PERSON_ID, personId);
				values.put(Contacts.ContactMethods.DATA, emailStr);
				int type = convertType3(emails.get(i).type);
				if (type == -1) {
					Log.i(TAG, "cannot convert email type "
							+ emails.get(i).type
							+ ", use default value instead");
					type = Contacts.PhonesColumns.TYPE_OTHER;
				}
				values.put(Contacts.PhonesColumns.TYPE, type);
				values.put(Contacts.ContactMethods.KIND, Contacts.KIND_EMAIL);
				contentResolver.insert(Contacts.ContactMethods.CONTENT_URI,
						values);
				Log.d(TAG, "insert email " + i);
			}
			/**
			 * insert postal address
			 */
			for (int i = 0; i < addresses.size(); i++) {
				String addressStr = addresses.get(i).value.trim();
				/**
				 * email is empty, ignore this item
				 */
				if (addressStr.length() == 0)
					continue;
				/**
				 * check email format, not implemented yet
				 */

				values.clear();
				values.put(Contacts.ContactMethods.PERSON_ID, personId);
				values.put(Contacts.ContactMethods.DATA, addressStr);
				int type = convertType3(addresses.get(i).type);
				if (type == -1) {
					Log.i(TAG, "cannot convert email type "
							+ addresses.get(i).type
							+ ", use default value instead");
					type = Contacts.PhonesColumns.TYPE_OTHER;
				}
				values.put(Contacts.PhonesColumns.TYPE, type);
				values.put(Contacts.ContactMethods.KIND, Contacts.KIND_POSTAL);
				contentResolver.insert(Contacts.ContactMethods.CONTENT_URI,
						values);
				Log.d(TAG, "insert address " + i);
			}
			/**
			 * insert organization
			 */
			for (int i = 0; i < orgs.size(); i++) {
				values.clear();
				values.put(Contacts.OrganizationColumns.COMPANY,
						orgs.get(i).company);
				values.put(Contacts.OrganizationColumns.TITLE,
						orgs.get(i).title);
				values.put(Contacts.OrganizationColumns.PERSON_ID, personId);
				int type = convertType2(orgs.get(i).type);
				if (type == -1) {
					Log.i(TAG, "cannot convert org type " + orgs.get(i).type
							+ ", use default value");
					type = Contacts.OrganizationColumns.TYPE_OTHER;
				}
				values.put(Contacts.OrganizationColumns.TYPE, type);
				contentResolver.insert(Contacts.Organizations.CONTENT_URI,
						values);
				Log.d(TAG, "insert org " + i);
			}
			/**
			 * insert url, temporarily not implemented, because android does not
			 * allow us to insert two rows with the same PERSION_ID field value
			 * and NAME field value into Contacts.Extension table.
			 */
			// for (int i = 0; i < urls.size(); i++) {
			// String urlStr = urls.get(i).value.trim();
			// if (urlStr.length() == 0) {
			// continue;
			// }
			// values.clear();
			// values.put(Contacts.Extensions.PERSON_ID, personId);
			// values.put(Contacts.Extensions.NAME, "URL");
			// values.put(Contacts.Extensions.VALUE, urlStr);
			// contentResolver.insert(Contacts.Extensions.CONTENT_URI, values);
			// Log.d(TAG, "insert url " + i);
			// }
		}
	}

	/**
	 * Convert type defined in this class to android contact type, ie.,
	 * Contact.Phones
	 * 
	 * @param myType
	 * @return
	 */
	private int convertType1(int myType) {
		int result = -1;
		switch (myType) {
		case TYPE_HOME:
			result = Contacts.PhonesColumns.TYPE_HOME;
			break;
		case TYPE_WORK:
			result = Contacts.PhonesColumns.TYPE_WORK;
			break;
		case TYPE_OTHER:
			result = Contacts.PhonesColumns.TYPE_OTHER;
			break;
		case TYPE_MOBILE:
			result = Contacts.PhonesColumns.TYPE_MOBILE;
			break;
		case TYPE_PAGER:
			result = Contacts.PhonesColumns.TYPE_PAGER;
			break;
		case TYPE_FAX_WORK:
			result = Contacts.PhonesColumns.TYPE_FAX_WORK;
			break;
		case TYPE_FAX_HOME:
			result = Contacts.PhonesColumns.TYPE_FAX_HOME;
			break;
		}
		return result;
	}

	/**
	 * for organization
	 * 
	 * @param myType
	 * @return
	 */
	private int convertType2(int myType) {
		int result = -1;
		switch (myType) {
		case TYPE_WORK:
			result = Contacts.OrganizationColumns.TYPE_WORK;
			break;
		case TYPE_OTHER:
			result = Contacts.OrganizationColumns.TYPE_OTHER;
			break;
		}
		return result;
	}

	/**
	 * for other items used for Contacts.ContactMethods
	 * 
	 * @param myType
	 * @return
	 */
	private int convertType3(int myType) {
		int result = -1;
		switch (myType) {
		case TYPE_WORK:
			result = Contacts.ContactMethodsColumns.TYPE_WORK;
			break;
		case TYPE_HOME:
			result = Contacts.ContactMethodsColumns.TYPE_HOME;
			break;
		case TYPE_OTHER:
			result = Contacts.ContactMethodsColumns.TYPE_OTHER;
			break;
		}
		return result;
	}

	public boolean updateItemType(int itemName, int index, int newType) {
		Log.d(TAG, "update new type, itemName:" + itemName + ", index:" + index
				+ ", newType:" + newType);
		switch (itemName) {
		case ITEM_PHONE:
			if (index >= 0 && index < phones.size()) {
				if (checkType(itemName, newType)) {
					phones.get(index).type = newType;
					return true;
				} else {
					Log.e(TAG, "invalid type for item " + itemName + ", type:"
							+ newType);
				}
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;
		case ITEM_EMAIL:
			if (index >= 0 && index < emails.size()) {
				if (checkType(itemName, newType)) {
					emails.get(index).type = newType;
					return true;
				} else {
					Log.e(TAG, "invalid type for item " + itemName + ", type:"
							+ newType);
				}
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;
		case ITEM_ADDRESS:
			if (index >= 0 && index < addresses.size()) {
				if (checkType(itemName, newType)) {
					addresses.get(index).type = newType;
					return true;
				} else {
					Log.e(TAG, "invalid type for item " + itemName + ", type:"
							+ newType);
				}
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;

		case ITEM_ORG:
			if (index >= 0 && index < orgs.size()) {
				if (checkType(itemName, newType)) {
					orgs.get(index).type = newType;
					return true;
				} else {
					Log.e(TAG, "invalid type for item " + itemName + ", type:"
							+ newType);
				}
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;

		case ITEM_URL:
			if (index >= 0 && index < urls.size()) {
				if (checkType(itemName, newType)) {
					urls.get(index).type = newType;
					return true;
				} else {
					Log.e(TAG, "invalid type for item " + itemName + ", type:"
							+ newType);
				}
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;
		}
		return false;
	}

	public boolean updateItemValue(int itemName, int index, String value1) {
		return updateItemValue(itemName, index, value1, null);
	}

	public boolean updateItemValue(int itemName, int index, String value1,
			String value2) {

		Log.d(TAG, "update item:" + itemName + ", index:" + index + ", value:"
				+ value1);
		if (value1 == null) {
			value1 = "";
		}
		if (value2 == null) {
			value2 = "";
		}
		switch (itemName) {
		case ITEM_NAME:
			name = value1;
			return true;
		case ITEM_PHONE:
			if (index >= 0 && index < phones.size()) {
				phones.get(index).value = value1;
				return true;
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;
		case ITEM_EMAIL:
			if (index >= 0 && index < emails.size()) {
				emails.get(index).value = value1;
				return true;
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;
		case ITEM_ADDRESS:
			if (index >= 0 && index < addresses.size()) {
				addresses.get(index).value = value1;
				return true;
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;

		case ITEM_ORG:
			if (index >= 0 && index < orgs.size()) {
				orgs.get(index).company = value1;
				orgs.get(index).title = value2;
				return true;
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;

		case ITEM_URL:
			if (index >= 0 && index < urls.size()) {
				urls.get(index).value = value1;
				return true;
			} else {
				Log.e(TAG, "invalid index for item " + itemName
						+ " with index " + index);
			}
			break;
		case ITEM_NOTE:
			note = value1;
			break;
		}
		return false;
	}

	public boolean deleteItem(int itemName, int index) {
		Log.d(TAG, "delete item:" + itemName + ", index:" + index);
		return true;
	}

	public void add(int itemName, String value) {

		if (value == null)
			value = "";
		switch (itemName) {
		case ITEM_PHONE:
			Phone phone = new Phone();
			phone.type = TYPE_WORK;
			phone.value = value;

			addPhoneItem(phone);
			break;
		case ITEM_EMAIL:
			Email email = new Email();
			email.type = TYPE_WORK;
			email.value = value;

			addEmailItem(email);
			break;
		case ITEM_ADDRESS:
			Address address = new Address();
			address.type = TYPE_WORK;
			address.value = value;

			addAddressItem(address);
			break;
		case ITEM_ORG:
			Organization org = new Organization();
			org.company = value;
			org.type = TYPE_WORK;

			addOrgItem(org);
			break;
		case ITEM_URL:
			Url url = new Url();
			url.type = TYPE_WORK;
			url.value = value;

			addUrlItem(url);
			break;
		}
	}

	private void addPhoneItem(Phone phone) {
		addPhoneItem(phone, true);
	}

	private void addPhoneItem(Phone phone, boolean newItem) {
		if (phoneViewGroup == null) {
			Log.e(TAG, "viewGroup==null");
			return;
		}
		if (phone == null) {
			Log.e(TAG, "phone==null");
			return;
		}
		View newView = inflateViews();
		phoneViewGroup.addView(newView);
		ItemIdentifier identifier = new ItemIdentifier();

		identifier.itemName = ITEM_PHONE;

		if (newItem) {
			phones.add(phone);
		}
		identifier.id = phone.getId();
		identifier.parent = newView;

		itemType.setOnClickListener(onSelectTypeListener);
		itemType.setText(getTypeName(phone.type));
		itemType.setTag(identifier);

		itemValue.setText(phone.value);
		itemValue.setInputType(InputType.TYPE_CLASS_PHONE);

		removeItem.setOnClickListener(removeItemListener);
		removeItem.setTag(identifier);
	}

	private void addEmailItem(Email email) {
		addEmailItem(email, true);
	}

	private void addEmailItem(Email email, boolean newItem) {
		if (emailViewGroup == null) {
			Log.e(TAG, "emailViewGroup==null");
			return;
		}
		if (email == null) {
			Log.e(TAG, "email==null");
			return;
		}
		View newView = inflateViews();
		emailViewGroup.addView(newView);
		ItemIdentifier identifier = new ItemIdentifier();
		identifier.itemName = ITEM_EMAIL;

		if (newItem) {
			emails.add(email);
		}
		identifier.id = email.getId();
		identifier.parent = newView;
		itemType.setOnClickListener(onSelectTypeListener);
		itemType.setText(getTypeName(email.type));
		itemType.setTag(identifier);

		itemValue.setText(email.value);

		removeItem.setOnClickListener(removeItemListener);
		removeItem.setTag(identifier);
	}

	private void addAddressItem(Address address) {
		addAddressItem(address, true);
	}

	private void addAddressItem(Address address, boolean newItem) {
		if (addressViewGroup == null) {
			Log.e(TAG, "addressViewGroup==null");
			return;
		}
		if (address == null) {
			Log.e(TAG, "address==null");
			return;
		}
		View newView = inflateViews();
		addressViewGroup.addView(newView);
		ItemIdentifier identifier = new ItemIdentifier();
		identifier.itemName = ITEM_ADDRESS;

		if (newItem) {
			addresses.add(address);
		}
		identifier.id = address.getId();
		identifier.parent = newView;
		itemType.setOnClickListener(onSelectTypeListener);
		itemType.setText(getTypeName(address.type));
		itemType.setTag(identifier);

		itemValue.setText(address.value);

		removeItem.setOnClickListener(removeItemListener);
		removeItem.setTag(identifier);
	}

	private void addOrgItem(Organization org) {
		addOrgItem(org, true);
	}

	private void addOrgItem(Organization org, boolean newItem) {
		if (orgViewGroup == null) {
			Log.e(TAG, "orgViewGroup==null");
			return;
		}
		if (org == null) {
			Log.e(TAG, "org==null");
			return;
		}
		View newView = inflateViews2();
		orgViewGroup.addView(newView);
		ItemIdentifier identifier = new ItemIdentifier();
		identifier.itemName = ITEM_ORG;

		if (newItem) {
			orgs.add(org);
		}
		identifier.id = org.getId();
		identifier.parent = newView;
		itemType.setOnClickListener(onSelectTypeListener);
		itemType.setText(getTypeName(org.type));
		itemType.setTag(identifier);

		itemValue.setText(org.company);
		itemValue2.setText(org.title);

		removeItem.setOnClickListener(removeItemListener);
		removeItem.setTag(identifier);
	}

	private void addUrlItem(Url url) {
		addUrlItem(url, true);
	}

	private void addUrlItem(Url url, boolean newItem) {
		if (urlViewGroup == null) {
			Log.e(TAG, "urlViewGroup==null");
			return;
		}
		if (url == null) {
			Log.e(TAG, "url==null");
			return;
		}
		View newView = inflateViews();
		urlViewGroup.addView(newView);
		ItemIdentifier identifier = new ItemIdentifier();
		identifier.itemName = ITEM_URL;

		if (newItem) {
			urls.add(url);
		}
		identifier.id = url.getId();
		identifier.parent = newView;
		itemType.setOnClickListener(onSelectTypeListener);
		itemType.setText(getTypeName(url.type));
		itemType.setTag(identifier);

		itemValue.setText(url.value);

		removeItem.setOnClickListener(removeItemListener);
		removeItem.setTag(identifier);

	}

	public OnSelectItemType getOnSelectItemType() {
		return onSelectItemType;
	}

	public void setOnSelectItemType(OnSelectItemType onSelectItemType) {
		this.onSelectItemType = onSelectItemType;
	}

	public class ItemIdentifier {
		int itemName;
		int id;
		View parent;
	}

	public String getTypeName(int type) {
		switch (type) {
		case TYPE_HOME:
			return typeHome;
		case TYPE_WORK:
			return typeWork;
		case TYPE_OTHER:
			return typeOther;
		case TYPE_PAGER:
			return typePager;
		case TYPE_MOBILE:
			return typeMobile;
		case TYPE_FAX_HOME:
			return typeFaxHome;
		case TYPE_FAX_WORK:
			return typeFaxWork;
		}
		return "";
	}

	/**
	 * check if the item that itemName specifies supports the type
	 * 
	 * @param itemName
	 * @param type
	 * @return
	 */
	private boolean checkType(int itemName, int type) {
		int[] supportedTypes = getSupportedTypes(itemName);
		if (supportedTypes == null) {
			return false;
		}
		for (int i = 0; i < supportedTypes.length; i++) {
			if (supportedTypes[i] == type) {
				return true;
			}
		}
		return false;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int[] getSupportedTypes(int itemName) {
		switch (itemName) {
		case ITEM_PHONE:
			return supportedItemTypes1;
		case ITEM_ORG:
			return supportedItemTypes2;
		case ITEM_EMAIL:
		case ITEM_ADDRESS:
		case ITEM_URL:
			return supportedItemTypes3;
		}
		return null;
	}

	public interface OnSelectItemType {
		void displayItems(ContactPerson contact, int itemName, int index,
				int[] types, String[] typeStrs);
	}

	public EditText getNameView() {
		return nameView;
	}

	public void setNameView(EditText nameView) {
		this.nameView = nameView;
	}

	public EditText getNoteView() {
		return noteView;
	}

	public void setNoteView(EditText noteView) {
		this.noteView = noteView;
	}

	public ViewGroup getPhoneViewGroup() {
		return phoneViewGroup;
	}

	public void setPhoneViewGroup(ViewGroup phoneViewGroup) {
		this.phoneViewGroup = phoneViewGroup;
	}

	public ViewGroup getEmailViewGroup() {
		return emailViewGroup;
	}

	public void setEmailViewGroup(ViewGroup emailViewGroup) {
		this.emailViewGroup = emailViewGroup;
	}

	public ViewGroup getAddressViewGroup() {
		return addressViewGroup;
	}

	public void setAddressViewGroup(ViewGroup addressViewGroup) {
		this.addressViewGroup = addressViewGroup;
	}

	public ViewGroup getOrgViewGroup() {
		return orgViewGroup;
	}

	public void setOrgViewGroup(ViewGroup orgViewGroup) {
		this.orgViewGroup = orgViewGroup;
	}

	public ViewGroup getUrlViewGroup() {
		return urlViewGroup;
	}

	public void setUrlViewGroup(ViewGroup urlViewGroup) {
		this.urlViewGroup = urlViewGroup;
	}

	private class RemoveItemListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ItemIdentifier identifier = (ItemIdentifier) v.getTag();
			if (identifier != null) {
				if (identifier.itemName > 0 && identifier.itemName < 8) {
					switch (identifier.itemName) {
					case ITEM_PHONE:
						removeFromList(ITEM_PHONE, identifier.id);
						phoneViewGroup.removeView(identifier.parent);
						Log.d(TAG, "remove phone item, id:" + identifier.id);
						break;
					case ITEM_EMAIL:
						removeFromList(ITEM_EMAIL, identifier.id);
						emailViewGroup.removeView(identifier.parent);
						Log.d(TAG, "remove email item, id:" + identifier.id);
						break;
					case ITEM_ADDRESS:
						removeFromList(ITEM_ADDRESS, identifier.id);
						addressViewGroup.removeView(identifier.parent);
						Log.d(TAG, "remove address item, id:" + identifier.id);
						break;
					case ITEM_ORG:
						removeFromList(ITEM_ORG, identifier.id);
						orgViewGroup.removeView(identifier.parent);
						Log.d(TAG, "remove org item, id:" + identifier.id);
						break;
					case ITEM_URL:
						removeFromList(ITEM_URL, identifier.id);
						urlViewGroup.removeView(identifier.parent);
						Log.d(TAG, "remove url item, id:" + identifier.id);
						break;
					}
				}
			}
		}
	}

	private void removeFromList(int itemName, int id) {
		List list = null;
		switch (itemName) {
		case ITEM_PHONE:
			list = phones;
			break;
		case ITEM_EMAIL:
			list = emails;
			break;
		case ITEM_ADDRESS:
			list = addresses;
			break;
		case ITEM_ORG:
			list = orgs;
			break;
		case ITEM_URL:
			list = urls;
			break;
		}
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				BaseDomain base = (BaseDomain) list.get(i);
				if (base.getId() == id) {
					list.remove(i);
					Log.d(TAG, "remove item, itemName:" + itemName + ", id="
							+ id);
				}
			}
		} else {
			Log.e(TAG, "no match item " + itemName);
		}
	}

	private class SelectItemTypeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ItemIdentifier identifier = (ItemIdentifier) v.getTag();
			if (identifier != null) {
				if (identifier.itemName > 0 && identifier.itemName < 8) {
					Log.v(TAG, "item name:" + identifier.itemName + ", index:"
							+ identifier.id);
					String[] typesStr = null;
					int[] types = null;
					switch (identifier.itemName) {
					case ITEM_PHONE:
						types = supportedItemTypes1;
						typesStr = supportedItemTypeStr1;
						break;
					case ITEM_ORG:
						types = supportedItemTypes2;
						typesStr = supportedItemTypeStr2;
						break;
					case ITEM_EMAIL:
					case ITEM_ADDRESS:
					case ITEM_URL:
						types = supportedItemTypes3;
						typesStr = supportedItemTypeStr3;
						break;
					}
					if (typesStr != null && onSelectItemType != null) {
						int index = getItemIndex(identifier.itemName,
								identifier.id);
						if (index < 0) {
							Log.e(TAG, "invalid index for item "
									+ identifier.itemName);
						} else {
							onSelectItemType
									.displayItems(ContactPerson.this,
											identifier.itemName, index, types,
											typesStr);
						}
					} else {
						Log.e(TAG, "typeStr==null");
					}
				} else {
					Log.e(TAG, "unknown type:" + identifier.itemName);
				}
			} else {
				Log.i(TAG, "identifier==null");
			}
		}
	}

	private int getItemIndex(int itemName, int id) {
		List list = null;
		switch (itemName) {
		case ITEM_PHONE:
			list = phones;
			break;
		case ITEM_EMAIL:
			list = emails;
			break;
		case ITEM_ADDRESS:
			list = addresses;
			break;
		case ITEM_ORG:
			list = orgs;
			break;
		case ITEM_URL:
			list = urls;
			break;
		}
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				BaseDomain base = (BaseDomain) list.get(i);
				if (base.getId() == id) {
					return i;
				}
			}
		}
		return -1;
	}
}