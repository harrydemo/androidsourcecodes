package com.xcontacts.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContactsEntity;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.xcontacts.activities.R;
import com.xcontacts.ui.EmailItemsContainer;
import com.xcontacts.ui.ImItemsContainer;
import com.xcontacts.ui.OrganizationItem;
import com.xcontacts.ui.OrganizationItemsContainer;
import com.xcontacts.ui.PhoneItemsContainer;
import com.xcontacts.ui.PostalItem;
import com.xcontacts.ui.PostalItemsContainer;
import com.xcontacts.ui.WebsiteItem;
import com.xcontacts.ui.WebsiteItemsContainer;
import com.xcontacts.ui.model.AbstractItem;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

// When the user chooses a new photo mark it as super primary
/**
 * 编辑或添加联系人的界面
 * 
 * @author Lefter
 */
public class EditContactActivity extends Activity implements
		View.OnClickListener {
	/**
	 * 用于存放Phone、Email、Im、Organization、Postal等容器的容器
	 */
	private LinearLayout mContainer;
	/**
	 * 存放所有Phone信息的容器
	 */
	private PhoneItemsContainer mPhonesContainer;
	/**
	 * 存放所有Email信息的容器
	 */
	private EmailItemsContainer mEmailsContainer;
	/**
	 * 存放所有Postal信息的容器
	 */
	private PostalItemsContainer mPostalsContainer;
	/**
	 * 存放所有Organization信息的容器
	 */
	private OrganizationItemsContainer mOrganizationsContainer;
	/**
	 * 存放所有Im信息的容器
	 */
	private ImItemsContainer mImsContainer;
	/**
	 * 存放所有Website信息的容器
	 */
	private WebsiteItemsContainer mWebsitesContainer;
	/**
	 * 显示联系人的头像
	 */
	private ImageView mImageViewPhoto;
	/**
	 * photo相关信息在data表中_id字段的值
	 */
	private long dataIdOfPhoto;
	/**
	 * 用于存放联系人头像。用于数据库的插入和更新
	 */
	private ContentValues photoContentValues = new ContentValues();
	/**
	 * 头像缓存用于判断用户更换的头像是否与原先的头像一样
	 */
	private Bitmap mPhotoBitmap;
	/**
	 * 联系人是否有头像
	 */
	private boolean hasPhoto = false;
	/**
	 * 是否改变了联系人的头像
	 */
	private boolean hasChangedPhoto = false;
	/**
	 * FirstName(StructuredName.GIVEN_NAME)
	 */
	private EditText etFirstName;
	/**
	 * LastName(StructuredName.FAMILY_NAME)
	 */
	private EditText etLastName;
	/**
	 * name相关信息在data表中_id字段的值
	 */
	private long dataIdOfName;
	/**
	 * 用于存放StructuredName相关的信息。用于数据库的插入和更新
	 */
	private ContentValues nameContentValues = new ContentValues();
	/**
	 * 用于显示该联系人的昵称
	 */
	private EditText etNickname;
	/**
	 * nickname相关信息在data表中_id字段的值
	 */
	private long dataIdOfNickname;
	/**
	 * 用于存放该联系人的昵称信息。用于数据库的插入和更新
	 */
	private ContentValues nickNameContentValues = new ContentValues();
	/**
	 * 用于显示该联系人的备注信息
	 */
	private EditText etNotes;
	/**
	 * 备注相关信息在data表中_id字段的值
	 */
	private long dataIdOfNote;
	/**
	 * 用于存放对该联系人的备注信息。用于数据库的插入和更新
	 */
	private ContentValues noteContentValues = new ContentValues();

	private Button btnOk;// 保存
	private Button btnCancel;// 取消

	/** The launch code when picking a photo and the raw data is returned */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/** The launch code when taking a picture */
	private static final int CAMERA_WITH_DATA = 3023;
	private static final int ICON_SIZE = 96;
	/**
	 * 照相机拍摄照片转化为该File对象
	 */
	private File mCurrentPhotoFile;
	/**
	 * 使用照相机拍摄照片作为头像时会使用到这个路径
	 */
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	/**
	 * 编辑或新建联系人时传递过来的Intent对象
	 */
	private Intent intent;
	/**
	 * 编辑联系人时，查询的到的该联系人的信息被保存在mCursor中
	 */
	private Cursor mCursor;
	/**
	 * 新建联系人还是编辑联系人
	 */
	private static boolean isInsert = false;
	/**
	 * 编辑联系人时，该联系人在raw_contacts表中的_id
	 */
	private static long rawContactId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_contact);
		// 获得传递过来的Intent对象
		intent = getIntent();
		if (intent != null) {
			MyLog.d("传递的Intent不为空");
			final String action = intent.getAction();
			MyLog.d("传递的Intent的Action:" + action);
			// 根据Intent对象的Action判断是编辑联系人还是新建联系人
			if (action.equals(Intent.ACTION_INSERT)) {// 添加联系人
				isInsert = true;
				init(true);
			} else if (action.equals(Intent.ACTION_EDIT)) {// 编辑联系人
				isInsert = false;
				init(false);
				bindViews();
			}
		} else {
			MyLog.e("传递的Intent为空");
		}
	}

	/**
	 * @return 新建还是编辑联系人
	 */
	public static boolean isInsert() {
		return isInsert;
	}

	/**
	 * 初始化UI
	 * 
	 * @param isInsert
	 *            true,新建联系人;false,编辑联系人
	 */
	private void init(boolean isInsert) {
		// 初始化显示头像的ImageView对象,并添加监听器
		mImageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
		mImageViewPhoto.setImageResource(R.drawable.ic_edit_contact);
		mImageViewPhoto.setOnClickListener(this);
		// 初始化FirstName、LastName、NickName、Notes对象.并为这四个EditText对象添加监听器
		etFirstName = (EditText) findViewById(R.id.editTextFirstName);
		etFirstName.addTextChangedListener(new MyTextWachter(
				StructuredName.GIVEN_NAME, nameContentValues));
		etLastName = (EditText) findViewById(R.id.editTextLastName);
		etLastName.addTextChangedListener(new MyTextWachter(
				StructuredName.FAMILY_NAME, nameContentValues));
		etNickname = (EditText) findViewById(R.id.etNickName);
		etNickname.addTextChangedListener(new MyTextWachter(Nickname.NAME,
				nickNameContentValues));
		etNotes = (EditText) findViewById(R.id.etNotes);
		etNotes.addTextChangedListener(new MyTextWachter(Note.NOTE,
				noteContentValues));
		// 初始化按钮,并添加监听器
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		// 初始化Phone、Email等信息最外围的容器
		mContainer = (LinearLayout) findViewById(R.id.container);
		// 初始化Phone信息的容器,并添加到最外围的容器中
		mPhonesContainer = new PhoneItemsContainer(this);
		mPhonesContainer.initViews(R.string.phone, R.array.phoneTypes,
				R.string.phoneHint);
		mContainer.addView(mPhonesContainer);
		// 初始化Email信息的容器,并添加到最外围的容器中
		mEmailsContainer = new EmailItemsContainer(this);
		mEmailsContainer.initViews(R.string.email, R.array.emailTypes,
				R.string.emailHint);
		mContainer.addView(mEmailsContainer);
		// 初始化Postal信息的容器,并添加到最外围的容器中
		mPostalsContainer = new PostalItemsContainer(this);
		mPostalsContainer.initViews(R.string.postalAddress,
				R.array.postalTypes, R.array.postalHints);
		mContainer.addView(mPostalsContainer);
		// 初始化Organization信息的容器,并添加到最外围的容器中
		mOrganizationsContainer = new OrganizationItemsContainer(this);
		mOrganizationsContainer.initViews(R.string.organization,
				R.array.organizationTypes, R.array.organizationHints);
		mContainer.addView(mOrganizationsContainer);
		// 初始化Im信息的容器,并添加到最外围的容器中
		mImsContainer = new ImItemsContainer(this);
		mImsContainer.initViews(R.string.im, R.array.imTypes, R.string.imHint);
		mContainer.addView(mImsContainer);
		// 初始化Website信息的容器,并添加到最外围的容器中
		mWebsitesContainer = new WebsiteItemsContainer(this);
		mWebsitesContainer.initViews(R.string.webSite, 0, R.string.websiteHint);
		mContainer.addView(mWebsitesContainer);
		// 根据函数参数判断是添加联系人还是编辑联系人
		if (isInsert) {// 如果是添加联系人
			// 默认添加一个Phone
			mPhonesContainer.addItem(R.array.phoneTypes, R.string.phoneHint);
			// 默认添加一个Email
			mEmailsContainer.addItem(R.array.emailTypes, R.string.emailHint);
		}
	}

	/**
	 * 编辑联系人时，我们调用这个函数把待编辑联系人的已有信息显示出来
	 */
	private void bindViews() {
		MyLog.i("bindViews()");
		final Uri uri = intent.getData();
		final String mimeType = intent.resolveType(getContentResolver());
		MyLog.i("intent-->mimeType:" + mimeType);
		String selection = null;
		/**
		 * 被编辑的联系人在Contacts中的_id的值
		 */
		long contactId = -1;
		// 根据传递过来的intent对象的mimetype区分是Contacts还是RawContacts,然后初始化查询条件和被编辑联系人在raw_contacts中的id
		if (Contacts.CONTENT_ITEM_TYPE.equals(mimeType)) {
			// Handle selected aggregate
			contactId = ContentUris.parseId(uri);
			Cursor tmpCursor = getContentResolver().query(
					RawContacts.CONTENT_URI, new String[] { RawContacts._ID },
					RawContacts.CONTACT_ID + "=?",
					new String[] { String.valueOf(contactId) }, null);
			if (tmpCursor.moveToFirst())
				rawContactId = tmpCursor.getLong(0);
			tmpCursor.close();
			selection = RawContacts.CONTACT_ID + "=" + contactId;
		} else if (RawContacts.CONTENT_ITEM_TYPE.equals(mimeType)) {
			rawContactId = ContentUris.parseId(uri);
			contactId = queryForContactId(getContentResolver(), rawContactId);
			selection = RawContacts.CONTACT_ID + "=" + contactId;
		}
		MyLog.i("contactId:" + contactId);
		MyLog.i("rawContactId:" + rawContactId);
		MyLog.i("selection:" + selection);
		if ((!TextUtils.isEmpty(selection)) && (contactId != -1)) {// 查询条件不为空,且正确的查到了contactId
			MyLog.i("查询RawContactsEntity");
			// RawContactsEntity.CONTENT_URI进行查询,已得到被编辑的联系人的详细信息
			mCursor = getContentResolver().query(RawContactsEntity.CONTENT_URI,
					null, selection, null, null);
		}
		if (mCursor != null) {// 如果查询到了信息
			MyLog.d("mCursor != null");
			startManagingCursor(mCursor);
			MyLog.i("mCursor.getCount():" + mCursor.getCount());
			// 初始化头像
			MyLog.i("Photo...");
			preparePhoto(contactId);
			// 取得FirstName、Lastname
			MyLog.i("StructedName...");
			prepareStructuredName(mCursor);
			// 取得所有的Phone
			MyLog.i("Phone...");
			preparePhones(mCursor);
			// Email
			MyLog.i("Email...");
			prepareEmails(mCursor);
			// Postal
			MyLog.i("Postal address...");
			preparePostals(mCursor);
			// Organization
			MyLog.i("Organization...");
			prepareOrganziations(mCursor);
			// Im
			MyLog.i("Im...");
			prepareIms(mCursor);
			// Notes
			MyLog.i("Notes...");
			prepareNote(mCursor);
			// Nickname
			MyLog.i("Nickname...");
			prepareNickname(mCursor);
			// Website
			MyLog.i("Website...");
			prepareWebsites(mCursor);
		} else {
			MyLog.e("mCursor == null");
		}
	}

	/**
	 * 初始化联系人的头像。
	 */
	private void preparePhoto(long contactId) {
		// 根据contactId查询contacts表中photo_id列的值,以此来判断该联系人是否有头像
		Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI,
				new String[] { Contacts.PHOTO_ID }, Contacts._ID + " = ? ",
				new String[] { String.valueOf(contactId) }, null);
		if (cursor.moveToFirst()) {// 查到了数据
			// 如果没有头像photoId将被赋值为0,更新时注意判断photoId是否大于0
			long photoId = cursor.getLong(0);
			// 保存photo在data表中_id的值,更新时使用
			dataIdOfPhoto = photoId;
			MyLog.i("photoId:" + photoId);
			if (photoId > 0) {
				String[] projection = new String[] { Photo.PHOTO };
				String photoSelection = Data._ID + " = ? ";
				String[] selectionArgs = new String[] { String.valueOf(photoId) };
				Cursor photoCursor = getContentResolver().query(
						Data.CONTENT_URI, projection, photoSelection,
						selectionArgs, null);
				if (photoCursor.moveToFirst()) {// 用户设置了头像
					byte[] photo = photoCursor.getBlob(0);
					Bitmap bitmapPhoto = BitmapFactory.decodeByteArray(photo,
							0, photo.length);
					mImageViewPhoto.setImageBitmap(bitmapPhoto);
					// 我们将头像存储在自定义的一个ContentValues对象中,在更新时使用
					photoContentValues.put(Photo.PHOTO, photo);
					hasPhoto = true;
					MyLog.i("用户设置了头像");
				} else {// 该联系人没有头像，使用默认的图片
					hasPhoto = false;
					mImageViewPhoto
							.setImageResource(R.drawable.ic_edit_contact);
					MyLog.i("用户没有设置头像,使用默认图片");
				}
				photoCursor.close();
			} else {
				// 没有头像,使用默认图片.(我们在布局文件中已经声明)
			}
		}
		cursor.close();// 注意Cursor对象的关闭
	}

	/**
	 * 编辑联系人时,初始化联系人的姓名信息
	 */
	private void prepareStructuredName(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		MyLog.i("mCursor.getCount():" + count);
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			MyLog.i("itemMimeType:" + itemMimeType);
			if (itemMimeType.equals(StructuredName.CONTENT_ITEM_TYPE)) {
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				dataIdOfName = id;
				String firstName = cursor.getString(cursor
						.getColumnIndex(StructuredName.GIVEN_NAME));
				String lastName = cursor.getString(cursor
						.getColumnIndex(StructuredName.FAMILY_NAME));
				if (!TextUtils.isEmpty(firstName))
					MyLog.i("firstName:" + firstName);
				etFirstName.setText(firstName);
				if (!TextUtils.isEmpty(lastName))
					MyLog.i("lastName:" + lastName);
				etLastName.setText(lastName);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的电话信息
	 */
	private void preparePhones(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Phone.CONTENT_ITEM_TYPE)) {
				MyLog.i("添加一个phone");
				// 添加一行phone
				mPhonesContainer
						.addItem(R.array.phoneTypes, R.string.phoneHint);
				// 获取所有同类型item的容器
				LinearLayout itemsContainer = mPhonesContainer
						.getmItemsContainer();
				int index = itemsContainer.getChildCount() - 1;
				MyLog.i("index:" + index);
				AbstractItem phoneItem = (AbstractItem) itemsContainer
						.getChildAt(index);
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				phoneItem.setDataId(id);
				// 取得这一个phone信息的type
				int type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
				// 初始化button时把类型放到ContentValues对象中，以防用户不改变类型就保存
				phoneItem.getChanges().put(Phone.TYPE, type);
				MyLog.i("type" + type);
				if (type == Phone.TYPE_ASSISTANT || type == Phone.TYPE_CUSTOM) {
					// 自定义类型,取得自定义type的lable
					String lable = cursor.getString(cursor
							.getColumnIndex(Phone.LABEL));
					phoneItem.setTypeString(lable);
					MyLog.d("自定义" + lable);
				} else {
					// 不是自定义
					String[] types = getResources().getStringArray(
							R.array.phoneTypes);
					phoneItem.setTypeString(types[phoneItem
							.getPositionBasedOnType(type)]);
				}
				String content = cursor.getString(cursor
						.getColumnIndex(Phone.NUMBER));
				phoneItem.setContent(content);
				MyLog.i("content:" + content);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的Email信息
	 */
	private void prepareEmails(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Email.CONTENT_ITEM_TYPE)) {
				MyLog.i("添加一个Email");
				mEmailsContainer
						.addItem(R.array.emailTypes, R.string.emailHint);
				// 获取所有同类型item的容器
				LinearLayout itemsContainer = mEmailsContainer
						.getmItemsContainer();
				int index = itemsContainer.getChildCount() - 1;
				MyLog.i("index:" + index);
				AbstractItem emailItem = (AbstractItem) itemsContainer
						.getChildAt(index);
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				emailItem.setDataId(id);
				// email的Type
				int type = cursor.getInt(cursor.getColumnIndex(Email.TYPE));
				// 初始化button时把类型放到ContentValues对象中，以防用户不改变类型就保存
				emailItem.getChanges().put(Email.TYPE, type);
				MyLog.i("type" + type);
				if (type == Email.TYPE_CUSTOM) {
					// 自定义类型,取得自定义type的lable
					String lable = cursor.getString(cursor
							.getColumnIndex(Email.LABEL));
					emailItem.setTypeString(lable);
					MyLog.d("自定义" + lable);
				} else {
					// 不是自定义
					String[] types = getResources().getStringArray(
							R.array.emailTypes);
					emailItem.setTypeString(types[emailItem
							.getPositionBasedOnType(type)]);
				}
				String content = cursor.getString(cursor
						.getColumnIndex(Email.DATA));
				emailItem.setContent(content);
				MyLog.i("content:" + content);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的Postal信息
	 */
	private void preparePostals(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {
				MyLog.i("添加一个Postal address");
				mPostalsContainer.addItem(R.array.postalTypes,
						R.array.postalHints);
				// 获取包裹所有Organization的外层LinearLayout
				LinearLayout itemsContainer = mPostalsContainer
						.getmItemsContainer();
				int index = itemsContainer.getChildCount() - 1;
				MyLog.i("index:" + index);
				PostalItem postalItem = (PostalItem) itemsContainer
						.getChildAt(index);
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				postalItem.setDataId(id);
				// Postal address的type
				int type = cursor.getInt(cursor
						.getColumnIndex(StructuredPostal.TYPE));
				// 初始化button时把类型放到ContentValues对象中，以防用户不改变类型就保存
				postalItem.getChanges().put(StructuredPostal.TYPE, type);
				MyLog.i("type" + type);
				if (type == StructuredPostal.TYPE_CUSTOM) {
					// 自定义类型,取得自定义type的lable
					String lable = cursor.getString(cursor
							.getColumnIndex(StructuredPostal.LABEL));
					postalItem.setTypeString(lable);
					MyLog.d("自定义" + lable);
				} else {
					// 不是自定义
					String[] types = getResources().getStringArray(
							R.array.postalTypes);
					postalItem.setTypeString(types[postalItem
							.getPositionBasedOnType(type)]);
				}

				String street = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.STREET));
				postalItem.setStreet(street);
				MyLog.d("		Street:" + street);
				String pobox = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.POBOX));
				postalItem.setPobox(pobox);
				MyLog.d("		PO Box:" + pobox);
				String neighborhood = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
				postalItem.setNeighborhood(neighborhood);
				MyLog.d("		Neighborhood:" + neighborhood);
				String city = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.CITY));
				postalItem.setCity(city);
				MyLog.d("		City:" + city);
				String state = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.REGION));
				postalItem.setState(state);
				MyLog.d("		State:" + state);
				String zipCode = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.POSTCODE));
				postalItem.setZipcode(zipCode);
				MyLog.d("		ZIP Code:" + zipCode);
				String country = cursor.getString(cursor
						.getColumnIndex(StructuredPostal.COUNTRY));
				postalItem.setCountry(country);
				MyLog.d("		Country:" + country);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的Organization信息
	 */
	private void prepareOrganziations(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Organization.CONTENT_ITEM_TYPE)) {
				MyLog.i("添加一个Organization");
				mOrganizationsContainer.addItem(R.array.organizationTypes,
						R.array.organizationHints);
				// 获取包裹所有Organization的外层LinearLayout
				LinearLayout itemsContainer = mOrganizationsContainer
						.getmItemsContainer();
				int index = itemsContainer.getChildCount() - 1;
				MyLog.i("index:" + index);
				OrganizationItem organizationItem = (OrganizationItem) itemsContainer
						.getChildAt(index);
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				organizationItem.setDataId(id);
				// organization的Type
				int type = cursor.getInt(cursor
						.getColumnIndex(Organization.TYPE));
				// 初始化button时把类型放到ContentValues对象中，以防用户不改变类型就保存
				organizationItem.getChanges().put(StructuredPostal.TYPE, type);
				MyLog.i("type" + type);
				if (type == Organization.TYPE_CUSTOM) {
					// 自定义类型,取得自定义type的lable
					String lable = cursor.getString(cursor
							.getColumnIndex(Organization.LABEL));
					organizationItem.setTypeString(lable);
					MyLog.d("自定义" + lable);
				} else {
					// 不是自定义
					String[] types = getResources().getStringArray(
							R.array.organizationTypes);
					organizationItem.setTypeString(types[organizationItem
							.getPositionBasedOnType(type)]);
				}
				String company = cursor.getString(cursor
						.getColumnIndex(Organization.COMPANY));
				organizationItem.setCompany(company);
				MyLog.i("company:" + company);
				String title = cursor.getString(cursor
						.getColumnIndex(Organization.TITLE));
				organizationItem.setTitle(title);
				MyLog.i("title:" + title);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的Im信息
	 */
	private void prepareIms(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Im.CONTENT_ITEM_TYPE)) {
				MyLog.i("添加一个Im");
				mImsContainer.addItem(R.array.imTypes, R.string.imHint);
				// 获取所有同类型item的容器
				LinearLayout itemsContainer = mImsContainer
						.getmItemsContainer();
				int index = itemsContainer.getChildCount() - 1;
				MyLog.i("index:" + index);
				AbstractItem imItem = (AbstractItem) itemsContainer
						.getChildAt(index);
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				imItem.setDataId(id);
				// im的Type,是根据protocol来区分的
				int type = cursor.getInt(cursor.getColumnIndex(Im.PROTOCOL));
				// 初始化button时把类型放到ContentValues对象中，以防用户不改变类型就保存
				imItem.getChanges().put(Im.PROTOCOL, type);
				MyLog.i("type" + type);
				if (type == Im.PROTOCOL_CUSTOM) {
					// 自定义类型,取得自定义type的lable
					String lable = cursor.getString(cursor
							.getColumnIndex(Im.CUSTOM_PROTOCOL));
					MyLog.d("自定义" + lable);
					imItem.setTypeString(lable);
				} else {
					// 不是自定义
					String[] types = getResources().getStringArray(
							R.array.imTypes);
					imItem.setTypeString(types[imItem
							.getPositionBasedOnType(type)]);
				}
				String content = cursor.getString(cursor
						.getColumnIndex(Im.DATA));
				imItem.setContent(content);
				MyLog.i("content:" + content);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的备注信息
	 */
	private void prepareNote(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Note.CONTENT_ITEM_TYPE)) {
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				dataIdOfNote = id;
				// 添加Notes
				String strNote = cursor.getString(cursor
						.getColumnIndex(Note.NOTE));
				if (!TextUtils.isEmpty(strNote)) {
					etNotes.setText(strNote);
					break;
				}
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的昵称信息
	 */
	private void prepareNickname(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Nickname.CONTENT_ITEM_TYPE)) {
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				dataIdOfNickname = id;
				// 添加Nickname
				String strNickname = cursor.getString(cursor
						.getColumnIndex(Nickname.NAME));
				if (!TextUtils.isEmpty(strNickname)) {
					etNickname.setText(strNickname);
					break;
				}
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 编辑联系人时,初始化联系人的Website信息
	 */
	private void prepareWebsites(Cursor cursor) {
		int count = cursor.getCount();
		String itemMimeType;
		cursor.moveToFirst();
		for (int i = 0; i < count; i++) {
			itemMimeType = cursor.getString(cursor
					.getColumnIndex(RawContactsEntity.MIMETYPE));
			if (itemMimeType.equals(Website.CONTENT_ITEM_TYPE)) {
				MyLog.i("添加一个Website");
				mWebsitesContainer.addItem(0, R.string.websiteHint);
				// 获取所有同类型item的容器
				LinearLayout itemsContainer = mWebsitesContainer
						.getmItemsContainer();
				int index = itemsContainer.getChildCount() - 1;
				MyLog.i("index:" + index);
				WebsiteItem websiteItem = (WebsiteItem) itemsContainer
						.getChildAt(index);
				// 先保存在data表中_id的值
				long id = cursor.getLong(cursor
						.getColumnIndex(RawContactsEntity.DATA_ID));
				websiteItem.setDataId(id);
				// Website不区分type
				String content = cursor.getString(cursor
						.getColumnIndex(Website.URL));
				MyLog.i("content:" + content);
				websiteItem.setContent(content);
			}
			cursor.moveToNext();
		}
	}

	/**
	 * 查询RawContacts中_id等于rawContactId的记录的contact_id字段的值
	 */
	public long queryForContactId(ContentResolver cr, long rawContactId) {
		Cursor contactIdCursor = null;
		long contactId = -1;
		try {
			contactIdCursor = cr.query(RawContacts.CONTENT_URI,
					new String[] { RawContacts.CONTACT_ID }, RawContacts._ID
							+ "=" + rawContactId, null, null);
			if (contactIdCursor != null && contactIdCursor.moveToFirst()) {
				contactId = contactIdCursor.getLong(0);
			}
		} finally {
			if (contactIdCursor != null) {
				contactIdCursor.close();
			}
		}
		return contactId;
	}

	/**
	 * Creates a dialog offering two options: take a photo or pick a photo from
	 * the gallery.
	 */
	private void createPickPhotoDialog() {
		Context context = EditContactActivity.this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);

		String[] choices;
		choices = new String[2];
		choices[0] = getString(R.string.take_photo);
		choices[1] = getString(R.string.pick_photo);
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle(R.string.attachToContact);
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 更换头像前，先保存一下原来的头像
						mImageViewPhoto.setDrawingCacheEnabled(true);
						mPhotoBitmap = Bitmap.createBitmap(mImageViewPhoto
								.getDrawingCache());
						mImageViewPhoto.setDrawingCacheEnabled(false);

						switch (which) {
						case 0:
							doTakePhoto();
							break;
						case 1:
							doPickPhotoFromGallery();
							break;
						}
					}
				});
		builder.create().show();
	}

	/**
	 * Launches Gallery to pick a photo.
	 */
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.photoPickerNotFoundText,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Constructs an intent for picking a photo from Gallery, cropping it and
	 * returning the bitmap.
	 */
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", ICON_SIZE);
		intent.putExtra("outputY", ICON_SIZE);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * Launches Camera to take a picture and store it in a file.
	 */
	protected void doTakePhoto() {
		try {
			// Launch camera to take photo for selected contact
			PHOTO_DIR.mkdirs();
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.photoPickerNotFoundText,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Create a file name for the icon photo using current time.
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * Constructs an intent for capturing a photo and storing it in a temporary
	 * file.
	 */
	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageViewPhoto:
			// 点击设置联系人头像
			// 创建联系人时与修改联系人时弹出的Dialog不同
			createPickPhotoDialog();
			break;
		case R.id.btnCancel:
			this.finish();
			break;
		case R.id.btnOk:
			MyLog.w("Ok pressed");
			// 保存数据
			/*
			 * 最好新开线程
			 */
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			if (isInsert) {
				ops.add(ContentProviderOperation
						.newInsert(RawContacts.CONTENT_URI)
						.withValue(RawContacts.ACCOUNT_TYPE, null)
						.withValue(RawContacts.ACCOUNT_NAME, null).build());
			}
			// Name
			String strFirstName = etFirstName.getText().toString();
			String strLastName = etLastName.getText().toString();
			if (TextUtils.isEmpty(strFirstName)
					&& TextUtils.isEmpty(strLastName)) {
				// 没有输入姓名
			} else {
				if (isInsert) {
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, 0);
					builder.withValue(Data.MIMETYPE,
							StructuredName.CONTENT_ITEM_TYPE);
					builder.withValues(nameContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
				} else {
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newUpdate(Data.CONTENT_URI);
					builder.withSelection(Data._ID + "=?",
							new String[] { String.valueOf(dataIdOfName) });
					builder.withValues(nameContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
				}
			}
			// Photo
			// 比较ImageView的图像是否发生变化，如果发生了变化，则保存后者
			// 简单一点，如果不是默认的图像，我们就保存
			mImageViewPhoto.setDrawingCacheEnabled(true);
			final Bitmap newBitmap = Bitmap.createBitmap(mImageViewPhoto
					.getDrawingCache());
			mImageViewPhoto.setDrawingCacheEnabled(false);
			if (hasChangedPhoto) {// 用户执行了更换联系人头像的操作
				MyLog.d("改变头像...");
				// 简单一点,只要用户执行了更换头像的动作,我们就认为头像要被插入到数据库
				if (hasPhoto) {// 编辑联系人,有头像
					if (!compare2Bitmaps(newBitmap, mPhotoBitmap)) {
						MyLog.w("	不同于原有的头像");
						// 如果不相同,我们就保存
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
						photoContentValues.put(Photo.PHOTO, out.toByteArray());
					}
				} else {// 没有头像,我们直接保存新的头像
					MyLog.d("	新建头像");
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					photoContentValues.put(Photo.PHOTO, out.toByteArray());
				}
			}
			MyLog.d("Photo: " + photoContentValues.toString());
			if (hasChangedPhoto) {
				if (isInsert) {// 新建联系人
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, 0);
					builder.withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
					//
					builder.withValue(Photo.IS_SUPER_PRIMARY, 1);
					builder.withValues(photoContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
				} else {// 编辑联系人
					if (dataIdOfPhoto > 0) {// 编辑联系人时有头像,直接更新
						ContentProviderOperation.Builder builder = ContentProviderOperation
								.newUpdate(Data.CONTENT_URI);
						builder.withSelection(Data._ID + "=?",
								new String[] { String.valueOf(dataIdOfPhoto) });
						MyLog.d("dataIdOfPhoto:" + dataIdOfPhoto);
						builder.withValues(photoContentValues);
						builder.withYieldAllowed(true);
						ops.add(builder.build());
					} else {// 编辑联系人时,没有头像.因此需要使用插入操作
						MyLog.w("编辑联系人(创建时没有头像)时,用户选择了头像");
						ContentProviderOperation.Builder builder = ContentProviderOperation
								.newInsert(Data.CONTENT_URI);
						builder.withValue(Data.RAW_CONTACT_ID, rawContactId);
						builder.withValue(Data.MIMETYPE,
								Photo.CONTENT_ITEM_TYPE);
						//
						builder.withValue(Photo.IS_SUPER_PRIMARY, 1);
						builder.withValues(photoContentValues);
						builder.withYieldAllowed(true);
						ops.add(builder.build());
					}
				}
			}
			// Phone
			ArrayList<ContentProviderOperation> tmpPhones = mPhonesContainer
					.getItemsContentProviderOperation();
			if (tmpPhones != null) {
				for (ContentProviderOperation tmpContentProviderOperation : tmpPhones) {
					ops.add(tmpContentProviderOperation);
				}
			}
			// Email
			ArrayList<ContentProviderOperation> tmpEmails = mEmailsContainer
					.getItemsContentProviderOperation();
			if (tmpEmails != null) {
				for (ContentProviderOperation tmpContentProviderOperation : tmpEmails) {
					ops.add(tmpContentProviderOperation);
				}
			}
			// Postal
			ArrayList<ContentProviderOperation> tmpPostals = mPostalsContainer
					.getItemsContentProviderOperation();
			if (tmpPostals != null) {
				for (ContentProviderOperation tmpContentProviderOperation : tmpPostals) {
					ops.add(tmpContentProviderOperation);
				}
			}
			// Organization
			ArrayList<ContentProviderOperation> tmpOrganizations = mOrganizationsContainer
					.getItemsContentProviderOperation();
			if (tmpOrganizations != null) {
				for (ContentProviderOperation tmpContentProviderOperation : tmpOrganizations) {
					ops.add(tmpContentProviderOperation);
				}
			}
			// Im
			ArrayList<ContentProviderOperation> tmpIms = mImsContainer
					.getItemsContentProviderOperation();
			if (tmpIms != null) {
				for (ContentProviderOperation tmpContentProviderOperation : tmpIms) {
					ops.add(tmpContentProviderOperation);
				}
			}
			// Website
			ArrayList<ContentProviderOperation> tmpWebsites = mWebsitesContainer
					.getItemsContentProviderOperation();
			if (tmpWebsites != null) {
				for (ContentProviderOperation tmpContentProviderOperation : tmpWebsites) {
					ops.add(tmpContentProviderOperation);
				}
			}
			// NickName
			String strNickname = etNickname.getText().toString();
			if (!TextUtils.isEmpty(strNickname)) {
				// 不为空，可以保存
				// 使用TYPE_DEFAULT
				if (isInsert) {
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, 0);
					builder.withValue(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE);
					builder.withValue(Nickname.TYPE, Nickname.TYPE_DEFAULT);
					builder.withValues(nickNameContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
					MyLog.d("insert nickname");
				} else {
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newUpdate(Data.CONTENT_URI);
					builder.withSelection(Data._ID + "=?",
							new String[] { String.valueOf(dataIdOfNickname) });
					builder.withValues(nickNameContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
					MyLog.d("update nickname");
				}
			}
			// Notes
			String strNotes = etNotes.getText().toString();
			if (!TextUtils.isEmpty(strNotes)) {
				// 不为空，可以保存
				if (isInsert) {
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newInsert(Data.CONTENT_URI);
					builder.withValueBackReference(Data.RAW_CONTACT_ID, 0);
					builder.withValue(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
					builder.withValues(noteContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
					MyLog.d("insert note");
				} else {
					ContentProviderOperation.Builder builder = ContentProviderOperation
							.newUpdate(Data.CONTENT_URI);
					builder.withSelection(Data._ID + "=?",
							new String[] { String.valueOf(dataIdOfNote) });
					builder.withValues(noteContentValues);
					builder.withYieldAllowed(true);
					ops.add(builder.build());
					MyLog.d("update note");
				}
			}

			try {
				getContentResolver()
						.applyBatch(ContactsContract.AUTHORITY, ops);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (OperationApplicationException e) {
				e.printStackTrace();
			}
			this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * Sends a newly acquired photo to Gallery for cropping
	 */
	protected void doCropPhoto(File f) {
		try {
			// Add the image to the media store
			MediaScannerConnection.scanFile(this,
					new String[] { f.getAbsolutePath() },
					new String[] { null }, null);

			// Launch gallery to crop the photo
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			MyLog.e("Cannot crop image" + e.getMessage());
			Toast.makeText(this, R.string.photoPickerNotFoundText,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Constructs an intent for image cropping.
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", ICON_SIZE);
		intent.putExtra("outputY", ICON_SIZE);
		intent.putExtra("return-data", true);
		return intent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Ignore failed requests
		if (resultCode != RESULT_OK)
			return;

		// resultCode == RESULT_OK
		hasChangedPhoto = true;

		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {
			final Bitmap photo = data.getParcelableExtra("data");
			mImageViewPhoto.setImageBitmap(photo);
			break;
		}

		case CAMERA_WITH_DATA: {
			doCropPhoto(mCurrentPhotoFile);
			break;
		}
		}
	}

	/**
	 * 比较两个Bitmap是否相同
	 * 
	 * @return true,相同；fasle，不同。
	 */
	private boolean compare2Bitmaps(Bitmap bit1, Bitmap bit2) {
		int width = mImageViewPhoto.getWidth();
		int height = mImageViewPhoto.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (bit1.getPixel(i, j) != bit2.getPixel(i, j)) {
					MyLog.d("两个图像不一样");
					return false;
				}
			}
		}
		MyLog.d("两个图像一样");
		return true;
	}

	public static long getRawContactId() {
		return rawContactId;
	}
}