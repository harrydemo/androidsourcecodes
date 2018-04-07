package com.xcontacts.ui;

import com.xcontacts.activities.R;
import com.xcontacts.ui.model.MyLinearLayout;
import com.xcontacts.utils.MyDialog;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class OrganizationItem extends MyLinearLayout {

	/**
	 * 包裹相同类型信息的LinearLayout。如：包裹Phone信息的最外层的LinearLayout
	 */
	private LinearLayout mContainer;
	/**
	 * 包裹一行的LinearLayout
	 */
	private LinearLayout mItemContainer;
	private Button btnType;
	// Organization需要使用的2个字段
	private EditText etCompany;
	private EditText etTitle;

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

	public OrganizationItem(Context context) {
		super(context);
		mItemContainer = this;
		mItemContainer.setOrientation(HORIZONTAL);
		mItemContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		btnType = new Button(context);
		btnType.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// Organization用2个EditText，我们使用LinearLayout来包裹这2个EditText
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(VERTICAL);
		linearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

		etCompany = new EditText(context);
		etCompany.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		etTitle = new EditText(context);
		etTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		linearLayout.addView(etCompany);
		linearLayout.addView(etTitle);

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
		etCompany.setHint(hints[0]);
		etTitle.setHint(hints[1]);

		this.mTypes = getResources().getStringArray(arrayRes);

		// 下面这四行使得用户添加一行数据时，数据的type会按顺序循环显示出来
		final int countOfTypes = this.mTypes.length;
		int typeMinusCustom = countOfTypes - 1;// 减去1是为了排除Custom
		int countOfChilds = container.getChildCount();
		int indexOfNextType = countOfChilds % typeMinusCustom;

		this.btnType.setText(mTypes[indexOfNextType]);
		getChanges().put(Organization.TYPE, getItemType(indexOfNextType));
		this.btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyDialog.show(OrganizationItem.this, btnType, mTypes,
						Organization.TYPE, Organization.LABEL, getChanges());
			}
		});
		// 监听EditText中内容变化
		etCompany.addTextChangedListener(new MyTextWachter(
				Organization.COMPANY, getChanges()));
		etTitle.addTextChangedListener(new MyTextWachter(Organization.TITLE,
				getChanges()));
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
		int organizationType = 0;
		switch (position) {
		case 0:
			organizationType = Organization.TYPE_WORK;
			break;
		case 1:
			organizationType = Organization.TYPE_OTHER;
			break;
		case 2:// 特殊情况
			organizationType = Organization.TYPE_CUSTOM;
			break;
		// 如果是上述情况，则用户自定义Lable(即data3字段)
		}
		return organizationType;
	}

	public int getPositionBasedOnType(int type) {
		int position = 0;
		switch (type) {
		case Organization.TYPE_WORK:
			position = 0;
			break;
		case Organization.TYPE_OTHER:
			position = 1;
			break;
		case Organization.TYPE_CUSTOM:
			position = 2;
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

	public void setTypeString(String typeString) {
		btnType.setText(typeString);
	}

	public void setCompany(String company) {
		etCompany.setText(company);
	}

	public void setTitle(String title) {
		etTitle.setText(title);
	}

}