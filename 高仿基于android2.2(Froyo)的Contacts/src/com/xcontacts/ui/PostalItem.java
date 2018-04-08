package com.xcontacts.ui;

import com.xcontacts.activities.R;
import com.xcontacts.ui.model.MyLinearLayout;
import com.xcontacts.utils.MyDialog;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PostalItem extends MyLinearLayout {

	/**
	 * 包裹相同类型信息的LinearLayout。如：包裹Phone信息的最外层的LinearLayout
	 */
	private LinearLayout mContainer;
	/**
	 * 包裹一行的LinearLayout
	 */
	private LinearLayout mItemContainer;
	private Button btnType;
	// Postal Address需要使用的7个字段
	private EditText etStreet;
	private EditText etPObox;
	private EditText etNeighborhood;
	private EditText etCity;
	private EditText etState;
	private EditText etZipCode;
	private EditText etCountry;

	private ImageButton ibDele;
	/**
	 * 存储信息的类型
	 */
	private String[] mTypes;
	/**
	 * 编辑联系人时，我们需要每条信息在Data表中_id的值，用作更新条件
	 */
	private long dataId;
	/**
	 * 编辑联系人时，当某些信息被更新时，我们把改变后的信息存放在changes对象中
	 */
	private ContentValues changes = null;

	public PostalItem(Context context) {
		super(context);
		mItemContainer = this;
		mItemContainer.setOrientation(HORIZONTAL);
		mItemContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		btnType = new Button(context);
		btnType.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// Postal Address用7个EditText，我们使用LinearLayout来包裹这7个EditText
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(VERTICAL);
		linearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

		etStreet = new EditText(context);
		etStreet.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		etPObox = new EditText(context);
		etPObox.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		etNeighborhood = new EditText(context);
		etNeighborhood.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		etCity = new EditText(context);
		etCity.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		etState = new EditText(context);
		etState.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		etZipCode = new EditText(context);
		etZipCode.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		etCountry = new EditText(context);
		etCountry.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		linearLayout.addView(etStreet);
		linearLayout.addView(etPObox);
		linearLayout.addView(etNeighborhood);
		linearLayout.addView(etCity);
		linearLayout.addView(etState);
		linearLayout.addView(etZipCode);
		linearLayout.addView(etCountry);

		ibDele = new ImageButton(context);
		ibDele.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		ibDele.setBackgroundResource(R.drawable.ic_btn_round_minus);

		mItemContainer.addView(btnType);
		mItemContainer.addView(linearLayout);
		mItemContainer.addView(ibDele);
	}

	public void initViews(LinearLayout container, int arrayRes, int hintArrayRes) {
		this.mContainer = container;
		String[] hints = getResources().getStringArray(hintArrayRes);
		etStreet.setHint(hints[0]);
		etPObox.setHint(hints[1]);
		etNeighborhood.setHint(hints[2]);
		etCity.setHint(hints[3]);
		etState.setHint(hints[4]);
		etZipCode.setHint(hints[5]);
		etCountry.setHint(hints[6]);

		this.mTypes = getResources().getStringArray(arrayRes);

		// 下面这四行使得用户添加一行数据时，数据的type会按顺序循环显示出来
		final int countOfTypes = this.mTypes.length;
		int typeMinusCustom = countOfTypes - 1;// 减去1是为了排除Custom
		int countOfChilds = container.getChildCount();
		int indexOfNextType = countOfChilds % typeMinusCustom;

		this.btnType.setText(mTypes[indexOfNextType]);
		getChanges().put(StructuredPostal.TYPE, getItemType(indexOfNextType));

		this.btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyDialog.show(PostalItem.this, btnType, mTypes,
						StructuredPostal.TYPE, StructuredPostal.LABEL,
						getChanges());
			}
		});
		// 监听EditText中内容变化
		this.etStreet.addTextChangedListener(new MyTextWachter(
				StructuredPostal.STREET, changes));
		this.etPObox.addTextChangedListener(new MyTextWachter(
				StructuredPostal.POBOX, changes));
		this.etNeighborhood.addTextChangedListener(new MyTextWachter(
				StructuredPostal.NEIGHBORHOOD, changes));
		this.etCity.addTextChangedListener(new MyTextWachter(
				StructuredPostal.CITY, changes));
		this.etState.addTextChangedListener(new MyTextWachter(
				StructuredPostal.REGION, changes));
		this.etZipCode.addTextChangedListener(new MyTextWachter(
				StructuredPostal.POSTCODE, changes));
		this.etCountry.addTextChangedListener(new MyTextWachter(
				StructuredPostal.COUNTRY, changes));

		// 点击，删除这一行
		this.ibDele.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mContainer.removeView(mItemContainer);
				// 如果是编辑联系人时删除原来的信息，则应当在数据库中删除相关记录
				long dataId = getDataId();
				if (dataId > 0) {
					getContext().getContentResolver().delete(Data.CONTENT_URI,
							Data._ID + "=?",
							new String[] { String.valueOf(dataId) });
					MyLog.w("删除联系人的原有信息！删除条件是：Data._id = " + dataId);
				}
			}
		});
	}

	public int getItemType(int position) {
		int postalType = 0;
		switch (position) {
		case 0:
			postalType = StructuredPostal.TYPE_HOME;
			break;
		case 1:
			postalType = StructuredPostal.TYPE_WORK;
			break;
		case 2:
			postalType = StructuredPostal.TYPE_OTHER;
			break;
		case 3:// 特殊情况
			postalType = StructuredPostal.TYPE_CUSTOM;
			break;
		// 如果是上述情况，则用户自定义Lable(即data3字段)
		}
		return postalType;
	}

	public int getPositionBasedOnType(int type) {
		int position = 0;
		switch (type) {
		case StructuredPostal.TYPE_HOME:
			position = 0;
			break;
		case StructuredPostal.TYPE_WORK:
			position = 1;
			break;
		case StructuredPostal.TYPE_OTHER:
			position = 2;
			break;
		case StructuredPostal.TYPE_CUSTOM:
			position = 3;
			break;
		}
		return position;
	}

	/**
	 * @return the dataId
	 */
	public long getDataId() {
		return dataId;
	}

	/**
	 * @param dataId
	 *            the dataId to set
	 */
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public ContentValues getChanges() {
		if (changes == null) {
			changes = new ContentValues();
		}
		return changes;
	}

	public void setStreet(String street) {
		etStreet.setText(street);
	}

	public void setPobox(String pobox) {
		etPObox.setText(pobox);
	}

	public void setNeighborhood(String neighborhood) {
		etNeighborhood.setText(neighborhood);
	}

	public void setCity(String city) {
		etCity.setText(city);
	}

	public void setState(String state) {
		etState.setText(state);
	}

	public void setZipcode(String zipcode) {
		etZipCode.setText(zipcode);
	}

	public void setCountry(String country) {
		etCountry.setText(country);
	}

	public void setTypeString(String typeString) {
		btnType.setText(typeString);
	}
}